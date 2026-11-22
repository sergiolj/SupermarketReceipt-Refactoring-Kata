package dojo.supermarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BundleOfferTest {

    private static final double DELTA = 0.001;
    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
    private final Product toothpaste = new Product("toothpaste", ProductUnit.EACH);
    private final Product apples = new Product("apples", ProductUnit.KILO);
    private SupermarketCatalog catalog;
    private BundleOffer bundle;

    @BeforeEach
    void setUp() {
        catalog = new FakeCatalog();
        catalog.addProduct(toothbrush, 0.99);
        catalog.addProduct(toothpaste, 1.79);
        catalog.addProduct(apples, 1.99);
        bundle = new BundleOffer(List.of(toothbrush, toothpaste), 10.0);
    }

    @Test
    void oneCompleteBundleGetsPercentOffTheirTotal() {
        Discount d = bundle.discountFor(Map.of(toothbrush, 1.0, toothpaste, 1.0), catalog).orElseThrow();
        assertEquals(toothbrush, d.product());
        assertEquals("bundle 10.0% off", d.description());
        assertEquals(-0.278, d.discountAmount(), DELTA); // 10% de 0.99 + 1.79
    }

    @Test
    void extraItemsOutsideACompleteBundleAreNotDiscounted() {
        Discount d = bundle.discountFor(Map.of(toothbrush, 2.0, toothpaste, 1.0), catalog).orElseThrow();
        assertEquals(-0.278, d.discountAmount(), DELTA);
    }

    @Test
    void twoCompleteBundlesDoubleTheDiscount() {
        Discount d = bundle.discountFor(Map.of(toothbrush, 2.0, toothpaste, 2.0), catalog).orElseThrow();
        assertEquals(-0.556, d.discountAmount(), DELTA);
    }

    @Test
    void noDiscountWhenAProductIsMissing() {
        assertTrue(bundle.discountFor(Map.of(toothbrush, 3.0), catalog).isEmpty());
    }

    @Test
    void noDiscountForAnEmptyCart() {
        assertTrue(bundle.discountFor(Map.of(), catalog).isEmpty());
    }

    @Test
    void weightedProductsCountOnlyWholeUnits() {
        BundleOffer withApples = new BundleOffer(List.of(toothbrush, apples), 10.0);
        assertTrue(withApples.discountFor(Map.of(toothbrush, 1.0, apples, 0.8), catalog).isEmpty());
        Discount d = withApples.discountFor(Map.of(toothbrush, 1.0, apples, 1.5), catalog).orElseThrow();
        assertEquals(-0.298, d.discountAmount(), DELTA); // 10% de 0.99 + 1.99, um pacote
    }

    @Test
    void bundleNeedsAtLeastTwoDistinctProducts() {
        assertThrows(IllegalArgumentException.class, () -> new BundleOffer(List.of(toothbrush), 10.0));
        assertThrows(IllegalArgumentException.class, () -> new BundleOffer(List.of(toothbrush, toothbrush), 10.0));
    }
}
