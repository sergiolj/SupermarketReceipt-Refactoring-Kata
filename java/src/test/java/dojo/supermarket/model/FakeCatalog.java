package dojo.supermarket.model;

import java.util.HashMap;
import java.util.Map;

public class FakeCatalog implements SupermarketCatalog {

    private final Map<String, Product> products = new HashMap<>();
    private final Map<String, Money> prices = new HashMap<>();

    @Override
    public void addProduct(Product product, Money price) {
        this.products.put(product.name(), product);
        this.prices.put(product.name(), price);
    }

    @Override
    public Money getUnitPrice(Product p) {
        return this.prices.get(p.name());
    }

}
