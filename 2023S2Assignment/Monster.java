package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;


/**
 * Represents a monster in the game with specific attributes and behaviours,
 * including the ability to move, take damage, and be displayed on the screen.
 */
public class Monster {
    private Mana mana;
    private String type;
    private float hp;
    private float maxHp;
    public float speed;
    private double armour;
    private int manaGainedOnKill;
    private int quantity;

    public int x;
    public int y;
    public float currentRow;
    public float currentCol;
    private float spawnRow;
    private float spawnCol;
    private List<Position> path;
    public int currentPathIndex = 0;

    public boolean isSpawned = false;

    public boolean isDead = false;
    public int deathFrameCount = 0;
    
    private PImage gremlin;
    private PImage gremlin_1;
    private PImage gremlin_2;
    private PImage gremlin_3;
    private PImage gremlin_4;
    private PImage gremlin_5;
    private PImage beetle;
    private PImage worm;

    private char[][] layout;

    public int frozenTimeRemaining; // use for extension

    public int drawState;// use for test

    /**
     * Primary constructor to initialize a new monster with given attributes.
     *
     * @param type             The type of the monster (e.g., "gremlin", "beetle", "worm").
     * @param hp               The initial health points of the monster.
     * @param speed            The speed at which the monster moves.
     * @param armour           The monster's armour value to reduce incoming damage.
     * @param manaGainedOnKill The amount of mana gained when the monster is killed.
     * @param quantity         The number of monsters to be created.
     * @param mana             The mana object associated with the monster.
     * @param gremlin          The PImage object for gremlin's appearance.
     * @param gremlin_1        The PImage object for gremlin's first death animation frame.
     * @param gremlin_2        The PImage object for gremlin's second death animation frame.
     * @param gremlin_3        The PImage object for gremlin's third death animation frame.
     * @param gremlin_4        The PImage object for gremlin's fourth death animation frame.
     * @param gremlin_5        The PImage object for gremlin's fifth death animation frame.
     * @param beetle           The PImage object for beetle's appearance.
     * @param worm             The PImage object for worm's appearance.
     * @param layout           The layout of the game grid.
     */
    public Monster(String type, float hp, float speed, double armour, int manaGainedOnKill, int quantity, Mana mana, PImage gremlin, PImage gremlin_1, PImage gremlin_2, PImage gremlin_3, PImage gremlin_4, PImage gremlin_5, PImage beetle, PImage worm, char[][] layout) {
        this.mana = mana;
        this.gremlin = gremlin;
        this.gremlin_1 = gremlin_1;
        this.gremlin_2 = gremlin_2;
        this.gremlin_3 = gremlin_3;
        this.gremlin_4 = gremlin_4;
        this.gremlin_5 = gremlin_5;
        this.beetle = beetle;
        this.worm = worm;
        this.type = type;
        this.hp = hp;
        this.maxHp = hp;
        this.speed = speed;
        this.armour = armour;
        this.manaGainedOnKill = manaGainedOnKill;
        this.quantity = quantity;
        this.layout = layout;
    }


    /**
     * Secondary constructor to create a new monster based on an existing one.
     *
     * @param monster The existing monster to copy attributes from.
     * @param gremlin          The PImage object for gremlin's appearance.
     * @param gremlin_1        The PImage object for gremlin's first death animation frame.
     * @param gremlin_2        The PImage object for gremlin's second death animation frame.
     * @param gremlin_3        The PImage object for gremlin's third death animation frame.
     * @param gremlin_4        The PImage object for gremlin's fourth death animation frame.
     * @param gremlin_5        The PImage object for gremlin's fifth death animation frame.
     * @param beetle           The PImage object for beetle's appearance.
     * @param worm             The PImage object for worm's appearance.
     * @param layout           The layout of the game grid.
     */
    public Monster(Monster monster,PImage gremlin, PImage gremlin_1, PImage gremlin_2, PImage gremlin_3, PImage gremlin_4, PImage gremlin_5, PImage beetle, PImage worm, char[][] layout) {
        this.gremlin = gremlin;
        this.gremlin_1 = gremlin_1;
        this.gremlin_2 = gremlin_2;
        this.gremlin_3 = gremlin_3;
        this.gremlin_4 = gremlin_4;
        this.gremlin_5 = gremlin_5;
        this.beetle = beetle;
        this.worm = worm;
        this.mana = monster.mana;
        this.type = monster.type;
        this.hp = monster.hp;
        this.maxHp = monster.hp;
        this.speed = monster.speed;
        this.armour = monster.armour;
        this.manaGainedOnKill = monster.manaGainedOnKill;
        this.quantity = monster.quantity;
        this.layout = layout;
    }

