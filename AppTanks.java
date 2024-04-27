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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
// import java.util.logging;

public class App extends PApplet {

    // private String URLBasic = "/Users/cyeoungjpg/Desktop/tanks_scaffold/src/main/resources/Tanks/basic.png";
    // private String URLTree1 = "/Users/cyeoungjpg/Desktop/tanks_scaffold/src/main/resources/Tanks/tree1.png";
    // private PImage backgroundImgBasic;
    // private PImage backgroundImgTree1;

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = HEIGHT / CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 1;

    public static final int FPS = 30;

    public String configPath;
    public JSONObject config;

    public static Random random = new Random();
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    private PImage basicImage;
    private PImage desertImage;
    private PImage forestImage;
    private PImage fuelImage;
    private PImage hillsImage;
    private PImage parachuteImage;
    private PImage snowImage;
    private PImage tree1Image;
    private PImage tree2Image;
    private PImage wind1Image;
    private PImage windImage;

    private Fuel fuel;
    private Power power;

    private Board board;
    private char[][] layout;

    private Levels levels;

    private Tanks tanks;
    // private Tower hoveredTower;

    private GameOptions gameOptions;

    private boolean gameOver = false;

    public App() {
        this.configPath = "config.json";
    }

    public JSONObject getConfig() {
        return config;
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        // backgroundImgBasic = loadImage(URLBasic, "png");
        // backgroundImgTree1 = loadImage(URLTree1, "png");
        frameRate(FPS);
    
        this.basicImage = this.loadImage("src/main/resources/Tanks/basic.png");
        this.desertImage = this.loadImage("src/main/resources/Tanks/desert.png");
        this.forestImage = this.loadImage("src/main/resources/Tanks/forest.png");
        this.fuelImage = this.loadImage("src/main/resources/Tanks/fuel.png");
        this.hillsImage = this.loadImage("src/main/resources/Tanks/hills.png");
        this.parachuteImage = this.loadImage("src/main/resources/Tanks/parachute.png");
        this.snowImage = this.loadImage("src/main/resources/Tanks/snow.png");
        this.tree1Image = this.loadImage("src/main/resources/Tanks/tree1.png");
        this.tree2Image = this.loadImage("src/main/resources/Tanks/tree2.png");
        this.wind1Image = this.loadImage("src/main/resources/Tanks/wind-1.png");
        this.windImage = this.loadImage("src/main/resources/Tanks/wind.png");
    
        this.config = loadJSONObject(configPath);
        JSONObject config = this.config;
    
        // Read the "levels" array
        JSONArray levelsArray = config.getJSONArray("levels");
    
        // Assuming you have variables initialFuel, initialFuelLost, and power defined
        this.fuel = new Fuel(initialFuel, initialFuelLost, this);
        this.power = new Power(power);
    
        // Process the level data
        for (int i = 0; i < levelsArray.length(); i++) {
            JSONObject level = levelsArray.getJSONObject(i);
            String layout = level.getString("layout");
            String background = level.getString("background");
            String foregroundColour = level.getString("foreground-colour");
            String trees = level.optString("trees", null);
    
            // Create the Board object
            this.board = new Board(App.CELLSIZE, this, layoutString, getBackgroundImage(background), getFuelImage(), this.hillsImage, getTreeImage(trees), this.tree2Image, this.windImage, this.wind1Image);
            this.layout = this.board.getLayout();
    
            // Create the Levels object
            this.levels = new Levels(levelsArray, this.layout, this.fuel, this.Power, getBackgroundImage(background), getForegroundColor(foregroundColour), getTreeImage(trees));
        }
    
        // Read the "player_colours" object
        JSONObject playerColours = config.getJSONObject("player_colours");
        Iterator<String> keys = playerColours.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = playerColours.getString(key);
            // Process the player color data as needed
        }
    }
    
    // Helper methods
    private PImage getBackgroundImage(String backgroundName) {
        if (backgroundName.equals("snow.png")) {
            return this.snowImage;
        } else if (backgroundName.equals("desert.png")) {
            return this.desertImage;
        } else if (backgroundName.equals("basic.png")) {
            return this.basicImage;
        }
        // Add more cases for other background images
        return null;
    }
    
    private PImage getTreeImage(String treeName) {
        if (treeName != null) {
            if (treeName.equals("tree1.png")) {
                return this.tree1Image;
            } else if (treeName.equals("tree2.png")) {
                return this.tree2Image;
            }
        }
        return null;
    }
    
    private color getForegroundColor(String colorString) {
        if (colorString != null) {
            String[] colorValues = colorString.split(",");
            if (colorValues.length == 3) {
                return color(parseInt(colorValues[0]), parseInt(colorValues[1]), parseInt(colorValues[2]));
            }
        }
        return color(0); // Default color (black)
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        if (!gameOver) {
            gameOptions.handleKeyPressed(key);
        } else if (key == 'r' || key == 'R') {
            restartGame();
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){

        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TODO - powerups, like repair and extra fuel and teleport
        // gameOptions.checkClick(mouseX, mouseY);
        // tanks.click(mouseX, mouseY);

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    private void restartGame() {
        gameOver = false;
        setup();
    }

    // private int[][] terrain; // 2D array to store terrain heights
    // private String[] levelData; 

    // private void loadLevel(String fileName) {
        // // levelData = loadStrings(fileName);
        // List<String> terrainData = readTerrainData(fileName);
        // terrain = new int[BOARD_WIDTH][BOARD_HEIGHT];
    
        // for (int y = 0; y < terrainData.size(); y++) {
        //     String line = terrainData.get(y).trim(); // Trim leading/trailing spaces
        //     for (int x = 0; x < BOARD_WIDTH; x++) {
        //         char c;
        //         if (x < line.length()) {
        //             c = line.charAt(x);
        //         } else {
        //             c = ' '; // Treat empty spaces as terrain
        //         }
        //         switch (c) {
        //             case 'X':
        //                 terrain[x][y] = CELLHEIGHT;
        //                 break;
        //             case 'A':
        //                 // Set position of player A's tank
        //                 break;
        //             case 'B':
        //                 // Set position of player B's tank
        //                 break;
        //             case 'T':
        //                 terrain[x][y] = CELLHEIGHT;
        //                 break;
        //             case ' ':
        //                 terrain[x][y] = CELLHEIGHT; // Treat spaces as terrain
        //                 break;
        //             // Handle other cases ...
        //         }
        //     }
        // }
    
        // // Apply terrain smoothing algorithm
        // smoothTerrain();
        // 1D Array
    // try {
    //     File file = new File(fileName);
    //     Scanner scanner = new Scanner(file);
    //     terrain = new int[BOARD_WIDTH][BOARD_HEIGHT];

    //     int y = 0;
    //     while (scanner.hasNextLine() && y < BOARD_HEIGHT) {
    //         String line = scanner.nextLine().trim();
    //         for (int x = 0; x < line.length() && x < BOARD_WIDTH; x++) {
    //             char c = line.charAt(x);
    //             switch (c) {
    //                 case 'X':
    //                     terrain[x][y] = CELLHEIGHT;
    //                     break;
    //                 case 'A':
    //                     // Set position of player A's tank
    //                     break;
    //                 case 'B':
    //                     // Set position of player B's tank
    //                     break;
    //                 case 'T':
    //                     terrain[x][y] = CELLHEIGHT;
    //                     break;
    //                 case ' ':
    //                     terrain[x][y] = 0; // Treat spaces as no terrain
    //                     break;
    //                 // Handle other cases ...
    //             }
    //         }
    //         y++;
    //     }

    //     scanner.close();

    //     // Apply terrain smoothing algorithm
    //     smoothTerrain();
    // } catch (FileNotFoundException e) {
    //     System.out.println("File not found!");
    // }
    // }

    // private List<String> readTerrainData(String fileName) {
    //     List<String> terrainData = new ArrayList<>();
    //     try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             terrainData.add(line);
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return terrainData;
    // }

    // private void smoothTerrain() {
    //     int[][] smoothedTerrain = new int[BOARD_WIDTH][BOARD_HEIGHT];
    
    //     // Apply moving average once
    //     for (int y = 0; y < BOARD_HEIGHT; y++) {
    //         for (int x = 0; x < BOARD_WIDTH; x++) {
    //             int sum = 0;
    //             int count = 0;
    //             for (int i = -CELLAVG / 2; i <= CELLAVG / 2; i++) {
    //                 int xPos = x + i;
    //                 if (xPos >= 0 && xPos < BOARD_WIDTH) {
    //                     sum += terrain[xPos][y];
    //                     count++;
    //                 }
    //             }
    //             smoothedTerrain[x][y] = sum / count;
    //         }
    //     }
    
    //     // Apply moving average again
    //     for (int y = 0; y < BOARD_HEIGHT; y++) {
    //         for (int x = 0; x < BOARD_WIDTH; x++) {
    //             int sum = 0;
    //             int count = 0;
    //             for (int i = -CELLAVG / 2; i <= CELLAVG / 2; i++) {
    //                 int xPos = x + i;
    //                 if (xPos >= 0 && xPos < BOARD_WIDTH) {
    //                     sum += smoothedTerrain[xPos][y];
    //                     count++;
    //                 }
    //             }
    //             terrain[x][y] = sum / count;
    //         }
    //     }



    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {

        // image(backgroundImgBasic, 0, 0);
        // image(backgroundImgTree1, 200, 200);


        // for (int x = 0; x < BOARD_WIDTH; x++) {
        //     for (int y = 0; y < BOARD_HEIGHT; y++) {
        //         int terrainHeight = terrain[x][y];
        //         if (terrainHeight > 0) {
        //             fill(0); // Set the fill color to black
        //             rect(x * CELLSIZE, HEIGHT - TOPBAR - (y + 1) * CELLSIZE, CELLSIZE, terrainHeight);
        //         }
        //     }
        // }
        


        // stroke(255); // Set the stroke color to white
        // for (int x = 0; x <= BOARD_WIDTH; x++) {
        //     line(x * CELLSIZE, HEIGHT - TOPBAR, x * CELLSIZE, HEIGHT - TOPBAR - BOARD_HEIGHT * CELLSIZE);
        // }
        // for (int y = 0; y <= BOARD_HEIGHT; y++) {
        //     line(0, HEIGHT - TOPBAR - y * CELLSIZE, BOARD_WIDTH * CELLSIZE, HEIGHT - TOPBAR - y * CELLSIZE);
        // }

        // for (int x = 0; x < BOARD_WIDTH; x++) {
        //     for (int y = 0; y < BOARD_HEIGHT; y++) {
        //         int terrainHeight = terrain[x][y];
        //         if (terrainHeight > 0) {
        //             fill(0); // Set the fill color to black
        //             rect(x * CELLSIZE, HEIGHT - TOPBAR - (y + 1) * CELLSIZE, CELLSIZE, terrainHeight);
        //         }
        //     }
        // }
        
        

        //----------------------------------
        //display HUD:
        //----------------------------------
        //TODO

        //----------------------------------
        //display scoreboard:
        //----------------------------------
        //TODO 
        
		//----------------------------------
        //----------------------------------

        //TODO: Check user action
        background(255, 255, 255);

        if (!gameOver) {
            if (!gameOptions.isPaused()) {
                int updateTimes = 1;
                if (gameOptions.isFastForward()) {
                    updateTimes = 2;
                }
    
                for (int i = 0; i < updateTimes; i++) {
                    this.fuel.update();
                    this.levels.update(this);
                }
            }
    
            this.fuel.draw(this);
            this.board.drawMap(this);
            this.levels.draw(this);
            this.gameOptions.draw(this);
            this.tanks.drawTanks(this);
            // Draw other game entities
    
            // Hover logic for tanks or other entities
            // if (hoveredTank != null) {
            //     this.tanks.drawUpgradeBox(this, this.hoveredTank);
            //     this.tanks.drawAttackRange(this, hoveredTank);
            // }
        } else {
            this.fuel.draw(this);
            this.board.drawMap(this);
            this.levels.draw(this);
            this.gameOptions.draw(this);
            this.tanks.drawTanks(this);
            // Draw other game entities
    
            // Hover logic for tanks or other entities
            // if (hoveredTank != null) {
            //     this.tanks.drawUpgradeBox(this, this.hoveredTank);
            //     this.tanks.drawAttackRange(this, hoveredTank);
            // }
        }
    
        if (levels.isGameWon() || fuel.isGameLost()) {
            gameOver = true;
        }
    
        if (gameOver) {
            this.pushStyle();
            if (fuel.isGameLost()) {
                textSize(35);
                textAlign(CENTER, CENTER);
                text("YOU LOST", (WIDTH - 120) / 2, HEIGHT / 2 - 132);
                textSize(25);
                text("Press 'r' to restart", (WIDTH - 120) / 2, HEIGHT / 2 - 75);
            } else if (levels.isGameWon()) {
                textSize(35);
                textAlign(CENTER, CENTER);
                text("YOU WIN", (WIDTH - 120) / 2, HEIGHT / 2 - 132);
            }
            this.popStyle();
        }
    }

    /**
     * The entry point of the application.
     *
     * @param args The command-line arguments.
     */

    public static void main(String[] args) {
        PApplet.main("Tanks.App");
        
    }
    /**
    * @param turretImage The image to be rotated
    * @param angle between 0 and 360 degrees
    * @return the new rotated image
    */

    @Override
    public void rotateTurret(double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = turretWidth;
        int h = turretHeight;
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);
    
        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
    
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);
        int x = w / 2;
        int y = h / 2;
        at.rotate(rads, x, y);
        at.scale(1, -1); // Invert the y-axis
        at.translate(0, -h); // Translate to account for the inverted y-axis
        g2d.setTransform(at);
        g2d.drawImage(turretImage, 0, 0, null);
        g2d.dispose();
    
        turretImage = new BufferedImage(rotated.getColorModel(), rotated.getRaster(), rotated.isAlphaPremultiplied(), null);
    }

}


