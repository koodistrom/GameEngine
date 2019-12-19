package GameEngine;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * The GameWindow class is the main class of the game engine.
 * To use the game engine you have to make your main class extend this class and call launch method
 * from your main method.
 *
 * @author Jaakko Mäntylä.
 */
public class GameWindow extends Application {

    /**
     * Group that is added to the game scene. All gameobjects are added to this in start method
     */
    Group root = new Group();
    /**
     * The Debug lines.
     */
    Group debugLines = new Group();

    /**
     * Game scene that draws all gameobjects
     */
    final Scene scene = new Scene( root,600  , 600);

    /**
     * List of game objects in scene. Add game objects to this for them to be in scene.
     */
    private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();

    /**
     * The Time.
     */
    long time;
    /**
     * The Delta time.
     */
    double deltaTime;
    /**
     * The World.
     */
    PhysicsWorld world;
    
    private boolean debug = true;


    /**
     * Called when game launches. One's has to override this method in the main class of their game
     * and code things they want to happen at the start (i.e. add gameobjects) adn then call super.start(primaryStage);.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("best game");



        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                deltaTime = (now-time)/ 1000000000.0;
                time = now;
                getWorld().updateWorld();
                gameLoop(now);
            }
        };

        for(int i=0; i<gameObjects.size(); i++){
            root.getChildren().add(gameObjects.get(i));

            world.getBodies().add(gameObjects.get(i).getBody());

            if(debug) {
                gameObjects.get(i).getBody().getShapes().forEach((n) -> debugLines.getChildren().add(n));
                debugLines.getChildren().add(gameObjects.get(i).getBody().getDebugCentroid());
            }

        }
        root.getChildren().add(debugLines);
        primaryStage.setScene(scene);
        primaryStage.show();

        time = System.nanoTime();
        deltaTime = 0;
        loop.start();


    }

    /**
     * This method is called by javafx.animation.AnimationTimer's handle method
     *
     * @param now current time in nanoseconds
     */
    public void gameLoop(long now){
        System.out.println(deltaTime);


    }

    /**
     * Gets delta time.
     *
     * @return the delta time
     */
    public double getDeltaTime() {
        return deltaTime;
    }

    /**
     * Sets delta time.
     *
     * @param deltaTime the delta time
     */
    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
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
     * Gets scene.
     *
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Gets game objects.
     *
     * @return the game objects
     */
    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    /**
     * Sets game objects.
     *
     * @param gameObjects the game objects
     */
    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }
}
