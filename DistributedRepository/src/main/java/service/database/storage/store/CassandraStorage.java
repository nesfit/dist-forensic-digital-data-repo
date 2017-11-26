package service.database.storage.store;

public class CassandraStorage<T extends ISave> implements IStorage<T> {

    @Override
    public void store(T object) {
        object.save();
    }

}
