package WebIO.SearchIO;

import Utils.JDBCUtil;
import Utils.Lg;
import WebIO.PostBean;
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

import static Utils.JDBCUtil.ReadAsChars;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/MdUser4ClientIO")
public class MdUser4ClientIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String parameter= null;
        Lg.e("到达");
        try{
            parameter = ReadAsChars(request);//解密数据
        }catch (Exception e){
//            response.getWriter().write(gson.toJson(new WebResponse(false,"上传失败,请求体解析错误")));
        }

        if (null ==parameter ||"".equals(parameter)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"查询条件拼接出错，查询失败")));
            return;
        }
        PostBean pBean = gson.fromJson(parameter, PostBean.class);

        if (null ==pBean.client_ids || "".equals(pBean.client_ids)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"请先选择客户")));
            return;
        }

//        String version = request.getParameter("version");
        String SQL = "";
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        ArrayList<WebResponse.MdUser> container = new ArrayList<>();
//        System.out.println(parameter);
//        if (parameter != null) {
//        List<TestB> testBS = new ArrayList<>();
//        testBS.add(new TestB("one","A"));
//        testBS.add(new TestB("one2","B"));
//        testBS.add(new TestB("one3","C"));
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
                SQL = "select FID,FName from t_UserPDASupply where FCustID in(" + pBean.client_ids+")";
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    int i = rs.getRow();
                    while (rs.next()) {
                        WebResponse.MdUser bean = webResponse.new MdUser();
                        bean.FID = rs.getString("FID");
                        bean.FName = rs.getString("FName");
                        container.add(bean);
                    }
                    webResponse.state=true;
                    webResponse.size = container.size();
                    webResponse.mdUsers = container;
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
