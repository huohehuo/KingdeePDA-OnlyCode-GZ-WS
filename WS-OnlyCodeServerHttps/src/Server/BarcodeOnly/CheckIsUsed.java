package Server.BarcodeOnly;


import Bean.CheckBarCodeIsUse;
import Bean.OnlyBarCodeResponseBean;
import Utils.CommonJson;
import Utils.JDBCUtil;
import Utils.getDataBaseUrl;
import com.google.gson.Gson;
import sun.rmi.transport.tcp.TCPTransport;

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

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/CheckIsUsed")
public class CheckIsUsed extends HttpServlet {
    public final static String TAG = "/CheckIsUsed";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        String parameter = request.getParameter("json");
        if(parameter!=null){
            try {
                CheckBarCodeIsUse checkBarCodeIsUse = new Gson().fromJson(parameter,CheckBarCodeIsUse.class);
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
                System.out.println("/CheckIsUsed"+request.getParameter("sqlip")+" "+request.getParameter("sqlport")+" "+request.getParameter("sqlname")+" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
                String SQL = "SELECT * FROM a_DetailsTable WHERE FBarCode = ?";
                sta = conn.prepareStatement(SQL);
                sta.setString(1,checkBarCodeIsUse.barCode);
                rs = sta.executeQuery();

                if(!rs.next()){
                    String SQL1 = "SELECT FBarCode,FBatchNo,FBillNo,FIsInStore,FIsOutStore,convert( float,Fqty) as Fqty,FItemID,FUnitID,FStockID,FStockPlaceID FROM t_PDABarCodeSign WHERE FBarCode = ?";
                    sta = conn.prepareStatement(SQL1);
                    sta.setString(1,checkBarCodeIsUse.barCode);
                    System.out.println(TAG+"barcode:"+checkBarCodeIsUse.barCode);
                    rs = sta.executeQuery();
                    if(rs.next()){
                        OnlyBarCodeResponseBean oBean = new OnlyBarCodeResponseBean();
                        oBean.FBarCode = rs.getString("FBarCode");
                        oBean.FBatchNo = rs.getString("FBatchNo");
                        oBean.FBillNo = rs.getString("FBillNo");
                        oBean.FIsInStore = rs.getString("FIsInStore");
                        oBean.FIsOutStore = rs.getString("FIsOutStore");
                        oBean.FQty = rs.getString("FQty");
                        oBean.FItemID = rs.getString("FItemID");
                        oBean.FUnitID = rs.getString("FUnitID");
                        oBean.FStockID = rs.getString("FStockID");
                        oBean.FStockPlaceID = rs.getString("FStockPlaceID");
                        response.getWriter().write(CommonJson.getCommonJson(true,new Gson().toJson(oBean)));
                    }else{
                        response.getWriter().write(CommonJson.getCommonJson(false,"未找到条码数据"));
                    }
                }else{
                    response.getWriter().write(CommonJson.getCommonJson(false,"条码正在被其他巴枪使用"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            } catch (ClassNotFoundException e) {
                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }
        }else{
            response.getWriter().write(CommonJson.getCommonJson(false,"数据有误"));
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
