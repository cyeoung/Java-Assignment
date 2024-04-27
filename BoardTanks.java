
package Tanks;
import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Tanks.App.CELLHEIGHT;
import static Tanks.App.CELLSIZE;

public class Board {
    private int rows = 20; // Fixed row count based on the level file format
    private int cols = 28; // Fixed column count based on the level file format
    private char[][] layout;
    private PImage backgroundImage;
    private String layoutFile;
    private PImage fuelImage;
    private PImage hillsImage;
    private PImage treeImage1;
    private PImage treeImage2;
    private PImage windImage;
    private PImage wind1Image;
    private List<Character> drawnCells = new ArrayList<>();

    public Board(int rows, int cols, String layoutFile, PImage backgroundImage, PImage fuelImage, PImage hillsImage, PImage treeImage1, PImage treeImage2, PImage wind1Image, PImage windImage) {
        this.rows = rows;
        this.cols = cols;
        this.backgroundImage = backgroundImage;
        this.layoutFile = layoutFile;
        this.fuelImage = fuelImage;
        this.hillsImage = hillsImage;
        this.treeImage1 = treeImage1;
        this.treeImage2 = treeImage2;
        this.windImage = windImage;
        this.wind1Image = wind1Image;
        this.layout = new char[rows][cols];
        loadLayout(this.layoutFile);
        smoothTerrain();
    }

    public boolean loadLayout(String layoutFile) {
        File file = new File(layoutFile);
        if (!file.exists()) {
            System.err.println("Layout file not found: " + layoutFile);
            return false;
        }
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
        } catch (NoSuchElementException e) {
            System.err.println("Layout file is empty or incomplete.");
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

    // public void drawMap(PApplet app, int cellSize) {
    //     drawnCells.clear();
    //     app.image(backgroundImage, 0, 0);

    //     for (int row = 0; row < rows; row++) {
    //         for (int col = 0; col < cols; col++) {
    //             int x = col * cellSize;
    //             int y = row * cellSize;
    //             System.out.println(x);
    //             System.out.println(y);

    //             switch (layout[row][col]) {
    //                 case ' ':
    //                     // Draw a white cell (blank space)
    //                     app.fill(0); // Set fill color to white
    //                     app.rect(x, y, cellSize, cellSize); // Draw a white rectangle
    //                     drawnCells.add(' '); // Add blank space character to the list of drawn cells
    //                     break;
    //                 case 'X':
    //                     app.line(x, y, x + cellSize, y + cellSize);
    //                     app.line(x + cellSize, y, x, y + cellSize);
    //                     break;
    //                 case 'T':
    //                     // You can add the image drawing logic here for other characters like 'T'
    //                     break;
    //                 default:
    //                     break;
    //             }
    //         }
    //     }
    // }

    public void drawMap(PApplet app) {
        drawnCells.clear();
        app.image(backgroundImage, 0, 0);

         // Create a copy of the layout array to avoid modifying the original data
        char[][] smoothedLayout = new char[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                smoothedLayout[row][col] = layout[row][col];
            }
        }

        // Apply the moving average filter
        int windowSize = 32;
        int halfWindow = windowSize / 2;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int count = 0;
                int sum = 0;
                for (int i = -halfWindow; i <= halfWindow; i++) {
                    for (int j = -halfWindow; j <= halfWindow; j++) {
                        int neighborRow = row + i;
                        int neighborCol = col + j;
                        if (neighborRow >= 0 && neighborRow < rows && neighborCol >= 0 && neighborCol < cols) {
                            sum += smoothedLayout[neighborRow][neighborCol] == 'X' ? 1 : 0;
                            count++;
                        }
                    }
                }
                float average = (float) sum / count;
                smoothedLayout[row][col] = average >= 0.5f ? 'X' : ' ';
            }
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * CELLHEIGHT;
                int y = row * CELLSIZE;

                switch (layout[row][col]) {
                    // case ' ':
                    //     // Draw a white cell (blank space)
                    //     app.fill(255); // Set fill color to white
                    //     app.rect(x, y, CELLSIZE, CELLHEIGHT); // Draw a white rectangle
                    //     drawnCells.add(' '); // Add blank space character to the list of drawn cells
                    //     break;
                    case 'X':
                        // app.line(x, y, x + CELLSIZE, y + CELLHEIGHT);
                        // app.line(x + CELLSIZE, y, x, y + CELLHEIGHT);
                        app.stroke(255);
                        app.fill(255); // Set fill color to white
                        app.rect(x, y, CELLSIZE, CELLHEIGHT); // Draw a black rectangle representing an obstacle
                        drawnCells.add('X'); // Add obstacle character to the list of drawn cells

                        // if (row == 0 || row == rows - 1 || col == 0 || col == cols - 1) {
                        //     app.line(x, y, col == 0 ? x : x + CELLSIZE, y);
                        //     app.line(x, y, x, row == 0 ? y : y + CELLHEIGHT);
                        //     app.line(x + CELLSIZE, y, x + CELLSIZE, y + CELLHEIGHT);
                        //     app.line(x, y + CELLHEIGHT, x + CELLSIZE, y + CELLHEIGHT);
                        // }
                        for (int belowRow = row + 1; belowRow < rows; belowRow++) {
                            if (layout[belowRow][col] != 'X') {
                                int belowX = col * CELLHEIGHT;
                                int belowY = belowRow * CELLSIZE;
                                app.fill(255);
                                app.stroke(255); // Set fill color to white
                                app.rect(belowX, belowY, CELLSIZE, CELLHEIGHT); // Draw a white rectangle below the 'X'
                            } else {
                                // If we encounter another 'X', stop filling cells below
                                break;
                            }
                        }
                        break;
                    case 'T':
                        // You can add the image drawing logic here for other characters like 'T'
                        app.fill(0, 255, 0); // Set fill color to green (example for trees)
                        app.rect(x, y, CELLSIZE, CELLHEIGHT); // Draw a green rectangle representing a tree
                        drawnCells.add('T'); // Add tree character to the list of drawn cells
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
