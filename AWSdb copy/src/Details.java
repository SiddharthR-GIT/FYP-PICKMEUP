import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/Details"})
public class Details extends HttpServlet {

    private Connection connection;
    private Statement statement;
    private PreparedStatement find;
    private boolean isEmailViable;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter pr = response.getWriter();


        try{

            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://rds-mysql-db.czetjngd8wtj.eu-west-1.rds.amazonaws.com:3306/Details?autoReconnect=true&useSSL=false";
            Class.forName(driver);  // load the driver
            connection = DriverManager.getConnection(url,"MasterUser","password");

            find = connection.prepareStatement("SELECT *" + "FROM peopleDetails");
            statement = connection.createStatement();
            isEmailViable = isValidEmailAddress(request.getParameter("Email"));

            if(isEmailViable == true) {
                String sqlStatement = "INSERT INTO peopleDetails (First_Name, Last_Name, Email, Title) " +
                        "VALUES("
                        +"'" + request.getParameter("first_name") +"'" +','
                        +"'" + request.getParameter("last_name")+"'" +','
                        +"'" + request.getParameter("Email") +"'"+ ','
                        +"'" + request.getParameter("Title") +"')";

                statement.executeUpdate(sqlStatement);
            }
            else{
                pr.println("<html><head><title>ERROR</title></head><body>");
                pr.println("<p>You Problem is that the Email is not valid </p>");
            }
        }catch(SQLException sql){
            sql.printStackTrace();
            pr.println("<html><head><title>ERROR</title></head><body>");
            pr.println("<p>You Problem is: " + sql + "</p>");
            pr.println(sql);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            pr.close();
            try{
                if(statement != null){
                    statement.close();
                }
                if (connection != null){
                    connection.close();
                }
            }catch(SQLException sql){
                sql.printStackTrace();
            }

        }
    }
    public static boolean isValidEmailAddress(String email){
        boolean feedback = true;

        try{
            InternetAddress emailAdd = new InternetAddress(email);
            emailAdd.validate(); // valid the email if its real or fake

        } catch (AddressException e) {
            e.printStackTrace();
            feedback = false;
        }
        return feedback;
    }
}