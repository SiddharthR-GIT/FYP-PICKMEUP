import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;

@WebServlet(urlPatterns = "/Passenger")
public class Passenger extends HttpServlet {
    private PreparedStatement find,getDetails;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String origin = request.getParameter("OriginIn");
        String destination = request.getParameter("DestinationIn");


        String xmlGen = new DistanceMatrixXMLGenerator().urlDistance(origin,destination);

        String distance2Destination = DistanceMatrixDOMParser.getDistance(xmlGen);// getting the distance  raw value and converting to km distance from XML File
        String journeyDuration = DistanceMatrixDOMParser.getJourneyDuration(xmlGen);//getting duration value from XML File

        String ckName = null;
        String ckValue = null;
        Cookie ck [] = request.getCookies();
        for(int i=0;i<ck.length;i++){
            ckName = ck[i].getName();
            ckValue = ck[i].getValue();
        }


        PrintWriter pr = response.getWriter();
        Logger log = Logger.getLogger(Passenger.class.getName());

        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);  // load the driver
            String url = "jdbc:mysql://localhost:3306/Details?autoReconnect=true&useSSL=false";
            Connection connection = DriverManager.getConnection(url, "root", "password");

            find = connection.prepareStatement("SELECT * FROM peopleDetails WHERE Origin=?");
            getDetails=connection.prepareStatement("SELECT * FROM peopleDetails ");

            pr.println("<html><head><title>PICKMEUP</title></head><body>");
            pr.println("<p>" + ckName + "," + ckValue + "</p>");
            pr.println("<p>Journey Distance" + distance2Destination + " KM</p>");
            pr.println("<p>Journey Duration" + journeyDuration + "</p>");
            pr.println("<p>Origin: " + origin + "    Destination: " + destination + "</p></body></html>");
            log.info("Getting the values");

            ResultSet rs = getDetails.executeQuery();
            pr.println("<p>        First Name        Last Name        Title        StartPoint        EndPoint");

            while (rs.next()) {
                if (rs.getString("Title").equals("Driver") || rs.getString("Title").equals("driver")){
                    pr.println("<p>"
                            + "        " + rs.getString("First_Name")
                            + "        " + rs.getString("Last_name")
                            + "        " + rs.getString("Title")
                            + "        " + rs.getString("Origin")
                            + "        " + rs.getString("Destination")
                            + "<iframe src=\"https://afternoon-basin-90601.herokuapp.com/\" width=\"100%\" height=\"300\">"
                            + "    <p>Your browser does not support iframes.</p>"
                            + "</iframe>"
                            + "</p></body></html>");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            pr.flush();
            pr.close();
        }
    }
}
