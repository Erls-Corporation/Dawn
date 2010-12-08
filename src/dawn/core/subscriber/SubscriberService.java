package dawn.core.subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dawn.user.subscriber.Subscriber;

public class SubscriberService {
    private static SubscriberService theInstance = new SubscriberService();

    private ExecutorService theActiveSubscribersExecutor;
    private List<Subscriber> theActiveSubscribers;

    public static SubscriberService getInstance() {
        return theInstance;
    }

    private SubscriberService() {
        theActiveSubscribers = new ArrayList<Subscriber>();
        theActiveSubscribersExecutor = Executors.newCachedThreadPool();
    }

    public void addStrategy(Subscriber aStrategy) {
        theActiveSubscribers.add(aStrategy);
    }
    
    public void start() {
        for (Subscriber myStrategy : theActiveSubscribers) {
            theActiveSubscribersExecutor.execute(myStrategy);
        }
    }

    public void stop() {
        theActiveSubscribersExecutor.shutdown();

        for (Subscriber myStrategy : theActiveSubscribers) {
            myStrategy.stopStrategy();
        }
    }
}
