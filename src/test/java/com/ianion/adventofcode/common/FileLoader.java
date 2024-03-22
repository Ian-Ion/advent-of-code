package com.ianion.adventofcode.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileLoader {

    public static String readFileAsString(String filename) {
        Path filePath = new File(filename).toPath();
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file", e);
        }
    }

    public static String[] readFileAsStringArray(String filename) {
        Path filePath = new File(filename).toPath();
        try {
            return Files.readString(filePath).split("\n");
        } catch (IOException e) {
            throw new RuntimeException("Could not read file", e);
        }
    }


}
