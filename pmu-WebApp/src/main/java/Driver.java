import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 */
@WebServlet(urlPatterns = "/Driver")
public class Driver extends HttpServlet {


    /**
     * @param request-
     * @param response-
     * @throws IOException-
     * @throws ServletException-
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        String dbName = System.getProperty("RDS_DB_NAME");
        String userName = System.getProperty("RDS_USERNAME");
        String password = System.getProperty("RDS_PASSWORD");
        String hostname = System.getProperty("RDS_HOSTNAME");
        String port = System.getProperty("RDS_PORT");
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName +
                            "?user=" + userName + "&password=" + password;

        PrintWriter pr = response.getWriter();

        Logger log = Logger.getLogger(Driver.class.getName());

        String origin = request.getParameter("OriginIn");
        String destination = request.getParameter("DestinationIn");

        String xmlGen = new DistanceMatrixXMLGenerator().urlDistance(origin,destination);
        String distance2Destination = DistanceMatrixDOMParser.getDistance(xmlGen);// getting the distance  raw value and converting to km distance from XML File
        String journeyDuration = DistanceMatrixDOMParser.getJourneyDuration(xmlGen);//getting duration value from XML File


        HttpSession session = request.getSession(false); // false as session has already been created
        if(session !=null){
            String sessionName = (String) session.getAttribute("name");
            pr.println("<html><head><title>Pick Me Up</title></head><body>");
            pr.println("<div class = \"topnav\">" +
                    "<a class = \"active\"> Hi "+sessionName+"</a>" +
                    "<a>Origin: " + origin +"</a>" +
                    "<a>Destination: " + destination +"</a>" +
                    "<a>Journey Distance: "+distance2Destination+"Km</a>" +
                    "<a>Duration: "+journeyDuration+"</a>" +
                    "<a href =\"logout.jsp\">Log Out</a>" +
                    "</div>");

            pr.println(" <link rel=\"stylesheet\" href=\"tableStyle.css\">");

            try{
                getActorsAttributes(jdbcUrl, pr, origin, destination);

            }catch(Exception e){
                e.printStackTrace();
            }finally {
                pr.flush();
                pr.close();
            }
        }
        else{
            pr.println("<h2><font color=\"red\">Please Login first!</font></h2>");
            request.getRequestDispatcher("login.html").include(request,response);
            pr.flush();
            pr.close();
        }
    }

    /**
     * Explanation:
     * @param jdbcUrl - URL to connect AWS Relational Database Service
     * @param pr- print on the webpage
     * @param origin-Origin of the Driver
     * @param destination-Destination of the Driver
     * @throws ClassNotFoundException - method being called in
     * @throws SQLException-
     */
    static void getActorsAttributes(String jdbcUrl, PrintWriter pr, String origin, String destination) throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.jdbc.Driver";
        Class.forName(driver);  // load the driver
        Connection connection = DriverManager.getConnection(jdbcUrl);

        PreparedStatement getDetails = connection.prepareStatement("SELECT * FROM peopleDetails");
        ResultSet rs = getDetails.executeQuery();

        pr.println("<table id=\"t01\">");
        pr.println("<tr><th>First Name</th><th>Surname</th><th>Place of Origin</th><th>Destination</th><th>Service</th></tr>");


        while (rs.next()) {
            if (rs.getString("Title").equals("Passenger") || rs.getString("Title").equals("passenger")) {
                if (rs.getString("Origin").equals(origin) && rs.getString("Destination").equals(destination)) { // same origin and same destination
                    pr.println("<tr>"
                            + "<td>" + rs.getString("First_Name") + "</td>"
                            + "<td>" + rs.getString("Last_name") + "</td>"
                            + "<td>" + rs.getString("Origin") + "</td>"
                            + "<td>" + rs.getString("Destination") + "</td><td><form><input type=\"button\" value=\"Chat\" onclick=\"window.location.href='https://afternoon-basin-90601.herokuapp.com/'\"/></form></td>");
                }
            }
        }
        pr.println("</table></p></body></html>");
    }

    /**
     *
     */
    @Override
    public void destroy(){
        Logger log = Logger.getLogger(Driver.class.getName());
        log.info("Destroy Servlet");
    }
}
