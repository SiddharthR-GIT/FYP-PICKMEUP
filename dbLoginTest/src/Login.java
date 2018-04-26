import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    private Connection connection;
    private PreparedStatement findPassword,findTitle,findName;



    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String loginEmail = request.getParameter("Email");
        String title = request.getParameter("Title");
        String Password = request.getParameter("Password");
        String hashedPasskey = SignUp.sha_256(Password);
        PrintWriter pr = response.getWriter();



        Logger log = Logger.getLogger(Connection.class.getName());
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);  // load the driver
            String url = "jdbc:mysql://localhost:3306/Details?autoReconnect=true&useSSL=false";
            connection = DriverManager.getConnection(url, "root", "password");

            findPassword = connection.prepareStatement("SELECT * FROM Login WHERE Email =?");
            findTitle = connection.prepareStatement("SELECT * FROM Login WHERE Title=?");
            findName = connection.prepareStatement("SELECT *FROM peopleDetails where Email=?");
            //data to enter the sign up page
            String checkPasswords = checkLoginDB(loginEmail); // checking for duplicate email
            String checkTitle = checkTitle(title);
            String getName = getName(loginEmail);



            if (checkPasswords.equals(hashedPasskey)) {
                if (checkTitle.equals("Passenger") || checkTitle.equals("passenger")) {

//                    Cookie ck =new Cookie("Hi ",getName.toUpperCase());
//                    response.addCookie(ck);
                    HttpSession session =request.getSession();
                    session.setAttribute("name",getName.toUpperCase());

                    RequestDispatcher dispatcher = request.getRequestDispatcher("Passenger.html");
                    dispatcher.forward(request, response);
                    log.info("Here");
                } else {
                    /*RequestDispatcher dispatcher = request.getRequestDispatcher("Driver.html");
                    dispatcher.forward(request,response);*/

                    log.info("Driver page1");
                }
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("Login.html");
                dispatcher.include(request, response);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String checkLoginDB(String LoginEmail) {
        try {
            findPassword.setString(1,LoginEmail);
            ResultSet rs = findPassword.executeQuery();
            if (rs.next()) {
                //email exist
                return rs.getString(4);//next page

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;//direct to next page
    }
    public String checkTitle(String title) {
        try {
            findTitle.setString(1,title);
            ResultSet rs = findTitle.executeQuery();
            if (rs.next()) {
                //email exist
                return rs.getString("Title");//next page

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;//request was unsuccessful
    }
    public String getName(String email){
        try {
            findName.setString(1,email);
            ResultSet rs = findName.executeQuery();
            if(rs.next()){
                return rs.getString("First_Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;//
    }
}




