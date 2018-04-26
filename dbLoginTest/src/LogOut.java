import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogOut extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pr= response.getWriter();

        request.getRequestDispatcher("link.html").include(request,response);

        HttpSession session = request.getSession();
        session.invalidate();


        pr.println("You have logged out");
        pr.println();
        pr.close();

    }
}
