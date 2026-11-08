package posPd;

import java.math.BigDecimal;

public class Check extends AuthorizedPayment implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


	private String routingNumber;
	public String getRoutingNumber() {
	    return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
	    this.routingNumber = routingNumber;
	}

	private String accountNumber;
	public String getAccountNumber() {
	    return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
	    this.accountNumber = accountNumber;
	}

	private String checkNumber;

	public String getCheckNumber() {
	    return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
	    this.checkNumber = checkNumber;
	}

	public Check() {

	}

	public Check(String amount, String accountNumber) {
	    setAmount(new BigDecimal(amount));
        setAmtTendered(new BigDecimal(amount)); //
	    this.accountNumber = accountNumber;
	}

	public Boolean isAuthorized() {
	    return getAmtTendered().signum() > 0 && routingNumber != null
            && routingNumber.matches("[0-9]{9}") && accountNumber != null
            && accountNumber.matches("[0-9]{4,20}") && checkNumber != null && checkNumber.matches("[0-9]+");
	}

	public String toString() {
	    return "Check: Amount=" + getAmount() + ", Account=" + accountNumber;
	}
}
