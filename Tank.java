public class Tank {
    private String color;
    private String turret;
    private int fuelConsumption = 1;
    private int parachute;
    private int health;
    private int power;
    private float x, float y, float rotationAngle;
    private final int INITIAL_FUEL = 250; //movement consumes 1 unit
    private final int INITIAL_HEALTH = 100; // it is also maximum health
    private final int INITIAL_POWER = 50;
    private final int INITIAL_PARACHUTES = 3; 
    /**If using a parachute, the tank descends at a rate of 60 pixels per second 
     * and sustains no damage. If no parachutes are available, 
     * it descends at a rate of 120 pixels per second and 
     * sustains damage of 1hp for each pixel of height 
     * (this is added as score to the player who fired the projectile 
     * that caused the terrain to be destroyed). */
    public Tank(String color, String turret, int fuelConsumption, int parachute, int health, int power) {
        this.color = color;
        this.turret = turret;
        this.fuelConsumption = fuelConsumption;
        this.parachute = parachute;
        this.health = health;
        this.power = power;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTurret() {
        return turret;
    }

    public void setTurret(String turret) {
        this.turret = turret;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public int getParachute() {
        return parachute;
    }

    public void setParachute(int parachute) {
        this.parachute = parachute;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }



    public void turretMovement(){

    }

    public void powerUpHealth(){

    }

    public void powerUpFuel(){

    }

    public void powerUpParachute(){

    }

    public void powerUpProjectile(){

    }
}

/**
 * public Tower(float range, 
 * float firingSpeed,
 *  int damage,
 *  int rangeLevel,
 *  int speedLevel,
 *  int damageLevel,
 *  int row, 
 * int col,
 *  PImage fireball
 * */
