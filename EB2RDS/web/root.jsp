<%--
  Created by IntelliJ IDEA.
  User: sid
  Date: 11/02/2018
  Time: 11:00
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="java.sql.*" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.Connection" %>
<%
  // Read RDS connection information from the environment
  String dbName = System.getProperty("RDS_DB_NAME");
  String userName = System.getProperty("RDS_USERNAME");
  String password = System.getProperty("RDS_PASSWORD");
  String hostname = System.getProperty("RDS_HOSTNAME");
  String port = System.getProperty("RDS_PORT");
  String jdbcUrl = "jdbc:mysql://" + hostname + ":" +
          port + "/" + dbName + "?user=" + userName + "&password=" + password;

  String first_name = request.getParameter("first_name");
  String last_name = request.getParameter("last_name");
  String Email = request.getParameter("Email");
  String Title = request.getParameter("Title");

  PrintWriter pr = response.getWriter();



  // Load the JDBC driver
  try {

    Class.forName("com.mysql.jdbc.Driver");
    Connection con = DriverManager.getConnection(jdbcUrl);
    Statement st = con.createStatement();
    int i = st.executeUpdate("INSERT INTO peopleDetails(First_Name,Last_Name,Email,Title) VALUES ('"+first_name+"','"+last_name+"','"+Email+"','"+Title+"')");
    pr.println("<html><head><title>PICKMEUP</title></head><body>");
    pr.println("<p>Connected </p></body></html>");
    pr.flush();


  } catch (Exception e) {
    pr.println("<html><head><title>PICKMEUP</title></head><body>");
    pr.println("<p>"+ e.getMessage() + "</p></body></html>");
    pr.flush();
    throw new RuntimeException("Cannot find the driver in the classpath!", e);
  }



%>

