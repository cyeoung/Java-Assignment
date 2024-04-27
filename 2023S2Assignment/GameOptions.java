package WizardTD;

import java.util.*;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Represents the game options
 */
public class GameOptions {
    private Mana mana;
    private FreezePotions potions;
    private boolean isFFSelected;
    private boolean isPauseSelected;
    private boolean isBuildTowerSelected;
    private boolean isU1Selected;
    private boolean isU2Selected;
    private boolean isU3Selected;
    private boolean isMSelected;
    public boolean isFFHovered;
    public boolean isPauseHovered;
    public boolean isBuildTowerHovered;
    public boolean isU1Hovered;
    public boolean isU2Hovered;
    public boolean isU3Hovered;
    public boolean isMHovered;
    private boolean isBuyFreezePotionSelected;
    private boolean isUseFreezePotionSelected;
    public boolean isBuyFreezePotionHovered;
    public boolean isUseFreezePotionHovered;
    public int initialTowerCost;

    /**
     * Constructor initializes the game options with specified initial tower cost, mana, and potions.
     * @param initialTowerCost The initial cost for building a tower.
     * @param mana The mana object to be used in the game.
     * @param potions The potions object to be used in the game.
     */
    public GameOptions(int initialTowerCost, Mana mana, FreezePotions potions) {
        this.isFFSelected = false;
        this.isPauseSelected = false;
        this.isBuildTowerSelected = false;
        this.isU1Selected = false;
        this.isU2Selected = false;
        this.isU3Selected = false;
        this.isMSelected = false;
        this.isFFHovered = false;
        this.isPauseHovered = false;
        this.isBuildTowerHovered = false;
        this.isU1Hovered = false;
        this.isU2Hovered = false;
        this.isU3Hovered = false;
        this.isMHovered = false;
        this.isBuyFreezePotionSelected = false;
        this.isUseFreezePotionSelected = false;
        this.isBuyFreezePotionHovered = false;
        this.isUseFreezePotionHovered = false;
        this.initialTowerCost = initialTowerCost;
        this.mana = mana;
        this.potions = potions;
    }

     /**
     * Checks if the use freeze potion option is selected.
     * 
     * @return True if the freeze potion is selected, false otherwise.
     */
    public boolean getIsUseFreezePotionSelected(){
        return this.isUseFreezePotionSelected;
    }

    /**
     * Checks if the build tower option is selected.
     *
     * @return True if build tower is selected, false otherwise.
     */
    public boolean getIsBuildTowerSelected(){
        return this.isBuildTowerSelected;
    }

    /**
     * Checks if the U1 upgrade option is selected.
     *
     * @return True if U1 upgrade is selected, false otherwise.
     */
    public boolean getIsU1Selected(){

        return this.isU1Selected;
    }

    /**
     * Checks if the U2 upgrade option is selected.
     *
     * @return True if U2 upgrade is selected, false otherwise.
     */
    public boolean getIsU2Selected(){

        return this.isU2Selected;
    }

    /**
     * Checks if the U3 upgrade option is selected.
     *
     * @return True if U3 upgrade is selected, false otherwise.
     */
    public boolean getIsU3Selected(){
        return this.isU3Selected;
    }

    /**
     * Checks if the fast forward option is selected.
     *
     * @return True if fast forward is selected, false otherwise.
     */
    public boolean isFastForward() {
        return isFFSelected;
    }
    
    /**
     * Checks if the game is paused.
     *
     * @return True if the game is paused, false otherwise.
     */
    public boolean isPaused() {
        return isPauseSelected;
    }

    /**
     * Handles key presses and toggles the corresponding game options.
     * @param key The character representing the pressed key.
     */
    public void handleKeyPressed(char key) {
        if (key == 'f' || key == 'F') {
            isFFSelected = !isFFSelected;
        }else if(key == 'p' || key == 'P') {
            isPauseSelected = !isPauseSelected;
        }else if(key == 't' || key == 'T') {
            isBuildTowerSelected = !isBuildTowerSelected;
        }else if(key == '1') {
            isU1Selected = !isU1Selected;
        }else if(key == '2') {
            isU2Selected = !isU2Selected;
        }else if(key == '3') {
            isU3Selected = !isU3Selected;
        }else if(key == 'm' || key == 'M') {
            this.mana.castManaPoolSpell();
        }else if (key == 'b' || key == 'B') {
            this.potions.purchasePotion();
        }else if(key == 'u' || key == 'U') {
            isUseFreezePotionSelected = !isUseFreezePotionSelected;
        }
    }

