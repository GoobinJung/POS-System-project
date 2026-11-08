package posTest;

import posDm.*;
import posPd.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.*;
import java.util.*;

/** Dependency-free regression checks. Run from the repository root. */
public final class RegressionTest {
    private static int checks;
    private static void check(boolean result, String message) {
        checks++;
        if (!result) throw new AssertionError(message);
    }
    private static void money(String expected, BigDecimal actual, String message) {
        check(new BigDecimal(expected).compareTo(actual) == 0, message + ": " + actual);
    }
    private static void rejects(Runnable action, String message) {
        boolean rejected = false;
        try { action.run(); } catch (IllegalArgumentException | IllegalStateException ex) { rejected = true; }
        check(rejected, message);
    }
    private static final class Fixture {
        final Store store = new Store("test", "Regression store");
        final TaxCategory tax = new TaxCategory("Food", "1/1/2020", "0.07");
        final Cashier cashier = new Cashier("1", new Person("Demo", "", "", "", "", "", ""), "test-password");
        final Register register;
        final Item item;
        final Session session;
        Fixture() {
            store.addTaxCategory(tax);
            store.addCashier(cashier);
            register = new Register(store, "1");
            item = new Item(store, "1", "Sample", "Food");
            store.addUPC(new UPC("12345678", item));
            new Price(item, "10.00", "2020-01-01");
            session = new Session(store, cashier, register);
            session.setStartCash(new BigDecimal("100"));
            store.addSession(session);
        }
        Sale sale(String quantity) {
            Sale sale = new Sale();
            new SaleLineItem(sale, item, quantity);
            return sale;
        }
    }

    private static void checkout() {
        Fixture f = new Fixture();
        Sale sale = f.sale("2");
        money("20", sale.calcSubTotal(), "Quantity subtotal");
        money("1.40", sale.calcTax(), "Tax rounding");
        money("21.40", sale.calcTotal(), "Total");
        check(f.session.getNumSales() == 0, "Draft is not a completed sale");
        check(!sale.isPaymentEnough(), "Unpaid sale");
        rejects(() -> f.session.addSale(sale), "Cannot record an unpaid sale");
        Cash exact = new Cash("0", new BigDecimal("21.40"));
        sale.addPayment(exact);
        check(sale.isPaymentEnough(), "Exact payment accepted");
        rejects(() -> sale.setTaxFree(true), "Cannot alter paid sale");
        f.session.addSale(sale);
        f.session.addSale(sale);
        rejects(() -> exact.setAmount(BigDecimal.ZERO), "Completed payment amount is frozen");
        check(f.session.getNumSales() == 1, "Completion is idempotent");
        money("121.40", f.session.calcExpectedCash(), "Expected drawer includes cash sales");
        rejects(() -> new SaleLineItem(sale, f.item, "1"), "Completed sale is immutable");
        f.session.close(new BigDecimal("120.40"));
        money("-1", f.session.calcCashCountDiff(), "Cash shortage");
        rejects(() -> f.session.addSale(f.sale("1")), "Closed session rejects sales");
        money("21.40", (BigDecimal) Reports.dailySales(f.store, LocalDate.now()).get(0)[5], "Daily report");
        check(Reports.cashierSales(f.store, LocalDate.now()).size() == 1, "Cashier report");
        check((Integer) Reports.itemSales(f.store, LocalDate.now()).get(0)[2] == 2, "Item report quantity");
        check(Reports.dailySales(f.store, LocalDate.now().minusDays(1)).isEmpty(), "Report date filter");
    }

