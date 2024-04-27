package WizardTD;

import java.util.*;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * Represents a wave of monsters in the game, containing various types of monsters, their spawn logic, and related properties.
 */
public class Wave {
    private Mana mana;
    private float pre_wave_pause;
    private float duration;
    private float nextSpawnTime = 0;
    private float currentTime = 0;
    private int totalQuantity = 0;
    private List<Monster> monsters;
    private List<String> monsterType = new ArrayList<>();
    private Map<String, Integer> spawnedMonsterCount = new HashMap<>();
    private char[][] layout;
    private Map<String, List<int[]>> allSpawnPositions;
    private Map<String, Integer> spawnIndices;
    private PImage gremlin;
    private PImage gremlin_1;
    private PImage gremlin_2;
    private PImage gremlin_3;
    private PImage gremlin_4;
    private PImage gremlin_5;
    private PImage beetle;
    private PImage worm;

    /**
     * Constructs a new wave of monsters using the given parameters.
     *
     * @param waveObj The JSON object containing the wave's properties and monster's specifications.
     * @param layout The 2D character array representing the game layout.
     * @param mana The mana object associated with the wave.
     * @param gremlin The PImage object for the gremlin monster.
     * @param gremlin_1 The first PImage object for the gremlin monster's animation.
     * @param gremlin_2 The second PImage object for the gremlin monster's animation.
     * @param gremlin_3 The third PImage object for the gremlin monster's animation.
     * @param gremlin_4 The fourth PImage object for the gremlin monster's animation.
     * @param gremlin_5 The fifth PImage object for the gremlin monster's animation.
     * @param beetle The PImage object for the beetle monster.
     * @param worm The PImage object for the worm monster.
     */
    public Wave(JSONObject waveObj, char[][] layout,Mana mana, PImage gremlin, PImage gremlin_1, PImage gremlin_2, PImage gremlin_3, PImage gremlin_4, PImage gremlin_5, PImage beetle, PImage worm){
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
        this.duration = waveObj.getFloat("duration");
        this.pre_wave_pause = waveObj.getFloat("pre_wave_pause");
        JSONArray monstersArray = waveObj.getJSONArray("monsters");
        this.monsters = new ArrayList<>();
        for (int j = 0; j < monstersArray.size(); j++) {
            JSONObject monsterObj = monstersArray.getJSONObject(j);
            String type = monsterObj.getString("type");
            int hp = monsterObj.getInt("hp");
            float speed = monsterObj.getFloat("speed");
            double armour = monsterObj.getFloat("armour");
            int manaGainedOnKill = monsterObj.getInt("mana_gained_on_kill");
            int quantity = monsterObj.getInt("quantity");

            monsterType.add(type);
            totalQuantity += quantity;
            monsters.add(new Monster(type, hp, speed, armour, manaGainedOnKill, quantity,this.mana, this.gremlin, this.gremlin_1, this.gremlin_2,this.gremlin_3, this.gremlin_4, this.gremlin_5, this.beetle, this.worm,this.layout));
        }
        this.startWave(layout);
    }
    
    /**
     * Gets the duration of the pause before the wave starts.
     *
     * @return The duration of the pre-wave pause.
     */
    public float getPreWavePause() {
        return this.pre_wave_pause;
    }

     /**
     * Gets the total duration of the wave.
     *
     * @return The duration of the wave.
     */
    public float getDuration(){
        return this.duration;
    }

    public List<Monster> getMonsters(){
        return this.monsters;
    }

     /**
     * Updates the wave, potentially spawning a new monster if the conditions are met.
     *
     * @param frameTime The time of the current frame.
     * @param app The PApplet object, used for drawing.
     * @return The newly spawned monster, or null if no monster was spawned.
     */
    public Monster update(double frameTime) {
        float interval = this.duration / this.totalQuantity;
        currentTime += frameTime;
        if (currentTime >= nextSpawnTime && currentTime <= duration) {
            // spawn monsters
            Monster monster = spawnRandomMonster();
            //Calculate the time to spawn the next monster
            nextSpawnTime += interval;
            return monster;
            
        }
        return null;
    }

    public boolean startWave(char[][] layout) {
        if (layout != null){
            allSpawnPositions = spawn(layout);
            spawnIndices = new HashMap<>();
            spawnedMonsterCount = new HashMap<>();
            for (String type : allSpawnPositions.keySet()) {
                spawnIndices.put(type, 0);
                spawnedMonsterCount.put(type, 0);
            }
            return true;
        }
        return false;
    }

    /**
     * Spawns a random monster from the wave, adhering to the quantity restrictions of each monster type.
     *
     * @return The spawned monster, or null if all monsters of all types have already been spawned.
     */
    public Monster spawnRandomMonster() {
        if (monsters.isEmpty()) {
            return null;
        }
    
        int randomIndex = (int) (Math.random() * monsters.size());
        Monster monsterType = monsters.get(randomIndex);
        String type = monsterType.getType();
        int alreadySpawned = spawnedMonsterCount.getOrDefault(type, 0);
    
        if (alreadySpawned >= monsterType.getQuantity()) {
            // If a sufficient number of monsters of this type have been spawned, remove them from the monsters list
            monsters.remove(randomIndex);
            return spawnRandomMonster();  // Call recursively until finding a monster that has not spawned in sufficient numbers
        }
    
        int index = spawnIndices.get(type);
        int[] nextPosition = allSpawnPositions.get(type).get(index);
    
        // Create new monster and set position
        Monster newMonster = new Monster(monsterType, this.gremlin, this.gremlin_1, this.gremlin_2, this.gremlin_3, this.gremlin_4, this.gremlin_5, this.beetle, this.worm, this.layout);
        newMonster.setPosition(nextPosition[0], nextPosition[1]);
    
        spawnIndices.put(type, index + 1);
        spawnedMonsterCount.put(type, alreadySpawned + 1);
    
        return newMonster;
    }
    
    /**
     * Determines the spawn positions for all monsters in the wave based on the game layout.
     *
     * @param layout The 2D character array representing the game layout.
     * @return A map of monster types to lists of their spawn positions.
     */
    public Map<String, List<int[]>> spawn(char[][] layout) {
        Map<String, List<int[]>> allPositions = new HashMap<>();
    
        for (Monster monster : monsters) {
            List<int[]> positions = new ArrayList<>();
            for (int i = 0; i < monster.getQuantity(); i++) {
                int[] pos = monster.spawn(layout);
                positions.add(pos);
            }
            allPositions.put(monster.getType(), positions);
        }
    
        return allPositions;
    }
}