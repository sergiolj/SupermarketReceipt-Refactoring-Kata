package dojo.supermarket.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Cadastro das promoções em vigor e aplicação delas sobre as quantidades de um carrinho.
 * Há no máximo uma oferta por produto; cadastrar outra substitui a anterior.
 */
public class SpecialOffers {

    private final Map<Product, OfferStrategy> offers = new HashMap<>();

    public void add(Product product, OfferStrategy strategy) {
        offers.put(product, strategy);
    }

    /**
     * Registra na nota o desconto de cada produto do carrinho que tenha oferta aplicável.
     */
    void applyTo(Map<Product, Double> quantities, SupermarketCatalog catalog, Receipt receipt) {
        for (Map.Entry<Product, Double> entry : quantities.entrySet()) {
            Product product = entry.getKey();
            OfferStrategy strategy = offers.get(product);
            if (strategy == null) {
                continue;
            }
            strategy.discountFor(product, entry.getValue(), catalog.getUnitPrice(product))
                    .ifPresent(receipt::addDiscount);
        }
    }
}
