import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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
        String title = request.getParameter("Title");
        String hashedPasskey =sha_256(loginPassword);
        PrintWriter pr = response.getWriter();

        String dbName = System.getProperty("RDS_DB_NAME");
        String userName = System.getProperty("RDS_USERNAME");
        String password = System.getProperty("RDS_PASSWORD");
        String hostname = System.getProperty("RDS_HOSTNAME");
        String port = System.getProperty("RDS_PORT");
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;

        Logger log = Logger.getLogger(Connection.class.getName());
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);  // load the driver
            connection = DriverManager.getConnection(jdbcUrl);

            find = connection.prepareStatement("SELECT * FROM Login WHERE Email=?,Title=?,passkey=?");//data to enter the sign up page

            String checkPasswords = checkLoginDB(loginEmail,title,hashedPasskey); // checking for duplicate email
            String checkTitle = checkTitle(loginEmail,title,hashedPasskey);
            log.info("Here");
            pr.println("<html><head><title>PICKMEUP</title></head><body>");
            pr.println("<h2>Logged In</h2></body></html>");
            pr.flush();
            if(checkPasswords.equals(hashedPasskey)){
                pr.println("<html><head><title>PICKMEUP</title></head><body>");
                pr.println("<h2>Page in loop</h2></body></html>");
                pr.flush();
                response.sendRedirect("Passenger.html");
                Cookie ck = new Cookie("HI,",loginEmail);  // Creating cookie
                response.addCookie(ck);

                if(checkTitle.equals("Passenger".toLowerCase()) || checkTitle.equals("Passenger".toUpperCase()) || checkTitle.equals("Passenger")){

                    log.info("Passenger Page");
                    //response.sendRedirect("Passenger.html");
                    pr.println("<h2>Page in loop</h2>");
                    pr.flush();
                }else{

                    log.info("Driver Page");
                    response.sendRedirect("Driver.html");
                    pr.println("<html><head><title>PICKMEUP</title></head><body>");
                    pr.println("<h2>Driver page</h2></body></html>");
                    pr.flush();
                }
            }
            else {
                pr.println("<html><head><title>PICKMEUP</title></head><body>");
                pr.println("<h2>ERROR - no match password or email,Press back</h2></body></html>");
                pr.flush();

            }
        } catch (Exception e) {
            pr.flush();
            e.printStackTrace();
        }finally {
            pr.println("<html><head><title>PICKMEUP</title></head><body>");
            pr.println("<h2>close</h2></body></html>");
            pr.flush();
            pr.close();
        }

    }


    public String checkLoginDB(String LoginEmail,String Title,String hassPass) {
        try {
            find.setString(1,LoginEmail);
            find.setString(2,Title);
            find.setString(3,hassPass);
            ResultSet rs = find.executeQuery();
            if (rs.next()) {
                //email exist
                //System.out.println( rs.getString("passKey"));//return password coloum
                return rs.getString("passKey");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;//direct to next page
    }


    public String checkTitle(String LoginEmail,String Title,String hassPass){
        try{
            find.setString(1,LoginEmail);
            find.setString(2,Title);
            find.setString(3,hassPass);
            ResultSet rs = find.executeQuery();
            if(rs.next()){
                return rs.getString("Title");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public static String sha_256(String password){

        try{
            MessageDigest msg = MessageDigest.getInstance("SHA-256");
            byte[] hash = msg.digest(password.getBytes("UTF-8"));
            StringBuffer hexStr = new StringBuffer();

            for(int i = 0; i< hash.length;i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)hexStr.append('0');
                hexStr.append(hex);
            }
            return hexStr.toString();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
