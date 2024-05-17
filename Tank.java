package Tanks;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;

public class Tank {
    private PApplet parent;
    public float x, y;
    public float turretAngle;
    public int health;
    public int fuel;
    public int power;
    private List<Projectile> projectiles = new ArrayList<>(); // Define projectiles
    private static final int NUM_PLAYERS = 4; // Define NUM_PLAYERS
    private static int currentPlayer = 0;
    private static final float FALL_RATE = 120.0f;
    List<Float> terrainHeights = new ArrayList<>();
    private static final float AIM_RATE = 3.0f;
    private static final float MOVE_SPEED = 60.0f;
    private static final float DESCENT_RATE_WITH_PARACHUTE = 60.0f; // Descent rate with parachute in pixels per second
    private static final int INITIAL_PARACHUTES = 3;
    public PImage parachuteImage;

    private int parachutes; // Number of parachutes available
    private boolean isInAir;

    public Tank(PApplet parent, float startX, float startY, List<Float> terrainHeights, PImage parachuteImage) {
        this.parent = parent;
        this.x = startX;
        this.y = startY;
        this.terrainHeights = terrainHeights;
        this.turretAngle = 0;
        this.health = 100;
        this.fuel = 250;
        this.power = 50;
        this.parachutes = INITIAL_PARACHUTES; // Initialize with 3 parachutes
        this.isInAir = true;
        this.parachuteImage = parachuteImage; // Set the parachute image
        updateVerticalPosition();
    }

    public int getHealth() {
        return health;
    }

    public int getScore() {
        return 0; // Return a default score of 0
    }

    public int getPower() {
        return power;
    }

    private void fallToTerrain() {
        int col = (int) (x); // Use the CELLSIZE constant

        // Ensure col is within the bounds of terrainHeights
        if (col >= 0 && col < terrainHeights.size()) {
            float terrainY = terrainHeights.get(col);
//            System.out.println("Tank position: x=" + x + ", y=" + y + ", terrainY=" + terrainY);
            if (y < terrainY) {
                float fallDistance = Math.min(FALL_RATE, terrainY - y);
                y += fallDistance; // Move downwards
//                health -= fallDistance; // Decrease health
                if (health < 0) health = 0; // Ensure health doesn't go below 0
            }
        } else {
            // Handle the case when col is out of bounds
            System.err.println("Column index out of bounds: " + col + " for x: " + x);
        }
    }

    public void moveLeft(float deltaTime) {
        if (fuel > 0) {
            float distance = MOVE_SPEED * deltaTime;
            x -= distance;
            updateVerticalPosition();
            fuel -= 1;
        }
    }

    public void moveRight(float deltaTime) {
        if (fuel > 0) {
            float distance = MOVE_SPEED * deltaTime;
            x += distance;
            updateVerticalPosition();
            fuel -= 1;
        }
    }

    public void aimUp(float deltaTime) {
        turretAngle -= AIM_RATE * deltaTime;
    }

    public void aimDown(float deltaTime) {
        turretAngle += AIM_RATE * deltaTime;
    }


    public void increasePower() {
        if (power < health) {
            power += 36;
        }
        if (power >= 100) {
            power = 100;
        }
    }

    public void decreasePower() {
        if (power > 0) {
            power -= 36;
        }
        if (power <= 0) {
            power = 0;
        }
    }

    public void fire() {
        System.out.println("Firing at angle " + turretAngle + " with power " + power);
    }

    public void fireProjectile() {
        float angle = turretAngle;
        float speed = power;
        if (speed > health) {
            speed = health;
        }
        Projectile projectile = new Projectile(parent, x, y, angle, speed);
        projectiles.add(projectile);
        fuel -= 10;
        power = 0;
        currentPlayer++;
        if (currentPlayer > NUM_PLAYERS - 1) {
            currentPlayer = 0;
        }
    }

    private void updateVerticalPosition() {
        int col = (int)(x);

        // Ensure col is within the bounds of terrainHeights
        if (col >= 0 && col < terrainHeights.size()) {
            y = terrainHeights.get(col); // Update the vertical position to match the terrain
        } else {
            System.err.println("Column index out of bounds: " + col + " for x: " + x);
        }
    }

