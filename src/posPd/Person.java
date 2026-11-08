package posPd;

public class Person {

	private Cashier cashier;
	private String name;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String sSN;

	public Person() {
	     
	}

	public Person(String name, String address, String city, String state, String zip, String phone, String ssn) {
		setName(name);
		setAddress(address);
		setCity(city);
		setState(state);
		setZip(zip);
		setPhone(phone);
		setSSN(ssn);
	}
	
	public void setSSN(String ssn) {
	    this.sSN = ssn;
	}

	public void setPhone(String phone) {
	    this.phone = phone;
	}

	public void setZip(String zip) {
	    this.zip = zip;
	}

	public void setState(String state) {
	    this.state = state;
	}

	public void setCity(String city) {
	    this.city = city;
	}

	public void setAddress(String address) {
	    this.address = address;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public Cashier getCashier() {
	    return this.cashier;
	}
	
	public void setCashier(Cashier cashier) {
	    this.cashier = cashier;
	}
	
	public String getName() {
	    return this.name;
	}
	
	public String getAddress() { 
	    return this.address;
	}
	
	public String getCity() {
	    return this.city;
	}
	
	public String getState() {
	    return this.state;
	}
	
	public String getZip() {
	    return this.zip;
	}
	
	public String getPhone() {
	    return this.phone;
	}
	
	public String getSSN() {
	    return this.sSN;
	}
	
	public String toString() {
	    return name + " " + address + " " + city + ", " + state + " " + zip + " " + phone + " " + sSN;
	}
}