// package Tanks;

// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Random;
// import java.nio.file.Files;
// import java.nio.file.Paths;

// import processing.core.PApplet;
// import processing.core.PVector;
// import processing.core.PImage;
// import processing.data.JSONObject;
// import processing.data.JSONArray;
// import processing.event.KeyEvent;
// import processing.event.MouseEvent;


// public class App extends PApplet {

//     private String URLBasic = "/Users/cyeoungjpg/Desktop/tanks_scaffold/src/main/resources/Tanks/basic.png";
//     private PImage backgroundImgBasic;

//     public static final int CELLSIZE = 32; // 8;
//     public static final int CELLHEIGHT = 32;
//     public static final int CELLAVG = 32;
//     public static final int TOPBAR = 0;
//     public static int WIDTH = 864; // CELLSIZE*BOARD_WIDTH;
//     public static int HEIGHT = 640; // BOARD_HEIGHT*CELLSIZE+TOPBAR;
//     public static final int BOARD_WIDTH = WIDTH / CELLSIZE;
//     public static final int BOARD_HEIGHT = 20;
//     public static final int INITIAL_PARACHUTES = 1;
//     public static final int FPS = 30;
//     public String configPath;
//     public static Random random = new Random();

//     private boolean upPressed = false;
//     private boolean downPressed = false;
//     private boolean leftPressed = false;
//     private boolean rightPressed = false;
//     private boolean wPressed = false;
//     private boolean sPressed = false;
//     private boolean spacePressed = false;

