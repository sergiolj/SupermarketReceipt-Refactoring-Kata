package dojo.supermarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de caracterização: registram o comportamento ATUAL do legado
 * (incluindo casos-limite) para dar segurança às refatorações.
 */
class OfferCharacterizationTest {

    private SupermarketCatalog catalog;
    private Teller teller;
    private Product toothbrush;
    private Product apples;

    @BeforeEach
    void setUp() {
        catalog = new FakeCatalog();
        toothbrush = new Product("toothbrush", ProductUnit.EACH);
        apples = new Product("apples", ProductUnit.KILO);
        catalog.addProduct(toothbrush, Money.of("0.99"));
        catalog.addProduct(apples, Money.of("1.99"));
        teller = new Teller(catalog);
    }

    private Receipt checkout(ShoppingCart cart) {
        return teller.checksOutArticlesFrom(cart);
    }

    private ShoppingCart cartWith(Product product, double quantity) {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(product, quantity);
        return cart;
    }

    private void assertSingleDiscount(Receipt receipt, Product product, String description, double amount) {
        assertEquals(1, receipt.getDiscounts().size());
        Discount d = receipt.getDiscounts().get(0);
        assertEquals(product, d.product());
        assertEquals(description, d.description());
        assertEquals(Money.of(amount), d.discountAmount());
    }

    // ---------- sem oferta ----------

    @Test
    void emptyCartHasZeroTotalAndNoItems() {
        Receipt receipt = checkout(new ShoppingCart());
        assertEquals(Money.of(0.0), receipt.getTotalPrice());
        assertTrue(receipt.getItems().isEmpty());
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void productWithoutOfferPaysFullPrice() {
        Receipt receipt = checkout(cartWith(toothbrush, 4));
        assertTrue(receipt.getDiscounts().isEmpty());
        assertEquals(Money.of(3.96), receipt.getTotalPrice());
    }

    @Test
    void offerOnAnotherProductDoesNotAffectCart() {
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 20.0);
        Receipt receipt = checkout(cartWith(toothbrush, 2));
        assertTrue(receipt.getDiscounts().isEmpty());
        assertEquals(Money.of(1.98), receipt.getTotalPrice());
    }

    // ---------- TEN_PERCENT_DISCOUNT ----------

    @Test
    void percentDiscountOnWeightedProduct() {
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 20.0);
        Receipt receipt = checkout(cartWith(apples, 2.5));
        // 2.5 * 1.99 = 4.975 ; 20% = 0.995
        assertSingleDiscount(receipt, apples, "20.0% off", -0.995);
        assertEquals(Money.of(3.98), receipt.getTotalPrice());
    }

    @Test
    void percentDiscountAppliesEvenForSingleUnit() {
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, toothbrush, 10.0);
        Receipt receipt = checkout(cartWith(toothbrush, 1));
        assertSingleDiscount(receipt, toothbrush, "10.0% off", -0.099);
    }

    // ---------- THREE_FOR_TWO ----------

    @Test
    void threeForTwoBelowThresholdHasNoDiscount() {
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);
        Receipt receipt = checkout(cartWith(toothbrush, 2));
        assertTrue(receipt.getDiscounts().isEmpty());
        assertEquals(Money.of(1.98), receipt.getTotalPrice());
    }

    @Test
    void threeForTwoWithFourItemsDiscountsOnlyOne() {
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);
        Receipt receipt = checkout(cartWith(toothbrush, 4));
        assertSingleDiscount(receipt, toothbrush, "3 for 2", -0.99);
        assertEquals(Money.of(2.97), receipt.getTotalPrice());
    }

    @Test
    void threeForTwoWithSixItemsDiscountsTwo() {
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);
        Receipt receipt = checkout(cartWith(toothbrush, 6));
        assertSingleDiscount(receipt, toothbrush, "3 for 2", -1.98);
        assertEquals(Money.of(3.96), receipt.getTotalPrice());
    }

    // ---------- TWO_FOR_AMOUNT ----------

    @Test
    void twoForAmountBelowThresholdHasNoDiscount() {
        teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, toothbrush, 1.50);
        Receipt receipt = checkout(cartWith(toothbrush, 1));
        assertTrue(receipt.getDiscounts().isEmpty());
        assertEquals(Money.of(0.99), receipt.getTotalPrice());
    }

    @Test
    void twoForAmountWithFourItemsAppliesTwiceFixedPrice() {
        teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, toothbrush, 1.50);
        Receipt receipt = checkout(cartWith(toothbrush, 4));
        // total = 1.50*2 = 3.00 ; sem oferta = 3.96 ; desconto = 0.96
        assertSingleDiscount(receipt, toothbrush, "2 for 1.50", -0.96);
        assertEquals(Money.of(3.00), receipt.getTotalPrice());
    }

    // ---------- FIVE_FOR_AMOUNT ----------

    @Test
    void fiveForAmountBelowThresholdHasNoDiscount() {
        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, toothbrush, 3.99);
        Receipt receipt = checkout(cartWith(toothbrush, 4));
        assertTrue(receipt.getDiscounts().isEmpty());
        assertEquals(Money.of(3.96), receipt.getTotalPrice());
    }

    @Test
    void fiveForAmountWithTenItemsAppliesTwice() {
        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, toothbrush, 3.99);
        Receipt receipt = checkout(cartWith(toothbrush, 10));
        // 9.90 - 2*3.99 = 1.92
        assertSingleDiscount(receipt, toothbrush, "5 for 3.99", -1.92);
        assertEquals(Money.of(7.98), receipt.getTotalPrice());
    }

    // ---------- carrinho com várias linhas / produtos ----------

    @Test
    void sameProductAddedTwiceHasQuantitiesAggregatedForOffer() {
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 1);
        cart.addItemQuantity(toothbrush, 2);

        Receipt receipt = checkout(cart);

        assertEquals(2, receipt.getItems().size()); // uma linha por adição
        assertSingleDiscount(receipt, toothbrush, "3 for 2", -0.99);
        assertEquals(Money.of(1.98), receipt.getTotalPrice());
    }

    @Test
    void multipleProductsEachGetTheirOwnDiscount() {
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 10.0);
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 3);
        cart.addItemQuantity(apples, 2.0);

        Receipt receipt = checkout(cart);

        assertEquals(2, receipt.getItems().size());
        assertEquals(2, receipt.getDiscounts().size());
        // 2.97 + 3.98 - 0.99 - 0.40 (0.398 arredondado) = 5.56
        assertEquals(Money.of(5.56), receipt.getTotalPrice());
    }

    @Test
    void lastOfferRegisteredForAProductWins() {
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, toothbrush, 50.0);
        Receipt receipt = checkout(cartWith(toothbrush, 3));
        assertSingleDiscount(receipt, toothbrush, "50.0% off", -1.485);
    }

    @Test
    void receiptItemsKeepInsertionOrder() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(apples);
        cart.addItem(toothbrush);
        Receipt receipt = checkout(cart);
        assertEquals(apples, receipt.getItems().get(0).product());
        assertEquals(toothbrush, receipt.getItems().get(1).product());
    }
}
