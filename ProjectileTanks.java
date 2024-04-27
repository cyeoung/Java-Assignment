package Tanks;

import processing.core.PApplet;
import processing.core.PVector;

public class Projectile {
    private float x;
    private float y;
    private PVector velocity;
    private PVector gravity = new PVector(0, 3.6f); // Constant gravity acceleration of 3.6 pixels/second^2
    private int powerLevel;
    private int radius = 30; // Default explosion radius

    public Projectile(float x, float y, float angle, int powerLevel) {
        this.x = x;
        this.y = y;
        this.powerLevel = powerLevel;
        this.velocity = calculateInitialVelocity(angle, powerLevel);
    }

    private PVector calculateInitialVelocity(float angle, int powerLevel) {
        float magnitude = map(powerLevel, 0, 100, 1, 9); // Map power level to velocity magnitude
        float radians = PApplet.radians(angle);
        return new PVector(magnitude * PApplet.cos(radians), magnitude * PApplet.sin(radians));
    }

    public void update() {
        velocity.add(gravity); // Apply gravity acceleration
        x += velocity.x;
        y += velocity.y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    private float map(float value, float start1, float stop1, float start2, float stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    // public void draw(PApplet app) {
    //     app.image(this.fireball, this.x, this.y);
    // }
}