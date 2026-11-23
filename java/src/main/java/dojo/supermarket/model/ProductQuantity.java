package dojo.supermarket.model;

public record ProductQuantity(Product product, double quantity) {

    public ProductQuantity {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (!Double.isFinite(quantity) || quantity < 0) {
            throw new IllegalArgumentException("Quantity must be finite and non-negative");
        }

        if (!product.unit().acceptsQuantity(quantity)) {
            throw new IllegalArgumentException("Quantity must be an integer for discrete units");
        }
    }
}