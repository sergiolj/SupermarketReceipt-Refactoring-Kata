package dojo.supermarket.model;

import java.util.Optional;

/**
 * Leve N unidades por um valor fixo (por exemplo, 2 por 1.50 ou 5 por 7.49).
 * As unidades que sobram depois dos grupos completos pagam o preço normal.
 *
 * @param groupSize quantidade de unidades de cada grupo
 * @param amount    valor fixo cobrado por grupo completo
 */
public record QuantityForAmountOffer(int groupSize, Money amount) implements OfferStrategy {

    @Override
    public Optional<Discount> discountFor(Product product, double quantity, Money unitPrice) {
        int units = (int) quantity;
        if (units < groupSize) {
            return Optional.empty();
        }
        Money payable = amount.times(units / groupSize).plus(unitPrice.times(units % groupSize));
        Money fullPrice = unitPrice.times(quantity);
        return Optional.of(new Discount(product, groupSize + " for " + amount, payable.minus(fullPrice)));
    }
}
