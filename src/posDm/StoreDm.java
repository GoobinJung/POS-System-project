package posDm;

import posPd.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StoreDm {

    public static Store importStore(String fileName) {
        Store store = new Store("001", "Default Store");
        Session currentSession = null;
        Sale currentSale = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] token = line.split(",");
                String dataType = token[0];

                if (dataType.equals("Store")) {
                    store.setName(token[1]);

                } else if (dataType.equals("TaxCategory")) {
                    if (store.findTaxCategoryForCategory(token[1]) == null) {
                        TaxCategory tc = new TaxCategory(token[1]);
                        tc.addTaxRate(new TaxRate(LocalDate.parse(token[3], formatter), new BigDecimal(token[2])));
                        store.addTaxCategory(tc);
                    }

                } else if (dataType.equals("Cashier")) {
                    if (store.findCashierForNumber(token[1]) == null) {
                        Person p = new Person(token[2], token[3], token[4], token[5], token[6], token[7], token[8]);
                        Cashier c = new Cashier(token[1], p, token[9]);
                        store.addCashier(c);
                    }

                } else if (dataType.equals("Item")) {
                    if (store.findItemForNumber(token[1]) == null) {
                        Item item = new Item(store, token[1], token[3], token[4]);
                        item.addUPC(new UPC(token[2], item));
                        item.addPrice(new Price(item, token[5], LocalDate.parse(token[6], formatter).toString()));
                        
                        if (token.length > 7 && token[7] != null && !token[7].isEmpty()) {
                            PromoPrice promo = new PromoPrice(token[7], LocalDate.parse(token[8], formatter).toString(), 
                                                             LocalDate.parse(token[9], formatter).toString());
                            promo.setItem(item);
                            item.addPrice(promo);
                        }
                        store.addItem(item);
                    }

                } else if (dataType.equals("Register")) {
                    if (store.findRegisterForNumber(token[1]) == null) {
                        Register register = new Register(store, token[1]);
                        store.addRegister(register);
                    }

                } else if (dataType.equals("Session")) {
                  
                    Cashier cashier = store.findCashierForNumber(token[2]);
                    Register register = store.findRegisterForNumber(token[1]);
                    if (cashier != null && register != null) {
                        currentSession = new Session(store, cashier, register);
                       
                        store.addSession(currentSession); 
                        
                    }

                } else if (dataType.equals("Sale")) {
                    if (currentSession != null) {
                        currentSale = new Sale(currentSession, token[1]);
                       
                        
                    }

                } else if (dataType.equals("SaleLineItem")) {
                    if (currentSale != null) {
                        Item saleItem = store.findItemForNumber(token[1]);
                        if (saleItem != null) {
                            SaleLineItem sli = new SaleLineItem(currentSale, saleItem, token[2]);
                            currentSale.addSaleLineItem(sli); 
                        }
                    }

                } else if (dataType.equals("Payment")) {
                    if (currentSale != null && token.length > 3) {
                        String type = token[1];
                        BigDecimal amount = new BigDecimal(token[2]);
                        BigDecimal tendered = new BigDecimal(token[3]);
                        
                        Payment payment = null;
                        if (type.equals("Cash")) {
                            payment = new Cash(amount.toString(), tendered);
                        } else if (type.equals("Credit") && token.length > 6) {
                            Credit credit = new Credit(amount.toString(), token[4], token[5], 
                                                     LocalDate.parse(token[6], formatter).toString());
                            payment = credit;
                        } else if (type.equals("Check") && token.length > 5) {
                            Check check = new Check(amount.toString(), token[5]);
                            payment = check;
                        }
                        
                        if (payment != null) {
                            currentSale.addPayment(payment);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error importing store data: " + e.getMessage());
            e.printStackTrace();
        }

        return store;
    }
}