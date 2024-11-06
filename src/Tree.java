public class Tree extends Gatherable {
    public Tree(int quantity, Quality quality) {
        super(quantity, quality);
    }

    @Override
    public boolean gatherable() {
        return true;
    }

    @Override
    public String toString() {
        return "Tree{" +
                "quantity=" + quantity +
                ", quality=" + quality +
                '}';
    }
}
