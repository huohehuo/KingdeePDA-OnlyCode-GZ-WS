package Server.BarcodeOnly;

import Bean.CheckBarCodeIsUse;
import Bean.DetailsTableBean;
import Bean.OnlyBarCodeResponseBean;
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

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/InsertCheckIsUsed")
public class InsertCheckIsUsed extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        String parameter = request.getParameter("json");
        if(parameter!=null){
            synchronized (this){
                try {
                    DetailsTableBean dBean = new Gson().fromJson(parameter,DetailsTableBean.class);
                    conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
                    System.out.println("/InsertCheckIsUsed"+request.getParameter("sqlip")+" "+request.getParameter("sqlport")+" "+request.getParameter("sqlname")+" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
                    String SQL = "SELECT * FROM a_DetailsTable WHERE FBarCode = ?";
                    sta = conn.prepareStatement(SQL);
                    sta.setString(1,dBean.FBarCode);
                    rs = sta.executeQuery();

                    if(!rs.next()){
                        String SQL1  = "INSERT INTO a_DetailsTable VALUES (?,?,?,?,?,?,?,?,?)";
                        sta = conn.prepareStatement(SQL1);
                        sta.setString(1,dBean.FPDAID);
                        sta.setString(2,dBean.FOrderID);
                        sta.setString(3,dBean.FBarCode);
                        sta.setString(4,dBean.FItemID);
                        sta.setString(5,dBean.FStockID);
                        sta.setString(6,dBean.FStockPlaceID);
                        sta.setString(7,dBean.FBatchNo);
                        sta.setString(8,dBean.FKFPeriod);
                        sta.setString(9,dBean.FKFDate);
                        int i = sta.executeUpdate();
                        if(i>0){
                            response.getWriter().write(CommonJson.getCommonJson(true,""));
                        }else{
                            response.getWriter().write(CommonJson.getCommonJson(false,"添加失败"));
                        }
                    }else{
                        response.getWriter().write(CommonJson.getCommonJson(false,"条码正在被其他巴枪使用"));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

                } catch (ClassNotFoundException e) {
                    response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

                }finally {
                    JDBCUtil.close(rs,sta,conn);
                }
            }

        }else{
            response.getWriter().write(CommonJson.getCommonJson(false,"数据有误"));
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
