package dojo.supermarket.model;

public interface SupermarketCatalog {

    void addProduct(Product product, Money price);

    Money getUnitPrice(Product product);
}
