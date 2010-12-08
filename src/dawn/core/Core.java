package dawn.core;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import dawn.core.services.feed.FeedService;
import dawn.core.subscriber.SubscriberService;

public class Core {
    public static void main(String[] args) {
        try {
            Class.forName("dawn.user.strategy.window.core.WindowCore")
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

        FeedService.getInstance().start();
        SubscriberService.getInstance().start();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(11));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SubscriberService.getInstance().stop();
        FeedService.getInstance().stop();
    }
}
