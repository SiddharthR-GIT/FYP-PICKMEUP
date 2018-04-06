import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    Connection connection = null;
    PreparedStatement find;

    public Main() throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/Details?autoReconnect=true&useSSL=false";
        Class.forName(driver);  // load the driver
        connection = DriverManager.getConnection(url, "root", "password");
        Scanner sc = new Scanner(System.in);
        String password = sc.nextLine();


        try {

            find = connection.prepareStatement("SELECT *" + "FROM Login WHERE Email =?,Title =?,passKey=?");


        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            find.setString(1,password);
            ResultSet rs = find.executeQuery();
            if(rs.next()){
                System.out.println();rs.getString("passKey");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
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
    public String checkTitle(String password){


        return null;
    }
}