    /**
     * Checks if the mouse is hovering over any of the game option buttons.
     * @param mouseX The X coordinate of the mouse cursor.
     * @param mouseY The Y coordinate of the mouse cursor.
     */
    public void checkHover(int mouseX, int mouseY) {
        isFFHovered = mouseX >= 650 && mouseX <= 695 && mouseY >= 50 && mouseY <= 95;
        isPauseHovered = mouseX >= 650 && mouseX <= 695 && mouseY >= 110 && mouseY <= 155;
        isBuildTowerHovered = mouseX >= 650 && mouseX <= 695 && mouseY >= 170 && mouseY <= 215;
        isU1Hovered = mouseX >= 650 && mouseX <= 695 && mouseY >= 230 && mouseY <= 275;
        isU2Hovered = mouseX >= 650 && mouseX <= 695 && mouseY >= 290 && mouseY <= 335;
        isU3Hovered = mouseX >= 650 && mouseX <= 695 && mouseY >= 350 && mouseY <= 395;
        isMHovered = mouseX >= 650 && mouseX <= 695 && mouseY >= 410 && mouseY <= 455;
        isBuyFreezePotionHovered = mouseX >= 650 && mouseX <= 695 && mouseY >= 470 && mouseY <= 515;
        isUseFreezePotionHovered = mouseX >= 650 && mouseX <= 695 && mouseY >= 530 && mouseY <= 575;
    }

    /**
     * Handles mouse clicks and toggles the corresponding game options or performs actions.
     * @param mouseX The X coordinate of the mouse cursor.
     * @param mouseY The Y coordinate of the mouse cursor.
     */
    public void checkClick(int mouseX, int mouseY) {
        if (mouseX >= 650 && mouseX <= 695) {
            if (mouseY >= 50 && mouseY <= 95) {
                isFFSelected = !isFFSelected;
            } else if (mouseY >= 110 && mouseY <= 155) {
                isPauseSelected = !isPauseSelected;
            } else if (mouseY >= 170 && mouseY <= 215){
                isBuildTowerSelected = !isBuildTowerSelected;
            }else if (mouseY >= 230 && mouseY <= 275){
                isU1Selected = !isU1Selected;
            }else if (mouseY >= 290 && mouseY <= 335){
                isU2Selected = !isU2Selected;
            }else if (mouseY >= 350 && mouseY <= 395){
                isU3Selected = !isU3Selected;
            }else if (mouseY >= 410 && mouseY <= 455){
                this.mana.castManaPoolSpell();
            }else if (mouseY >= 470 && mouseY <= 515) {
                this.potions.purchasePotion();
            }else if (mouseY >= 530 && mouseY <= 575) {
                isUseFreezePotionSelected = !isUseFreezePotionSelected;
            }
        }else if(isUseFreezePotionSelected){
            int col = mouseX / 32;
            int row = (mouseY - 40) / 32;
            if (row >= 0 && row < 20 && col >= 0 && col < 20) {
                this.potions.applyPotionEffect(col * 32 + 16, row * 32 + 56);
            }
        }
    }

    /**
     * Draws the game options buttons on the screen.
     * @param app The PApplet object to be used for drawing.
     */
    public void draw(PApplet app) {
        drawButton(650, 50, "FF", "2x speed", isFFSelected, isFFHovered, app);
        drawButton(650, 110, "P", "PAUSE", isPauseSelected, isPauseHovered, app);
        drawButton(650, 170, "T", "BUILD\nTOWER", isBuildTowerSelected, isBuildTowerHovered, app);
        drawButton(650, 230, "U1", "Upgrade\nrange", isU1Selected, isU1Hovered, app);
        drawButton(650, 290, "U2", "Upgrade\nspeed", isU2Selected, isU2Hovered, app);
        drawButton(650, 350, "U3", "Upgrade\ndamage", isU3Selected, isU3Hovered, app);
        drawButton(650, 410, "M", "Mana pool\ncost: " + (int) this.mana.getManaPoolSpellCost(), isMSelected, isMHovered, app);
        drawButton(650, 470, "B", "Buy Potion\ncost: 200", isBuyFreezePotionSelected, isBuyFreezePotionHovered, app);
        drawButton(650, 530, "U", "Use Potion\nquantity: " + this.potions.getQuantity(), isUseFreezePotionSelected, isUseFreezePotionHovered, app);
        drawTowerCost(app);
        drawManaCost(app);
    }

