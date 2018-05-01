package DistanceMatrixInXML;

import com.DOMParseXML.ReadXML;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;


public class distanceObject {
    private static final String API_KEY = "AIzaSyCFreEK4Ur8T7aV3CRG7pwSbKvfaT89YpQ";
    private static final String TARGET_XML_URL =
            "https://maps.googleapis.com/maps/api/distancematrix/xml?units=Metric&origins=Letterkenny,Ireland&destinations=Sligo" +
                    "&key=" + API_KEY;


    public distanceObject() {
        try {
            URL url = new URL(TARGET_XML_URL);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/xml");
            httpURLConnection.setDoOutput(true);
            String response = httpURLConnection.getResponseMessage();

            System.out.println("Response is " + response.toString());


            if (httpURLConnection.getInputStream() == null) {
                System.out.println("No stream");
            }

            Scanner httpResponseScanner = new Scanner(httpURLConnection.getInputStream());
            String respXml = "";

            while (httpResponseScanner.hasNext()) {

                String line = httpResponseScanner.nextLine();
                if (line.contains("text") && line.length() > 100) {
                    System.out.println(line.length());
                }
                respXml += line;

            }
            new ReadXML().DOMParseXML(respXml);


            System.out.println("Done");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new distanceObject();
    }
}