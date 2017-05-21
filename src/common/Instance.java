package common;

import java.util.ArrayList;
import java.util.List;

public class Instance {
    private int qtyItems;
    private int binCapacity;
    private List<Integer> items;
    
    public Instance() {
        items = new ArrayList<Integer>();
    }
    
    public int getQtyItems() {
        return qtyItems;
    }
    public void setQtyItems(int qtyItems) {
        this.qtyItems = qtyItems;
    }
    public int getBinCapacity() {
        return binCapacity;
    }
    public void setBinCapacity(int binCapacity) {
        this.binCapacity = binCapacity;
    }
    public List<Integer> getItems() {
        return items;
    }
    public void setItems(List<Integer> items) {
        this.items = items;
    }
    
}
