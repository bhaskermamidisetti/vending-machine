package com.vending.machine;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InventoryTest {
    private Inventory inventory;

    @BeforeAll
    public void Init() {
        inventory = new Inventory();
    }


    @Test
    public void getQuantity() {
        int quantity = inventory.getQuantity(Item.COKE);
        assertEquals(0, quantity);
    }

    @Test
    public void removeItem() {
        inventory.deduct(Item.COKE);
        assertFalse(inventory.hasItem(Item.COKE));
    }

    @Test
    public void hasItem() {
        assertFalse(inventory.hasItem(Item.COKE));
    }
}