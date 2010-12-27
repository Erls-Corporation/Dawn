package dawn.core.data.market.option;

import java.util.LinkedList;
import java.util.List;

import dawn.core.data.market.Market;
import dawn.core.data.market.MarketSnapshot;

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
    
    public List<OptionMarket> getCallMarkets() {
        List<OptionMarket> myOptionList = new LinkedList<OptionMarket>();
        for (Market myMarket : theCallList) {
            myOptionList.add((OptionMarket)myMarket);
        }
        return myOptionList;
    }
    
    public List<OptionMarket> getPutMarkets() {
        List<OptionMarket> myOptionList = new LinkedList<OptionMarket>();
        for (Market myMarket : thePutList) {
            myOptionList.add((OptionMarket)myMarket);
        }
        return myOptionList;
    }
}
