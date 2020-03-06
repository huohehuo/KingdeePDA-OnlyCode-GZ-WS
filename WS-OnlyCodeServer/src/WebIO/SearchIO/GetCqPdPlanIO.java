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
@WebServlet(urlPatterns = "/GetCqPdPlanIO")
public class GetCqPdPlanIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
//        String parameter = request.getParameter("json");
//        String version = request.getParameter("version");
        String SQL = "";
        Gson gson = new Gson();
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        ArrayList<WebResponse.CqPdPlanBean> container = new ArrayList<>();
//        System.out.println(parameter);
//        if (parameter != null) {
//        List<TestB> testBS = new ArrayList<>();
//        testBS.add(new TestB("one","A"));
//        testBS.add(new TestB("one2","B"));
//        testBS.add(new TestB("one3","C"));
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
                SQL = "select t1.FName as 盘点方案名称,t1.FID as 盘点方案ID,CONVERT(varchar(50), t1.FDate,20) as 日期,t2.FName as 制单人,t1.FRemark as 备注 from t_PDABarCodeCheckFirst t1 left join t_user t2 on t1.FUserID=t2.FUserID";
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    int i = rs.getRow();
                    while (rs.next()) {
                        WebResponse.CqPdPlanBean bean = webResponse.new CqPdPlanBean();
                        bean.FID = rs.getString("盘点方案ID");
                        bean.FDate = rs.getString("日期");
                        bean.FName = rs.getString("盘点方案名称");
                        bean.FMaker = rs.getString("制单人");
                        bean.FRemark = rs.getString("备注");
                        container.add(bean);
                    }
                    webResponse.state=true;
                    webResponse.size = container.size();
                    webResponse.cqPdPlanBeans = container;
                    Lg.e("返回数据：",webResponse);
                    response.getWriter().write(gson.toJson(webResponse));
//                    response.getWriter().write(CommonJson.getCommonJson(true,"{answer:123}"));
//                    response.getWriter().write("{\"answer\":\"one\",\"value\":\"A\"}");
                }else{
                    response.getWriter().write(gson.toJson(new WebResponse(false,"数据库表查询失败")));
//                    response.getWriter().write(CommonJson.getCommonJson(false,"未查询到数据"));
                }


            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库表查询失败")));
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
