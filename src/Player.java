public class Player extends Character {
    private int wood;
    private int stone;
    private int food;

    public Player(String name, int attack, int defense, int health) {
        super(name, attack, defense, health);
        this.wood = 0;
        this.stone = 0;
        this.food = 0;
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

    @Override
    public void damage(int amount) {
        health -= Math.max(0, amount - defense);
        if (health <= 0) {
            die();
        }
    }
}
