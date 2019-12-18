package GameEngine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javafx.geometry.Point2D;
import org.junit.Test;
import javafx.scene.shape.Polygon;

import java.util.LinkedList;

/**
 * Unit test for simple App.
 */
public class CollisionListenerTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {


        assertTrue(true);
    }

    @Test
    public void polyToPointTest(){
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                0.0, 0.0,
                20.0, 10.0,
                10.0, 20.0 });
        LinkedList<Point2D> l = CollisionChecker.pointsFromPolygon(polygon);
        LinkedList<Point2D> tester = new LinkedList<Point2D>();
        tester.add(new Point2D(0,0));
        tester.add(new Point2D(20,10));
        tester.add(new Point2D(10,20));

        assertTrue(l.equals(tester));

    }

    @Test
    public void vecSubtracTestTest(){
        Point2D tester = new Point2D(1,2);
        Point2D p =  CollisionChecker.vecSubtract(new Point2D(3,5), new Point2D(2,3));
        assertTrue(p.equals(tester));
    }

    @Test
    public void dotPTest(){
        Point2D tester = new Point2D(1,2);
        double p =  CollisionChecker.dotProduct(new Point2D(3,5), new Point2D(2,3));
        assertTrue(p==21);
    }

    @Test
    public void projOverlapTest() {

        assertTrue(CollisionChecker.projectionsOverlap(new double[]{1,10}, new double[]{2,11}));
        assertTrue(CollisionChecker.projectionsOverlap(new double[]{1,10}, new double[]{2,3}));
        assertTrue(CollisionChecker.projectionsOverlap(new double[]{2,10}, new double[]{1,7}));
        assertFalse(CollisionChecker.projectionsOverlap(new double[]{1,10}, new double[]{11,13}));
    }

    @Test
    public void collisionCheckTest(){
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                0.0, 0.0,
                20.0, 10.0,
                10.0, 20.0 });

        Polygon polygon2 = new Polygon();
        polygon2.getPoints().addAll(new Double[]{
                30.0, 30.0,
                50.0, 400.0,
                40.0, 50.0 });

        Polygon polygon3 = new Polygon();
        polygon3.getPoints().addAll(new Double[]{
                1.0, 1.0,
                20.0, 10.0,
                10.0, 20.0 });

        //assertFalse(CollisionChecker.checkCollision(polygon, polygon2));
        //assertTrue(CollisionChecker.checkCollision(polygon, polygon3));

    }
}