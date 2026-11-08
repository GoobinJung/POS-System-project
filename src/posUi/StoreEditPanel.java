package posUi;

import javax.swing.JFrame;
import javax.swing.JPanel;

import posPd.Store;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StoreEditPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField textField;

    /**
     * Create the panel.
     */
    public StoreEditPanel(JFrame currentFrame, Store store) {
    	setLayout(null);
    	
    	JLabel lblNewLabel = new JLabel("Edit Store");
    	lblNewLabel.setBounds(182, 10, 61, 24);
    	add(lblNewLabel);
    	
    	textField = new JTextField(store.getName());
    	textField.setBounds(158, 106, 96, 18);
    	add(textField);
    	textField.setColumns(10);
    	
    	JLabel lblNewLabel_1 = new JLabel("Name:");
    	lblNewLabel_1.setBounds(105, 103, 61, 24);
    	add(lblNewLabel_1);
    	
    	JButton btnNewButton = new JButton("Save");
    	btnNewButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		   store.setName(textField.getText());
        		   currentFrame.getContentPane().removeAll();
        		   currentFrame.getContentPane().add(new HomePanel(store));
        		   currentFrame.getContentPane().revalidate();
    		}
    	});
    	btnNewButton.setBounds(70, 196, 84, 20);
    	add(btnNewButton);
    	
    	JButton btnNewButton_1 = new JButton("Cancel");
    	btnNewButton_1.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		currentFrame.getContentPane().removeAll();
    		currentFrame.getContentPane().add(new HomePanel(store));
    		currentFrame.getContentPane().revalidate();
    		}
    	});
    	btnNewButton_1.setBounds(237, 196, 84, 20);
    	add(btnNewButton_1);

    }
}
