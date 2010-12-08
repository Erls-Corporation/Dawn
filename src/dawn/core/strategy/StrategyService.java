package dawn.core.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dawn.user.strategy.Strategy;
import dawn.user.strategy.window.core.WindowCore;

public class StrategyService {
    private ExecutorService theActiveStrategiesExecutor;
    private List<Strategy> theActiveStrategies;
    
    public StrategyService() {
        theActiveStrategies = new ArrayList<Strategy>();
        
        init();
    }
    
    private void init() {
        theActiveStrategiesExecutor = Executors.newCachedThreadPool();
        
        //TODO: i don't like this part; somehow get the strategy
        // to add itself to the running code.
        WindowCore aWindowStrategy = new WindowCore();
        theActiveStrategies.add(aWindowStrategy);
    }
    
    public void start() {
        for (Strategy myStrategy : theActiveStrategies) {
            theActiveStrategiesExecutor.execute(myStrategy);
        }
    }
    
    public void stop() {
        theActiveStrategiesExecutor.shutdown();
        
        for (Strategy myStrategy : theActiveStrategies) {
            myStrategy.stop();
        }
    }
}
