<%--
  Created by IntelliJ IDEA.
  User: NB
  Date: 2017/8/7
  Time: 17:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>方左扫描助手服务端</title>
  </head>
  <body >
  <div style="width: 500px;;margin: auto;margin-top: 200px;border:1px dashed #000;padding: 50px">
    <%--<% out.print("你的外网IP地址："+request.getRemoteAddr());%>--%>
    <h1>服务器启动成功</h1>
    <hr/>
    <br>
    <br>
    <h2><% out.print("服务器IP地址："+java.net.InetAddress.getLocalHost().getHostAddress());%></h2>
    <h2><% out.print("服务器端口："+request.getLocalPort());%></h2>
  </div>
  </body>


</html>
