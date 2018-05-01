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
import org.jetbrains.annotations.Nullable;

/**
 *
 */
@WebServlet(urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    private PreparedStatement findPassword,findTitle,findName;

    /**
     * @param request
     * @param response
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String loginEmail = request.getParameter("Email");
        String loginPassword = request.getParameter("Password");
        String title = request.getParameter("Title");
        String hashedPasskey = SignUp.sha_256(loginPassword);

        String dbName = System.getProperty("RDS_DB_NAME");
        String userName = System.getProperty("RDS_USERNAME");
        String password = System.getProperty("RDS_PASSWORD");
        String hostname = System.getProperty("RDS_HOSTNAME");
        String port = System.getProperty("RDS_PORT");
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;

        PrintWriter pr = response.getWriter();
        Logger log = Logger.getLogger(Connection.class.getName());
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);  // load the driver
            Connection connection = DriverManager.getConnection(jdbcUrl);

            findPassword = connection.prepareStatement("SELECT * FROM Login WHERE Email =?");
            findTitle = connection.prepareStatement("SELECT * FROM Login WHERE Title=?");
            findName = connection.prepareStatement("SELECT *FROM peopleDetails WHERE Email=?");

            //data to enter the sign up page
            String checkPasswords = checkLoginDB(loginEmail); // checking for duplicate email
            String checkTitle = checkTitle(title);
            String getName = getName(loginEmail);

            assert checkPasswords != null;
            if (checkPasswords.equals(hashedPasskey)) {

                assert checkTitle != null;
                if (checkTitle.equals("Passenger") || checkTitle.equals("passenger")) {

                    assert getName != null;

                    HttpSession session = request.getSession();
                    session.setAttribute("name", getName.toUpperCase());

                    RequestDispatcher dispatcher = request.getRequestDispatcher("Passenger.html");
                    dispatcher.forward(request, response);
                    log.info("Here");
                } else if (checkTitle.equals("Driver") || checkTitle.equals("driver")) {

                    assert getName != null;
                    HttpSession session = request.getSession();
                    session.setAttribute("name", getName.toUpperCase());

                    RequestDispatcher dispatcher = request.getRequestDispatcher("Driver.html");
                    dispatcher.forward(request, response);

                    log.info("Driver page1");
                } else {
                    pr.println("<h2> User Name or Password do not match, please try again</h2>");
                    pr.println("<a href=\"login.html\">\n" +
                            "   <button>Back</button>\n" +
                            "</a>");


                }
            }else{
                pr.println("<h2> User Name or Password do not match, please try again</h2>");
                pr.println("<a href=\"login.html\">\n" +
                        "   <button>Back</button>\n" +
                        "</a>");

            }
        } catch (Exception e) {

            pr.println(e.getMessage());
        }
        finally {
            pr.flush();
            pr.close();
        }
    }

    /**
     * @param LoginEmail
     * @return
     */
    @Nullable
    private String checkLoginDB(String LoginEmail) {
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
        return null;//direct to previous page
    }

    /**
     * @param title
     * @return
     */
    @Nullable
    private String checkTitle(String title) {
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

    /**
     * @param email
     * @return
     */
    @Nullable
    private String getName(String email){
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
    /**
     *
     */
    @Override
    public void destroy(){
        Logger log = Logger.getLogger(Driver.class.getName());
        log.info("Destroy Login Servlet");
    }
}
