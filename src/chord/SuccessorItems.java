package chord;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SuccessorItems {
    private LinkedHashMap<Integer, ArrayList<Item>> successorItems;

    public void setItems(LinkedHashMap<Integer, ArrayList<Item>> successorItems){
        this.successorItems = successorItems;
    }

    public LinkedHashMap<Integer, ArrayList<Item>> getItems(){
        return this.successorItems;
    }
}
