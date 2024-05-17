
package Tanks;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.List;

public class Tree {
    private PApplet parent;
    private float x, y;
    private List<Float> terrainHeights;
    private PImage image;
    private int cellSize, cellHeight;
    private static final float FALL_RATE = 120.0f / 30.0f;

    public Tree(PApplet parent, float x, float y, List<Float> terrainHeights, PImage image, int cellSize, int cellHeight) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.terrainHeights = terrainHeights;
        this.image = image;
        this.cellSize = cellSize;
        this.cellHeight = cellHeight;
    }

    public void draw() {
        // Draw tree
        parent.image(image, x, y, cellSize, cellHeight);
    }
}

