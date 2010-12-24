package dawn.core.services;

import dawn.core.services.feed.FeedService;
import dawn.core.subscriber.SubscriberService;

public class ServiceManager {
    public static void startServices() {
        FeedService.getInstance().start();
        SubscriberService.getInstance().start();
    }

    public static void stopServices() {
        SubscriberService.getInstance().stop();
        FeedService.getInstance().stop();
    }
}
