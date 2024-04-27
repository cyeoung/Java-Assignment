package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

/**
 * Manages the placement, upgrading, and interaction of towers within the game, including the visualization of towers and their respective actions.
 */
public class Towers {
    private Mana mana;
    private int initialTowerRange;
    private float initialTowerFiringSpeed;
    private int initialTowerDamage;
    private int initialTowerCost;

    private char[][] layout;

    public List<Tower> towers = new ArrayList<Tower>();

    private boolean[][] hasTower;

    private GameOptions gameOptions;

    public int hoveredCol = 0;
    public int hoveredRow = 0;
    public boolean hovered = false;

    private PImage tower_0;
    private PImage tower_1;
    private PImage tower_2;
    private PImage fireball;

    /**
     * Constructs a Towers object initialized with specific properties, layout, and images for different tower states and actions.
     *
     * @param mana the mana instance used to manage the mana within the game
     * @param initialTowerRange the initial range that towers can attack
     * @param initialTowerFiringSpeed the initial speed at which towers fire
     * @param initialTowerDamage the initial damage towers deal to enemies
     * @param initialTowerCost the initial cost to build a tower
     * @param layout the 2D grid indicating the game layout
     * @param gameOptions the options/settings of the game
     * @param tower_0 the image representing the level 0 tower
     * @param tower_1 the image representing the level 1 tower
     * @param tower_2 the image representing the level 2 tower
     * @param fireball the image representing the fireball attack
     */
    public Towers(Mana mana, int initialTowerRange,float initialTowerFiringSpeed,int initialTowerDamage,int initialTowerCost,char[][] layout,GameOptions gameOptions,PImage tower_0,PImage tower_1,PImage tower_2,PImage fireball){
        this.mana = mana;
        this.initialTowerRange = initialTowerRange;
        this.initialTowerFiringSpeed = initialTowerFiringSpeed;
        this.initialTowerDamage = initialTowerDamage;
        this.initialTowerCost = initialTowerCost;
        this.layout = layout;
        this.hasTower = new boolean[20][20];
        this.tower_0 = tower_0;
        this.tower_1 = tower_1;
        this.tower_2 = tower_2;
        this.fireball = fireball;
        this.gameOptions = gameOptions;
    }
    
    /**
     * Retrieves the current game options associated with the towers.
     *
     * @return the current game options
     */
    public GameOptions getGameOptions(){
        return this.gameOptions;
    }

    /**
     * Updates the state of all towers, managing their interactions with the provided list of monsters.
     *
     * @param monsters the current list of monsters on the field
     * @param app the PApplet instance used for drawing
     */
    public void update(List<Monster> monsters){
        if (towers != null){
           for (Tower tower: towers){
                tower.update(monsters);
            }
        }
    }

    /**
     * Handles click events for building or upgrading towers at the specified mouse position.
     *
     * @param mouseX the X-coordinate of the mouse click
     * @param mouseY the Y-coordinate of the mouse click
     * @return true if a tower was built or upgraded, false otherwise
     */
    public boolean click(int mouseX, int mouseY) {
        int col = mouseX / 32;
        int row = (mouseY - 40) / 32;

        if (row >= 0 && row < 20 && col >= 0 && col < 20) {
            BuildOrUpgrade(row, col);
            return true;
        }
        return false;
    }

    /**
     * Handles the visualization of potential tower placement on the game grid when the player hovers over a cell.
     *
     * @param mouseX The X-coordinate of the mouse position.
     * @param mouseY The Y-coordinate of the mouse position.
     */
    public void hoverBuild(int mouseX, int mouseY) {
        int col = mouseX / 32;
        int row = (mouseY - 40) /32;
        if (row >= 0 && row < 20 && col >= 0 && col < 20) {
            if (gameOptions.getIsBuildTowerSelected() && layout[row][col] == ' ' && !hasTower[row][col]){
                this.hoveredCol = col * 32;
                this.hoveredRow = row * 32 + 40;
                this.hovered = true;
            }else{
                this.hovered = false;
            }
        }else{
            this.hovered = false;
        }
    }

    /**
     * Determines if the mouse is hovering over an existing tower on the game grid.
     *
     * @param mouseX The X-coordinate of the mouse position.
     * @param mouseY The Y-coordinate of the mouse position.
     * @return The tower over which the mouse is hovering, or null if the mouse is not hovering over any tower.
     */
    public Tower hover(int mouseX, int mouseY) {
        for (Tower tower : towers) {
            int x = tower.getCurrentCol();
            int y = tower.getCurrentRow();
    
            if (mouseX >= x && mouseX <= x + 32 && mouseY >= y && mouseY <= y + 32) {
                return tower;
            }
        }
        return null;
    }

