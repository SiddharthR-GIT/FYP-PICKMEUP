import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LandingPage extends HttpServlet{

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html");
    }

    @Override
    public void destroy(){
        Logger log = Logger.getLogger(Driver.class.getName());
        log.info("Destroy Landing Page Servlet");
    }
}
