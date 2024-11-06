public abstract class Character {
    public String name;
    public int attack;
    public int defense;
    public int health;
    public boolean isAlive = true;

    public Character(String name, int attack, int defense, int health) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
    }

    public abstract void damage(int amount);

    public abstract void takeDamage(int amount);

    public void die() {
        isAlive = false;
    }
}

