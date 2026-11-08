package posPd;
import java.time.LocalDate;

public class PromoPrice extends Price {

	private LocalDate endDate;

	public LocalDate getEndDate() { 
		return endDate;
	}

	public PromoPrice(String price, String effectiveDate, String endDate) {
	    super(null, price, effectiveDate); 
	    this.endDate = LocalDate.parse(endDate);
	}

	public Boolean isEffective(LocalDate date) {
	    return getEffectiveDate().isBefore(date) || getEffectiveDate().equals(date) 
	           && (endDate.isAfter(date) || endDate.equals(date));
	}

	public String toString() {
	    return super.toString() + " End: " + endDate;
	}
} 
