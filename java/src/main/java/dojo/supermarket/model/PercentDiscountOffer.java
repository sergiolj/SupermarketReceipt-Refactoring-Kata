package dojo.supermarket.model;

import java.util.Optional;

/**
 * Desconto percentual sobre o total do produto, aplicado desde a primeira unidade.
 *
 * @param percent percentual de desconto (por exemplo, 10.0 para 10%)
 */
public record PercentDiscountOffer(double percent) implements OfferStrategy {

    @Override
    public Optional<Discount> discountFor(Product product, double quantity, double unitPrice) {
        double discountAmount = -quantity * unitPrice * percent / 100.0;
        return Optional.of(new Discount(product, percent + "% off", discountAmount));
    }
}
