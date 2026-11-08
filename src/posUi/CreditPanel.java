package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;


import posPd.Credit;
import posPd.Sale;
import posPd.Session;
import posPd.Store;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.awt.event.ActionEvent;

public class CreditPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField amountTextField;
    private JTextField accountNumTextField;
    private JTextField expireDateTextField;
    private JTextField cardTypeTextField;

    /**
     * Create the panel.
     */
    public CreditPanel(JFrame currentFrame, JPanel currentPanel, Store store, Session session, Sale sale, Credit credit) {
	setLayout(null);
	
	JLabel lblNewLabel = new JLabel("Enter Credit Payment");
	lblNewLabel.setBounds(40, 10, 136, 13);
	add(lblNewLabel);
	
	JLabel amountLable = new JLabel("Amount :");
	amountLable.setBounds(40, 30, 60, 18);
	add(amountLable);
	
	amountTextField = new JTextField();
	amountTextField.setBounds(100, 30, 96, 18);
	add(amountTextField);
	amountTextField.setColumns(10);
	
	JLabel cardTypeLable = new JLabel("Card Type :");
	cardTypeLable.setBounds(29, 53, 71, 18);
	add(cardTypeLable);
	
	cardTypeTextField = new JTextField();
	cardTypeTextField.setBounds(100, 52, 96, 20);
	add(cardTypeTextField);
	cardTypeTextField.setColumns(10);
	
	JLabel accountNumLable = new JLabel("Account Number :");
	accountNumLable.setBounds(0, 79, 82, 18);
	add(accountNumLable);
	
	accountNumTextField = new JTextField();
	accountNumTextField.setBounds(100, 79, 96, 18);
	add(accountNumTextField);
	accountNumTextField.setColumns(10);
	
	JLabel expireDateLable = new JLabel("Expire date :");
	expireDateLable.setBounds(29, 104, 71, 18);
	add(expireDateLable);
	
	expireDateTextField = new JTextField();
	expireDateTextField.setBounds(100, 104, 96, 18);
	add(expireDateTextField);
	expireDateTextField.setColumns(10);
	
	JButton saveButton = new JButton("Save");
	saveButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    credit.setAmtTendered(new BigDecimal(amountTextField.getText()));
		    credit.setCardType(cardTypeTextField.getText());
		    credit.setAcctNumber(accountNumTextField.getText());
		    credit.setExpireDate(LocalDate.parse(expireDateTextField.getText()));
		    credit.setAmount(sale.calcTotal());
		    sale.addPayment(credit);
		    
		    currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new PaymentPanel(currentFrame, currentPanel, store, session, sale));
		    currentFrame.getContentPane().revalidate();
		}
	});
	saveButton.setBounds(16, 142, 84, 20);
	add(saveButton);
	
	JButton cancelButton = new JButton("Cancel");
	cancelButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
	                currentFrame.getContentPane().add(new PaymentPanel (currentFrame, currentPanel, store, session, sale));
	                currentFrame.getContentPane().revalidate();
		}
	});
	cancelButton.setBounds(110, 142, 84, 20);
	add(cancelButton);

    }

}
