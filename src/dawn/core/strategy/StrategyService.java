package dawn.core.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dawn.user.strategy.Strategy;

public class StrategyService {
    private static StrategyService theInstance = new StrategyService();

    private ExecutorService theActiveStrategiesExecutor;
    private List<Strategy> theActiveStrategies;

    public static StrategyService getInstance() {
        return theInstance;
    }

    private StrategyService() {
        theActiveStrategies = new ArrayList<Strategy>();
        theActiveStrategiesExecutor = Executors.newCachedThreadPool();
    }

    public void addStrategy(Strategy aStrategy) {
        theActiveStrategies.add(aStrategy);
        theActiveStrategiesExecutor.execute(aStrategy);
    }

    public void stop() {
        theActiveStrategiesExecutor.shutdown();

        for (Strategy myStrategy : theActiveStrategies) {
            myStrategy.stopStrategy();
        }
    }
}
