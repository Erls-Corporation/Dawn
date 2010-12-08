package dawn.core.data.market;

import java.util.LinkedList;
import java.util.List;

public class MarketSnapshot {
    protected final String theExchange;
    protected final String theSymbol;

    protected List<Market> theCallList;
    protected List<Market> thePutList;

    public MarketSnapshot(String aExchange, String aSymbol) {
        theExchange = aExchange;
        theSymbol = aSymbol;

        theCallList = new LinkedList<Market>();
        thePutList = new LinkedList<Market>();
    }

    protected void addCallMarket(Market aCallMarket) {
        theCallList.add(aCallMarket);
    }

    protected void addPutMarket(Market aPutMarket) {
        thePutList.add(aPutMarket);
    }
}
