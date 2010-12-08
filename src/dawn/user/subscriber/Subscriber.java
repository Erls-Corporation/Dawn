package dawn.user.subscriber;

import dawn.core.subscriber.SubscriberService;

public abstract class Subscriber implements Runnable {
    private boolean isRunning;

    public Subscriber() {
        init();

        SubscriberService.getInstance().addStrategy(this);
    }

    public void stopStrategy() {
        isRunning = false;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            step();
            pause();
        }
        stop();
    }

    protected abstract void init();
    protected abstract void step();
    protected abstract void pause();
    protected abstract void stop();
}