//     private float turretRotation = 0; // in radians
//     private float tankPositionX = 0;
//     private float tankPositionY = 0;

//     private int player1Parachutes = 3;
//     private int player2Parachutes = 3;

//     private Map<Character, List<PVector>> positions = new HashMap<>();

//     public static final String FILE_PATH1 = "tanks/level1.txt";
//     public static final String FILE_PATH2 = "tanks/level2.txt";
//     public static final String FILE_PATH3 = "tanks/level3.txt";
//     private static final int INITIAL_FUEL = 250;
//     private static final int INITIAL_HEALTH = 100;
//     private static final int INITIAL_POWER = 50;

//     private String[] levelLayout;
        
//     int tankFuel = INITIAL_FUEL;
//     int tankHealth = INITIAL_HEALTH;
//     int turretPower = INITIAL_POWER;

//     public void settings() {
//         size(CELLSIZE * BOARD_WIDTH, CELLSIZE * BOARD_HEIGHT);
//     }
//     private int currentLevelIndex = 1;

//     public void setup() {

//         size();

//         frameRate(FPS);
//         JSONObject config = loadJSONObject("config.json");
//         String level1LayoutFile = config.getJSONObject("levels").getString("layout");
//         String[] level1Layout = loadStrings("level1.txt");
//         String[] level2Layout = loadStrings("level2.txt");
//         String[] level3Layout = loadStrings("level3.txt");

