package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductQuantityTest {

    private final Product rice = new Product("rice", ProductUnit.EACH);
    private final Product apples = new Product("apples", ProductUnit.KILO);

    @Test
    void createsProductQuantityWithValidData() {
        ProductQuantity productQuantity = new ProductQuantity(rice, 2);

        assertEquals(rice, productQuantity.product());
        assertEquals(2.0, productQuantity.quantity());
    }

    @Test
    void rejectsNullProduct() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProductQuantity(null, 1)
        );
    }

    @Test
    void rejectsNegativeQuantity() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProductQuantity(rice, -1)
        );
    }

    @Test
    void rejectsNaNQuantity() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProductQuantity(rice, Double.NaN)
        );
    }

    @Test
    void rejectsPositiveInfinityQuantity() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProductQuantity(rice, Double.POSITIVE_INFINITY)
        );
    }

    @Test
    void rejectsNegativeInfinityQuantity() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProductQuantity(rice, Double.NEGATIVE_INFINITY)
        );
    }

    @Test
    void acceptsZeroQuantity() {
        ProductQuantity productQuantity = new ProductQuantity(rice, 0);

        assertEquals(0.0, productQuantity.quantity());
    }

    @Test
    void acceptsFractionalQuantityForWeightedProduct() {
        ProductQuantity productQuantity = new ProductQuantity(apples, 1.5);

        assertEquals(1.5, productQuantity.quantity());
    }

    @Test
    void rejectsFractionalQuantityForDiscreteProduct() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProductQuantity(rice, 1.5)
        );
    }

    @Test
    void acceptsIntegerQuantityForDiscreteProduct() {
        ProductQuantity productQuantity = new ProductQuantity(rice, 3);

        assertEquals(3.0, productQuantity.quantity());
    }
}