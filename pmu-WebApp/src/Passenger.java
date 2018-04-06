import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/Passenger")
public class Passenger extends HttpServlet {


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String origin = request.getParameter("OriginIn");
        String destination = request.getParameter("DestinationIn");

        new DistanceMatrixXMLGenerator(origin,destination); // generating an DistanceMatrixResponse XML

        String distance2Destination = DistanceMartixDOMParser.getDistance();// getting the distance  raw value and converting to km distance from XML File
        String journeyDuration = DistanceMartixDOMParser.getJourneyDuration();//getting duration value from XML File

        String ckName = null;
        String ckValue = null;
        Cookie ck [] = request.getCookies();
        for(int i=0;i<ck.length;i++){
             ckName = ck[i].getName();
            ckValue = ck[i].getValue();
        }


        PrintWriter pr = response.getWriter();
        Logger log = Logger.getLogger(Passenger.class.getName());

        pr.println("<html><head><title>PICKMEUP</title></head><body>");
        pr.println("<p>" + ckName + "," + ckValue + "</p>");
        pr.println("<p>Journey Distance" + distance2Destination + " KM</p>");
        pr.println("<p>Journey Duration" + journeyDuration + "</p>");
        pr.println("<p>Origin: " + origin + "    Destination: " + destination + "</p></body></html>");
        log.info("Getting the values");
        pr.flush();
        pr.close();
    }
}
