package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TellerTest {

    @Test
    void acceptsCustomOfferStrategyWithoutChangingExistingClasses() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product rice = new Product("rice", ProductUnit.EACH);
        catalog.addProduct(rice, Money.of("2.00"));

        // Nova promoção: 1.00 de desconto fixo a partir de duas unidades.
        OfferStrategy oneOffFromTwoUnits = (product, quantity, unitPrice) ->
                quantity >= 2 ? Optional.of(new Discount(product, "1.00 off", Money.of("-1.00"))) : Optional.empty();

        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(rice, oneOffFromTwoUnits);
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(rice, 2);

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertEquals(1, receipt.getDiscounts().size());
        assertEquals("1.00 off", receipt.getDiscounts().get(0).description());
        assertEquals(Money.of("3.00"), receipt.getTotalPrice());
    }
}
