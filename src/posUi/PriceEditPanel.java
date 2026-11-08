package posUi;
import javax.swing.*;
import posPd.*;
import java.time.LocalDate;

public class PriceEditPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public PriceEditPanel(JFrame frame, Store store, Item item, Price price, boolean isNew) {
        JPanel body = Ui.page(this, "Price");
        JTextField amount = Ui.field(body, "Price", isNew ? "" : price.getPrice().toPlainString());
        JTextField start = Ui.field(body, "Effective date (YYYY-MM-DD)", isNew
            ? LocalDate.now().toString() : price.getEffectiveDate().toString());
        JCheckBox promo = new JCheckBox("Promotional price", price instanceof PromoPrice);
        Ui.row(body, promo);
        JTextField end = Ui.field(body, "Promotion end (YYYY-MM-DD)", price instanceof PromoPrice
            ? ((PromoPrice) price).getEndDate().toString() : LocalDate.now().plusDays(7).toString());
        Ui.row(body, Ui.button("Save", () -> {
            String value = Ui.money(amount).toPlainString();
            String effective = Ui.date(start).toString();
            Price replacement = promo.isSelected() ? new PromoPrice(value, effective, Ui.date(end).toString())
                : new Price(null, value, effective);
            for (Price existing : item.getPrices()) if (existing != price && existing.compareTo(replacement) == 0)
                throw new IllegalArgumentException("A price of this type already starts on that date.");
            if (!isNew) item.getPrices().remove(price);
            replacement.setItem(item); item.addPrice(replacement);
            boolean draft = !store.getItems().containsValue(item);
            if (!draft) Ui.save(store);
            Ui.show(frame, new ItemEditPanel(frame, store, item, draft));
        }), Ui.button("Cancel", () -> Ui.show(frame,
            new ItemEditPanel(frame, store, item, !store.getItems().containsValue(item)))));
    }
}
