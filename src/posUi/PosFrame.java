package posUi;

import posDm.StoreDm;
import posPd.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PosFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Store store;
    private Session activeSession;
    private Sale activeSale;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try { new PosFrame(StoreDm.loadStore()).setVisible(true); }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Cannot open store: " + ex.getMessage()
                    + "\nNo saved data was overwritten.", "POS startup", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    public PosFrame(Store store) {
        super("Point of Sale - " + store.getName());
        this.store = store;
        setSize(900, 700);
        setMinimumSize(new Dimension(760, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        item(file, "Save", () -> { Ui.save(store); JOptionPane.showMessageDialog(this, "Store saved."); });
        bar.add(file);
        JMenu maintain = new JMenu("Maintain");
        item(maintain, "Store", () -> maintain(new StoreEditPanel(this, store)));
        item(maintain, "Cashier", () -> maintain(new CashierListPanel(this, store)));
        item(maintain, "Register", () -> maintain(new RegisterListPanel(this, store)));
        item(maintain, "Items", () -> maintain(new ItemListPanel(this, store)));
        item(maintain, "Tax Categories", () -> maintain(new TaxCategoryListPanel(this, store)));
        bar.add(maintain);
        JMenu pos = new JMenu("POS");
        item(pos, "Login / Return to checkout", () -> Ui.show(this, activeSession == null
            ? new LoginPanel(this, store) : new PosSalePanel(this, store, activeSession, activeSale)));
        bar.add(pos);
        JMenu reports = new JMenu("Reports");
        item(reports, "Daily sales", () -> Ui.show(this, new DailySalesPanel(store)));
        item(reports, "Cashier sales", () -> Ui.show(this, new CashierReportPanel(store)));
        item(reports, "Item sales", () -> Ui.show(this, new ItemReportPanel(store)));
        bar.add(reports);
        setJMenuBar(bar);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent event) {
                Ui.run(PosFrame.this, () -> {
                    if (activeSale != null && !activeSale.getSaleLineItems().isEmpty()
                        && JOptionPane.showConfirmDialog(PosFrame.this,
                            "Discard the current unfinished sale and close? Completed sales will be saved.",
                            "Unfinished sale", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
                    Ui.save(store);
                    dispose();
                });
            }
        });
        Ui.show(this, new HomePanel(store));
    }
    private void item(JMenu menu, String label, Ui.Action action) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(event -> Ui.run(this, action));
        menu.add(item);
    }
    private void maintain(JPanel panel) {
        if (activeSession != null) throw new IllegalStateException("End the cashier session before editing store data.");
        Ui.show(this, panel);
    }
    void checkout(Session session, Sale sale) {
        activeSession = session;
        activeSale = sale;
        Ui.show(this, new PosSalePanel(this, store, session, sale));
    }
    void finishSession() {
        activeSession = null;
        activeSale = null;
        Ui.show(this, new HomePanel(store));
    }
}

