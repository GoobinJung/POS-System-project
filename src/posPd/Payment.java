package posPd;
import java.math.BigDecimal;

public abstract class Payment {

	private BigDecimal amount;
	private BigDecimal amtTendered;

	public Boolean countsAsCash() {
	    return false; 
	}
	
	public BigDecimal getAmount() {
	    return amount;
	}
	
	public void setAmount(BigDecimal amount) {
	    this.amount = amount;
	}
	
	public BigDecimal getAmtTendered() {
	    return amtTendered;
	}
	
	public void setAmtTendered(BigDecimal amtTendered) {
	    this.amtTendered = amtTendered;
	}
}