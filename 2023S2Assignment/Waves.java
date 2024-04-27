package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;


import java.util.*;


/**
 * Represents the waves of monsters in the game, including handling, updating, and drawing waves and monsters.
 */
public class Waves {
    private Mana mana;
    private List<Wave> waveList = new ArrayList<>();
    public int WaveIndex = -1;
    private float timeToNextWave;
    private char[][] layout;
    public final double frameTime = 1.0/60;
    public double spawnInterval;
    public List<List<int[]>> monstersPosition;
    public boolean finished = false;
    private boolean won = false;
    private PImage gremlin;
    private PImage gremlin_1;
    private PImage gremlin_2;
    private PImage gremlin_3;
    private PImage gremlin_4;
    private PImage gremlin_5;
    private PImage beetle;
    private PImage worm;

    private List<Monster> activeMonsters = new ArrayList<>();

    /**
     * Constructs a new Waves instance with the provided parameters.
     *
     * @param wavesArray The JSON array containing wave data.
     * @param layout The layout of the game map.
     * @param mana The Mana object used in the game.
     * @param gremlin The image for the gremlin monster.
     * @param gremlin_1 The first alternative image for the gremlin monster.
     * @param gremlin_2 The second alternative image for the gremlin monster.
     * @param gremlin_3 The third alternative image for the gremlin monster.
     * @param gremlin_4 The fourth alternative image for the gremlin monster.
     * @param gremlin_5 The fifth alternative image for the gremlin monster.
     * @param beetle The image for the beetle monster.
     * @param worm The image for the worm monster.
     */
    public Waves(JSONArray wavesArray,char[][] layout,Mana mana, PImage gremlin, PImage gremlin_1, PImage gremlin_2, PImage gremlin_3, PImage gremlin_4, PImage gremlin_5, PImage beetle, PImage worm) {
        this.mana = mana;
        this.gremlin = gremlin;
        this.gremlin_1 = gremlin_1;
        this.gremlin_2 = gremlin_2;
        this.gremlin_3 = gremlin_3;
        this.gremlin_4 = gremlin_4;
        this.gremlin_5 = gremlin_5;
        this.beetle = beetle;
        this.worm = worm;
        this.layout = layout;
        for (int i = 0; i < wavesArray.size(); i++) {
            JSONObject waveObj = wavesArray.getJSONObject(i);
            this.waveList.add(new Wave(waveObj,this.layout, this.mana, this.gremlin, this.gremlin_1, this.gremlin_2,this.gremlin_3, this.gremlin_4, this.gremlin_5, this.beetle, this.worm));
        }
    }

    /**
     * @return The list of Wave objects.
     */
    public List<Wave> getWaveList(){
        return this.waveList;
    }

    /**
     * @return The current wave index.
     */
    public int getWaveIndex(){
        return this.WaveIndex;
    }

    /**
     * @return The remaining time to the next wave.
     */
    public float getTimeToNextWave(){
        return this.timeToNextWave;
    }

    /**
     * @return The list of currently active monsters.
     */
    public List<Monster> getActiveMonsters(){
        return this.activeMonsters;
    }

    /**
     * Checks if all waves are completed.
     *
     * @return True if all waves are completed, false otherwise.
     */
    public boolean areAllWavesCompleted() {
        return WaveIndex == waveList.size() && finished;
    }
    
     /**
     * Checks if all monsters are defeated.
     *
     * @return True if all monsters are defeated, false otherwise.
     */
    public boolean areAllMonstersDefeated() {
        return activeMonsters.isEmpty();
    }
    /**
     * @return True if the player won, false otherwise.
     */
    public boolean getWon(){
        return this.won;
    }
    /**
     * Handles the progression of waves, including the timing and index updating.
     */
    public void waveHandle(){
        if (this.timeToNextWave > 0) {
            this.timeToNextWave -= frameTime;
            if (this.timeToNextWave <= 0){
                WaveIndex++;
            }
        } else {
            if (WaveIndex < waveList.size()) {
                startNewWave();
            }
            else if (WaveIndex == waveList.size() && activeMonsters.isEmpty()) {
                finished = true;
            }
        }
    }

    /**
     * Determines if the player has won the game based on the game state.
     */
    public void judgeWon(){
        if (finished && activeMonsters.isEmpty() && this.mana.getCurrentMana() >= 0) {
            this.won = true;
        }
    }

    /**
     * Updates the state of the active monsters, including their removal if defeated.
     */
    public void monsterUpdate(){
        Iterator<Monster> iterator = activeMonsters.iterator();
        while (iterator.hasNext()) {
            Monster monster = iterator.next();
            if (monster.getdeathFrameCount() >= 17) {
                this.mana.setCurrentMana((float) monster.getManaGainedOnKill() * this.mana.getManaGainedMultiplier());
                iterator.remove();
            } else {
                monster.update();
            }
        }
    }

    /**
     * Updates the current wave state and spawns new monsters if applicable.
     *
     * @param app The PApplet object used for rendering.
     */
    public void waveUpdate(PApplet app){
        if (WaveIndex >= 0 && WaveIndex < waveList.size()) {
            Wave currentWave = waveList.get(WaveIndex);
            Monster newMonster = currentWave.update(frameTime);
            if (newMonster != null){
                activeMonsters.add(newMonster);
            }
        }
    }

    /**
     * Updates the entire game state including waves and monsters.
     *
     * @param app The PApplet object used for rendering.
     */
    public void update(PApplet app) {
        this.waveHandle();
        this.waveUpdate(app);
        this.judgeWon();
        this.monsterUpdate();
    }

    /**
     * Starts a new wave based on the current game state and wave index.
     */
    public void startNewWave() {
        if (WaveIndex <= waveList.size()) {
            if (WaveIndex == -1) {
                Wave nextWave = waveList.get(WaveIndex + 1);
                this.timeToNextWave = nextWave.getPreWavePause();
            } else {
                Wave currentWave = waveList.get(WaveIndex);
                if (WaveIndex < waveList.size() - 1) {
                    Wave nextWave = waveList.get(WaveIndex + 1);
                    this.timeToNextWave = currentWave.getDuration() + nextWave.getPreWavePause();
                } else if (WaveIndex == waveList.size() - 1) {
                    this.timeToNextWave = currentWave.getDuration();
                }
            }
        }
    }
    
    /**
     * Renders the wave and monster elements on the screen.
     *
     * @param app The PApplet object used for rendering.
     */
    public void draw(PApplet app){
        if (WaveIndex < waveList.size() - 1 && this.timeToNextWave > 0) {
            app.textSize(24);
            app.fill(0);
            int remainingTime = (int) Math.floor(timeToNextWave);
            app.text("Wave " + (WaveIndex + 2) + " starts: " + remainingTime,10, 30);
        }
        for (Monster monster : activeMonsters) {
                monster.draw(app);
        }
    }
}
