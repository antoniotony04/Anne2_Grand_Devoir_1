import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    public String name;
    public int attack;
    public int defense;
    public int health;
    public boolean isAlive = true;
    public List<Item> inventar;


    public Character(String name, int attack, int defense, int health) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.inventar = new ArrayList<Item>();
    }

    public void addItemToInventory(Item item) {
        inventar.add(item);
    }

    public List<Item> getInventory() {
        return inventar;
    }

    public abstract void damage(int amount);

    public abstract void takeDamage(int amount);

    public abstract void die();
}

