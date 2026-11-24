package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();

    List<ProductQuantity> getItems() {
        return Collections.unmodifiableList(items);
    }

    void addItem(Product product) {
        addItemQuantity(product, Quantity.ONE);
    }

    Map<Product, Quantity> productQuantities() {
        Map<Product, Quantity> quantities = new HashMap<>();

        for (ProductQuantity item : items) {
            quantities.merge(
                    item.product(),
                    item.quantity(),
                    Quantity::plus);
        }

        return Collections.unmodifiableMap(quantities);
    }

    public void addItemQuantity(Product product, Quantity quantity) {
        items.add(new ProductQuantity(product, quantity));
    }

    public void addItemQuantity(Product product, double quantity) {
        addItemQuantity(product, Quantity.of(quantity));
    }
}
