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
        double unitPrice = 0.99;
        
        catalog.addProduct(toothbrush, unitPrice);
    	
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, unitPrice);

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
    
    @Test
    void twoForAmountExactNumberOfOfferItens() {
    	  SupermarketCatalog catalog = new FakeCatalog();
          Product soup = new Product("soup", ProductUnit.EACH);
          double unitPrice = 1.20;
          double offerArgument = 2.00;
          int qtd = 2;
          double totalDiscount = offerArgument * (qtd / 2) + qtd % 2 * unitPrice;
          double discountAmount = unitPrice * qtd - totalDiscount;
          
          catalog.addProduct(soup, unitPrice);
          
          Teller teller = new Teller(catalog);
          teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, soup, offerArgument);
          
          ShoppingCart cart = new ShoppingCart();
          cart.addItemQuantity(soup, qtd);
          
          Discount twoForAmount =new Discount(soup, "2 for " + offerArgument, -discountAmount);
          List<Discount> discountsList = new ArrayList<>();
          discountsList.add(twoForAmount);
   
          Receipt receipt = teller.checksOutArticlesFrom(cart);

          // ASSERT
          assertEquals(2.00, receipt.getTotalPrice(), 0.01);
          assertEquals(discountsList, receipt.getDiscounts());
          assertEquals(1, receipt.getItems().size());
          
          ReceiptItem receiptItem = receipt.getItems().get(0);
          assertEquals(soup, receiptItem.product());
          assertEquals(1.20, receiptItem.price());
          assertEquals(2.40, receiptItem.totalPrice(),0.01);
          assertEquals(2, receiptItem.quantity());
    }
    
    @Test
    void twoForAmountOneItemExcedesOffer() {
    	  SupermarketCatalog catalog = new FakeCatalog();
          Product soup = new Product("soup", ProductUnit.EACH);
          double unitPrice = 1.20;
          double offerArgument = 2.00;
          int qtd = 3;
          double totalDiscount = offerArgument * (qtd / 2) + qtd % 2 * unitPrice;
          double discountAmount = unitPrice * qtd - totalDiscount;
          
          catalog.addProduct(soup, unitPrice);
          
          Teller teller = new Teller(catalog);
          teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, soup, offerArgument);
          
          ShoppingCart cart = new ShoppingCart();
          cart.addItemQuantity(soup, qtd);
          
          Discount twoForAmount =new Discount(soup, "2 for " + offerArgument, -discountAmount);
          List<Discount> discountsList = new ArrayList<>();
          discountsList.add(twoForAmount);
   
          Receipt receipt = teller.checksOutArticlesFrom(cart);

          // ASSERT
          assertEquals(3.20, receipt.getTotalPrice(), 0.01);
          assertEquals(discountsList, receipt.getDiscounts());
          assertEquals(1, receipt.getItems().size());
          
          ReceiptItem receiptItem = receipt.getItems().get(0);
          assertEquals(soup, receiptItem.product());
          assertEquals(1.20, receiptItem.price());
          assertEquals(3.60, receiptItem.totalPrice(),0.01);
          assertEquals(3, receiptItem.quantity());
    }

}
