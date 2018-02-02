package service.pcap;

import java.io.IOException;
import java.util.List;

public interface IPcapParser {

    public List<?> parseInput(String path) throws IOException;

}
