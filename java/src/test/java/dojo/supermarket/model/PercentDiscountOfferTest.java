package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PercentDiscountOfferTest {

    private static final double DELTA = 0.001;
    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
    private final Product apples = new Product("apples", ProductUnit.KILO);

    @Test
    void discountsPercentOfTotalForUnitProducts() {
        Discount d = new PercentDiscountOffer(10.0).discountFor(toothbrush, 10, 0.99).orElseThrow();
        assertEquals(toothbrush, d.product());
        assertEquals("10.0% off", d.description());
        assertEquals(-0.99, d.discountAmount(), DELTA);
    }

    @Test
    void discountsPercentOfTotalForWeightedProducts() {
        Discount d = new PercentDiscountOffer(20.0).discountFor(apples, 2.5, 1.99).orElseThrow();
        assertEquals("20.0% off", d.description());
        assertEquals(-0.995, d.discountAmount(), DELTA);
    }

    @Test
    void appliesEvenToASingleUnit() {
        Discount d = new PercentDiscountOffer(10.0).discountFor(toothbrush, 1, 0.99).orElseThrow();
        assertEquals(-0.099, d.discountAmount(), DELTA);
    }
}
