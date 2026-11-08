package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import posPd.Store;
import posPd.Item;
import posPd.Price;
import posPd.PromoPrice;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.swing.JCheckBox;

public class PriceEditPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField priceField;
    private JTextField dateField;
    private JTextField endDateField;
    private JLabel lblEndDate;
   

    /**
     * Create the panel.
     */
    public PriceEditPanel(JFrame currentFrame, Store store, Item item, Price price, boolean isNew) {
        
        
        setLayout(null);
        
        JLabel lblTitle = new JLabel("Edit Price");
        lblTitle.setBounds(180, 0, 100, 20);
        add(lblTitle);
        
        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setBounds(50, 50, 80, 20);
        add(lblPrice);
        
        priceField = new JTextField();
        priceField.setBounds(140, 50, 150, 20);
        if (!isNew) priceField.setText(price.getPrice().toString());
        add(priceField);
        
        JLabel lblDate = new JLabel("Effective Date:");
        lblDate.setBounds(50, 80, 86, 20);
        add(lblDate);
        
        dateField = new JTextField();
        dateField.setBounds(140, 80, 150, 20);
        if (!isNew) dateField.setText(price.getEffectiveDate().toString());
        add(dateField);
        
        
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isNew) {
                    Price newPrice = new Price(item, priceField.getText(), dateField.getText());
                    item.addPrice(newPrice);
                } else {
                    
                    price.setPrice(new BigDecimal(priceField.getText()));
                    price.setEffectiveDate(LocalDate.parse(dateField.getText()));
                    
                }
                
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new ItemEditPanel(currentFrame, store, item, false));
                currentFrame.getContentPane().revalidate();
            }
        });
        btnSave.setBounds(100, 150, 80, 25);
        add(btnSave);
        
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new ItemEditPanel(currentFrame, store, item, false));
                currentFrame.getContentPane().revalidate();
            }
        });
        btnCancel.setBounds(200, 150, 80, 25);
        add(btnCancel);
        
        JCheckBox promoPriceCheckBox = new JCheckBox("Promo Price");
        promoPriceCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblEndDate.setVisible(promoPriceCheckBox.isSelected());
                endDateField.setVisible(promoPriceCheckBox.isSelected());
            }
        });
        promoPriceCheckBox.setBounds(155, 25, 106, 20);
        add(promoPriceCheckBox);
    

        lblEndDate = new JLabel("End Date:");
        lblEndDate.setBounds(50, 120, 80, 20);
        lblEndDate.setVisible(false);
        add(lblEndDate);
    
        endDateField = new JTextField();
        endDateField.setBounds(140, 120, 150, 20);
        endDateField.setVisible(false);
        add(endDateField);
    
}
}