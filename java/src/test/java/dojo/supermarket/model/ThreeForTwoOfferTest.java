package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreeForTwoOfferTest {

    private static final double DELTA = 0.001;
    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
    private final ThreeForTwoOffer offer = new ThreeForTwoOffer();

    @Test
    void noDiscountBelowThreeUnits() {
        assertTrue(offer.discountFor(toothbrush, 2, 0.99).isEmpty());
    }

    @Test
    void noDiscountWhenFractionalQuantityTruncatesBelowThree() {
        assertTrue(offer.discountFor(toothbrush, 2.9, 0.99).isEmpty());
    }

    @Test
    void oneFreeUnitForThreeAndForFour() {
        assertEquals(-0.99, offer.discountFor(toothbrush, 3, 0.99).orElseThrow().discountAmount(), DELTA);
        assertEquals(-0.99, offer.discountFor(toothbrush, 4, 0.99).orElseThrow().discountAmount(), DELTA);
    }

    @Test
    void twoFreeUnitsForSix() {
        Discount d = offer.discountFor(toothbrush, 6, 0.99).orElseThrow();
        assertEquals(toothbrush, d.product());
        assertEquals("3 for 2", d.description());
        assertEquals(-1.98, d.discountAmount(), DELTA);
    }
}
