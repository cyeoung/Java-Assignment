package Tanks;

import processing.core.PApplet;

public class Tank {
    private char playerChar;
    private String playerColor;
    private int x, y;
    private float turretAngle;
    private int health;
    private int fuel;
    private int parachutes;

    public Tank(char playerChar, String playerColor) {
        this.playerChar = playerChar;
        this.playerColor = playerColor;
        // Initialize the tank's position, angle, health, fuel, and parachutes
    }

    public void drawTank(PApplet app) {
        int r = Integer.parseInt(playerColor.substring(0, 2), 16);
        int g = Integer.parseInt(playerColor.substring(2, 4), 16);
        int b = Integer.parseInt(playerColor.substring(4, 6), 16);
        app.fill(app.color(r, g, b));
        app.text(playerChar, x, y);
    }


    public void updateTank(float deltaTime) {
        // Implement the logic to update the tank's position, angle, and other properties
        // based on user input and game mechanics
    }
}