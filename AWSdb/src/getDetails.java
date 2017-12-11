import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class getDetails extends HttpServlet {
    private Connection connection;
    private Statement statement;
    private PreparedStatement lastID;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            /*String driver = "org.mysql.jdbc.Driver";
            String url = "jdbc:mysql://192.168.0.80/aws-database.czetjngd8wtj.eu-west-1.rds.amazonaws.com:3306/";
            Class.forName(driver);
            connection = DriverManager.getConnection(url+"Data","MasterUser", "password");*/

            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/SignUp?autoReconnect=true&useSSL=false";
            Class.forName(driver);  // load the driver
            connection = DriverManager.getConnection(url,"root","password");

            lastID = connection.prepareStatement("SELECT ID, First_Name, Last_Name, Email, Title"
                    + "FROM SignUpData"+ "WHERE ID = LAST_INSERT_ID()");

            /*lastID = connection.prepareStatement("SELECT ID, First_Name, Last_Name, Email, Title"
                    + "FROM SignUp"+ "WHERE ID = LAST_INSERT_ID()");*/
            
            statement = connection.createStatement();
        }catch(SQLException sql){
            sql.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}