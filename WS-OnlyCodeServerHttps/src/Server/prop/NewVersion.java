package Server.prop;

import Bean.NewVersionBean;
import Utils.CommonJson;
import Utils.JDBCUtil;
import com.google.gson.Gson;

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
@WebServlet(urlPatterns = "/NewVersion")
public class NewVersion extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getSQLiteConn1();
            sta = conn.prepareStatement("SELECT * FROM NEW_VERSION WHERE id = 1");
            rs = sta.executeQuery();
            if(rs!=null&&rs.getRow()>=0){
                NewVersionBean newVersionBean = new NewVersionBean();
                newVersionBean.Version = rs.getInt("Version");
                newVersionBean.Rem = rs.getString ("Rem");
                newVersionBean.downLoadURL = rs.getString("DownLoadURL");
                response.getWriter().write(CommonJson.getCommonJson(true,new Gson().toJson(newVersionBean)));
            }else{
                response.getWriter().write(CommonJson.getCommonJson(false,"未找到数据"));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

        }finally {
            JDBCUtil.close(rs,sta,conn);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
