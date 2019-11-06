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
  <body>
  <% out.print("你的外网IP地址："+request.getRemoteAddr());%>
  <% out.print("你的内网IP地址："+request.getLocalAddr());%>
  </body>


</html>
