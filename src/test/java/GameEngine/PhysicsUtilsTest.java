package GameEngine;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import org.junit.Test;



import static org.junit.Assert.assertTrue;

public class PhysicsUtilsTest {

    @Test
    public void area() {

        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                0.0, 0.0,
                50.0, 0.0,
                50.0, 60.0,
                0.0, 60.0});

        double area = PhysicEngineUtils.calculatePolygonArea(polygon);

        //assertTrue(PhysicEngineUtils.calculatePolygonArea(polygon)==200.0);

        System.out.println("inertia " +PhysicEngineUtils.polygonMmoi(polygon, PhysicEngineUtils.calculateCentroid(polygon)));
        Point2D p = new Point2D(2,2);

    }

}
