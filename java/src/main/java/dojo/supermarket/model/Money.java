package dojo.supermarket.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Valor monetário com duas casas decimais, arredondado com {@link RoundingMode#HALF_UP}.
 * Substitui o uso de {@code double} para dinheiro, que gera erros de arredondamento binário.
 * <p>
 * Cada operação que produz um novo valor arredonda ao centavo uma única vez.
 */
public record Money(BigDecimal amount) implements Comparable<Money> {

    private static final int SCALE = 2;
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("Money amount cannot be null");
        }
        amount = amount.setScale(SCALE, RoundingMode.HALF_UP);
    }

    /** Cria um valor a partir de texto decimal, por exemplo {@code "0.99"}. */
    public static Money of(String value) {
        return new Money(new BigDecimal(value));
    }

    /** Cria um valor a partir de um decimal; usa a representação decimal do número, sem ruído binário. */
    public static Money of(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Money amount must be finite");
        }
        return new Money(BigDecimal.valueOf(value));
    }

    public Money plus(Money other) {
        return new Money(amount.add(other.amount));
    }

    public Money minus(Money other) {
        return new Money(amount.subtract(other.amount));
    }

    public Money negate() {
        return new Money(amount.negate());
    }

    /** Multiplica por uma quantidade (inteira ou fracionada, como quilos) e arredonda ao centavo. */
    public Money times(double quantity) {
        return times(BigDecimal.valueOf(quantity));
    }

    /** Multiplica por uma quantidade exata e arredonda ao centavo. */
    public Money times(Quantity quantity) {
        return times(quantity.value());
    }

    /** Multiplica por um fator decimal exato e arredonda ao centavo uma única vez. */
    public Money times(BigDecimal factor) {
        return new Money(amount.multiply(factor));
    }

    /** Calcula um percentual do valor (por exemplo, {@code 10.0} para 10%), arredondado ao centavo. */
    public Money percent(double percent) {
        return times(BigDecimal.valueOf(percent).divide(ONE_HUNDRED));
    }

    public boolean isNegative() {
        return amount.signum() < 0;
    }

    @Override
    public int compareTo(Money other) {
        return amount.compareTo(other.amount);
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }
}
