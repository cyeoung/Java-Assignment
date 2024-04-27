package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

public class Fireball {
    public float x;
    public float y;

    private Monster target;
    private float speed = 5;
    
    private PImage fireball;

    public Fireball(float x, float y, Monster target, PImage fireball) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.fireball = fireball;
    }

    public Monster getTarget(){
        return this.target;
    }

    public void move() {
        float dx = target.currentRow + 10 - x;
        float dy = target.currentCol + 10 - y;
        float distance = PApplet.dist(x, y, target.currentRow + 10, target.currentCol + 10);

        if (distance <= speed) {
            return;
        }

        x += dx / distance * speed;
        y += dy / distance * speed;
    }

    public boolean hasHitTarget() {
        float distance = PApplet.dist(x, y, target.currentRow + 10, target.currentCol + 10);
        return distance <= speed;
    }

    public void draw(PApplet app) {
        app.image(this.fireball, this.x, this.y);
    }
}
