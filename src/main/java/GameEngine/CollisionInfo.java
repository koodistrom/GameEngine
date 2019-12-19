package GameEngine;

import javafx.geometry.Point2D;

/**
 * Minimum transaltion vector. Holds information of the length of the overlap and the axis the overlap is happening.
 */
public class CollisionInfo {
    /**
     * The Overlap.
     */
    double overlap;
    /**
     * The Axis.
     */
    Point2D axis;

    /**
     * Approximation of the location where two bodies are touching.
     */
    Point2D collisionPoint;

    /**
     * Instantiates a new Mtv.
     *
     * @param overlap        the overlap
     * @param axis           the axis
     * @param collisionPoint the collision point
     */
    public CollisionInfo(double overlap, Point2D axis, Point2D collisionPoint){
        this.overlap = overlap;
        this.axis = axis;
        this.collisionPoint = collisionPoint;
    }

    /**
     * Gets overlap.
     *
     * @return the overlap
     */
    public double getOverlap() {
        return overlap;
    }

    /**
     * Sets overlap.
     *
     * @param overlap the overlap
     */
    public void setOverlap(double overlap) {
        this.overlap = overlap;
    }

    /**
     * Gets axis.
     *
     * @return the axis
     */
    public Point2D getAxis() {
        return axis;
    }

    /**
     * Sets axis.
     *
     * @param axis the axis
     */
    public void setAxis(Point2D axis) {
        this.axis = axis;
    }

    /**
     * Gets collision point.
     *
     * @return the collision point
     */
    public Point2D getCollisionPoint() {
        return collisionPoint;
    }

    /**
     * Sets collision point.
     *
     * @param collisionPoint the collision point
     */
    public void setCollisionPoint(Point2D collisionPoint) {
        this.collisionPoint = collisionPoint;
    }

    @Override
    public String toString() {
        return "CollisionInfo{" +
                "overlap=" + overlap +
                ", axis=" + axis +
                ", collision point=" + collisionPoint +
                '}';
    }
}
