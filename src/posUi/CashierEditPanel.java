package posUi;
import javax.swing.*;
import posPd.*;

public class CashierEditPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public CashierEditPanel(JFrame frame, Store store, Cashier cashier, boolean isNew) {
        JPanel body = Ui.page(this, isNew ? "Add cashier" : "Edit cashier");
        Person person = cashier.getPerson();
        JTextField number = Ui.field(body, "Cashier number", cashier.getNumber());
        number.setEditable(isNew);
        JTextField name = Ui.field(body, "Name", person.getName());
        JTextField identifier = Ui.field(body, "Fictional identifier (optional)", person.getSSN());
        JTextField address = Ui.field(body, "Address (optional)", person.getAddress());
        JTextField city = Ui.field(body, "City (optional)", person.getCity());
        JTextField state = Ui.field(body, "State (optional)", person.getState());
        JTextField zip = Ui.field(body, "ZIP (optional)", person.getZip());
        JTextField phone = Ui.field(body, "Phone (optional)", person.getPhone());
        JPasswordField password = new JPasswordField(24);
        Ui.input(body, isNew ? "Password" : "New password (blank = keep)", password);
        Ui.row(body, Ui.button("Save", () -> {
            String id = Ui.required(number, "Cashier number");
            Ui.unique(store.getCashiers(), id, cashier);
            String newName = Ui.required(name, "Name");
            char[] chars = password.getPassword();
            String newPassword = new String(chars);
            java.util.Arrays.fill(chars, '\0');
            if (isNew && newPassword.isBlank()) throw new IllegalArgumentException("Password is required.");
            if (!newPassword.isBlank()) cashier.setPassword(newPassword);
            cashier.setNumber(id);
            person.setName(newName);
            person.setSSN(identifier.getText().trim());
            person.setAddress(address.getText().trim());
            person.setCity(city.getText().trim());
            person.setState(state.getText().trim());
            person.setZip(zip.getText().trim());
            person.setPhone(phone.getText().trim());
            store.addCashier(cashier);
            Ui.save(store);
            Ui.show(frame, new CashierListPanel(frame, store));
        }), Ui.button("Cancel", () -> Ui.show(frame, new CashierListPanel(frame, store))));
    }
}
