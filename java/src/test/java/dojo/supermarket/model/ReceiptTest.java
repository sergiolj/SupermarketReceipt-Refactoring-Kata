package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {

    private final Product rice = new Product("rice", ProductUnit.EACH);

    @Test
    void emptyReceiptTotalsZero() {
        assertEquals(0.0, new Receipt().getTotalPrice(), 0.0001);
    }

    @Test
    void totalIsSumOfItemsPlusDiscounts() {
        Receipt receipt = new Receipt();
        receipt.addProduct(rice, 2, 2.49, 4.98);
        receipt.addProduct(rice, 1, 2.49, 2.49);
        // descontos são valores negativos
        receipt.addDiscount(new Discount(rice, "10.0% off", -0.747));
        assertEquals(6.723, receipt.getTotalPrice(), 0.0001);
    }

    @Test
    void addProductCreatesReceiptItem() {
        Receipt receipt = new Receipt();
        receipt.addProduct(rice, 2, 2.49, 4.98);
        ReceiptItem item = receipt.getItems().get(0);
        assertEquals(new ReceiptItem(rice, 2, 2.49, 4.98), item);
    }

    @Test
    void itemsListIsUnmodifiable() {
        Receipt receipt = new Receipt();
        receipt.addProduct(rice, 1, 2.49, 2.49);
        assertThrows(UnsupportedOperationException.class, () -> receipt.getItems().clear());
    }

    @Test
    void discountsAreRecorded() {
        Receipt receipt = new Receipt();
        Discount d = new Discount(rice, "3 for 2", -2.49);
        receipt.addDiscount(d);
        assertEquals(1, receipt.getDiscounts().size());
        assertSame(d, receipt.getDiscounts().get(0));
    }
}
