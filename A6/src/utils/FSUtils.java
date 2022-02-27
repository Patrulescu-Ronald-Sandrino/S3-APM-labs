package utils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FSUtils {
    public static void deleteDirectory(File directory) throws IOException {
        if (directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (!file.delete()) {
                    System.err.println("Failed to delete file " + file.getName());
                }
            }
        }
        if (!directory.delete()) {
            throw new IOException("Failed to delete directory " + directory.getName());
        }
    }
}
