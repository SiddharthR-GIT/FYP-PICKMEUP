import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    private Connection connection;
    private Statement statement;
    private PreparedStatement find;

    {
        try {
            find = connection.prepareStatement("SELECT *" + "FROM Login WHERE Email =?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String Email = request.getParameter("Email");
        String Password = request.getParameter("Password");
        String hashedPasskey =HashPassword.sha_256(Password);
        PrintWriter pr = response.getWriter();

        Logger log = Logger.getLogger(Connection.class.getName());
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/Details?autoReconnect=true&useSSL=false";
            Class.forName(driver);  // load the driver
            connection = DriverManager.getConnection(url,"root","password");
            log.info("Entered Databse");
            //data to enter the sign up page
            int result = checkLoginDB(Email); // checking for duplicate email
            if(result == 0){
                pr.println("<html><head><title>PICKMEUP</title></head><body>");
                pr.println("<p>That email already in the Database </p></body></html>");
                log.warning("Person Already Exist");
                pr.flush();
            }
            else {
                try {
                    statement = connection.createStatement();
                    String sqlStatement = "INSERT INTO Login(Email, passKey) VALUES('" + Email + "','" + hashedPasskey + "')";
                    statement.executeUpdate(sqlStatement);
                    log.info("Inserted");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public int checkLoginDB(String Email) {
        try {
            ResultSet rs = find.executeQuery();
            if (rs.next()) {
                return 0;//Show insertion error in DB
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;//direct to next page
    }
}
