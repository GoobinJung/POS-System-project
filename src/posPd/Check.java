package posPd;

import java.math.BigDecimal;

public class Check extends AuthorizedPayment {

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
	    setAmount(new BigDecimal(amount)); // 
	    this.accountNumber = accountNumber;
	}

	public Boolean isAuthorized() {
	    return true; 
	}

	public String toString() {
	    return "Check: Amount=" + getAmount() + ", Account=" + accountNumber;
	}
}