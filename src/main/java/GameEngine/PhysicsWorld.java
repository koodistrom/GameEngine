package GameEngine;

import javafx.geometry.Point2D;

import java.util.LinkedList;

/**
 * This class holds information of bodies interacting in the physical world and updates the forces to bodies caused by
 * non player input sources, like collisions to other objects or gravity.
 */
public class PhysicsWorld {
    /**
     * The ratio of Javafx window size units per meters used by the physics simulation.
     */
    double pixelsPerMeter;
    /**
     * The Gravity vector.
     */
    Point2D gravity;
    /**
     * The Bodies included in this world.
     */
    LinkedList<Body> bodies;

    /**
     * Instantiates a new Physics world.
     *
     * @param pixelsPerMeter  The ratio of Javafx window size units per meters used by the physics simulation.
     * @param gravity        the gravity
     */
    public PhysicsWorld(double pixelsPerMeter, Point2D gravity) {
        this.pixelsPerMeter = pixelsPerMeter;
        this.gravity = gravity;
        bodies = new LinkedList<Body>();
    }

    /**
     * Updates bodies by applying gravity to them and calling their move method.
     */
    public void updateBodies(){
        getBodies().forEach(body -> {
            body.applyForce(gravity.multiply(body.getMass()),new Point2D(0,0));
            body.updatePosition();
        });
    }

    /**
     * Gets pixels per meter.
     *
     * @return the pixels per meter
     */
    public double getPixelsPerMeter() {
        return pixelsPerMeter;
    }

    /**
     * Sets pixels per meter.
     *
     * @param pixelsPerMeter the pixels per meter
     */
    public void setPixelsPerMeter(double pixelsPerMeter) {
        this.pixelsPerMeter = pixelsPerMeter;
    }

    /**
     * Gets gravity.
     *
     * @return the gravity
     */
    public Point2D getGravity() {
        return gravity;
    }

    /**
     * Sets gravity.
     *
     * @param gravity the gravity
     */
    public void setGravity(Point2D gravity) {
        this.gravity = gravity;
    }

    /**
     * Gets bodies.
     *
     * @return the bodies
     */
    public LinkedList<Body> getBodies() {
        return bodies;
    }

    /**
     * Sets bodies.
     *
     * @param bodies the bodies
     */
    public void setBodies(LinkedList<Body> bodies) {
        this.bodies = bodies;
    }
}
