package posPd;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Sale implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<Payment> payments = new ArrayList<>();
    private final ArrayList<SaleLineItem> saleLineItems = new ArrayList<>();
    private LocalDateTime dateTime = LocalDateTime.now();
    private boolean taxFree;
    private Session session;
    private boolean completed;

    public Sale() {}
    public Sale(Session session, String isTaxFree) {
        this.session = session;
        taxFree = "Y".equals(isTaxFree);
    }
    public LocalDateTime getDateTime() { return dateTime; }
    public boolean isTaxFree() { return taxFree; }
    public boolean isCompleted() { return completed; }
    public void requireEditable() {
        if (completed) throw new IllegalStateException("This sale has already been completed.");
    }
    private void requireUnpaid() {
        requireEditable();
        if (!payments.isEmpty()) throw new IllegalStateException("Remove payments before changing the sale.");
    }
    public void setTaxFree(Boolean value) { requireUnpaid(); taxFree = Boolean.TRUE.equals(value); }
    public void addPayment(Payment payment) {
        requireEditable();
        if (saleLineItems.isEmpty()) throw new IllegalArgumentException("Add an item first.");
        if (payment == null || payment.getAmtTendered().signum() <= 0)
            throw new IllegalArgumentException("Payment must be greater than zero.");
        if (payments.contains(payment)) return;
        if (calcAmountForPayment().signum() == 0) throw new IllegalArgumentException("The sale is already paid.");
        if (payment instanceof AuthorizedPayment && !((AuthorizedPayment) payment).isAuthorized())
            throw new IllegalArgumentException("Invalid demo payment details.");
        BigDecimal applied = payment.getAmtTendered().min(calcAmountForPayment());
        if (!payment.countsAsCash() && payment.getAmtTendered().compareTo(applied) > 0)
            throw new IllegalArgumentException("Check and credit payments cannot exceed the balance due.");
        payment.setAmount(applied);
        payments.add(payment);
    }
    /** Preserve recorded sample payments, even when historical totals differ. */
    public void addImportedPayment(Payment payment) {
        requireEditable();
        if (payment == null) throw new IllegalArgumentException("Payment is required.");
        payments.add(payment);
    }
    public void removePayment(Payment payment) { requireEditable(); payments.remove(payment); }
    public List<Payment> getPayments() { return Collections.unmodifiableList(payments); }
    public void addSaleLineItem(SaleLineItem item) {
        requireUnpaid();
        if (item == null) throw new IllegalArgumentException("Item is required.");
        if (!saleLineItems.contains(item)) { saleLineItems.add(item); item.setSale(this); }
    }
    public List<SaleLineItem> getSaleLineItems() { return Collections.unmodifiableList(saleLineItems); }
    public void removeSaleLineItem(SaleLineItem item) { requireUnpaid(); saleLineItems.remove(item); }
    public BigDecimal calcSubTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (SaleLineItem item : saleLineItems) total = total.add(item.calcSubTotal());
        return Money.round(total);
    }
    public BigDecimal calcTax() {
        BigDecimal total = BigDecimal.ZERO;
        if (!taxFree) for (SaleLineItem item : saleLineItems) total = total.add(item.calcTax());
        return Money.round(total);
    }
    public BigDecimal calcTotal() { return calcSubTotal().add(calcTax()); }
    public BigDecimal getTotalPayments() {
        BigDecimal total = BigDecimal.ZERO;
        for (Payment payment : payments) total = total.add(payment.getAmount());
        return Money.round(total);
    }
    public BigDecimal calcAmtTendered() {
        BigDecimal total = BigDecimal.ZERO;
        for (Payment payment : payments) total = total.add(payment.getAmtTendered());
        return Money.round(total);
    }
    public Boolean isPaymentEnough() {
        return !saleLineItems.isEmpty() && getTotalPayments().compareTo(calcTotal()) >= 0;
    }
    public BigDecimal calcAmountForPayment() { return calcTotal().subtract(getTotalPayments()).max(Money.parse("0")); }
    public BigDecimal calcChange() { return calcAmtTendered().subtract(getTotalPayments()).max(Money.parse("0")); }
    public BigDecimal calcCashReceived() {
        BigDecimal total = BigDecimal.ZERO;
        for (Payment payment : payments) if (payment.countsAsCash()) total = total.add(payment.getAmount());
        return Money.round(total);
    }
    public void complete() {
        if (completed) return;
        if (!isPaymentEnough()) throw new IllegalStateException("The sale has an unpaid balance.");
        for (Payment payment : payments) payment.freeze();
        completed = true;
    }
    public void markImportedComplete() {
        for (Payment payment : payments) payment.freeze();
        completed = true;
    }
    public Session getSession() { return session; }
    public void setSession(Session value) {
        if (completed && session != value) throw new IllegalStateException("Completed sale cannot move sessions.");
        session = value;
    }
    @Override public String toString() {
        return "Sale " + dateTime + " | Subtotal: " + calcSubTotal() + " | Tax: " + calcTax()
            + " | Total: " + calcTotal() + " | Payment: " + getTotalPayments() + " | Change: " + calcChange();
    }
}

