package dawn.core.data.market.option;

import java.util.Calendar;

import dawn.core.data.market.Market;
import dawn.core.data.market.MarketSnapshot;

public class OptionMarketSnapshot extends MarketSnapshot {

    public OptionMarketSnapshot(String aExchange, String aSymbol) {
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
        for (Market myMarket : theCallList) {
            OptionMarket myOptionMarket = (OptionMarket) myMarket;
            System.out.println(
                    myOptionMarket.getStrike() 
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
                    + myOptionMarket.getExpiry() );
        }
        System.out.print("\n");
        System.out.print("Put:\n");
        for (Market myMarket : thePutList) {
            OptionMarket myOptionMarket = (OptionMarket) myMarket;
            System.out.println(
                    myOptionMarket.getStrike() 
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
                    + myOptionMarket.getExpiry() );
        }
        System.out.print("\n\n");
    }
}
