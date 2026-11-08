package posPd;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Credit extends AuthorizedPayment implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


	private String cardType;
	public String getCardType() {
	    return cardType;
	}

	public void setCardType(String cardType) {
	    this.cardType = cardType;
	}

	private String acctNumber;
	public String getAcctNumber() {
	    return acctNumber;
	}

	public void setAcctNumber(String acctNumber) {
	    this.acctNumber = acctNumber == null ? null : acctNumber.substring(Math.max(0, acctNumber.length() - 4));
	}

	private LocalDate expireDate;

	public LocalDate getExpireDate() {
	    return expireDate;
	}

	public void setExpireDate(LocalDate expireDate) {
	    this.expireDate = expireDate;
	}

	public Credit() {

	}

	public Credit(String amount, String cardType, String acctNumber, String expireDate) {
	    setAmount(new BigDecimal(amount));
        setAmtTendered(new BigDecimal(amount));
	    this.cardType = cardType;
	    this.acctNumber = acctNumber;
	    this.expireDate = LocalDate.parse(expireDate);
	}

	public Boolean isAuthorized() {
	    return getAmtTendered().signum() > 0 && cardType != null && !cardType.isBlank()
            && acctNumber != null && acctNumber.matches("[0-9]{4}")
            && expireDate != null && !expireDate.isBefore(LocalDate.now());
	}

	public String toString() {
	    return "Credit: Amount=" + getAmount() + ", Type=" + cardType + ", Expire=" + expireDate;
	}
}
