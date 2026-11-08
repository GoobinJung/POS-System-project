package posUi;
import javax.swing.*;
import posPd.*;

public class EndSessionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public EndSessionPanel(JFrame frame, Store store, Session session, Sale sale) {
        JPanel body = Ui.page(this, "Session reconciliation");
        Ui.row(body, new JLabel("Cashier: " + session.getCashier() + " | Register: " + session.getRegister()));
        Ui.row(body, new JLabel("Completed sales: " + session.getNumSales() + " | Total sales: " + session.calcTotal()));
        Ui.row(body, new JLabel("Starting cash: " + session.getStartCash() + " | Expected cash: " + session.calcExpectedCash()));
        JTextField counted = Ui.field(body, "Counted cash", session.calcExpectedCash().toPlainString());
        JLabel difference = new JLabel("Difference: 0.00");
        Ui.row(body, difference);
        Ui.row(body, Ui.button("Calculate difference", () ->
            difference.setText("Difference: " + Ui.money(counted).subtract(session.calcExpectedCash()))));
        Ui.row(body, Ui.button("End Session", () -> {
            if (!session.isClosed()) session.close(Ui.money(counted));
            Ui.save(store);
            Ui.frame(frame).finishSession();
        }), Ui.button("Back", () -> Ui.frame(frame).checkout(session, sale)));
    }
}

