package DistanceMatrixInXML;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;


public class distanceObject {
    private static final String API_KEY = "AIzaSyCFreEK4Ur8T7aV3CRG7pwSbKvfaT89YpQ";
    private static final String TARGET_XML_URL =
            "https://maps.googleapis.com/maps/api/distancematrix/xml?units=Metric&origins=Galway,Ireland&destinations=Tuam" +
                    "&key=" + API_KEY;


    private static final String XML_FILENAME = "/Users/sid/Desktop/5th year/Project/TEST4AWS/Jython/src/distance.xml";

    FileWriter fw = null;
    BufferedWriter bw = null;

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

            fw = new FileWriter(XML_FILENAME);
            bw = new BufferedWriter(fw);
            bw.write(respXml);

            System.out.println("Done");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }

    public static void main(String[] args) {
        new distanceObject();
        //ReadXML.readJSONObject(JSON_FILENAME);
    }

}