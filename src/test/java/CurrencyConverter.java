import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Currency converter class accesses currency exchange rate data
 * using the currencylayer REST API and allows converting the USD to ILS
 * according to the most recent exchange rate.
 */

public class CurrencyConverter {
    //The currency exchange data url - currencylayer REST API
    public static final String CURRENCY_API_LINK = "http://api.currencylayer.com/live?access_key=4924481b96598eb4bde460c735935ab3";


    public static void main(String[] args) throws IOException {
        double xchgRate = 0.0;  //retrieved exchange rate
        double amount = 0.0;    //amount of USD to convert
        String jsonData;        //retrieved JSON data

        // use OKHttp client to create the connection and retrieve data
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.currencylayer.com/live?access_key=4924481b96598eb4bde460c735935ab3")
                .build();
        Response response = client.newCall(request).execute();
        jsonData = response.body().string();

        //call the getUSD2ILSRate method to fetch the USD to ILS exchange rate and handle the JSONException
        try {
            xchgRate = getUSD2ILSRate(jsonData);
        }catch (JSONException e){
            System.out.println("Could not get conversion rate. Please try again later.");
        }

        //Print the welcome message
        printWelcomeMsg();
        //get from the user an amount to convert
        amount = getUserInput();

        //print an output
        System.out.printf("%n1. 1 Dollar equals to %f Shekels.",xchgRate);
        System.out.printf("%n2. User entered an amount of %.2f USD.", amount);
        System.out.printf("%n3. The converted amount is %.2f ISL (%f*%.2f).%n", amount*xchgRate, xchgRate, amount );
    }

    /**
     * Prints the welcome message
     */
    public static void printWelcomeMsg(){
        System.out.println("Welcome to currency converter");
    }

    /**
     * Gets an amount as a console user input, using Scanner class object.
     * In the case of incorrect input type the application keeps requesting until
     * the correct input is provided.
     * @see InputMismatchException
     * @return amount as a double value
     */
    public static double getUserInput(){
        System.out.printf("%nPlease enter an amount of Dollars to convert: ");

        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextDouble();
            }catch (InputMismatchException e){
                System.out.printf("%nIncorrect amount. Please try again: ");
            }
        }
    }

    /**
     * parse JSON data and fetch USD-to-ILS conversion rate using USDILS key.
     * @param jsonData  - JSON data body
     * @return double value representing an actual USD to ILS exchange rate
     * @throws JSONException
     */
    public static double getUSD2ILSRate(String jsonData) throws JSONException{
            // parse JSON
            JSONObject mainJsonObject = new JSONObject(jsonData);
            // get Json object by key "quotes"
            JSONObject resultsJson = mainJsonObject.getJSONObject("quotes");
            // get value by the key "USDILS" and return it to a caller
            return resultsJson.getDouble("USDILS");
    }
}

