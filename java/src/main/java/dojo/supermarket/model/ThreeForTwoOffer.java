package dojo.supermarket.model;

import java.util.Optional;

/**
 * Leve 3, pague 2: a cada três unidades, uma é gratuita. Só se aplica a partir de três unidades.
 */
public record ThreeForTwoOffer() implements OfferStrategy {

    private static final int GROUP_SIZE = 3;
    private static final int PAID_PER_GROUP = 2;

    @Override
    public Optional<Discount> discountFor(Product product, double quantity, double unitPrice) {
        int units = (int) quantity;
        if (units < GROUP_SIZE) {
            return Optional.empty();
        }
        int completeGroups = units / GROUP_SIZE;
        double discountAmount = quantity * unitPrice
                - ((completeGroups * PAID_PER_GROUP * unitPrice) + units % GROUP_SIZE * unitPrice);
        return Optional.of(new Discount(product, "3 for 2", -discountAmount));
    }
}
