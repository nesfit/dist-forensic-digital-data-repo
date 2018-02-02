import common.properties.PathResolver;
import common.util.FileManager;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestFile {

    public static void main(String[] args) throws IOException, URISyntaxException {
        String filepath = FileManager.GenerateTmpPath();
        System.out.println(filepath);
        FileManager.SaveContent(filepath, FileManager.ReadContent(PathResolver.resolveFilePathName("dns.pcap")));
    }

}
