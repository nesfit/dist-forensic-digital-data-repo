package common.util;

import java.net.URISyntaxException;

import com.google.common.io.Files;
import common.properties.PathResolver;
import common.properties.Properties;
import common.properties.PropertyConstants;

import java.io.File;
import java.io.IOException;

public class FileManager {

    public static void SaveContent(String filename, byte[] content) throws IOException {
        Files.write(content, new File(filename));
    }

    public static byte[] ReadContent(String filename) throws IOException, URISyntaxException {
        return java.nio.file.Files.readAllBytes(PathResolver.resolveFilePath(filename));
    }

    public static void RemoveFile(String filename) {
        new File(filename).delete();
    }

    public static String GenerateTmpPath() {
        return Properties.getInstance().loadProperty(PropertyConstants.TMP_DIRECTORY) + "tmp.pcap";
    }

}
