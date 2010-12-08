package dawn.core;

import java.util.concurrent.TimeUnit;

import dawn.core.strategy.StrategyService;

public class Core {
    private static StrategyService theStrategyService;

    public static void main(String[] args) {
        theStrategyService = new StrategyService();

        theStrategyService.start();

        //just test like this for now; this will eventually be some
        //cool UI of some sort I promise!
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        theStrategyService.stop();
    }
}
