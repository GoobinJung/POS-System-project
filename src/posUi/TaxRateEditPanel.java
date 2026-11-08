package posUi;
import javax.swing.*;
import posPd.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxRateEditPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public TaxRateEditPanel(JFrame frame, Store store, TaxCategory category, boolean newCategory,
        TaxRate rate, boolean isNew) {
        JPanel body = Ui.page(this, "Tax rate");
        JTextField value = Ui.field(body, "Rate (0.07 = 7%)", isNew ? "0" : rate.getTaxRate().toPlainString());
        JTextField date = Ui.field(body, "Effective date (YYYY-MM-DD)", isNew
            ? LocalDate.now().toString() : rate.getEffectiveDate().toString());
        Ui.row(body, Ui.button("Save", () -> {
            TaxRate replacement = new TaxRate(Ui.date(date), new BigDecimal(value.getText().trim()));
            for (TaxRate existing : category.getTaxRates())
                if (existing != rate && existing.compareTo(replacement) == 0)
                    throw new IllegalArgumentException("A tax rate already starts on that date.");
            if (!isNew) category.removeTaxRate(rate);
            category.addTaxRate(replacement);
            if (!newCategory) Ui.save(store);
            Ui.show(frame, new TaxCategoryEditPanel(frame, store, category, newCategory));
        }), Ui.button("Cancel", () -> Ui.show(frame, new TaxCategoryEditPanel(frame, store, category, newCategory))));
    }
}
