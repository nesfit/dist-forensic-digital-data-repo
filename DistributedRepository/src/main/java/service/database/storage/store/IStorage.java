package service.database.storage.store;

public interface IStorage<T extends ISave> {

    public void store(T object);

}
