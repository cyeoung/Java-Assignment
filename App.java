package Tanks;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.core.PConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class App extends PApplet {

    public static final int CELLSIZE = 32;
    public static final int CELLHEIGHT = 32;
    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static final int WIDTH = 864;
    public static final int HEIGHT = 640;
    public static final int INITIAL_PARACHUTES = 1;
    public static final int FPS = 30;

    public String configPath;
    public String currentLevel;
    public String foregroundColor;

    public int rows = 20;
    public int cols = 28;
    public int wind;
    public int currentPlayer;
    public int fuelX;
    public int fuelY;
    public float x, y;


    public JSONObject config;
    public static Random random = new Random();

    public PImage basicImage;
    public PImage desertImage;
    public PImage forestImage;
    public PImage fuelImage;
    public PImage hillsImage;
    public PImage parachuteImage;
    public PImage snowImage;
    public PImage tree1Image;
    public PImage tree2Image;
    public PImage wind1Image;
    public PImage windImage;

    public char[][] layout;
    public boolean gameOver = false;

    private Tank[] tanks;

    ArrayList<Projectile> projectiles = new ArrayList<>();
    ArrayList<Tree> trees = new ArrayList<>();

    ArrayList<Float> terrainHeights = new ArrayList<>();
    ArrayList<Float> terrainHeights2 = new ArrayList<>();
    List<Float> smoothedTerrainHeights2 = new ArrayList<>();

    public ArrayList<PVector> treePositions = new ArrayList<>();

    public ArrayList<PVector> terrainPositions = new ArrayList<>();
    public HashMap<Character, PVector> tankPositions = new HashMap<Character, PVector>();


    public App() {
        this.configPath = "config.json";
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        frameRate(FPS);
        loadResources();
        loadConfig();
        layout = new char[rows][cols];
        currentLevel = "level2.txt"; // Start with level1.txt
        loadLayout(currentLevel);

        processLevelData();

//        loadLayout(currentLevel);

        tanks = new Tank[4];
        List<Character> tankOrder = new ArrayList<Character>();
        tankOrder.add('A');
        tankOrder.add('B');
        tankOrder.add('C');
        tankOrder.add('D');

        for (int i = 0; i < tankOrder.size(); i++) {
            tanks[i] = new Tank(this, tankPositions.get(tankOrder.get(i)).x, terrainHeights2.get((int) tankPositions.get(tankOrder.get(i)).x), terrainHeights2, parachuteImage);
//            System.out.println(tanks[i]);
        }

        for (PVector pos : treePositions) {
            if (currentLevel.equals("level1.txt")) {
                trees.add(new Tree(this, pos.x - 15, terrainHeights2.get((int) pos.x) - 31, terrainHeights2, tree2Image, 35, 35));
            } else if (currentLevel.equals("level3.txt")) {
                trees.add(new Tree(this, pos.x - 15, terrainHeights2.get((int) pos.x) - 31, terrainHeights2, tree1Image, 35, 35));
            }
        }


        currentPlayer = 0;
        wind = (int) random(-35, 36);
    }


    private void loadResources() {
        this.basicImage = loadImage("src/main/resources/Tanks/basic.png");
        this.desertImage = loadImage("src/main/resources/Tanks/desert.png");
        this.forestImage = loadImage("src/main/resources/Tanks/forest.png");
        this.fuelImage = loadImage("src/main/resources/Tanks/fuel.png");
        this.hillsImage = loadImage("src/main/resources/Tanks/hills.png");
        this.parachuteImage = loadImage("src/main/resources/Tanks/parachute.png");
        this.snowImage = loadImage("src/main/resources/Tanks/snow.png");
        this.tree1Image = loadImage("src/main/resources/Tanks/tree1.png");
        this.tree2Image = loadImage("src/main/resources/Tanks/tree2.png");
        this.wind1Image = loadImage("src/main/resources/Tanks/wind-1.png");
        this.windImage = loadImage("src/main/resources/Tanks/wind.png");
    }

    private void loadConfig() {
        this.config = loadJSONObject(configPath);
    }

//    private void initializeBoard() {
//        layout = new char[rows][cols];
//        currentLevel = "level3.txt"; // Start with level1.txt
//        loadLayout(currentLevel);
//    }

    private void processLevelData() {
        if (config == null) {
            // Handle the case when config is not loaded
            System.err.println("Config is not loaded!");
            return;
        }
        JSONArray levelsArray = config.getJSONArray("levels");
        List<Level> levels = new ArrayList<>();
        int currentLevelIndex = 0;

        for (int i = 0; i < levelsArray.size(); i++) {
            JSONObject levelData = levelsArray.getJSONObject(i);
            String layout = levelData.getString("layout");

            if (!layout.equals(currentLevel)) {
                // If layout does not match the current level, skip to the next level
                continue;
            }

            String background = levelData.getString("background");
            String foregroundColor = levelData.getString("foreground-colour"); // Convert int to String
//            System.out.println(foregroundColor);
            this.foregroundColor = foregroundColor;
            String trees = null;
            if (levelData.hasKey("trees")) {
                trees = levelData.getString("trees");
            }

            Level level = new Level(layout, background, foregroundColor, trees);
            levels.add(level);

            currentLevelIndex++;

        }
    }

    public void loadLayout(String layoutFile) {
        File file = new File(layoutFile);
        if (!file.exists()) {
            System.err.println("Layout file not found: " + layoutFile);
            return;
        }
        try {
            Scanner sc = new Scanner(file);
            treePositions.clear();
            terrainPositions.clear();
            tankPositions.clear();
            terrainHeights.clear();
            terrainHeights2.clear();


            for (int i = 0; i < 897; i++) {
                terrainHeights2.add(0f);
            }

            for (int i = 0; i < cols * CELLSIZE; i++) {
                terrainHeights.add((float) HEIGHT);
            }

            for (int row = 0; row < rows; row++) {
                String line = sc.nextLine();
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }
                int length = line.length();

//            // Skip empty lines at the beginning of the file
//            while (sc.hasNextLine()) {
//                String line = sc.nextLine().trim();
//                if (!line.isEmpty()) {
//                    break;
//                }
//            }
//
//            for (int row = 0; row < rows; row++) {
//                if (!sc.hasNextLine()) {
//                    break; // No more lines to read
//                }
//                String line = sc.nextLine();
//                if (line.isEmpty()) {
//                    continue; // Skip empty lines
//                }
//                int length = line.length();


                for (int col = 0; col < length; col++) {
                    layout[row][col] = line.charAt(col);

                    int x = col * 32;
                    int y = row * 32;

                    switch (layout[row][col]) {
                        case 'T':
                            treePositions.add(new PVector(x, y));
                            break;
                        case 'X':
                            for (int j = 0; j < CELLSIZE; j++) {
                                for (int k = x; k <= x + 32; k++) {
                                        terrainHeights2.set(k, (float) y);
//                                    System.out.println(terrainHeights2);
                                }
                            }
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                            tankPositions.put(layout[row][col], new PVector(x, y));
//                            System.out.println(tankPositions);
                            break;
                    }
                }

                for (int col = length; col < cols; col++) {
                    layout[row][col] = ' ';
                }

            }


            List<Float> smoothedTerrainHeights = computeMovingAverage(terrainHeights2, CELLSIZE);
            terrainHeights2.clear();
            terrainHeights2.addAll(smoothedTerrainHeights);

            System.out.println(smoothedTerrainHeights);

            List<Float> smoothedTerrainHeights2 = computeMovingAverage(smoothedTerrainHeights, CELLSIZE);
            terrainHeights2.clear();
            terrainHeights2.addAll(smoothedTerrainHeights2);

            System.out.println("Terrain Heights: " + smoothedTerrainHeights2.size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            System.err.println("Layout file is empty or incomplete.");
        }
    }

//    public List<Float> computeMovingAverage(List<Float> values, int windowSize) {
//        List<Float> movingAverage = new ArrayList<>();
//        System.out.println("computeMovingAverage called with values size: " + values.size() + " and windowSize: " + windowSize);
//
//        if (values.size() < windowSize) {
//            return values; // Return the original list if the size is less than the window size
//        }
//
//        for (int i = 0; i <= values.size() - windowSize; i++) {
//            float sum = 0;
//            for (int j = i; j < i + windowSize; j++) {
//                sum += values.get(j);
//            }
//            movingAverage.add(sum / windowSize);
//        }
//
//        for (int i = 0; i < windowSize; i++) {
//            movingAverage.add(movingAverage.get(movingAverage.size() - 1));
////            System.out.println(movingAverage);
//        }
//        System.out.println("computeMovingAverage result size: " + movingAverage.size());
//        System.out.println(movingAverage);
//
//        return movingAverage;
//    }
public List<Float> computeMovingAverage(List<Float> values, int windowSize) {
    List<Float> movingAverage = new ArrayList<>();
    System.out.println("computeMovingAverage called with values size: " + values.size() + " and windowSize: " + windowSize);

    // Filter out empty values (assuming 0 represents an empty value)
    List<Float> filteredValues = new ArrayList<>();
    for (Float value : values) {
        if (value != 0) { // Change this condition if a different value represents "empty"
            filteredValues.add(value);
        }
    }

    System.out.println("Filtered values size: " + filteredValues.size());
    System.out.println(filteredValues);

    // Return the original list if the size of filtered values is less than the window size
    if (filteredValues.size() < windowSize) {
        return values;
    }

    // Compute the moving average on the filtered list
    for (int i = 0; i <= filteredValues.size() - windowSize; i++) {
        float sum = 0;
        for (int j = i; j < i + windowSize; j++) {
            sum += filteredValues.get(j);
        }
        movingAverage.add(sum / windowSize);
    }

    // Extend the moving average list to match the original list size
    while (movingAverage.size() < values.size()) {
        movingAverage.add(movingAverage.get(movingAverage.size() - 1));
    }

    System.out.println("computeMovingAverage result size: " + movingAverage.size());
    System.out.println(movingAverage);

    return movingAverage;
}



    public void drawMap() {
        if (currentLevel.equals("level1.txt")) {
            image(snowImage, 0, 0);
        } else if (currentLevel.equals("level2.txt")) {
            image(desertImage, 0, 0);
        } else if (currentLevel.equals("level3.txt")) {
            image(basicImage, 0, 0);
        }

        for (int x = 0; x < terrainHeights2.size(); x++) {
            float y = terrainHeights2.get(x);

            if (y < HEIGHT) {
                if (currentLevel.equals("level1.txt")) {
                    stroke(255, 255, 255);
                    fill(255, 255, 255);
                } else if (currentLevel.equals("level2.txt")) {
                    stroke(234,221,181);
                    fill(234,221,181);
                } else if (currentLevel.equals("level3.txt")) {
                    stroke(120,171,0);
                    fill(120,171,0);
                }
                line(x, y, x, HEIGHT);
            }
        }

    }


    @Override
    public void keyPressed() {
        float deltaTime = 1.0f / FPS;
        Tank tank = tanks[currentPlayer];
        switch (key) {
            case 'w':
                tank.increasePower();
                break;
            case 's':
                tank.decreasePower();
                break;
            case ' ':
                Projectile projectile = new Projectile(this, tank.x, tank.y, tank.turretAngle, tank.power);
                projectiles.add(projectile);
                tank.fire();
                currentPlayer = (currentPlayer + 1) % tanks.length;
                wind += (int) random(-5, 6);  // Change wind after each turn
                break;
            case 'r':
                restartGame();
                break;
            case 'R':
                restartGame();
                break;
        }
        if (keyCode == UP) {
//            tank.aimUp();
            tank.aimUp(deltaTime);
        } else if (keyCode == DOWN) {
//            tank.aimDown();
            tank.aimDown(deltaTime);
        } else if (keyCode == LEFT) {
//            tank.moveLeft();
            tank.moveLeft(deltaTime);
        } else if (keyCode == RIGHT) {
//            tank.moveRight();
            tank.moveRight(deltaTime);
        }
    }

    private void restartGame() {
        setup();
    }

    @Override
    public void keyReleased() {

    }


    @Override
    public void mousePressed(MouseEvent e) {
        // Handle mouse pressed events
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Handle mouse released events
    }

    void drawScoreTable() {
        int tableX = width - 130; // Adjust the value to control the distance from the right edge
        int tableY = 80; // Adjust the value to control the distance from the top edge
        int tableWidth = 120;
        int tableHeight = 110;
        int rowHeight = 20;

        // Define player colors
        int[] playerColors = new int[4];
        playerColors[0] = color(0, 0, 255);   // Tank A (Blue)
        playerColors[1] = color(255, 0, 0);   // Tank B (Red)
        playerColors[2] = color(0, 255, 255); // Tank C (Cyan)
        playerColors[3] = color(255, 255, 0); // Tank D (Yellow)


        pushStyle();
        // Draw the table background
        stroke(0);

        strokeWeight(4);
        noFill(); // Slightly transparent white background
        rect(tableX, tableY, tableWidth, tableHeight);

        // Draw the table header
        fill(0);
        textAlign(CENTER, CENTER);
        text("Scores", tableX + 27, tableY + 10);

        line(tableX, tableY + 20, tableX + tableWidth, tableY + 20);
        popStyle();
        // Draw the player scores
        int rowY = tableY + 30;
        textAlign(LEFT, CENTER);
        for (int i = 0; i < tanks.length; i++) {
            Tank tank = tanks[i];
            fill(playerColors[i]);
            stroke(0);
            text("Player " + (char)('A' + i), tableX + 5, rowY);
            fill(playerColors[i]);
            stroke(0);
            text(tank.getScore(), tableX + tableWidth - 17, rowY);
            rowY += rowHeight;
        }
    }


    @Override
    public void draw() {
        drawMap();
        drawScoreTable();

        // Update and draw the projectiles
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            projectile.update();
            projectile.display();
            if (projectile.isOffScreen()) {
                projectiles.remove(i);
            }
        }

        float deltaTime = 1.0f / FPS;

        for (Tree tree : trees) {
            System.out.println(trees);
            tree.draw();
        }


        // Draw health bar
        int healthBarX = width / 2 - 100; // Adjust the x-coordinate as needed
        int healthBarY = 10; // Adjust the y-coordinate as needed
        int healthBarWidth = 200; // Adjust the width as needed
        int healthBarHeight = 30; // Adjust the height as needed

        fill(0);
        text("Health: ", healthBarX, healthBarY + healthBarHeight / 2); // Display "Health: " text

        int healthTextWidth = (int) textWidth("Health: "); // Get the width of the "Health: " text
        int healthValueX = healthBarX + healthTextWidth + 5; // Calculate the x-coordinate for the health value

        fill(255, 0, 0); // Set the color for the background of the health bar (red)
        rect(healthValueX, healthBarY, healthBarWidth - healthTextWidth - 5, healthBarHeight); // Draw the background rectangle

        int playerHealth = tanks[currentPlayer].getHealth();
        float healthBarFill = map(playerHealth, 0, 100, 0, healthBarWidth - healthTextWidth - 5); // Map the health value to the width of the bar

        fill(0, 0, 255); // Set the color for the fill of the health bar (blue)
        rect(healthValueX, healthBarY, healthBarFill, healthBarHeight); // Draw the fill rectangle

        fill(0);
        text(playerHealth, healthValueX + healthBarFill + 5, healthBarY + healthBarHeight / 2); // Display health value

        // Draw power text
        textSize(14);
        textAlign(CENTER, TOP); // Align text horizontally to the center and vertically to the top
        float powerTextX = width / 2 - 66; // Calculate the x-coordinate for the center of the screen
        float powerTextY = 55; // Adjust the y-coordinate for the top position (you can change this value as needed)
        fill(0); // Set text color to green
        text("Power: " + tanks[currentPlayer].getPower(), powerTextX, powerTextY); // Display power value

        // HUD Text
        textSize(17);
        textAlign(LEFT, TOP);
        fill(0); // Set text color to white
        String playerTurnText = "Player " + (char) ('A' + currentPlayer) + "'s turn";
        text(playerTurnText, 22, 14); // Display player turn text

        // Display fuel and wind information
        textSize(14);
        textAlign(LEFT, TOP); // Align text to the left
        fill(0); // Set text color to white

        // Display fuel value next to the fuel image
        int fuelTextX = fuelX + fuelImage.width + 155;
        int fuelTextY = fuelY + 15;
        text(tanks[currentPlayer].fuel, fuelTextX, fuelTextY);

        // Display tanks
        if (tanks[0] != null) {
            tanks[0].displayA();
            tanks[0].update(deltaTime);
        }
        if (tanks[1] != null) {
            tanks[1].displayB();
            tanks[0].update(deltaTime);
        }
        if (tanks[2] != null) {
            tanks[2].displayC();
            tanks[0].update(deltaTime);
        }
        if (tanks[3] != null) {
            tanks[3].displayD();
            tanks[0].update(deltaTime);
        }

        // Load and display the fuel image
        fuelImage.resize(25, 25);
        int fuelX = 155;
        int fuelY = 10;
        image(fuelImage, fuelX, fuelY);

        // Calculate the x and y coordinates for the top right corner
        int windImageX = WIDTH - windImage.width - 40; // Adjust the value '10' to control the distance from the right edge
        int windImageY = 8; // Adjust this value to control the distance from the top edge

        // Draw the image at the calculated coordinates
        image(windImage, windImageX, windImageY);
        text(wind, windImageX + 70, windImageY + 20); // Align wind text to the right

    }

    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }
}