package dawn.user.strategy.window.core;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import dawn.user.strategy.Strategy;

public class WindowCore implements Strategy {
    private final int TIME_STEP = 1;
    private boolean isRunning;
    
    public WindowCore() {
        isRunning = false;
    }

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
        isRunning = false;
    }
    
    private void step() {
        System.out.println(new Date().toString());
    }
    
    private void pause() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(TIME_STEP));
        } catch (InterruptedException e) {
            e.printStackTrace();
            
            isRunning = false;
        }
    }
}
