package Server.ProductSearch;

import Bean.DownloadReturnBean;
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
@WebServlet(urlPatterns = "/ProductSearchWhere")
public class ProductSearchWhere extends HttpServlet {
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
        System.out.println(parameter);
        if (parameter != null) {
            try {
                System.out.println(request.getParameter("sqlip") + " " + request.getParameter("sqlport") + " " + request.getParameter("sqlname") + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
                if (version.equals("500116")|| version.equals("500115"))
                {
                    SQL = "select top 50 FSecCoefficient,FSecUnitID,FIsSnManage,FItemID,FNumber,FModel,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,'' as FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 and (FItemID = "+parameter+") order by FNumber";//k3 rise 12.3
                }
                else if (version.equals("800103") || version.equals("800102") || version.contains("5001"))
                {
                    SQL = "select top 50 FSecCoefficient,FSecUnitID,FIsSnManage,FItemID,FNumber,FModel,FName,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 and (FItemID = "+parameter+") order by FNumber";//旗舰版和k3
                }
                else
                {
                    SQL = "select top 50 FSecCoefficient,FSecUnitID,FIsSnManage,FItemID,FNumber,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod,FModel,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,'' as FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 and (FItemID = "+parameter+") order by FNumber";//专业版
                }
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
                if(rs!=null){
                    int i = rs.getRow();
                    System.out.println("rs的长度"+i);
                    while (rs.next()) {
                        DownloadReturnBean.product productBean = downloadReturnBean.new product();
                        productBean.FSecCoefficient = rs.getString("FSecCoefficient");
                        productBean.FSecUnitID = rs.getString("FSecUnitID");
                        productBean.FBatchManager = rs.getString("FBatchManager");
                        productBean.FIsSnManage = rs.getString("FIsSnManage");
                        productBean.FDefaultLoc = rs.getString("FDefaultLoc");
                        productBean.FItemID = rs.getString("FItemID");
                        productBean.FModel = rs.getString("FModel");
                        productBean.FISKFPeriod = rs.getString("FISKFPeriod");
                        productBean.FKFPeriod = rs.getString("FKFPeriod");
                        productBean.FName = rs.getString("FName");
                        productBean.FNumber = rs.getString("FNumber");
                        productBean.FSalePrice = rs.getString("FSalePrice");
                        productBean.FSPID = rs.getString("FSPID");
                        productBean.FUnitGroupID = rs.getString("FUnitGroupID");
                        productBean.FUnitID = rs.getString("FUnitID");
                        container.add(productBean);
                    }
                    downloadReturnBean.products = container;
                    response.getWriter().write(CommonJson.getCommonJson(true,gson.toJson(downloadReturnBean)));
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
        doPost(request,response);
    }
}
