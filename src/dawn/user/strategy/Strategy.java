package dawn.user.strategy;

import dawn.core.strategy.StrategyService;

public abstract class Strategy implements Runnable {
    private boolean isRunning;

    public Strategy() {
        init();

        StrategyService.getInstance().addStrategy(this);
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
