package posUi;
import javax.swing.*;
import posPd.*;

public class StoreEditPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public StoreEditPanel(JFrame frame, Store store) {
        JPanel body = Ui.page(this, "Store");
        JTextField name = Ui.field(body, "Store name", store.getName());
        Ui.row(body, Ui.button("Save", () -> {
            store.setName(Ui.required(name, "Store name"));
            Ui.save(store);
            frame.setTitle("Point of Sale - " + store.getName());
            Ui.show(frame, new HomePanel(store));
        }), Ui.button("Cancel", () -> Ui.show(frame, new HomePanel(store))));
    }
}

