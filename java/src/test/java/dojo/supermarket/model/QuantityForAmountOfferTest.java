package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityForAmountOfferTest {

    private static final double DELTA = 0.001;
    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);

    @Test
    void twoForAmountBelowGroupSizeHasNoDiscount() {
        assertTrue(new QuantityForAmountOffer(2, 1.50).discountFor(toothbrush, 1, 0.99).isEmpty());
    }

    @Test
    void twoForAmountWithExactGroup() {
        Discount d = new QuantityForAmountOffer(2, 2.00).discountFor(toothbrush, 2, 1.20).orElseThrow();
        assertEquals("2 for 2.0", d.description());
        assertEquals(-0.40, d.discountAmount(), DELTA);
    }

    @Test
    void twoForAmountAppliesPerCompleteGroup() {
        Discount d = new QuantityForAmountOffer(2, 1.50).discountFor(toothbrush, 4, 0.99).orElseThrow();
        assertEquals("2 for 1.5", d.description());
        assertEquals(-0.96, d.discountAmount(), DELTA);
    }

    @Test
    void fiveForAmountBelowGroupSizeHasNoDiscount() {
        assertTrue(new QuantityForAmountOffer(5, 3.99).discountFor(toothbrush, 4, 0.99).isEmpty());
    }

    @Test
    void fiveForAmountWithLeftoverPaysFullPriceForTheRest() {
        Discount d = new QuantityForAmountOffer(5, 10.00).discountFor(toothbrush, 6, 2.59).orElseThrow();
        assertEquals("5 for 10.0", d.description());
        assertEquals(-2.95, d.discountAmount(), DELTA);
    }

    @Test
    void fiveForAmountAppliesTwiceForTenUnits() {
        Discount d = new QuantityForAmountOffer(5, 3.99).discountFor(toothbrush, 10, 0.99).orElseThrow();
        assertEquals(-1.92, d.discountAmount(), DELTA);
    }
}
