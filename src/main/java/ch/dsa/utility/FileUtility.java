package ch.dsa.utility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class FileUtility {
    public static String readLines(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (Exception e) {
            return "";
        }
    }

    public static String[] readSplitLines(String path) {
        return splitString(readLines(path));
    }

    public static List<String> readFile(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static String[] splitString(String s) {
        return s.split("\\s+");
    }
}
