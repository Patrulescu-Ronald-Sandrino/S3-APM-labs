package interpreter;

import model.values.RefValue;
import model.values.Value;

import java.util.*;
import java.util.stream.Collectors;

public class GarbageCollector {

    private static Set<Integer> getReferencedAddresses(List<Collection<Value>> symbolTableValuesList, Map<Integer, Value> heap) {
        Set<Integer> result = new HashSet<>();
        symbolTableValuesList.forEach(symbolTableValue -> symbolTableValue.stream()
                .filter(value -> value instanceof RefValue && ((RefValue) value).getAddress() != 0 &&
                        !result.contains(((RefValue) value).getAddress()))
                .forEach(value -> {
                    RefValue refValue = (RefValue) value;
                    result.add(refValue.getAddress());
                    var heapValue = heap.get(refValue.getAddress());
                    while (heapValue instanceof RefValue) {
                        RefValue heapRefValue = (RefValue) heapValue;
                        result.add(heapRefValue.getAddress());
                        heapValue = heap.get(heapRefValue.getAddress());
                    }
                }));
        return result;
    }

    public static Map<Integer, Value> getCleanedHeapEntries(List<Collection<Value>> symbolTableValuesList, Map<Integer, Value> heap) {
        Set<Integer> referencedAddresses = getReferencedAddresses(symbolTableValuesList, heap);

        return heap.entrySet().stream()
                .filter(entry -> referencedAddresses.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
