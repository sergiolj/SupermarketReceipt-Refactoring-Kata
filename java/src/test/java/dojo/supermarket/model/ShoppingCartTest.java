package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private final Product rice = new Product("rice", ProductUnit.EACH);
    private final Product apples = new Product("apples", ProductUnit.KILO);

    @Test
    void newCartIsEmpty() {
        ShoppingCart cart = new ShoppingCart();
        assertTrue(cart.getItems().isEmpty());
        assertTrue(cart.productQuantities().isEmpty());
    }

    @Test
    void addItemAddsOneUnit() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(rice);
        assertEquals(1, cart.getItems().size());
        assertEquals(Quantity.ONE, cart.productQuantities().get(rice));
    }

    @Test
    void addItemQuantityAccumulatesPerProductButKeepsEachLine() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(apples, 1.5);
        cart.addItemQuantity(apples, 0.5);
        cart.addItemQuantity(rice, 2);

        assertEquals(3, cart.getItems().size());
        assertEquals(Quantity.of(2.0), cart.productQuantities().get(apples));
        assertEquals(Quantity.of(2.0), cart.productQuantities().get(rice));
    }

    @Test
    void exposedCollectionsAreUnmodifiable() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(rice);
        assertThrows(UnsupportedOperationException.class, () -> cart.getItems().clear());
        assertThrows(UnsupportedOperationException.class, () -> cart.productQuantities().clear());
    }

    @Test
    void fractionalKilosAddUpExactly() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(apples, 0.1);
        cart.addItemQuantity(apples, 0.2);
        assertEquals(Quantity.of("0.300"), cart.productQuantities().get(apples));
    }
}
