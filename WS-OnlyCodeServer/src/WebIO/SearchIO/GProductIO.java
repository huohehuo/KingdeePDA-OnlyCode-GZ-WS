package WebIO.SearchIO;

import Utils.JDBCUtil;
import Utils.Lg;
import WebIO.WebResponse;
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
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/GProductIO")
public class GProductIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
//        String fid = request.getParameter("fid");
//        if (null ==fid ||"".equals(fid)){
//            response.getWriter().write(gson.toJson(new WebResponse(false,"用户ID为空，查询失败")));
//            return;
//        }
//        String version = request.getParameter("version");
        String SQL = "";
        String con = "";
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        ArrayList<WebResponse.GProduct> container = new ArrayList<>();
//        System.out.println(parameter);
//        if (parameter != null) {
//        List<TestB> testBS = new ArrayList<>();
//        testBS.add(new TestB("one","A"));
//        testBS.add(new TestB("one2","B"));
//        testBS.add(new TestB("one3","C"));
//        Lg.e("列表",testBS);

            try {
                conn = JDBCUtil.getConn4Web();
                SQL = "SELECT   FInterID, FID,FName FROM t_SubMessage Where FInterID>0  AND FDeleted=0  And FTypeID=10002 ORDER BY FID"+con;
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    int i = rs.getRow();
                    while (rs.next()) {
                        WebResponse.GProduct bean = webResponse.new GProduct();
                        bean.FID = rs.getString("FID");
                        bean.FName = rs.getString("FName");
                        bean.FInterID = rs.getString("FInterID");
                        container.add(bean);
                    }
                    webResponse.state=true;
                    webResponse.size = container.size();
                    webResponse.gProducts = container;
                    Lg.e("返回数据：",webResponse);
                    response.getWriter().write(gson.toJson(webResponse));
//                    response.getWriter().write(CommonJson.getCommonJson(true,"{answer:123}"));
//                    response.getWriter().write("{\"answer\":\"one\",\"value\":\"A\"}");
                }else{
                    response.getWriter().write(gson.toJson(new WebResponse(false,"数据库表查询失败-nothing data")));
//                    response.getWriter().write(CommonJson.getCommonJson(false,"未查询到数据"));
                }


            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库表查询失败-SQL Check Error")));
//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库表查询失败")));
//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }

//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
