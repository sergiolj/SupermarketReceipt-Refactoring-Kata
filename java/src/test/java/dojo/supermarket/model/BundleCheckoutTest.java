package dojo.supermarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Cenários do kata para a oferta de pacote, do cadastro no Teller até a nota fiscal. */
class BundleCheckoutTest {

    private static final double DELTA = 0.001;
    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
    private final Product toothpaste = new Product("toothpaste", ProductUnit.EACH);
    private Teller teller;

    @BeforeEach
    void setUp() {
        SupermarketCatalog catalog = new FakeCatalog();
        catalog.addProduct(toothbrush, 0.99);
        catalog.addProduct(toothpaste, 1.79);
        teller = new Teller(catalog);
        teller.addBundleOffer(List.of(toothbrush, toothpaste), 10.0);
    }

    private Receipt checkout(double brushes, double pastes) {
        ShoppingCart cart = new ShoppingCart();
        if (brushes > 0) cart.addItemQuantity(toothbrush, brushes);
        if (pastes > 0) cart.addItemQuantity(toothpaste, pastes);
        return teller.checksOutArticlesFrom(cart);
    }

    @Test
    void oneToothbrushAndOneToothpasteGetTenPercentOff() {
        Receipt receipt = checkout(1, 1);
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(2.502, receipt.getTotalPrice(), DELTA); // 2.78 - 0.278
    }

    @Test
    void twoToothbrushesAndOneToothpasteGetTheSameDiscountAsOneOfEach() {
        Receipt receipt = checkout(2, 1);
        assertEquals(-0.278, receipt.getDiscounts().get(0).discountAmount(), DELTA);
        assertEquals(3.492, receipt.getTotalPrice(), DELTA); // 3.77 - 0.278
    }

    @Test
    void incompleteBundleGetsNoDiscount() {
        Receipt receipt = checkout(3, 0);
        assertEquals(0, receipt.getDiscounts().size());
        assertEquals(2.97, receipt.getTotalPrice(), DELTA);
    }

    @Test
    void bundleDiscountAddsToProductOffers() {
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);
        Receipt receipt = checkout(3, 1);
        // 3 escovas + 1 pasta = 4.76; 3 por 2: -0.99; pacote: -0.278
        assertEquals(2, receipt.getDiscounts().size());
        assertEquals(3.492, receipt.getTotalPrice(), DELTA);
    }
}
