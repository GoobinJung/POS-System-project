package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;

import posPd.Cash;
import posPd.Sale;
import posPd.Session;
import posPd.Store;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.awt.Color;
import java.awt.event.ActionEvent;

public class CashPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField textField;

    /**
     * Create the panel.
     */
    public CashPanel(JFrame currentFrame, JPanel currentPanel, Store store, Session session, Sale sale, Cash cash  ) {

	setLayout(null);
	
	//setBackground(Color.green);
	
	JLabel lblNewLabel = new JLabel("Enter cash payment");
	lblNewLabel.setBounds(55, 10, 111, 12);
	add(lblNewLabel);
	
	JLabel lblNewLabel_1 = new JLabel("Amount tendered :");
	lblNewLabel_1.setBounds(10, 62, 103, 18);
	add(lblNewLabel_1);
	
	textField = new JTextField();
	textField.setBounds(124, 62, 96, 18);
	add(textField);
	textField.setColumns(10);
	
	JButton saveButton = new JButton("Save");
	saveButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    cash.setAmtTendered(new BigDecimal (textField.getText()));
		    cash.setAmount(sale.calcTotal());
		    sale.addPayment(cash);
		    
		    currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new PaymentPanel(currentFrame, currentPanel, store, session, sale));
		    currentFrame.getContentPane().revalidate();
		}
	});
	saveButton.setBounds(16, 97, 84, 20);
	add(saveButton);
	
	JButton cancelButton = new JButton("Cancel");
	cancelButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
	                currentFrame.getContentPane().add(new PaymentPanel (currentFrame, currentPanel, store, session, sale));
	                currentFrame.getContentPane().revalidate();
		}
	});
	cancelButton.setBounds(122, 97, 84, 20);
	add(cancelButton);
	
	
    }

}
