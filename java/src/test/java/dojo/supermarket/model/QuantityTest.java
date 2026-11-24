package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityTest {

    @Test
    void equalsByValueRegardlessOfHowItWasCreated() {
        assertEquals(Quantity.of("2"), Quantity.of(2.0));
        assertEquals(Quantity.units(2), Quantity.of(2));
        assertEquals("2.500", Quantity.of(2.5).toString());
    }

    @Test
    void sumsFractionalKilosWithoutBinaryError() {
        assertEquals(Quantity.of("0.300"), Quantity.of(0.1).plus(Quantity.of(0.2)));
    }

    @Test
    void wholeUnitsTruncatesTheFraction() {
        assertEquals(2, Quantity.of(2.9).wholeUnits());
        assertEquals(0, Quantity.of(0.5).wholeUnits());
        assertEquals(10, Quantity.units(10).wholeUnits());
    }

    @Test
    void knowsWholeNumbersAndOne() {
        assertTrue(Quantity.of(3).isWholeNumber());
        assertFalse(Quantity.of(1.5).isWholeNumber());
        assertTrue(Quantity.ONE.isOne());
        assertTrue(Quantity.of(1.0).isOne());
        assertFalse(Quantity.of(1.5).isOne());
    }

    @Test
    void acceptsZero() {
        assertEquals(Quantity.ZERO, Quantity.of(0));
    }

    @Test
    void rejectsNegativeAndNonFiniteValues() {
        assertThrows(IllegalArgumentException.class, () -> Quantity.of(-1));
        assertThrows(IllegalArgumentException.class, () -> Quantity.of(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> Quantity.of(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> Quantity.of(Double.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new Quantity(null));
    }

    @Test
    void comparesByValue() {
        assertTrue(Quantity.of(2).compareTo(Quantity.of(1.5)) > 0);
        assertEquals(0, Quantity.of("1").compareTo(Quantity.of(1.000)));
    }
}
