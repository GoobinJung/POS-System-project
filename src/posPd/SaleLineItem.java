package posPd;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SaleLineItem implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Sale sale;
    private Item item;
    private int quantity;
    private String itemNumber;
    private String description;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;

    public SaleLineItem() {}
    public SaleLineItem(Sale sale, Item item, String quantity) {
        if (sale == null || item == null) throw new IllegalArgumentException("Select a valid item.");
        sale.requireEditable();
        try { this.quantity = Integer.parseInt(quantity.trim()); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Quantity must be a whole number."); }
        if (this.quantity <= 0 || this.quantity > 100000)
            throw new IllegalArgumentException("Quantity must be between 1 and 100000.");
        this.item = item;
        this.sale = sale;
        itemNumber = item.getNumber();
        description = item.getDescription();
        LocalDate date = sale.getDateTime().toLocalDate();
        unitPrice = item.getPriceForDate(date).getPrice();
        if (item.getTaxCategory() == null) throw new IllegalArgumentException("Item needs a tax category.");
        taxRate = item.getTaxCategory().getTaxRateforDate(date);
        sale.addSaleLineItem(this);
    }
    public SaleLineItem(Store store, Sale sale, String number, String quantity) {
        this(sale, store.findItemForNumber(number), quantity);
    }
    public int getQuantity() { return quantity; }
    public Item getItem() { return item; }
    public String getItemNumber() { return itemNumber; }
    public String getDescription() { return description; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    void setSale(Sale sale) { this.sale = sale; }
    public BigDecimal calcSubTotal() { return Money.round(unitPrice.multiply(BigDecimal.valueOf(quantity))); }
    public BigDecimal calcTax() { return Money.round(calcSubTotal().multiply(taxRate)); }
    @Override public String toString() { return itemNumber + " " + description + " x" + quantity + " = " + calcSubTotal(); }
}

