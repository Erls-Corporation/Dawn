package dawn.user.subscriber.window.core;

import java.util.concurrent.TimeUnit;

import dawn.core.data.market.option.OptionMarketSnapshot;
import dawn.core.services.feed.google.GoogleOptionFeed;
import dawn.user.subscriber.Subscriber;

public class WindowCore extends Subscriber {
    private GoogleOptionFeed theGoogleSPYFeed;

    private final int TIME_STEP = 5;

    @Override
    protected void init() {
        theGoogleSPYFeed = new GoogleOptionFeed("NYSE", "CRM", TIME_STEP);
    }

    @Override
    protected void step() {
        OptionMarketSnapshot mySnapshot = theGoogleSPYFeed.getSnapshot();

        if (mySnapshot != null) {
            //just shows the current snapshot
            mySnapshot.show();
        }
    }

    @Override
    protected void pause() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(TIME_STEP));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void stop() {

    }
}
