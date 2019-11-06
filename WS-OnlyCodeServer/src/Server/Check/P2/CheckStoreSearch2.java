package Server.Check.P2;

import Bean.CheckLogBackBean;
import Bean.CheckLogSearchBean;
import Bean.DownloadReturnBean;
import Utils.CommonJson;
import Utils.JDBCUtil;
import Utils.Lg;
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
@WebServlet(urlPatterns = "/CheckStoreSearch2")
public class CheckStoreSearch2 extends HttpServlet {
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
        String condition = "";
        ArrayList<CheckLogBackBean> container = new ArrayList<>();
        System.out.println(parameter);
        if (parameter != null) {
            try {
                System.out.println(request.getParameter("sqlip") + " " + request.getParameter("sqlport") + " " + request.getParameter("sqlname") + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
                CheckLogSearchBean searchBean = gson.fromJson(parameter, CheckLogSearchBean.class);
//                if (searchBean.FNumber != null && !searchBean.FNumber.equals("")) {
//                        condition += " and  t2.FNumber = " + searchBean.FNumber;
//                }
//                if (searchBean.FName != null && !searchBean.FName.equals("")) {
//                    condition += " and t2.FName = '" + searchBean.FName+"'";
//                }
//                if (searchBean.FStorage != null && !searchBean.FStorage.equals("")) {
//                    condition += " and t3.FName = '" + searchBean.FStorage+"'";
//                }

//                if (searchBean.FStartTime != null && !searchBean.FStartTime.equals("") && searchBean.FEndTime != null && !searchBean.FEndTime.equals("")) {
//                    condition += " and  t1.FDateUpLoad between " + "\'" + searchBean.FStartTime + "\'" + "and" + "\'" + searchBean.FEndTime +" 23:59:59"+ "\'";
//                }
//                SQL = "select t2.FModel,t2.FNumber as 物料编码,t2.FName as 物料名称,convert(float,t1.FQty) as 基本单位库存,convert(float,t1.FSecQty) as 辅助单位库存,t6.FName as 辅助单位,t5.FName as 基本单位,t3.FName as 仓库,t4.FName as 仓位,t1.FBatchNo as 批次,t1.FKFDate as 生产日期,t1.FKFPeriod as 保质期,t1.FStockID,t1.FItemID from ICInventory t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_stock t3 on t1.FStockID = t3.FItemID left join t_stockPlace t4 on t1.FStockPlaceID = t4.FSPID left join t_Measureunit t5 on t2.FUnitID = t5.FItemID left join t_Measureunit t6 on t2.FSecUnitID = t6.FItemID  where 1=1  and t1.FStockID in(select FStockID from t_UserPDASupplyStock where FID = "+searchBean.FUserID+" ) "+condition;
                SQL = "select  t1.FBarCode as 条码,t2.FNumber as 商品代码,t2.FName as 商品名称,t2.FModel as 规格 ,convert(float,t1.FQty) as 数量,t3.FName as 单位 from t_PDABarCodeSign t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_MeasureUnit t3 on t1.FUnitID=t3.FItemID left join t_Stock t6 on t1.FStockID=t6.FItemID  left join t_StockPlace t7 on t1.FStockPlaceID=t7.FSPID where t1.FIsInStore='已入库' and t1.FItemID='"+searchBean.FItemID+"' and t1.FStockID = '"+searchBean.FStockID+"'";
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
                if(rs!=null){
                    int i = rs.getRow();
                    System.out.println("rs的长度"+i);
                    while (rs.next()) {
                        CheckLogBackBean bean = new CheckLogBackBean();
                        bean.FBarCode = rs.getString("条码");
                        bean.FName = rs.getString("商品名称");
                        bean.FNumber = rs.getString("商品代码");
                        bean.FModel = rs.getString("规格");
                        bean.FNum = rs.getString("数量");
                        bean.FUnit = rs.getString("单位");

                        container.add(bean);
                    }
                    Lg.e("获得查询日志数据:",container);
                    downloadReturnBean.checkLogBackBeans = container;
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
