package posPd;
import java.math.BigDecimal;

public class Cash extends Payment implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


	public Cash() {

	}

	public Cash(String amount, BigDecimal amtTendered) {
	    setAmount(new BigDecimal(amount));
	    setAmtTendered(amtTendered);
	}

	public Boolean countsAsCash() {
	    return true;
	}

	public String toString() {
	    return "Cash: Amount=" + getAmount() + ", Tendered=" + getAmtTendered();
	}
}
