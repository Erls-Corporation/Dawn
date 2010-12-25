package dawn.core.services.feed.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dawn.core.data.market.Side;
import dawn.core.data.market.option.OptionMarket;
import dawn.core.data.market.option.OptionQuote;
import dawn.core.data.market.option.OptionMarketSnapshot;
import dawn.core.data.market.option.OptionType;

public class GoogleOptionFeedUtils {
    private static final String URL = "http://www.google.com/finance/option_chain?q=";
    
    private static double getVolatility(String aExchange, String aSymbol) {
        //TODO: use http://www.google.com/finance/historical?q=aExchange:aSymbol
        return 0.01;
    }
    
    private static double getAnnualRate(String aExchange, String aSymbol) {
        //TODO: ???
        return 0.01;
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
        String myGoogleFeedURL = URL + aExchange + ":" + aSymbol;
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

            String[] underlyingPrice = mySource.toString().split(
                    "underlying_price:|}};");
            double basePrice = Double.NEGATIVE_INFINITY;
            for (String underlying : underlyingPrice) {
                try {
                    basePrice = Double.parseDouble(underlying);
                } catch (NumberFormatException e) {
                    continue;
                }
            }
            
            if (basePrice == Double.NEGATIVE_INFINITY) {
                return null;
            }
            
            double volatility = getVolatility(aExchange, aSymbol);
            double rate = getAnnualRate(aExchange, aSymbol);
            
            myOptionMarketSnapshot = new OptionMarketSnapshot(aExchange,
                    aSymbol, basePrice, volatility, rate);

            String[] parsedSource = mySource.toString().split(
                    "puts:\\[|\\],calls:\\[|\\],underlying_id|<[.*]}\\],");
            String dataSetOne = parsedSource[1];
            String dataSetTwo = parsedSource[2];

            parsedSource = dataSetOne.split("\\},\\{|\\{|\\}");
            for (String myGoogleMarket : parsedSource) {
                // System.out.println(myGoogleMarket);
                OptionMarket myOptionMarket = convertToOptionMarket(myGoogleMarket);
                if (myOptionMarket != null) {
                    myOptionMarketSnapshot.addOptionMarket(myOptionMarket);
                }
            }

            parsedSource = dataSetTwo.split("\\},\\{|\\{|\\}");
            for (String myGoogleMarket : parsedSource) {
                // System.out.println(myGoogleMarket);
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
