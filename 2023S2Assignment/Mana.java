package WizardTD;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * The Mana class represents the mana resource in a game.
 * It includes methods to manage and visualize the player's current mana,
 * mana capacity, and interactions related to mana, such as spell casting.
 */
public class Mana {
    private float currentMana;
    public float manaCap;
    private float manaGainedPerFrame;
    private float manaPoolSpellCost;
    private float manaGainedMultiplier;
    private final float manaPoolSpellCostIncreasePerUse;
    private final float manaPoolSpellCapMultiplier;
    private final float manaPoolSpellManaGainedMultiplier;
    private int manaPollLevel;
    private PFont boldFont;
    private boolean lose = false;

    /**
     * Constructs a Mana object with specified initial values.
     *
     * @param initialMana The initial amount of mana.
     * @param initialManaCap The initial maximum amount of mana that can be held.
     * @param initialManaGained The initial rate at which mana is gained per frame.
     * @param manaPoolSpellInitialCost The initial cost of casting the mana pool spell.
     * @param manaPoolSpellCostIncreasePerUse The increase in cost for each subsequent casting of the mana pool spell.
     * @param manaPoolSpellCapMultiplier The multiplier effect on mana cap when the mana pool spell is cast.
     * @param manaPoolSpellManaGainedMultiplier The multiplier effect on mana gained when the mana pool spell is cast.
     * @param app The PApplet object, used for rendering.
     */
    public Mana(float initialMana, float initialManaCap, float initialManaGained,float manaPoolSpellInitialCost, 
    float manaPoolSpellCostIncreasePerUse, float manaPoolSpellCapMultiplier,
    float manaPoolSpellManaGainedMultiplier, PApplet app) {
        this.currentMana = initialMana;
        this.manaCap = initialManaCap;
        this.manaGainedPerFrame = initialManaGained / 60;
        this.manaPoolSpellCost = manaPoolSpellInitialCost; 
        this.manaPoolSpellCostIncreasePerUse = manaPoolSpellCostIncreasePerUse;
        this.manaPoolSpellCapMultiplier = manaPoolSpellCapMultiplier;
        this.manaPoolSpellManaGainedMultiplier = manaPoolSpellManaGainedMultiplier;
        this.manaGainedMultiplier = 1;
        this.manaPollLevel = 0;
        this.boldFont = app.createFont("", 26, true);
    }

    /**
     * Retrieves the current mana of the player.
     *
     * @return The current mana.
     */
    public float getCurrentMana(){
        return this.currentMana;
    }

    /**
     * Gets the current cost of casting the mana pool spell.
     * 
     * @return The current mana cost of casting the mana pool spell.
     */
    public float getManaPoolSpellCost(){
        return this.manaPoolSpellCost;
    }

    /**
     * Gets the multiplier for the rate at which mana is gained.
     * 
     * @return The multiplier for mana gained.
     */
    public float getManaGainedMultiplier(){
        return this.manaGainedMultiplier;
    }

    /**
     * Checks if the player has lost the game due to running out of mana.
     * 
     * @return true if the player has lost, false otherwise.
     */
    public boolean getLose(){
        return this.lose;
    }

    /**
     * Gets the current level of the mana pool spell.
     * 
     * @return The level of the mana pool spell, indicating how many times it has been cast.
     */
    public int getManaPollLevel(){
        return this.manaPollLevel;
    }

    /**
     * Sets the game state to lost when called, typically as a result of the player running out of mana.
     */
    public void lose(){
        this.lose = true;
    }

    /**
     * Sets the current mana of the player.
     * If mana goes below zero, the game is lost.
     * If mana exceeds the mana cap, it's set to the mana cap.
     *
     * @param x The amount to increase the current mana by. Can be negative to reduce mana.
     */
    public void setCurrentMana(float x){
        this.currentMana = this.currentMana + x;
        if (this.currentMana < 0) {
            this.currentMana = 0; // Make sure mana does not go below zero
            this.lose();
        }else if (this.currentMana > this.manaCap){
            this.currentMana = this.manaCap;
        }
    }

    /**
     * Updates the current mana, increasing it according to the mana gained per frame.
     * The mana will not exceed the mana cap.
     */
    public void update() {
        currentMana += manaGainedPerFrame * manaGainedMultiplier;
        if (currentMana > manaCap) {
            currentMana = manaCap;
        }
    }

    /**
     * Casts the mana pool spell, consuming mana and increasing mana cap and
     * mana gained per frame accordingly.
     */
    public void castManaPoolSpell() {
        if (currentMana >= manaPoolSpellCost) {
            currentMana -= manaPoolSpellCost;
            manaCap *= manaPoolSpellCapMultiplier;
            manaGainedMultiplier += (manaPoolSpellManaGainedMultiplier - 1);
            manaPoolSpellCost += manaPoolSpellCostIncreasePerUse;
            manaPollLevel += 1;
        }
    }

    /**
     * Draws the mana bar on the screen.
     *
     * @param app The PApplet object, used for rendering.
     */
    public void draw(PApplet app) {
        float barWidth = 340;
        float barHeight = 20;
        float barX = app.width - barWidth - 30;
        float barY = 10;
        float textOffset = 70;

        app.pushStyle();
        app.textFont(boldFont);
        app.fill(0);
        app.textSize(20);
        app.text("MANA:", barX - textOffset, barY + barHeight - 2);

        app.fill(255);
        app.stroke(0);
        app.strokeWeight(2);
        app.rect(barX, barY, barWidth, barHeight);  // Draw outer frame
    
        float manaPercentage = this.currentMana / this.manaCap;
        float filledWidth = barWidth * manaPercentage;
    
        app.fill(0, 255, 255);  // Aqua color
        app.stroke(0);
        app.strokeWeight(2);
        app.rect(barX, barY, filledWidth, barHeight);  // Draw inner fill

        app.fill(0);
        app.textSize(18);
        String manaText = (int)this.currentMana + " / " + (int)this.manaCap;
        app.text(manaText, barX + (barWidth / 2) - (app.textWidth(manaText) / 2), barY + barHeight - 3);
        app.popStyle();
    }
}
