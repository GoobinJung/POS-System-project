package posPd;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaxCategory implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


	private Collection<Item> items;
	private TreeSet<TaxRate> taxRates;
	private String category;



	public TaxCategory(String name, String date, String rate) {
	    this(name);


	    LocalDate effectiveDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/yyyy"));
	    this.addTaxRate(new TaxRate(effectiveDate, new BigDecimal(rate)));
	}

	public void setTaxCategory(String category) {
	    this.category = category;
	}


	public TaxCategory() {
	    items = new ArrayList<Item>();
	    taxRates = new TreeSet<TaxRate>();
	}

	public TaxCategory(String category) {
	    this();
	    this.category = category;
	}

	public BigDecimal getTaxRateforDate(LocalDate date) {
	    for (TaxRate tr : taxRates.descendingSet()) {
	        if (tr.isEffective(date)) return tr.getTaxRate();
	    }
	    return BigDecimal.ZERO;
	}

	public void addTaxRate(TaxRate taxRate) {
	    ((TreeSet<TaxRate>)taxRates).add(taxRate);
	}

	public String toString() {
	    return category;
	}

	public void removeTaxRate(TaxRate taxRate) {
	    taxRates.remove(taxRate);
	}

	public String getCategory() {
	    return category;
	}

	public TreeSet<TaxRate> getTaxRates() {
	    return this.taxRates;
	}


}
