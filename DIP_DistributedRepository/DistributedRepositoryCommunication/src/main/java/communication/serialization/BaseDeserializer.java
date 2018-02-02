package communication.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class BaseDeserializer {

    public static <T> T Deserialize(byte[] bytes, Class<T> valueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        T data = mapper.readValue(bytes, valueType);
        return data;
    }

}
