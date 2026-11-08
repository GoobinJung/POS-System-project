package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;


import posPd.Sale;
import posPd.Session;
import posPd.Store;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EndSessionPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField numSalesTextField;
    private JTextField totalSalesTextField;
    private JTextField enterCashTextField;
    private JTextField cashCountDiffField;

    /**
     * Create the panel.
     */
    public EndSessionPanel(JFrame currentFrame, Store store, Session session, Sale sale) {
	setLayout(null);
	
	JLabel lblNewLabel = new JLabel("Session Summary");
	lblNewLabel.setBounds(190, 10, 96, 20);
	add(lblNewLabel);
	
	JLabel lblNewLabel_1 = new JLabel("Cashier :" + session.getCashier().toString());
	lblNewLabel_1.setBounds(29, 55, 129, 20);
	add(lblNewLabel_1);
	
	JLabel lblNewLabel_2 = new JLabel("Register : " + session.getRegister().toString());
	lblNewLabel_2.setBounds(29, 85, 110, 20);
	add(lblNewLabel_2);
	
	JLabel lblNewLabel_3 = new JLabel("Number Sales :");
	lblNewLabel_3.setBounds(29, 122, 96, 20);
	add(lblNewLabel_3);
	
	numSalesTextField = new JTextField(String.valueOf(session.getNumSales()));
	numSalesTextField.setBounds(123, 123, 96, 18);
	add(numSalesTextField);
	numSalesTextField.setColumns(10);
	
	JLabel lblNewLabel_4 = new JLabel("Total Sales : ");
	lblNewLabel_4.setBounds(39, 152, 76, 20);
	add(lblNewLabel_4);
	
	totalSalesTextField = new JTextField(String.format("%.2f",session.calcTotal()));
	totalSalesTextField.setBounds(123, 151, 96, 18);
	add(totalSalesTextField);
	totalSalesTextField.setColumns(10);
	
	JLabel lblNewLabel_5 = new JLabel("Enter Cash :");
	lblNewLabel_5.setBounds(39, 182, 76, 20);
	add(lblNewLabel_5);
	
	enterCashTextField = new JTextField();
	enterCashTextField.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    //store.setName(textField.getText());
		  //  session.setStartCash(enterCashTextField.getText());
		}
	});
	
	enterCashTextField.setBounds(123, 180, 96, 18);
	add(enterCashTextField);
	enterCashTextField.setColumns(10);
	
	JLabel lblNewLabel_6 = new JLabel("Cash Count Diff :");
	lblNewLabel_6.setBounds(20, 211, 105, 18);
	add(lblNewLabel_6);
	
	cashCountDiffField = new JTextField(String.format("%.2f",session.calcCashCountDiff()));
	cashCountDiffField.setBounds(123, 211, 96, 18);
	add(cashCountDiffField);
	cashCountDiffField.setColumns(10);
	
	JButton endSessionButton = new JButton("End Session");
	endSessionButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
	                currentFrame.getContentPane().add(new HomePanel (store));
	                currentFrame.getContentPane().revalidate();
		}
	});
	endSessionButton.setBounds(162, 251, 110, 20);
	add(endSessionButton);
	
    }

}
