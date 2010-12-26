package dawn.core.services.pricer.blackscholes;

import dawn.core.data.market.option.OptionMarket;
import dawn.core.data.market.option.OptionMarketSnapshot;
import dawn.core.data.market.option.OptionType;

//http://www.cs.princeton.edu/introcs/22library/BlackScholes.java.html
public class BlackScholesOptionPricer {
    private static double MILLISECONDS_PER_YEAR = 1000 * 60 * 60 * 24 * 365;

    private static double getCallOptionPrice(double aUnderlyingPrice,
            double aStrikePrice, double aAnnualRate, double aVolatility,
            long aTimeToExpiry) {
        double myTimeToExpiry = aTimeToExpiry / MILLISECONDS_PER_YEAR;
        double d1 = (Math.log(aUnderlyingPrice / aStrikePrice) + (aAnnualRate + aVolatility
                * aVolatility / 2)
                * myTimeToExpiry)
                / (aVolatility * Math.sqrt(myTimeToExpiry));
        double d2 = d1 - aVolatility * Math.sqrt(myTimeToExpiry);
        return aUnderlyingPrice * Normal.cdf(d1) - aStrikePrice
                * Math.exp(-aAnnualRate * myTimeToExpiry) * Normal.cdf(d2);
    }

    private static double getPutOptionPrice(double aUnderlyingPrice,
            double aStrikePrice, double aAnnualRate, double aVolatility,
            long aTimeToExpiry) {
        double myTimeToExpiry = aTimeToExpiry / MILLISECONDS_PER_YEAR;
        double d1 = (Math.log(aUnderlyingPrice / aStrikePrice) + (aAnnualRate + aVolatility
                * aVolatility / 2)
                * myTimeToExpiry)
                / (aVolatility * Math.sqrt(myTimeToExpiry));
        double d2 = d1 - aVolatility * Math.sqrt(myTimeToExpiry);

        return (1 - Normal.cdf(d2)) * aStrikePrice
                * Math.exp(-aAnnualRate * myTimeToExpiry)
                - (1 - Normal.cdf(d1)) * aUnderlyingPrice;
    }

    public static double getOptionPrice(OptionMarketSnapshot aOptionSnapshot,
            OptionMarket myOptionMarket) {
        if (myOptionMarket.getType() == OptionType.CALL) {
            return getCallOptionPrice(aOptionSnapshot.getUnderlyingPrice(),
                    myOptionMarket.getStrike(),
                    aOptionSnapshot.getAnnualRate(), aOptionSnapshot
                            .getVolatility(), myOptionMarket.getExpiry());
        } else if (myOptionMarket.getType() == OptionType.PUT) {
            return getPutOptionPrice(aOptionSnapshot.getUnderlyingPrice(),
                    myOptionMarket.getStrike(),
                    aOptionSnapshot.getAnnualRate(), aOptionSnapshot
                            .getVolatility(), myOptionMarket.getExpiry());
        }

        return Double.NaN;
    }

    private static double getCallDelta(double aUnderlyingPrice,
            double aStrikePrice, double aAnnualRate, double aVolatility,
            long aTimeToExpiry) {
        double myTimeToExpiry = aTimeToExpiry / MILLISECONDS_PER_YEAR;
        double d1 = (Math.log(aUnderlyingPrice / aStrikePrice) + (aAnnualRate + aVolatility
                * aVolatility / 2)
                * myTimeToExpiry)
                / (aVolatility * Math.sqrt(myTimeToExpiry));
        return Normal.cdf(d1);
    }

    private static double getPutDelta(double aUnderlyingPrice,
            double aStrikePrice, double aAnnualRate, double aVolatility,
            long aTimeToExpiry) {
        return getCallDelta(aUnderlyingPrice, aStrikePrice, aAnnualRate,
                aVolatility, aTimeToExpiry) - 1;
    }

    public static double getDelta(OptionMarketSnapshot aOptionSnapshot,
            OptionMarket aOptionMarket) {
        if (aOptionMarket.getType() == OptionType.CALL) {
            return getCallDelta(aOptionSnapshot.getUnderlyingPrice(),
                    aOptionMarket.getStrike(), aOptionSnapshot.getAnnualRate(),
                    aOptionSnapshot.getVolatility(), aOptionMarket.getExpiry());
        } else if (aOptionMarket.getType() == OptionType.PUT) {
            return getPutDelta(aOptionSnapshot.getUnderlyingPrice(),
                    aOptionMarket.getStrike(), aOptionSnapshot.getAnnualRate(),
                    aOptionSnapshot.getVolatility(), aOptionMarket.getExpiry());
        }

        return Double.NaN;
    }

    private static double getGamma(double aUnderlyingPrice,
            double aStrikePrice, double aAnnualRate, double aVolatility,
            long aTimeToExpiry) {
        double myTimeToExpiry = aTimeToExpiry / MILLISECONDS_PER_YEAR;
        double d1 = (Math.log(aUnderlyingPrice / aStrikePrice) + (aAnnualRate + aVolatility
                * aVolatility / 2)
                * myTimeToExpiry)
                / (aVolatility * Math.sqrt(myTimeToExpiry));

        return Normal.dcdf(d1)
                / (aUnderlyingPrice * aVolatility * Math.sqrt(myTimeToExpiry));
    }

    public static double getGamma(OptionMarketSnapshot aOptionSnapshot,
            OptionMarket aOptionMarket) {
        return getGamma(aOptionSnapshot.getUnderlyingPrice(), aOptionMarket
                .getStrike(), aOptionSnapshot.getAnnualRate(), aOptionSnapshot
                .getVolatility(), aOptionMarket.getExpiry());
    }
    
    private static double getVega(double aUnderlyingPrice,
            double aStrikePrice, double aAnnualRate, double aVolatility,
            long aTimeToExpiry) {
        double myTimeToExpiry = aTimeToExpiry / MILLISECONDS_PER_YEAR;
        double d1 = (Math.log(aUnderlyingPrice / aStrikePrice) + (aAnnualRate + aVolatility
                * aVolatility / 2)
                * myTimeToExpiry)
                / (aVolatility * Math.sqrt(myTimeToExpiry));

        return Normal.dcdf(d1) * aUnderlyingPrice * Math.sqrt(myTimeToExpiry);
    }

    public static double getVega(OptionMarketSnapshot aOptionSnapshot,
            OptionMarket aOptionMarket) {
        return getVega(aOptionSnapshot.getUnderlyingPrice(), aOptionMarket
                .getStrike(), aOptionSnapshot.getAnnualRate(), aOptionSnapshot
                .getVolatility(), aOptionMarket.getExpiry());
    }
}