    /**
     * Handles the building or upgrading of towers on the game grid, taking into account the selected options and available mana.
     *
     * @param row The row index on the game grid where the build or upgrade is initiated.
     * @param col The column index on the game grid where the build or upgrade is initiated.
     */
    public void BuildOrUpgrade(int row, int col) {
        if (this.layout[row][col] == ' ' && hasTower[row][col]) {
            Tower tower = getTowerAtPosition(row, col);
            if (tower != null) {
                if (this.gameOptions.getIsU1Selected()) {
                    float cost = 20 + tower.getRangeLevel() * 10;
                    if (this.mana.getCurrentMana() >= cost) {
                        tower.upgradeRange();
                        this.mana.setCurrentMana(-cost);
                    }
                }
                
                if (this.gameOptions.getIsU2Selected()) {
                    float cost = 20 + tower.getSpeedLevel() * 10;
                    if (this.mana.getCurrentMana() >= cost) {
                        tower.upgradeSpeed();
                        this.mana.setCurrentMana(-cost);
                    }
                }
    
                if (this.gameOptions.getIsU3Selected()) {
                    float cost = 20 + tower.getDamageLevel() * 10;
                    if (this.mana.getCurrentMana() >= cost) {
                        tower.upgradeDamage(this.initialTowerDamage);
                        this.mana.setCurrentMana(-cost);
                    }
                }
            }
        } else if (this.gameOptions.getIsBuildTowerSelected() && this.layout[row][col] == ' ' && !hasTower[row][col]) {
            float cost = this.initialTowerCost;
            if (this.gameOptions.getIsU1Selected()){
                cost += 20;
            }
            if (this.gameOptions.getIsU2Selected()){
                cost += 20;
            }
            if (this.gameOptions.getIsU3Selected()){
                cost += 20;
            }
    
            if (this.mana.getCurrentMana() >= cost){
                int range = this.initialTowerRange;
                float firingSpeed = this.initialTowerFiringSpeed;
                int damage = this.initialTowerDamage;
                int rangeLevel = 0;
                int speedLevel = 0;
                int damageLevel = 0;
    
                if (this.gameOptions.getIsU1Selected()){
                    range += 32;
                    rangeLevel += 1;
                }
                if (this.gameOptions.getIsU2Selected()){
                    firingSpeed += 0.5;
                    speedLevel += 1;
                }
                if (this.gameOptions.getIsU3Selected()){
                    damage += initialTowerDamage/2;
                    damageLevel += 1;
                }
    
                Tower tower = new Tower(range, firingSpeed, damage, rangeLevel, speedLevel, damageLevel, row, col, this.fireball);
                towers.add(tower);
                hasTower[row][col] = true;
    
                this.mana.setCurrentMana(-cost);
            } else {
                return;
            }
        }
    }

    /**
     * Draws a visual representation of a tower on the game grid where the player is considering placement, including potential upgrades.
     *
     * @param app The PApplet instance used for drawing the hovered tower.
     */
    private Tower getTowerAtPosition(int row, int col) {
        for (Tower tower : towers) {
            if (tower.getRow() == row && tower.getCol() == col) {
                return tower;
            }
        }
        return null;
    }


    /**
     * Visualizes towers on the screen, including their upgrades and different levels.
     *
     * @param app the PApplet instance used for drawing
     */
    public void drawTowers(PApplet app){
        if (towers != null){
            for(Tower tower: towers){
                int x = tower.getCurrentCol();
                int y = tower.getCurrentRow();

                if (tower.isAllLevelZero()) {
                    tower.draw(app);
                    app.image(tower_0, x, y);
                } else if (tower.isAllLevelOne()) {
                    tower.draw(app);
                    app.image(tower_1, x, y);
                } else if (tower.isAllLevelTwo()) {
                    tower.draw(app);
                    app.image(tower_2, x, y);
                }else if (tower.getMinLevel() == 0) {
                    tower.draw(app);
                    app.image(tower_0, x, y);
                    drawUpgrades(app, x, y ,tower);
                } else if (tower.getMinLevel() == 1) {
                    tower.draw(app);
                    app.image(tower_1, x, y);
                    drawUpgrades(app, x, y ,tower);
                } else {
                    tower.draw(app);
                    app.image(tower_2, x, y);
                    drawUpgrades(app, x, y ,tower);
                }
            }
        }
        drawHoverTowers(app);
    }

    /**
     * Draws a visual representation of a tower on the game grid where the player is considering placement, including potential upgrades.
     *
     * @param app The PApplet instance used for drawing the hovered tower.
     */
    public void drawHoverTowers(PApplet app){
        if (this.hovered ){
            if (!hasTower[(this.hoveredRow - 40) / 32][this.hoveredCol / 32]){
                int range = this.initialTowerRange;
                app.tint(128);
                if (gameOptions.getIsU1Selected() && gameOptions.getIsU2Selected() && gameOptions.getIsU3Selected()) {
                    app.image(tower_1, this.hoveredCol, this.hoveredRow);
                    range += 32;
                } else {
                    app.image(tower_0, this.hoveredCol, this.hoveredRow);
                    if (gameOptions.getIsU1Selected()) {
                        range += 32;
                        app.pushStyle();
                        app.stroke(200, 0, 255);
                        app.noFill();
                        app.ellipse(this.hoveredCol + 4, this.hoveredRow + 4, 4, 4);
                        app.popStyle();
                    }
                    if (gameOptions.getIsU2Selected()) {
                        app.pushStyle();
                        app.stroke(135, 206, 235);
                        app.strokeWeight(1);
                        app.noFill();
                        app.rect(this.hoveredCol + 5, this.hoveredRow + 5, 32 - 5 * 2, 32 - 5 * 2);
                        app.popStyle();
                    }
                    if (gameOptions.getIsU3Selected()) {
                        app.pushStyle();
                        app.textSize(8);
                        app.fill(200, 0, 255);
                        app.text('X', this.hoveredCol, this.hoveredRow + 32);
                        app.popStyle();
                    }
                }
                app.pushStyle();
                app.clip(0, App.TOPBAR, 20 * App.CELLSIZE, 20 * App.CELLSIZE);
                app.noFill();
                app.stroke(255, 255, 0, 128);
                app.strokeWeight(2);
                int x = this.hoveredCol + 16;
                int y = this.hoveredRow + 16;
                app.ellipse(x, y, range * 2, range * 2);
                app.noClip();
                app.popStyle();
                app.noTint();
            }
        }   
    }

