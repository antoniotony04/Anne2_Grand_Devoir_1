public class Grain extends Gatherable {
    public Grain(int quantity, Quality quality) {
        super(quantity, quality);
    }

    @Override
    public boolean gatherable() {
        return true;
    }

    @Override
    public String toString() {
        return "Grain{" +
                "quantity=" + quantity +
                ", quality=" + quality +
                '}';
    }
}
