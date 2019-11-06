package Server.FirstinOrOut;

import Bean.EarlyBatchNoReturnBean;
import Bean.GetEarlyBatchNo;
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
@WebServlet(urlPatterns = "/GetEarlyBatchNO")
public class GetEarlyBatchNO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        String json = request.getParameter("json");
        if(json!=null&&!json.equals("")){
            try {
                GetEarlyBatchNo getEarlyBatchNo = new Gson().fromJson(json,GetEarlyBatchNo.class);
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
                System.out.println(request.getParameter("sqlip")+" "+request.getParameter("sqlport")+" "+request.getParameter("sqlname")+" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
                String SQL = "select * from (select  FBatchNo,convert(float,sum(FQty)) as FQty from ICInventory  where  FItemID =? and FQty>0 and LEN(SUBSTRing(FBatchNo,1,6))=6  and SUBSTRing(FBatchNo,1,6) not  LIKE '%[^0-9]%'  group by FBatchNo) as t where  CONVERT(int,SUBSTRING(FBatchNo,1,6))<?";
                sta = conn.prepareStatement(SQL);
                sta.setString(1,getEarlyBatchNo.productID);
                sta.setString(2,getEarlyBatchNo.subBatchNo);
                rs = sta.executeQuery();
                EarlyBatchNoReturnBean earlyBatchNoReturnBean = new EarlyBatchNoReturnBean();
                ArrayList<EarlyBatchNoReturnBean.BatchNoHas> container = new ArrayList<>();
                while (rs.next()){
                    EarlyBatchNoReturnBean.BatchNoHas bean = earlyBatchNoReturnBean.new BatchNoHas();
                    bean.BatchNo = rs.getString("FBatchNo");
                    bean.num = rs.getString("FQty");
                    container.add(bean);
                }
                if(container.size()>0){
                    earlyBatchNoReturnBean.list = container;
                    writer.write(CommonJson.getCommonJson(true,new Gson().toJson(earlyBatchNoReturnBean)));
                }else{
                    writer.write(CommonJson.getCommonJson(false,"1"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                writer.write(CommonJson.getCommonJson(false,"1"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                writer.write(CommonJson.getCommonJson(false,e.toString()));
            }finally {
                JDBCUtil.close(rs,sta,conn);
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
