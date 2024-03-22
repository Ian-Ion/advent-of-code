package com.ianion.adventofcode.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FileLoader {

    public static String readFileAsString(String filename) {
        Path filePath = new File(filename).toPath();
        try {
            return Files.readString(filePath).replaceAll("\\s+", "");
        } catch (IOException e) {
            throw new RuntimeException("Could not read file", e);
        }
    }

    public static List<String> readFileAsStringList(String filename) {
        Path filePath = new File(filename).toPath();
        try {
            return Arrays.stream(Files.readString(filePath).split("\n"))
                    .map(s -> s.replaceAll("\\s+", ""))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Could not read file", e);
        }
    }


}