    /**
     * Draws a button with specified properties on the screen.
     *
     * @param x The x-coordinate where the button should be drawn.
     * @param y The y-coordinate where the button should be drawn.
     * @param buttonText The text displayed on the button.
     * @param description The description text displayed next to the button.
     * @param isSelected A flag indicating if the button is currently selected.
     * @param isHovered A flag indicating if the mouse is currently hovering over the button.
     * @param app The PApplet object used for drawing the button.
     */
    private void drawButton(int x, int y, String buttonText, String description, boolean isSelected, boolean isHovered, PApplet app) {
        app.pushStyle();
    
        app.strokeWeight(3);  // Set the thickness of the edge lines
        app.textAlign(PConstants.LEFT, PConstants.TOP);
    
        if (isSelected) {
            app.fill(255, 255, 0);
        } else if (isHovered){
            app.fill(128);
        }else {
            app.noFill();
        }
    
        app.rect(x, y, 45, 45);
    
        app.fill(0);
        app.textSize(24);
        app.textFont(app.createFont("", 26, true));
        app.text(buttonText, x + 5, y + 5);
    
        app.textSize(12);
        app.textFont(app.createFont("", 12, true));
        app.text(description, x + 50, y + 5);
    
        app.popStyle();
    }

    /**
     * Draws the tower cost on the screen if the build tower button is hovered.
     * @param app The PApplet object to be used for drawing.
     * @return True if the cost is drawn, false otherwise.
     */
    public boolean drawTowerCost(PApplet app) {
        if (isBuildTowerHovered) {
            int cost = initialTowerCost;
            if (isU1Selected) cost += 20;
            if (isU2Selected) cost += 20;
            if (isU3Selected) cost += 20;
    
            int x = 615;
            int y = 170; 
    
            String costText = "Cost: " + cost;
    
            app.pushStyle();

            app.textSize(12);

            int textWidth = (int) app.textWidth(costText) + 10;
    
            x -= textWidth - 20;

            app.fill(255);
            app.rect(x, y, textWidth, 20 );


            app.fill(0);
            app.text(costText, x + 5, y + 14);
            app.popStyle();
            
            return true;
        }
        return false;
    }

    /**
     * Draws the mana cost on the screen if the mana button is hovered.
     * @param app The PApplet object to be used for drawing.
     * @return True if the cost is drawn, false otherwise.
     */
    public boolean drawManaCost(PApplet app){
        if (isMHovered){
            int cost = (int) this.mana.getManaPoolSpellCost();

            int x = 615;
            int y = 410;

            String costText = "Cost: " + cost;

            app.pushStyle();
            app.textSize(12);
            int textWidth = (int) app.textWidth(costText) + 10;
            x -= textWidth - 20;
            app.fill(255);

            app.rect(x, y, textWidth, 20 );
    
            app.fill(0);
            app.text(costText, x + 5, y + 14);
            app.popStyle();
            return true;
        }
        return false;
    }

    /**
     * Retrieves the cost of selected upgrades for a given tower.
     * @param tower The tower whose upgrades cost is to be retrieved.
     * @return A map containing the names and costs of selected upgrades.
     */
    public Map<String, Integer> getSelectedUpgradesCost(Tower tower) {
        Map<String, Integer> selectedUpgradesCost = new LinkedHashMap<>();
        if (getIsU1Selected()) {  
            selectedUpgradesCost.put("Range", 20 + tower.getRangeLevel() * 10);
        }
        if (getIsU2Selected()) {
            selectedUpgradesCost.put("Speed", 20 + tower.getSpeedLevel() * 10);
        }
        if (getIsU3Selected()) {
            selectedUpgradesCost.put("Damage", 20 + tower.getDamageLevel() * 10);
        }
        return selectedUpgradesCost;
    }
}
