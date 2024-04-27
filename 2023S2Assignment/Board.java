package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

import java.io.*;
import java.util.*;

/**
 * Represents a game board with a specific layout defined in a text file.
 * It handles the drawing of different elements on the board, like paths,
 * shrubs, and the wizard house, using the PApplet graphics context.
 */
public class Board {
    private int rows;
    private int cols;
    private ImageRotator imageRotator;
    private char[][] layout;
    private String layoutFile;
    private PImage grass;
    private PImage shrub;
    private PImage path_0;
    private PImage path_1;
    private PImage path_2;
    private PImage path_3;
    private PImage wizardHouse;

    private List<Character> drawnCells = new ArrayList<>(); //use for tests
    
    /**
     * Creates a new game board with the specified parameters.
     *
     * @param rows the number of rows in the board
     * @param cols the number of columns in the board
     * @param imageRotator the utility to rotate images
     * @param layoutFile the file containing the board layout
     * @param grass the image for the grass cell
     * @param shrub the image for the shrub cell
     * @param path_0 to path_3 the images for different path cells
     * @param wizardHouse the image for the wizard house cell
     */
    public Board(int rows,int cols, ImageRotator imageRotator, String layoutFile, PImage grass, PImage shrub, PImage path_0, PImage path_1, PImage path_2, PImage path_3,PImage wizardHouse) {
        this.rows = rows;
        this.cols = cols;
        this.imageRotator = imageRotator;
        this.layoutFile = layoutFile;
        this.grass = grass;
        this.shrub = shrub;
        this.path_0 = path_0;
        this.path_1 = path_1;
        this.path_2 = path_2;
        this.path_3 = path_3;
        this.wizardHouse = wizardHouse;
        this.layout = new char[rows][cols];
        loadLayout(this.layoutFile);
    }

    /**
     * Loads the layout from a file and populates the board's layout accordingly.
     *
     * @param layoutFile the name of the file containing the layout
     * @return true if the layout is loaded successfully, false otherwise
     */
    public boolean loadLayout(String layoutFile) {
        try {
            Scanner sc = new Scanner(new File(layoutFile));
            for (int row = 0; row < rows; row++) {
                String line = sc.nextLine();
                int length = line.length();
    
                for (int col = 0; col < length; col++) {
                    layout[row][col] = line.charAt(col);
                }
                // If the line has fewer characters than 20, pad the remainder with ' '
                for (int col = length; col < cols; col++) {
                    layout[row][col] = ' ';
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the current layout of the board.
     *
     * @return a 2D character array representing the layout of the board
     */
    public char[][] getLayout(){
        return this.layout;
    }

    /**
     * Draws the map on the given PApplet context based on the current layout.
     *
     * @param app the PApplet context on which the map is to be drawn
     */
    public void draw_map(PApplet app) {
        drawnCells.clear(); 
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                int x = col * App.CELLSIZE;
                int y = row * App.CELLSIZE + App.TOPBAR;

                switch (layout[row][col]) {
                    case ' ':
                        app.image(grass, x, y);
                        drawnCells.add(' ');
                        break;
                    case 'S':
                        app.image(shrub, x, y);
                        drawnCells.add('S');
                        break;
                    /* 1：path0 (up does not exist, down does not exist, left or right exists)
                    2：path1 (left exists, right does not exist, down exists, up does not exist)
                    3: path2 ( left, right and down exists, up does not exist)
                    4: path3 (left, right, up and down all exist)
                    5: (path0, 90) (left does not exist, right does not exist, up or down exists)
                    6: (path1, 90) (left exists, right does not exist, down does not exist, up exists)
                    7: (path1, 180) (left does not exist, right exists, down does not exist, up exists)
                    8: (path1, 270) (left does not exist, right exists, down exists, up does not exist)
                    9: (path2, 90) (left, up and down exists, right does not exist)
                    10: (path2, 180) (left, right and up exists, down does not exist)
                    11: (path2, 270) (right, up and down exists, left does not exist)
                     */
                    case 'X':
                        boolean upExists = row > 0 && layout[row - 1][col] == 'X';
                        boolean downExists = row < rows - 1 && layout[row + 1][col] == 'X';
                        boolean leftExists = col > 0 && layout[row][col - 1] == 'X';
                        boolean rightExists = col < cols - 1 && layout[row][col + 1] == 'X';
                        if ((leftExists || rightExists) && !upExists && !downExists){
                        app.image(path_0, x, y);
                        drawnCells.add('0');
                        break;    
                        }else if (leftExists && !rightExists && !upExists && downExists){
                            app.image(path_1, x, y);
                            drawnCells.add('1');
                            break; 
                        }else if (leftExists && rightExists && !upExists && downExists){
                            app.image(path_2, x, y);
                            drawnCells.add('2');
                            break; 
                        }else if (leftExists && rightExists && upExists && downExists){
                            app.image(path_3, x, y);
                            drawnCells.add('3');
                            break; 
                        }else if (!leftExists && !rightExists && (upExists || downExists)){
                            app.image(imageRotator.rotateImageByDegrees(path_0, 90), x, y);
                            drawnCells.add('4');
                            break; 
                        }else if (leftExists && !rightExists && upExists && !downExists){
                            app.image(imageRotator.rotateImageByDegrees(path_1, 90), x, y);
                            drawnCells.add('5');
                            break; 
                        }else if (!leftExists && rightExists && upExists && !downExists){
                            app.image(imageRotator.rotateImageByDegrees(path_1, 180), x, y);
                            drawnCells.add('6');
                            break; 
                        }else if (!leftExists && rightExists && !upExists && downExists){
                            app.image(imageRotator.rotateImageByDegrees(path_1, 270), x, y);
                            drawnCells.add('7');
                            break; 
                        }else if (leftExists && !rightExists && upExists && downExists){
                            app.image(imageRotator.rotateImageByDegrees(path_2, 90), x, y);
                            drawnCells.add('8');
                            break; 
                        }else if (leftExists && rightExists && upExists && !downExists){
                            app.image(imageRotator.rotateImageByDegrees(path_2, 180), x, y);
                            drawnCells.add('9');
                            break;
                        }else if (!leftExists && rightExists && upExists && downExists){
                            app.image(imageRotator.rotateImageByDegrees(path_2, 270), x, y);
                            drawnCells.add('X');
                            break; 
                        }
                        
                    default:
                        break;
                }   
            }
        } 
    }

    /**
     * Retrieves the characters of the cells that have been drawn on the board.
     * This is primarily used for testing purposes to verify the drawn layout.
     *
     * @return a list of characters representing the drawn cells
     */
    public List<Character> getDrawnCells() {
        return drawnCells;
    }

    /**
     * Draws the wizard house on the board if the corresponding cell is present in the layout.
     *
     * @param app the PApplet context on which the wizard house is to be drawn
     * @return true if the wizard house is drawn, false otherwise
     */
    public boolean draw_wizard(PApplet app){
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                int x = col * App.CELLSIZE;
                int y = row * App.CELLSIZE + App.TOPBAR;

                if (layout[row][col] == 'W') {
                    app.image(grass, x, y);
                    app.image(wizardHouse, x-8, y-8);
                    return true;
                }
            }
        }
        return false;
    }
}
