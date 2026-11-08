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
import posPd.UPC;

public class UpcEditPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField upcField;
    private Store store;
    private Item item;
    private UPC upc;
    private boolean isNew;

    /**
     * Create the panel.
     */
    public UpcEditPanel(JFrame currentFrame, Store store, Item item, UPC upc, boolean isNew) {
        this.store = store;
        this.item = item;
        this.upc = upc;
        this.isNew = isNew;
        
        setLayout(null);
        
        JLabel lblTitle = new JLabel("Edit UPC");
        lblTitle.setBounds(180, 10, 100, 20);
        add(lblTitle);
        
        JLabel lblUPC = new JLabel("UPC Code:");
        lblUPC.setBounds(50, 50, 80, 20);
        add(lblUPC);
        
        upcField = new JTextField();
        upcField.setBounds(140, 50, 150, 20);
        if (!isNew) upcField.setText(upc.getUpc());
        add(upcField);
        
        
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		
                if (isNew) {
                    UPC newUPC = new UPC(upcField.getText(), item);
                    store.addUPC(newUPC);  
                } else {
                    
                    upc.setUpc(upcField.getText());
                }
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new ItemEditPanel(currentFrame, store, item, false));
                currentFrame.getContentPane().revalidate();
            }
            }) ;
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
    }
}