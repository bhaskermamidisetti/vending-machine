package com.vending.machine;

import com.vending.machine.exceptions.NotFullPaidException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VendingMachineImplTest {

    private VendingMachineImpl vendingMachine;

    @BeforeAll
    public void init() {
        vendingMachine = new VendingMachineImpl();
    }

    @ParameterizedTest
    @EnumSource(Item.class)
    public void selectItemAndGetPrice(Item item) {
        Long result = vendingMachine.selectItemAndGetPrice(item);

        assertEquals(Long.valueOf(item.getPrice()), result);
    }

    @DisplayName("Should add Coins into cash Inventory")
    @ParameterizedTest
    @EnumSource(Coin.class)
    public void insertCoin(Coin coin) {
        vendingMachine.currentBalance = 10L;
        vendingMachine.insertCoin(coin);
        Inventory<Coin> coinInventory = vendingMachine.cashInventory;

        assertTrue(coinInventory.hasItem(coin));
    }

    @DisplayName("Should refund Coins for given balance")
    @Test
    public void refund() {
        vendingMachine.currentBalance = 30L;
        List<Coin> coinList = vendingMachine.refund();

        assertEquals("QUARTER", coinList.get(0).name());
        assertEquals("NICKLE", coinList.get(1).name());
    }

    @Test
    public void should_get_QUARTER_for_given_amount() {
        List<Coin> coinList = vendingMachine.getChange(50L);
        for (Coin coin : coinList) {
            assertEquals("QUARTER", coin.name());
        }
    }

    @Test
    public void should_get_DIME_for_given_amount() {
        List<Coin> coinList = vendingMachine.getChange(10L);

        for (Coin coin : coinList) {
            assertEquals("DIME", coin.name());
        }
    }

    @DisplayName("Should return NotFullPaidException when currentbalance is lessthan item price")
    @Test
    public void collectItemAndChange_NotFullPaidException() {
        vendingMachine.currentBalance = 8L;
        vendingMachine.currentItem = Item.COKE;
        Exception exception = assertThrows(NotFullPaidException.class, () ->
                vendingMachine.collectItemAndChange());

        assertEquals("Price not full paid, remaining : 17", exception.getMessage());
    }

    @DisplayName("should return Item-Coke and coin for given balance")
    @Test
    public void collectItemAndChange() {
        vendingMachine.currentBalance = 50L;
        Bucket<Item, List<Coin>> bucket = vendingMachine.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> coinList = bucket.getSecond();

        assertEquals("COKE", item.name());
        assertEquals("QUARTER", coinList.get(0).name());
    }

    @Test
    public void hasSufficientChange_True() {
        vendingMachine.currentBalance = 50L;
        vendingMachine.currentItem = Item.COKE;
        boolean isSufficientChange = vendingMachine.hasSufficientChange();

        assertTrue(isSufficientChange);
    }

    @Test
    public void getTotalSales() {
        vendingMachine.totalSales = 10L;
        long totalSales = vendingMachine.getTotalSales();

        assertEquals(10L, totalSales);
    }
}