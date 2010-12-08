package dawn.core.data.market;

public class Market {
    private final Quote theBid;
    private final Quote theAsk;

    public Market(Quote aBid, Quote aAsk) {
        theBid = aBid;
        theAsk = aAsk;
    }
    
    public Quote getBid() {
        return theBid;
    }
    
    public Quote getAsk() {
        return theAsk;
    }
}
