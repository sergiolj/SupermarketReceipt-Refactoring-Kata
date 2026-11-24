package dojo.supermarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpecialOffersTest {

    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
    private final Product apples = new Product("apples", ProductUnit.KILO);
    private SupermarketCatalog catalog;
    private SpecialOffers offers;
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        catalog = new FakeCatalog();
        catalog.addProduct(toothbrush, Money.of("0.99"));
        catalog.addProduct(apples, Money.of("1.99"));
        offers = new SpecialOffers();
        receipt = new Receipt();
    }

    @Test
    void productsWithoutOfferGetNoDiscount() {
        offers.applyTo(Map.of(toothbrush, Quantity.of(3.0)), catalog, receipt);
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void offerForAnotherProductDoesNotApply() {
        offers.add(apples, new PercentDiscountOffer(10));
        offers.applyTo(Map.of(toothbrush, Quantity.of(3.0)), catalog, receipt);
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void applicableOfferAddsDiscountToReceipt() {
        offers.add(toothbrush, new ThreeForTwoOffer());
        offers.applyTo(Map.of(toothbrush, Quantity.of(3.0)), catalog, receipt);
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(Money.of("-0.99"), receipt.getDiscounts().get(0).discountAmount());
    }

    @Test
    void offerBelowItsMinimumAddsNothing() {
        offers.add(toothbrush, new ThreeForTwoOffer());
        offers.applyTo(Map.of(toothbrush, Quantity.of(2.0)), catalog, receipt);
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void newOfferForSameProductReplacesPreviousOne() {
        offers.add(toothbrush, new ThreeForTwoOffer());
        offers.add(toothbrush, new PercentDiscountOffer(50));
        offers.applyTo(Map.of(toothbrush, Quantity.of(3.0)), catalog, receipt);
        assertEquals("50.0% off", receipt.getDiscounts().get(0).description());
    }
}
