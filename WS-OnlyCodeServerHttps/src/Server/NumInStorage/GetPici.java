package Server.NumInStorage;

import Bean.DownloadReturnBean;
import Bean.GetBatchNoBean;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/GetPici")
public class GetPici extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        GetBatchNoBean gBean = new Gson().fromJson(request.getParameter("json"),GetBatchNoBean.class);
        System.out.println("GetPici:--json:"+request.getParameter("json"));
        ArrayList<DownloadReturnBean.InstorageNum> container = new ArrayList<>();
        try {
            conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
            System.out.println(request.getParameter("sqlip")+" "+request.getParameter("sqlport")+" "+request.getParameter("sqlname")+" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
            String SQL = "select FItemID,FStockID,convert(float,FQty) as FQty,FBal,FStockPlaceID,FKFPeriod," +
                    "FKFDate,FBatchNo FROM ICInventory WHERE FItemID = ? AND FStockID = ? AND FStockPlaceID=?";
            sta = conn.prepareStatement(SQL);
            sta.setString(1,gBean.ProductID);
            sta.setString(2,gBean.StorageID);
            sta.setString(3,gBean.WaveHouseID);
            rs = sta.executeQuery();
            DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
            if(rs!=null){
                while (rs.next()){
                    DownloadReturnBean.InstorageNum instorageNum = downloadReturnBean.new InstorageNum();
                    instorageNum.FItemID = rs.getString("FItemID");
                    instorageNum.FStockID = rs.getString("FStockID");
                    instorageNum.FQty = rs.getString("FQty");
                    instorageNum.FBal = rs.getString("FBal");
                    instorageNum.FStockPlaceID = rs.getString("FStockPlaceID");
                    instorageNum.FKFPeriod = rs.getString("FKFPeriod");
                    instorageNum.FKFDate = rs.getString("FKFDate");
                    instorageNum.FBatchNo = rs.getString("FBatchNo");
//                    instorageNum.FUnitID = rs.getString("FUnitID");
                    container.add(instorageNum);
                }
                if(container.size()>0){
                    System.out.println("GetPici返回数据："+container.toString());
                    downloadReturnBean.InstorageNum=container;
                    response.getWriter().write(CommonJson.getCommonJson(true,new Gson().toJson(downloadReturnBean)));
                }else{
                    response.getWriter().write(CommonJson.getCommonJson(false,"未找到数据"));
                }

            }
        } catch (SQLException e) {
            response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
