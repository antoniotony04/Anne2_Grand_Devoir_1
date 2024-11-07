import java.util.Comparator;
public class aparareComparator implements Comparator<Item>{
    @Override
    public int compare(Item o1, Item o2) {
        return Integer.compare(o1.getDefenseValue(), o2.getDefenseValue());
    }
}
