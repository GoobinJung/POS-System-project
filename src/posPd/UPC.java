package posPd;

public class UPC {

	private Item item;
	private String upc;

	public UPC(String upcNumber, Item anItem) {
	    setUpc(upcNumber);
	    setItem(anItem);
	    anItem.addUPC(this);
	}
	public UPC() {
	    
	}
	
	
	public String getUpc() {
	    return this.upc;
	}
	
	
	
	public void setUpc(String upc) {
	    this.upc = upc;
	}
	
	public Item getItem() {
	    return this.item;
	}
	
	public void setItem(Item item) {
	    this.item = item;
	}
	
	public static Boolean validateUPC (String string) {
	    return true;
	}
	
	public String toString() {
	    return upc;
	}
	
	
}