    /**
     * Visualizes the upgrades of a specific tower on the game grid.
     *
     * @param app The PApplet instance used for drawing.
     * @param x The X-coordinate of the top left corner of the tower cell.
     * @param y The Y-coordinate of the top left corner of the tower cell.
     * @param tower The tower whose upgrades are to be visualized.
     */
    public void drawUpgrades(PApplet app, int x, int y, Tower tower) {
        int min = Math.min(tower.getMinLevel(), 2);

        if (tower.getSpeedLevel() - min> 0) {
            app.pushStyle();
            int speedLevelExcess = tower.getSpeedLevel() - min;
            app.stroke(135, 206, 235);
            app.strokeWeight(speedLevelExcess);
            app.noFill();
            int strokeOffset = speedLevelExcess / 2;
            app.rect(x + 5 - strokeOffset, 
                 y + 5 - strokeOffset, 
                 32 - 5 * 2 + strokeOffset * 2, 
                 32 - 5 * 2 + strokeOffset * 2);
            app.popStyle();
        }

        app.pushStyle();
        if (tower.getRangeLevel() > 0) {
            app.stroke(200, 0, 255);
            app.noFill(); 
            for (int i = 0; i < tower.getRangeLevel() - min; i++) {
                app.ellipse(x + 4 + i * (7), y + 4, 4, 4);
            }
        }
        app.popStyle();

        app.pushStyle();
        app.textSize(8);
        if (tower.getDamageLevel() > 0) {
            app.fill(200, 0, 255);
            for (int i = 0; i < tower.getDamageLevel() - min; i++) {
                app.text('X', x + i * 7, y + 32);
            }
        }
        app.popStyle();
    }

    /**
     * Displays a box with detailed information about the selected upgrades, including their costs.
     *
     * @param app The PApplet instance used for drawing.
     * @param tower The tower for which the upgrade information is displayed.
     */
    public void drawUpgradeBox(PApplet app, Tower tower) {
        app.pushStyle();
        int x = 650;
        int y = 670;
        int width = 90;
        int lineHeight = 15;

        Map<String, Integer> selectedUpgradesCost = gameOptions.getSelectedUpgradesCost(tower);

        if (selectedUpgradesCost.size() == 0) {
            return;
        }

        int totalHeight = (selectedUpgradesCost.size() + 2) * lineHeight + 10;

        app.fill(255); 
        app.rect(x, y - totalHeight, width, totalHeight); 

        app.fill(0); 
        app.textSize(12);  
        app.textAlign(PApplet.LEFT, PApplet.TOP);
        int offsetY = -totalHeight + 3;
        app.text("Upgrade cost", x + 5, y + offsetY - 2);
        offsetY += lineHeight;

        app.stroke(0);  
        app.line(x, y + offsetY, x + width, y + offsetY);
        offsetY += 3;

        int totalCost = 0;
        for (String upgradeType : selectedUpgradesCost.keySet()) {
            Integer cost = selectedUpgradesCost.get(upgradeType);
            app.text(upgradeType + ": " + cost, x + 5, y + offsetY);
            totalCost += cost;
            offsetY += lineHeight;
        }

        offsetY += 3;
        app.line(x, y + offsetY, x + width, y + offsetY);
        offsetY += 3;

        app.text("Total: " + totalCost, x + 5, y + offsetY - 2);
        app.popStyle();
    }

    /**
     * Visualizes the attack range of a specific tower on the game grid.
     *
     * @param app The PApplet instance used for drawing.
     * @param tower The tower whose attack range is to be visualized.
     */
    public void drawAttackRange(PApplet app, Tower tower) {
        app.pushStyle();
        app.clip(0, App.TOPBAR, 20 * App.CELLSIZE, 20 * App.CELLSIZE);
        app.noFill();
        app.stroke(255, 255, 0);
        app.strokeWeight(2);
        int x = tower.getCurrentCol() + 16;
        int y = tower.getCurrentRow() + 16;
        app.ellipse(x, y, tower.getRange() * 2, tower.getRange() * 2);
        app.noClip();
        app.popStyle();
    }
}
