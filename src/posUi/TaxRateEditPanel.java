package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import posPd.Store;
import posPd.TaxCategory;
import posPd.TaxRate;
import posPd.Register;
import javax.swing.JList;
import java.math.BigDecimal;
import java.time.LocalDate;


public class TaxRateEditPanel extends JPanel {
	

    private static final long serialVersionUID = 1L;
    private JTextField taxRateField;
    private JTextField dateField;
  
 

    JButton editButton;
    JButton deleteButton;
  
    /**
     * Create the panel.
     */
    public TaxRateEditPanel(JFrame currentFrame, Store store, TaxCategory taxCategory, boolean isNewCategory, TaxRate taxrate, boolean isNew) {
        
        setLayout(null);
        
        JLabel lblTitle = new JLabel("Edit Tax Rate");
        lblTitle.setBounds(180, 10, 131, 20);
        add(lblTitle);
        
        JLabel lblNumber = new JLabel("Rate:");
        lblNumber.setBounds(30, 79, 80, 20);
        add(lblNumber);
        
        taxRateField = new JTextField();
        taxRateField.setBounds(91, 80, 85, 20);
        if (!isNew) taxRateField.setText(taxrate.getTaxRate().toString());
        add(taxRateField);
        
        JLabel lblNewLabel = new JLabel("Date:");
        lblNewLabel.setBounds(30, 128, 44, 12);
        add(lblNewLabel);
        
        dateField = new JTextField();
        dateField.setBounds(91, 125, 85, 18);
        if (!isNew) dateField.setText(taxrate.getEffectiveDate().toString());
        add(dateField);
        dateField.setColumns(10);
        
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
        		taxrate.setTaxRate(new BigDecimal(taxRateField.getText()));
                 taxrate.setEffectiveDate(LocalDate.parse(dateField.getText()));
                 if(isNew) {
                     taxCategory.addTaxRate(taxrate);
                 }
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new TaxCategoryEditPanel(currentFrame, store, taxCategory, isNewCategory));
                currentFrame.getContentPane().revalidate();
            }
        });
        btnSave.setBounds(91, 217, 80, 25);
        add(btnSave);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new TaxCategoryEditPanel(currentFrame, store, taxCategory, isNewCategory));
                currentFrame.getContentPane().revalidate();
            }
        });
        btnCancel.setBounds(200, 217, 80, 25);
        add(btnCancel);
    }
}