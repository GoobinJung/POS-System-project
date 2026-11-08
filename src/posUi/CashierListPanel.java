package posUi;
import javax.swing.*;
import posPd.*;

public class CashierListPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public CashierListPanel(JFrame frame, Store store) {
        JPanel body = Ui.page(this, "Cashiers");
        DefaultListModel<Cashier> model = new DefaultListModel<>();
        for (Cashier value : store.getCashiers().values()) model.addElement(value);
        JList<Cashier> list = new JList<>(model);
        body.add(new JScrollPane(list));
        Ui.row(body, Ui.button("Add", () -> Ui.show(frame, new CashierEditPanel(frame, store, new Cashier(), true))),
            Ui.button("Edit", () -> Ui.show(frame, new CashierEditPanel(frame, store, Ui.selected(list), false))),
            Ui.button("Delete", () -> {
                Cashier value = Ui.selected(list);
                if (JOptionPane.showConfirmDialog(frame, "Delete selected record?", "Delete",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
                store.removeCashier(value);
                Ui.save(store);
                Ui.show(frame, new CashierListPanel(frame, store));
            }), Ui.button("Home", () -> Ui.show(frame, new HomePanel(store))));
    }
}
