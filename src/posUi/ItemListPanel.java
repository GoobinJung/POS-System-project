package posUi;
import javax.swing.*;
import posPd.*;

public class ItemListPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public ItemListPanel(JFrame frame, Store store) {
        JPanel body = Ui.page(this, "Items");
        DefaultListModel<Item> model = new DefaultListModel<>();
        for (Item value : store.getItems().values()) model.addElement(value);
        JList<Item> list = new JList<>(model);
        body.add(new JScrollPane(list));
        Ui.row(body, Ui.button("Add", () -> Ui.show(frame, new ItemEditPanel(frame, store, new Item(), true))),
            Ui.button("Edit", () -> Ui.show(frame, new ItemEditPanel(frame, store, Ui.selected(list), false))),
            Ui.button("Delete", () -> {
                Item value = Ui.selected(list);
                if (JOptionPane.showConfirmDialog(frame, "Delete selected record?", "Delete",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
                store.removeItem(value);
                Ui.save(store);
                Ui.show(frame, new ItemListPanel(frame, store));
            }), Ui.button("Home", () -> Ui.show(frame, new HomePanel(store))));
    }
}

