package posPd;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Session implements Comparable<Session>, Comparator<Session> { 

	private Register register;
	private Cashier cashier;
	private Collection<Sale> sales;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private BigDecimal startCash;
	private BigDecimal enterCash;
	
	public Collection<Sale> getSales() {
	    return sales;
	}
	
	public BigDecimal getEnterCash() {
	    return enterCash;
	}

	public void setEnterCash(BigDecimal enterCash) {
	    this.enterCash = enterCash;
	}

	public BigDecimal getStartCash() {
	    return startCash;
	}
	
	public int getNumSales() {
	    return sales.size();
	}




	public void setStartCash(BigDecimal startCash) {
	    this.startCash = startCash;
	}

	private BigDecimal endCash;
	
	

	public BigDecimal getEndCash() {
	    return endCash;
	}




	public void setEndCash(BigDecimal endCash) {
	    this.endCash = endCash;
	}




	public Session() {
		sales = new ArrayList<Sale>();
		startDateTime = LocalDateTime.now();
		startCash = new BigDecimal("0.00");
		endCash = new BigDecimal("0.00");
	}
	
	

	
	public Session(Store store, Cashier cashier, Register register) {
	    this();
	    setCashier(cashier);
	    setRegister(register);
	   
	    cashier.addSession(this);
	}
	

	public Register getRegister() {
	    return this.register;
	}

	private void setRegister(Register register) {
	    this.register = register;
	}
	
	public Cashier getCashier() {
	    return this.cashier;
	}

	private void setCashier(Cashier cashier) {
	    this.cashier = cashier;
	}

	public void addSale(Sale sale) {
	 
	    if (sale != null) {
	
		sales.add(sale);
	    }
	}

	public void removeSale(Sale sale) {
	    sales.remove(sale);
	}

	public BigDecimal calcCashCountDiff() {
	    return endCash.subtract(startCash);
	}

	public BigDecimal calcTotal() {
	    BigDecimal total = BigDecimal.ZERO;
	    for (Sale s : sales) {
	        total = total.add(s.calcTotal());
	    }
	    return total;
	}
	
	
	
	
	
	
	
	public int compareTo(Session session) {
	return (getStartDateTime().compareTo(session.getStartDateTime()));
	}

	public LocalDateTime getStartDateTime() { 
	    return this.startDateTime;
	}

	
	public int compare(Session session1, Session session2) {
	return session1.compareTo(session2);
	}

	
	public String toString() {
	    String result = "";
	    
	    result += "Session : Cashier :" + cashier.getPerson().getName();
	    result += " Register :" + register.getNumber();
	    result += " Date : " + startDateTime.toString();
	    result += "\nTotal : " + calcTotal().setScale(2, BigDecimal.ROUND_HALF_UP);
	    result += "\n";
	    
	  
	    for (Sale sale : (ArrayList<Sale>)sales) {
	        result += sale.toString() + "\n";
	        
	    }
	    
	    return result;
	}

	
}