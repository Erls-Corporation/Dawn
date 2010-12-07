package dawn.user.strategy;

import java.util.ArrayList;
import java.util.List;

import dawn.user.strategy.window.core.WindowCore;

public class StrategyService {
    private List<Strategy> theActiveStrategies;
    
    public StrategyService() {
        theActiveStrategies = new ArrayList<Strategy>();
    }
    
    public void init() {
        WindowCore aWindowStrategy = new WindowCore();
        theActiveStrategies.add(aWindowStrategy);
    }
    
    public void start() {
        
    }
    
    public void stop() {
        
    }
}
