package dawn.core.services.feed;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedService {
    private static final FeedService theInstance = new FeedService();

    private ExecutorService theFeedExecutor;
    private List<Feed> theFeedList;

    public static FeedService getInstance() {
        return theInstance;
    }

    private FeedService() {
        theFeedExecutor = Executors.newCachedThreadPool();
        theFeedList = new LinkedList<Feed>();
    }
    
    public synchronized void addFeed(Feed aFeed) {
        theFeedList.add(aFeed);
    }
    
    public void start() {
        for (Feed myFeed : theFeedList) {
            theFeedExecutor.execute(myFeed);
        }
    }

    public void stop() {
        theFeedExecutor.shutdown();

        for (Feed myFeed : theFeedList) {
            myFeed.stopFeed();
        }
    }
}
