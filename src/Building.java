public class Building {
    public String name;
    public int woodCost;
    public int stoneCost;

    public Building(String name, int woodCost, int stoneCost) {
        this.name = name;
        this.woodCost = woodCost;
        this.stoneCost = stoneCost;
    }

    public String getName() {
        return name;
    }

    public int getWoodCost() {
        return woodCost;
    }

    public int getStoneCost() {
        return stoneCost;
    }

    @Override
    public String toString() {
        return "Building: " + name + " (Wood: " + woodCost + ", Stone: " + stoneCost + ")";
    }
}
