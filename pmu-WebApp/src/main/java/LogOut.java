import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogOut extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");
    }
}
