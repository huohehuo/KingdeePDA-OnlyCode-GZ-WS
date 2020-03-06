package Server.Other;

import Bean.YHRequestBean;
import Utils.CommonJson;
import Utils.JDBCUtil;
import Utils.getDataBaseUrl;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/GetYH")
public class GetYH extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        String json = request.getParameter("json");
        if(json!=null&&!json.equals("")){
            try {
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip")
                        , request.getParameter("sqlport"), request.getParameter("sqlname")),
                        request.getParameter("sqlpass"), request.getParameter("sqluser"));
                System.out.println(request.getParameter("sqlip")+" "
                        +request.getParameter("sqlport")+" "+request.getParameter("sqlname")
                        +" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
                String SQL = "SELECT  t1.FItemID ,FItemClassID,t1.FNumber,t1.FParentID,FLevel,FDetail," +
                        "t1.FName,FAddress,FPhone,'' FEmail   FROM t_Item t1  with(index (uk_Item2)) " +
                        "LEFT JOIN t_SonCompany x2 ON t1.FItemID = x2.FItemID  WHERE FItemClassID = 10 " +
                        "AND (t1.FDetail = 1) AND t1.FDeleteD=0  ORDER BY t1.FNumber";
                sta = conn.prepareStatement(SQL);
                rs = sta.executeQuery();
                YHRequestBean bean = new YHRequestBean();
                ArrayList<YHRequestBean.YH> container = new ArrayList<>();
                while (rs.next()){
                    YHRequestBean.YH b = bean.new YH();
                    b.FItemID = rs.getString("FItemID");
                    b.FName = rs.getString("FName");
                    container.add(b);
                }
                bean.list = container;
                if(container.size()>0){
                    writer.write(CommonJson.getCommonJson(true,new Gson().toJson(bean)));

                }else {
                    writer.write(CommonJson.getCommonJson(false,"no data"));
                }

            } catch (SQLException e) {
                writer.write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                writer.write(CommonJson.getCommonJson(false,"服务器错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
                e.printStackTrace();
            }finally {
                JDBCUtil.close(rs,sta,conn);
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