//         Map<Integer, String[]> levelLayouts = new HashMap<>();
//         levelLayouts.put(1, level1Layout);
//         levelLayouts.put(2, level2Layout);
//         levelLayouts.put(3, level3Layout);
//         try {
//             // Read the content of config.json file
//             String content = new String(Files.readAllBytes(Paths.get("config.json")));
            
//             // Parse JSON content
//             JSONObject json = new JSONObject(content);

//             // Access levels array
//             JSONArray levels = json.getJSONArray("levels");

//             // Access player_colours object
//             JSONObject playerColours = json.getJSONObject("player_colours");

//             // Process levels
//             for (int i = 0; i < levels.length(); i++) {
//                 JSONObject level = levels.getJSONObject(i);

//                 // Access level properties
//                 String layout = level.getString("layout");
//                 String background = level.getString("background");
//                 String foregroundColour = level.getString("foreground-colour");
//                 String trees = level.optString("trees", ""); // Optional field, use optString to handle missing keys

//                 // Process each level data...
//             }

//             // Process player colours
//             // You can access player colours using playerColours.getString("A") and so on

//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//         levelLayout = loadStrings(level1LayoutFile);
//         readPositionsFromFile(FILE_PATH1);
//     }

//     public void keyPressed() {
//         switch (keyCode) {
//             case UP:
//                 upPressed = true;
//                 break;
//             case DOWN:
//                 downPressed = true;
//                 break;
//             case LEFT:
//                 leftPressed = true;
//                 break;
//             case RIGHT:
//                 rightPressed = true;
//                 break;
//         }

