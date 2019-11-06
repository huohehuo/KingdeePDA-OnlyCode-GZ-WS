package Server.Other;

import Bean.SNAddBean;
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

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/SNAddOrder")
public class SNAddOrder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        PrintWriter writer = response.getWriter();
        String json = request.getParameter("json");
        if(json!=null&&!json.equals("")){
            SNAddBean sBean = new Gson().fromJson(json,SNAddBean.class);
            try {
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip")
                        , request.getParameter("sqlport"), request.getParameter("sqlname")),
                        request.getParameter("sqlpass"), request.getParameter("sqluser"));
                String SQL = "exec proc_InsertBarCode ?,?,?,?,?,?,?,?,?,?,?,?";
                sta = conn.prepareStatement(SQL);
                sta.setString(1,sBean.FPDAID);
                sta.setString(2,sBean.FOrderID);
                sta.setString(3,sBean.FItemID);
                sta.setString(4,sBean.FStockID);
                sta.setString(5,sBean.FStockPlaceID);
                sta.setString(6,sBean.FBatchNo);
                sta.setString(7,sBean.FKFPeriod);
                sta.setString(8,sBean.FKFDate);
                sta.setString(9,sBean.FQty);
                sta.setString(10,sBean.FSerialShort);
                sta.setString(11,sBean.FValueBegin);
                sta.setString(12,sBean.FValueEnd);
                sta.execute();
                writer.write(CommonJson.getCommonJson(true,""));
            } catch (SQLException e) {
                writer.write(CommonJson.getCommonJson(false,"序列号已经被使用过了"));
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                writer.write(CommonJson.getCommonJson(false,e.toString()));
                e.printStackTrace();
            }finally {
                JDBCUtil.close(rs,sta,conn);
            }
            System.out.println(request.getParameter("sqlip")+" "
                    +request.getParameter("sqlport")+" "+request.getParameter("sqlname")
                    +" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
        }else{
            writer.write(CommonJson.getCommonJson(false,"数据在传输过程中遗失"));
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
