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
@WebServlet(urlPatterns = "/ManSearchLike")
public class ManSearchLike extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String parameter = request.getParameter("json");
        String version = request.getParameter("version");
        String SQL = "";
        Gson gson = new Gson();
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        ArrayList<DownloadReturnBean.employee> container = new ArrayList<>();
        System.out.println(parameter);
        if (parameter != null) {
            try {
                System.out.println(request.getParameter("sqlip") + " " + request.getParameter("sqlport") + " " + request.getParameter("sqlname") + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
//                if (version.equals("500116")  || version.equals("500115"))
//                {
//                    SQL = "select top 50 FSecCoefficient,FSecUnitID,FIsSnManage,FItemID,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod,FNumber,FModel,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,'' as FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 and (FNumber like '%"+parameter+"%' or FName like '%"+parameter+"%') order by FNumber";//k3 rise 12.3
//                }
//                else if (version.equals("800103") || version.equals("800102")  || version.contains("5001"))
//                {
//                    SQL = "select top 50 FSecCoefficient,FSecUnitID,FIsSnManage,FItemID,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod,FNumber,FModel,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 and (FNumber like '%"+parameter+"%' or FName like '%"+parameter+"%') order by FNumber";//旗舰版和k3
//                }
//                else
//                {
//                    SQL = "select top 50 FSecCoefficient,FSecUnitID,FIsSnManage,FItemID,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod,FNumber,FModel,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,'' as FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 and (FNumber like '%"+parameter+"%' or FName like '%"+parameter+"%') order by FNumber";//专业版
                    SQL = "select FItemID,FName,FNumber,FDepartmentID,FEmpGroup,FEmpGroupID from t_Emp where FItemID>0 and FDeleteD=0 AND (FNumber like '%"+parameter+"%' or FName like '%"+parameter+"%') order by FName collate Chinese_PRC_CI_AS ";//专业版
//                }
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
                if(rs!=null){
                    int i = rs.getRow();
                    System.out.println("rs的长度"+i);
                    while (rs.next()) {
                        DownloadReturnBean.employee emp = downloadReturnBean.new employee();
                        emp.FItemID = rs.getString("FItemID");
                        emp.FName = rs.getString("FName");
                        emp.FNumber = rs.getString("FNumber");
                        emp.FDpartmentID = rs.getString("FDepartmentID");
                        emp.FEmpGroup = rs.getString("FEmpGroup");
                        emp.FEmpGroupID = rs.getString("FEmpGroupID");
                        container.add(emp);
                    }
                    downloadReturnBean.employee = container;
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
