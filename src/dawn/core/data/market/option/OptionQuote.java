package dawn.core.data.market.option;

import dawn.core.data.market.Quote;
import dawn.core.data.market.Side;

public class OptionQuote extends Quote {

    public OptionQuote(Side aSide, int aQuantity, double aPrice) {
        super(aSide, aQuantity, aPrice);
    }

}
