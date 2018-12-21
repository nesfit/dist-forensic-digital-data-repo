package cz.vutbr.fit.distributedrepository.util.serialize;

import java.io.*;

public class Serializer {

    public static byte[] Serialize(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        return baos.toByteArray();
    }

    public static Object Deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object object = ois.readObject();
        ois.close();
        return object;
    }

}
