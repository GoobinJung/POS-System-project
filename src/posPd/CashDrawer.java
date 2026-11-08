package posPd;

import java.math.BigDecimal;

public class CashDrawer implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


	private BigDecimal cashAmount;
	private int position;

	public CashDrawer() {
	    cashAmount = BigDecimal.ZERO;
	}

	public void removeCash(BigDecimal cash) {
        BigDecimal value = Money.amount(cash);
        if (value.compareTo(cashAmount) > 0) throw new IllegalArgumentException("Not enough cash in the drawer.");
	    cashAmount = cashAmount.subtract(value);
	}

	public void addCash(BigDecimal cash) {
	    cashAmount = cashAmount.add(Money.amount(cash));
	}

	public String toString() {
	    return "CashDrawer: Amount=" + cashAmount;
	}

	public BigDecimal getCashAmount() {
	    return cashAmount;
	}


}