//         switch (key) {
//             case 'w':
//             case 'W':
//                 wPressed = true;
//                 break;
//             case 's':
//             case 'S':
//                 sPressed = true;
//                 break;
//             case ' ':
//                 spacePressed = true;
//                 break;
//         }
//     }

//     public void keyReleased() {
//         switch (keyCode) {
//             case UP:
//                 upPressed = false;
//                 break;
//             case DOWN:
//                 downPressed = false;
//                 break;
//             case LEFT:
//                 leftPressed = false;
//                 break;
//             case RIGHT:
//                 rightPressed = false;
//                 break;
//         }

//         switch (key) {
//             case 'w':
//             case 'W':
//                 wPressed = false;
//                 break;
//             case 's':
//             case 'S':
//                 sPressed = false;
//                 break;
//             case ' ':
//                 spacePressed = false;
//                 break;
//         }
//     }

//     public void mousePressed(MouseEvent e) {
//         // Add functionality for mouse press event if needed
//     }

//     public void mouseReleased(MouseEvent e) {
//         // Add functionality for mouse release event if needed
//     }

//     private boolean tankIsInMidair() {
//         int tankColumn = (int) (tankPositionX / CELLSIZE); // Convert tank's x position to column index
//         int tankRow = (int) (tankPositionY / CELLSIZE); // Convert tank's y position to row index
    
//         Object[] terrain;
//         // Check if the tank is within the bounds of the terrain
//         if (tankColumn < 0 || tankColumn >= terrain[0].length || tankRow < 0 || tankRow >= terrain.length) {
//             return true; // Tank is outside terrain bounds, thus in midair
//         }
    
//         // Check if there is terrain at the tank's position
//         return terrain[tankRow][tankColumn] == 0; // Assuming 0 represents no terrain
//     }

//     public void updateTankPosition(){
//         // Update tank position
//         if (leftPressed && tankFuel > 0) {
//             tankPositionX -= 60.0 / FPS;
//             tankFuel--;
//         }
//         if (rightPressed && tankFuel > 0) {
//             tankPositionX += 60.0 / FPS;
//             tankFuel--;
//         }
//     }

//     public void draw() {

//         drawPositions();

//         if (tankIsInMidair()) {
//             deployParachute();
//         }

//         updateTankPosition();

//         // Update turret rotation
//         if (upPressed) {
//             turretRotation += 3 * (PI / FPS);
//         }
//         if (downPressed) {
//             turretRotation -= 3 * (PI / FPS);
//         }

//         // Update turret power
//         if (wPressed && turretPower < tankHealth) {
//             turretPower += 36 / FPS;
//         }
//         if (sPressed && turretPower > 0) {
//             turretPower -= 36 / FPS;
//         }
       
//         drawTank(tankPositionX, tankPositionY, turretRotation);

//         // Display HUD
//         fill(0);
//         textSize(16);
//         text("Fuel: " + tankFuel, 10, 20);
//         text("Health: " + tankHealth, 10, 40);
//         text("Power: " + (int) turretPower, 10, 60);
//     }

