package GameEngine;

import javafx.geometry.Point2D;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;


/**
 * Class holds methods needed for doing graham scan for vertices to create convex hull around them.
 * Class is used by ShapeCreator class for creating shapes used in collision testing from images.
 */
public class GrahamScanner {


    /**
     * A global point needed for  sorting points with reference.
     */
    Point2D p0;


    /**
     * moves the point with lowest y value to the front of the List.
     *
     * @param points the points
     * @return the linked list with the point containing lowest y value first
     */
    public static LinkedList<Point2D> putLowestFirst(LinkedList<Point2D> points){
        LinkedList<Point2D> lowestFirst = points;
        int minIndex = 0;
        for(int i = 0; i<points.size(); i++){
            if(points.get(i).getY()<points.get(minIndex).getY()){
                minIndex=i;
            }else if(points.get(i).getY()==points.get(minIndex).getY() && points.get(i).getY() < points.get(minIndex).getY()) {
                minIndex = i;
            }
        }

        Collections.swap(lowestFirst, 0, minIndex);

        return lowestFirst;
    }





    /**
     * Returns square of the distance of the points
     *
     * @param p1 point 1
     * @param p2 point 2
     * @return distance of the points squared
     */

    double distSq(Point2D p1, Point2D p2)
    {
        return (p1.getX() - p2.getX())*(p1.getX() - p2.getX()) +
                (p1.getY() - p2.getY())*(p1.getY() - p2.getY());
    }

    /**
     * returns orientation of the points. 0 means colinear. 1 means clockwise. 2 means counter clocwise.
     *
     * @param p point p
     * @param q point q
     * @param r point r
     * @return the int implicating orientation of the points
     */

    int orientation(Point2D p, Point2D q, Point2D r)
    {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val < 0.01 && val> -0.01) return 0;
        return (val > 0.01)? 1: 2;
    }

    /**
     * Class used by Collections.sort method to compare points in a List by their orientation.
     *
     */

    class PointComparator implements Comparator<Point2D>{

        @Override
        public int compare(Point2D p1, Point2D p2) {

            // Find orientation
            int o = orientation(p0, p1, p2);
            if (o == 0)
                return (distSq(p0, p2) >= distSq(p0, p1))? -1 : 1;

            return (o == 2)? -1: 1;

        }
    }

    /**
     * Method takes a random shaped polygon as LinkedList off Point2D and returns a smallest posiible
     * convex polygon you can surround the input polygon with
     *
     * @param points Points of a polygon to be surrounded with convex hull
     * @return linked list containing vertices of the convex hull as Point2D
     */

    public LinkedList<Point2D> convexHull(LinkedList<Point2D> points)
    {

        points = putLowestFirst(points);

        // Sort n-1 points with respect to the first point.
        // A point p1 comes before p2 in sorted output if p2
        // has larger polar angle (in counterclockwise
        // direction) than p1
        p0 = points.get(0);
        points.remove(0);
        Collections.sort(points, new PointComparator());

        points.addFirst(p0);

        // If two or more points make same angle with p0,
        // Remove all but the one that is farthest from p0
        // Remember that, in above sorting, our criteria was
        // to keep the farthest point at the end when more than
        // one points have same angle.

        int m = 1; // Initialize size of modified array
        LinkedList<Point2D> neededPoints = new LinkedList<Point2D>();
        neededPoints.addFirst(p0);
        for (int i=1; i<points.size(); i++)
        {
            // Keep removing i while angle of i and i+1 is same
            // with respect to p0
            while (i < points.size()-1 && orientation(p0, points.get(i),
                    points.get(i+1)) == 0)
                i++;


            neededPoints.add(points.get(i));
            m++;  // Update size of modified array
        }

        points = neededPoints;

        // If modified array of points has less than 3 points,
        // convex hull is not possible
        if (m < 3) return null;


        neededPoints = new LinkedList<Point2D>();
        neededPoints.add(points.get(0));
        neededPoints.add(points.get(1));
        neededPoints.add(points.get(2));

        // Process remaining n-3 points
        for (int i = 3; i < m; i++)
        {
            // Keep removing top while the angle formed by
            // points next-to-top, top, and points[i] makes
            // a non-left turn
            while (orientation(neededPoints.get(neededPoints.size()-2), neededPoints.getLast(), points.get(i)) != 2)
                neededPoints.removeLast();
            neededPoints.add(points.get(i));
        }

        points = neededPoints;
        return points;
    }
}
