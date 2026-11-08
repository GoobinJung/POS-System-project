package posUi;
import javax.swing.*;
import posPd.*;

public class ItemEditPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public ItemEditPanel(JFrame frame, Store store, Item item, boolean isNew) {
        JPanel body = Ui.page(this, isNew ? "Add item" : "Edit item");
        JTextField number = Ui.field(body, "Item number", item.getNumber());
        number.setEditable(isNew);
        JTextField description = Ui.field(body, "Description", item.getDescription());
        JComboBox<TaxCategory> category = new JComboBox<>(store.getTaxCategories().values().toArray(new TaxCategory[0]));
        if (item.getTaxCategory() != null) category.setSelectedItem(item.getTaxCategory());
        Ui.input(body, "Tax category", category);
        Ui.Action draft = () -> {
            String id = Ui.required(number, "Item number");
            Ui.unique(store.getItems(), id, item);
            String text = Ui.required(description, "Description");
            TaxCategory tax = (TaxCategory) category.getSelectedItem();
            if (tax == null) throw new IllegalArgumentException("Create a tax category first.");
            item.setNumber(id); item.setDescription(text); item.setTaxCategory(tax);
        };
        DefaultListModel<UPC> upcs = new DefaultListModel<>();
        for (UPC upc : item.getUpcs().values()) upcs.addElement(upc);
        JList<UPC> upcList = new JList<>(upcs);
        Ui.row(body, new JLabel("UPCs"));
        body.add(new JScrollPane(upcList));
        Ui.row(body, Ui.button("Add UPC", () -> {
            draft.run(); Ui.show(frame, new UpcEditPanel(frame, store, item, new UPC(), true));
        }), Ui.button("Edit UPC", () -> {
            UPC selected = Ui.selected(upcList); draft.run();
            Ui.show(frame, new UpcEditPanel(frame, store, item, selected, false));
        }), Ui.button("Delete UPC", () -> {
            UPC selected = Ui.selected(upcList);
            item.getUpcs().remove(selected.getUpc()); store.removeUPC(selected);
            if (!isNew) Ui.save(store);
            Ui.show(frame, new ItemEditPanel(frame, store, item, isNew));
        }));
        DefaultListModel<Price> prices = new DefaultListModel<>();
        for (Price price : item.getPrices()) prices.addElement(price);
        JList<Price> priceList = new JList<>(prices);
        Ui.row(body, new JLabel("Prices"));
        body.add(new JScrollPane(priceList));
        Ui.row(body, Ui.button("Add price", () -> {
            draft.run(); Ui.show(frame, new PriceEditPanel(frame, store, item, new Price(), true));
        }), Ui.button("Edit price", () -> {
            Price selected = Ui.selected(priceList); draft.run();
            Ui.show(frame, new PriceEditPanel(frame, store, item, selected, false));
        }), Ui.button("Delete price", () -> {
            item.getPrices().remove(Ui.selected(priceList));
            if (!isNew) Ui.save(store);
            Ui.show(frame, new ItemEditPanel(frame, store, item, isNew));
        }));
        Ui.row(body, Ui.button("Save item", () -> {
            draft.run();
            if (item.getUpcs().isEmpty() || item.getPrices().isEmpty())
                throw new IllegalArgumentException("Add at least one UPC and price.");
            store.addItem(item);
            for (UPC upc : item.getUpcs().values()) store.addUPC(upc);
            Ui.save(store);
            Ui.show(frame, new ItemListPanel(frame, store));
        }), Ui.button("Back to items", () -> Ui.show(frame, new ItemListPanel(frame, store))));
        Ui.row(body, new JLabel("Saved UPC and price edits apply immediately to existing items."));
    }
}

