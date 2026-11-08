package posTest;

import posPd.Cashier;
import posPd.Item;
import posPd.Person;
import posPd.Price;
import posPd.Register;
import posPd.Sale;
import posPd.SaleLineItem;
import posPd.Session;
import posPd.Store;
import posPd.TaxCategory;
import posPd.TaxRate;
import posPd.UPC;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import posDm.StoreDm;

public class StoreTest {
    
    public static void printStore(Store myStore) {
	System.out.println(myStore);
	System.out.println("=============");
	System.out.println("Cashiers");
	System.out.println("=============");
	for (Cashier cashier : myStore.getCashiers().values()) {
	    System.out.println(cashier.getPerson().getName());
	}
	
	System.out.println("=============");
	System.out.println("Registers");
	System.out.println("=============");
	for (Map.Entry<String, Register> entry : myStore.getRegisters().entrySet()) {
	    System.out.println(entry.getValue().getNumber());
	}
	
	System.out.println("=============");
	System.out.println("Items");
	System.out.println("=============");
	for (Map.Entry<String, Item> entry : myStore.getItems().entrySet()) {
	    Item item = entry.getValue();
	    Price price = item.getPrices().first(); 
	    System.out.println(item.getNumber() + " " + item.getDescription() + " Price:" + price.getPrice().toPlainString() + 
	                       " Tax Rate:" + item.getTaxCategory().getTaxRateforDate(LocalDate.now()).setScale(2, BigDecimal.ROUND_HALF_UP));
	    System.out.println(String.format("%12s", item.getUpcs().firstEntry().getValue().getUpc()).replace(' ', '_'));
	}
	
	System.out.println("=============");
	System.out.println("Sessions");
	System.out.println("=============");
	for (Session session : myStore.getSessions()) {
	    System.out.println(session.toString());
	}
	
    }


    
    public StoreTest(Store aStore) {
	
    }
    public void doAcTest_1_1(Store aStore) {
	Item anItem;
	UPC upc;
	Price price;
	
	
        System.out.println("***************");
        System.out.println("POS AC 1.1 - Items, Tax Category, UPC, and Price");
        System.out.println("***************");
        
        
        
        TaxCategory taxCat = new TaxCategory("Food", "01/01/25" , "0.07");
        aStore.addTaxCategory(taxCat);
        
        // Add Item 1
        anItem = new Item(aStore, "0001", "Plain M&M", "Food");
        upc = new UPC("11111111111", anItem);
        aStore.addUPC(upc);
        price = new Price(anItem, "3.29", "2025-01-01");
        
        // Add Item 2
        anItem = new Item(aStore, "0002", "Peanut M&M", "Food");
        upc = new UPC("22222222222", anItem);
        aStore.addUPC(upc);
        price = new Price(anItem, "3.39", "2025-01-01");
        
        // Add Item 3
        anItem = new Item(aStore, "0003", "Crunchy M&M", "Food");
        upc = new UPC("33333333333", anItem);
        aStore.addUPC(upc);
        price = new Price(anItem, "3.49", "2025-01-01");
        
        
        for (Item theItem : aStore.getItems().values()) {
            System.out.println(theItem.getNumber() + " " + theItem.getDescription() + " Price:" + theItem.getPrices().first().getPrice().toPlainString() + 
                               " Tax Rate:" + theItem.getTaxCategory().getTaxRateforDate(LocalDate.now()).setScale(2, BigDecimal.ROUND_HALF_UP));
            System.out.println(String.format("%12s", theItem.getUpcs().firstEntry().getValue().getUpc()).replace(' ', '_'));
        }   
    }

    public void doAcTest_1_2(Store aStore) {
	
	Cashier aCashier;
	Person aPerson;
	
        System.out.println("***************");
        System.out.println("POS AC 1.2 - Store Cashiers");
        System.out.println("***************");
        
        // Add person 1
        aPerson = new Person("John Smith", "111-11-1111", "1 Main Street", "Edmond", "OK", "73013", "405-111-1111");
        aCashier = new Cashier("1", aPerson, "password1");
        aStore.addCashier(aCashier);
        
        // Add person 2
        aPerson = new Person("Jane Doe", "222-22-2222", "2 Main Street", "Edmond", "OK", "73013", "405-222-2222");
        aCashier = new Cashier("2", aPerson, "password2");
        aStore.addCashier(aCashier);
        
        // Add person 3
        aPerson = new Person("Sam Spade", "333-33-3333", "3 Main Street", "Edmond", "OK", "73013", "405-333-3333");
        aCashier = new Cashier("3", aPerson, "password3");
        aStore.addCashier(aCashier);
        
        
        for (Cashier theCashier : aStore.getCashiers().values()) {
            System.out.println(theCashier.getNumber() + " " + theCashier.getPerson().getName() + " " + 
                               theCashier.getPerson().getAddress() + " Edmond,OK " + theCashier.getPerson().getZip() + " " + 
                               theCashier.getPerson().getPhone());
        }
    }

    public void doAcTest_1_3(Store aStore) {
	 System.out.println("***************");
	 System.out.println("POS AC 1.3");
	 System.out.println("***************");
	 
	 // add register 1
	 new Register(aStore, "1");
	 
	 // add register 2
	 new Register(aStore, "2");
	 
	 
	 for (Register aRegister : aStore.getRegisters().values()) {
	     System.out.println(aRegister.getNumber());
	 }
    }

    public void doAcTest_1_4(Store aStore) {
	System.out.println("***************");
	System.out.println("POS AC 1.4");
	System.out.println("***************");
	
	// Create new session and add to store
	Register aRegister = aStore.findRegisterForNumber("1");
	Cashier aCashier = aStore.findCashierForNumber("1");
	Session session = new Session(aStore, aCashier, aRegister);
	
	// Create new Sale and add to session
	Sale sale = new Sale(session, "N");
	
	// Create new SaleLineItem for Sale
	SaleLineItem sli1 = new SaleLineItem(aStore, sale, "0001", "1");
	SaleLineItem sli2 = new SaleLineItem(aStore, sale, "0002", "2");
	
	
	System.out.println(sale); 
	}
    
    public static void main(String[] args) {
    	   
            
	    Store store = StoreDm.importStore("StoreData.csv");
	    StoreTest test = new StoreTest(store);
	    printStore(store); 
	}
}