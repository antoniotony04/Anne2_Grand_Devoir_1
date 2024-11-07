import java.util.ArrayList;

public class Player extends Character {
    public int wood;
    public int stone;
    public int food;
    public Item arma = new Item("Pumn", 1, 0, 0);
    public Item casca = new Item("Nimic", 2, 0, 0);
    public Item armura = new Item("Nimic", 3, 0, 0);
    public ArrayList<Item> inventar = new ArrayList<>();

    public ArrayList<Item> getInventar() {
        return inventar;
    }

    public Player(String name, int attack, int defense, int health) {
        super(name, attack, defense, health);
        this.wood = 0;
        this.stone = 0;
        this.food = 0;
    }

    public void setFood(int food) {
        this.food = food;
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

    public int getWood() {
        return wood;
    }

    public int getStone() {
        return stone;
    }

    public int getFood() {
        return food;
    }

    public Item getArma() {
        return arma;
    }

    public void setArma(Item arma) {
        this.arma = arma;
        attack += arma.getAttackValue();
    }

    public Item getCasca() {
        return casca;
    }

    public void setCasca(Item casca) {
        this.casca = casca;
        defense += casca.getDefenseValue();
    }

    public Item getArmura() {
        return armura;
    }

    public void setArmura(Item armura) {
        this.armura = armura;
        defense += armura.getDefenseValue();
    }

    public void addItemToInventory(Item item) {
        inventar.add(item);
        System.out.println("Ai adaugat " + item.name + " in inventar.");
    }

    public void echipeazaItem(Item item) {
        if (item.getType() == 2) {
            this.setCasca(item);
        } else if (item.getType() == 3) {
            this.setArmura(item);
        } else if (item.getType() == 1) {
            this.setArma(item);
        }

        System.out.println("Ai echipat: " + item.toString());
    }

    @Override
    public void damage(int amount) {
        int effectiveDamage = amount - defense;
        if (effectiveDamage > 0) {
            health -= effectiveDamage;
            System.out.println(name + " a primit " + effectiveDamage + " daune. Viata ramasa: " + health);
        } else {
            System.out.println(name + " nu a fost ranit datorita apararii.");
        }
        if (health <= 0) {
            health = 0;
            die();
        }
    }

    @Override
    public void die() {
        isAlive = false;
        System.out.println(name + " a murit!");
    }

    @Override
    public void takeDamage(int amount) {
        damage(amount); // Delegate to the damage method
    }
}
