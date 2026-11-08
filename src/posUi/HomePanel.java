package posUi;

import javax.swing.JPanel;

import posPd.Store;
import javax.swing.JLabel;

public class HomePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Create the panel.
     */
    public HomePanel(Store store) {
	
    	JLabel lblNewLabel = new JLabel (store.getName());
    	lblNewLabel.setBounds(127,37,200,100);
    	
    	
    	add(lblNewLabel);

    }

}
