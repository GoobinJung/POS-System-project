package posPd;

import java.math.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Price implements Comparator<Price>, Comparable<Price>, java.io.Serializable {
    private static final long serialVersionUID = 1L;


	private BigDecimal price;
	private LocalDate effectiveDate;
	private Item item;

	public Price(Item item, String price, String effectiveDateString) {
	    setItem(item);
	    setPrice(new BigDecimal(price));

	    setEffectiveDate(LocalDate.parse(effectiveDateString));
	    if (item != null) {
	        item.addPrice(this);
	    }
	}

         public Price() {

	}

	public BigDecimal getPrice() {
	    return this.price;
	}


	public void setPrice(BigDecimal price) {
	    this.price = Money.amount(price);
	}

	public LocalDate getEffectiveDate() {
	    return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
	    this.effectiveDate = Objects.requireNonNull(effectiveDate, "Effective date is required.");
	}

	public Item getItem() {
	    return item;
	}

	public void setItem (Item item) {
	    this.item = item;
	}

	public boolean isInEffectForDate(LocalDate aLocalDate) {
	    return (getEffectiveDate().compareTo(aLocalDate) <= 0);
	}

	public BigDecimal calcAmtForQty(int quantity) {
	    return getPrice().multiply(new BigDecimal(quantity));
	}

	public int compareTo(Price price) {
	    int dateOrder = getEffectiveDate().compareTo(price.getEffectiveDate());
        if (dateOrder != 0) return dateOrder;
        return getClass().getName().compareTo(price.getClass().getName());
	}

	public int compare(Price p1, Price p2) {
	    return p1.compareTo(p2);
	}

	public String toString() {
	    return getPrice().toPlainString() + " " + getEffectiveDate().toString();
	}

	public BigDecimal getTaxRate() {
	    return item == null || item.getTaxCategory() == null ? BigDecimal.ZERO
            : item.getTaxCategory().getTaxRateforDate(LocalDate.now());
	}
}