//     private void drawParachute(float tankX, float tankY) {
//         // Draw parachute above the tank
//         float parachuteX = tankX;
//         float parachuteY = tankY - 40; // Adjust the distance above the tank

//         // Draw parachute canopy
//         fill(255, 0, 0); // Red color
//         ellipse(parachuteX, parachuteY, 50, 20);

//         // Draw parachute strings
//         stroke(0); // Black color for strings
//         line(parachuteX - 25, parachuteY, parachuteX - 25, parachuteY + 40); // Left string
//         line(parachuteX + 25, parachuteY, parachuteX + 25, parachuteY + 40); // Right string
//     }

//     private void deployParachute() {
//         if (tankFuel <= 0 && tankHealth > 0) {
//             int currentPlayer;
//             // Check if parachutes are available
//             if (currentPlayer == 1 && player1Parachutes > 0) {
//                 tankPositionY += 60 / FPS; // Descend with parachute
//                 player1Parachutes--; // Deduct one parachute
//             } else if (currentPlayer == 2 && player2Parachutes > 0) {
//                 tankPositionY += 60 / FPS; // Descend with parachute
//                 player2Parachutes--; // Deduct one parachute
//             } else {
//                 // Descend without parachute and sustain damage
//                 tankPositionY += 120 / FPS;
//                 tankHealth -= tankPositionY; // Damage based on height
//             }
//         }
//     }

//     // Method to fire a projectile
//     private void fireProjectile() {
//         // Calculate velocity magnitude based on power level
//         float velocityMagnitude = map(turretPower, 0, tankHealth, 1, 9);

//         // Calculate velocity components based on turret rotation
//         float velocityX = velocityMagnitude * cos(turretRotation);
//         float velocityY = velocityMagnitude * sin(turretRotation);

//         // Create projectile object with initial position and velocity
//         Projectile projectile = new Projectile(tankPositionX, tankPositionY, velocityX, velocityY);

//         // Add projectile to list of active projectiles
//         activeProjectiles.add(projectile);

//         // End player's turn
//         endPlayerTurn();
//     }

//     public void drawTank(float x, float y, float rotationAngle) {
//         pushMatrix();
//         translate(x, y);
//         rotate(rotationAngle);

//         // Body
//         fill(255, 0, 0); // Red color for the tank body
//         rect(-20, -10, 40, 20); // Rectangle representing the tank body

//         // Turret
//         fill(0, 0, 255); // Blue color for the turret
//         rect(-5, -15, 10, 30); // Rectangle representing the tank turret

//         // Gun
//         fill(0); // Black color for the gun
//         rect(0, -1, 20, 2); // Rectangle representing the tank gun

//         popMatrix();
//     }

//     private void readPositionsFromFile(String filePath) {
//         try {
//             BufferedReader reader = new BufferedReader(new FileReader(filePath));
//             String line;
//             int y = 0;
//             while ((line = reader.readLine()) != null) {
//                 char[] chars = line.toCharArray();
//                 for (int x = 0; x < chars.length; x++) {
//                     char c = chars[x];
//                     if (c != ' ') {
//                         List<PVector> positionsList = positions.getOrDefault(c, new ArrayList<>());
//                         positionsList.add(new PVector(x * CELLSIZE, y * CELLSIZE));
//                         positions.put(c, positionsList);
//                     }
//                 }
//                 y++;
//             }
//             reader.close();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
//     private void drawPositions() {
//         for (Map.Entry<Character, List<PVector>> entry : positions.entrySet()) {
//             char c = entry.getKey();
//             List<PVector> positionsList = entry.getValue();
//             switch (c) {
//                 case 'T':
//                     for (PVector position : positionsList) {
//                         drawTank(position.x, position.y, 0);
//                     }
//                     break;
//                 case 'X':
//                     for (PVector position : positionsList) {
//                         drawMountain(position.x, position.y);
//                     }
//                     break;
//                 // Add cases for other characters as needed
//             }
//         }
//     }

//     public void drawMountain(float x, float y) {
//         fill(100);  // Gray color for mountain
//         triangle(x, y, x + 30, y - 60, x + 60, y);  // Triangle representing mountain
//     }

//     public static void main(String[] args) {
//         PApplet.main("Tanks.App");
//     }
// }
