package Server.ProductSearch;

import Bean.DownloadReturnBean;
import Bean.ProductReturnBean;
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
@WebServlet(urlPatterns = "/SearchProducts")
public class SearchProducts extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        String parameter = request.getParameter("json");
        String version = request.getParameter("version");
        String SQL = "";
        Gson gson = new Gson();
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        ArrayList<DownloadReturnBean.product> container = new ArrayList<>();
        System.out.println("SearchProducts---json:"+parameter);
        if (parameter != null && !parameter.equals("")) {
            try {
                System.out.println(request.getParameter("sqlip") + " " + request.getParameter("sqlport") + " " + request.getParameter("sqlname") + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
                if (version.contains("5001") && !version.equals("500116") && !version.equals("500115") ) {
                    SQL = "select t1.FBarcode,0 as FINNUM,t2.FIsSnManage,t2.FName,t1.FTypeID,t1.FItemID,t2.FISKFPeriod,convert(INT,t2.FKFPeriod) as FKFPeriod,t1.FItemID,t1.FBarCode,t2.FNumber,t1.FUnitID,t2.FModel,t2.FUnitGroupID,t2.FDefaultLoc,t2.FBatchManager ,isnull(t2.FSalePrice,0) as FSalePrice,t2.FSPID from t_Barcode t1 left join t_ICItem t2 on t1.FItemID=t2.FItemID  where t1.FBarcode ='" + parameter+"'";//旗舰版
                } else {
                    SQL = "select t2.FIsSnManage,t2.FName,t1.FTypeID,t1.FItemID,t2.FISKFPeriod,convert(INT,t2.FKFPeriod) as FKFPeriod,t1.FItemID,t1.FBarCode,t2.FNumber,'' as FUnitID,t2.FModel,t2.FUnitGroupID,t2.FDefaultLoc,t2.FBatchManager ,isnull(t2.FSalePrice,0) as FSalePrice,t2.FSPID from t_Barcode t1 left join t_ICItem t2 on t1.FItemID=t2.FItemID where t1.FBarCode ='" + parameter+"'";
                }
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
                if(rs!=null){
                    int i = rs.getRow();
                    while (rs.next()) {
                        DownloadReturnBean.product productBean = downloadReturnBean.new product();
                        productBean.FIsSnManage = rs.getString("FIsSnManage");
                        productBean.FISKFPeriod = rs.getString("FISKFPeriod");
                        productBean.FKFPeriod = rs.getString("FKFPeriod");
                        productBean.FBatchManager = rs.getString("FBatchManager");
                        productBean.FDefaultLoc = rs.getString("FDefaultLoc");
                        productBean.FItemID = rs.getString("FItemID");
                        productBean.FModel = rs.getString("FModel");
                        productBean.FName = rs.getString("FName");
                        productBean.FNumber = rs.getString("FNumber");
                        productBean.FSalePrice = rs.getString("FSalePrice");
                        productBean.FSPID = rs.getString("FSPID");
                        productBean.FUnitGroupID = rs.getString("FUnitGroupID");
                        productBean.FUnitID = rs.getString("FUnitID");
                        if (version.contains("5001") && !version.equals("500116") && !version.equals("500115") ) {
                            productBean.FTaxRate = rs.getString("FINNUM");
                        }
                        container.add(productBean);
                    }
                    if(container.size()>0){
                        downloadReturnBean.products = container;
                        response.getWriter().write(CommonJson.getCommonJson(true,gson.toJson(downloadReturnBean)));
                    }else{
                        response.getWriter().write(CommonJson.getCommonJson(false,"未查询到数据"));
                    }

                }else{
                    response.getWriter().write(CommonJson.getCommonJson(false,"未查询到数据"));
                }


            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
