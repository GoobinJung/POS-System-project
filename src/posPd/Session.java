package posPd;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Session implements Comparable<Session>, Comparator<Session>, java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Register register;
    private Cashier cashier;
    private final ArrayList<Sale> sales = new ArrayList<>();
    private LocalDateTime startDateTime = LocalDateTime.now();
    private LocalDateTime endDateTime;
    private BigDecimal startCash = Money.parse("0");
    private BigDecimal endCash = Money.parse("0");

    public Session() {}
    public Session(Store store, Cashier cashier, Register register) {
        if (cashier == null || register == null) throw new IllegalArgumentException("Select a cashier and register.");
        this.cashier = cashier;
        this.register = register;
        cashier.addSession(this);
        register.addSession(this);
    }
    public Collection<Sale> getSales() { return Collections.unmodifiableList(sales); }
    public BigDecimal getEnterCash() { return endCash; }
    public void setEnterCash(BigDecimal value) { setEndCash(value); }
    public BigDecimal getStartCash() { return startCash; }
    public void setStartCash(BigDecimal value) {
        if (isClosed() || !sales.isEmpty()) throw new IllegalStateException("Starting cash cannot be changed now.");
        startCash = Money.amount(value);
    }
    public int getNumSales() { return sales.size(); }
    public BigDecimal getEndCash() { return endCash; }
    public void setEndCash(BigDecimal value) { endCash = Money.amount(value); }
    public Register getRegister() { return register; }
    public Cashier getCashier() { return cashier; }
    public void addSale(Sale sale) {
        if (sales.contains(sale)) return;
        if (isClosed()) throw new IllegalStateException("This session is closed.");
        if (sale == null) throw new IllegalArgumentException("Sale is required.");
        sale.setSession(this);
        sale.complete();
        sales.add(sale);
    }
    public void addImportedSale(Sale sale) {
        if (!sales.contains(sale)) { sale.setSession(this); sale.markImportedComplete(); sales.add(sale); }
    }
    public void removeSale(Sale sale) {
        throw new IllegalStateException("Completed sales cannot be removed.");
    }
    public BigDecimal calcExpectedCash() {
        BigDecimal total = startCash;
        for (Sale sale : sales) total = total.add(sale.calcCashReceived());
        return Money.round(total);
    }
    public BigDecimal calcCashCountDiff() { return endCash.subtract(calcExpectedCash()); }
    public BigDecimal calcTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Sale sale : sales) total = total.add(sale.calcTotal());
        return Money.round(total);
    }
    public void close(BigDecimal counted) {
        if (isClosed()) throw new IllegalStateException("This session is already closed.");
        setEndCash(counted);
        endDateTime = LocalDateTime.now();
    }
    public boolean isClosed() { return endDateTime != null; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    @Override public int compareTo(Session other) { return startDateTime.compareTo(other.startDateTime); }
    @Override public int compare(Session first, Session second) { return first.compareTo(second); }
    @Override public String toString() {
        StringBuilder result = new StringBuilder("Session: " + cashier + " | Register: " + register
            + " | Sales: " + getNumSales() + " | Total: " + calcTotal() + "\n");
        for (Sale sale : sales) result.append("  ").append(sale).append("\n");
        return result.toString();
    }
}
