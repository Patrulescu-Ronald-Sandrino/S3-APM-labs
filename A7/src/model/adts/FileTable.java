package model.adts;

import model.values.StringValue;

import java.io.BufferedReader;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class FileTable implements IFileTable<StringValue, BufferedReader> {
    private final Map<StringValue, BufferedReader> fileTable;

    public FileTable() {
        this.fileTable = new Hashtable<>();
    }

    @Override
    public boolean isDefined(StringValue id) {
        return fileTable.containsKey(id);
    }

    @Override
    public BufferedReader lookUp(StringValue id) {
        return fileTable.get(id);
    }

    @Override
    public void update(StringValue id, BufferedReader value) {
        fileTable.put(id, value);
    }

    @Override
    public BufferedReader remove(StringValue id) {
        return fileTable.remove(id);
    }

    @Override
    public String toString() {
        String result = "";
        for (StringValue fileName : fileTable.keySet()) {
            result = result.concat(fileName.toString() + "\n");
        }
        return result;
    }


    @Override
    public Set<Map.Entry<StringValue, BufferedReader>> getContent() {
        return fileTable.entrySet();
    }
}
