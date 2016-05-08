package com.slawomirkaczmarek.jalive.io;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PresentationFiles {

    public static PresentationFile newPresentationFile(Path path) throws IOException, URISyntaxException {
        String filePath = path.toAbsolutePath().toString().replace("\\", "/");
        URI uri = URI.create("jar:file:/" + filePath);
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        FileSystem fileSystem = FileSystems.newFileSystem(uri, env);
        return new PresentationFile(fileSystem);
    }
}
