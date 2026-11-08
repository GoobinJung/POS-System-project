package posUi;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;

import posPd.Item;
import posPd.Sale;
import posPd.SaleLineItem;
import posPd.Session;
import posPd.Store;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PosSalePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField itemTextField;
    private JTextField quantityTextField;
    JList<SaleLineItem> list;
    DefaultListModel<SaleLineItem> listModel;
    private JTextField subTotalTextField;
    private JTextField taxTextField;
    private JTextField totalTextField;
    private JTextField amtTextField;
    private JTextField changeTextField;
    JLabel noItemFoundLable;
    JPanel currentPanel = this;

    /**
     * Create the panel.
     */
    public PosSalePanel(JFrame currentFrame, Store store, Session session, Sale sale) {
	setLayout(null);
	
	JLabel lblNewLabel = new JLabel("Sale");
	lblNewLabel.setBounds(227, 10, 44, 12);
	add(lblNewLabel);
		
	JLabel cashierLable = new JLabel("Cashier :" + session.getCashier().toString());
	cashierLable.setBounds(10, 36, 109, 12);
	add(cashierLable);
	
	JLabel registerLable = new JLabel("Register : " + session.getRegister().toString());
	registerLable.setBounds(10, 58, 100, 12);
	add(registerLable);
	
	JLabel itemLable = new JLabel("Item : ");
	itemLable.setBounds(10, 99, 44, 12);
	add(itemLable);
	
	itemTextField = new JTextField();
	itemTextField.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		 Item item = store.findItemForUPC(itemTextField.getText());
		 if(item != null) {
		     SaleLineItem sli = new SaleLineItem(sale, item, quantityTextField.getText());
		     listModel.addElement(sli);
		     
		     subTotalTextField.setText(String.format("%.2f", sale.calcSubTotal()));
		     taxTextField.setText(String.format("%.2f",sale.calcTax()));
		     totalTextField.setText(String.format("%.2f",sale.calcTotal()));
		     amtTextField.setText(String.format("%.2f",sale.calcAmtTendered()));
		     changeTextField.setText(String.format("%.2f",sale.calcChange()));
		     noItemFoundLable.setVisible(false);
		 }
		 else {
		     noItemFoundLable.setVisible(true);
		 }
		}
	});
	
	itemTextField.setBounds(43, 96, 96, 18);
	add(itemTextField);
	itemTextField.setColumns(10);
	
	JLabel quantityLable = new JLabel("Quantity :");
	quantityLable.setBounds(146, 99, 69, 12);
	add(quantityLable);
	
	quantityTextField = new JTextField("1");
	quantityTextField.setBounds(211, 96, 44, 18);
	add(quantityTextField);
	quantityTextField.setColumns(10);
	
	listModel = new DefaultListModel<SaleLineItem>();
	
	list = new JList<SaleLineItem>(listModel);
	list.setBounds(20, 121, 185, 83);
	add(list);
	
	JCheckBox texFreeCheckBox = new JCheckBox("Tax Free");
	texFreeCheckBox.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if(texFreeCheckBox.isSelected()) {
			sale.setTaxFree(true);
		    }
		    else {
			sale.setTaxFree(false);
		    }
		    taxTextField.setText(String.format("%.2f",sale.calcTax()));
		    totalTextField.setText(String.format("%.2f",sale.calcTotal()));
		    changeTextField.setText(String.format("%.2f",sale.calcChange()));
		    
		}
	});
	texFreeCheckBox.setBounds(315, 54, 92, 20);
	add(texFreeCheckBox);
	
	JLabel subTotalLable = new JLabel("SubTotal :");
	subTotalLable.setBounds(287, 99, 69, 12);
	add(subTotalLable);
	
	subTotalTextField = new JTextField(String.format("%.2f", sale.calcSubTotal()));
	subTotalTextField.setBounds(344, 96, 96, 18);
	add(subTotalTextField);
	subTotalTextField.setColumns(10);
	
	JLabel taxLable = new JLabel("Tax :");
	taxLable.setBounds(290, 135, 44, 12);
	add(taxLable);
	
	taxTextField = new JTextField(String.format("%.2f",sale.calcTax()));
	taxTextField.setBounds(344, 133, 96, 18);
	add(taxTextField);
	taxTextField.setColumns(10);
	
	JLabel totalLable = new JLabel("Total :");
	totalLable.setBounds(290, 166, 44, 12);
	add(totalLable);
	
	totalTextField = new JTextField(String.format("%.2f",sale.calcTotal()));
	totalTextField.setBounds(344, 163, 96, 18);
	add(totalTextField);
	totalTextField.setColumns(10);
	
	JLabel amtTendredLable = new JLabel("Amt Tendered :");
	amtTendredLable.setBounds(247, 203, 87, 12);
	add(amtTendredLable);
	
	amtTextField = new JTextField(String.format("%.2f",sale.calcAmtTendered()));
	amtTextField.setBounds(344, 200, 96, 18);
	add(amtTextField);
	amtTextField.setColumns(10);
	
	JLabel changeLable = new JLabel("Change :");
	changeLable.setBounds(277, 235, 57, 12);
	add(changeLable);
	
	changeTextField = new JTextField(String.format("%.2f",sale.calcChange()));
	changeTextField.setBounds(344, 232, 96, 18);
	add(changeTextField);
	changeTextField.setColumns(10);
	
	JButton paymentButton = new JButton("Payment");
	paymentButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
	                currentFrame.getContentPane().add(new PaymentPanel (currentFrame, currentPanel, store, session, sale));
	                currentFrame.getContentPane().revalidate();
		}
	});
	paymentButton.setBounds(10, 231, 109, 20);
	add(paymentButton);
	
	JButton completeSaleButton = new JButton("Complete Sale");
	completeSaleButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    session.addSale(sale);
		    currentFrame.getContentPane().removeAll();
	                currentFrame.getContentPane().add(new PosSalePanel (currentFrame, store, session, new Sale()));
	                currentFrame.getContentPane().revalidate();
		}
	});
	completeSaleButton.setBounds(129, 231, 121, 20);
	completeSaleButton.setEnabled(false);
	if(sale.isPaymentEnough()) { 
	    completeSaleButton.setEnabled(true);
	}
	add(completeSaleButton);
	
	JButton cancelSaleButton = new JButton("Cancel Sale");
	cancelSaleButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
	                currentFrame.getContentPane().add(new PosSalePanel (currentFrame, store, session, new Sale()));
	                currentFrame.getContentPane().revalidate();
		}
	});
	cancelSaleButton.setBounds(10, 261, 109, 20);
	add(cancelSaleButton);
	
	JButton endSessionButton = new JButton("End Session");
	endSessionButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
	                currentFrame.getContentPane().add(new EndSessionPanel (currentFrame, store, session, sale));
	                currentFrame.getContentPane().revalidate();
		    
		}
	});
	endSessionButton.setBounds(129, 261, 121, 20);
	
	endSessionButton.setEnabled(false);
	if(sale.isPaymentEnough()) { 
	    endSessionButton.setEnabled(true);
	}
	add(endSessionButton);
	
	noItemFoundLable = new JLabel("NO ITEM FOUND");
	noItemFoundLable.setBounds(68, 209, 92, 12);
	noItemFoundLable.setVisible(false);
	add(noItemFoundLable);
	

    }
}
