package GameEngine;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * The GameObject class holds all information for game objects seen on screen.
 *
 * @author Jaakko Mäntylä
 */
public class GameObject extends ImageView{


    private double height;
    private double width;
    private Body body;
    private GameObjectType type = GameObjectType.SOLID;
    private GameWindow gameWindow;

    /**
     * Instantiates a new Game object.
     *
     * @param x          the x coordinate
     * @param y          the y coordinate
     * @param height     the height of the object
     * @param filePath   the file path to an image used as visual representation of the object
     * @param gameWindow the game window in which the game object is.
     * @param type       the type of game object
     */
    public GameObject(double x, double y, double height, String filePath, GameWindow gameWindow, GameObjectType type) {
        BufferedImage bi;
        this.gameWindow = gameWindow;
        setPreserveRatio(true);
        setFitHeight(height);
        setX(x);
        setY(y);

        try{

            bi = ImageIO.read(new File(filePath));
            setImage(SwingFXUtils.toFXImage(bi, null));

        }catch(Exception e){
            System.out.println(e);
            bi = null;
        }



        this.height = height;
        this.width = (height * (getImage().getWidth()/getImage().getHeight()));

        if(type==GameObjectType.SOLID || type==GameObjectType.SENSOR ) {
            body = new Body(getX(), getY(), this, bi, height);
        }

    }

    /**
     * Instantiates a new Game object.
     */
    public GameObject(){

    };

    /**
     * Gets body.
     *
     * @return the body
     */
    public Body getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(Body body) {
        this.body = body;
    }

    /**
     * Moves the game object to the direction and length of coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void move(double x, double y){
        setX(getX()+x);
        setY(getY()+y);
        body.move(x,y);
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public GameObjectType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(GameObjectType type) {
        this.type = type;
    }

    /**
     * Gets game window.
     *
     * @return the game window
     */
    public GameWindow getGameWindow() {
        return gameWindow;
    }

    /**
     * Sets game window.
     *
     * @param gameWindow the game window
     */
    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }
}
