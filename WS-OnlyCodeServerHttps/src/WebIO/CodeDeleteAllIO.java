package WebIO;

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
import java.util.ArrayList;

/**
 *          删除制定用户的所有临时表数据
 */
@WebServlet(urlPatterns = "/CodeDeleteAllIO")
public class CodeDeleteAllIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
//        String barcode = request.getParameter("barcode");
//        String userid = request.getParameter("userid");
        String imie = request.getParameter("imie");
        String SQL = "";
        Gson gson = new Gson();
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        String backString = "";
        if (null ==imie ||"".equals(imie)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"唯一码为空，删除失败")));
            return;
        }
//        if (null ==userid ||"".equals(userid)){
//            response.getWriter().write(gson.toJson(new WebResponse(false,"用户ID为空，查询失败")));
//        }
        ArrayList<WebResponse.Product> container = new ArrayList<>();
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
//                SQL = "select FName as 用户名,FPassWord as 密码,FUserID,FID as 用户ID,isnull(FLevel,1) as FLevel,ISNULL(FPermit,'') as FPermit from t_UserPDASupply where FDeleted=0 AND FName ="+name+ " AND FPassWord = "+pwd;
//                sta = conn.prepareStatement(SQL);
                sta = conn.prepareStatement("delete from a_DetailsTable where FPDAID = '"+imie+"'");
//                System.out.println("SQL:"+SQL);
                sta.execute();
                response.getWriter().write(gson.toJson(new WebResponse(true,"删除成功")));
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));
//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));

//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }

//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
