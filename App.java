package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.MouseEvent;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.util.*;

/**
 * The {@code App} class extends {@code PApplet} and implements {@code ImageRotator}, serving as the main class for the WizardTD game.
 * It initializes and manages the game's graphical window, game elements, resources, and game logic. 
 * It interacts with the player through mouse and keyboard inputs and renders game elements on the screen.
 * This class also handles the game loop, updating the game state, and drawing the screen at a specified frame rate.
 * 
 * The game is grid-based, with different cells representing various in-game elements, including the player's towers, 
 * enemy paths, and spawning points. The game progresses through waves of enemies with different attributes and behaviors.
 * The player's objective is to strategically place and upgrade towers to prevent enemies from reaching the end of their path.
 * 
 * This class uses Processing library methods to create the game window, handle user inputs, and render game elements.
 * The game's configuration, including the initial setup of towers and enemy waves, is loaded from a JSON file.
 *
 * @author Zhengyu Li
 */
public class App extends PApplet implements ImageRotator {

    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE+TOPBAR;

    public static final int FPS = 60;

    public String configPath;
    public JSONObject config;

    private PImage grass;
    private PImage shrub;
    private PImage path_0;
    private PImage path_1;
    private PImage path_2;
    private PImage path_3;
    private PImage gremlin;
    private PImage gremlin_1;
    private PImage gremlin_2;
    private PImage gremlin_3;
    private PImage gremlin_4;
    private PImage gremlin_5;
    private PImage beetle;
    private PImage worm;
    private PImage tower_0;
    private PImage tower_1;
    private PImage tower_2;
    private PImage wizardHouse;
    private PImage fireball;

    private Mana mana;
    private FreezePotions potions;

    private Board board;
    private char[][] layout;

    private Waves waves;

    private Towers towers;
    private Tower hoveredTower;

    private GameOptions gameOptions;

