package posPd;
import java.math.BigDecimal;

public abstract class Payment implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal amount = Money.parse("0");
    private BigDecimal amtTendered = Money.parse("0");
    private boolean frozen;
    public void freeze() { frozen = true; }
    private void requireEditable() {
        if (frozen) throw new IllegalStateException("Completed payment cannot be changed.");
    }
    public Boolean countsAsCash() { return false; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { requireEditable(); this.amount = Money.amount(amount); }
    public BigDecimal getAmtTendered() { return amtTendered; }
    public void setAmtTendered(BigDecimal amount) { requireEditable(); amtTendered = Money.amount(amount); }
}
