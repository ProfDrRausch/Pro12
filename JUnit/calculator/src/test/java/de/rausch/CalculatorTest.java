package de.rausch; 

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void additionTest() {
        Calculator calculator = new Calculator();
        assertEquals(5, calculator.add(2, 3), "Die Addition sollte korrekt sein.");
    }

    @Test
    void subtractionTest() {
        Calculator calculator = new Calculator();
        assertEquals(2, calculator.subtract(3, 2), "Die Subtraktion sollte korrekt sein.");
    }
}