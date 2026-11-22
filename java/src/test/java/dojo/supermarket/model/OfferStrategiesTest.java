package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OfferStrategiesTest {

    @Test
    void createsStrategyForEachOfferType() {
        assertEquals(new ThreeForTwoOffer(), OfferStrategies.from(SpecialOfferType.THREE_FOR_TWO, 0));
        assertEquals(new PercentDiscountOffer(10.0), OfferStrategies.from(SpecialOfferType.TEN_PERCENT_DISCOUNT, 10.0));
        assertEquals(new QuantityForAmountOffer(2, 1.5), OfferStrategies.from(SpecialOfferType.TWO_FOR_AMOUNT, 1.5));
        assertEquals(new QuantityForAmountOffer(5, 7.49), OfferStrategies.from(SpecialOfferType.FIVE_FOR_AMOUNT, 7.49));
    }
}
