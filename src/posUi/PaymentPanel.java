package posUi;
import javax.swing.*;
import posPd.*;

public class PaymentPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public PaymentPanel(JFrame frame, JPanel previous, Store store, Session session, Sale sale) {
        JPanel body = Ui.page(this, "Payment (simulation)");
        Ui.row(body, new JLabel("Total: " + sale.calcTotal() + " | Due: " + sale.calcAmountForPayment()
            + " | Tendered: " + sale.calcAmtTendered() + " | Change: " + sale.calcChange()));
        DefaultListModel<Payment> model = new DefaultListModel<>();
        for (Payment payment : sale.getPayments()) model.addElement(payment);
        JList<Payment> payments = new JList<>(model);
        body.add(new JScrollPane(payments));
        Ui.row(body,
            Ui.button("Cash", () -> Ui.show(frame, new CashPanel(frame, this, store, session, sale, new Cash()))),
            Ui.button("Check", () -> Ui.show(frame, new CheckPanel(frame, this, store, session, sale, new Check()))),
            Ui.button("Credit", () -> Ui.show(frame, new CreditPanel(frame, this, store, session, sale, new Credit()))));
        Ui.row(body, Ui.button("Remove selected payment", () -> {
            sale.removePayment(Ui.selected(payments));
            Ui.show(frame, new PaymentPanel(frame, this, store, session, sale));
        }));
        JButton done = Ui.button("Payment Complete", () -> Ui.frame(frame).checkout(session, sale));
        done.setEnabled(sale.isPaymentEnough());
        Ui.row(body, done, Ui.button("Back to sale", () -> Ui.frame(frame).checkout(session, sale)));
    }
}

