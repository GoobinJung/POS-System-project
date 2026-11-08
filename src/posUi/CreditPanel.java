package posUi;
import javax.swing.*;
import posPd.*;

public class CreditPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public CreditPanel(JFrame frame, JPanel previous, Store store, Session session, Sale sale, Credit credit) {
        JPanel body = Ui.page(this, "Credit payment (simulation)");
        JTextField amount = Ui.field(body, "Amount", sale.calcAmountForPayment().toPlainString());
        JTextField type = Ui.field(body, "Card type", "Visa");
        JTextField lastFour = Ui.field(body, "Demo card last four digits", "");
        JTextField expiry = Ui.field(body, "Expiration (YYYY-MM-DD)", "");
        Ui.row(body, Ui.button("Save", () -> {
            String digits = Ui.required(lastFour, "Last four digits");
            if (!digits.matches("[0-9]{4}")) throw new IllegalArgumentException("Enter exactly four digits.");
            credit.setAmtTendered(Ui.money(amount));
            credit.setCardType(Ui.required(type, "Card type"));
            credit.setAcctNumber(digits);
            credit.setExpireDate(Ui.date(expiry));
            sale.addPayment(credit);
            Ui.show(frame, new PaymentPanel(frame, previous, store, session, sale));
        }), Ui.button("Cancel", () -> Ui.show(frame, new PaymentPanel(frame, previous, store, session, sale))));
    }
}

