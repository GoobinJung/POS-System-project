package posUi;
import javax.swing.*;
import posPd.*;

public class CheckPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public CheckPanel(JFrame frame, JPanel previous, Store store, Session session, Sale sale, Check check) {
        JPanel body = Ui.page(this, "Check payment (simulation)");
        JTextField amount = Ui.field(body, "Amount", sale.calcAmountForPayment().toPlainString());
        JTextField routing = Ui.field(body, "Routing number (9 digits)", "");
        JTextField account = Ui.field(body, "Demo account number", "");
        JTextField number = Ui.field(body, "Check number", "");
        Ui.row(body, Ui.button("Save", () -> {
            check.setAmtTendered(Ui.money(amount));
            check.setRoutingNumber(Ui.required(routing, "Routing number"));
            check.setAccountNumber(Ui.required(account, "Account number"));
            check.setCheckNumber(Ui.required(number, "Check number"));
            sale.addPayment(check);
            Ui.show(frame, new PaymentPanel(frame, previous, store, session, sale));
        }), Ui.button("Cancel", () -> Ui.show(frame, new PaymentPanel(frame, previous, store, session, sale))));
    }
}

