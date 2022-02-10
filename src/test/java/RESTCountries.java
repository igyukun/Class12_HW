import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * RESTCountries class accesses the country geo-economical data using restcountries REST API
 * and returns its region, common borders and currency symbol.
 * The class provides a simple console-base UI to get from user a country name and return him
 * retrieved information.
 */
public class RESTCountries {
    //the URL to the REST resource with the world countries information
    public static final String COUNTRIES_URL = "https://restcountries.com/v3.1/all";


    public static void main(String[] args) throws IOException, JSONException {
        // use OKHttp client to create the connection and retrieve data
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(COUNTRIES_URL).build();
        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();

        // Create JSON array using the top-level data
        JSONArray countries = new JSONArray(jsonData);

        System.out.println("Welcome to geographical reference app.");

        //Main outer loop, which iterates the entire flow until user inputs 'exit' command
        while (true) {
            boolean found = false; //indicates whether the country has been found

            //get the country name from user
            String countryname = getUserInput();

            //check the exit command and terminate the application if given
            if (countryname.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using our geographical reference app.");
                break;
            }
            //Iterate through the top-level JSON array, search for the requested country
            //and fetch its data
            for (int i = 0; i < countries.length(); i++) {
                //get a country element from the array
                JSONObject jobject = countries.getJSONObject(i);
                //check if the 'name.common' key value matches the requested country
                if (jobject.getJSONObject("name").
                          getString("common").
                          equalsIgnoreCase(countryname)){
                    //Set found indicator to true
                    found = true;
                    //call the country region printout
                    printRegion(jobject);
                    //call the common borders list printout
                    printBorders(jobject);
                    //call the currency symbol/s printout
                    printCurrSymbol(jobject);
                }
            }
            System.out.println();
            //Prompt if the country name was not found in the list
            if (!found)
                System.out.printf("%nNo such country.");
        }
    }

    /**
     * Gets a country name from a user
     * @return the country name as a String
     */
    public static String getUserInput(){
        System.out.print("\nEnter the country name (type 'exit' to terminate): ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Fetch and print the region value
     * @param jobject - the country branch of the JSON document
     */
    public static void printRegion(JSONObject jobject){
        System.out.printf("%n    The region is: " + jobject.getString("region"));
    }

    /**
     * Fetch and print the common borders list. The method handles a situation when
     * the requested country does not have common borders, e.g. Australia.
     * @param jobject - the country branch of the JSON document
     */
    public static void printBorders(JSONObject jobject){
        try {
            //Get an array of the common borders
            JSONArray borders = jobject.getJSONArray("borders");
            System.out.printf("%n    The borders are with:");

            //Iterate through the array and print the borders
            for (int i = 0; i < borders.length(); i++) {
                String bord = borders.getString(i);
                System.out.print(" " + borders.getString(i) + " ");
            }
        }catch (JSONException e){
            //The exception is generated when there are no official common borders (e.g. Australia)
            System.out.printf("%n    No official common borders.");
        }
    }

    /**
     * Prints country currency symbol/s.
     * @param jobject - the country branch of the JSON document
     */
    public static void printCurrSymbol(JSONObject jobject){
        //Fetch the currencies branch
        JSONObject currencies = jobject.getJSONObject("currencies");
        System.out.printf("%n    The currency symbol/s: ");
        //Since the child of the "currencies" is not known, the Iterator object should be used
        //to fetch its name
        Iterator iterator = currencies.keys();
        //Iterate through the Iterator object elements and get the keys
        //Using the fetched key create, get a new JSON object and print the symbol value
        while (iterator.hasNext()) {
            String key = (String)iterator.next();
            System.out.println(currencies.getJSONObject(key).getString("symbol")+" ");
        }
    }
}
