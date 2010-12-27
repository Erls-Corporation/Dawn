package dawn.core.services.feed.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import dawn.core.data.market.Side;
import dawn.core.data.market.option.OptionMarket;
import dawn.core.data.market.option.OptionQuote;
import dawn.core.data.market.option.OptionMarketSnapshot;
import dawn.core.data.market.option.OptionType;

public class GoogleOptionFeedUtils {
    private static final String DESCRIPTION_URL = "http://www.google.com/finance?q=";
    private static final String OPTION_URL = "http://www.google.com/finance/option_chain?q=";
    private static final String HISTORY_URL = "http://www.google.com/finance/historical?output=csv&histperiod=daily&q=";

    private static double getVolatility(String aExchange, String aSymbol) {
        String myGoogleFeedURL = HISTORY_URL + aExchange + ":" + aSymbol;
        try {
            BufferedReader myBufferedReader = new BufferedReader(
                    new InputStreamReader(new URL(myGoogleFeedURL).openStream()));

            List<Double> priceDiffList = new LinkedList<Double>();

            String nextLine = myBufferedReader.readLine();
            while (nextLine != null) {
                nextLine = myBufferedReader.readLine();
                if (nextLine != null) {
                    String[] data = nextLine.split(",");
                    Double openPrice = Double.parseDouble(data[1]);
                    Double closePrice = Double.parseDouble(data[4]);

                    priceDiffList.add((closePrice - openPrice) / openPrice);
                }
            }

            double sum = 0.0;
            for (double priceDiff : priceDiffList) {
                sum += priceDiff;
            }

            double mean = sum / priceDiffList.size();
            sum = 0.0;

            for (double priceDiff : priceDiffList) {
                sum += (priceDiff - mean) * (priceDiff - mean);
            }

            return Math.sqrt(sum / priceDiffList.size());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Double.NEGATIVE_INFINITY;
    }

    private static double getAnnualRate(String aExchange, String aSymbol,
            double aUnderlyingPrice) {
        String myGoogleFeedURL = DESCRIPTION_URL + aExchange + ":" + aSymbol;
        StringBuilder mySource = new StringBuilder();
        try {
            BufferedReader myBufferedReader = new BufferedReader(
                    new InputStreamReader(new URL(myGoogleFeedURL).openStream()));

            String nextLine = myBufferedReader.readLine();
            while (nextLine != null) {
                nextLine = myBufferedReader.readLine();
                if (nextLine != null) {
                    mySource.append(nextLine);
                }
            }

            String[] parsedSource = mySource.toString().split(
                    "<span class=\"goog-inline-block key\" "
                            + "data-snapfield=\"range\">Range</span>|"
                            + "<span class=\"goog-inline-block key\" "
                            + "data-snapfield=\"range_52week\">52 week</span>");
            String source = parsedSource[1];
            parsedSource = source
                    .split("<span class=\"goog-inline-block val\">|"
                            + "</span><li>| - ");

            Double lowPrice = Double.parseDouble(parsedSource[1]);
            Double highPrice = Double.parseDouble(parsedSource[2]);

            return (highPrice - lowPrice) / aUnderlyingPrice;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Double.NEGATIVE_INFINITY;
    }

    private static OptionMarket convertToOptionMarket(String aGoogleMarket) {
        Double myBidPrice = null;
        Double myAskPrice = null;
        Double myStrike = null;
        OptionType myOptionType = null;
        Double myPrice = null;
        Double myChange = null;
        Integer myQuantity = null;
        Integer myOpenInterest = null;
        Long myExpiry = null;

        String[] myGoogleStatusTokens = aGoogleMarket.split(",");

        for (String myOutput : myGoogleStatusTokens) {
            myOutput = myOutput.trim();
            if (myOutput.startsWith("vol:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myQuantity = Integer.parseInt(myOutput);
                }
            } else if (myOutput.startsWith("b:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myBidPrice = Double.parseDouble(myOutput);
                }
            } else if (myOutput.startsWith("a:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myAskPrice = Double.parseDouble(myOutput);
                }
            } else if (myOutput.startsWith("strike:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myStrike = Double.parseDouble(myOutput);
                }
            } else if (myOutput.startsWith("s:")) {
                int i = 0;
                for (i = 0; i < myOutput.length(); i++) {
                    char c = myOutput.charAt(i);
                    if (Character.isDigit(c)) {
                        break;
                    }
                }
                myOutput = myOutput.substring(i); // or we should pass the name
                // of the underlying and
                // remove from substr..
                if (myOutput.contains("C")) {
                    myOptionType = OptionType.CALL;
                } else if (myOutput.contains("P")) {
                    myOptionType = OptionType.PUT;
                }
            } else if (myOutput.startsWith("p:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.matches(".*\\d")) {
                    myPrice = Double.parseDouble(myOutput);
                }
            } else if (myOutput.startsWith("c:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|\"| ", "");
                if (myOutput.matches(".*\\d")) {
                    myChange = Double.parseDouble(myOutput);
                }
            } else if (myOutput.startsWith("oi:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myOpenInterest = Integer.parseInt(myOutput);
                }
            } else if (myOutput.startsWith("expiry:")) {
                myOutput = myOutput.substring(myOutput.indexOf("expiry:") + 7)
                        .replaceAll(":|-|\"|", "");

                try {
                    Date myDate = new SimpleDateFormat("MMM dd")
                            .parse(myOutput);
                    myExpiry = myDate.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else if (myOutput.matches("^\\d{4}\"$")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");

                try {
                    Date myDate = new SimpleDateFormat("yyyy").parse(myOutput);
                    myExpiry = myDate.getTime() + myExpiry
                            - new Date().getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (myOptionType != null && myBidPrice != null && myAskPrice != null
                && myStrike != null && myPrice != null && myChange != null
                && myQuantity != null && myOpenInterest != null
                && myExpiry != null) {
            OptionMarket myOptionMarket = new OptionMarket(myOptionType,
                    myStrike,
                    new OptionQuote(Side.BID, myQuantity, myBidPrice),
                    new OptionQuote(Side.ASK, myQuantity, myAskPrice), myPrice,
                    myChange, myQuantity, myOpenInterest, myExpiry);
            return myOptionMarket;
        } else {
            return null;
        }
    }

    public static OptionMarketSnapshot convertToSnapshot(String aExchange,
            String aSymbol) {
        String myGoogleFeedURL = OPTION_URL + aExchange + ":" + aSymbol;
        StringBuilder mySource = new StringBuilder();
        OptionMarketSnapshot myOptionMarketSnapshot = null;
        try {
            BufferedReader myBufferedReader = new BufferedReader(
                    new InputStreamReader(new URL(myGoogleFeedURL).openStream()));
            String nextLine = myBufferedReader.readLine();
            while (nextLine != null) {
                mySource.append(nextLine);
                nextLine = myBufferedReader.readLine();
            }

            String[] underlyingPrices = mySource.toString().split(
                    "underlying_price:|}};");
            double underlyingPrice = Double.NEGATIVE_INFINITY;
            for (String underlying : underlyingPrices) {
                try {
                    underlyingPrice = Double.parseDouble(underlying);
                } catch (NumberFormatException e) {
                    continue;
                }
            }

            double volatility = getVolatility(aExchange, aSymbol);
            double rate = getAnnualRate(aExchange, aSymbol, underlyingPrice);

            if (underlyingPrice == Double.NEGATIVE_INFINITY
                    || volatility == Double.NEGATIVE_INFINITY
                    || rate == Double.NEGATIVE_INFINITY) {
                return null;
            }

            myOptionMarketSnapshot = new OptionMarketSnapshot(aExchange,
                    aSymbol, underlyingPrice, volatility, rate);

            String[] parsedSource = mySource.toString().split(
                    "puts:\\[|\\],calls:\\[|\\],underlying_id|<[.*]}\\],");
            String dataSetOne = parsedSource[1];
            String dataSetTwo = parsedSource[2];

            parsedSource = dataSetOne.split("\\},\\{|\\{|\\}");
            for (String myGoogleMarket : parsedSource) {
                OptionMarket myOptionMarket = convertToOptionMarket(myGoogleMarket);
                if (myOptionMarket != null) {
                    myOptionMarketSnapshot.addOptionMarket(myOptionMarket);
                }
            }

            parsedSource = dataSetTwo.split("\\},\\{|\\{|\\}");
            for (String myGoogleMarket : parsedSource) {
                OptionMarket myOptionMarket = convertToOptionMarket(myGoogleMarket);
                if (myOptionMarket != null) {
                    myOptionMarketSnapshot.addOptionMarket(myOptionMarket);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return myOptionMarketSnapshot;
    }
}
