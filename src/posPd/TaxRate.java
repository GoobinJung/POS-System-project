package posPd;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxRate implements Comparable<TaxRate>, java.io.Serializable {
    private static final long serialVersionUID = 1L;


	private BigDecimal taxRate;
	private LocalDate effectiveDate;

	public TaxRate() {

	}


	public TaxRate(LocalDate effectiveDate, BigDecimal rate) {
		this.effectiveDate = effectiveDate;
		if (rate == null || rate.signum() < 0 || rate.compareTo(BigDecimal.ONE) > 0)
            throw new IllegalArgumentException("Tax rate must be between 0 and 1.");
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
        if (rate == null || rate.signum() < 0 || rate.compareTo(BigDecimal.ONE) > 0)
            throw new IllegalArgumentException("Tax rate must be between 0 and 1.");
	    this.taxRate = rate;
	}

	public LocalDate getEffectiveDate() {
	    return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
	    this.effectiveDate = effectiveDate;
	}
}
