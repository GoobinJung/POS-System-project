package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;



import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import posPd.Store;
import posPd.Register;

public class RegisterEditPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField numberField;
    private JTextField nameField;
 
    private Store store;
    private Register register;
    private boolean isNew;

    /**
     * Create the panel.
     */
    public RegisterEditPanel(JFrame currentFrame, Store store, Register register, boolean isNew) {
        this.store = store;
        this.register = register;
        this.isNew = isNew;
        
        
        
        setLayout(null);
        
        JLabel lblTitle = new JLabel("Edit Register");
        lblTitle.setBounds(180, 10, 100, 20);
        add(lblTitle);
        
        // Cashier Number
        JLabel lblNumber = new JLabel("Number:");
        lblNumber.setBounds(80, 39, 80, 20);
        add(lblNumber);
        
        numberField = new JTextField();
        numberField.setBounds(140, 40, 85, 20);
        if (!isNew) numberField.setText(register.getNumber());
       
        add(numberField);
        
      
        
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register.setNumber(numberField.getText());
                if(isNew) {
                    store.addRegister(register);
                }
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new RegisterListPanel(currentFrame, store));
                currentFrame.getContentPane().revalidate();
            }
        });
        btnSave.setBounds(91, 193, 80, 25);
        add(btnSave);
        
      
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new RegisterListPanel(currentFrame, store));
                currentFrame.getContentPane().revalidate();
            }
        });
        btnCancel.setBounds(200, 193, 80, 25);
        add(btnCancel);
    }
    
 
    
}