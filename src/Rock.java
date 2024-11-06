public class Rock extends Gatherable {
    public Rock(int quantity, Quality quality) {
        super(quantity, quality);
    }

    @Override
    public boolean gatherable() {
        return true;
    }

    @Override
    public String toString() {
        return "Rock{" +
                "quantity=" + quantity +
                ", quality=" + quality +
                '}';
    }
}
