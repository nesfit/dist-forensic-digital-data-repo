package cz.vutbr.fit.util;

import cz.vutbr.fit.properties.PathResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileManager {

    private static String TMP_DIRECTORY;

    public static void SaveContent(String filename, byte[] content) throws IOException {
        java.nio.file.Files.write(Paths.get(filename), content);
    }

    public static byte[] ReadContent(String filename) throws IOException, URISyntaxException {
        return java.nio.file.Files.readAllBytes(PathResolver.resolveFilePath(filename));
    }

    public static void RemoveFile(String filename) {
        new File(filename).delete();
    }

    public static String GenerateTmpPath() {
        return TMP_DIRECTORY + UUID.randomUUID() + FileExtension.PCAP;
    }

    @Value("${tmp.directory}")
    public void setTmpDirectory(String tmpDirectory) {
        FileManager.TMP_DIRECTORY = tmpDirectory;
    }

}
