public abstract class Gatherable {
    protected int quantity;
    protected Quality quality;

    public Gatherable(int quantity, Quality quality) {
        this.quantity = quantity;
        this.quality = quality;
    }

    public abstract boolean gatherable();

    @Override
    public String toString() {
        return "Gatherable{" +
                "quantity=" + quantity +
                ", quality=" + quality +
                '}';
    }

    public enum Quality {
        COMMON, RARE, EPIC
    }
}

