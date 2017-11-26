package service.pcap;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IPcapParser {

    public List<?> parseInput(InputStream is) throws IOException;

}
