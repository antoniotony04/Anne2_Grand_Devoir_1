public class Enemy extends Character {
    public Enemy(String name, int attack, int defense, int health) {
        super(name, attack, defense, health);
    }

    @Override
    public void damage(int amount) {
        
    }

    public String dropLoot() {
        return "Random Loot";
    }
}
