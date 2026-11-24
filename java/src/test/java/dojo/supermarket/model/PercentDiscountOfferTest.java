package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PercentDiscountOfferTest {

    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
    private final Product apples = new Product("apples", ProductUnit.KILO);

    @Test
    void discountsPercentOfTotalForUnitProducts() {
        Discount d = new PercentDiscountOffer(10.0).discountFor(toothbrush, Quantity.of(10), Money.of("0.99")).orElseThrow();
        assertEquals(toothbrush, d.product());
        assertEquals("10.0% off", d.description());
        assertEquals(Money.of("-0.99"), d.discountAmount());
    }

    @Test
    void discountsPercentOfTotalForWeightedProducts() {
        Discount d = new PercentDiscountOffer(20.0).discountFor(apples, Quantity.of(2.5), Money.of("1.99")).orElseThrow();
        assertEquals("20.0% off", d.description());
        assertEquals(Money.of("-1.00"), d.discountAmount()); // 0.995 arredonda para 1.00
    }

    @Test
    void appliesEvenToASingleUnit() {
        Discount d = new PercentDiscountOffer(10.0).discountFor(toothbrush, Quantity.of(1), Money.of("0.99")).orElseThrow();
        assertEquals(Money.of("-0.10"), d.discountAmount()); // 0.099 arredonda para 0.10
    }
}
