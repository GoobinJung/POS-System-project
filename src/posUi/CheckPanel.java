package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;


import posPd.Check;
import posPd.Sale;
import posPd.Session;
import posPd.Store;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.awt.event.ActionEvent;

public class CheckPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField amountTextField;
    private JTextField routingNumTextField;
    private JTextField accountNumTextField;
    private JTextField checkNumTextField;

    /**
     * Create the panel.
     */
    public CheckPanel(JFrame currentFrame, JPanel currentPanel, Store store, Session session, Sale sale, Check check) {
	setLayout(null);
	
	JLabel lblNewLabel = new JLabel("Enter Check");
	lblNewLabel.setBounds(60, 0, 74, 24);
	add(lblNewLabel);
	
	JLabel lblNewLabel_1 = new JLabel("Amount : ");
	lblNewLabel_1.setBounds(46, 26, 52, 24);
	add(lblNewLabel_1);
	
	amountTextField = new JTextField();
	amountTextField.setBounds(109, 29, 96, 18);
	add(amountTextField);
	amountTextField.setColumns(10);
	
	JLabel routingNumLable = new JLabel("Routing number :");
	routingNumLable.setBounds(10, 54, 97, 24);
	add(routingNumLable);
	
	routingNumTextField = new JTextField();
	routingNumTextField.setBounds(109, 57, 96, 18);
	add(routingNumTextField);
	routingNumTextField.setColumns(10);
	
	JLabel accountNumLable = new JLabel("Account Number :");
	accountNumLable.setBounds(10, 88, 96, 18);
	add(accountNumLable);
	
	accountNumTextField = new JTextField();
	accountNumTextField.setBounds(109, 88, 96, 18);
	add(accountNumTextField);
	accountNumTextField.setColumns(10);
	
	JLabel checkNumLable = new JLabel("Check Number :");
	checkNumLable.setBounds(20, 116, 84, 18);
	add(checkNumLable);
	
	checkNumTextField = new JTextField();
	checkNumTextField.setBounds(109, 116, 96, 18);
	add(checkNumTextField);
	checkNumTextField.setColumns(10);
	
	JButton saveButton = new JButton("Save");
	saveButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    check.setAmtTendered(new BigDecimal(amountTextField.getText()));
		    check.setRoutingNumber(routingNumTextField.getText());
		    check.setAccountNumber(accountNumTextField.getText());
		    check.setCheckNumber(checkNumTextField.getText());
		    check.setAmount(sale.calcTotal());
		    sale.addPayment(check);
		    
		    currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new PaymentPanel(currentFrame, currentPanel, store, session, sale));
		    currentFrame.getContentPane().revalidate();
		    
		    
		}
	});
	saveButton.setBounds(14, 152, 84, 20);
	add(saveButton);
	
	JButton cancelButton = new JButton("Cancel");
	cancelButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
	                currentFrame.getContentPane().add(new PaymentPanel (currentFrame, currentPanel, store, session, sale));
	                currentFrame.getContentPane().revalidate();
		}
	});
	cancelButton.setBounds(109, 152, 84, 20);
	add(cancelButton);
	

    }

}
