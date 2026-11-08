package posUi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import posPd.TaxCategory;
import posPd.Store;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class TaxCategoryListPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    DefaultListModel<TaxCategory> listModel;
    JButton editButton;
    JButton deleteButton;

    /**
     * Create the panel.
     */
    public TaxCategoryListPanel(JFrame currentFrame, Store store) {
         setLayout(null);
    	
    	
    	listModel = new DefaultListModel<TaxCategory>();
    	for(TaxCategory taxcategory : store.getTaxCategories().values()) {
    	    listModel.addElement(taxcategory);
    	}
    	
    	JList<TaxCategory> list = new JList<TaxCategory>(listModel);
    	list.addListSelectionListener(new ListSelectionListener() {
    		public void valueChanged(ListSelectionEvent e) {
    		editButton.setEnabled(true);
    		deleteButton.setEnabled(true);
    		
    		
    		}
    	});
    	list.setBounds(74, 91, 101, 82);
    	add(list);
    	
    	JLabel lblNewLabel = new JLabel("TaxCategoty List");
    	lblNewLabel.setBounds(196, 30, 101, 12);
    	add(lblNewLabel);
    	
    	JButton addButton = new JButton("Add");
    	addButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new TaxCategoryEditPanel(currentFrame, store, new TaxCategory(), true));
		    currentFrame.getContentPane().revalidate();
		
    		}
    	});
    	addButton.setBounds(45, 216, 84, 20);
    	add(addButton);
    	
    	editButton = new JButton("Edit");
    	editButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new TaxCategoryEditPanel(currentFrame, store, list.getSelectedValue(), false));
		    currentFrame.getContentPane().revalidate();
    		}
    	});
    	editButton.setBounds(168, 216, 84, 20);
    	editButton.setEnabled(false);
    	add(editButton);
    	
    	deleteButton = new JButton("Delete");
    	deleteButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		    store.removeTaxCategory(list.getSelectedValue());
    		    listModel.removeElement(list.getSelectedValue());
    		}
    	});
    	deleteButton.setBounds(301, 216, 84, 20);
    	deleteButton.setEnabled(false);
    	add(deleteButton);

    }

}
