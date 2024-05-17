package Tanks;
import processing.core.PApplet;
import java.lang.Math;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class Projectile {
    private PApplet parent;
    private float x, y;
    private float vx, vy;
    private final float MAX_SPEED = 540;

    public Projectile(PApplet parent, float startX, float startY, float angle, float speed) {
        this.parent = parent;
        this.x = startX;
        this.y = startY;
        this.vx = (float)(speed * cos(angle * (float)Math.PI / 180));
        this.vy = (float)(speed * sin(angle * (float)Math.PI / 180));
    }

    public void update() {
        x += vx;
        y += vy;
        vy += 3.6; // Apply acceleration due to gravity
    }

    public void display() {
        parent.fill(0);
        parent.ellipse(x - 30, y - 25, 10, 10);
    }

    public boolean isOffScreen() {
        return x < 0 || x > parent.width || y < 0 || y > parent.height;
    }
}