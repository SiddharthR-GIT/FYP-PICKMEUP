import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.print.attribute.standard.Destination;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/Passenger")
public class Passenger extends HttpServlet {


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String ckName = null;
        String ckValue = null;
        String dbName = System.getProperty("RDS_DB_NAME");
        String userName = System.getProperty("RDS_USERNAME");
        String password = System.getProperty("RDS_PASSWORD");
        String hostname = System.getProperty("RDS_HOSTNAME");
        String port = System.getProperty("RDS_PORT");
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;

        PrintWriter pr = response.getWriter();
        Logger log = Logger.getLogger(Passenger.class.getName());

        String origin = request.getParameter("OriginIn");
        String destination = request.getParameter("DestinationIn");


        String xmlGen = new DistanceMatrixXMLGenerator().urlDistance(origin,destination);
        String distance2Destination = DistanceMatrixDOMParser.getDistance(xmlGen);// getting the distance  raw value and converting to km distance from XML File
        String journeyDuration = DistanceMatrixDOMParser.getJourneyDuration(xmlGen);//getting duration value from XML File


        Cookie ck [] = request.getCookies();
        for (Cookie aCk : ck) {
            ckName = aCk.getName();
            ckValue = aCk.getValue();
        }


        pr.println("<html><head><title>PICKMEUP</title></head><body>");
        pr.println("<p>" + ckName + "," + ckValue + "</p>");

        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);  // load the driver
            Connection connection = DriverManager.getConnection(jdbcUrl);

            pr.println("<p>Journey Distance" + distance2Destination + " KM</p>");
            pr.println("<p>Journey Duration" + journeyDuration + "</p>");
            pr.println("<p>Origin: " + origin + "\n    Destination: " + destination + "</p>");

            PreparedStatement getDetails = connection.prepareStatement("SELECT * FROM peopleDetails");
            ResultSet rs = getDetails.executeQuery();
            pr.println("<p>\t\t        First Name\t\t       Last Name\t\t       StartPoint\t\t        EndPoint</p>");
            while (rs.next()) {
                if (rs.getString("Title").equals("Driver") || rs.getString("Title").equals("driver")) {
                    if (rs.getString("Origin").equals(origin) || rs.getString("Destination").equals(destination)){
                        pr.println("<p>"
                                + "        " + rs.getString("First_Name")
                                + "        " + rs.getString("Last_name")
                                + "        " + rs.getString("Origin")
                                + "        " + rs.getString("Destination") + "<form><input type=\"button\" value=\"Chat\" onclick=\"window.location.href='https://afternoon-basin-90601.herokuapp.com/'\"/></form>"
                                /*+ "<iframe src=\"https://afternoon-basin-90601.herokuapp.com/\" width=\"100%\" height=\"300\">"
                                + "    <p>Your browser does not support iframes.</p>"
                                + "</iframe>"*/
                                + "</p></body></html>");
                    }
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
