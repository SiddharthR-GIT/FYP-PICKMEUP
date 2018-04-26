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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;

@WebServlet(urlPatterns = "/Passenger")
public class Passenger extends HttpServlet {
    private PreparedStatement find,getDetails;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        String origin = request.getParameter("OriginIn");
        String destination = request.getParameter("DestinationIn");


        String xmlGen = new DistanceMatrixXMLGenerator().urlDistance(origin,destination);
        String distance2Destination = DistanceMatrixDOMParser.getDistance(xmlGen);// getting the distance  raw value and converting to km distance from XML File
        String journeyDuration = DistanceMatrixDOMParser.getJourneyDuration(xmlGen);//getting duration value from XML File

        String sessionName = null;




        PrintWriter pr = response.getWriter();
        Logger log = Logger.getLogger(Passenger.class.getName());

        HttpSession session=request.getSession(false);// create is false as session has already been created
        if(session != null) {

            sessionName = (String)session.getAttribute("name");
            pr.println("<html><head><title>PICKMEUP</title></head><body>");
            pr.println("<p> HI ," + sessionName + "</p>");
            pr.println(" <link rel=\"stylesheet\" href=\"tableStyle.css\">");
            try {
                String driver = "com.mysql.jdbc.Driver";
                String url = "jdbc:mysql://localhost:3306/Details?autoReconnect=true&useSSL=false";
                Class.forName(driver);  // load the driver
                Connection connection = DriverManager.getConnection(url, "root", "password");

                pr.println("<p>Journey Distance" + distance2Destination + " KM</p>");
                pr.println("<p>Journey Duration" + journeyDuration + "</p>");
                pr.println("<p>Origin: " + origin + "\n    Destination: " + destination + "</p>");

                PreparedStatement getDetails = connection.prepareStatement("SELECT * FROM peopleDetails");
                ResultSet rs = getDetails.executeQuery();

                pr.println("<table id=\"t01\">");
                pr.println("<tr><th>First Name</th><th>Surname</th><th>Place of Origin</th><th>Destination</th><th>Service</th></tr>");


                while (rs.next()) {
                    if (rs.getString("Title").equals("Driver") || rs.getString("Title").equals("driver")) {
                        if (rs.getString("Origin").equals(origin) && rs.getString("Destination").equals(destination)) { // same origin and same destination
                            pr.println("<tr>"
                                    + "<td>" + rs.getString("First_Name") + "</td>"
                                    + "<td>" + rs.getString("Last_name") + "</td>"
                                    + "<td>" + rs.getString("Origin") + "</td>"
                                    + "<td>" + rs.getString("Destination") + "</td><td><form><input type=\"button\" value=\"Chat\" onclick=\"window.location.href='https://afternoon-basin-90601.herokuapp.com/'\"/></form></td>");
                        }
                    }
                }

                pr.println("<a href =\"LogOut\">Log Out</a>");
                pr.println("</table></p></body></html>");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                pr.flush();
                pr.close();
            }
        }
        else{
            pr.println("<h2>Please Login first</h2>");
            request.getRequestDispatcher("login.html").include(request,response);
            pr.flush();
            pr.close();
        }
    }
}
