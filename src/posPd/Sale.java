package posPd;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Sale {

    private ArrayList<Payment> payments;
    private ArrayList<SaleLineItem> saleLineItems;
    private LocalDateTime dateTime;
    public Boolean taxFree;
    private Session session;

    public Sale() {
        dateTime = LocalDateTime.now();
        saleLineItems = new ArrayList<SaleLineItem>();
        payments = new ArrayList<Payment>();
        taxFree = false;
    }

    public Sale(Session theSession, String isTaxFree) {
        this();
        taxFree = isTaxFree.equals("Y");
        theSession.addSale(this);
        setSession(theSession);
    }
    
    public void setTaxFree(Boolean taxFree) {
	this.taxFree = taxFree;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
    }

    public void addSaleLineItem(SaleLineItem sli) {
        saleLineItems.add(sli);
        sli.setSale(this);
    }

    public ArrayList<SaleLineItem> getSaleLineItems() {
        return saleLineItems;
    }

    public void removeSaleLineItem(SaleLineItem sli) {
        saleLineItems.remove(sli);
    }

    public BigDecimal calcTotal() {
        return calcSubTotal().add(calcTax());
    }

    public BigDecimal calcSubTotal() {
        BigDecimal sub = BigDecimal.ZERO;
        for (SaleLineItem sli : saleLineItems) {
            sub = sub.add(sli.calcSubTotal());
        }
        return sub;
    }

    public BigDecimal calcTax() {
        if (taxFree) return BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        for (SaleLineItem sli : saleLineItems) {
            tax = tax.add(sli.calcTax());
        }
        return tax;
    }

    public BigDecimal getTotalPayments() {
        BigDecimal total = BigDecimal.ZERO;
        for (Payment p : payments) {
            total = total.add(p.getAmount());
        }
        return total;
    }

    public Boolean isPaymentEnough() {
        return calcAmtTendered().compareTo(calcTotal()) > 0;
    }

    public BigDecimal calcAmountForPayment() {
        return this.calcTotal().subtract(calcAmtTendered());
    }

    public BigDecimal calcChange() {
        return calcAmtTendered().subtract(calcTotal());
    }

    public BigDecimal calcAmtTendered() {
        BigDecimal tendered = BigDecimal.ZERO;
        for (Payment p : payments) {
            tendered = tendered.add(p.getAmtTendered());
        }
        return tendered;
    }

 
    public String toString() {
        String result = "";
        
        result += "Sale : SubTotal = " + calcSubTotal().setScale(2, BigDecimal.ROUND_HALF_UP);
        result += " Tax = " + calcTax().setScale(2, BigDecimal.ROUND_HALF_UP);
        result += " Total = " + calcTotal().setScale(2, BigDecimal.ROUND_HALF_UP);
        result += " Payment = " + getTotalPayments().setScale(2, BigDecimal.ROUND_HALF_UP);
        result += " Change = " + calcChange().setScale(2, BigDecimal.ROUND_HALF_UP);
        result += "\n";
        
        for (SaleLineItem sli : saleLineItems) {
            result += "    " + sli.toString() + "\n";
        }
        
        return result;
    }

    public Session getSession() {
        return session;
    }

    public void setSession (Session session) {
        this.session = session;
    }

    

}
