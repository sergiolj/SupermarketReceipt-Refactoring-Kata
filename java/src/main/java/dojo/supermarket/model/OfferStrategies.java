package dojo.supermarket.model;

/**
 * Fábrica que converte um {@link SpecialOfferType} e seu argumento numérico na estratégia correspondente.
 */
public final class OfferStrategies {

    private OfferStrategies() {
    }

    /**
     * @param type     tipo da oferta
     * @param argument percentual (10%), ou valor fixo (2 ou 5 por valor); ignorado em "3 por 2"
     */
    public static OfferStrategy from(SpecialOfferType type, double argument) {
        return switch (type) {
            case THREE_FOR_TWO -> new ThreeForTwoOffer();
            case TEN_PERCENT_DISCOUNT -> new PercentDiscountOffer(argument);
            case TWO_FOR_AMOUNT -> new QuantityForAmountOffer(2, argument);
            case FIVE_FOR_AMOUNT -> new QuantityForAmountOffer(5, argument);
        };
    }
}
