package posUi;
import javax.swing.*;
import posPd.*;

public class TaxCategoryListPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public TaxCategoryListPanel(JFrame frame, Store store) {
        JPanel body = Ui.page(this, "Tax categories");
        DefaultListModel<TaxCategory> model = new DefaultListModel<>();
        for (TaxCategory value : store.getTaxCategories().values()) model.addElement(value);
        JList<TaxCategory> list = new JList<>(model);
        body.add(new JScrollPane(list));
        Ui.row(body, Ui.button("Add", () -> Ui.show(frame, new TaxCategoryEditPanel(frame, store, new TaxCategory(), true))),
            Ui.button("Edit", () -> Ui.show(frame, new TaxCategoryEditPanel(frame, store, Ui.selected(list), false))),
            Ui.button("Delete", () -> {
                TaxCategory value = Ui.selected(list);
                if (JOptionPane.showConfirmDialog(frame, "Delete selected record?", "Delete",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
                store.removeTaxCategory(value);
                Ui.save(store);
                Ui.show(frame, new TaxCategoryListPanel(frame, store));
            }), Ui.button("Home", () -> Ui.show(frame, new HomePanel(store))));
    }
}

