<%--
  Created by IntelliJ IDEA.
  User: NB
  Date: 2017/8/7
  Time: 17:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.sun.org.apache.xpath.internal.operations.String" %>
<html>
<head>
    <title>查询维修历史</title>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.1.0/css/bootstrap.min.css">
    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>

    <!-- popper.min.js 用于弹窗、提示、下拉菜单 -->
    <script src="https://cdn.staticfile.org/popper.js/1.12.5/umd/popper.min.js"></script>

    <!-- 最新的 Bootstrap4 核心 JavaScript 文件 -->
    <script src="https://cdn.staticfile.org/twitter-bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body>

<jsp:include page="headLayout.jsp"/>
<%--
<%
    WebDao aa = new WebDao();
//    List list = (List) request.getAttribute("pl_list");
    List list = aa.getRegister();


    for (int i = 0; i < list.size(); i++) {
        RegisterBean rs = (RegisterBean) list.get(i);
%>--%>
<%--<%
    String tips = (String) request.getAttribute("tips");
%>
<h5 ><%=tips%></h5>--%>


<div class="container" style="margin-top: 36px">
    <div  class="card col-xs-12">
        <div class="card-body col-xs-12">
            <form action="HistoryBack" method="post">
                <div class="form-group">
                    <a for="email">输入查询ID</a>
                    <%--<input type="text" class="form-control" id="name" placeholder="Enter your name" name="name" VALUE="5d3ed4e248502d035b4f0200">--%>
                    <%--<input type="text" class="form-control" id="name" placeholder="Enter your word" name="name" VALUE="056518C19Y0208">--%>
                    <input type="text" class="form-control" id="name" placeholder="Enter your word" name="name">
                </div>
                <div class="form-check">
                    <!-- <label class="form-check-label">
                      <input class="form-check-input" type="checkbox"> Remember me
                    </label> -->
                </div>
                <button type="submit" class="btn btn-primary">确定</button>
            </form>

            <%--<table class="table">--%>
                <%--<thead>--%>
                <%--<tr>--%>
                    <%--<th>用户码</th>--%>
                    <%--<th>手机信息</th>--%>
                    <%--<th>版本号</th>--%>
                    <%--<th>最后登录时间</th>--%>
                    <%--<th>操作</th>--%>
                <%--</tr>--%>
                <%--</thead>--%>
                <%--<tbody>--%>
                <%--<%--%>
                    <%--WebDao aa = new WebDao();--%>
<%--//    List list = (List) request.getAttribute("pl_list");--%>
                    <%--List list = aa.getRegister();--%>
                    <%--for (int i = 0; i < list.size(); i++) {--%>
                        <%--RegisterBean rs = (RegisterBean) list.get(i);--%>
                <%--%>--%>

                <%--<tr>--%>
                    <%--<td><%=rs.getRegister_code() %></td>--%>
                    <%--<td><%=rs.getVal1() %></td>--%>
                    <%--<td><%=rs.getVal2() %></td>--%>
                    <%--<td><%=rs.getVal3() %></td>--%>
                    <%--&lt;%&ndash;<td style="height: 45px;width:80px"><%=rs.getLast_use_date() %></td>&ndash;%&gt;--%>
                    <%--<td><a href="register_delete?json=<%=rs.getRegister_code() %>">删除</a></td>--%>
                <%--</tr>--%>
                <%--</tbody>--%>
                <%--<%} %>--%>
            <%--</table>--%>
        </div>

    </div>
</div>


<%--<table border="0" bgcolor="ccceee" width="750" style="height: 161px;">
    <tr bgcolor="CCCCCC" align="center">
        <td style="height: 30px;width:80px ">用户码</td>
        <td style="height: 30px;width:180px ">手机IMIE码</td>
        <td style="height: 30px;width:80px ">版本号</td>
        <td style="height: 30px;width:80px ">操作</td>

    </tr>

    <tr align="center">
        <td style="height: 45px; width:80px"><%=rs.getRegister_code() %>
        </td>
        <td style="height: 45px; width:180px"><%=rs.getVal1() %>
        </td>
        <td style="height: 45px; width:80px"><%=rs.getVal2() %>
        </td>
        &lt;%&ndash;<td style="height: 45px;width:80px"><%=rs.getLast_use_date() %></td>&ndash;%&gt;
        <td width="80px" align="center"><a href="RegisterDelete?json=<%=rs.getRegister_code() %>">删除</a></td>
    </tr>
    <%} %>

</table>--%>
</body>


</html>
