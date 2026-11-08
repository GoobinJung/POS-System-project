package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;

import posPd.Cash;
import posPd.Check;
import posPd.Credit;
import posPd.Sale;
import posPd.Session;
import posPd.Store;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PaymentPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField paymentDueTextField;
    private JTextField amountTenderedTextField;
    private JPanel cashPanel = null;
    private JPanel checkPanel = null;
    private JPanel creditPanel = null;

    /**
     * Create the panel.
     */
    public PaymentPanel(JFrame currentFrame, JPanel currentPanel, Store store, Session session, Sale sale ) {
	setLayout(null);
	
	JLabel lblNewLabel = new JLabel("Payment Due :");
	lblNewLabel.setBounds(32, 38, 96, 12);
	add(lblNewLabel);
	
	paymentDueTextField = new JTextField(String.format("%.2f",sale.calcAmountForPayment()));
	paymentDueTextField.setBounds(32, 60, 96, 18);
	add(paymentDueTextField);
	paymentDueTextField.setColumns(10);
	
	JLabel lblNewLabel_1 = new JLabel("Amount Tendered :");
	lblNewLabel_1.setBounds(32, 96, 107, 12);
	add(lblNewLabel_1);
	
	amountTenderedTextField = new JTextField(String.format("%.2f",sale.calcAmtTendered()));
	amountTenderedTextField.setBounds(32, 118, 96, 18);
	add(amountTenderedTextField);
	amountTenderedTextField.setColumns(10);
	
	JButton cashButton = new JButton("Cash");
	cashButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    cashPanel = new CashPanel(currentFrame, currentPanel, store, session, sale, new Cash());
		    cashPanel.setBounds(138, 60, 271, 181);
		    if(checkPanel != null) {
			remove(checkPanel);
			checkPanel = null;
		    }
		    if(creditPanel != null) {
			remove(creditPanel);
			creditPanel = null;
		    }
		    add(cashPanel);
		    currentFrame.getContentPane().revalidate();
		    currentFrame.getContentPane().repaint();
		}
	});
	cashButton.setBounds(32, 169, 84, 20);
	add(cashButton);
	
	JButton checkButton = new JButton("Check");
	checkButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    checkPanel = new CheckPanel(currentFrame, currentPanel, store, session, sale, new Check());
		    checkPanel.setBounds(138, 60, 271, 181);
		    if(cashPanel != null) {
			remove(cashPanel);
			cashPanel = null;
		    }
		    if(creditPanel != null) {
			remove(creditPanel);
			creditPanel = null;
		    }
		    add(checkPanel);
		    currentFrame.getContentPane().revalidate();
		    currentFrame.getContentPane().repaint();
		}
	});
	checkButton.setBounds(32, 205, 84, 20);
	add(checkButton);
	
	JButton creditButton = new JButton("Credit");
	creditButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    creditPanel = new CreditPanel(currentFrame, currentPanel, store, session, sale, new Credit());
		    creditPanel.setBounds(138, 60, 271, 181);
		    if(cashPanel != null) {
			remove(cashPanel);
			cashPanel = null;
		    }
		    if(checkPanel != null) {
			remove(checkPanel);
			checkPanel = null;
		    }
		    add(creditPanel);
		    currentFrame.getContentPane().revalidate();
		    currentFrame.getContentPane().repaint();
		    
		}
	});
	creditButton.setBounds(32, 240, 84, 20);
	add(creditButton);
	
	JLabel lblNewLabel_2 = new JLabel("Payment");
	lblNewLabel_2.setBounds(197, 21, 69, 18);
	add(lblNewLabel_2);
	
	//JPanel panel = new JPanel();
	//panel.setBounds(138, 60, 271, 181);
	//add(panel);
	
	
	
	JButton paymentCompleteButton = new JButton("Payment Complete");
	paymentCompleteButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    session.addSale(sale);
		    currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new PosSalePanel(currentFrame, store, session, sale));
		    currentFrame.getContentPane().revalidate();
		}
	});
	paymentCompleteButton.setBounds(86, 270, 145, 20);
	paymentCompleteButton.setEnabled(false);
	if(sale.isPaymentEnough()) { 
	    paymentCompleteButton.setEnabled(true);
	}
	add(paymentCompleteButton);
	
	JButton cancelButton = new JButton("Cancel");
	cancelButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new PosSalePanel(currentFrame, store, session, sale));
		    currentFrame.getContentPane().revalidate();
		}
	});
	cancelButton.setBounds(240, 270, 145, 20);
	
	add(cancelButton);
	

    }

}
