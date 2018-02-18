package common.properties;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class PathResolver {

    public static String resolveFilePathName(String filename) throws URISyntaxException, IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(filename);
        return Paths.get(resource.toURI()).toFile().getPath();
    }

    public static Path resolveFilePath(String filename) throws URISyntaxException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(filename);
        return Paths.get(resource.toURI());
    }

}