package posUi;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import posDm.StoreDm;
import posPd.Store;

import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PosFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    Store store = StoreDm.importStore("StoreData.csv"); 
		    PosFrame frame = new PosFrame(store);
		    frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the frame.
     */
    public PosFrame(Store store) {
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 500, 400);
	JFrame currentFrame = this;
	
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	contentPane.setLayout(new BorderLayout(0,0));
	setContentPane(contentPane);
	
	JMenuBar menuBar = new JMenuBar();
	setJMenuBar(menuBar);
	
	JMenu mnNewMenu = new JMenu("Maintain");
	menuBar.add(mnNewMenu);
	
	JMenuItem mntmNewMenuItem = new JMenuItem("Store");
	mntmNewMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    getContentPane().removeAll();
		    getContentPane().add(new StoreEditPanel(currentFrame, store));
		    getContentPane().revalidate();
		    
		}
	});
	mnNewMenu.add(mntmNewMenuItem);
	
	JMenuItem mntmNewMenuItem_1 = new JMenuItem("Cashier");
	mntmNewMenuItem_1.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    getContentPane().removeAll();
		    getContentPane().add(new CashierListPanel(currentFrame, store));
		    getContentPane().revalidate();
		    
		}
	});
	mnNewMenu.add(mntmNewMenuItem_1);
	
	JMenuItem mntmNewMenuItem_2 = new JMenuItem("Register");
	mntmNewMenuItem_2.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    getContentPane().removeAll();
		    getContentPane().add(new RegisterListPanel(currentFrame, store));
		    getContentPane().revalidate();
		}
	});
	mnNewMenu.add(mntmNewMenuItem_2);
	
	
	JMenuItem mntmNewMenuItem_3 = new JMenuItem("Items");
	mntmNewMenuItem_3.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    getContentPane().removeAll();
		    getContentPane().add(new ItemListPanel(currentFrame, store));
		    getContentPane().revalidate();
		}
	});
	mnNewMenu.add(mntmNewMenuItem_3);
	
	JMenuItem mntmNewMenuItem_4 = new JMenuItem("Tax Categories");
	mntmNewMenuItem_4.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    getContentPane().removeAll();
		    getContentPane().add(new TaxCategoryListPanel(currentFrame, store));
		    getContentPane().revalidate();
		}
	});
	
	
	mnNewMenu.add(mntmNewMenuItem_4);
	
	JMenu mnNewMenu_1 = new JMenu("POS");
	menuBar.add(mnNewMenu_1);

	JMenuItem login = new JMenuItem("Login");
	login.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    getContentPane().removeAll();
		    getContentPane().add(new LoginPanel(currentFrame, store));
		    getContentPane().revalidate();
		}
	});
	mnNewMenu_1.add(login);
	
	
	JMenu mnNewMenu_2 = new JMenu("Reports");
	menuBar.add(mnNewMenu_2);

        getContentPane().removeAll();
        getContentPane().add(new HomePanel(store));
	getContentPane().revalidate();

    }

}
