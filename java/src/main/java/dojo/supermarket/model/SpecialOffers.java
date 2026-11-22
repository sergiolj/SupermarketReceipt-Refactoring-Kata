package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cadastro das promoções em vigor e aplicação delas sobre as quantidades de um carrinho.
 * Há no máximo uma oferta por produto; cadastrar outra substitui a anterior. As ofertas de pacote
 * são independentes e somam-se às ofertas por produto.
 */
public class SpecialOffers {

    private final Map<Product, OfferStrategy> offers = new HashMap<>();
    private final List<BundleOffer> bundles = new ArrayList<>();

    public void add(Product product, OfferStrategy strategy) {
        offers.put(product, strategy);
    }

    public void addBundle(BundleOffer bundle) {
        bundles.add(bundle);
    }

    /**
     * Registra na nota o desconto de cada produto do carrinho que tenha oferta aplicável e, depois,
     * o de cada pacote completo.
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
        for (BundleOffer bundle : bundles) {
            bundle.discountFor(quantities, catalog).ifPresent(receipt::addDiscount);
        }
    }
}
