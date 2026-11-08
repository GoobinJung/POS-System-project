package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import posPd.Cashier;
import posPd.Register;
import posPd.Sale;
import posPd.Session;
import posPd.Store;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField StartingCashField;
    private JTextField passwordField;
    JComboBox <Cashier>cashierNumberComboBox;
    JComboBox registerNumberComboBox;
    JLabel invalidPasswordLable;
   

    /**
     * Create the panel.
     */
    public LoginPanel(JFrame currentFrame, Store store) {
	setLayout(null);
	
	JLabel lblNewLabel = new JLabel("Login");
	lblNewLabel.setBounds(196, 10, 44, 12);
	add(lblNewLabel);
	
	JLabel lblNewLabel_1 = new JLabel("Cashier Number");
	lblNewLabel_1.setBounds(111, 64, 74, 12);
	add(lblNewLabel_1);
	
	JLabel lblNewLabel_2 = new JLabel("Register Number");
	lblNewLabel_2.setBounds(108, 96, 77, 12);
	add(lblNewLabel_2);
	
	JLabel lblNewLabel_3 = new JLabel("Starting Cash");
	lblNewLabel_3.setBounds(108, 124, 77, 12);
	add(lblNewLabel_3);
	
	JLabel lblNewLabel_4 = new JLabel("Password");
	lblNewLabel_4.setBounds(111, 152, 74, 12);
	add(lblNewLabel_4);
	
	DefaultComboBoxModel<Cashier> cashierList = new DefaultComboBoxModel<Cashier>();
	for (Cashier cashier : store.getCashiers().values()) {
            cashierList.addElement(cashier);
        }
	
	cashierNumberComboBox = new JComboBox<Cashier>(cashierList);
	cashierNumberComboBox.setBounds(213, 60, 84, 20);
	add(cashierNumberComboBox);
	
	registerNumberComboBox = new JComboBox();
	registerNumberComboBox.setBounds(213, 92, 84, 20);
	for (Register register : store.getRegisters().values()) {
            registerNumberComboBox.addItem(register);
        }
	add(registerNumberComboBox);
	
	StartingCashField = new JTextField();
	StartingCashField.setBounds(213, 121, 84, 18);
	add(StartingCashField);
	StartingCashField.setColumns(10);
	
	passwordField = new JPasswordField();
	passwordField.setBounds(213, 149, 84, 18);
	add(passwordField);
	passwordField.setColumns(10);
	
	JButton loginButton = new JButton("Login");
	loginButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    String password = passwordField.getText();
		    Cashier cashier = (Cashier) cashierNumberComboBox.getSelectedItem();
		    if(cashier.isAuthorized(password)) {
			//System.out.println(cashier.toString());
			Session session = new Session(store, cashier,(Register)registerNumberComboBox.getSelectedItem());
			//System.out.println(session.getCashier().toString());
			BigDecimal startingCash = new BigDecimal(StartingCashField.getText());
			session.setStartCash(startingCash);
			store.addSession(session);
			currentFrame.getContentPane().removeAll();
			currentFrame.getContentPane().add(new PosSalePanel(currentFrame, store, session, new Sale()));
			currentFrame.getContentPane().revalidate();
		    }
		    else {
			invalidPasswordLable.setVisible(true);
		    }
		    
		}
	});
	loginButton.setBounds(101, 210, 84, 20);
	add(loginButton);
	
	JButton cancelButton = new JButton("Cancel");
	cancelButton.setBounds(230, 210, 84, 20);
	add(cancelButton);
	
	invalidPasswordLable = new JLabel("Invalid password");
	invalidPasswordLable.setBounds(324, 214, 116, 12);
	invalidPasswordLable.setVisible(false);
	add(invalidPasswordLable);
    }
}
