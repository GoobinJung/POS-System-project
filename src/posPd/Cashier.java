package posPd;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Cashier {

	private Person person;
	private ArrayList<Session> sessions;
	private String number;
	private String password;

	public Cashier() {
		sessions = new ArrayList<Session>();
		person = new Person();
	}

	public Cashier(String number, Person person, String password) {
		this();
		setNumber(number);
		setPerson(person);
		person.setCashier(this);
		setPassword(password);
	}

	public void setNumber(String number) {
	    this.number = number;
	}

	public void setPerson(Person person) {
	    this.person = person;
	}

	public void addSession(Session session) {
		getSessions().add(session);
	}
	
	public String testString() {
	    return getNumber() + " " + getPerson().toString();
	}
	
	public Person getPerson() { 
	    return this.person;
	}

	public String toString() {
	    return getNumber() + " - " + getPerson().getName(); 
	}
	
	public BigDecimal calculateTotalDifferenceForDate(LocalDate aLocalDate) {
	    BigDecimal totalDiff = new BigDecimal("0.00");
	    
	    for (Session session : getSessions()) {
		if (session.getStartDateTime().toLocalDate().equals(aLocalDate)) {
		    totalDiff = totalDiff.add(session.calcCashCountDiff()); 
		}
	    }
	    
	    return totalDiff;
	}
    
	public ArrayList<Session> getSessions(){
	    return this.sessions;
	}
	
	
	public String getPassword() {
	    return this.password;
	}
	
	public void setPassword(String password) {
	    this.password = password;
	}

	public void removeSession(Session session) {
	    sessions.remove(session);
	}

	public Boolean isAuthorized(String password) {
	    return this.password.equals(password);
	}

	public String getNumber() {
	    return this.number;
	}
}