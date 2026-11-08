package posPd;
import java.time.LocalDate;

public class PromoPrice extends Price {
    private static final long serialVersionUID = 1L;
    private final LocalDate endDate;
    public PromoPrice(String price, String effectiveDate, String endDate) {
        super(null, price, effectiveDate);
        this.endDate = LocalDate.parse(endDate);
        if (this.endDate.isBefore(getEffectiveDate()))
            throw new IllegalArgumentException("Promotion end date must follow the start date.");
    }
    public LocalDate getEndDate() { return endDate; }
    public Boolean isEffective(LocalDate date) { return isInEffectForDate(date); }
    @Override public boolean isInEffectForDate(LocalDate date) {
        return super.isInEffectForDate(date) && !date.isAfter(endDate);
    }
    @Override public String toString() { return super.toString() + " through " + endDate; }
}

