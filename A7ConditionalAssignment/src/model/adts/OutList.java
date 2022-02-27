package model.adts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OutList<V> implements IOutList<V> {
    private final List<V> list;

    public OutList() {
        this.list = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public void add(V v) {
        list.add(v);
    }

    @Override
    public String toString() {
//        return list.toString();
        String result = "";
        for (V element : list) {
            result = result.concat(element.toString() + '\n');
        }
        return result;
    }

    public String toStringListWrapper() {
        return list.toString();
    }

}
