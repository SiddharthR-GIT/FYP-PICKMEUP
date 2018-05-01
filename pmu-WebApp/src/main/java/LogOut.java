import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class LogOut extends HttpServlet {

    /**
     * @param request
     * @param response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");

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
