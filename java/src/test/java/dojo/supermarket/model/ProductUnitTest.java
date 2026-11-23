package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductUnitTest {

    @Test
    void kiloAcceptsIntegerQuantity() {
        assertTrue(ProductUnit.KILO.acceptsQuantity(2));
    }

    @Test
    void kiloAcceptsFractionalQuantity() {
        assertTrue(ProductUnit.KILO.acceptsQuantity(1.5));
    }

    @Test
    void eachAcceptsIntegerQuantity() {
        assertTrue(ProductUnit.EACH.acceptsQuantity(3));
    }

    @Test
    void eachRejectsFractionalQuantity() {
        assertFalse(ProductUnit.EACH.acceptsQuantity(1.5));
    }

    @Test
    void kiloIsNotDiscrete() {
        assertFalse(ProductUnit.KILO.isDiscrete());
    }

    @Test
    void eachIsDiscrete() {
        assertTrue(ProductUnit.EACH.isDiscrete());
    }
}