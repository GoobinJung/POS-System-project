package posUi;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import posDm.StoreDm;
import posPd.*;

final class Ui {
    private Ui() {}
    @FunctionalInterface interface Action { void run() throws Exception; }
    static void run(Component parent, Action action) {
        try { action.run(); }
        catch (Exception ex) {
            if (Boolean.getBoolean("pos.test")) throw new IllegalStateException("UI action failed: " + ex.getMessage(), ex);
            JOptionPane.showMessageDialog(parent, ex.getMessage() == null ? ex.toString() : ex.getMessage(),
                "Please check", JOptionPane.ERROR_MESSAGE);
        }
    }
    static JButton button(String text, Action action) {
        JButton button = new JButton(text);
        button.addActionListener(event -> run(button, action));
        return button;
    }
    static JPanel page(JPanel target, String title) {
        target.setLayout(new BorderLayout(10, 10));
        target.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel heading = new JLabel(title);
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 20f));
        target.add(heading, BorderLayout.NORTH);
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        target.add(body, BorderLayout.CENTER);
        return body;
    }
    static void row(JPanel body, Component... components) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        for (Component component : components) row.add(component);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
        body.add(row);
    }
    static void input(JPanel body, String label, JComponent field) { row(body, new JLabel(label), field); }
    static JTextField field(JPanel body, String label, String value) {
        JTextField field = new JTextField(value == null ? "" : value, 24);
        input(body, label, field);
        return field;
    }
    static String required(JTextField field, String label) {
        String value = field.getText().trim();
        if (value.isEmpty()) throw new IllegalArgumentException(label + " is required.");
        return value;
    }
    static BigDecimal money(JTextField field) { return Money.parse(field.getText()); }
    static LocalDate date(JTextField field) {
        try { return LocalDate.parse(field.getText().trim()); }
        catch (RuntimeException ex) { throw new IllegalArgumentException("Use a valid date in YYYY-MM-DD format."); }
    }
    static <T> T selected(JList<T> list) {
        T value = list.getSelectedValue();
        if (value == null) throw new IllegalArgumentException("Select a row first.");
        return value;
    }
    static void show(JFrame frame, JPanel panel) {
        frame.setContentPane(new JScrollPane(panel));
        frame.revalidate();
        frame.repaint();
    }
    static void save(Store store) throws IOException { StoreDm.save(store); }
    static PosFrame frame(JFrame frame) { return (PosFrame) frame; }
    static String text(String value) { return value == null ? "" : value; }
    static void unique(java.util.Map<String, ?> map, String key, Object current) {
        if (map.containsKey(key) && map.get(key) != current)
            throw new IllegalArgumentException("Identifier already exists: " + key);
    }
}
