import processing.core.PApplet;
import processing.core.PImage;

public class Tree {
    private int x, y;
    private PImage treeImage;

    public Tree(int x, int y, String treesFile) {
        this.x = x;
        this.y = y;
        this.treeImage = loadImage(treesFile);
    }

    public static PImage loadImage(String filename) {
        return PApplet.loadImage(filename);
    }

    public void drawTree(PApplet app) {
        app.image(treeImage, x, y);
    }
}