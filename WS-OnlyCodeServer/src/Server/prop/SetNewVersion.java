package Server.prop;

import Utils.CommonJson;
import Utils.JDBCUtil;
import com.google.gson.Gson;
import org.sqlite.JDBC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/SetNewVersion")
public class SetNewVersion extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        String version = request.getParameter("version");
        System.out.println("version:"+version);
        Version v = new Gson().fromJson(version,Version.class);
        try {
            conn = JDBCUtil.getSQLiteConn1();
            String SQL = "UPDATE NEW_VERSION SET Version = ?,Rem = ?  WHERE id = 1";
            sta = conn.prepareStatement(SQL);
            sta.setInt(1,v.version);
            sta.setString(2,v.rem);
            int i = sta.executeUpdate();
            if(i>0){
                response.getWriter().write(CommonJson.getCommonJson(true,""));
            }else{
                response.getWriter().write(CommonJson.getCommonJson(false,"插入失败"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
            e.printStackTrace();
        }finally {
            JDBCUtil.close(null,sta,conn);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    public class Version{
        public int version;
        public String rem;

    }
}
