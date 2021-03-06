package WebIO;

import Utils.JDBCUtil;
import Utils.Lg;
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
 *          用户检测登录
 */
@WebServlet(urlPatterns = "/UserCheckIO")
public class UserCheckIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        String SQL = "";
        Gson gson = new Gson();
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        if (null ==name ||"".equals(name)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"请输入用户名")));
            return;
        }
        if (null ==pwd ||"".equals(pwd)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"请输入密码")));
            return;
        }
        ArrayList<WebResponse.Suppliers4P2> container = new ArrayList<>();
//        System.out.println(parameter);
//        if (parameter != null) {
//        List<TestB> testBS = new ArrayList<>();
//        testBS.add(new TestB("one","A"));
//        testBS.add(new TestB("one2","B"));
//        testBS.add(new TestB("one3","C"));
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
                SQL = "select FName as 用户名,FPassWord as 密码,FUserID,FID as 用户ID,isnull(FLevel,1) as FLevel,ISNULL(FPermit,'') as FPermit from t_UserPDASupply where FDeleted=0 AND FName ='"+name+ "' AND FPassWord = '"+pwd+"'";
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    int i = rs.getRow();
                    while (rs.next()) {
                        WebResponse.Suppliers4P2 bean = webResponse.new Suppliers4P2();
                        bean.FID = rs.getString("用户ID");
                        bean.FName = rs.getString("用户名");
                        bean.FPassWord = rs.getString("密码");
                        bean.FUserID = rs.getString("FUserID");
                        bean.FLevel = rs.getString("FLevel");
                        bean.FPermit = rs.getString("FPermit");
                        container.add(bean);
                    }
                    if (container.size()>0){
                        webResponse.state = true;
                        webResponse.backString = "登录成功";
                        webResponse.suppliers4P2s = container;
                        Lg.e("登录返回：",webResponse);
                        response.getWriter().write(gson.toJson(webResponse));
                    }else{
                        response.getWriter().write(gson.toJson(new WebResponse(false,"无法找到该用户信息")));
                    }
//                    webResponse.suppliers4P2s = container;
//                    Lg.e("返回数据：",webResponse);
//                    response.getWriter().write(gson.toJson(webResponse));
//                    response.getWriter().write(CommonJson.getCommonJson(true,"{answer:123}"));
//                    response.getWriter().write("{\"answer\":\"one\",\"value\":\"A\"}");
                }else{
                    response.getWriter().write(gson.toJson(new WebResponse(false,"数据库表查询失败")));
//                    response.getWriter().write(CommonJson.getCommonJson(false,"未查询到数据"));
                }


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
