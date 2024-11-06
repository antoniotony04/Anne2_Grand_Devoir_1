public abstract class Character {
    protected String name;
    protected int attack;
    protected int defense;
    protected int health;
    protected boolean isAlive = true;

    public Character(String name, int attack, int defense, int health) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
    }

    public abstract void damage(int amount);

    public void takeDamage(int amount) {
        if (amount > 0) {
            health -= amount;
            if (health <= 0) {
                health = 0;
                die();
            }
        }
    }


    public void die() {
        isAlive = false;
    }
}

