package Tanks;

public class Level {
    private String layout;
    private String background;
    private String foregroundColor;
    private String trees;

    public Level(String layout, String background, String foregroundColor, String trees) {
        this.layout = layout;
        this.background = background;
        this.foregroundColor = foregroundColor;
        this.trees = trees;
    }

    // Getters
    public String getLayout() {
        return layout;
    }

    public String getBackground() {
        return background;
    }

    public String getForegroundColor() {
        return foregroundColor;
    }

    public String getTrees() {
        return trees;
    }

    // Setters
    public void setLayout(String layout) {
        this.layout = layout;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setForegroundColor(String foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public void setTrees(String trees) {
        this.trees = trees;
    }
}

