package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    Inventory inv = null;
    @BeforeEach


    public void setUp(){
    inv= Inventory.getInstance();
    }

    @Test
    public void test(){
    }


    @Test
    void getInstance() {
        Inventory check = Inventory.getInstance();
        assertNotNull(check);
        assertEquals(inv, check);
    }

    @Test
    void load() {
        String check[] = new String[2];
        check[0] = "Skyhook";
        check[1] = "helicopter";

        inv.load(check);

        assertEquals(true, "helicopter");
        assertNotEquals(true, "van");
    }


    @Test
    void getItem() {
        String check[] = new String[2];
        check[0] = "Skyhook";
        check[1] = "helicopter";

        inv.load(check);

        assertEquals(true, inv.getItem("Skyhook"));
        assertNotEquals(true, inv.getItem("plane"));
//        assertDoesNotThrow(()->{inv.getItem("");});
//        assertDoesNotThrow(()->{inv.getItem(null);});


    }


}