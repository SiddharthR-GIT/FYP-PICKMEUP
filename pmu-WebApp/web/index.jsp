<%--
  Created by IntelliJ IDEA.
  User: sid
  Date: 09/03/2018
  Time: 14:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form name="landingpage" method="post" action="/LanndingPage">
<%
    RequestDispatcher rd = request.getRequestDispatcher("index.html");
    rd.include(request,response);
%>
</form>