    private static void payments() {
        Fixture f = new Fixture();
        Sale sale = f.sale("1");
        Credit credit = new Credit();
        credit.setAmtTendered(new BigDecimal("5"));
        credit.setCardType("Visa");
        credit.setAcctNumber("1234");
        credit.setExpireDate(LocalDate.now().plusYears(1));
        sale.addPayment(credit);
        money("5.70", sale.calcAmountForPayment(), "Partial card payment");
        Cash cash = new Cash("0", new BigDecimal("10"));
        sale.addPayment(cash);
        money("10.70", sale.getTotalPayments(), "Applied payments are capped at balance");
        money("4.30", sale.calcChange(), "Change is cash overpayment");
        money("5.70", sale.calcCashReceived(), "Only applied cash affects drawer");
        f.session.addSale(sale);
        money("105.70", f.session.calcExpectedCash(), "Mixed payment drawer total");

        Sale checkSale = f.sale("1");
        Check checkPayment = new Check();
        checkPayment.setAmtTendered(new BigDecimal("10.70"));
        checkPayment.setRoutingNumber("123456789");
        checkPayment.setAccountNumber("12345678");
        checkPayment.setCheckNumber("1001");
        checkSale.addPayment(checkPayment);
        check(checkSale.isPaymentEnough(), "Check exact payment");
        money("0", checkSale.calcCashReceived(), "Check does not count as drawer cash");

        Sale invalid = f.sale("1");
        Credit bad = new Credit("11", "Visa", "1234", LocalDate.now().plusYears(1).toString());
        rejects(() -> invalid.addPayment(bad), "Card overpayment rejected");
        bad.setAmtTendered(new BigDecimal("5"));
        bad.setExpireDate(LocalDate.now().minusDays(1));
        rejects(() -> invalid.addPayment(bad), "Expired demo card rejected");
        rejects(() -> invalid.addPayment(new Cash("0", BigDecimal.ZERO)), "Zero payment rejected");
        rejects(() -> new Cash("-1", BigDecimal.ONE), "Negative amount rejected");
        rejects(() -> Money.parse("1.001"), "Fractional cents rejected");
        rejects(() -> new SaleLineItem(invalid, f.item, "0"), "Zero quantity rejected");
        rejects(() -> new SaleLineItem(invalid, f.item, "abc"), "Invalid quantity rejected");
        Sale free = f.sale("1");
        free.setTaxFree(true);
        money("10", free.calcTotal(), "Tax exemption");
        free.addPayment(new Cash("0", new BigDecimal("10")));
        free.removePayment(free.getPayments().get(0));
        check(!free.isPaymentEnough(), "Removing payment restores balance");
        rejects(() -> new Sale().addPayment(new Cash("0", BigDecimal.ONE)), "Empty checkout rejects payment");
    }

    private static void pricing() {
        Fixture f = new Fixture();
        new Price(f.item, "12", "2024-01-01");
        new Price(f.item, "99", "2099-01-01");
        PromoPrice promo = new PromoPrice("8", "2025-09-01", "2025-09-30");
        f.item.addPrice(promo);
        money("10", f.item.getPriceForDate(LocalDate.of(2023, 1, 1)).getPrice(), "Historical regular price");
        money("12", f.item.getPriceForDate(LocalDate.of(2025, 8, 31)).getPrice(), "Latest effective regular price");
        money("8", f.item.getPriceForDate(LocalDate.of(2025, 9, 1)).getPrice(), "Promotion start inclusive");
        money("8", f.item.getPriceForDate(LocalDate.of(2025, 9, 30)).getPrice(), "Promotion end inclusive");
        money("12", f.item.getPriceForDate(LocalDate.of(2025, 10, 1)).getPrice(), "Expired promotion excluded");
        f.tax.addTaxRate(new TaxRate(LocalDate.of(2024, 1, 1), new BigDecimal("0.09")));
        f.tax.addTaxRate(new TaxRate(LocalDate.of(2099, 1, 1), new BigDecimal("0.99")));
        money("0.09", f.tax.getTaxRateforDate(LocalDate.of(2025, 1, 1)), "Latest tax rate");
        money("0", f.tax.getTaxRateforDate(LocalDate.of(2010, 1, 1)), "No effective tax rate");
        rejects(() -> new PromoPrice("1", "2025-10-01", "2025-09-01"), "Invalid promo date range");
        rejects(() -> new TaxRate(LocalDate.now(), new BigDecimal("2")), "Invalid rate");
        Sale sale = f.sale("1");
        BigDecimal previous = sale.calcTotal();
        Price active = f.item.getPriceForDate(LocalDate.now());
        active.setPrice(new BigDecimal("50"));
        f.tax.addTaxRate(new TaxRate(LocalDate.now(), new BigDecimal("0.20")));
        check(previous.compareTo(sale.calcTotal()) == 0, "Sale prices and taxes are snapshots");
        check(!UPC.validateUPC("not-a-upc"), "UPC validation");
        Item another = new Item(f.store, "2", "Other", "Food");
        rejects(() -> f.store.addUPC(new UPC("12345678", another)), "Duplicate UPC rejected");
        rejects(() -> f.store.addCashier(new Cashier("1", new Person(), "other")), "Duplicate cashier rejected");
        rejects(() -> f.store.removeCashier(f.cashier), "Referenced cashier protected");
        rejects(() -> f.store.removeRegister(f.register), "Referenced register protected");
        rejects(() -> f.store.removeTaxCategory(f.tax), "Referenced tax category protected");
        rejects(() -> f.store.addTaxCategory(new TaxCategory("Food")), "Duplicate tax category rejected");
        CashDrawer drawer = new CashDrawer();
        drawer.addCash(new BigDecimal("10"));
        drawer.removeCash(new BigDecimal("2"));
        money("8", drawer.getCashAmount(), "Cash drawer operations");
        rejects(() -> drawer.removeCash(new BigDecimal("9")), "Cash drawer prevents overdraft");
    }

