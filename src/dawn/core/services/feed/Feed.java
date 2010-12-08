package dawn.core.services.feed;

public abstract class Feed implements Runnable {
    protected boolean isRunning = false;

    public Feed() {
        init();
        
        FeedService.getInstance().addFeed(this);
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            step();
            pause();
        }
    }

    public void stopFeed() {
        isRunning = false;
        stop();
    }

    protected abstract void init();
    protected abstract void step();
    protected abstract void pause();
    protected abstract void stop();
}
