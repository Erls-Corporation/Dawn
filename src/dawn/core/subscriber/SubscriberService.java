package dawn.core.subscriber;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dawn.user.subscriber.Subscriber;

public class SubscriberService {
    private static final SubscriberService theInstance = new SubscriberService();

    private ExecutorService theActiveSubscribersExecutor;
    private List<Subscriber> theActiveSubscribers;

    public static SubscriberService getInstance() {
        return theInstance;
    }
    
    public static void newSubscriber(String aSubscriberClassName) {
        try {
            Class.forName(aSubscriberClassName)
                    .getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private SubscriberService() {
        theActiveSubscribers = new ArrayList<Subscriber>();
        theActiveSubscribersExecutor = Executors.newCachedThreadPool();
    }

    public synchronized void addSubscriber(Subscriber aSubscriber) {
        theActiveSubscribers.add(aSubscriber);
    }
    
    public void start() {
        for (Subscriber mySubscriber : theActiveSubscribers) {
            theActiveSubscribersExecutor.execute(mySubscriber);
        }
    }

    public void stop() {
        theActiveSubscribersExecutor.shutdown();

        for (Subscriber mySubscriber : theActiveSubscribers) {
            mySubscriber.stopSubscription();
        }
    }
}
