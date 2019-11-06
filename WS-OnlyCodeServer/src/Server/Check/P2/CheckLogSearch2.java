package Server.Check.P2;

import Bean.CheckLogBackBean;
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
@WebServlet(urlPatterns = "/CheckLogSearch2")
public class CheckLogSearch2 extends HttpServlet {
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
//                CheckLogSearchBean searchBean = gson.fromJson(parameter, CheckLogSearchBean.class);
//                if (searchBean.FBarCode != null && !searchBean.FBarCode.equals("")) {
//                        condition += " and  t2.FBillNo = " + searchBean.FBarCode;
//                }
//                if (searchBean.FClient != null && !searchBean.FClient.equals("")) {
//                    condition += " and  t3.FName = " + searchBean.FClient;
//                }
//                if (searchBean.FStartTime != null && !searchBean.FStartTime.equals("") && searchBean.FEndTime != null && !searchBean.FEndTime.equals("")) {
//                    condition += " and  t1.FDateUpLoad between " + "\'" + searchBean.FStartTime + "\'" + "and" + "\'" + searchBean.FEndTime +" 23:59:59"+ "\'";
//                }
//                SQL = "select t2.FBillNo as 单据编号,t3.FName as 客户名称,t5.FName as 商品名称,convert(float,SUM(t4.FQty)) as 数量,CONVERT(decimal(28,4),ISNULL( t1.FPrice,0)) as 单价,t1.FInterID  from t_PDABarCodeSign_Out t1 inner join ICStockBill t2 on t1.FInterID = t2.FInterID inner join t_Organization t3 on t2.FSupplyID = t3.FItemID inner join t_PDABarCodeSign t4 on t1.FBarCode=t4.FBarCode inner join t_ICItem t5 on t5.FItemID = t4.FItemID where t1.FTypeID = 21 "+condition+" group by t1.FInterID,t4.FItemID,t3.FName,t2.FBillNo,t5.FName,t1.FPrice";
                SQL = "select t2.FBillNo as 单据编号,t3.FName as 客户名称,t1.FBarCode as 条码,t5.FName as 商品名称,t4.FQty as 数量,CONVERT(decimal(28,4),ISNULL( t1.FPrice,0)) as 单价  from t_PDABarCodeSign_Out t1 inner join ICStockBill t2 on t1.FInterID = t2.FInterID inner join t_Organization t3 on t2.FSupplyID = t3.FItemID inner join t_PDABarCodeSign t4 on t1.FBarCode=t4.FBarCode inner join t_ICItem t5 on t5.FItemID = t4.FItemID  where t1.FInterID = "+parameter;
//                SQL = "SELECT  t1.FItemID ,FItemClassID,t1.FNumber,t1.FParentID,FLevel,FDetail,t1.FName,FAddress,FPhone,FEmail,x2.FTypeID  FROM t_Item t1  with(index (uk_Item2)) LEFT JOIN t_Organization x2 ON t1.FItemID = x2.FItemID  WHERE FItemClassID = 1 AND (t1.FDetail = 1) AND t1.FDeleteD=0 and (t1.FNumber like '%"+parameter+"%' or t1.FName like '%"+parameter+"%') ORDER BY t1.FNumber";
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
                        bean.FBillNo = rs.getString("单据编号");
                        bean.FCLientName = rs.getString("客户名称");
                        bean.FName = rs.getString("商品名称");
                        bean.FNum = rs.getString("数量");
                        bean.FPrice = rs.getString("单价");
//                        bean.FInterID = rs.getString("FInterID");
                        container.add(bean);
                    }
                    Lg.e("获得查询日志数据2:",container);
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
