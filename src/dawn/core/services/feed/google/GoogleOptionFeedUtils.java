package dawn.core.services.feed.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import dawn.core.data.market.Side;
import dawn.core.data.market.option.OptionMarket;
import dawn.core.data.market.option.OptionQuote;
import dawn.core.data.market.option.OptionMarketSnapshot;
import dawn.core.data.market.option.OptionType;

public class GoogleOptionFeedUtils {
    private static final String URL = "http://www.google.com/finance/option_chain?q=";

    private static OptionMarket convertToOptionMarket(String aGoogleMarket) {
        Double myBidPrice = null;
        Double myAskPrice = null;
        Double myStrike = null;
        OptionType myOptionType = null;
        Double myPrice = null;
        Double myChange = null;
        Integer myQuantity = null;
        Integer myOpenInterest = null;
        

        String[] myGoogleStatusTokens = aGoogleMarket.split(",");
        for (String myOutput : myGoogleStatusTokens) {
            if (myOutput.contains("vol:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myQuantity = Integer.parseInt(myOutput);
                }
            } else if (myOutput.contains("b:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myBidPrice = Double.parseDouble(myOutput);
                }
            } else if (myOutput.contains("a:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myAskPrice = Double.parseDouble(myOutput);
                }
            } else if (myOutput.contains("strike:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myStrike = Double.parseDouble(myOutput);
                }
            } else if (myOutput.contains("s:")) {
                if (myOutput.contains("C")) {
                    myOptionType = OptionType.CALL;
                } else if (myOutput.contains("P")) {
                    myOptionType = OptionType.PUT;
                }
            } else if (myOutput.contains("p:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myPrice = Double.parseDouble(myOutput);
                }
            } else if (myOutput.contains("c:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myChange = Double.parseDouble(myOutput);
                }
            } else if (myOutput.contains("oi:")) {
                myOutput = myOutput.replaceAll("[a-zA-Z]|:|-|\"| ", "");
                if (myOutput.length() > 0) {
                    myOpenInterest = Integer.parseInt(myOutput);
                }
            }
        }
        if (
                    myOptionType != null 
                    && myBidPrice != null 
                    && myAskPrice != null
                    && myStrike != null
                    && myPrice != null
                    && myChange != null
                    && myQuantity != null 
                    && myOpenInterest != null
    			) {
            OptionMarket myOptionMarket = 
                new OptionMarket(
        	        myOptionType,
        	        myStrike,
        	        new OptionQuote(Side.BID, myQuantity, myBidPrice),
        	        new OptionQuote(Side.ASK, myQuantity, myAskPrice),
        	        myPrice,
        	        myChange,
        	        myQuantity,
        	        myOpenInterest
				);
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

            String[] parsedSource = mySource.toString().split(
                    "puts:\\[|\\],calls:\\[|\\],underlying_id|<[.*]}\\],");
            String dataSetOne = parsedSource[1];
            String dataSetTwo = parsedSource[2];

            myOptionMarketSnapshot = new OptionMarketSnapshot(aExchange, aSymbol);

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
