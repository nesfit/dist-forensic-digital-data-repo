package service.serialize;

import java.io.*;
import java.util.Base64;

public class Serializer {

    public static String Serialize(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static Object Deserialize(String string) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(string);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object object = ois.readObject();
        ois.close();
        return object;
    }

}
