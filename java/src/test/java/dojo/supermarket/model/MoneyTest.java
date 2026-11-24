package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTest {

    @Test
    void keepsTwoDecimalPlacesAndEqualsByValue() {
        assertEquals(Money.of("1.5"), Money.of("1.50"));
        assertEquals("1.50", Money.of(1.5).toString());
        assertEquals(Money.of("2"), Money.of(2.0));
    }

    @Test
    void roundsHalfUpToTheCent() {
        assertEquals("0.10", Money.of("0.099").toString());
        assertEquals("0.99", Money.of("0.994").toString());
        assertEquals("1.00", Money.of("0.995").toString());
        assertEquals("-1.00", Money.of("-0.995").toString());
    }

    @Test
    void doublesDoNotCarryBinaryNoise() {
        assertEquals(Money.of("0.30"), Money.of(0.1).plus(Money.of(0.2)));
        assertEquals(Money.of("0.99"), Money.of(0.99));
    }

    @Test
    void addsSubtractsAndNegates() {
        assertEquals(Money.of("2.78"), Money.of("0.99").plus(Money.of("1.79")));
        assertEquals(Money.of("0.80"), Money.of("1.79").minus(Money.of("0.99")));
        assertEquals(Money.of("-0.99"), Money.of("0.99").negate());
        assertTrue(Money.of("-0.01").isNegative());
        assertFalse(Money.ZERO.isNegative());
    }

    @Test
    void multipliesByWholeAndFractionalQuantities() {
        assertEquals(Money.of("2.97"), Money.of("0.99").times(3));
        assertEquals(Money.of("4.98"), Money.of("1.99").times(2.5)); // 4.975 arredondado
    }

    @Test
    void multipliesByExactDecimalFactorRoundingOnce() {
        assertEquals(Money.of("0.28"), Money.of("2.78").times(new BigDecimal("0.1"))); // 0.278
    }

    @Test
    void computesPercentages() {
        assertEquals(Money.of("0.99"), Money.of("9.90").percent(10.0));
        assertEquals(Money.of("0.10"), Money.of("0.99").percent(10.0)); // 0.099
    }

    @Test
    void comparesByAmount() {
        assertTrue(Money.of("1.00").compareTo(Money.of("0.99")) > 0);
        assertEquals(0, Money.of("1").compareTo(Money.of("1.00")));
    }

    @Test
    void rejectsNullAndNonFiniteValues() {
        assertThrows(IllegalArgumentException.class, () -> new Money(null));
        assertThrows(IllegalArgumentException.class, () -> Money.of(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> Money.of(Double.POSITIVE_INFINITY));
    }
}
