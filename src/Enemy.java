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

    @Override
    public void takeDamage(int amount) {
        if (amount > 0) {
            health -= amount;
            if (health <= 0) {
                health = 0;
                die();
            }
        }
    }
}