    private boolean gameOver = false;
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    /**
     * Constructor for creating an instance of the App class.
     * Initializes the path to the configuration file for loading game settings.
     */
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
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);

        // Load images during setup
        this.grass = this.loadImage("src/main/resources/WizardTD/grass.png");
        this.shrub = this.loadImage("src/main/resources/WizardTD/shrub.png");
        this.path_0 = this.loadImage("src/main/resources/WizardTD/path0.png");
        this.path_1 = this.loadImage("src/main/resources/WizardTD/path1.png");
        this.path_2 = this.loadImage("src/main/resources/WizardTD/path2.png");
        this.path_3 = this.loadImage("src/main/resources/WizardTD/path3.png");
        this.tower_0 = this.loadImage("src/main/resources/WizardTD/tower0.png");
        this.tower_1 = this.loadImage("src/main/resources/WizardTD/tower1.png");
        this.tower_2 = this.loadImage("src/main/resources/WizardTD/tower2.png");
        this.wizardHouse = this.loadImage("src/main/resources/WizardTD/wizard_house.png");
        this.gremlin = this.loadImage("src/main/resources/WizardTD/gremlin.png");
        this.gremlin_1 = this.loadImage("src/main/resources/WizardTD/gremlin1.png");
        this.gremlin_2 = this.loadImage("src/main/resources/WizardTD/gremlin2.png");
        this.gremlin_3 = this.loadImage("src/main/resources/WizardTD/gremlin3.png");
        this.gremlin_4 = this.loadImage("src/main/resources/WizardTD/gremlin4.png");
        this.gremlin_5 = this.loadImage("src/main/resources/WizardTD/gremlin5.png");
        this.beetle = this.loadImage("src/main/resources/WizardTD/beetle.png");
        this.worm = this.loadImage("src/main/resources/WizardTD/worm.png");
        this.fireball = this.loadImage("src/main/resources/WizardTD/fireball.png");

        this.config = loadJSONObject(configPath);
        String layoutFile = config.getString("layout");
        JSONArray wavesArray = config.getJSONArray("waves");
        int initialTowerRange = config.getInt("initial_tower_range");
        float initialTowerFiringSpeed = config.getFloat("initial_tower_firing_speed");
        int initialTowerDamage = config.getInt("initial_tower_damage");
        int towerCost = config.getInt("tower_cost");
        float initialMana = config.getFloat("initial_mana");
        float initialManaCap = config.getFloat("initial_mana_cap");
        float initialManaGained = config.getFloat("initial_mana_gained_per_second");
        float manaPollSpellInitialCost = config.getFloat("mana_pool_spell_initial_cost");
        float manaPoolSpellCostIncreasePerUse = config.getFloat("mana_pool_spell_cost_increase_per_use");
        float manaPoolSpellCapMultiplier = config.getFloat("mana_pool_spell_cap_multiplier");
        float manaPoolSpellManaGainedMultiplier = config.getFloat("mana_pool_spell_mana_gained_multiplier");

        this.mana = new Mana(initialMana, initialManaCap, initialManaGained, manaPollSpellInitialCost, manaPoolSpellCostIncreasePerUse, manaPoolSpellCapMultiplier, manaPoolSpellManaGainedMultiplier, this);
        this.potions = new FreezePotions(mana);

        this.board = new Board(20,20,this,layoutFile,this.grass,this.shrub,this.path_0,this.path_1,this.path_2,this.path_3,this.wizardHouse);
        this.layout = this.board.getLayout();
  
        this.waves = new Waves(wavesArray,this.layout,this.mana,this.gremlin,this.gremlin_1,this.gremlin_2,this.gremlin_3,this.gremlin_4,this.gremlin_5,this.beetle,this.worm);

        this.gameOptions = new GameOptions(towerCost, this.mana, this.potions);

        this.towers = new Towers(this.mana, initialTowerRange, initialTowerFiringSpeed, initialTowerDamage, towerCost,this.layout, this.gameOptions, this.tower_0, this.tower_1, this.tower_2, this.fireball);
    }


    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(){
        if (!gameOver) {
            gameOptions.handleKeyPressed(key);
        } else if (key == 'r' || key == 'R') {
            restartGame();
        }
    }

    /**
     * Receive mouse pressed signal.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        gameOptions.checkClick(mouseX, mouseY);
        towers.click(mouseX, mouseY);
    }

    /**
     * Receive mouse moved signal.
     */
    @Override
    public void mouseMoved() {
        gameOptions.checkHover(mouseX, mouseY);
        this.hoveredTower = towers.hover(mouseX, mouseY);
        towers.hoverBuild(mouseX, mouseY);
    }

     /**
     * Restarts the game, resetting the game over state and reinitializing 
     * game elements to their default states.
     */
    private void restartGame() {
        gameOver = false;
        setup();
    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        background(132, 115, 74);

        if (!gameOver) {
            if (!gameOptions.isPaused()) {
                int updateTimes = 1;
                if (gameOptions.isFastForward()) {
                    updateTimes = 2;
                }
                for (int i = 0; i < updateTimes; i++) {
                    this.mana.update();
                    this.waves.update(this);
                    List<Monster> activeMonsters = this.waves.getActiveMonsters();
                    this.potions.setMonsters(activeMonsters);
                    this.towers.update(activeMonsters);
                    this.potions.update();
                }
            }

            this.mana.draw(this);
            this.board.draw_map(this);
            this.waves.draw(this);
            this.gameOptions.draw(this);
            this.towers.drawTowers(this);
            this.potions.draw(this);

            if (hoveredTower != null) {
                this.towers.drawUpgradeBox(this, this.hoveredTower);
                this.towers.drawAttackRange(this, hoveredTower);
            }   

            this.board.draw_wizard(this);
        }else{
            this.mana.draw(this);
            this.board.draw_map(this);
            this.waves.draw(this);
            this.gameOptions.draw(this);
            this.towers.drawTowers(this);
            this.potions.draw(this);

            if (hoveredTower != null) {
                this.towers.drawUpgradeBox(this, this.hoveredTower);
                this.towers.drawAttackRange(this, hoveredTower);
            }

            this.board.draw_wizard(this);
        }

        if (waves.getWon() || mana.getLose()) {
            gameOver = true;
        }
        
        if (gameOver) {
            this.pushStyle();
           if (mana.getLose()) {
                textSize(35);
                textAlign(CENTER, CENTER);
                text("YOU LOST", (width - 120) / 2, height / 2 - 132);
                textSize(25);
                text("Press 'r' to restart", (width - 120) / 2, height / 2 - 75);
            } else if (waves.getWon()) {
                textSize(35);
                textAlign(CENTER, CENTER);
                text("YOU WIN", (width - 120) / 2, height / 2 - 132);
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
        PApplet.main("WizardTD.App");
    }

    /**
     * Source: https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     * @param pimg The image to be rotated
     * @param angle between 0 and 360 degrees
     * @return the new rotated image
     */
    @Override
    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = this.createImage(newWidth, newHeight, RGB);
        //BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }
}
