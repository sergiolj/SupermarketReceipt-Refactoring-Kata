package dojo.supermarket.model;

import java.util.Optional;

/**
 * Leve 3, pague 2: a cada três unidades, uma é gratuita. Só se aplica a partir de três unidades.
 */
public record ThreeForTwoOffer() implements OfferStrategy {

    private static final int GROUP_SIZE = 3;
    private static final int PAID_PER_GROUP = 2;

    @Override
    public Optional<Discount> discountFor(Product product, Quantity quantity, Money unitPrice) {
        int units = quantity.wholeUnits();
        if (units < GROUP_SIZE) {
            return Optional.empty();
        }
        int completeGroups = units / GROUP_SIZE;
        Money fullPrice = unitPrice.times(quantity);
        Money payable = unitPrice.times(completeGroups * PAID_PER_GROUP + units % GROUP_SIZE);
        return Optional.of(new Discount(product, "3 for 2", payable.minus(fullPrice)));
    }
}
