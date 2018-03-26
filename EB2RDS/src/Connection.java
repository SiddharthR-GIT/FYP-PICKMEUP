import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/Connection"})
public class Connection extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        getRemoteConnection();

    }
    private  Connection getRemoteConnection () {
        Logger logger = Logger.getLogger(Connection.class.getName());
        if (System.getProperty("RDS_HOSTNAME") != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String dbName = System.getProperty("RDS_DB_NAME");

                String userName = System.getProperty("RDS_USERNAME");
                String password = System.getProperty("RDS_PASSWORD");
                String hostname = System.getProperty("RDS_HOSTNAME");
                String port = System.getProperty("RDS_PORT");

                String Url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
                logger.info("Getting remote connection with connection string from environment variables.");
                java.sql.Connection con = DriverManager.getConnection(Url);
                logger.info("Remote connection successful.");
                return (Connection) con;
            } catch (ClassNotFoundException e) {
                logger.warning(e.toString());
            } catch (SQLException e) {
                logger.warning(e.toString());
            }
        }
        return null;
    }
}
