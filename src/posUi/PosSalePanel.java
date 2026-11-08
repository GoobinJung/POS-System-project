package posUi;
import javax.swing.*;
import posPd.*;

public class PosSalePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public PosSalePanel(JFrame frame, Store store, Session session, Sale sale) {
        JPanel body = Ui.page(this, "Checkout");
        Ui.row(body, new JLabel("Cashier: " + session.getCashier() + " | Register: " + session.getRegister()));
        JTextField quantity = Ui.field(body, "Quantity", "1");
        JTextField upc = Ui.field(body, "Item UPC (press Enter)", "");
        DefaultListModel<SaleLineItem> model = new DefaultListModel<>();
        for (SaleLineItem line : sale.getSaleLineItems()) model.addElement(line);
        JList<SaleLineItem> lines = new JList<>(model);
        body.add(new JScrollPane(lines));
        Ui.Action add = () -> {
            Item item = store.findItemForUPC(Ui.required(upc, "UPC"));
            if (item == null) throw new IllegalArgumentException("No item found for that UPC.");
            new SaleLineItem(sale, item, quantity.getText());
            Ui.frame(frame).checkout(session, sale);
        };
        upc.addActionListener(event -> Ui.run(this, add));
        Ui.row(body, Ui.button("Add item", add), Ui.button("Remove selected item", () -> {
            sale.removeSaleLineItem(Ui.selected(lines));
            Ui.frame(frame).checkout(session, sale);
        }));
        JCheckBox taxFree = new JCheckBox("Tax free", sale.isTaxFree());
        taxFree.setEnabled(sale.getPayments().isEmpty());
        taxFree.addActionListener(event -> Ui.run(this, () -> {
            sale.setTaxFree(taxFree.isSelected());
            Ui.frame(frame).checkout(session, sale);
        }));
        Ui.row(body, taxFree);
        Ui.row(body, new JLabel("Subtotal: " + sale.calcSubTotal() + " | Tax: " + sale.calcTax()
            + " | Total: " + sale.calcTotal()));
        Ui.row(body, new JLabel("Applied payments: " + sale.getTotalPayments() + " | Due: "
            + sale.calcAmountForPayment() + " | Change: " + sale.calcChange()));
        JButton payment = Ui.button("Payment", () -> {
            if (sale.getSaleLineItems().isEmpty()) throw new IllegalArgumentException("Add an item first.");
            Ui.show(frame, new PaymentPanel(frame, this, store, session, sale));
        });
        JButton complete = Ui.button("Complete Sale", () -> {
            session.addSale(sale);
            Ui.save(store);
            Ui.frame(frame).checkout(session, new Sale());
        });
        complete.setEnabled(sale.isPaymentEnough());
        Ui.row(body, payment, complete, Ui.button("Cancel Sale", () -> {
            if (!sale.getSaleLineItems().isEmpty() && JOptionPane.showConfirmDialog(frame,
                "Discard this unfinished sale and its demo payments?", "Cancel sale",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            Ui.frame(frame).checkout(session, new Sale());
        }));
        Ui.row(body, Ui.button("End Session", () -> {
            if (!sale.getSaleLineItems().isEmpty())
                throw new IllegalArgumentException("Complete or cancel the current sale first.");
            Ui.show(frame, new EndSessionPanel(frame, store, session, sale));
        }));
    }
}

