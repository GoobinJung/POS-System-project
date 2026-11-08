package posPd;
import java.util.*;

public class Store {

	private TreeMap<String, Item> items;
	private TreeMap<String, Cashier> cashiers;
	private TreeMap<String, Register> registers;
	private TreeMap<String, UPC> upcs;
	private TreeMap<String, TaxCategory> taxCategories;
	private ArrayList<Session> sessions;
	

	private String number;
	private String name;

	public Store() {
		items = new TreeMap<String, Item>();
		cashiers = new TreeMap<String, Cashier>();
		registers = new TreeMap<String, Register>();
		upcs = new TreeMap<String, UPC>();
		taxCategories = new TreeMap<String, TaxCategory>();
		sessions = new ArrayList<Session>();
	}

	public Store(String name) {
		this();
		setName(name);
	}
	

	public Store(String number, String name) {
	    this();
	    setNumber(number);
	    setName(name);
	}
	
	

	public String getNumber() {
	    return number;
	}

	public String getName() {
	    return name;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addItem(Item item) {
	    if(item != null) {
		getItems().put(item.getNumber(), item);
	    }
		
	}

	public TreeMap<String, Item> getItems() {
	    return this.items;
	}

	public void removeItem(Item item) {
		items.remove(item.getNumber());
	}

	public void addUPC(UPC upc) {
	    if (upc != null) {
		getUpcs().put(upc.getUpc(), upc);
	    }
		
	}

	public TreeMap<String, UPC> getUpcs() {
	    
	    return this.upcs;
	}
	
	public TreeMap<String, Cashier> getCashiers() {
	    
	    return this.cashiers;
	}

	public void removeUPC(UPC upc) {
		upcs.remove(upc.getUpc());
	}

	public void addRegister(Register register) {
	    if (register != null) {
		registers.put(register.getNumber(), register);
	    }
	}

	public void removeRegister(Register register) {
		registers.remove(register.getNumber());
	}

	public void addCashier(Cashier cashier) {
	    if (cashier != null) {
		getCashiers().put(cashier.getNumber(), cashier);
	    }
	}

	public void removeCashier(Cashier cashier) {
		cashiers.remove(cashier.getNumber());
	}

	public void addTaxCategory(TaxCategory taxCategory) {
	    if (taxCategory != null) {
		getTaxCategories().put(taxCategory.getCategory(), taxCategory);
	    }
	}

	public void removeTaxCategory(TaxCategory taxCategory) {
		taxCategories.remove(taxCategory.getCategory());
	}

	public void addSession(Session session) {
	    if (session != null) {
		getSessions().add(session);
	    }
	}

	public ArrayList<Session> getSessions() {
	    return this.sessions; 
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	public Register findRegisterForNumber(String number) {
	    if (number.length() > 0) {
		return getRegisters().get(number);
	    } else
		return null;
	}

	public Item findItemForUPC(String upc) {
		Item returnItem = null;
		for(Item item : items.values()) {
		    for(UPC itemUPC : item.getUpcs().values()) {
			if(itemUPC.getUpc().equals(upc)) {
			    returnItem = item;
			}
		    }
		}
		return returnItem;
	}

	public Item findItemForNumber(String number) {
	    if (number.length() > 0) {
		return getItems().get(number);
		} else
		return null;
	}

	public Cashier findCashierForNumber(String number) {
		return cashiers.get(number);
	}

	public TaxCategory findTaxCategoryForCategory(String category) {
	    if (category.length() > 0) {
		return taxCategories.get(category);
	    } else
		return null;
	}
	
	public TreeMap<String, TaxCategory> getTaxCategories() {
	    return this.taxCategories; 
	}

	public String toString() {
		return "Store Number: " + number + ", Name: " + name;
	}

	public TreeMap<String, Register> getRegisters() {
	    return this.registers;
	}
}