package dawn.core.data.market.option;

import dawn.core.data.market.Market;

public class OptionMarket extends Market {
    private final OptionType theType;
    private final double theStrike;

    public OptionMarket(OptionType aType, double aStrike,
            OptionQuote aBidQuote, OptionQuote aAskQuote) {
        super(aBidQuote, aAskQuote);
        theType = aType;
        theStrike = aStrike;
    }

    public OptionQuote getBid() {
        return (OptionQuote) theBidQuote;
    }

    public OptionQuote getAsk() {
        return (OptionQuote) theAskQuote;
    }

    public double getStrike() {
        return theStrike;
    }

    public OptionType getType() {
        return theType;
    }
}
