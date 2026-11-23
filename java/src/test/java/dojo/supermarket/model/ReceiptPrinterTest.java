package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiptPrinterTest {

    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
    private final Product apples = new Product("apples", ProductUnit.KILO);

    
    @Test   
    void printSingleReceiptItemWithoutDiscountWithColumnArgument() {
    	Receipt receipt = new Receipt();
    	receipt.addProduct(apples, 2.50, 1.25);
    	ReceiptPrinter printer = new ReceiptPrinter(20);
    	String outString = printer.printReceipt(receipt);
    	
    	assertEquals("apples" + " ".repeat(10) + "3.13\n" 
    				+ "  " + "1.25" + " * " + "2.500\n"
    				+ "\n"
    				+ "Total: " + " ".repeat(9) + "3.13\n", outString);
    }
    
    @Test   
    void printSingleReceiptItemWithoutDiscountDefautlColumnArgument() {
    	Receipt receipt = new Receipt();

    	receipt.addProduct(apples, 2.50, 1.25);
    	ReceiptPrinter printer = new ReceiptPrinter();
    	String outString = printer.printReceipt(receipt);

    	assertEquals("apples" + " ".repeat(30) + "3.13\n" 
    				+ "  " + "1.25" + " * " + "2.500\n"
    				+ "\n"
    				+ "Total: " + " ".repeat(29) + "3.13\n", outString);
    }
    
    
    @Test   
    void printLessThanOneKilo() {
    	Receipt receipt = new Receipt();
    	
    	receipt.addProduct(apples, 0.50, 1.20);
    	ReceiptPrinter printer = new ReceiptPrinter();
    	String outString = printer.printReceipt(receipt);

    	assertEquals("apples" + " ".repeat(30) + "0.60\n" 
    				+ "  " + "1.20" + " * " + "0.500\n"
    				+ "\n"
    				+ "Total: " + " ".repeat(29) + "0.60\n", outString);
    }
    
    @Test   
    void printsOneKiloItemHasNoMultiplicationLine() {
    	Receipt receipt = new Receipt();
    	receipt.addProduct(apples, 1.00, 1.25);
    	ReceiptPrinter printer = new ReceiptPrinter();
    	String outString = printer.printReceipt(receipt);
    	
    	assertEquals("apples" + " ".repeat(30) + "1.25\n" 
    				+ "\n"
    				+ "Total: " + " ".repeat(29) + "1.25\n", outString);
    }
    
    @Test
    void printsEmptyReceiptWithOnlyTotal() {
        String out = new ReceiptPrinter(20).printReceipt(new Receipt());
        assertEquals("\nTotal:          0.00\n", out);
    }

    @Test
    void printsSingleUnitItemWithoutQuantityLine() {
        Receipt receipt = new Receipt();
        receipt.addProduct(toothbrush, 1, 0.99);
        String out = new ReceiptPrinter(20).printReceipt(receipt);
        assertEquals("toothbrush      0.99\n\nTotal:          0.99\n", out);
    }

    @Test
    void printsDiscreteQuantityAsInteger() {
        Receipt receipt = new Receipt();
        receipt.addProduct(toothbrush, 3, 0.99);
        String out = new ReceiptPrinter(20).printReceipt(receipt);
        assertEquals("toothbrush      2.97\n  0.99 * 3\n\nTotal:          2.97\n", out);
    }

    @Test
    void printsWeightedQuantityWithThreeDecimals() {
        Receipt receipt = new Receipt();
        receipt.addProduct(apples, 2.5, 1.99);
        String out = new ReceiptPrinter(20).printReceipt(receipt);
        assertEquals("apples          4.98\n  1.99 * 2.500\n\nTotal:          4.98\n", out);
    }

    @Test
    void printsDiscountLineAndDiscountedTotal() {
        Receipt receipt = new Receipt();
        receipt.addProduct(toothbrush, 3, 0.99);
        receipt.addDiscount(new Discount(toothbrush, "3 for 2", -0.99));
        String out = new ReceiptPrinter(30).printReceipt(receipt);
        assertEquals(
                "toothbrush                2.97\n"
                + "  0.99 * 3\n"
                + "3 for 2(toothbrush)      -0.99\n"
                + "\n"
                + "Total:                    1.98\n", out);
    }

    @Test
    void longNamesAreNotTruncatedNorPadded() {
        Receipt receipt = new Receipt();
        receipt.addProduct(new Product("very-long-product-name", ProductUnit.EACH), 1, 1.0);
        String out = new ReceiptPrinter(10).printReceipt(receipt);
        assertEquals("very-long-product-name1.00\n\nTotal: 1.00\n", out);
    }
}
