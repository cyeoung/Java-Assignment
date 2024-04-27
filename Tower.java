package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

/**
 * Represents a tower in the game, including its attributes like range, firing speed, and damage,
 * and behaviors like firing at monsters.
 */
public class Tower {
    private float range;
    private float firingSpeed;
    private int damage;
    
    private int rangeLevel;
    private int speedLevel;
    private int damageLevel;

    private int x;
    private int y;
    private int currentRow;
    private int currentCol;

    public List<Fireball> fireballs = new ArrayList<>();

    private PImage fireball;
    private int framesSinceFire;

    /**
     * Constructs a new Tower with the given attributes.
     *
     * @param range The range of the tower.
     * @param firingSpeed The firing speed of the tower.
     * @param damage The damage inflicted by the tower's attacks.
     * @param rangeLevel The initial level of the tower's range attribute.
     * @param speedLevel The initial level of the tower's speed attribute.
     * @param damageLevel The initial level of the tower's damage attribute.
     * @param row The row position of the tower on the game grid.
     * @param col The column position of the tower on the game grid.
     * @param fireball The visual representation of the tower's fireball attack.
     */
    public Tower(float range, float firingSpeed, int damage, int rangeLevel, int speedLevel, int damageLevel, int row, int col, PImage fireball) {
        this.range = range;
        this.firingSpeed = firingSpeed;
        this.framesSinceFire = (int) (60 / firingSpeed);
        this.damage = damage;
        
        this.rangeLevel = rangeLevel;
        this.speedLevel = speedLevel;
        this.damageLevel = damageLevel;
 
        this.x = col;
        this.y = row;
        this.currentCol = this.x * 32;
        this.currentRow = this.y * 32 + 40;
        this.fireball = fireball;
    }

    /**
     * Retrieves the current range level of the tower.
     *
     * @return The range level.
     */
    public int getRangeLevel(){
        return this.rangeLevel;
    }

    /**
     * Retrieves the current speed level of the tower.
     *
     * @return The speed level.
     */
    public int getSpeedLevel(){
        return this.speedLevel;
    }

    /**
     * Retrieves the current damage level of the tower.
     *
     * @return The damage level.
     */
    public int getDamageLevel(){
        return this.damageLevel;
    }

    /**
     * Retrieves the current row position of the tower on the game grid.
     *
     * @return The row position.
     */
    public int getCurrentRow(){
        return this.currentRow;
    }

    /**
     * Retrieves the current column position of the tower on the game grid.
     *
     * @return The column position.
     */
    public int getCurrentCol(){
        return this.currentCol;
    }

    /**
     * Retrieves the grid row coordinate of the tower.
     *
     * @return The grid row coordinate.
     */
    public int getRow(){
        return this.y;
    }

    /**
     * Retrieves the grid column coordinate of the tower.
     *
     * @return The grid column coordinate.
     */
    public int getCol(){
        return this.x;
    }

    /**
     * Retrieves the current range of the tower's attack.
     *
     * @return The attack range.
     */
    public float getRange(){
        return this.range;
    }

    /**
     * Updates the tower's state, including firing at monsters within range.
     *
     * @param monsters The list of monsters currently on the game grid.
     */
    public void update(List<Monster> monsters){
        int fireIntervalFrames = (int) (60 / firingSpeed);
        framesSinceFire++;
        List<Monster> monstersInRange = new ArrayList<>();
        for (Monster monster : monsters) {
            if (isWithinRange(monster) && monster.gethp() > 0) {  
                monstersInRange.add(monster);
            }
        }

        if (!monstersInRange.isEmpty() && framesSinceFire >= fireIntervalFrames) {
            int randomIndex = (int) (Math.random() * monstersInRange.size());
            Monster target = monstersInRange.get(randomIndex);

            Fireball fireball = new Fireball((float) (this.currentCol + 16),(float) (this.currentRow  + 16), target, this.fireball);
            fireballs.add(fireball);
            
            framesSinceFire = 0;
        }

        Iterator<Fireball> iterator = fireballs.iterator();
        while (iterator.hasNext()) {
            Fireball fireball = iterator.next();
            fireball.move();
            if (fireball.hasHitTarget()) {
                fireball.getTarget().setHp(damage);
                iterator.remove();
            }
        }
    }

    /**
     * Draws the tower's fireballs on the game grid.
     *
     * @param app The PApplet instance used for drawing.
     */
    public void draw(PApplet app) {
        if (fireballs != null){
            for (Fireball fireball : fireballs) {
                fireball.draw(app);
            }
        }
    }

    /**
     * Checks if a given monster is within the tower's attack range.
     *
     * @param monster The monster to check.
     * @return true if the monster is within range, false otherwise.
     */
    private boolean isWithinRange(Monster monster) {
        if(monster.currentRow >= 0 && monster.currentRow <= 640 && monster.currentCol >= 40 && monster.currentCol <= 680){
            float distance = PApplet.dist(this.currentCol + 16, this.currentRow + 16, monster.currentRow + 10, monster.currentCol + 10);
            return distance <= this.range;
        }
        return false; 
    }

    /**
     * Upgrades the tower's range attribute and increases its range level.
     */
    public void upgradeRange() {
        range += 32;
        rangeLevel++;
    }

    /**
     * Upgrades the tower's range attribute and increases its range level.
     */
    public void upgradeSpeed() {
        firingSpeed += 0.5;
        speedLevel++;
    }
    /**
     * Upgrades the tower's damage attribute and increases its damage level.
     *
     * @param initialTowerDamage The initial damage attribute of the tower.
     */
    public void upgradeDamage(int initialTowerDamage) {
        damage += initialTowerDamage / 2;
        damageLevel++;
    }

    /**
     * Checks if all upgrade levels of the tower are at zero.
     *
     * @return true if all levels are zero, false otherwise.
     */
    public boolean isAllLevelZero() {
        return rangeLevel == 0 && speedLevel == 0 && damageLevel == 0;
    }
    
    /**
     * Checks if all upgrade levels of the tower are at one.
     *
     * @return true if all levels are one, false otherwise.
     */
    public boolean isAllLevelOne() {
        return rangeLevel == 1 && speedLevel == 1 && damageLevel == 1;
    }

    /**
     * Checks if all upgrade levels of the tower are at two.
     *
     * @return true if all levels are two, false otherwise.
     */
    public boolean isAllLevelTwo() {
        return rangeLevel == 2 && speedLevel == 2 && damageLevel == 2;
    }

    /**
     * Retrieves the minimum upgrade level among range, speed, and damage.
     *
     * @return The minimum level.
     */
    public int getMinLevel() {
        return Math.min(rangeLevel, Math.min(speedLevel, damageLevel));
    }
}