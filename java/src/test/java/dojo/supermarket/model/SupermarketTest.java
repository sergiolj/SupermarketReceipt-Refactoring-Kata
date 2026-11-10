package dojo.supermarket.model;

import dojo.supermarket.ReceiptPrinter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SupermarketTest {

    // Todo: test all kinds of discounts are applied properly

    @Test
    void tenPercentDiscount() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, 0.99);
        Product apples = new Product("apples", ProductUnit.KILO);
        catalog.addProduct(apples, 1.99);

        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, toothbrush, 10.0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(apples, 2.5);
        
        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        assertEquals(4.975, receipt.getTotalPrice(), 0.01);
        assertEquals(Collections.emptyList(), receipt.getDiscounts());
        assertEquals(1, receipt.getItems().size());
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertEquals(apples, receiptItem.product());
        assertEquals(1.99, receiptItem.price());
        assertEquals(2.5*1.99, receiptItem.totalPrice());
        assertEquals(2.5, receiptItem.quantity());

    }

    @Test
    void threeForTwoDiscount() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, 0.99);
    	
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0.99);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 3);
        
        double legacyDiscountValue = (2 * 0.99) - (3 * 0.99);
        
        Discount threeForTwo =new Discount(toothbrush, "3 for 2",legacyDiscountValue);
        List<Discount> discountsList = new ArrayList<>();
        discountsList.add(threeForTwo);
 
        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        assertEquals(1.98, receipt.getTotalPrice(), 0.01);
        assertEquals(discountsList, receipt.getDiscounts());
        assertEquals(1, receipt.getItems().size());
        
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertEquals(toothbrush, receiptItem.product());
        assertEquals(0.99, receiptItem.price());
        assertEquals(2.96, receiptItem.totalPrice(),0.01);
        assertEquals(3, receiptItem.quantity());
    }

}
