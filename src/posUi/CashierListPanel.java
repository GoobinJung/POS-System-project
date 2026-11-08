package posUi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import posPd.Cashier;
import posPd.Store;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class CashierListPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    DefaultListModel<Cashier> listModel;
    JButton editButton;
    JButton deleteButton;

    /**
     * Create the panel.
     */
    public CashierListPanel(JFrame currentFrame, Store store) {
         setLayout(null);
    	
    	listModel = new DefaultListModel<Cashier>();
    	for(Cashier cashier : store.getCashiers().values()) {
    	    listModel.addElement(cashier);
    	}
    	
    	JList<Cashier> list = new JList<Cashier>(listModel);
    	list.addListSelectionListener(new ListSelectionListener() {
    		public void valueChanged(ListSelectionEvent e) {
    		editButton.setEnabled(true);
    		deleteButton.setEnabled(true);
    		}
    	});
    	list.setBounds(74, 91, 101, 82);
    	add(list);
    	
    	JLabel lblNewLabel = new JLabel("Cashier List");
    	lblNewLabel.setBounds(196, 30, 76, 12);
    	add(lblNewLabel);
    	
    	JButton addButton = new JButton("Add");
    	addButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new CashierEditPanel(currentFrame, store, new Cashier(), true));
		    currentFrame.getContentPane().revalidate();
    		}
    	});
    	addButton.setBounds(45, 216, 84, 20);
    	add(addButton);
    	
    	editButton = new JButton("Edit");
    	editButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new CashierEditPanel(currentFrame, store, list.getSelectedValue(), false));
		    currentFrame.getContentPane().revalidate();
    		}
    	});
    	editButton.setBounds(168, 216, 84, 20);
    	editButton.setEnabled(false);
    	add(editButton);
    	
    	deleteButton = new JButton("Delete");
    	deleteButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		    store.removeCashier(list.getSelectedValue());
    		    listModel.removeElement(list.getSelectedValue());
    		}
    	});
    	deleteButton.setBounds(301, 216, 84, 20);
    	deleteButton.setEnabled(false);
    	add(deleteButton);
    }
}