    public void drawProjectiles() {
        for (Projectile projectile : projectiles) {
            projectile.update();
            projectile.display();
            if (projectile.isOffScreen()) {
                projectiles.remove(projectile);
            }
        }
    }

    public void displayProjectiles() {
        for (Projectile projectile : projectiles) {
            projectile.display();
        }
    }

    public void removeProjectile(Projectile projectile) {
        projectiles.remove(projectile);
    }

    // Ensure these methods are defined
    public void displayA() {
        if (isInAir && parachutes > 0) {
            parent.image(parachuteImage, x, y - 40, 30, 30); // Adjust the position and size as needed
        }

        // Update position based on terrain
        fallToTerrain();

        // Draw tank body as a trapezoid
        parent.fill(0,0,255); // Yellow for example
        parent.quad(x + 20, y,       // Top-left corner
                x - 20, y,       // Top-right corner
                x - 10, y - 15,  // Bottom-right corner
                x + 10, y - 15); // Bottom-left corner

        // Draw tank turret
        parent.fill(0); // Black turret
        parent.pushMatrix();
        parent.translate(x, y); // Move the origin to the center top of the tank
        parent.rotate(turretAngle);
        parent.rect(-2, -30, 4, 15); // Adjust turret position relative to the new origin
        parent.popMatrix();

    }

    public void update(float deltaTime) {
        if (isInAir) {
            if (parachutes > 0) {
                y += DESCENT_RATE_WITH_PARACHUTE * deltaTime;
                if (y >= terrainHeights.get(Math.round(x)) - 15) {
                    y = terrainHeights.get(Math.round(x)) - 15; // Land the tank
                    isInAir = false;
                    parachutes--; // Use a parachute
                }
            } else {
                y += FALL_RATE * deltaTime;
                health -= FALL_RATE * deltaTime; // Damage the tank
                if (y >= terrainHeights.get(Math.round(x)) - 15) {
                    y = terrainHeights.get(Math.round(x)) - 15; // Land the tank
                    isInAir = false;
                }
            }
        }
    }

    public void displayB() {
        // Update position based on terrain
        fallToTerrain();

        // Draw tank body as a trapezoid
        parent.fill(255,0,0); // Yellow for example
        parent.quad(x + 20, y,       // Top-left corner
                x - 20, y,       // Top-right corner
                x - 10, y - 15,  // Bottom-right corner
                x + 10, y - 15); // Bottom-left corner

        // Draw tank turret
        parent.fill(0); // Black turret
        parent.pushMatrix();
        parent.translate(x, y); // Move the origin to the center top of the tank
        parent.rotate(turretAngle);
        parent.rect(-2, -30, 4, 15); // Adjust turret position relative to the new origin
        parent.popMatrix();
    }

    public void displayC() {
        // Update position based on terrain
        fallToTerrain();

        // Draw tank body as a trapezoid
        parent.fill(0,255,255); // Yellow for example
        parent.quad(x + 20, y,       // Top-left corner
                x - 20, y,       // Top-right corner
                x - 10, y - 15,  // Bottom-right corner
                x + 10, y - 15); // Bottom-left corner

        // Draw tank turret
        parent.fill(0); // Black turret
        parent.pushMatrix();
        parent.translate(x, y); // Move the origin to the center top of the tank
        parent.rotate(turretAngle);
        parent.rect(-2, -30, 4, 15); // Adjust turret position relative to the new origin
        parent.popMatrix();
    }

    public void displayD() {
        // Update position based on terrain
        fallToTerrain();

        // Draw tank body as a trapezoid
        parent.fill(255,255,0); // Yellow for example
        parent.quad(x + 20, y,       // Top-left corner
                x - 20, y,       // Top-right corner
                x - 10, y - 15,  // Bottom-right corner
                x + 10, y - 15); // Bottom-left corner

        // Draw tank turret
        parent.fill(0); // Black turret
        parent.pushMatrix();
        parent.translate(x, y); // Move the origin to the center top of the tank
        parent.rotate(turretAngle);
        parent.rect(-2, -30, 4, 15); // Adjust turret position relative to the new origin
        parent.popMatrix();
    }
}

