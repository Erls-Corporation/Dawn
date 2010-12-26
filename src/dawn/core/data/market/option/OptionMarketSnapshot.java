package dawn.core.data.market.option;

import java.util.Calendar;

import dawn.core.data.market.Market;
import dawn.core.data.market.MarketSnapshot;
import dawn.core.services.pricer.blackscholes.BlackScholesOptionPricer;

public class OptionMarketSnapshot extends MarketSnapshot {

    private final double theVolatility;
    private final double theAnnualRate;

    public OptionMarketSnapshot(String aExchange, String aSymbol,
            double aUnderlyingPrice, double aVolatility, double aAnnualRate) {
        super(aExchange, aSymbol, aUnderlyingPrice);

        theVolatility = aVolatility;
        theAnnualRate = aAnnualRate;
    }

    public void addOptionMarket(OptionMarket aOptionMarket) {
        switch (aOptionMarket.getType()) {
            case CALL:
                addCallMarket(aOptionMarket);
                break;
            case PUT:
                addPutMarket(aOptionMarket);
                break;
            default:
                throw new IllegalArgumentException(this + "Invalid Market Type");
        }
    }

    public String getExchange() {
        return theExchange;
    }

    public String getSymbol() {
        return theSymbol;
    }

    public double getUnderlyingPrice() {
        return theUnderlyingPrice;
    }

    public double getVolatility() {
        return theVolatility;
    }

    public double getAnnualRate() {
        return theAnnualRate;
    }

    public void show() {
        System.out.print(theExchange + ":" + theSymbol + "\n");
        System.out.print(Calendar.getInstance().getTime() + "\n");
        System.out.print("Underlying Price: " + theUnderlyingPrice + "\n");
        System.out.print("Volatility: " + theVolatility + ", " + "Rate: "
                + theAnnualRate + "\n");
        System.out.print("Call:\n");
        for (Market myMarket : theCallList) {
            showOption(myMarket);
        }
        System.out.print("\n");
        System.out.print("Put:\n");
        for (Market myMarket : thePutList) {
            showOption(myMarket);
        }
        System.out.print("\n\n");
    }

    private void showOption(Market myMarket) {
        OptionMarket myOptionMarket = (OptionMarket) myMarket;

        System.out.println(myOptionMarket.getStrike() + ": Bid-Ask: "
                + myOptionMarket.getBid().getPrice() + " - "
                + myOptionMarket.getAsk().getPrice() + ", Last: "
                + myOptionMarket.getPrice() + ", Change: "
                + myOptionMarket.getChange() + ", Qty: "
                + myOptionMarket.getQuantity() + ", OpInt: "
                + myOptionMarket.getOpenInterest() + ", Expry: "
                + myOptionMarket.getExpiry() + "\n"
                + "Black Scholes Theoretical: "
                + BlackScholesOptionPricer.getOptionPrice(this, myOptionMarket)
                + ", Delta: "
                + BlackScholesOptionPricer.getDelta(this, myOptionMarket));
    }
}
