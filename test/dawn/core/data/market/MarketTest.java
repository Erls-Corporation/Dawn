package dawn.core.data.market;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MarketTest {
    
    @Test
    public void testMarket() {
        Market myMarket = new Market(null, null);
        assertEquals(null, myMarket.getAsk());
    }
}
