package posPd;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxRate implements Comparable<TaxRate> { 

	private BigDecimal taxRate;
	private LocalDate effectiveDate;

	public TaxRate() {
	    
	}

	
	public TaxRate(LocalDate effectiveDate, BigDecimal rate) {
		this.effectiveDate = effectiveDate;
		this.taxRate = rate; 
	}

	
	public boolean isEffective(LocalDate date) {
	    return effectiveDate.compareTo(date) <= 0;
	}

	public int compareTo(TaxRate taxRate) {
	    return this.effectiveDate.compareTo(taxRate.effectiveDate);
	}

	public String toString() {
	    return taxRate.toPlainString() + " " + effectiveDate;
	}
	
	public BigDecimal getTaxRate() {
	    return taxRate;
	}
	
	public void setTaxRate(BigDecimal rate) {
	    this.taxRate = rate;
	}
	
	public LocalDate getEffectiveDate() {
	    return effectiveDate;
	}
	
	public void setEffectiveDate(LocalDate effectiveDate) {
	    this.effectiveDate = effectiveDate;
	}
}