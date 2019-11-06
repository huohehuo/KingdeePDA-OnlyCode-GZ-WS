package Server.prop;

import Utils.CommonJson;
import Utils.JDBCUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/RegisterCode")
public class RegisterCode extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        String paramter = request.getParameter("json");
        if(paramter!=null&&!paramter.equals("")){
            try {
                conn = JDBCUtil.getSQLiteConn1();
                String SQL = "INSERT INTO REGISTER VALUES (?,?,?,?)";
                sta = conn.prepareStatement(SQL);
                sta.setString(1,paramter);
                sta.setString(2,"");
                sta.setString(3,"");
                sta.setString(4,"");
                int i = sta.executeUpdate();
                if(i>0){
                    response.getWriter().write(CommonJson.getCommonJson(true,""));
                }else{
                    response.getWriter().write(CommonJson.getCommonJson(false,"注册失败"));
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
