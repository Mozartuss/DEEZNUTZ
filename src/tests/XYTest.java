package tests;

import core.XY;
import org.junit.Test;

import static org.junit.Assert.*;

public class XYTest {
    private final XY test1 = new XY(10, 10);
    private final XY test2 = new XY(20, 25);
    private final XY testDistanceFrom1 = new XY(15, 15);
    private final XY testDistanceFrom2 = new XY(6, 3);


    @Test
    public void addVector() {
        assertEquals(new XY(30, 35), test1.addVector(test2));
    }

    @Test
    public void reduceVector() {
        assertEquals(new XY(-10, -15), test1.reduceVector(test2));
    }

    @Test
    public void timesVector() {
        assertEquals(new XY(30, 30), test1.times(3));

    }

    @Test
    public void distanceFrom() {
        assertEquals(15.0, testDistanceFrom1.distanceFrom(testDistanceFrom2));
        assertEquals(0.0, testDistanceFrom1.distanceFrom(testDistanceFrom1));
    }

    @Test
    public void equals() {
        assertTrue(test1.equals(new XY(10, 10)));
        assertFalse(test1.equals(new XY(15, 10)));

    }
}
