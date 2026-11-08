package posUi;
import javax.swing.*;
import posPd.Store;

public class HomePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public HomePanel(Store store) {
        JPanel body = Ui.page(this, store.getName());
        Ui.row(body, new JLabel("POS > Login to start or resume checkout."));
        Ui.row(body, new JLabel("Maintain manages store data. Reports provides sales summaries and CSV export."));
        Ui.row(body, new JLabel("Demo application: use fictional payment and customer information only."));
    }
}

