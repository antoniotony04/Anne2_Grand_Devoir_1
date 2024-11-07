import java.util.Comparator;
public class valoareAtacComparator implements Comparator<Item> {
    @Override
    public int compare(Item o1, Item o2) {
        return Integer.compare(o2.getAttackValue(), o1.getAttackValue());
    }
}
