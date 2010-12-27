package dawn.user.subscriber.window.core;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import dawn.core.data.market.option.OptionMarket;
import dawn.core.data.market.option.OptionMarketSnapshot;
import dawn.core.services.feed.google.GoogleOptionFeed;
import dawn.core.services.pricer.blackscholes.BlackScholesOptionPricer;
import dawn.user.subscriber.Subscriber;

public class WindowCore extends Subscriber {
    private GoogleOptionFeed theGoogleSPYFeed;

    private final int TIME_STEP = 5;

    @Override
    protected void init() {
        theGoogleSPYFeed = new GoogleOptionFeed("NYSE", "SPY", TIME_STEP);
    }

    @Override
    protected void step() {
        OptionMarketSnapshot mySnapshot = theGoogleSPYFeed.getSnapshot();

        if (mySnapshot != null) {
            // just shows the current snapshot
            show(mySnapshot);
        }
    }

    @Override
    protected void pause() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(TIME_STEP));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void stop() {

    }

    private void show(OptionMarketSnapshot aOptionMarketSnapshot) {
        System.out.print(aOptionMarketSnapshot.getExchange() + ":"
                + aOptionMarketSnapshot.getSymbol() + "\n");
        System.out.print(Calendar.getInstance().getTime() + "\n");
        System.out.print("Underlying Price: "
                + aOptionMarketSnapshot.getUnderlyingPrice() + "\n");
        System.out.print("Volatility: " + aOptionMarketSnapshot.getVolatility()
                + ", " + "Rate: " + aOptionMarketSnapshot.getAnnualRate()
                + "\n");
        System.out.print("Call:\n");
        for (OptionMarket myMarket : aOptionMarketSnapshot.getCallMarkets()) {
            showOption(aOptionMarketSnapshot, myMarket);
        }
        System.out.print("\n");
        System.out.print("Put:\n");
        for (OptionMarket myMarket : aOptionMarketSnapshot.getPutMarkets()) {
            showOption(aOptionMarketSnapshot, myMarket);
        }
        System.out.print("\n\n");
    }

    private void showOption(OptionMarketSnapshot aOptionMarketSnapshot,
            OptionMarket myOptionMarket) {
        System.out.println(myOptionMarket.getStrike()
                + ": Bid-Ask: "
                + myOptionMarket.getBid().getPrice()
                + " - "
                + myOptionMarket.getAsk().getPrice()
                + ", Last: "
                + myOptionMarket.getPrice()
                + ", Change: "
                + myOptionMarket.getChange()
                + ", Qty: "
                + myOptionMarket.getQuantity()
                + ", OpInt: "
                + myOptionMarket.getOpenInterest()
                + ", Expry: "
                + myOptionMarket.getExpiry()
                + ", Theoretical: "
                + BlackScholesOptionPricer.getOptionPrice(
                        aOptionMarketSnapshot, myOptionMarket)
                + "\n"
                + "Delta: "
                + BlackScholesOptionPricer.getDelta(aOptionMarketSnapshot,
                        myOptionMarket)
                + ", Gamma: "
                + BlackScholesOptionPricer.getGamma(aOptionMarketSnapshot,
                        myOptionMarket)
                + ", Vega: "
                + BlackScholesOptionPricer.getVega(aOptionMarketSnapshot,
                        myOptionMarket)
                + ", Theta: "
                + BlackScholesOptionPricer.getTheta(aOptionMarketSnapshot,
                        myOptionMarket)
                + ", Rho: "
                + BlackScholesOptionPricer.getRho(aOptionMarketSnapshot,
                        myOptionMarket));
    }
}
