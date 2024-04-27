package Tanks;

import processing.core.PApplet;
import processing.core.PImage;

public class Board {
    private int rows = 20; // Fixed row count based on the level file format
    private int cols = 28; // Fixed column count based on the level file format
    private char[][] layout;
    private PImage backgroundImage;
    private PImage fuelImage;
    private PImage hillsImage;
    private PImage treeImage1;
    private PImage treeImage2;
    private PImage windImage;
    private PImage wind1Image;

    private List<Character> drawnCells = new ArrayList<>();

    public Board(int cellSize, App app, String layoutString, PImage backgroundImage, PImage fuelImage, PImage hillsImage, PImage treeImage1, PImage treeImage2, PImage windImage, PImage wind1Image) {
        this.backgroundImage = backgroundImage;
        this.fuelImage = fuelImage;
        this.hillsImage = hillsImage;
        this.treeImage1 = treeImage1;
        this.treeImage2 = treeImage2;
        this.windImage = windImage;
        this.wind1Image = wind1Image;
        this.layout = new char[rows][cols];
        loadLayout(layoutString, cellSize, app);
        smoothTerrain();
    }

    private void loadLayout(String layoutString, int cellSize, App app) {
        String[] lines = layoutString.split("\\r?\\n");
        for (int row = 0; row < rows && row < lines.length; row++) {
            String line = lines[row];
            for (int col = 0; col < cols && col < line.length(); col++) {
                char c = line.charAt(col);
                layout[row][col] = c;

                if (c == 'T' || c == 't') {
                    int x = col * cellSize + (int) (Math.random() * 30 - 15); // Randomize position within +/- 30 pixels
                    int y = row * cellSize + App.TOPBAR + (int) (Math.random() * 30 - 15);
                    app.image(c == 'T' ? treeImage1 : treeImage2, x, y);
                }
            }
        }
    }

    public char[][] getLayout() {
        return layout;
    }

    private void smoothTerrain() {
        // Implement terrain smoothing algorithm here
        // ...
    }

    public void drawMap(App app) {
        app.image(backgroundImage, 0, 0);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * App.CELLSIZE;
                int y = row * App.CELLSIZE + App.TOPBAR;

                switch (layout[row][col]) {
                    case 'F':
                        app.image(fuelImage, x, y);
                        break;
                    case 'H':
                        app.image(hillsImage, x, y);
                        break;
                    case 'W':
                        app.image(windImage, x, y);
                        break;
                    case 'w':
                        app.image(wind1Image, x, y);
                        break;
                    // Add more cases for other layout characters
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

    public void drawTanks(PApplet app, PImage tankImage) {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                int x = col * App.CELLSIZE;
                int y = row * App.CELLSIZE + App.TOPBAR;
    
                char c = layout[row][col];
                if (Character.isLetter(c) && Character.isUpperCase(c)) {
                    // Draw tank image for player represented by the uppercase letter
                    app.image(tankImage, x, y);
                }
            }
        }
    }
}
/*
private char[][] layout;
    private PImage backgroundImage;
    private PImage treeImage1;
    private List<Character> drawnCells = new ArrayList<>();

    public Board(int cellSize, PApplet app, String layoutString, PImage backgroundImage, PImage treeImage1) {
        this.backgroundImage = backgroundImage;
        this.treeImage1 = treeImage1;
        this.layout = new char[rows][cols];
        loadLayout(layoutString, cellSize, app);
    }

    private void loadLayout(String layoutString, int cellSize, PApplet app) {
        String[] lines = layoutString.split("\\r?\\n");
        for (int row = 0; row < rows && row < lines.length; row++) {
            String line = lines[row];
            for (int col = 0; col < cols && col < line.length(); col++) {
                char c = line.charAt(col);
                layout[row][col] = c;

                if (c == 'T' || c == 't') {
                    int x = col * cellSize + (int) (Math.random() * 30 - 15); // Randomize position within +/- 30 pixels
                    int y = row * cellSize + (int) (Math.random() * 30 - 15);
                    app.image(treeImage1, x, y);
                }
            }
        }
    }

    public char[][] getLayout() {
        return layout;
    }

    public void drawMap(PApplet app, int cellSize) {
        drawnCells.clear();
        app.image(backgroundImage, 0, 0);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * cellSize;
                int y = row * cellSize;

                switch (layout[row][col]) {
                    case ' ':
                        // Draw a white cell (blank space)
                        app.fill(255); // Set fill color to white
                        app.rect(x, y, cellSize, cellSize); // Draw a white rectangle
                        drawnCells.add(' '); // Add blank space character to the list of drawn cells
                        break;
                    case 'X':
                        app.line(x, y, x + cellSize, y + cellSize);
                        app.line(x + cellSize, y, x, y + cellSize);
                        break;
                    case 'T':
                        // You can add the image drawing logic here for other characters like 'T'
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public List<Character> getDrawnCells() {
        return drawnCells;
    }
} 
*/
