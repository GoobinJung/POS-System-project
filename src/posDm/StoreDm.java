package posDm;

import posPd.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** Imports the original seed CSV and atomically saves the local object graph. */
public final class StoreDm {
    private StoreDm() {}
    public static Path statePath() { return Path.of(System.getProperty("pos.state", "StoreState.ser")); }
    public static Store loadStore() throws IOException {
        return Files.exists(statePath()) ? load(statePath()) : importStore("StoreData.csv");
    }
    public static Store load(Path path) throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(path))) {
            input.setObjectInputFilter(ObjectInputFilter.Config.createFilter(
                "maxdepth=100;maxrefs=200000;maxbytes=20971520;posPd.*;java.base/*;!*"));
            Object object = input.readObject();
            if (!(object instanceof Store)) throw new IOException("Saved file is not a POS store.");
            return (Store) object;
        } catch (ClassNotFoundException | RuntimeException ex) {
            throw new IOException("Cannot read saved store. Keep the file and restore a backup.", ex);
        }
    }
    public static void save(Store store) throws IOException { save(store, statePath()); }
    public static void save(Store store, Path target) throws IOException {
        Path absolute = target.toAbsolutePath();
        Files.createDirectories(absolute.getParent());
        Path temp = Files.createTempFile(absolute.getParent(), "pos-state-", ".tmp");
        try {
            try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(temp))) {
                output.writeObject(store);
            }
            if (Files.exists(absolute)) Files.copy(absolute, Path.of(absolute + ".bak"), StandardCopyOption.REPLACE_EXISTING);
            try { Files.move(temp, absolute, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING); }
            catch (AtomicMoveNotSupportedException ex) { Files.move(temp, absolute, StandardCopyOption.REPLACE_EXISTING); }
        } finally { Files.deleteIfExists(temp); }
    }

    public static Store importStore(String fileName) {
        Store store = new Store("001", "Default Store");
        Session session = null;
        Sale sale = null;
        int lineNumber = 0;
        DateTimeFormatter date = DateTimeFormatter.ofPattern("M/d/uuuu")
            .withResolverStyle(java.time.format.ResolverStyle.STRICT);
        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;
                List<String> fields = parseCsvLine(line);
                String[] t = fields.toArray(new String[0]);
                if (t[0].equals("Sale") || t[0].equals("Session")) {
                    if (sale != null) { session.addImportedSale(sale); sale = null; }
                }
                switch (t[0]) {
                    case "Store": store.setName(t[1]); break;
                    case "TaxCategory": {
                        TaxCategory category = store.findTaxCategoryForCategory(t[1]);
                        if (category == null) { category = new TaxCategory(t[1]); store.addTaxCategory(category); }
                        category.addTaxRate(new TaxRate(LocalDate.parse(t[3], date), new BigDecimal(t[2])));
                        break;
                    }
                    case "Cashier": {
                        Person person = new Person(t[2], t[4], t[5], t[6], t[7], t[8], t[3]);
                        store.addCashier(new Cashier(t[1], person, t[9]));
                        break;
                    }
                    case "Item": {
                        Item item = new Item(store, t[1], t[3], t[4]);
                        if (item.getTaxCategory() == null) throw new IllegalArgumentException("Unknown tax category.");
                        store.addUPC(new UPC(t[2], item));
                        new Price(item, t[5], LocalDate.parse(t[6], date).toString());
                        if (t.length > 7 && !t[7].isBlank()) {
                            PromoPrice promo = new PromoPrice(t[7], LocalDate.parse(t[8], date).toString(),
                                LocalDate.parse(t[9], date).toString());
                            promo.setItem(item);
                            item.addPrice(promo);
                        }
                        break;
                    }
                    case "Register": new Register(store, t[1]); break;
                    case "Session":
                        if (session != null) session.close(session.calcExpectedCash());
                        session = new Session(store, store.findCashierForNumber(t[2]), store.findRegisterForNumber(t[1]));
                        store.addSession(session);
                        break;
                    case "Sale":
                        if (session == null) throw new IllegalArgumentException("Sale needs a session.");
                        sale = new Sale(session, t[1]);
                        break;
                    case "SaleLineItem":
                        if (sale == null) throw new IllegalArgumentException("Line item needs a sale.");
                        new SaleLineItem(store, sale, t[1], t[2]);
                        break;
                    case "Payment": {
                        if (sale == null) throw new IllegalArgumentException("Payment needs a sale.");
                        Payment payment;
                        switch (t[1]) {
                            case "Cash": payment = new Cash(t[2], new BigDecimal(t[3])); break;
                            case "Credit": payment = new Credit(t[2], t[4], t[5], LocalDate.parse(t[6], date).toString()); break;
                            case "Check":
                                Check check = new Check(t[2], t[5]);
                                check.setRoutingNumber(t[4]);
                                check.setCheckNumber(t[6]);
                                payment = check;
                                break;
                            default: throw new IllegalArgumentException("Unknown payment type.");
                        }
                        payment.setAmtTendered(new BigDecimal(t[3]));
                        sale.addImportedPayment(payment);
                        break;
                    }
                    default: throw new IllegalArgumentException("Unknown record type: " + t[0]);
                }
            }
            if (sale != null) session.addImportedSale(sale);
            if (session != null) session.close(session.calcExpectedCash());
            return store;
        } catch (IOException | RuntimeException ex) {
            throw new IllegalArgumentException("Cannot import " + fileName + " at line " + lineNumber + ": " + ex.getMessage(), ex);
        }
    }

    /** CSV quoting, escaped quotes, and trailing empty fields (one record per line). */
    public static List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') { field.append('"'); i++; }
                else quoted = !quoted;
            } else if (c == ',' && !quoted) { fields.add(field.toString()); field.setLength(0); }
            else field.append(c);
        }
        if (quoted) throw new IllegalArgumentException("Unclosed CSV quote.");
        fields.add(field.toString());
        return fields;
    }
}
