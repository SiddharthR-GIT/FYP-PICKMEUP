/*
Inserting password into database which are hashed
 */
import java.security.MessageDigest;

public class HashPassword {
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
