package dawn.core;

import java.util.concurrent.TimeUnit;

import dawn.user.strategy.StrategyService;

public class Core {
    private static StrategyService theStrategyService;

    public static void main(String[] args) throws InterruptedException {
        theStrategyService = new StrategyService();

        theStrategyService.start();

        Thread.sleep(TimeUnit.SECONDS.toMillis(10));

        theStrategyService.stop();
    }
}
