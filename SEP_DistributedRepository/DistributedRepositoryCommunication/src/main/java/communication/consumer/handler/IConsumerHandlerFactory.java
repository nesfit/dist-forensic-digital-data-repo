package communication.consumer.handler;

public interface IConsumerHandlerFactory<T> {

    public IConsumerHandler getConsumerHandler(T request);

}
