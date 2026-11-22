package dojo.supermarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpecialOffersTest {

    private static final double DELTA = 0.001;
    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
    private final Product apples = new Product("apples", ProductUnit.KILO);
    private SupermarketCatalog catalog;
    private SpecialOffers offers;
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        catalog = new FakeCatalog();
        catalog.addProduct(toothbrush, 0.99);
        catalog.addProduct(apples, 1.99);
        offers = new SpecialOffers();
        receipt = new Receipt();
    }

    @Test
    void productsWithoutOfferGetNoDiscount() {
        offers.applyTo(Map.of(toothbrush, 3.0), catalog, receipt);
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void offerForAnotherProductDoesNotApply() {
        offers.add(apples, new PercentDiscountOffer(10));
        offers.applyTo(Map.of(toothbrush, 3.0), catalog, receipt);
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void applicableOfferAddsDiscountToReceipt() {
        offers.add(toothbrush, new ThreeForTwoOffer());
        offers.applyTo(Map.of(toothbrush, 3.0), catalog, receipt);
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(-0.99, receipt.getDiscounts().get(0).discountAmount(), DELTA);
    }

    @Test
    void offerBelowItsMinimumAddsNothing() {
        offers.add(toothbrush, new ThreeForTwoOffer());
        offers.applyTo(Map.of(toothbrush, 2.0), catalog, receipt);
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void newOfferForSameProductReplacesPreviousOne() {
        offers.add(toothbrush, new ThreeForTwoOffer());
        offers.add(toothbrush, new PercentDiscountOffer(50));
        offers.applyTo(Map.of(toothbrush, 3.0), catalog, receipt);
        assertEquals("50.0% off", receipt.getDiscounts().get(0).description());
    }
}
