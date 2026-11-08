package posPd;

public class UPC implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


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
	    if (!validateUPC(upc)) throw new IllegalArgumentException("UPC must contain 8 to 14 digits.");
        this.upc = upc;
	}

	public Item getItem() {
	    return this.item;
	}

	public void setItem(Item item) {
	    this.item = item;
	}

	public static Boolean validateUPC (String string) {
	    return string != null && string.matches("[0-9]{8,14}");
	}

	public String toString() {
	    return upc;
	}


}
