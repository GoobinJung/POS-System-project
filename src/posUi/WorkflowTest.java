package posUi;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import posDm.StoreDm;
import posPd.*;

/** Exercises real Swing actions on the EDT without showing a window. */
public final class WorkflowTest {
    private static int checks;
    private static void check(boolean valid, String message) {
        checks++;
        if (!valid) throw new AssertionError(message);
    }
    private static <T extends Component> java.util.List<T> find(Container parent, Class<T> type) {
        java.util.List<T> result = new java.util.ArrayList<>();
        for (Component child : parent.getComponents()) {
            if (type.isInstance(child)) result.add(type.cast(child));
            if (child instanceof Container) result.addAll(find((Container) child, type));
        }
        return result;
    }
    private static void click(Container parent, String text) {
        JButton button = find(parent, JButton.class).stream().filter(b -> text.equals(b.getText())).findFirst().orElseThrow();
        check(button.isEnabled(), text + " enabled");
        button.doClick();
    }
    private static JTextField field(Container parent, int index) { return find(parent, JTextField.class).get(index); }
    private static void layout(Container container) {
        container.doLayout();
        for (Component child : container.getComponents()) if (child instanceof Container) layout((Container) child);
    }
    private static BufferedImage paint(JPanel panel) {
        panel.setSize(850, 620);
        layout(panel);
        BufferedImage image = new BufferedImage(850, 620, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        panel.printAll(graphics);
        graphics.dispose();
        return image;
    }
    private static void maintenance(PosFrame frame, Store store) {
        Ui.show(frame, new CashierEditPanel(frame, store, new Cashier(), true));
        field(frame, 0).setText("3");
        field(frame, 1).setText("Test cashier");
        field(frame, 8).setText("new-password");
        click(frame, "Save");
        check(store.findCashierForNumber("3").isAuthorized("new-password"), "New cashier saved");
        Ui.show(frame, new CashierEditPanel(frame, store, store.findCashierForNumber("3"), false));
        field(frame, 1).setText("Updated cashier");
        click(frame, "Save");
        check(store.findCashierForNumber("3").isAuthorized("new-password"), "Blank password keeps hash");
        Ui.show(frame, new RegisterEditPanel(frame, store, new Register(), true));
        field(frame, 0).setText("3");
        click(frame, "Save");
        check(store.findRegisterForNumber("3") != null, "New register saved");
        Ui.show(frame, new TaxCategoryEditPanel(frame, store, new TaxCategory(), true));
        field(frame, 0).setText("Test tax");
        click(frame, "Add rate");
        field(frame, 0).setText("0.05");
        click(frame, "Save");
        click(frame, "Save category");
        TaxCategory category = store.findTaxCategoryForCategory("Test tax");
        check(category != null && category.getTaxRates().size() == 1, "New tax category and rate saved");
        Ui.show(frame, new ItemEditPanel(frame, store, new Item(), true));
        field(frame, 0).setText("2001");
        field(frame, 1).setText("Test product");
        find(frame, JComboBox.class).get(0).setSelectedItem(category);
        click(frame, "Add UPC");
        field(frame, 0).setText("55555555555");
        click(frame, "Save");
        click(frame, "Add price");
        field(frame, 0).setText("2.00");
        click(frame, "Save");
        click(frame, "Save item");
        Item item = store.findItemForNumber("2001");
        check(item != null && store.findItemForUPC("55555555555") == item, "New item and UPC saved");
        Ui.show(frame, new ItemEditPanel(frame, store, item, false));
        click(frame, "Add price");
        field(frame, 0).setText("1.00");
        find(frame, JCheckBox.class).get(0).setSelected(true);
        click(frame, "Save");
        check(item.getPriceForDate(java.time.LocalDate.now()) instanceof PromoPrice, "Promotion form creates a promotion");
    }
    private static void panelSmoke(Store store) {
        Item item = store.findItemForNumber("1001");
        TaxCategory category = item.getTaxCategory();
        for (JPanel panel : new JPanel[] {
            new HomePanel(store), new LoginPanel(null, store), new StoreEditPanel(null, store),
            new CashierListPanel(null, store), new CashierEditPanel(null, store, store.findCashierForNumber("1"), false),
            new CashierEditPanel(null, store, new Cashier(), true),
            new RegisterListPanel(null, store), new RegisterEditPanel(null, store, store.findRegisterForNumber("1"), false),
            new ItemListPanel(null, store), new ItemEditPanel(null, store, item, false),
            new UpcEditPanel(null, store, item, item.getUpcs().firstEntry().getValue(), false),
            new PriceEditPanel(null, store, item, item.getPrices().first(), false),
            new TaxCategoryListPanel(null, store), new TaxCategoryEditPanel(null, store, category, false),
            new TaxRateEditPanel(null, store, category, false, category.getTaxRates().first(), false),
            new DailySalesPanel(store), new CashierReportPanel(store), new ItemReportPanel(store)
        }) { paint(panel); checks++; }
    }
    public static void main(String[] args) throws Exception {
        System.setProperty("pos.test", "true");
        Files.createDirectories(Path.of("out"));
        Path temp = Files.createTempDirectory(Path.of("out"), "pos-ui-test-");
        System.setProperty("pos.state", temp.resolve("state.ser").toString());
        final Throwable[] failure = new Throwable[1];
        SwingUtilities.invokeAndWait(() -> {
            PosFrame frame = null;
            try {
                Store store = StoreDm.importStore("StoreData.csv");
                panelSmoke(store);
                if (GraphicsEnvironment.isHeadless()) return;
                frame = new PosFrame(store);
                maintenance(frame, store);
                Ui.show(frame, new LoginPanel(frame, store));
                field(frame, 0).setText("100.00");
                field(frame, 1).setText("password1");
                click(frame, "Login");
                int sessionsBeforeRestart = store.getSessions().size();
                frame.dispose();
                store = StoreDm.load(StoreDm.statePath());
                frame = new PosFrame(store);
                Ui.show(frame, new LoginPanel(frame, store));
                field(frame, 0).setText("999.00");
                field(frame, 1).setText("password1");
                click(frame, "Login");
                check(store.getSessions().size() == sessionsBeforeRestart, "Restart resumes the open session");
                Session resumed = store.getSessions().stream().filter(s -> !s.isClosed()).findFirst().orElseThrow();
                check(resumed.getStartCash().compareTo(new java.math.BigDecimal("100")) == 0,
                    "Resuming keeps original starting cash");
                field(frame, 0).setText("2");
                field(frame, 1).setText("11111111111");
                click(frame, "Add item");
                String preview = System.getProperty("pos.preview");
                if (preview != null) {
                    JPanel panel = (JPanel) ((JScrollPane) frame.getContentPane()).getViewport().getView();
                    javax.imageio.ImageIO.write(paint(panel), "png", Path.of(preview).toFile());
                }
                click(frame, "Payment");
                click(frame, "Cash");
                // Exact amount is prefilled.
                click(frame, "Save");
                click(frame, "Payment Complete");
                int before = store.getSessions().stream().mapToInt(Session::getNumSales).sum();
                click(frame, "Complete Sale");
                int after = store.getSessions().stream().mapToInt(Session::getNumSales).sum();
                check(after == before + 1, "One sale per checkout");
                click(frame, "End Session");
                click(frame, "Calculate difference");
                click(frame, "End Session");
                check(store.getSessions().stream().allMatch(Session::isClosed), "Session closed");
                Store restored = StoreDm.load(StoreDm.statePath());
                check(restored.getSessions().stream().mapToInt(Session::getNumSales).sum() == after, "Checkout persisted");
            } catch (Throwable ex) { failure[0] = ex; }
            finally { if (frame != null) frame.dispose(); }
        });
        if (failure[0] != null) throw new AssertionError("Swing workflow failed", failure[0]);
        try (java.util.stream.Stream<Path> paths = Files.list(temp)) {
            for (Path path : paths.toList()) Files.delete(path);
        }
        Files.delete(temp);
        System.out.println("PASS: " + checks + " Swing checks"
            + (GraphicsEnvironment.isHeadless() ? " (panel rendering; frame flow skipped in headless mode)." : " including checkout and restart."));
    }
}
