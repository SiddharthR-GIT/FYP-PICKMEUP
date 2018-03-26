package hashStrCompare;

import java.security.MessageDigest;

public class stringHashCompare {
    public static void main(String[] args) {

        String password = "Password";
        String password1 = "Password";
        String password2= "John";
        String password3 = "Sid";

        String hp1 = sha_256(password1);
        String hp = sha_256(password);
        String hp2 = sha_256(password2);
        String hp3= sha_256(password3);

        if(hp.equals(hp1)){
            System.out.println("The are the same hash strings");
        }
        else{
            System.out.println("They are not the same hash string");
        }

        if(hp2.equals(hp3)){
            System.out.println("The are the same hash strings");

        }
        else{
            System.out.println("They are not the same hash string");

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
}
