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
import org.jetbrains.annotations.Nullable;

/**
 *
 */
@WebServlet(urlPatterns = "/Passenger")
public class Passenger extends HttpServlet {

    private PreparedStatement findEmail;


    /**
     * @param request-creates  an HttpServletRequest object and passes it as an argument to the servlet's service methods
     * @param response-creates an HttpServletResponse object and passes it as an argument to the servlet's service methods
     * @throws IOException-Signals      that an I/O exception of some sort has occurred.
     * @throws ServletException-Signals that there is an error in the servlet
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

        Logger log = Logger.getLogger(Passenger.class.getName());


        HttpSession sessionPassenger = request.getSession(false); // false as session has already been created
        String origin = request.getParameter("OriginIn");
        String destination = request.getParameter("DestinationIn");

        try {

            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);  // load the driver

            Connection connection = DriverManager.getConnection(jdbcUrl);
            PreparedStatement getDetails = connection.prepareStatement("SELECT * FROM peopleDetails");
            findEmail = connection.prepareStatement("SELECT * FROM peopleDetails WHERE Email =?");



            if (sessionPassenger != null) {
                String sessionName = (String) sessionPassenger.getAttribute("name");
                log.info(" pass here before");
                PreparedStatement pst = connection.prepareStatement("UPDATE peopleDetails SET Origin = ?, Destination=? WHERE First_Name = ?");
                pst.setString(1, origin);
                pst.setString(2, destination);
                pst.setString(3, sessionName);
                String xmlGenerator = new DistanceMatrixXMLGenerator().urlDistance(origin, destination);
                String distance2Destination = DistanceMatrixDOMParser.getDistance(xmlGenerator);// getting the distance  raw value and converting to km distance from XML File
                String journeyDuration = DistanceMatrixDOMParser.getJourneyDuration(xmlGenerator);//getting duration value from XML File
                pst.executeUpdate();
                pst.close();

                log.info("before pr");

                pr.println("<html><head><title>Pick Me Up</title></head><body>");
                pr.println("<div class = \"topnav\">" +
                        "<a class = \"active\"> Hi " + sessionName + "</a>" +
                        "<a>Origin: " + origin + "</a>" +
                        "<a>Destination: " + destination + "</a>" +
                        "<a>Journey Distance: " + distance2Destination + "Km</a>" +
                        "<a>Duration: " + journeyDuration + "</a>" +
                        "<a href =\"logout.jsp\">Log Out</a>" +
                        "</div>");

                pr.println(" <link rel=\"stylesheet\" href=\"tableStyle.css\">");

                ResultSet rs = getDetails.executeQuery();
                pr.println("<table id=\"t01\">");
                pr.println("<tr><th>First Name</th><th>Surname</th><th>Place of Origin</th><th>Destination</th><th>Service</th></tr>");

                log.info("info outside");
                while (rs.next()) {
                    if (rs.getString("Title").equals("Driver") || rs.getString("Title").equals("driver")) {
                        log.info("here1");
                        if (rs.getString("Origin").equals(origin) && rs.getString("Destination").equals(destination)) { // same origin or  same destination
                            log.info("here1");
                            pr.println("<tr>"
                                    + "<td>" + rs.getString("First_Name") + "</td>"
                                    + "<td>" + rs.getString("Last_name") + "</td>"
                                    + "<td>" + rs.getString("Origin") + "</td>"
                                    + "<td>" + rs.getString("Destination") + "</td><td><form><input type=\"button\" value=\"Chat\" onclick=\"window.location.href='https://afternoon-basin-90601.herokuapp.com/'\"/></form></td>");
                        }
                    }
                }
                pr.println("</table></p></body></html>");
            } else {
                log.info("info fucked pass");
                pr.println("<h2><font color=\"red\">Please Login first!</font></h2>");
                request.getRequestDispatcher("login.html").include(request, response);
                pr.flush();
                pr.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pr.flush();
            pr.close();
        }
    }


    /**
     *
     */
    @Override
    public void destroy() {
        Logger log = Logger.getLogger(Driver.class.getName());
        log.info("Destroy Driver Servlet");
    }


}
