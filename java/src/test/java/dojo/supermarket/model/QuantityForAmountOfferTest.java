package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityForAmountOfferTest {

    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);

    @Test
    void twoForAmountBelowGroupSizeHasNoDiscount() {
        assertTrue(new QuantityForAmountOffer(2, Money.of("1.50")).discountFor(toothbrush, Quantity.of(1), Money.of("0.99")).isEmpty());
    }

    @Test
    void twoForAmountWithExactGroup() {
        Discount d = new QuantityForAmountOffer(2, Money.of("2.00")).discountFor(toothbrush, Quantity.of(2), Money.of("1.20")).orElseThrow();
        assertEquals("2 for 2.00", d.description());
        assertEquals(Money.of("-0.40"), d.discountAmount());
    }

    @Test
    void twoForAmountAppliesPerCompleteGroup() {
        Discount d = new QuantityForAmountOffer(2, Money.of("1.50")).discountFor(toothbrush, Quantity.of(4), Money.of("0.99")).orElseThrow();
        assertEquals("2 for 1.50", d.description());
        assertEquals(Money.of("-0.96"), d.discountAmount());
    }

    @Test
    void fiveForAmountBelowGroupSizeHasNoDiscount() {
        assertTrue(new QuantityForAmountOffer(5, Money.of("3.99")).discountFor(toothbrush, Quantity.of(4), Money.of("0.99")).isEmpty());
    }

    @Test
    void fiveForAmountWithLeftoverPaysFullPriceForTheRest() {
        Discount d = new QuantityForAmountOffer(5, Money.of("10.00")).discountFor(toothbrush, Quantity.of(6), Money.of("2.59")).orElseThrow();
        assertEquals("5 for 10.00", d.description());
        assertEquals(Money.of("-2.95"), d.discountAmount());
    }

    @Test
    void fiveForAmountAppliesTwiceForTenUnits() {
        Discount d = new QuantityForAmountOffer(5, Money.of("3.99")).discountFor(toothbrush, Quantity.of(10), Money.of("0.99")).orElseThrow();
        assertEquals(Money.of("-1.92"), d.discountAmount());
    }
}
