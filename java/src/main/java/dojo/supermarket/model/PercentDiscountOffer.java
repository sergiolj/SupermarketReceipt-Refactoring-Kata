package dojo.supermarket.model;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Desconto percentual sobre o total do produto, aplicado desde a primeira unidade.
 *
 * @param percent percentual de desconto (por exemplo, 10.0 para 10%)
 */
public record PercentDiscountOffer(double percent) implements OfferStrategy {

    @Override
    public Optional<Discount> discountFor(Product product, double quantity, Money unitPrice) {
        BigDecimal factor = BigDecimal.valueOf(quantity).multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100));
        Money discountAmount = unitPrice.times(factor).negate();
        return Optional.of(new Discount(product, percent + "% off", discountAmount));
    }
}
