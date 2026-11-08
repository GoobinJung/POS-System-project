package posPd;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Shared money rules: non-negative values with cent precision. */
public final class Money {
    private Money() {}
    public static BigDecimal amount(BigDecimal value) {
        if (value == null || value.signum() < 0) throw new IllegalArgumentException("Enter a non-negative amount.");
        try { return value.setScale(2, RoundingMode.UNNECESSARY); }
        catch (ArithmeticException ex) { throw new IllegalArgumentException("Use at most two decimal places."); }
    }
    public static BigDecimal parse(String value) {
        try { return amount(new BigDecimal(value.trim())); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Enter a valid amount."); }
    }
    public static BigDecimal round(BigDecimal value) { return value.setScale(2, RoundingMode.HALF_UP); }
}
