package posUi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import posPd.*;
import posDm.CsvExport;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;

class ReportPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Store store;
    private final int type;
    private final JTextField date;
    private final DefaultTableModel model;
    private final JLabel summary = new JLabel();
    private List<Object[]> rows;
    private final String[] headers;
    ReportPanel(Store store, int type, String title) {
        this.store = store;
        this.type = type;
        JPanel body = Ui.page(this, title);
        date = Ui.field(body, "Date (YYYY-MM-DD)", LocalDate.now().toString());
        headers = type == 0 ? new String[] {"Time", "Cashier", "Register", "Subtotal", "Tax", "Total", "Applied", "Change"}
            : type == 1 ? new String[] {"Cashier", "Name", "Sales", "Total", "Cash"}
            : new String[] {"Item", "Description", "Quantity", "Subtotal"};
        model = new DefaultTableModel(headers, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        body.add(new JScrollPane(table));
        Ui.row(body, summary);
        Ui.row(body, Ui.button("Refresh", this::refresh), Ui.button("Export CSV", () -> {
            refresh();
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("sales-report-" + date.getText() + ".csv"));
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
            java.nio.file.Path path = chooser.getSelectedFile().toPath();
            if (Files.exists(path) && JOptionPane.showConfirmDialog(this, "Replace existing file?",
                "Export", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            CsvExport.write(path, headers, rows);
            JOptionPane.showMessageDialog(this, "Report exported.");
        }));
        refresh();
    }
    private void refresh() {
        LocalDate selected = Ui.date(date);
        rows = type == 0 ? Reports.dailySales(store, selected)
            : type == 1 ? Reports.cashierSales(store, selected) : Reports.itemSales(store, selected);
        model.setRowCount(0);
        BigDecimal total = Money.parse("0");
        int amountColumn = type == 0 ? 5 : 3;
        for (Object[] row : rows) { model.addRow(row); total = total.add((BigDecimal) row[amountColumn]); }
        summary.setText("Rows: " + rows.size() + " | " + (type == 2 ? "Subtotal: " : "Sales total: ") + total);
    }
}