    private static void persistence() throws Exception {
        Store seed = StoreDm.importStore("StoreData.csv");
        check(seed.getCashiers().size() == 2, "Seed cashiers");
        check(seed.getSessions().size() == 2, "Seed sessions not duplicated");
        int sales = 0, lines = 0;
        for (Session session : seed.getSessions()) {
            check(session.isClosed(), "Imported sessions are closed");
            for (Sale sale : session.getSales()) {
                sales++;
                lines += sale.getSaleLineItems().size();
                check(sale.isCompleted(), "Imported sale completed");
                sale.toString();
            }
        }
        check(sales == 4 && lines == 8, "CSV line items are not duplicated");
        check(seed.findCashierForNumber("1").getPerson().getAddress().equals("1 1st Street"), "CSV person columns");
        check(seed.findCashierForNumber("1").isAuthorized("password1"), "Demo password works");
        check(!seed.findCashierForNumber("1").isAuthorized("wrong"), "Wrong password rejected");
        check(seed.findCashierForNumber("1").getPassword().isEmpty(), "Password not returned to UI");
        Files.createDirectories(Path.of("out"));
        Path directory = Files.createTempDirectory(Path.of("out"), "pos-regression-");
        Path state = directory.resolve("StoreState.ser");
        StoreDm.save(seed, state);
        Store copy = StoreDm.load(state);
        check(copy.getSessions().size() == 2, "Saved sessions survive reload");
        check(copy.findCashierForNumber("1").isAuthorized("password1"), "Hash survives reload");
        check(!new String(Files.readAllBytes(state), StandardCharsets.ISO_8859_1).contains("password1"),
            "Snapshot does not contain plaintext password");
        check(copy.getSessions().get(0).getCashier() == copy.findCashierForNumber("2"), "Graph identity preserved");
        seed.setName("Updated store");
        StoreDm.save(seed, state);
        check(StoreDm.load(state).getName().equals("Updated store"), "Updated data persisted");
        check(!StoreDm.load(Path.of(state + ".bak")).getName().equals("Updated store"), "Previous snapshot backed up");
        byte[] corrupt = {1, 2, 3};
        Path broken = directory.resolve("broken.ser");
        Files.write(broken, corrupt);
        boolean failed = false;
        try { StoreDm.load(broken); } catch (java.io.IOException ex) { failed = true; }
        check(failed && Arrays.equals(corrupt, Files.readAllBytes(broken)), "Corrupt load does not overwrite data");
        Path csv = directory.resolve("report.csv");
        CsvExport.write(csv, new String[] {"Name", "Value"},
            Collections.singletonList(new Object[] {"A, \"quoted\" name", "12"}));
        String exported = Files.readString(csv);
        check(exported.contains("\"A, \"\"quoted\"\" name\""), "CSV escaping");
        check(StoreDm.parseCsvLine("\"A,B\",\"C\"\"D\",").equals(Arrays.asList("A,B", "C\"D", "")),
            "CSV quoting and trailing fields");
        Path invalid = directory.resolve("invalid.csv");
        Files.writeString(invalid, "Store,Demo\nUnknown,record\n");
        rejects(() -> StoreDm.importStore(invalid.toString()), "Invalid CSV fails rather than silently returning partial data");
        // Only remove files created by this test.
        try (java.util.stream.Stream<Path> paths = Files.list(directory)) {
            for (Path path : paths.toList()) Files.delete(path);
        }
        Files.delete(directory);
    }
    public static void main(String[] args) throws Exception {
        checkout();
        payments();
        pricing();
        persistence();
        System.out.println("PASS: " + checks + " regression checks.");
    }
}
