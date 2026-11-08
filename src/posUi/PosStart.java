package posUi;
import posPd.Store;
import posTest.StoreTest;

public class PosStart {
    public static void main(String[] args) {
        doProject_2_1();
    }

    private static void doProject_2_1() {
        System.out.println("Project 2.1.1 ");
        System.out.println();
        
        
        Store myStore = new Store("6","Goobin");
        System.out.println("Ready to test store:" + myStore);
        System.out.println();
        
        
        StoreTest storetest =new StoreTest(myStore);
        
        storetest.doAcTest_1_1(myStore);
        storetest.doAcTest_1_2(myStore);
        storetest.doAcTest_1_3(myStore);
        storetest.doAcTest_1_4(myStore);
        System.out.println();
        
    }
}