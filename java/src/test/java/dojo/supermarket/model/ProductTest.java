package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void createsProductWithValidData() {
        Product product = new Product("rice", ProductUnit.EACH);

        assertEquals("rice", product.name());
        assertEquals(ProductUnit.EACH, product.unit());
    }

    @Test
    void rejectsNullName() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Product(null, ProductUnit.EACH)
        );
    }

    @Test
    void rejectsEmptyName() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Product("", ProductUnit.EACH)
        );
    }

    @Test
    void rejectsBlankName() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Product("   ", ProductUnit.EACH)
        );
    }

    @Test
    void rejectsNullUnit() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Product("rice", null)
        );
    }
}