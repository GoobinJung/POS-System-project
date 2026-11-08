package posUi;
import javax.swing.*;
import posPd.*;
import java.math.BigDecimal;

public class LoginPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public LoginPanel(JFrame frame, Store store) {
        JPanel body = Ui.page(this, "Cashier login");
        JComboBox<Cashier> cashiers = new JComboBox<>(store.getCashiers().values().toArray(new Cashier[0]));
        JComboBox<Register> registers = new JComboBox<>(store.getRegisters().values().toArray(new Register[0]));
        Ui.input(body, "Cashier", cashiers);
        Ui.input(body, "Register", registers);
        JTextField start = Ui.field(body, "Starting cash (new session)", "100.00");
        JPasswordField password = new JPasswordField(24);
        Ui.input(body, "Password", password);
        Ui.row(body, Ui.button("Login", () -> {
            Cashier cashier = (Cashier) cashiers.getSelectedItem();
            Register register = (Register) registers.getSelectedItem();
            char[] entered = password.getPassword();
            boolean authorized = cashier != null && cashier.isAuthorized(new String(entered));
            java.util.Arrays.fill(entered, '\0');
            password.setText("");
            if (!authorized) throw new IllegalArgumentException("Invalid cashier or password.");
            if (register == null) throw new IllegalArgumentException("Create a register first.");
            Session session = null;
            for (Session existing : store.getSessions()) {
                if (existing.isClosed()) continue;
                if (existing.getRegister() == register && existing.getCashier() == cashier) session = existing;
                else if (existing.getRegister() == register || existing.getCashier() == cashier)
                    throw new IllegalArgumentException("Cashier or register already has another open session.");
            }
            if (session == null) {
                BigDecimal starting = Ui.money(start);
                session = new Session(store, cashier, register);
                session.setStartCash(starting);
                store.addSession(session);
            }
            Ui.save(store);
            Ui.frame(frame).checkout(session, new Sale());
        }), Ui.button("Cancel", () -> Ui.show(frame, new HomePanel(store))));
    }
}

