package ExampleGame;

import GameEngine.*;
import javafx.geometry.Point2D;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;

/**
 *
 * Example game.
 * @author Jaakko Mäntylä
 */
public class App extends GameWindow {

    Image image;
    GameObject go;
    GameObject car;
    double verticalDir;
    double horizontalDir;

    BufferedImage test;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){


        System.out.println("Author: Jaakko Mäntylä");
        setWorld( new PhysicsWorld(10.0, new Point2D(0, 0)));
        horizontalDir=0;
        verticalDir = 0;
        go = new GameObject(150,150,70,"C:\\Users\\Jaakko\\IdeaProjects\\Game_engine\\resources\\HeMan.png", this, GameObjectType.SOLID);
        car = new GameObject(350,150,70,"C:\\Users\\Jaakko\\IdeaProjects\\Game_engine\\resources\\car.png", this, GameObjectType.SOLID);
        //add your game objects to gameObjects ArrayList so they are added to the scene
        getGameObjects().add(go);
        getGameObjects().add(car);

        super.start(primaryStage);

        setKeyListeners();

    }

    @Override
    public void gameLoop(long now){
        // gameloop here

        go.setRotate(go.getRotate()+1);

        go.getBody().applyForce(new Point2D(horizontalDir*800, verticalDir*800), new Point2D(70*horizontalDir, 0));

        //go.getBody().getShapes().get(0).setRotate(go.getBody().getShapes().get(0).getRotate()+1);
        System.out.println(CollisionChecker.checkCollision((Polygon)go.getBody().getShapes().get(0),(Polygon) car.getBody().getShapes().get(0)) );


    }

    public void setKeyListeners(){
        getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                switch (event.getCode()) {
                    case UP:    verticalDir = -1; break;
                    case DOWN:  verticalDir = 1; break;
                    case LEFT:  horizontalDir  = -1; break;
                    case RIGHT: horizontalDir  = 1; break;

                }
            }
        });

        getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    verticalDir = 0; break;
                    case DOWN:  verticalDir = 0; break;
                    case LEFT:  horizontalDir  = 0; break;
                    case RIGHT: horizontalDir  = 0; break;
                }
            }
        });
    }



}
