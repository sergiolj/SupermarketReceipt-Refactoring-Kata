package dojo.supermarket;

import dojo.supermarket.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiptPrinterTest {

    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
    private final Product apples = new Product("apples", ProductUnit.KILO);

    @Test
    void printsEmptyReceiptWithOnlyTotal() {
        String out = new ReceiptPrinter(20).printReceipt(new Receipt());
        assertEquals("\nTotal:          0.00\n", out);
    }

    @Test
    void printsSingleUnitItemWithoutQuantityLine() {
        Receipt receipt = new Receipt();
        receipt.addProduct(toothbrush, 1, 0.99, 0.99);
        String out = new ReceiptPrinter(20).printReceipt(receipt);
        assertEquals("toothbrush      0.99\n\nTotal:          0.99\n", out);
    }

    @Test
    void printsDiscreteQuantityAsInteger() {
        Receipt receipt = new Receipt();
        receipt.addProduct(toothbrush, 3, 0.99, 2.97);
        String out = new ReceiptPrinter(20).printReceipt(receipt);
        assertEquals("toothbrush      2.97\n  0.99 * 3\n\nTotal:          2.97\n", out);
    }

    @Test
    void printsWeightedQuantityWithThreeDecimals() {
        Receipt receipt = new Receipt();
        receipt.addProduct(apples, 2.5, 1.99, 4.975);
        String out = new ReceiptPrinter(20).printReceipt(receipt);
        assertEquals("apples          4.98\n  1.99 * 2.500\n\nTotal:          4.98\n", out);
    }

    @Test
    void printsDiscountLineAndDiscountedTotal() {
        Receipt receipt = new Receipt();
        receipt.addProduct(toothbrush, 3, 0.99, 2.97);
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
        receipt.addProduct(new Product("very-long-product-name", ProductUnit.EACH), 1, 1.0, 1.0);
        String out = new ReceiptPrinter(10).printReceipt(receipt);
        assertEquals("very-long-product-name1.00\n\nTotal: 1.00\n", out);
    }
}
