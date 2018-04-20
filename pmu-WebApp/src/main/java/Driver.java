import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/Driver")
public class Driver extends HttpServlet {


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String dbName = System.getProperty("RDS_DB_NAME");
        String userName = System.getProperty("RDS_USERNAME");
        String password = System.getProperty("RDS_PASSWORD");
        String hostname = System.getProperty("RDS_HOSTNAME");
        String port = System.getProperty("RDS_PORT");
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;

        PrintWriter pr = response.getWriter();

        Logger log = Logger.getLogger(Driver.class.getName());

        String getOrigin = null;




    }
}
