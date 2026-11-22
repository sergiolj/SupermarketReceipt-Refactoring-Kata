package dojo.supermarket.model;

import dojo.supermarket.ReceiptPrinter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupermarketTest {
	
	private static final double DELTA = 0.001;
	
	private static void assertDiscount(Discount d, Product product, String description, double totalDiscountForProducts) {
	    assertEquals(product, d.product());
	    assertEquals(description, d.description());
	    assertEquals(totalDiscountForProducts, d.discountAmount(), DELTA);
	}
	
	private static void assertReceiptItemDescriptionAndPrice(ReceiptItem receiptItem, Product product, 
			double itemPrice, double totalWithoutDiscount, int itemQuantity) {
        assertEquals(product, receiptItem.product());
        assertEquals(itemPrice, receiptItem.price());
        assertEquals(totalWithoutDiscount, receiptItem.totalPrice(), DELTA);
        assertEquals(itemQuantity, receiptItem.quantity());
	}
	
	
    @Test
    void tenPercentDiscount() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, 0.99);

        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, toothbrush, 10.0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 10);
        
        Receipt receipt = teller.checksOutArticlesFrom(cart);
        assertEquals(8.91, receipt.getTotalPrice(), DELTA);
        assertEquals(1, receipt.getItems().size());
        
        Discount tenPercent = receipt.getDiscounts().get(0);
        assertDiscount(tenPercent, toothbrush, "10.0% off", -0.99);
        
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertReceiptItemDescriptionAndPrice(receiptItem, toothbrush, 0.99, 9.90, 10);
    }

    @Test
    void threeForTwoDiscountExactNumberOfOfferItens() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        
        catalog.addProduct(toothbrush, 0.99);
    	
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 3);
 
        Receipt receipt = teller.checksOutArticlesFrom(cart);
        assertEquals(1.98, receipt.getTotalPrice(), DELTA);
        assertEquals(1,receipt.getItems().size());
        
        Discount threeForTwo = receipt.getDiscounts().get(0);
        assertDiscount(threeForTwo, toothbrush, "3 for 2", -0.99);

        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertReceiptItemDescriptionAndPrice(receiptItem, toothbrush, 0.99, 2.97, 3);
    }
    
    @Test
    void threeForTowDiscountNotAppliedForTwoItems() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        
        catalog.addProduct(toothbrush, 0.99);
    	
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 2);
 
        Receipt receipt = teller.checksOutArticlesFrom(cart);
        assertEquals(1.98, receipt.getTotalPrice(), DELTA);
        assertEquals(1,receipt.getItems().size());
        
        assertTrue(receipt.getDiscounts().isEmpty());

        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertReceiptItemDescriptionAndPrice(receiptItem, toothbrush, 0.99, 1.98, 2);
    }
    
    @Test
    void threeForTowOneItemExcedesOffer() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        
        catalog.addProduct(toothbrush, 0.99);
    	
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 4);
 
        Receipt receipt = teller.checksOutArticlesFrom(cart);
        assertEquals(2.97, receipt.getTotalPrice(), DELTA);
        assertEquals(1,receipt.getItems().size());
        
        Discount threeForTwo = receipt.getDiscounts().get(0);
        assertDiscount(threeForTwo, toothbrush, "3 for 2", -0.99);

        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertReceiptItemDescriptionAndPrice(receiptItem, toothbrush, 0.99, 3.96, 4);
    }
    
    @Test
    void twoForAmountExactNumberOfOfferItens() {
    	  SupermarketCatalog catalog = new FakeCatalog();
          Product soup = new Product("soup", ProductUnit.EACH);
          
          catalog.addProduct(soup, 1.35);
          
          Teller teller = new Teller(catalog);
          teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, soup, 2.50);
          
          ShoppingCart cart = new ShoppingCart();
          cart.addItemQuantity(soup, 2);
          
          Receipt receipt = teller.checksOutArticlesFrom(cart);
          assertEquals(2.50, receipt.getTotalPrice(), DELTA);
          assertEquals(1, receipt.getItems().size());
         
          Discount twoForAmount = receipt.getDiscounts().get(0);
          assertDiscount(twoForAmount, soup, "2 for 2.5", -0.20);
          
          ReceiptItem receiptItem = receipt.getItems().get(0);
          assertReceiptItemDescriptionAndPrice(receiptItem, soup, 1.35, 2.70, 2);
    }
    
    @Test
    void twoForAmountOneItemExcedesOffer() {
    	  SupermarketCatalog catalog = new FakeCatalog();
          Product soup = new Product("soup", ProductUnit.EACH);
          
          catalog.addProduct(soup, 1.35);
          
          Teller teller = new Teller(catalog);
          teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, soup, 2.50);
          
          ShoppingCart cart = new ShoppingCart();
          cart.addItemQuantity(soup, 3);
   
          Receipt receipt = teller.checksOutArticlesFrom(cart);
          assertEquals(3.85, receipt.getTotalPrice(), DELTA);
          assertEquals(1, receipt.getItems().size());
          
          Discount twoForAmount = receipt.getDiscounts().get(0);
          assertDiscount(twoForAmount, soup, "2 for 2.5", -0.20);
          
          ReceiptItem receiptItem = receipt.getItems().get(0);
          assertReceiptItemDescriptionAndPrice(receiptItem, soup, 1.35, 4.05, 3);
    }
    
    @Test
    void twoForAmountDiscountNotAppliedForOneItem() {
      	  SupermarketCatalog catalog = new FakeCatalog();
          Product soup = new Product("soup", ProductUnit.EACH);
          
          catalog.addProduct(soup, 1.35);
          
          Teller teller = new Teller(catalog);
          teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, soup, 2.50);
          
          ShoppingCart cart = new ShoppingCart();
          cart.addItemQuantity(soup, 1);
          
          Receipt receipt = teller.checksOutArticlesFrom(cart);
          assertEquals(1.35, receipt.getTotalPrice(), DELTA);
          assertEquals(1, receipt.getItems().size());
         
          assertTrue(receipt.getDiscounts().isEmpty());
          
          ReceiptItem receiptItem = receipt.getItems().get(0);
          assertReceiptItemDescriptionAndPrice(receiptItem, soup, 1.35, 1.35, 1);
    }
    
    @Test
    void fiveForAmountExactNumberOfOfferItens() {
  	  SupermarketCatalog catalog = new FakeCatalog();
      Product deodorant = new Product("deodorant", ProductUnit.EACH);
      
      catalog.addProduct(deodorant, 2.59);
      
      Teller teller = new Teller(catalog);
      teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, deodorant, 10.00);
      
      ShoppingCart cart = new ShoppingCart();
      cart.addItemQuantity(deodorant, 5);
      
      Receipt receipt = teller.checksOutArticlesFrom(cart);
      assertEquals(10.00, receipt.getTotalPrice(), DELTA);
      assertEquals(1, receipt.getItems().size());

      Discount fiveForAmount = receipt.getDiscounts().get(0);
      assertDiscount(fiveForAmount, deodorant, "5 for 10.0", -2.95);

      ReceiptItem receiptItem = receipt.getItems().get(0);
      assertReceiptItemDescriptionAndPrice(receiptItem, deodorant, 2.59, 12.95, 5);

    }
    
    @Test
    void fiveForAmountOneItemExcedesOffer() {
  	  SupermarketCatalog catalog = new FakeCatalog();
      Product deodorant = new Product("deodorant", ProductUnit.EACH);
      
      catalog.addProduct(deodorant, 2.59);
      
      Teller teller = new Teller(catalog);
      teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, deodorant, 10.00);
      
      ShoppingCart cart = new ShoppingCart();
      cart.addItemQuantity(deodorant, 6);
      
      Receipt receipt = teller.checksOutArticlesFrom(cart);
      assertEquals(12.59, receipt.getTotalPrice(), DELTA);
      assertEquals(1, receipt.getItems().size());
      
      Discount fiveForAmount = receipt.getDiscounts().get(0);
      assertDiscount(fiveForAmount, deodorant, "5 for 10.0", -2.95);

      ReceiptItem receiptItem = receipt.getItems().get(0);
      assertReceiptItemDescriptionAndPrice(receiptItem, deodorant, 2.59, 15.54, 6);
    }
 
        @Test
    void fiveForAmountDiscountNotAplliedForFourItems() {
    	  SupermarketCatalog catalog = new FakeCatalog();
          Product deodorant = new Product("deodorant", ProductUnit.EACH);
          
          catalog.addProduct(deodorant, 2.59);
          
          Teller teller = new Teller(catalog);
          teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, deodorant, 10.00);
          
          ShoppingCart cart = new ShoppingCart();
          cart.addItemQuantity(deodorant, 4);
          
          Receipt receipt = teller.checksOutArticlesFrom(cart);
          assertEquals(10.36, receipt.getTotalPrice(), DELTA);
          assertEquals(1, receipt.getItems().size());
          
          assertTrue(receipt.getDiscounts().isEmpty());
         
          ReceiptItem receiptItem = receipt.getItems().get(0);
          assertReceiptItemDescriptionAndPrice(receiptItem, deodorant, 2.59, 10.36, 4);
    }
    
    @Test
    void emptyCart() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, 0.99);
        
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, toothbrush, 10.0);
        
        ShoppingCart cart = new ShoppingCart();
        
        Receipt receipt = teller.checksOutArticlesFrom(cart);
        assertEquals(0, receipt.getTotalPrice(), DELTA);
        assertTrue(receipt.getItems().isEmpty());
        assertTrue(receipt.getDiscounts().isEmpty());  
    }
    
    @Test
    void offerForProductNotInCart() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, 0.99);

        Product soup = new Product("soup", ProductUnit.EACH);
        catalog.addProduct(soup, 1.99);
        
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, toothbrush, 10.0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(soup, 5);
        
        Receipt receipt = teller.checksOutArticlesFrom(cart);
        assertEquals(9.95, receipt.getTotalPrice(), DELTA);
        assertEquals(1, receipt.getItems().size());
        
        assertTrue(receipt.getDiscounts().isEmpty());
        
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertReceiptItemDescriptionAndPrice(receiptItem, soup, 1.99, 9.95, 5);
    }
    

}
