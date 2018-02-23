package cz.vutbr.fit.util;

import cz.vutbr.fit.properties.PathResolver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.UUID;

public class FileManager {

    public static void SaveContent(String filename, byte[] content) throws IOException {
        java.nio.file.Files.write(Paths.get(filename), content);
    }

    public static byte[] ReadContent(String filename) throws IOException, URISyntaxException {
        return java.nio.file.Files.readAllBytes(PathResolver.resolveFilePath(filename));
    }

    public static void RemoveFile(String filename) {
        new File(filename).delete();
    }

    public static String GenerateTmpPath(String tmpDirectory) {
        return tmpDirectory + UUID.randomUUID() + FileExtension.PCAP;
    }

}
