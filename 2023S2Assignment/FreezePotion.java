package WizardTD;

import java.util.*;

/**
 * The FreezePotion class represents a freeze potion that can be activated to
 * freeze and damage monsters within a certain range. The effects of the potion
 * last for a specified duration.
 */
public class FreezePotion {
    private final int range = 96;
    private final int damage = 30;
    private final int freezeTime = 3;
    private int freezeEffectDuration = 180;
    private int currentFrame = 0;
    private int x, y;

    /**
     * Constructs a new FreezePotion object with default parameters.
     */
    public FreezePotion() {
    }

     /**
     * Returns the range of effect of this freeze potion.
     *
     * @return The range of effect.
     */
    public int getRange() {
        return range;
    }

    /**
     * Returns the damage inflicted by this freeze potion to each affected monster.
     *
     * @return The damage value.
     */
    public int getDamage() {
        return damage;
    }

     /**
     * Returns the time for which monsters are frozen by this potion.
     *
     * @return The freeze time in frames.
     */
    public int getFreezeTime() {
        return freezeTime;
    }

    /**
     * Activates the freeze potion at the specified coordinates, freezing and damaging
     * all monsters within its range of effect.
     *
     * @param monsters The list of currently active monsters in the game.
     * @param clickX   The x-coordinate where the potion is activated.
     * @param clickY   The y-coordinate where the potion is activated.
     */
    public void activate(List<Monster> monsters, int clickX, int clickY) {
        this.x = clickX;
        this.y = clickY;
        for (Monster monster : monsters) {
            if (isWithinRange(monster, clickX, clickY)) {
                monster.takeDamage(damage);
                monster.freeze(freezeTime);
            }
        }
    }

    /**
     * Checks if a given monster is within the range of effect of the potion activated at
     * the specified coordinates.
     *
     * @param monster The monster to check.
     * @param x       The x-coordinate of the potion's activation point.
     * @param y       The y-coordinate of the potion's activation point.
     * @return True if the monster is within range, false otherwise.
     */
    public boolean isWithinRange(Monster monster, int x, int y){
        float dx = monster.currentRow - x;
        float dy = monster.currentCol - y;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        return distance <= range;
    }

     /**
     * Updates the freeze effect duration counter. This should be called each frame
     * to decrease the effect's remaining duration.
     */
    public void update() {
        if (currentFrame < freezeEffectDuration) {
            currentFrame++;
        }
    }

    /**
     * Checks if the freeze effect of the potion is still active.
     *
     * @return True if the effect is active, false otherwise.
     */
    public boolean isEffectActive() {
        return currentFrame < freezeEffectDuration;
    }

    /**
     * Returns the x-coordinate where the potion was activated.
     *
     * @return The x-coordinate of the activation point.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate where the potion was activated.
     *
     * @return The y-coordinate of the activation point.
     */
    public int getY() {
        return y;
    }
}
