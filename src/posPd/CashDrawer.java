package posPd;

import java.math.BigDecimal;

public class CashDrawer {

	private BigDecimal cashAmount;
	private int position;

	public CashDrawer() {
	    cashAmount = BigDecimal.ZERO;
	}

	public void removeCash(BigDecimal cash) {
	    cashAmount = cashAmount.subtract(cash);
	}

	public void addCash(BigDecimal cash) {
	    cashAmount = cashAmount.add(cash);
	}

	public String toString() {
	    return "CashDrawer: Amount=" + cashAmount;
	}
	
	public BigDecimal getCashAmount() {
	    return cashAmount;
	}
	
	
}
