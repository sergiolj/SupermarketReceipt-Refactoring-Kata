package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreeForTwoOfferTest {

    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
    private final ThreeForTwoOffer offer = new ThreeForTwoOffer();

    @Test
    void noDiscountBelowThreeUnits() {
        assertTrue(offer.discountFor(toothbrush, Quantity.of(2), Money.of("0.99")).isEmpty());
    }

    @Test
    void noDiscountWhenFractionalQuantityTruncatesBelowThree() {
        assertTrue(offer.discountFor(toothbrush, Quantity.of(2.9), Money.of("0.99")).isEmpty());
    }

    @Test
    void oneFreeUnitForThreeAndForFour() {
        assertEquals(Money.of("-0.99"), offer.discountFor(toothbrush, Quantity.of(3), Money.of("0.99")).orElseThrow().discountAmount());
        assertEquals(Money.of("-0.99"), offer.discountFor(toothbrush, Quantity.of(4), Money.of("0.99")).orElseThrow().discountAmount());
    }

    @Test
    void twoFreeUnitsForSix() {
        Discount d = offer.discountFor(toothbrush, Quantity.of(6), Money.of("0.99")).orElseThrow();
        assertEquals(toothbrush, d.product());
        assertEquals("3 for 2", d.description());
        assertEquals(Money.of("-1.98"), d.discountAmount());
    }
}
