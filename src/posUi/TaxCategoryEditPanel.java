package posUi;
import javax.swing.*;
import posPd.*;

public class TaxCategoryEditPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public TaxCategoryEditPanel(JFrame frame, Store store, TaxCategory category, boolean isNew) {
        JPanel body = Ui.page(this, "Tax category");
        JTextField name = Ui.field(body, "Category name", category.getCategory());
        name.setEditable(isNew);
        Ui.Action draft = () -> {
            String id = Ui.required(name, "Category name");
            Ui.unique(store.getTaxCategories(), id, category);
            category.setTaxCategory(id);
        };
        DefaultListModel<TaxRate> model = new DefaultListModel<>();
        for (TaxRate rate : category.getTaxRates()) model.addElement(rate);
        JList<TaxRate> rates = new JList<>(model);
        body.add(new JScrollPane(rates));
        Ui.row(body, Ui.button("Add rate", () -> {
            draft.run();
            Ui.show(frame, new TaxRateEditPanel(frame, store, category, isNew, new TaxRate(), true));
        }), Ui.button("Edit rate", () -> {
            TaxRate selected = Ui.selected(rates); draft.run();
            Ui.show(frame, new TaxRateEditPanel(frame, store, category, isNew, selected, false));
        }), Ui.button("Delete rate", () -> {
            category.removeTaxRate(Ui.selected(rates));
            if (!isNew) Ui.save(store);
            Ui.show(frame, new TaxCategoryEditPanel(frame, store, category, isNew));
        }));
        Ui.row(body, Ui.button("Save category", () -> {
            draft.run();
            if (category.getTaxRates().isEmpty()) throw new IllegalArgumentException("Add at least one tax rate.");
            store.addTaxCategory(category);
            Ui.save(store);
            Ui.show(frame, new TaxCategoryListPanel(frame, store));
        }), Ui.button("Back to categories", () -> Ui.show(frame, new TaxCategoryListPanel(frame, store))));
        Ui.row(body, new JLabel("Saved rate edits apply immediately to existing categories."));
    }
}
