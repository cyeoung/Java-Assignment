package Tanks;

import org.checkerframework.checker.units.qual.A;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import Tanks.Tree;


public class App extends PApplet {
    public static final int CELLSIZE = 32; // 8;
    public static final int CELLHEIGHT = 32;
    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; // CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; // BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH / CELLSIZE;
    public static final int BOARD_HEIGHT = 20;
    public static final int INITIAL_PARACHUTES = 1;
    public static final int FPS = 30;
    public String configPath;
    public static Random random = new Random();

    private ArrayList<Level> levels;
    private int currentLevelIndex = 0;
    private Level currentLevel;

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */
    @Override
    public void setup() {
        frameRate(FPS);
        initLevels();
        currentLevel = levels.get(currentLevelIndex);
    }

    private void initLevels() {
        // Load the config.json file
        JSONObject config = loadJSONObject("config.json");
        JSONArray levelsConfig = config.getJSONArray("levels");

        // Initialize the levels
        levels = new ArrayList<>();
        for (int i = 0; i < levelsConfig.size(); i++) {
            JSONObject levelConfig = levelsConfig.getJSONObject(i);
            String layoutFile = levelConfig.getString("layout");
            String foregroundColor = levelConfig.getString("foreground-colour");
            String treesFile = levelConfig.getString("trees");
            String backgroundFile = levelConfig.getString("background");

            // Initialize the terrain
            Terrain terrain = new Terrain(layoutFile, foregroundColor);

            // Initialize the tanks
            ArrayList<Tank> tanks = initTanks(config);

            // Initialize the trees
            ArrayList<Tree> trees = initTrees(layoutFile, treesFile);

            // Create the level
            Level level = new Level(terrain, tanks, backgroundFile, treesFile);
            levels.add(level);
        }
    }

    private ArrayList<Tank> initTanks(JSONObject config) {
        ArrayList<Tank> tanks = new ArrayList<>();
        JSONObject playerColors = config.getJSONObject("player_colours");
        for (Object playerCharObj : playerColors.keys()) {
            String playerChar = playerCharObj.toString();
            String playerColor = playerColors.getString(playerChar);
            Tank tank = new Tank(playerChar.charAt(0), playerColor);
            tanks.add(tank);
        }
        return tanks;
    }

    private ArrayList<Tree> initTrees(String layoutFile, String treesFile) {
        ArrayList<Tree> trees = new ArrayList<>();
        // Implement the logic to parse the layout file and create the trees
        // For example:
        trees.add(new Tree(100, 200, treesFile));
        return trees;
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        // Implement the logic to handle key presses
    }

    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased() {
        // Implement the logic to handle key releases
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Implement the logic to handle mouse presses
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Implement the logic to handle mouse releases
    }

    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        background(255);

        // Draw the current level
        currentLevel.drawLevel(this);

        // Update the game objects for the current frame
        currentLevel.updateLevel(1.0f / frameRate);
    }

    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }
}