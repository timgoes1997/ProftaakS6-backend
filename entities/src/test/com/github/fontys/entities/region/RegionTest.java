package com.github.fontys.entities.region;

import com.github.fontys.entities.tracking.Location;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

public class RegionTest {

    @Test
    public void isWithinRegionSquare() {
        BorderLocation r1 = new BorderLocation(0.0d, 0.0d, 1L);
        BorderLocation r2 = new BorderLocation(0.0d, 50.0d, 2L);
        BorderLocation r3 = new BorderLocation(50.0d, 50.0d, 3L);
        BorderLocation r4 = new BorderLocation(50.0d, 0.0d, 4L);
        List<BorderLocation> squareBorders = new ArrayList<BorderLocation>();
        squareBorders.add(r1);
        squareBorders.add(r2);
        squareBorders.add(r3);
        squareBorders.add(r4);

        Region square = new Region("square");
        square.setBorderPoints(squareBorders);

        Location l1 = new Location(25.0d, 25.0d, Calendar.getInstance());
        Location l2 = new Location(75.0d, 75.0d, Calendar.getInstance());
        Location l3 = new Location(0.1d, 0.1d, Calendar.getInstance());
        Location l4 = new Location(-0.1d, -0.1d, Calendar.getInstance());
        Location l5 = new Location(-0.00001d, 1.0d, Calendar.getInstance());

        assertTrue(square.isWithinRegion(l1));
        assertFalse(square.isWithinRegion(l2));
        assertTrue(square.isWithinRegion(l3));
        assertFalse(square.isWithinRegion(l4));
        assertFalse(square.isWithinRegion(l5));
    }

    @Test
    public void isWithinDiamondLikeShape() {
        BorderLocation r1 = new BorderLocation(0.0d, 0.0d, 1L);
        BorderLocation r2 = new BorderLocation(25.0d, 50.0d, 2L);
        BorderLocation r3 = new BorderLocation(50.0d, 0.0d, 3L);
        BorderLocation r4 = new BorderLocation(25.0d, -50.0d, 4L);
        List<BorderLocation> squareBorders = new ArrayList<BorderLocation>();
        squareBorders.add(r1);
        squareBorders.add(r2);
        squareBorders.add(r3);
        squareBorders.add(r4);

        Region square = new Region("square");
        square.setBorderPoints(squareBorders);

        Location l1 = new Location(25.0d, 25.0d, Calendar.getInstance());
        Location l2 = new Location(37.5d, 26.0d, Calendar.getInstance());
        Location l3 = new Location(23.d, 40.0d, Calendar.getInstance());
        Location l4 = new Location(24.d, -50.d, Calendar.getInstance());
        Location l5 = new Location(-0.00001d, 1.0d, Calendar.getInstance());

        assertTrue(square.isWithinRegion(l1));
        assertFalse(square.isWithinRegion(l2));
        assertTrue(square.isWithinRegion(l3));
        assertFalse(square.isWithinRegion(l4));
        assertFalse(square.isWithinRegion(l5));
    }

    @Test
    public void isInOrder() {
        BorderLocation r1 = new BorderLocation(0.0d, 0.0d, 1L);
        BorderLocation r2 = new BorderLocation(0.0d, 50.0d, 2L);
        BorderLocation r3 = new BorderLocation(50.0d, 50.0d, 3L);
        BorderLocation r4 = new BorderLocation(50.0d, 0.0d, 4L);
        List<BorderLocation> squareBorders = new ArrayList<BorderLocation>();
        squareBorders.add(r4);
        squareBorders.add(r1);
        squareBorders.add(r3);
        squareBorders.add(r2);

        Region square = new Region("square");
        square.setBorderPoints(squareBorders);

        List<BorderLocation> inOrder = square.getBorderPoints();
        assertTrue(inOrder.get(0).getVerticeId() == 1L);
        assertTrue(inOrder.get(1).getVerticeId() == 2L);
        assertTrue(inOrder.get(2).getVerticeId() == 3L);
        assertTrue(inOrder.get(3).getVerticeId() == 4L);
    }

    @Test
    public void isInBerlinTest(){
        BorderLocation r1 = new BorderLocation(52.6d,13.25d, 1L);
        BorderLocation r2 = new BorderLocation(52.4d, 13.25d, 2L);
        BorderLocation r3 = new BorderLocation(52.4d, 13.6d, 3L);
        BorderLocation r4 = new BorderLocation(52.6d, 13.6d, 4L);
        List<BorderLocation> squareBorderBerlin = new ArrayList<BorderLocation>();
        squareBorderBerlin.add(r1);
        squareBorderBerlin.add(r2);
        squareBorderBerlin.add(r3);
        squareBorderBerlin.add(r4);

        Region square = new Region("square");
        square.setBorderPoints(squareBorderBerlin);

        Location test = new Location(52.554425, 13.467784, Calendar.getInstance());

        assertTrue(square.isWithinRegion(test));
    }
}