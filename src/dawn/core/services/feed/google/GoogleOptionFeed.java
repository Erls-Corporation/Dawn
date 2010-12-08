package dawn.core.services.feed.google;

import java.util.concurrent.TimeUnit;

import dawn.core.data.market.option.OptionMarketSnapshot;
import dawn.core.services.feed.Feed;

public class GoogleOptionFeed extends Feed {
    private final String theExchange;
    private final String theSymbol;
    private final int thePauseTime;

    private OptionMarketSnapshot theCurrentSnapshot;    

    public GoogleOptionFeed(String aExchange, String aSymbol, int aPauseTime) {
        theExchange = aExchange;
        theSymbol = aSymbol;
        thePauseTime = aPauseTime;

        theCurrentSnapshot = null;
    }
    
    @Override
    protected void init() {
        
    }

    @Override
    protected void step() {
        theCurrentSnapshot = GoogleOptionFeedUtils.convertToSnapshot(
                theExchange, theSymbol);
    }

    @Override
    protected void pause() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(thePauseTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }
    
    public OptionMarketSnapshot getSnapshot() {
        return theCurrentSnapshot;
    }
}
