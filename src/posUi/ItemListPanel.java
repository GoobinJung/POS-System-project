package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;

import posPd.Item;
import posPd.Register;
import posPd.Store;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class ItemListPanel extends JPanel {

    DefaultListModel<Item> listModel;
    private static final long serialVersionUID = 1L;
    JButton editButton;
    JButton deleteButton;

    /**
     * Create the panel.
     */
    public ItemListPanel (JFrame currentFrame, Store store) {
        setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Item List");
        lblNewLabel.setBounds(171, 25, 69, 12);
        add(lblNewLabel);
        
        listModel = new DefaultListModel<Item>();
    	for(Item item : store.getItems().values()) {
    	    listModel.addElement(item);
    	}
    	
        JList<Item> list = new JList<Item>(listModel);
        list.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        	    editButton.setEnabled(true);
        		deleteButton.setEnabled(true);
        	}
        });
        list.setBounds(53, 87, 288, 99);
        add(list);
        
        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	    currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new ItemEditPanel(currentFrame, store, new Item(), true));
		    currentFrame.getContentPane().revalidate();
        	}
        	
        	
        });
        addBtn.setBounds(45, 212, 84, 20);
        add(addBtn);
        
        editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new ItemEditPanel(currentFrame, store, list.getSelectedValue(), false));
		    currentFrame.getContentPane().revalidate();
		}
	});
        editButton.setBounds(156, 212, 84, 20);
        editButton.setEnabled(false);
        add(editButton);
        
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    store.removeItem(list.getSelectedValue());
		    listModel.removeElement(list.getSelectedValue());
		}
	});
        deleteButton.setBounds(262, 212, 84, 20);
        deleteButton.setEnabled(false);
        add(deleteButton);
    }
}
