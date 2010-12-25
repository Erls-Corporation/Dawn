package dawn.core.services.pricer.blackscholes;

//http://www.cs.princeton.edu/introcs/22library/BlackScholes.java.html
public class BlackScholesOptionPricer {
    private static double MILLISECONDS_PER_DAY = 86400000.0;

    public static double callPrice(double aSpotPrice, double aStrikePrice,
            double aAnnualRate, double aReturnVolatility, long aTimeToExpiry) {
        double myTimeToExpiry = aTimeToExpiry / MILLISECONDS_PER_DAY;
        double d1 = (Math.log(aSpotPrice / aStrikePrice) + (aAnnualRate + aReturnVolatility
                * aReturnVolatility / 2)
                * myTimeToExpiry)
                / (aReturnVolatility * Math.sqrt(myTimeToExpiry));
        double d2 = d1 - aReturnVolatility * Math.sqrt(myTimeToExpiry);
        return aSpotPrice * Gaussian.Phi(d1) - aStrikePrice
                * Math.exp(-aAnnualRate * myTimeToExpiry) * Gaussian.Phi(d2);
    }

    public static double putPrice(double aSpotPrice, double aStrikePrice,
            double aAnnualRate, double aReturnVolatility, long aTimeToExpiry) {
        double myTimeToExpiry = aTimeToExpiry / MILLISECONDS_PER_DAY;
        double d1 = (Math.log(aSpotPrice / aStrikePrice) + (aAnnualRate + aReturnVolatility
                * aReturnVolatility / 2)
                * myTimeToExpiry)
                / (aReturnVolatility * Math.sqrt(myTimeToExpiry));
        double d2 = d1 - aReturnVolatility * Math.sqrt(myTimeToExpiry);

        return (1 - Gaussian.Phi(d2)) * aStrikePrice
                * Math.exp(-aAnnualRate * myTimeToExpiry)
                - (1 - Gaussian.Phi(d1)) * aSpotPrice;
    }
}