    /**
     * @return The quantity of this type of monster.
     */
    public int getQuantity(){
        return this.quantity;
    }
    
    /**
     * @return The type identifier of this monster.
     */
    public String getType(){
        return this.type;
    }

    /**
     * @return The current health points of this monster.
     */
    public float gethp(){
        return this.hp;
    }

    /**
     * This can be used for animations or timing events post-death.
     *
     * @return The count of frames since this monster died.
     */
    public int getdeathFrameCount(){
        return this.deathFrameCount;
    }
    /**
     * @return The amount of mana gained when this monster is killed.
     */
    public int getManaGainedOnKill(){
        return this.manaGainedOnKill;
    }

    /**
     * Sets the quantity of this type of monster.
     *
     * @param quantity The new quantity of this type of monster.
     */
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    /**
     * Sets the position of the monster on the game grid.
     *
     * @param x The x-coordinate of the monster's position.
     * @param y The y-coordinate of the monster's position.
     */
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
        this.path = findPath(this.layout, x, y);
    }

     /**
     * Causes the monster to take damage and updates its health points.
     * The monster dies if its health points reach 0 or below.
     *
     * @param damage The amount of damage the monster takes.
     */
    public void takeDamage(int damage){
        this.hp -= damage;
        if (this.hp <= 0 && !this.isDead){
            die();
        }
    }
    /**
     * Reduces the monster's health points by a specified amount of damage, adjusted by the monster's armour. 
     * If the monster's health points reach 0 or below, it dies.
     *
     * @param damage The amount of damage the monster is supposed to take before considering the armour.
     */
    public void setHp(float damage){
        this.hp -= damage * this.armour;
        if (this.hp <= 0 && !this.isDead){
            die();
        }
    }

    /**
     *Sets the monster's state to dead and sets the death frame count. This method should be called
     * when the monster's health points reach 0 or below.
     */
    public void die() {
        this.isDead = true;
        this.deathFrameCount = 0;
    }

    /**
     * Freezes the monster for a specified amount of seconds, rendering it unable to move or take actions.
     *
     * @param seconds The duration for which the monster is frozen.
     */
    public void freeze(int seconds) {
        this.frozenTimeRemaining = seconds * 60; // use for extension
    }
    /**
     * Determines and sets the spawn position of the monster if it has not been spawned yet.
     * The spawn position is determined based on the monster's initial x and y coordinates.
     */
    public void SpawnPosition(){
        if (!isSpawned) {
                if (this.x == 0){
                    this.currentRow = (this.x - 1) * App.CELLSIZE + 10;
                    this.currentCol = this.y * App.CELLSIZE + App.TOPBAR + 5;
                }else if(this.y == 0){
                    this.currentRow = this.x * App.CELLSIZE + 5;
                    this.currentCol = (this.y - 1) * App.CELLSIZE + App.TOPBAR + 10;
                }else if(this.x == 19){
                    this.currentRow = (this.x + 1) * App.CELLSIZE;
                    this.currentCol = this.y * App.CELLSIZE + App.TOPBAR + 5;
                }else if(this.y == 19){
                    this.currentRow = this.x * App.CELLSIZE + 5;
                    this.currentCol = (this.y + 1) * App.CELLSIZE + App.TOPBAR;
                }
                this.spawnRow = this.currentRow;
                this.spawnCol = this.currentCol;
                isSpawned = true;
            }
    }

    /**
     * Updates the monster's state and position on each frame of the game. This includes managing
     * the monster's frozen state, spawning position, death state, and movement.
     */
    public void update() {
        if (frozenTimeRemaining > 0) { // use for extension
            frozenTimeRemaining--;
            return;
        }
        SpawnPosition();
        if(!this.isDead) {
            move();
        }
        respawn();
    }

    /**
     * Respawns the monster at its initial position if it has reached the Wizard House with positive HP.
     * It also checks if there is enough mana to banish the monster and acts accordingly.
     */
    public void respawn() {
        // Check if the monster has reached the Wizard House and has positive HP
        if (this.currentPathIndex >= this.path.size() && this.hp > 0) {
            // Respawn the monster at its initial position
            this.currentPathIndex = 0;
            this.currentRow = this.spawnRow; 
            this.currentCol = this.spawnCol;
            // Check if there is enough mana to banish the monster
            if(this.mana != null){
                if (this.mana.getCurrentMana() >= this.hp) {
                    this.mana.setCurrentMana(-this.hp);
                }else{
                    this.mana.setCurrentMana(-this.hp);
                    this.mana.lose();
                }
            }
        }
    }

    /**
     * Identifies and returns a random spawn point from the available edge positions on the game map layout.
     *
     * @param layout A 2D array representing the game map, where 'X' denotes a path.
     * @return An array containing the coordinates of a random spawn point, or null if no suitable positions are found.
     */
    public int[] spawn(char[][] layout) {
        List<int[]> edgePositions = new ArrayList<>();


        for (int col = 0; col < 20; col++) {
            if (layout[0][col] == 'X') {
                edgePositions.add(new int[]{col, 0});
            }
            if (layout[20 - 1][col] == 'X') {
                edgePositions.add(new int[]{col, 19});
            }
        }

        for (int row = 0; row < 20; row++) {
            if (layout[row][0] == 'X') {
                edgePositions.add(new int[]{0,row});
            }
            if (layout[row][20 - 1] == 'X') {
                edgePositions.add(new int[]{19,row});
            }
        }

        if (!edgePositions.isEmpty()) {
            Random rand = new Random();
            int[] pos = edgePositions.get(rand.nextInt(edgePositions.size()));
            return pos;
        }
        return null;
    }

    /**
     * Represents a position on the game map, including coordinates, previous position, and visited status.
     */
    class Position {
        int x, y;
        Position prev;
        boolean[][] visited;
        
        /**
         * Constructs a new Position instance with specified attributes.
         *
         * @param x The x-coordinate of this position on the game map.
         * @param y The y-coordinate of this position on the game map.
         * @param prev The previous position leading to this position, used for tracking the path.
         * @param visited A 2D array indicating the visited status of positions on the game map.
         */
        Position(int x, int y, Position prev, boolean[][] visited) {
            this.x = x;
            this.y = y;
            this.prev = prev;
            this.visited = visited;
        }
    }
    /**
     * Finds a path from the starting position to the wizard house on the given game map layout.
     *
     * @param layout A 2D array representing the game map.
     * @param startY The starting y-coordinate on the game map.
     * @param startX The starting x-coordinate on the game map.
     * @return A list of Position objects representing the path from start to the wizard house, or null if no path is found.
     */
    public List<Position> findPath(char[][] layout, int startY, int startX) {
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    
        List<Position> list = new ArrayList<>();
        list.add(new Position(startX, startY, null, new boolean[20][20]));
        list.get(0).visited[startX][startY] = true;
    
        List<Position> ends = new ArrayList<>();
        while (!list.isEmpty()) {
            Position curr = list.remove(0);
    
            if (layout[curr.x][curr.y] == 'W') {
                ends.add(curr);
                continue;
            }
    
            for (int[] dir : dirs) {
                int newX = curr.x + dir[0];
                int newY = curr.y + dir[1];
    
                if (newX >= 0 && newX < 20 && newY >= 0 && newY < 20 && !curr.visited[newX][newY] && (layout[newX][newY] == 'X' || layout[newX][newY] == 'W')) {
                    boolean[][] newVisited = copyVisited(curr.visited);
                    newVisited[newX][newY] = true;
                    list.add(new Position(newX, newY, curr, newVisited));
                }
            }
        }
    
        if (ends.isEmpty()) return null;
    
        int randomIndex = (int) (Math.random() * ends.size());
        Position randomEnd = ends.get(randomIndex);
    
        List<Position> path = new ArrayList<>();
        while (randomEnd != null) {
            path.add(0, randomEnd);
            randomEnd = randomEnd.prev;
        }
        return path;
    }
    /**
     * Creates a deep copy of the given 2D boolean array representing visited positions on the game map.
     *
     * @param original The original 2D boolean array to be copied.
     * @return A new 2D boolean array containing the copied values of the original array.
     */
    private boolean[][] copyVisited(boolean[][] original) {
        boolean[][] copy = new boolean[20][20];
        for (int i = 0; i < 20; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 20);
        }
        return copy;
    }

    /**
     * Moves the monster along the predefined path based on its speed, updating its current position.
     * The monster moves towards each point in the path until it reaches it, then proceeds to the next point.
     */
    public void move() {
        if (this.currentPathIndex >= this.path.size()){
            return;
        }
    
        float remainingSpeed = this.speed;
    
        while (remainingSpeed > 0 && this.currentPathIndex < this.path.size()) {
            Position targetPosition = this.path.get(currentPathIndex);
    
            float targetXPixel = targetPosition.y * App.CELLSIZE + 5;
            float targetYPixel = targetPosition.x * App.CELLSIZE + App.TOPBAR + 5;
            if (currentPathIndex == 0){
            }
            float dx = targetXPixel - this.currentRow;
            float dy = targetYPixel - this.currentCol;
            float distance = PApplet.dist(this.currentRow, this.currentCol, targetXPixel, targetYPixel);
    
            if (distance <= remainingSpeed) {
                this.currentRow = targetXPixel;
                this.currentCol = targetYPixel;
                remainingSpeed -= distance;
                currentPathIndex++;
            } else {
                dx /= distance;
                dy /= distance;
                this.currentRow += dx * remainingSpeed;
                this.currentCol += dy * remainingSpeed;
                remainingSpeed = 0;
            }
        }
    }
    
    
    /**
     * Draws the monster on the game map, updating its visual representation based on its type and state.
     * This method handles the visual representation of different monster types and their states (alive or dead).
     * It also displays the monster's health bar and handles the animation frames for dying monsters.
     *
     * @param app The PApplet object used for drawing the monster, its health bar, and handling animations.
     */
    public void draw(PApplet app) {
        // 1. Set clipping region to the area of the map
        app.clip(0, App.TOPBAR, 20 * App.CELLSIZE, 20 * App.CELLSIZE);
        if (type.equals("gremlin")){
            if (isDead) {
                if (deathFrameCount <= 16) {
                    if (deathFrameCount <= 3){
                        app.image(this.gremlin_1, this.currentRow, this.currentCol);
                        deathFrameCount++;
                        drawState = 0;
                    }else if (deathFrameCount <= 7) {
                        app.image(this.gremlin_2, this.currentRow, this.currentCol);
                        deathFrameCount++;
                        drawState = 1;
                    }else if (deathFrameCount <= 11) {
                        app.image(this.gremlin_3, this.currentRow, this.currentCol);
                        deathFrameCount++;
                        drawState = 2;
                    }else if (deathFrameCount <= 15) {
                        app.image(this.gremlin_4, this.currentRow, this.currentCol);
                        deathFrameCount++;
                        drawState = 3;
                    }else{
                        app.image(this.gremlin_5, this.currentRow, this.currentCol);
                        deathFrameCount++;
                        drawState = 4;
                    }
                }
            }else{
                app.image(this.gremlin, this.currentRow, this.currentCol);
                app.fill(255, 0, 0); //Set fill color to red
                app.rect(this.currentRow - 5, this.currentCol - 5, 30, 3); //Draw a red rectangle above the monster

                //Draw a health bar (green) based on the monster's current HP
                app.fill(0, 255, 0); //Set fill color to green
                float healthPercentage = (float) this.hp / this.maxHp; //Calculate the percentage of health bar length
                app.rect(this.currentRow - 5, this.currentCol - 5, 30 * healthPercentage, 3); //Draw a green rectangle on the red rectangle
                drawState = 5;
            }
        }else if (type.equals("beetle")){
            if (isDead) {
                if (deathFrameCount < 17) {
                    app.image(this.beetle, this.currentRow - 4, this.currentCol - 4);
                    deathFrameCount++;
                    drawState = 6;
                }        
            }else{
                app.image(this.beetle, this.currentRow - 4, this.currentCol - 4);
                app.fill(255, 0, 0);
                app.rect(this.currentRow - 4, this.currentCol- 9, 30, 3);

                app.fill(0, 255, 0);
                float healthPercentage = (float) this.hp / this.maxHp;
                app.rect(this.currentRow - 4, this.currentCol - 9, 30 * healthPercentage, 3);
                drawState = 7;
            }
        }else if (type.equals("worm")){
            if (isDead) {
                if (deathFrameCount < 17) {
                    app.image(this.worm, this.currentRow, this.currentCol);
                    deathFrameCount++;
                    drawState = 8;
                }
            }else{
                    app.image(this.worm, this.currentRow, this.currentCol);

                    app.fill(255, 0, 0);
                    app.rect(this.currentRow -2, this.currentCol - 5, 30, 3);

                    app.fill(0, 255, 0);
                    float healthPercentage = (float) this.hp / this.maxHp;
                    app.rect(this.currentRow - 2, this.currentCol - 5, 30 * healthPercentage, 3);
                    drawState = 9;
                }
            }
            app.noClip();
        }
    }
