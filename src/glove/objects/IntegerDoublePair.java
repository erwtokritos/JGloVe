package glove.objects;

/**
 *
 * @author thanos papaoikonou
 */
public class IntegerDoublePair implements Comparable<IntegerDoublePair>{
    
    private int id;
    private double value;

    public IntegerDoublePair(int id, double value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int compareTo(IntegerDoublePair other) {
        
        final int BEFORE = -1;        
        final int EQUAL = 0;                           
        final int AFTER = 1;
        
        if(this.value < other.value) {
            return AFTER;
        }else {
            return BEFORE;
        }
    }
    
    
}
