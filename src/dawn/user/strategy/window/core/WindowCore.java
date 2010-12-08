package dawn.user.strategy.window.core;

import dawn.user.strategy.Strategy;

public class WindowCore implements Strategy {
    private boolean isRunning = false;

    @Override
    public void run() {
       isRunning = true;
       while (isRunning) {
           step();
           pause();
       }
    }

    @Override
    public void stop() {
        
    }
    
    private void step() {
        
    }
    
    private void pause() {
        
    }
}
