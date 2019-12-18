package GameEngine;


import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;


import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.util.LinkedList;


/**
 * Class creates a convex hull to wrap an image. Class uses GrahamScanner class to do that.
 * Hull is created so that it is the smallest possible convex polygon covering all non transparent pixels in an
 * image.
 */
public  class ShapeCreator {


    /**
     * Maps see through pixels and returns an array boolean [ ] [ ] size of the image. Where true represents solid
     * pixel and false transparent.
     *
     * @param img the img
     * @return map of solid pixels boolean [ ] [ ]
     */
    public static  boolean[][] mapSeethroughPixels(BufferedImage img){
        ColorModel imgColorModel = img.getColorModel();
        Raster raster = img.getRaster();
        boolean[][] solidPixels = new boolean[img.getHeight()][img.getWidth()];
        for ( int x = 0; x < img.getWidth(); x++ ) {
            for ( int y = 0; y < img.getHeight(); y++ ) {
                Object pix = raster.getDataElements( x, y, null );
                if(imgColorModel.getAlpha(pix) == 0){
                    solidPixels[y][x] = false;
                }else {
                    solidPixels[y][x] = true;
                }

            }
        }

        return solidPixels;
    }

    /**
     * Finds edge pixels from a boolean [][] array where true indicates a solid and false indicates transparent pixels.
     *
     *
     * @param solidPixels the solid pixels
     * @return a linked list of Point2D indicating the edge pixesl of a non transparent parts of the image.
     */
    public static LinkedList<Point2D> findEdgePixels(boolean[][] solidPixels){

        LinkedList<Point2D> edgePixels = new LinkedList<Point2D>();
        for ( int x = 0; x < solidPixels.length; x++ ) {
            for (int y = 0; y < solidPixels[x].length; y++) {
                if (solidPixels[x][y]) {
                    boolean solidFound = false;
                    boolean translucentFound = false;
                    //checks area around selected spot
                    for (int n = x - 1; n <= x + 1; n++) {
                        for (int m = y - 1; m <= y + 1; m++) {
                            if ((n >= 0 && m >= 0 && n < solidPixels.length && m < solidPixels[x].length && !(m == y && n == x))) {
                                if (solidPixels[n][m]) {
                                    solidFound = true;
                                } else {
                                    translucentFound = true;
                                }
                            }
                        }
                    }
                    if (solidFound && translucentFound) {
                        edgePixels.add(new Point2D(x, y));
                    }

                }
            }
        }

     return edgePixels;
    }

    /**
     * Creates a convex hull shape from image.
     *
     * @param img    the image
     * @param scaler the scaler scales the created shape. Used to match the hull size to the size image is scaled to
     *               in the game.
     * @return the convex hull as a Polygon
     */
    public static Shape hullShapeFromImg(BufferedImage img,double scaler){
        boolean[][] solidPixels = mapSeethroughPixels(img);
        LinkedList<Point2D> imgEdgePixs = findEdgePixels(solidPixels);
        LinkedList<Point2D> convexHull = new GrahamScanner().convexHull(imgEdgePixs);

        return shapeFromList(convexHull, scaler);
    }

    /**
     * Creates a Polygon from LinkedList of vertices.
     *
     * @param points the vertices as a list
     * @param scaler the scaler for scaling the created polygon
     * @return the polygon
     */
    static Polygon shapeFromList(LinkedList<Point2D> points, double scaler){
        Double[] pointsArray = new Double[points.size()*2];
        for(int i = 0; i<points.size(); i++){
            pointsArray[(i*2)] = points.get(i).getY()*scaler;


            pointsArray[(i*2)+1] = points.get(i).getX()*scaler;
        }

        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(pointsArray);
        return polygon;
    }
    





}
