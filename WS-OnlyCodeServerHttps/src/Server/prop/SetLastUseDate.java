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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/SetLastUseDate")
public class SetLastUseDate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        String paramter = request.getParameter("json");

        if(paramter!=null&&!paramter.equals("")){
            try {
                conn = JDBCUtil.getSQLiteConn1();
                String SQL = "UPDATE REGISTER SET Last_use_date = ? WHERE Register_code = ?";
                sta = conn.prepareStatement(SQL);
                sta.setString(1,getTime());
                sta.setString(2,paramter);
                int i = sta.executeUpdate();
                if(i>0){
                    response.getWriter().write(CommonJson.getCommonJson(true,""));
                }else{
                    response.getWriter().write(CommonJson.getCommonJson(false,"更新失败"));
                }
            } catch (ClassNotFoundException e) {
                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }
}
