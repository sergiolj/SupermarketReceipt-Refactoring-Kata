package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {

    private final Product rice = new Product("rice", ProductUnit.EACH);

    @Test
    void emptyReceiptTotalsZero() {
        assertEquals(Money.ZERO, new Receipt().getTotalPrice());
    }

    @Test
    void totalIsSumOfItemsPlusDiscounts() {
        Receipt receipt = new Receipt();
        receipt.addProduct(rice, 2, Money.of("2.49"));
        receipt.addProduct(rice, 1, Money.of("2.49"));

        receipt.addDiscount(new Discount(rice, "10.0% off", Money.of("-0.75")));
        assertEquals(Money.of("6.72"), receipt.getTotalPrice());
    }

    @Test
    void addProductCreatesReceiptItem() {
        Receipt receipt = new Receipt();
        receipt.addProduct(rice, 2, Money.of("2.49"));
        ReceiptItem item = receipt.getItems().get(0);
        assertEquals(new ReceiptItem(rice, 2, Money.of("2.49")), item);
    }

    @Test
    void itemsListIsUnmodifiable() {
        Receipt receipt = new Receipt();
        receipt.addProduct(rice, 1, Money.of("2.49"));
        assertThrows(UnsupportedOperationException.class, () -> receipt.getItems().clear());
    }
    
    @Test
    void discountsListIsUnmodifiable() {
        Receipt receipt = new Receipt();
        receipt.addDiscount(new Discount(rice, "10.0% off", Money.of("-0.25")));
        assertThrows(UnsupportedOperationException.class, () -> receipt.getDiscounts().clear());
    }

    @Test
    void discountsAreRecorded() {
        Receipt receipt = new Receipt();
        Discount d = new Discount(rice, "3 for 2", Money.of("-2.49"));
        receipt.addDiscount(d);
        assertEquals(1, receipt.getDiscounts().size());
        assertSame(d, receipt.getDiscounts().get(0));
    }
}
