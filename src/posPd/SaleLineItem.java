package posPd;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class SaleLineItem {

    private Sale sale;
    private Item item;
    private int quantity;

    public SaleLineItem() {}

    public SaleLineItem(Sale sale, Item item, String quantity) {
	    this.sale = sale;
	    this.item = item;
	    this.quantity = Integer.parseInt(quantity);
	    sale.addSaleLineItem(this);
	}

    public SaleLineItem(Store aStore, Sale sale, String itemNumber, String quantity) {
        setQuantity(Integer.parseInt(quantity));
        setItem(aStore.findItemForNumber(itemNumber));
        getItem().addSaleLineItem(this);
        setSale(sale);
    }

    public int getQuantity() { return this.quantity; }

    private void setItem(Item item) { this.item = item; }

    private void setQuantity(int quantity) { this.quantity = quantity; }

    public Item getItem() { return this.item; }

    void setSale(Sale sale) { this.sale = sale; }

    public BigDecimal calcSubTotal() {
        return item.getPrices().first().getPrice().multiply(new BigDecimal(quantity));
    }

    public BigDecimal calcTax() {
        return calcSubTotal().multiply(item.getTaxCategory().getTaxRateforDate(LocalDate.now()));
    }

 
    public String toString() {
        Item item = getItem();
        Price currentPrice = item.getPrices().first();
        
        return item.getNumber() + " " + item.getDescription() + " " 
               + quantity + " " ;
    }
}
