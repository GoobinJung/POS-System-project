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
import javax.swing.JList;

public class TaxCategoryEditPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField categoryField;
    DefaultListModel<TaxRate> listModel;
    JList<TaxRate> list;
 
    
    JButton editButton;
    JButton deleteButton;
    JButton addTaxRateButton;
    
    /**
     * Create the panel.
     */
    public TaxCategoryEditPanel(JFrame currentFrame, Store store, TaxCategory taxcategory, boolean isNew) {
        
        
        setLayout(null);
        
        JLabel lblTitle = new JLabel("Edit Tax Category");
        lblTitle.setBounds(180, 10, 131, 20);
        add(lblTitle);
        
        JLabel lblNumber = new JLabel("Category:");
        lblNumber.setBounds(27, 79, 80, 20);
        add(lblNumber);
        
        categoryField = new JTextField();
        categoryField.setBounds(91, 80, 85, 20);
        categoryField.setText(taxcategory.getCategory());
        add(categoryField);
        
        
        JLabel lblTaxRates = new JLabel("Tax Rates:");
        lblTaxRates.setBounds(27, 120, 80, 20);
        add(lblTaxRates);
        
        listModel = new DefaultListModel<TaxRate>();
        
            for (TaxRate taxRate : taxcategory.getTaxRates()) {
                listModel.addElement(taxRate);
        
        }
        
        list = new JList<TaxRate>(listModel);
        list.setBounds(91, 120, 150, 80);
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        });
        add(list);
        
        
        addTaxRateButton = new JButton("Add Tax Rate");
        addTaxRateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		taxcategory.setTaxCategory(categoryField.getText());
                
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new TaxRateEditPanel(currentFrame, store, taxcategory, isNew,  new TaxRate(), true));
                currentFrame.getContentPane().revalidate();
            }
        });
        addTaxRateButton.setBounds(250, 120, 120, 25);
        add(addTaxRateButton);
        
        
        editButton = new JButton("Edit Tax Rate");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		taxcategory.setTaxCategory(categoryField.getText());
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new TaxRateEditPanel(currentFrame, store, taxcategory, isNew, list.getSelectedValue(), false));
                currentFrame.getContentPane().revalidate();
            }
        });
        editButton.setBounds(250, 150, 120, 25);
        editButton.setEnabled(false);
        add(editButton);
        
        deleteButton = new JButton("Delete Tax Rate");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taxcategory.removeTaxRate(list.getSelectedValue());
                listModel.removeElement(list.getSelectedValue());
            }
        });
        deleteButton.setBounds(250, 180, 120, 25);
        deleteButton.setEnabled(false);
        add(deleteButton);
        
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taxcategory.setTaxCategory(categoryField.getText());
                if(isNew) {
                    store.addTaxCategory(taxcategory);
                }
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new TaxCategoryListPanel(currentFrame, store));
                currentFrame.getContentPane().revalidate();
            }
        });
        btnSave.setBounds(91, 217, 80, 25);
        add(btnSave);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new TaxCategoryListPanel(currentFrame, store));
                currentFrame.getContentPane().revalidate();
            }
        });
        btnCancel.setBounds(200, 217, 80, 25);
        add(btnCancel);
    }
}