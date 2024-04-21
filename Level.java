package Tanks;

import processing.core.PApplet;

import java.util.ArrayList;
import Tanks.Tree;
import processing.core.PImage;

public class Level {
    private Terrain terrain;
    private ArrayList<Tank> tanks;
    private ArrayList<Tree> trees;
    private String backgroundFile;

    private ArrayList<Tree> initTrees(String treesFile) {
        ArrayList<Tree> trees = new ArrayList<>();
        // Implement the logic to parse the layout file and create the trees
        // For example:
        trees.add(new Tree(100, 200, treesFile));
        return trees;
    }

    public Level(Terrain terrain, ArrayList<Tank> tanks, String backgroundFile, String treesFile) {
        this.terrain = terrain;
        this.tanks = tanks;
        this.trees = initTrees(treesFile);
        this.backgroundFile = backgroundFile;
    }

    public void drawLevel(PApplet app) {
        // Draw the background
        PImage background = app.loadImage(backgroundFile);
        app.image(background, 0, 0);

        // Draw the terrain
        terrain.drawTerrain(app);

        // Draw the tanks
        for (Tank tank : tanks) {
            tank.drawTank(app);
        }

        // Draw the trees
        for (Tree tree : trees) {
            tree.drawTree(app);
        }
    }

    public void updateLevel(float deltaTime) {
        // Update the tanks
        for (Tank tank : tanks) {
            tank.updateTank(deltaTime);
        }

        // Update other game objects as needed
    }
}