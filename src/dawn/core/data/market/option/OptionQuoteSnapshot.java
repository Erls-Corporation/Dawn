package dawn.core.data.market.option;

import java.util.Calendar;

import dawn.core.data.market.Market;
import dawn.core.data.market.MarketSnapshot;

public class OptionQuoteSnapshot extends MarketSnapshot {

    public OptionQuoteSnapshot(String aExchange, String aSymbol) {
        super(aExchange, aSymbol);
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

    public void show() {
        System.out.print(Calendar.getInstance().getTime() + "\n");
        System.out.print("Call:\n");
        for (Market myOptionMarket : theCallList) {
            System.out.print(((OptionMarket) myOptionMarket).getStrike() + ": "
                    + myOptionMarket.getBid().getPrice() + " - "
                    + myOptionMarket.getAsk().getPrice() + "\n");
        }
        System.out.print("\n");
        System.out.print("Put:\n");
        for (Market myOptionMarket : thePutList) {
            System.out.print(((OptionMarket) myOptionMarket).getStrike() + ": "
                    + myOptionMarket.getBid().getPrice() + " - "
                    + myOptionMarket.getAsk().getPrice() + "\n");
        }
        System.out.print("\n\n");
    }
}