package posUi;
import javax.swing.*;
import posPd.*;

public class CashPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public CashPanel(JFrame frame, JPanel previous, Store store, Session session, Sale sale, Cash cash) {
        JPanel body = Ui.page(this, "Cash payment");
        JTextField tendered = Ui.field(body, "Amount tendered", sale.calcAmountForPayment().toPlainString());
        Ui.row(body, Ui.button("Save", () -> {
            cash.setAmtTendered(Ui.money(tendered));
            sale.addPayment(cash);
            Ui.show(frame, new PaymentPanel(frame, previous, store, session, sale));
        }), Ui.button("Cancel", () -> Ui.show(frame, new PaymentPanel(frame, previous, store, session, sale))));
    }
}

