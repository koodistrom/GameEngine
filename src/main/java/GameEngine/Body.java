package GameEngine;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Body holds information about physical properties of a game object
 * and calculates the effects of external forces to location and speed of the object.
 */
public class Body {

    /**
     * The Shapes that form the area on screen.
     */
    ArrayList<Shape> shapes;
    /**
     * The host game object that holds the body.
     */
    GameObject host;

    /**
     * The mass of the body.
     */
    double mass;
    /**
     * The Linear velocity of the body.
     */
    Point2D linearVelocity;
    /**
     * The Angular velocity of the body.
     */
    double angularVelocity;
    /**
     * The Density of the body.
     */
    double density;
    /**
     * The conversion rate of JavaFX units to meters used in physics calculations.
     */
    double pixelsPerMeter;
    /**
     * The World where body is located.
     */
    PhysicsWorld world;
    /**
     * The Centroid of the body.
     */
    Point2D centroid;
    /**
     * Debug centroid showing on the screen.
     */
    Circle debugCentroid;
    /**
     * The moment of inertia of the body.
     */
    double mmoi;
    /**
     * The Torque of the body.
     */
    double torque;
    /**
     * The Angle of the body in radians.
     */
    double angle;

    /**
     * List of bodies this body is currently colliding
     */
    LinkedList<Body> inCollisionWith;

    /**
     * Instantiates a new Body.
     *
     * @param x    the x coordinate
     * @param y    the y coordinate
     * @param host the host game object
     */
    Body(double x, double y, GameObject host){


        this.host = host;
        shapes = new ArrayList<Shape>() ;
    }

    /**
     * Instantiates a new Body.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param host   the host game object
     * @param img    the image from which body is calculated
     * @param height the height of the body
     */
    Body(double x, double y, GameObject host, BufferedImage img, double height){

        density = 1;
        angle = 0;
        linearVelocity = new Point2D(0.0,0.0);
        angularVelocity = 0;
        world = host.getGameWindow().getWorld();
        pixelsPerMeter = world.getPixelsPerMeter();
        this.host = host;
        shapes = new ArrayList<Shape>();
        torque = 0;
        inCollisionWith = new LinkedList<Body>();

        double scaler = height/img.getHeight();
        Shape s = ShapeCreator.hullShapeFromImg(img, scaler);
        s.setLayoutX(x);
        s.setLayoutY(y);
        s.setStroke(Color.BLUE);
        s.setFill(null);
        //s.setStrokeWidth(1);
        shapes.add(s);
        //TODO  area calculation returns negative when it should return positive value
        mass = Math.abs(PhysicEngineUtils.calculatePolygonArea((Polygon) s))/(pixelsPerMeter*pixelsPerMeter);
        centroid = PhysicEngineUtils.calculateCentroid((Polygon) s);
        mmoi = PhysicEngineUtils.polygonMmoi((Polygon)s, centroid)/(pixelsPerMeter*pixelsPerMeter);
        System.out.println("inertia " + mmoi);
        System.out.println("mass " + mass);
        debugCentroid = new Circle(centroid.getX(), centroid.getY(),1.0);

        System.out.println(centroid);
    }

