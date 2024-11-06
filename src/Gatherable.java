public abstract class Gatherable {
    public int quantity;
    public Quality quality;

    public Gatherable(int quantity, Quality quality) {
        this.quantity = quantity;
        this.quality = quality;
    }

    public abstract boolean gatherable();

    @Override
    public abstract String toString();


    public enum Quality {
        COMMON, RARE, EPIC
    }
}

