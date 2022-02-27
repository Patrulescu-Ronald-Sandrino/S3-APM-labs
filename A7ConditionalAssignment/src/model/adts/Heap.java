package model.adts;

import model.values.Value;

import java.util.HashMap;
import java.util.Map;

public class Heap implements IHeap<Integer, Value> {
    private Map<Integer, Value> heapDictionary;
    private int nextFreeAddress;

    public Heap() {
        this.heapDictionary = new HashMap<>();
        nextFreeAddress = 1;
    }

    public int getNextFreeAddress() {
        return nextFreeAddress;
    }

    @Override
    public boolean containsKey(Integer key) {
        return heapDictionary.containsKey(key);
    }

    @Override
    public Value get(Integer key) {
        return heapDictionary.get(key);
    }

    @Override
    public Value put(Integer key, Value value) {
        if (key < -1 || key == 0) {
            System.out.println("[Heap.put(" + key + ", " + value.toString() + ")] Invalid address for heap allocation");
//            throw new Exception("Invalid address for heap allocation");
        }
        if (key == -1) {
            return heapDictionary.put(nextFreeAddress++, value);
        }
        return heapDictionary.put(key, value);
    }

    @Override
    public Value remove(Integer key) {
        return heapDictionary.remove(key);
    }

    @Override
    public Map<Integer, Value> getContent() {
        return heapDictionary;
    }

    @Override
    public void setContent(Map<Integer, Value> newContent) {
        heapDictionary = newContent;
    }

    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<Integer, Value> entry : heapDictionary.entrySet()) {
//            result = result.concat("(" + entry.getKey() +", " + entry.getValue() + ")\n");
            result = result.concat(entry.getKey() + " --> " + entry.getValue() + "\n");
        }
        return result;
    }
}
