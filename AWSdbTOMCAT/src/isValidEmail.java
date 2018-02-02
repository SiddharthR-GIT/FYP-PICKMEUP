import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class isValidEmail {
    public static boolean isValidEmailAddress(String email){
        boolean feedback = true;

        try{
            InternetAddress emailAdd = new InternetAddress(email);
            emailAdd.validate(); // valid the email if its real or fake
            System.out.println("Email is valid");

        } catch (AddressException e) {
            e.printStackTrace();
            System.out.println("Email is  NOT valid");
            feedback = false;
        }
        return feedback;
    }
}
