package GameEngine;


import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import java.util.LinkedList;
import javafx.scene.shape.Polygon;


/**
 * Holds methods for collision checking between bodies.
 */
public class CollisionChecker {


    /**
     * Checks if two circles are colliding.
     *
     * @param x1      x coordinate of first circle
     * @param y1      y coordinate of first circle
     * @param radius1 the radius of the first circle
     * @param x2      x coordinate of second circle
     * @param y2      y coordinate of second circle
     * @param radius2 the radius of second circle
     * @return the boolean
     */
    public static boolean circleCircleCol(double x1, double y1, double radius1, double x2, double y2, double radius2){
        double distance;
        distance = Math. sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        if(distance<(radius1+radius2)){
            return true;
        }else{
            return false;
        }

    }

    /**
     * Returns vertices of a javafx.scene.shape.Polygon as a List of Point2Ds.
     *
     * @param shape polygon
     * @return the linked list
     */
    static LinkedList<Point2D> pointsFromPolygon(Polygon shape){
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
     * Gets normal vectors of polygons sides.
     *
     * @param vertices the vertices of a Polygon
     * @return linked list containing normal vectors as Point2Ds
     */
    static LinkedList<Point2D> getShapeNormals(LinkedList<Point2D> vertices){

        LinkedList<Point2D> normals = new LinkedList<Point2D>();

        for (int i = 0; i<vertices.size(); i++){
            Point2D normalvec =
            getNormal(vecSubtract(vertices.get(i), vertices.get((i+1)%vertices.size())));
            normals.add(normalvec);
        }

        return normals;
    }

    /**
     * Projects shape to an axis.
     *
     * @param axis  the axis of projection
     * @param shape the shape to be projected
     * @return double [ ] containing minimum an maximum values of shapes vertices projected on the axis
     */
    static double[] projection(Point2D axis, LinkedList<Point2D> shape) {


        double min = dotProduct(axis, shape.get(0));
        double max = min;
        double minVerticeIndex = 0;
        double maxVerticeIndex = 0;
        for (int i = 1; i < shape.size(); i++) {
            // NOTE: the axis must be normalized to get accurate projections
            double p = dotProduct(axis, shape.get(i));
            if (p < min) {
                min = p;
                minVerticeIndex = i;
            } else if (p > max) {
                max = p;
                maxVerticeIndex = i;
            }
        }
        double[] proj = new double[]{min, max, minVerticeIndex, maxVerticeIndex};

        return proj;

    }

    /**
     * Checks if the two given projections overlap.
     *
     * @param p1 array containing minimum an maximum values of shape's projection
     * @param p2 array containing minimum an maximum values of shape's projection
     * @return boolean does overlap occur
     */
    static boolean projectionsOverlap(double[] p1, double[] p2){
        if((p1[0]<=p2[1] && p1[0]>=p2[0])
                || (p1[1]<=p2[1] && p1[1]>=p2[0])
        || (p1[1]>p2[1] && p1[0]<p2[0] )){
            return true;
        }else{
            return false;
        }

    }

    /**
     * Dot product double.
     *
     * @param v1 vector 1
     * @param v2 vector 2
     * @return double dotproduct of vectors
     */
    static double dotProduct(Point2D v1, Point2D v2){
        return v1.getX()*v2.getX()+v1.getY()*v2.getY();
    }

    /**
     * Vector subtraction.
     *
     * @param v1 vector 1
     * @param v2 vector 2
     * @return subtraction of vectors as Point2D
     */
    static Point2D vecSubtract(Point2D v1, Point2D v2){
        return new Point2D(v1.getX()-v2.getX(), v1.getY()-v2.getY());
    }

    /**
     * Gets normalized (unit length) normal of a vector.
     *
     * @param vec the vector
     * @return normal vector as Point 2D
     */
    static Point2D getNormal(Point2D vec){
        Point2D p = new Point2D(vec.getY(), vec.getX()*-1);
        p = p.normalize();
        return p;
    }

    /**
     * Gets overlap of two projections given double[] where index 0 is the min value of projection and index 1 is max.
     *
     * @param projection1 the projection 1
     * @param projection2 the projection 2
     * @return the overlap of projections as a double
     */
    static double getOverlap(double[] projection1, double[] projection2){
        double overlap = Math.min(projection1[1], projection2[1])-Math.max(projection1[0], projection2[0]);
        return overlap;
    }

    /**
     * Gets overlapping vertice's index from the projection2.
     *
     * @param projection1 the projection 1
     * @param projection2 the projection 2
     * @return vertice index int;
     */
    static int getOverlappingVerticeIndex(double[] projection1, double[] projection2){
        if(projection1[0]<projection2[0]){
            return (int) projection2[2];
        }else {
            return (int) projection2[3];
        }
    }


    /**
     * Checks collision of two polygons.
     *
     * @param poly1 the polygon 1
     * @param poly2 the polygon 2
     * @return the boolean indicating are polygons colliding
     */
    public static CollisionInfo checkCollision(Polygon poly1, Polygon poly2){

        
        //needed  for overlap depth returning later
        double overlap = 10000000;
        double[] projectionOfAxisShape = null;
        double[] projectionCollPoint = null;
        boolean projectionAxisIsFrom1 = true;



        //gets points from te tested polygons in workable lists
        LinkedList<Point2D> vertices1 = pointsFromPolygon(poly1);
        LinkedList<Point2D> vertices2 = pointsFromPolygon(poly2);

        //gets normal vectors from the polygon point lists
        LinkedList<Point2D> normals1 = getShapeNormals(vertices1);
        LinkedList<Point2D> normals2 = getShapeNormals(vertices2);

        //puts the lists of normal vectors in a list so that both can be looped through with same code
        LinkedList<LinkedList<Point2D>> normals = new LinkedList<LinkedList<Point2D>>();
        normals.add(normals1);
        normals.add(normals2);


        //needed later for returning overlap depth
        Point2D smallest = null;

// loop over both polygons normals
        for (int n = 0; n < normals.size(); n++){

            for (int i = 0; i < normals.get(n).size(); i++) {
                Point2D axis = normals.get(n).get(i);
                // project both shapes onto the axis
                double[] projection1 = projection(axis, vertices1);
                double[] projection2 = projection(axis, vertices2);
                // do the projections overlap?
                if (!projectionsOverlap(projection1, projection2)) {
                    // then we can guarantee that the shapes do not overlap
                    return null;
                } else {


                // get the overlap
                double o = getOverlap(projection1, projection2);
                // check for minimum
                if (o < overlap) {
                    // then set this one as the smallest
                    overlap = o;
                    smallest = axis;
                    if(n==0){
                        projectionOfAxisShape=projection1;
                        projectionCollPoint=projection2;
                        projectionAxisIsFrom1 = true;
                    }else {
                        projectionOfAxisShape=projection2;
                        projectionCollPoint=projection1;
                        projectionAxisIsFrom1 = false;
                    }

                }



                }
            }
        }

        Point2D collisionPoint;
        int index = getOverlappingVerticeIndex(projectionOfAxisShape, projectionCollPoint);
        if(projectionAxisIsFrom1){
            collisionPoint = vertices2.get(index);
        }else {
            collisionPoint = vertices1.get(index);
        }


        CollisionInfo colInfo = new CollisionInfo(overlap, smallest, collisionPoint);


        return colInfo;
    }





}