    /**
     * Gets shapes.
     *
     * @return the shapes
     */
    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    /**
     * Sets shapes.
     *
     * @param shapes the shapes
     */
    public void setShapes(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    /**
     * Applies force to body and calculates velocities and angle accordingly.
     *
     * @param force the force to be applied on body
     * @param r     the r vector from centroid of the body to the point where force is applied
     */
    public void applyForce(Point2D force, Point2D r){
        Point2D acceleration = new Point2D(force.getX() / mass, force.getY() / mass);

        linearVelocity = new Point2D(linearVelocity.getX()+ acceleration.getX() * host.getGameWindow().getDeltaTime(),
                linearVelocity.getY()+acceleration.getY() * host.getGameWindow().getDeltaTime());

        calculateTorque(force, r);
        double angularAcceleration = torque / mmoi;
        angularVelocity += angularAcceleration * host.getGameWindow().getDeltaTime();
        angle += angularVelocity * host.getGameWindow().getDeltaTime();

    }

    /**
     * Updates position according to velocities and angle.
     */
    public void updatePosition(){
        double xChange = linearVelocity.getX() * host.getGameWindow().getDeltaTime()* world.getPixelsPerMeter();
        double yChange = linearVelocity.getY() * host.getGameWindow().getDeltaTime()* world.getPixelsPerMeter();
        getShapes().forEach(n->{
            n.setRotate(angle*180/Math.PI);
            host.setRotate(n.getRotate());
        n.setLayoutX(n.getLayoutX()+ xChange);
        n.setLayoutY(n.getLayoutY()+ yChange);
        host.setX(n.getLayoutX());
        host.setY(n.getLayoutY());


        });

        centroid = new Point2D(centroid.getX()+xChange, centroid.getY()+yChange);
        debugCentroid.setCenterX(centroid.getX());
        debugCentroid.setCenterY(centroid.getY());

    }

    /**
     * Calculates torque according to the force applied to the body.
     *
     * @param f the force to be applied on body
     * @param r the r vector from centroid of the body to the point where force is applied
     */
    public void calculateTorque(Point2D f, Point2D r){
        setTorque(f.getY()*r.getX()-f.getX()*r.getY());
    }

    /**
     * Moves body to the direction and length of given coordinates.
     *
     * @param x the x move distance
     * @param y the y move destance
     */
    public void move(double x, double y){
        getShapes().forEach(n->{
            n.setLayoutX(n.getLayoutX()+x);
            n.setLayoutY(n.getLayoutY()+y);
        });
    }


    /**
     * Gets mass.
     *
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * Sets mass.
     *
     * @param mass the mass
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Gets debug centroid.
     *
     * @return the debug centroid
     */
    public Circle getDebugCentroid() {
        return debugCentroid;
    }

    /**
     * Sets debug centroid.
     *
     * @param debugCentroid the debug centroid
     */
    public void setDebugCentroid(Circle debugCentroid) {
        this.debugCentroid = debugCentroid;
    }

    /**
     * Gets torque.
     *
     * @return the torque
     */
    public double getTorque() {
        return torque;
    }

    /**
     * Sets torque.
     *
     * @param torque the torque
     */
    public void setTorque(double torque) {
        this.torque = torque;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public GameObject getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(GameObject host) {
        this.host = host;
    }

    /**
     * Gets linear velocity.
     *
     * @return the linear velocity
     */
    public Point2D getLinearVelocity() {
        return linearVelocity;
    }

    /**
     * Sets linear velocity.
     *
     * @param linearVelocity the linear velocity
     */
    public void setLinearVelocity(Point2D linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    /**
     * Gets angular velocity.
     *
     * @return the angular velocity
     */
    public double getAngularVelocity() {
        return angularVelocity;
    }

    /**
     * Sets angular velocity.
     *
     * @param angularVelocity the angular velocity
     */
    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    /**
     * Gets density.
     *
     * @return the density
     */
    public double getDensity() {
        return density;
    }

    /**
     * Sets density.
     *
     * @param density the density
     */
    public void setDensity(double density) {
        this.density = density;
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
     * Gets world.
     *
     * @return the world
     */
    public PhysicsWorld getWorld() {
        return world;
    }

    /**
     * Sets world.
     *
     * @param world the world
     */
    public void setWorld(PhysicsWorld world) {
        this.world = world;
    }

    /**
     * Gets centroid.
     *
     * @return the centroid
     */
    public Point2D getCentroid() {
        return centroid;
    }

    /**
     * Sets centroid.
     *
     * @param centroid the centroid
     */
    public void setCentroid(Point2D centroid) {
        this.centroid = centroid;
    }

    /**
     * Gets mmoi.
     *
     * @return the mmoi
     */
    public double getMmoi() {
        return mmoi;
    }

    /**
     * Sets mmoi.
     *
     * @param mmoi the mmoi
     */
    public void setMmoi(double mmoi) {
        this.mmoi = mmoi;
    }

    /**
     * Gets angle.
     *
     * @return the angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Sets angle.
     *
     * @param angle the angle
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Gets in collision with.
     *
     * @return the in collision with
     */
    public LinkedList<Body> getInCollisionWith() {
        return inCollisionWith;
    }

    /**
     * Sets in collision with.
     *
     * @param inCollisionWith the in collision with
     */
    public void setInCollisionWith(LinkedList<Body> inCollisionWith) {
        this.inCollisionWith = inCollisionWith;
    }
}
