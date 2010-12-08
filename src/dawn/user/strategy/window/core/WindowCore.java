package dawn.user.strategy.window.core;

import java.util.concurrent.TimeUnit;

import dawn.core.data.market.option.OptionQuoteSnapshot;
import dawn.core.services.feed.google.GoogleOptionFeed;
import dawn.user.strategy.Strategy;

public class WindowCore extends Strategy {
    private GoogleOptionFeed theGoogleSPYFeed;

    private final int TIME_STEP = 5;

    @Override
    protected void init() {
        theGoogleSPYFeed = new GoogleOptionFeed("NYSE", "SPY", TIME_STEP);
    }

    @Override
    protected void step() {
        OptionQuoteSnapshot mySnapshot = theGoogleSPYFeed.getSnapshot();

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
