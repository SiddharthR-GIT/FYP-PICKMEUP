import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    private Connection connection;
    private PreparedStatement find;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String loginEmail = request.getParameter("Email");
        String loginPassword = request.getParameter("Password");
        String hashedPasskey =HashPassword.sha_256(loginPassword);
        PrintWriter pr = response.getWriter();

        String dbName = System.getProperty("RDS_DB_NAME");
        String userName = System.getProperty("RDS_USERNAME");
        String password = System.getProperty("RDS_PASSWORD");
        String hostname = System.getProperty("RDS_HOSTNAME");
        String port = System.getProperty("RDS_PORT");
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" +
                port + "/" + dbName + "?user=" + userName + "&password=" + password;

        Logger log = Logger.getLogger(Connection.class.getName());
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);  // load the driver
            connection = DriverManager.getConnection(jdbcUrl);

            find = connection.prepareStatement("SELECT * FROM Login WHERE Email =?");
            //data to enter the sign up page
            String checkPasswords = checkLoginDB(loginEmail); // checking for duplicate email
            if(checkPasswords.equals(hashedPasskey)){
                pr.println("<html><head><title>PICKMEUP</title></head><body>");
                pr.println("<h2>Password match</h2></body></html>");
                pr.flush();
            }
            else {
                pr.println("<html><head><title>PICKMEUP</title></head><body>");
                pr.println("<h2>ERROR - no match password or email</h2></body></html>");
                pr.flush();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String checkLoginDB(String LoginEmail) {
        try {
            find.setString(1,LoginEmail);
            ResultSet rs = find.executeQuery();
            if (rs.next()) {
                //email exist
                return rs.getString(4);//next page

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;//direct to next page
    }
}
