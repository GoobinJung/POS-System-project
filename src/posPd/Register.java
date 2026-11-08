package posPd;
import java.util.*;

public class Register implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


	private Collection<Session> session;

	private String number;

	public Register() {

	    session = new ArrayList<Session>();
	}




	public Register(Store aStore, String number) {
	    this();
	    setNumber(number);
	    aStore.addRegister(this);
	}

	public void setNumber(String number) {
	    this.number = number;
	}

	public String toString() {
	    return number;
	}

	public String getNumber() {
	    return this.number;
	}

	public void addSession(Session s) {
	    if (!session.contains(s)) session.add(s);
	}


}
