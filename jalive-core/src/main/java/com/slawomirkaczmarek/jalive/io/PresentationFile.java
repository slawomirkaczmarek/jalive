package com.slawomirkaczmarek.jalive.io;

import com.slawomirkaczmarek.jalive.presentation.PresentationDefinition;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class PresentationFile implements Closeable {
    private static final String DEFINITION_FILE_NAME = "definition.json";
    private static final String STYLE_FILE_NAME = "style.css";

    private FileSystem fileSystem;

    PresentationFile(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public PresentationDefinition readDefinition() throws IOException {
        PresentationDefinition result = null;
        Path path = fileSystem.getPath(DEFINITION_FILE_NAME);
        if (Files.exists(path)) {
            result = JSONFile.read(path, PresentationDefinition.class);
        }
        return result;
    }

    public void writeDefinition(PresentationDefinition definition) throws IOException {
        Path path = fileSystem.getPath(DEFINITION_FILE_NAME);
        JSONFile.write(path, definition);
    }

    public String readStyle() throws MalformedURLException {
        String result = null;
        Path path = fileSystem.getPath(STYLE_FILE_NAME);
        if (Files.exists(path)) {
            result = path.toUri().toURL().toExternalForm();
        }
        return result;
    }

    public InputStream readResource(String resource) throws IOException {
        InputStream result = null;
        Path path = fileSystem.getPath(resource);
        if (Files.exists(path)) {
            result = Files.newInputStream(path);
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        fileSystem.close();
    }
}
