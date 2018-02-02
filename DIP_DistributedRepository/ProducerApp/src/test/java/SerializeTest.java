import common.properties.PathResolver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Date;

public class SerializeTest {

    public static void main(String[] args) throws URISyntaxException, IOException {
        System.out.println(new Date());
        byte[] bytes = Files.readAllBytes(PathResolver.resolveFilePath("smallFlows.pcap"));
        String value = Base64.getEncoder().encodeToString(bytes);
        byte[] byteArray = Base64.getDecoder().decode(value);
        System.out.println("Size raw [B] = " + bytes.length);
        System.out.println("Size B64 [B] = " + value.length());
    }

}
