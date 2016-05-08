package com.slawomirkaczmarek.jalive.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class JSONFile {
    public static <T> T read(Path path, Class<T> clazz) throws IOException {
        T result = null;
        Gson gson = new Gson();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            result = gson.fromJson(br, clazz);
        }
        return result;
    }

    public static <T> void write(Path path, T object) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
            gson.toJson(object, bw);
        }
    }
}
