<%@ page import="java.io.PrintWriter" %><%--
  Created by IntelliJ IDEA.
  User: sid
  Date: 26/04/2018
  Time: 16:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<form name="logout" method="post" action="/Logout">
<%
    PrintWriter pr = response.getWriter();
    pr.println("<h2 style=\"text=align:center;\">Logged Out</h2>");

    HttpSession session1 = request.getSession();
    session1.invalidate(); //session invalidate
    RequestDispatcher rd = request.getRequestDispatcher("login.html");
    rd.include(request,response);

    pr.flush();
    pr.close();
%>
</form>
