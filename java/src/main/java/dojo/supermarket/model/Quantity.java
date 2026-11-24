package dojo.supermarket.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Quantidade de um produto: unidades inteiras ou quilos fracionados, em decimal exato com três casas.
 * Substitui o {@code double} (que acumulava erro binário nos quilos) e o <i>cast</i> para {@code int}
 * espalhado pelas regras de oferta, agora concentrado em {@link #wholeUnits()}.
 */
public record Quantity(BigDecimal value) implements Comparable<Quantity> {

    private static final int SCALE = 3;

    public static final Quantity ZERO = new Quantity(BigDecimal.ZERO);
    public static final Quantity ONE = new Quantity(BigDecimal.ONE);

    public Quantity {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException("Quantity must be finite and non-negative");
        }
        value = value.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static Quantity of(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Quantity must be finite and non-negative");
        }
        return new Quantity(BigDecimal.valueOf(value));
    }

    public static Quantity of(String value) {
        return new Quantity(new BigDecimal(value));
    }

    public static Quantity units(int count) {
        return new Quantity(BigDecimal.valueOf(count));
    }

    /** Parte inteira da quantidade (por exemplo, 2.9 vira 2): quantas unidades completas há. */
    public int wholeUnits() {
        return value.intValue();
    }

    public boolean isWholeNumber() {
        return value.stripTrailingZeros().scale() <= 0;
    }

    public boolean isOne() {
        return value.compareTo(BigDecimal.ONE) == 0;
    }

    public Quantity plus(Quantity other) {
        return new Quantity(value.add(other.value));
    }

    @Override
    public int compareTo(Quantity other) {
        return value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
