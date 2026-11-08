package posUi;
import javax.swing.*;
import posPd.*;

public class RegisterEditPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public RegisterEditPanel(JFrame frame, Store store, Register register, boolean isNew) {
        JPanel body = Ui.page(this, isNew ? "Add register" : "Register");
        JTextField number = Ui.field(body, "Register number", register.getNumber());
        number.setEditable(isNew);
        Ui.row(body, Ui.button("Save", () -> {
            String id = Ui.required(number, "Register number");
            Ui.unique(store.getRegisters(), id, register);
            register.setNumber(id);
            store.addRegister(register);
            Ui.save(store);
            Ui.show(frame, new RegisterListPanel(frame, store));
        }), Ui.button("Cancel", () -> Ui.show(frame, new RegisterListPanel(frame, store))));
    }
}
