package posUi;
import javax.swing.*;
import posPd.*;

public class RegisterListPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public RegisterListPanel(JFrame frame, Store store) {
        JPanel body = Ui.page(this, "Registers");
        DefaultListModel<Register> model = new DefaultListModel<>();
        for (Register value : store.getRegisters().values()) model.addElement(value);
        JList<Register> list = new JList<>(model);
        body.add(new JScrollPane(list));
        Ui.row(body, Ui.button("Add", () -> Ui.show(frame, new RegisterEditPanel(frame, store, new Register(), true))),
            Ui.button("Edit", () -> Ui.show(frame, new RegisterEditPanel(frame, store, Ui.selected(list), false))),
            Ui.button("Delete", () -> {
                Register value = Ui.selected(list);
                if (JOptionPane.showConfirmDialog(frame, "Delete selected record?", "Delete",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
                store.removeRegister(value);
                Ui.save(store);
                Ui.show(frame, new RegisterListPanel(frame, store));
            }), Ui.button("Home", () -> Ui.show(frame, new HomePanel(store))));
    }
}

