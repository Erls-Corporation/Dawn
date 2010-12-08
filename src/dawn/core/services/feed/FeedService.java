package dawn.core.services.feed;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedService {
    private ExecutorService theFeedExecutor;
    private Map<String, Feed> theFeedMap;    
    
    public FeedService() {
        theFeedMap = new HashMap<String, Feed>();
        
        init();
    }
    
    private void init() {
        theFeedExecutor = Executors.newCachedThreadPool();
    }
    
    public void stop() {
        theFeedExecutor.shutdown();
        
        for (Feed myFeed : theFeedMap.values()) {
            myFeed.stop();
        }
    }

    public void addFeed(String aFeedKey, Feed aFeed) {
        theFeedExecutor.execute(aFeed);
        
        theFeedMap.put(aFeedKey, aFeed);
    }
    
    public Feed getFeed(String aFeedKey) {
        return theFeedMap.get(aFeedKey);
    }
}
