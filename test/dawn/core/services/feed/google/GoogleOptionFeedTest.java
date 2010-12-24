package dawn.core.services.feed.google;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dawn.core.services.feed.FeedService;

public class GoogleOptionFeedTest {

    GoogleOptionFeed theFeed;
    
    @Before
    public void setUp() {
        FeedService.getInstance().start();
        
        theFeed = new GoogleOptionFeed("SPY", "NYSE", 0);
    }
    
    @After
    public void tearDown() {
        FeedService.getInstance().stop();
    }
    
    @Test
    public void testGetSnapshot() {
        
    }
}
