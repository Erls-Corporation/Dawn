package dawn.core;

import java.util.concurrent.TimeUnit;

import dawn.core.services.feed.FeedService;
import dawn.core.strategy.StrategyService;
import dawn.user.strategy.window.core.WindowCore;

public class Core {
    public static void main(String[] args) {
        new WindowCore();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(11));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StrategyService.getInstance().stop();
        FeedService.getInstance().stop();
    }
}
