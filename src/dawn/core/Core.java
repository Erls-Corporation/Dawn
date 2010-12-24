package dawn.core;

import java.util.concurrent.TimeUnit;

import dawn.core.services.ServiceManager;
import dawn.core.subscriber.SubscriberService;

public class Core {
    public static void main(String[] args) {
        SubscriberService.newSubscriber("dawn.user.subscriber.window.core.WindowCore");

        ServiceManager.startServices();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(11));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ServiceManager.stopServices();
    }
}
