public class Item {
    public String name;
    public int type; //1-Arma 2-Casca 3-Armura
    public int defenseValue;
    public int attackValue;

    public Item(String name, int type, int defenseValue, int attackValue) {
        this.name = name;
        this.type = type;
        this.defenseValue = defenseValue;
        this.attackValue = attackValue;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", defenseValue=" + defenseValue +
                ", attackValue=" + attackValue +
                '}';
    }
}
