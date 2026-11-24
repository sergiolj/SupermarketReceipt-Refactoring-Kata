package dojo.supermarket.model;

public record ProductQuantity(Product product, Quantity quantity) {

    public ProductQuantity {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (!product.unit().acceptsQuantity(quantity)) {
            throw new IllegalArgumentException("Quantity must be an integer for discrete units");
        }
    }

    public ProductQuantity(Product product, double quantity) {
        this(product, Quantity.of(quantity));
    }
}
