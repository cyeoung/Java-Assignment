package WizardTD;

import processing.core.PApplet;

import java.util.*;

/**
 * This class manages the freezing potions in the game. It handles the creation, application, 
 * and effects of freeze potions, as well as their interactions with monsters.
 */
public class FreezePotions {
    private List<FreezePotion> potions;
    private Mana mana;
    public int quantity;
    private List<Monster> activeMonsters = new ArrayList<>();
    public List<FreezePotion> activePotions = new ArrayList<>();
    
    /**
     * Constructor for the FreezePotions class.
     * 
     * @param mana The mana system to be used for purchasing freeze potions.
     */
    public FreezePotions(Mana mana) {
        this.mana = mana;
        this.potions = new ArrayList<>();
        this.quantity = 0;
    }

    /**
     * Returns the current quantity of freeze potions available.
     * 
     * @return The quantity of available freeze potions.
     */
    public int getQuantity(){
        return this.quantity;
    }

    /**
     * Returns the list of freeze potions.
     * 
     * @return A list containing all the freeze potions.
     */
    public List<FreezePotion> getPotions(){
        return this.potions;
    }

    /**
     * Sets the list of active monsters that can be affected by the freeze potions.
     * 
     * @param activeMonsters A list of monsters currently active in the game.
     */
    public void setMonsters(List<Monster> activeMonsters){
        this.activeMonsters = activeMonsters;
    }

    /**
     * Purchases a freeze potion if enough mana is available and the maximum quantity of potions
     * has not been reached.
     */
    public void purchasePotion() {
        if (mana.getCurrentMana() >= 200 && this.quantity < 99) {
            mana.setCurrentMana(-200);
            potions.add(new FreezePotion());
            this.quantity += 1;
        }
    }

    /**
     * Applies the effect of a freeze potion to monsters in range when activated at the specified
     * coordinates. Reduces the quantity of available potions.
     * 
     * @param clickX The x-coordinate where the potion is applied.
     * @param clickY The y-coordinate where the potion is applied.
     */
    public void applyPotionEffect(int clickX, int clickY) {
        if (this.quantity <= 0 || activeMonsters == null || activeMonsters.isEmpty()) {
            return;
        }
        FreezePotion potion = potions.remove(potions.size() - 1);
        potion.activate(activeMonsters, clickX, clickY);
        this.quantity--;
        activePotions.add(potion);
    }

    /**
     * Updates the status of all active freeze potions, removing them if their effects have ended.
     */
    public void update() {
        Iterator<FreezePotion> iterator = activePotions.iterator();
        while (iterator.hasNext()) {
            FreezePotion potion = iterator.next();
            potion.update();
            if (!potion.isEffectActive()) {
                iterator.remove();
            }
        }
    }

    /**
     * Draws the active freeze potions on the game's interface, indicating their range and effect.
     * 
     * @param app The PApplet object used for drawing.
     * @return True if the drawing is successful, otherwise false.
     */
    public boolean draw(PApplet app) {
        for (FreezePotion potion : activePotions) {
                app.pushStyle();
                app.fill(200, 200, 255, 30);
                app.ellipse(potion.getX(), potion.getY(), potion.getRange() * 2, potion.getRange() * 2);
                app.popStyle();
                return true;
            }
            return false;
        }
    }
