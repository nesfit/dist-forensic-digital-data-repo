import communication.command.DataType;
import communication.command.Operation;
import communication.request.KafkaRequest;
import communication.request.serialization.KafkaRequestDeserializer;
import communication.request.serialization.KafkaRequestSerializer;

public class Serialization {
    public static void main(String[] args) {

        KafkaRequest<Dummy> req = new KafkaRequest<>();
        req.setDataType(DataType.PCAP);
        req.setOperation(Operation.STORE);
        req.setValue(new Dummy());

        KafkaRequestSerializer ser = new KafkaRequestSerializer();
        byte[] raw = ser.serialize("s", req);

        KafkaRequestDeserializer deser = new KafkaRequestDeserializer();
        KafkaRequest<Dummy> r = deser.deserialize("s", raw);

        System.out.println(req.toString());
        System.out.println(r.toString());
    }
}
