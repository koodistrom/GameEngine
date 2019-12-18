package GameEngine;

import javafx.geometry.Point2D;

/**
 * Minimum transaltion vector. Holds information of the length of the overlap and the axis the overlap is happening.
 */
public class MTV {
    /**
     * The Overlap.
     */
    double overlap;
    /**
     * The Axis.
     */
    Point2D axis;

    /**
     * Instantiates a new Mtv.
     *
     * @param overlap the overlap
     * @param axis    the axis
     */
    public MTV(double overlap, Point2D axis){
        this.overlap = overlap;
        this.axis = axis;
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

    @Override
    public String toString() {
        return "MTV{" +
                "overlap=" + overlap +
                ", axis=" + axis +
                '}';
    }
}
