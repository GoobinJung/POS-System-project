package posUi;
import javax.swing.*;
import posPd.*;

public class UpcEditPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public UpcEditPanel(JFrame frame, Store store, Item item, UPC upc, boolean isNew) {
        JPanel body = Ui.page(this, "UPC");
        JTextField code = Ui.field(body, "UPC (8 to 14 digits)", upc.getUpc());
        Ui.row(body, Ui.button("Save", () -> {
            String value = Ui.required(code, "UPC");
            if (!UPC.validateUPC(value)) throw new IllegalArgumentException("UPC must contain 8 to 14 digits.");
            Item owner = store.findItemForUPC(value);
            if (owner != null && owner != item) throw new IllegalArgumentException("UPC is assigned to another item.");
            UPC existing = item.getUpcs().get(value);
            if (existing != null && existing != upc) throw new IllegalArgumentException("UPC already exists.");
            if (!isNew) { item.getUpcs().remove(upc.getUpc()); store.removeUPC(upc); }
            upc.setUpc(value); upc.setItem(item); item.addUPC(upc);
            boolean draft = !store.getItems().containsValue(item);
            if (!draft) { store.addUPC(upc); Ui.save(store); }
            Ui.show(frame, new ItemEditPanel(frame, store, item, draft));
        }), Ui.button("Cancel", () -> Ui.show(frame,
            new ItemEditPanel(frame, store, item, !store.getItems().containsValue(item)))));
    }
}
