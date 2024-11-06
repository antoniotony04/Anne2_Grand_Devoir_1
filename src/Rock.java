public class Rock extends Gatherable {
    public Rock(int quantity, Quality quality) {
        super(quantity, quality);
    }

    @Override
    public boolean gatherable() {
        return true;
    }
}
