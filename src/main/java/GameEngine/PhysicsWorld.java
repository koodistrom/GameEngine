package GameEngine;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.util.HashMap;
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



    public void solveCollision(Body body1, Body body2){

        body1.setLinearVelocity(calculateVelocitityAfterCollision(body1, body2));
        body2.setLinearVelocity(calculateVelocitityAfterCollision(body2, body1).multiply(-1));


    }

    public Point2D calculateVelocitityAfterCollision(Body body1, Body body2){
        double a = (body2.getMass()*2)/(body2.getMass()+body1.getMass());
        double b = (body1.getLinearVelocity().subtract(body2.getLinearVelocity()).dotProduct(body1.getCentroid().subtract(body2.getCentroid())))/
                (Math.pow(body1.getCentroid().subtract(body2.getCentroid()).magnitude(),2));
        Point2D c = body1.getCentroid().subtract(body2.getCentroid());
        Point2D newVelocity = c.multiply(a).multiply(b);
        return newVelocity;

    }


    public void updateWorld(){
        for(int i=0; i<getBodies().size(); i++){
            LinkedList<Body> collisionsNow = new LinkedList<>();
            for(int n = i+1; n<getBodies().size(); n++)
            if(CollisionChecker.checkCollision((Polygon)getBodies().get(i).getShapes().get(0),(Polygon) getBodies().get(n).getShapes().get(0))!=null){
                if(!getBodies().get(i).getInCollisionWith().contains(getBodies().get(n))){
                    solveCollision(getBodies().get(i), getBodies().get(n));
                }
                collisionsNow.add(getBodies().get(n));

            }
            getBodies().get(i).setInCollisionWith(collisionsNow);
        }

        updateBodies();
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
