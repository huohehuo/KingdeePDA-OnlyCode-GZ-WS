package Server.JhSearch;

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
@WebServlet(urlPatterns = "/jhSearchLike")
public class JhSearchLike extends HttpServlet {
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
        ArrayList<DownloadReturnBean.GetGoodsDepartment> container = new ArrayList<DownloadReturnBean.GetGoodsDepartment>();
        System.out.println("jhSearchLike--json:"+parameter);
        if (parameter != null) {
            try {
                System.out.println(request.getParameter("sqlip") + " " + request.getParameter("sqlport") + " " + request.getParameter("sqlname") + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
                if (version.equals("500116")  || version.equals("500115"))
                {
                    SQL = "select top 50 FSecCoefficient,FSecUnitID,FIsSnManage,FItemID,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod,FNumber,FModel,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,'' as FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 and (FNumber like '%"+parameter+"%' or FName like '%"+parameter+"%') order by FNumber";//k3 rise 12.3
                }
                else if (version.equals("800103") || version.equals("800102")  || version.contains("5001"))
                {
//                    SQL = "select top 50 FSecCoefficient,FSecUnitID,FIsSnManage,FItemID,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod,FNumber,FModel,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 and (FNumber like '%"+parameter+"%' or FName like '%"+parameter+"%') order by FNumber";//旗舰版和k3
                    SQL = "SELECT  TOP 50 t1.FItemID ,t1.FDeleted ,t1.FNumber,t1.FName,t1.FDetail FROM t_Item t1  with(index (uk_Item2)) LEFT JOIN t_Department x2 ON t1.FItemID = x2.FItemID  WHERE FItemClassID = 2 AND t1.FDetail=1  AND t1.FDeleteD=0 and (t1.FNumber like '%"+parameter+"%' or t1.FName like '%"+parameter+"%') ORDER BY t1.FNumber";
                }
                else
                {
                    SQL = "SELECT  TOP 50 t1.FItemID ,t1.FDeleted ,t1.FNumber,t1.FName,t1.FDetail FROM t_Item t1  with(index (uk_Item2)) LEFT JOIN t_Department x2 ON t1.FItemID = x2.FItemID  WHERE FItemClassID = 2 AND t1.FDetail=1  AND t1.FDeleteD=0 and (t1.FNumber like '%"+parameter+"%' or t1.FName like '%"+parameter+"%') ORDER BY t1.FNumber";
                }
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
                if(rs!=null){
                    int i = rs.getRow();
                    System.out.println("rs的长度"+i);
                    while (rs.next()) {
                        DownloadReturnBean.GetGoodsDepartment bean = downloadReturnBean.new GetGoodsDepartment();
                        bean.FItemID = rs.getString("FItemID");
                        bean.FDeleted = rs.getString("FDeleted");
                        bean.FNumber = rs.getString("FNumber");
                        bean.FName = rs.getString("FName");
                        bean.FDetail = rs.getString("FDetail");
                        container.add(bean);

                    }
                    System.out.println("获得供应商数据："+container.toString());
                    downloadReturnBean.getGoodsDepartments = container;
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
