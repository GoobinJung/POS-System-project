package posPd;
import java.util.*;

public class Item implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


	private TaxCategory taxCategory;
	private TreeMap<String, UPC> upcs;
	private ArrayList<SaleLineItem> saleLineItems;
	private String number;
	private TreeSet<Price> prices;
	private String description;

	public Item() {
	    saleLineItems = new ArrayList<SaleLineItem>();
	    prices = new TreeSet<Price>();
	    upcs = new TreeMap<String, UPC>();
	}


	public Item(Store aStore, String number, String description, String taxCategoryName) {
	    this();
	    setNumber(number);
	    setDescription(description);

	    setTaxCategory(aStore.findTaxCategoryForCategory(taxCategoryName));
	    aStore.addItem(this);
	}



	public String getNumber() {
	    return this.number;
	}

	public void setNumber(String number) {
	    this.number = number;
	}

	public String getDescription() {
	    return this.description;
	}

	public void setDescription(String description) {
	    this.description = description;
	}

	public ArrayList<SaleLineItem> getSaleLineItems() {
	    return this.saleLineItems;
	}


	public TreeMap<String, UPC> getUpcs() {
	    return this.upcs;
	}

	public TreeSet<Price> getPrices() {
	    return this.prices;
	}

	public void addUPC(UPC upc) {
	    if(upc != null) {
		getUpcs().put(upc.getUpc(), upc);
	    }
	}

	public void addPrice(Price price) {
	    if(price != null) {
		getPrices().add(price);
	    }
	}

	public void setTaxCategory(TaxCategory taxCategory) {
	    this.taxCategory = taxCategory;
	}

	public TaxCategory getTaxCategory() {
	    return this.taxCategory;
	}


	public Price getPriceForDate(java.time.LocalDate date) {
        Price regular = null;
        Price promo = null;
        for (Price price : prices) {
            if (!price.isInEffectForDate(date)) continue;
            if (price instanceof PromoPrice) {
                if (promo == null || price.getPrice().compareTo(promo.getPrice()) < 0) promo = price;
            } else if (regular == null || price.getEffectiveDate().isAfter(regular.getEffectiveDate())) {
                regular = price;
            }
        }
        if (promo != null && (regular == null || promo.getPrice().compareTo(regular.getPrice()) < 0)) return promo;
        if (regular == null) throw new IllegalArgumentException("No effective price for item " + number + ".");
        return regular;
    }

    public String toString() {
	    String upcStr = "";
	    for (Map.Entry<String, UPC> entry : upcs.entrySet()) {
	        upcStr += entry.getValue().getUpc() + " ";
	    }
	    String priceStr = "";
	    for (Price p : prices) {
	        priceStr += p.toString() + " ";
	    }
	    return  number + " " + description  + " " + priceStr + " " + (taxCategory == null ? "No tax category" : taxCategory.getTaxRates());
	}


	public void addSaleLineItem(SaleLineItem saleLineItem) {
	    saleLineItems.add(saleLineItem);
	}

}
