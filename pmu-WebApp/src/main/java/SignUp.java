import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    private PreparedStatement find, find2;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        String Email = request.getParameter("Email");
        String LoginEmail = request.getParameter("Email");
        String Title = request.getParameter("Title");
        String loginTitle = request.getParameter("Title");
        String Password = request.getParameter("Password");
        String hashPass = sha_256(Password);
        String originInput = request.getParameter("Origin");
        String destinationInput = request.getParameter("Destination");
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

            Connection connection = DriverManager.getConnection(jdbcUrl);

            //data to enter the sign up page
            find = connection.prepareStatement("SELECT * FROM peopleDetails WHERE Email =?");
            find2 = connection.prepareStatement("SELECT * FROM Login WHERE Email=?");

            int result = checkSignDB(Email);

            if (result == 0) { //error
                pr.println("<html><head><title>PICKMEUP</title></head><body>");
                pr.println("<p>Person Already exist in the database </p></body></html>");
                pr.flush();
            }
            else {//insert new person
                try {
                    Statement statement = connection.createStatement();
                    String sqlStatement = "INSERT INTO peopleDetails(First_Name,Last_Name,Email,Title,passKey,Origin,Destination) " +
                            "VALUES ('" + first_name + "','" + last_name + "','" + Email + "','" + Title + "','" + hashPass + "','"+ originInput+"','"+ destinationInput+"')";

                    statement.executeUpdate(sqlStatement);

                    Statement statement2 = connection.createStatement();
                    String sqlStatement2 = "INSERT INTO Login(Email,Title,passKey) " +
                            "VALUE('" + LoginEmail + "','" + loginTitle + "','" + hashPass + "')";

                    statement2.executeUpdate(sqlStatement2);
                    log.info("Before");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("login.html");
                    dispatcher.forward(request,response);
                    log.info("After");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public int checkSignDB( String Email) {
        try {
            find.setString(1,Email);
            ResultSet rs = find.executeQuery();
            if (rs.next()) {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;//direct to next page
    }

    public static String sha_256(String password){

        try{
            MessageDigest msg = MessageDigest.getInstance("SHA-256");
            byte[] hash = msg.digest(password.getBytes("UTF-8"));
            StringBuilder hexStr = new StringBuilder();

            for (byte aHash : hash) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) hexStr.append('0');
                hexStr.append(hex);
            }
            return hexStr.toString();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
