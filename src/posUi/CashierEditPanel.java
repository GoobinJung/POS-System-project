package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import posPd.Store;
import posPd.Cashier;
import posPd.Person;

public class CashierEditPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField numberField;
    private JTextField nameField;
    private JTextField ssnField;
    private JTextField addressField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField zipField;
    private JTextField phoneField;
    private JTextField passwordField;
    

    /**
     * Create the panel.
     */
    public CashierEditPanel(JFrame currentFrame, Store store, Cashier cashier, boolean isNew) {
        
        
        setLayout(null);
        
        JLabel lblTitle = new JLabel("Edit Cashier");
        lblTitle.setBounds(180, 10, 100, 20);
        add(lblTitle);
        
        JLabel lblNumber = new JLabel("Number:");
        lblNumber.setBounds(50, 40, 80, 20);
        add(lblNumber);
        
        numberField = new JTextField();
        numberField.setBounds(140, 40, 150, 20);
        if (!isNew) numberField.setText(cashier.getNumber());
        add(numberField);
        
        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 70, 80, 20);
        add(lblName);
        
        nameField = new JTextField();
        nameField.setBounds(140, 70, 150, 20);
        if (!isNew) nameField.setText(cashier.getPerson().getName());
        add(nameField);
        
        JLabel lblSsn = new JLabel("SSN:");
        lblSsn.setBounds(50, 100, 80, 20);
        add(lblSsn);
        
        ssnField = new JTextField();
        ssnField.setBounds(140, 100, 150, 20);
        if (!isNew) ssnField.setText(cashier.getPerson().getSSN());
        add(ssnField);
        
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(50, 130, 80, 20);
        add(lblAddress);
        
        addressField = new JTextField();
        addressField.setBounds(140, 130, 150, 20);
        if (!isNew) addressField.setText(cashier.getPerson().getAddress());
        add(addressField);
        
        JLabel lblCity = new JLabel("City:");
        lblCity.setBounds(50, 160, 80, 20);
        add(lblCity);
        
        cityField = new JTextField();
        cityField.setBounds(140, 160, 150, 20);
        if (!isNew) cityField.setText(cashier.getPerson().getCity());
        add(cityField);
        
        JLabel lblState = new JLabel("State:");
        lblState.setBounds(50, 190, 80, 20);
        add(lblState);
        
        stateField = new JTextField();
        stateField.setBounds(140, 190, 150, 20);
        if (!isNew) stateField.setText(cashier.getPerson().getState());
        add(stateField);
        
        JLabel lblZip = new JLabel("ZIP:");
        lblZip.setBounds(50, 220, 80, 20);
        add(lblZip);
        
        zipField = new JTextField();
        zipField.setBounds(140, 220, 150, 20);
        if (!isNew) zipField.setText(cashier.getPerson().getZip());
        add(zipField);
        
        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(50, 250, 80, 20);
        add(lblPhone);
        
        phoneField = new JTextField();
        phoneField.setBounds(140, 250, 150, 20);
        if (!isNew) phoneField.setText(cashier.getPerson().getPhone());
        add(phoneField);
        
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 280, 80, 20);
        add(lblPassword);
        
        passwordField = new JTextField();
        passwordField.setBounds(140, 280, 150, 20);
        if (!isNew) passwordField.setText(cashier.getPassword());
        add(passwordField);
        
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cashier.setNumber(numberField.getText());
                cashier.getPerson().setName(nameField.getText());
                cashier.getPerson().setSSN(ssnField.getText());
                cashier.getPerson().setAddress(addressField.getText());
                cashier.getPerson().setCity(cityField.getText());
                cashier.getPerson().setState(stateField.getText());
                cashier.getPerson().setZip(zipField.getText());
                cashier.getPerson().setPhone(phoneField.getText());
                cashier.setPassword(passwordField.getText());
                
                if(isNew) {
                    store.addCashier(cashier);
                }
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new CashierListPanel(currentFrame, store));
                currentFrame.getContentPane().revalidate();
            }
        });
        btnSave.setBounds(100, 320, 80, 25);
        add(btnSave);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new CashierListPanel(currentFrame, store));
                currentFrame.getContentPane().revalidate();
            }
        });
        btnCancel.setBounds(200, 320, 80, 25);
        add(btnCancel);
    }
}