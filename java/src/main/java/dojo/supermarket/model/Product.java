package dojo.supermarket.model;

public record Product(String name, ProductUnit unit) {

    public Product {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }

        if (unit == null) {
            throw new IllegalArgumentException("Product unit cannot be null");
        }
    }
}
