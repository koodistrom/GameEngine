package GameEngine;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Physic engine utils.
 */
public class PhysicEngineUtils {

    /**
     * Creates Point2D list from polygons vertices.
     *
     * @param shape the polygon of which vertices are listed
     * @return the linked list containing polygons vertices as Point2D
     */
    public static LinkedList<Point2D> pointListFromPolygon(Polygon shape){
        ObservableList<Double> pointsXY = shape.getPoints();
        LinkedList<Point2D> vertices = new LinkedList<Point2D>();
        double xLocation = shape.getLayoutX();
        double yLocation = shape.getLayoutY();

        for(int i = 0; i<pointsXY.size(); i+=2){
            double x = xLocation+pointsXY.get(i);
            double y = yLocation+pointsXY.get((i)+1);

            vertices.add( new Point2D(x,y));
        }
        return vertices;
    }

    /**
     * Calculates polygon's area double.
     *
     * @param polygon the polygon
     * @return area as double
     */
    public static double calculatePolygonArea(Polygon polygon){
        LinkedList<Point2D> vertices = pointListFromPolygon(polygon);
        double area = 0;


        for(int i=0; i<vertices.size(); i++){

            double sectionArea = calculateTriangleSectionArea(vertices.get(i), vertices.get((i+1)%vertices.size()));
            area += sectionArea;
           /* double tempA = points.get(i)*points.get((i+3)%points.size());
            a+= tempA;

            double tempB = points.get(i+1)*points.get((i+2)%points.size());
            b+= tempB;
            */

        }


        return area;

    }

    /**
     * Calculates triangle section area. Method is used by calculatePolygonArea. Method gives the area of a triangle
     * formed by point 0x, 0y and the input points.
     *
     * @param p1 the point 1
     * @param p2 the point 2
     * @return triangle's area as double
     */
    public static double calculateTriangleSectionArea(Point2D p1, Point2D p2){

        return (p1.getX()*p2.getY()-p1.getY()*p2.getX())/2;
    }

    /**
     * Returns centroid of a triangle formed by given points and origo.
     *
     * @param p1 the point 1
     * @param p2 the point 2
     * @return centroid of the triangle as a Point2D
     */
    public static Point2D centroid(Point2D p1, Point2D p2){
        return new Point2D((p1.getX()+p2.getX())/3, (p1.getY()+p2.getY())/3);
    }

    /**
     * Calculates the moment of inertia of a triangle formed by origo and the given point.
     *
     * @param area the area of the triangle
     * @param p1   the point 1
     * @param p2   the point 2
     * @return moment of inertia of the triangle as a double
     */
    public static double mmoiTriangle(double area, Point2D p1, Point2D p2){

        return (area/36.0)*(p1.dotProduct(p1)+p2.dotProduct(p2)+p1.dotProduct(p2));
    }

    /**
     * Calculates the moment of inertia of a triangle formed by origo and the given point.
     *
     * @param area the area of the triangle
     * @param p1   the point 1
     * @param p2   the point 2
     * @return moment of inertia of the triangle as a double
     */
    public static double mmoiTriangle2(double area, Point2D p1, Point2D p2){
        double b = length(p1);
        double h = pointFromLineDistance(p2, p1);
        return (1.0/36.0)*(b*h*h*h);
    }

    /**
     * Calculates the moment of inertia of a polygon
     * by breaking it in triangle sections.
     *
     * @param polygon      the polygon
     * @param rotationAxis the rotation axis the moment of inertia is calculated on
     * @return moment of inertia as a double
     */
    public static double polygonMmoi(Polygon polygon, Point2D rotationAxis){
        LinkedList<Point2D> vertices = pointListFromPolygon(polygon);
        double mmoi = 0;


        for(int i=0; i<vertices.size(); i++){

            double sectionArea = calculateTriangleSectionArea(vertices.get(i), vertices.get((i+1)%vertices.size()));


            double sectionMmoi = mmoiTriangle(sectionArea, vertices.get(i), vertices.get((i+1)%vertices.size()));

            Point2D centroid = centroid(vertices.get(i), vertices.get((i+1)%vertices.size()));
            Point2D distanceFromRotation = new Point2D(centroid.getX()-rotationAxis.getX(), centroid.getY()-rotationAxis.getY());

            mmoi += sectionMmoi+ sectionArea*distanceFromRotation.dotProduct(distanceFromRotation);

        }
        return mmoi;
    }

    /**
     * Calculates the centroid of a polygon.
     *
     * @param polygon the polygon
     * @return centroid of the polygon as Point2D
     */
    public static Point2D calculateCentroid(Polygon polygon)
    {

        List<Point2D> vertices = pointListFromPolygon(polygon);
        double centroidX = 0, centroidY = 0;
        double det = 0, tempDet = 0;
        int j = 0;
        int nVertices = (int) vertices.size();

        for (int i = 0; i < nVertices; i++)
        {
            // closed polygon
            if (i + 1 == nVertices)
                j = 0;
            else
                j = i + 1;

            // compute the determinant
            tempDet = vertices.get(i).getX() * vertices.get(j).getY() - vertices.get(j).getX()*vertices.get(i).getY();

            det += tempDet;

            centroidX += (vertices.get(i).getX() +  vertices.get(j).getX())*tempDet;
            centroidY += (vertices.get(j).getY() + vertices.get(i).getY())*tempDet;
        }
        System.out.println("detrminant" + det);
        // divide by the total mass of the polygon
        centroidX /= 3*det;
        centroidY /= 3*det;

        System.out.println("centroid"+ new Point2D(centroidX, centroidY));
        return new Point2D(centroidX, centroidY);
    }


    /**
     * Calculates the length of a given vector
     *
     * @param vector the vector
     * @return the length
     */
    public static double length(Point2D vector){
        return Math.sqrt(vector.getX()*vector.getX()+vector.getY()*vector.getY());
    }

    /**
     * Calculates the distance of a point to a given line
     *
     * @param point the point
     * @param line  the line as a vector
     * @return the distance
     */
    public static double pointFromLineDistance(Point2D point, Point2D line){
        double a = point.getX()*line.getY()+point.getY()*(-1)*line.getX();
        double b = Math.sqrt(line.getY()*line.getY()+line.getX()*line.getX());
        if(b==0){
            return 0;
        }else{
            return a/b;
        }

    }

    /**
     * Calculates triangle section area. Method is used by calculatePolygonArea. Method gives the area of a triangle
     * formed by point 0x, 0y and the input points.
     *
     * @param point the point of a triangle top
     * @param line  the base of a triangle
     * @return the area as a double
     */
    public static double triangleArea2(Point2D point, Point2D line){
        double h = pointFromLineDistance(point, line);
        double b = length(line);
        return b*h/2;
    }
}
