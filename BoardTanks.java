package Tanks;
import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        //loadLayout(layoutString, cellSize, app);
        smoothTerrain();
    }

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

    public char[][] getLayout() {
        return layout;
    }

    private void smoothTerrain() {
        // Implement terrain smoothing algorithm here
        // ...
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


