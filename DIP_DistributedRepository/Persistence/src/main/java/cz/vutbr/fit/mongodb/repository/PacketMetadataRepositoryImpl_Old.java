package cz.vutbr.fit.mongodb.repository;

/*import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;*/

public class PacketMetadataRepositoryImpl_Old /*implements InsertAsync*/ {

    // TODO: Use Reactive MongoDB instead.

    /*@Autowired
    MongoClient mongoClient;

    @PostConstruct
    private void init() {
        System.out.println("PacketMetadataRepositoryImpl - MongoClient: " + mongoClient);
    }

    @Override
    public void insertAsync(PacketMetadata packetMetadata) {
        // TODO: Cache collection
        MongoDatabase mongoDatabase = mongoClient.getDatabase("metadata");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("packet_metadata");

        Document record = new Document();
        record.append("refId", packetMetadata.getRefId());
        record.append("databaseType", packetMetadata.getDatabaseType());
        record.append("srcIpAddress", packetMetadata.getSrcIpAddress());
        record.append("dstIpAddress", packetMetadata.getDstIpAddress());
        mongoCollection.insertOne(record, PacketMetadataRepositoryImpl::onResult);
    }

    private static void onResult(Void result, Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }*/

}
