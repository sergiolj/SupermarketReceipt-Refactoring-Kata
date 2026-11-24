package dojo.supermarket.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Oferta de pacote: ao levar todos os produtos do conjunto, há um desconto percentual sobre o
 * total desses itens. Só pacotes completos são descontados: com duas escovas e uma pasta de dente
 * num pacote "escova + pasta", há um único pacote.
 * <p>
 * Diferente de {@link OfferStrategy}, a oferta olha o carrinho inteiro, porque envolve vários produtos.
 * O desconto é registrado com o primeiro produto do conjunto, pois {@link Discount} guarda um só produto.
 *
 * @param products produtos do pacote (ao menos dois, sem repetição), uma unidade de cada por pacote
 * @param percent  percentual de desconto sobre o total do pacote (por exemplo, 10.0 para 10%)
 */
public record BundleOffer(List<Product> products, double percent) {

    public BundleOffer {
        products = List.copyOf(products);
        if (products.size() < 2) {
            throw new IllegalArgumentException("Um pacote precisa de ao menos dois produtos");
        }
        if (products.stream().distinct().count() != products.size()) {
            throw new IllegalArgumentException("Um pacote não pode repetir produtos");
        }
    }

    /**
     * @param quantities quantidades de cada produto no carrinho
     * @param catalog    catálogo com os preços unitários
     * @return o desconto (valor negativo), ou vazio se não houver nenhum pacote completo
     */
    public Optional<Discount> discountFor(Map<Product, Quantity> quantities, SupermarketCatalog catalog) {
        int completeBundles = completeBundles(quantities);
        if (completeBundles == 0) {
            return Optional.empty();
        }
        Money bundlePrice = products.stream().map(catalog::getUnitPrice).reduce(Money.ZERO, Money::plus);
        Money discountAmount = bundlePrice.times(completeBundles).percent(percent).negate();
        return Optional.of(new Discount(products.get(0), "bundle " + percent + "% off", discountAmount));
    }

    private int completeBundles(Map<Product, Quantity> quantities) {
        return products.stream()
                .mapToInt(product -> quantities.getOrDefault(product, Quantity.ZERO).wholeUnits())
                .min()
                .orElse(0);
    }
}
