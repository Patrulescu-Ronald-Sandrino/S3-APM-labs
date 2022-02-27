package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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

    /**
     * copy and replace existing
     *
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void copyFile(String source, String destination) throws IOException {
//        inspired from https://www.geeksforgeeks.org/different-ways-to-copy-files-in-java/
//        File sourceFile = new File(source);
//        File destinationFile = new File(destination);
//        Files.copy(sourceFile.toPath(), destinationFile.toPath());
        Files.copy(Paths.get(source), Paths.get(destination), REPLACE_EXISTING);
    }
}
