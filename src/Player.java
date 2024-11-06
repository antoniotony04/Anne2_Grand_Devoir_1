// Player.java
public class Player extends Character {
    private int wood;
    private int stone;
    private int food;

    public Player(String name, int attack, int defense, int health) {
        super(name, attack, defense, health);
    }

    @Override
    public void damage(int amount) {
        
    }

    public void collectWood(int amount) {
        wood += amount;
    }

    public void collectStone(int amount) {
        stone += amount;
    }

    public void collectFood(int amount) {
        food += amount;
    }

    // Getters and Setters...
}
