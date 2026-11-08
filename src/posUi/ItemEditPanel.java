package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;

import posPd.Item;
import posPd.Price;
import posPd.Store;
import posPd.TaxCategory;
import posPd.UPC;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JMenu;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ItemEditPanel extends JPanel {
    
   
    DefaultListModel<UPC> upcListModel;
    DefaultListModel<Price> priceListModel;
    JButton upcAddButton;
    JButton upcEditButton;
    JButton upcDeleteButton;
    JButton priceAddButton;
    JButton priceEditButton;
    JButton priceDeleteButton;

    private static final long serialVersionUID = 1L;
  
    
    private JTextField descriptionField;
    private JTextField numberField;
    private JComboBox taxCategoryComboBox;

    /**
     * Create the panel.
     */
    public ItemEditPanel(JFrame currentFrame, Store store, Item item, boolean isNew) {
	
	
	setLayout(null);
	
	JLabel lblNewLabel = new JLabel("Edit Item");
	lblNewLabel.setBounds(192, 10, 44, 12);
	add(lblNewLabel);
	
	JLabel lblNewLabel_1 = new JLabel("Item Number:");
	lblNewLabel_1.setBounds(10, 66, 82, 12);
	add(lblNewLabel_1);
	
	JLabel lblNewLabel_2 = new JLabel("Description:");
	lblNewLabel_2.setBounds(10, 98, 82, 12);
	add(lblNewLabel_2);
	
	JLabel lblNewLabel_3 = new JLabel("Tax Category:");
	lblNewLabel_3.setBounds(10, 123, 82, 18);
	add(lblNewLabel_3);
	
	numberField = new JTextField();
	numberField.setBounds(95, 63, 96, 18);
	if (!isNew) numberField.setText(item.getNumber());
	add(numberField);
	numberField.setColumns(10);
	
	descriptionField = new JTextField();
	descriptionField.setBounds(95, 95, 96, 18);
	if (!isNew) descriptionField.setText(item.getDescription());
	add(descriptionField);
	descriptionField.setColumns(10);
	
	taxCategoryComboBox = new JComboBox();
	taxCategoryComboBox.setBounds(95, 122, 96, 20);
	for (TaxCategory taxCat : store.getTaxCategories().values()) {
            taxCategoryComboBox.addItem(taxCat);
        }
        if (!isNew) {
            taxCategoryComboBox.setSelectedItem(item.getTaxCategory());
        }
	add(taxCategoryComboBox);
	
	
	
	JButton saveButton = new JButton("Save");
	saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                item.setNumber(numberField.getText());
                item.setDescription(descriptionField.getText());
                item.setTaxCategory((TaxCategory) taxCategoryComboBox.getSelectedItem());
                if(isNew) {
                    store.addItem(item);
                }
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new ItemListPanel(currentFrame, store));
                currentFrame.getContentPane().revalidate();
            }
        });
	saveButton.setBounds(20, 222, 84, 20);
	add(saveButton);
	
	JButton cancelButton = new JButton("Cancel");
	cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentFrame.getContentPane().removeAll();
                currentFrame.getContentPane().add(new ItemListPanel (currentFrame, store));
                currentFrame.getContentPane().revalidate();
            }
        });
	cancelButton.setBounds(120, 222, 84, 20);
	add(cancelButton);
	
	
	
	
	upcListModel = new DefaultListModel<UPC>();
	if(!isNew) {
    	for(UPC upc : item.getUpcs().values()) {
    	upcListModel.addElement(upc);
    	}
	}
	
    	 JList<UPC> upcList = new JList<UPC>(upcListModel);
    	upcList.addListSelectionListener(new ListSelectionListener() {
         	public void valueChanged(ListSelectionEvent e) {
         	   upcEditButton.setEnabled(true);
         		upcDeleteButton.setEnabled(true);
         	}
         	});
         		
    	upcList.setBounds(288, 65, 82, 51);
	add(upcList);
         	
	upcAddButton = new JButton("Add");
	upcAddButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new UpcEditPanel(currentFrame, store, item,  new UPC(), true));
		    currentFrame.getContentPane().revalidate();
		}
	});
	upcAddButton.setBounds(258, 122, 62, 20);
	add(upcAddButton);
	
	upcEditButton = new JButton("Edit");
	upcEditButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new UpcEditPanel(currentFrame, store, item,  upcList.getSelectedValue(), false));
		    currentFrame.getContentPane().revalidate();
		}
	});
	upcEditButton.setBounds(336, 122, 62, 20);
	upcEditButton.setEnabled(false);
	add(upcEditButton);
	
	upcDeleteButton = new JButton("Delete");
	upcDeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                item.getUpcs().remove(upcList.getSelectedValue().getUpc());
                	upcListModel.removeElement(upcList.getSelectedValue());
            }
        });
	upcDeleteButton.setBounds(280, 143, 90, 20);
	upcDeleteButton.setEnabled(false);
	add(upcDeleteButton);
	
	
	
	
	priceListModel = new DefaultListModel<Price>();
	if(!isNew) {
    	for(Price price : item.getPrices()) {
    	priceListModel.addElement(price);
    	}
	}
	
	 JList<Price> priceList = new JList<Price>(priceListModel);
	 priceList.addListSelectionListener(new ListSelectionListener() {
         	public void valueChanged(ListSelectionEvent e) {
         	   priceEditButton.setEnabled(true);
         		priceDeleteButton.setEnabled(true);
         	}
         	});
	 
	 priceList.setBounds(290, 173, 80, 51);
	add(priceList);
	
	priceAddButton = new JButton("Add");
	priceAddButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new PriceEditPanel(currentFrame, store, item,  new Price(), true));
		    currentFrame.getContentPane().revalidate();
		}
	});	
	priceAddButton.setBounds(258, 234, 62, 20);
	add(priceAddButton);
	
	priceEditButton = new JButton("Edit");
	priceEditButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    currentFrame.getContentPane().removeAll();
		    currentFrame.getContentPane().add(new PriceEditPanel(currentFrame, store, item,  priceList.getSelectedValue(), false));
		    currentFrame.getContentPane().revalidate();
		}
	});
	priceEditButton.setBounds(330, 234, 68, 20);
	priceEditButton.setEnabled(false);
	add(priceEditButton);
	
	priceDeleteButton = new JButton("Delete");
	priceDeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                item.getPrices().remove(priceList.getSelectedValue());
                	priceListModel.removeElement(priceList.getSelectedValue());
            }
        });
	priceDeleteButton.setBounds(288, 255, 82, 20);
	priceDeleteButton.setEnabled(false);
	add(priceDeleteButton);
	
	


    }
}
