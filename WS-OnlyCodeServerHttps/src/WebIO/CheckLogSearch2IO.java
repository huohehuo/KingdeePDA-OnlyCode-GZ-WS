package WebIO;

import Utils.JDBCUtil;
import Utils.Lg;
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
         * 条码记录查询二级
 */
@WebServlet(urlPatterns = "/CheckLogSearch2IO")
public class CheckLogSearch2IO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String parameter = request.getParameter("FInterID");
        String SQL = "";
        Gson gson = new Gson();
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        String condition = "";
        ArrayList<WebResponse.CheckLogBackBean> container = new ArrayList<>();
        System.out.println(parameter);
        if (parameter != null) {
            try {
                conn = JDBCUtil.getConn4Web();
//                SQL = "select t2.FBillNo as 单据编号,t3.FName as 客户名称,t1.FBarCode as 条码,t5.FName as 商品名称,t4.FQty as 数量,CONVERT(decimal(28,4),ISNULL( t1.FPrice,0)) as 单价  from t_PDABarCodeSign_Out t1 inner join ICStockBill t2 on t1.FInterID = t2.FInterID inner join t_Organization t3 on t2.FSupplyID = t3.FItemID inner join t_PDABarCodeSign t4 on t1.FBarCode=t4.FBarCode inner join t_ICItem t5 on t5.FItemID = t4.FItemID  where t1.FInterID = "+parameter;
                SQL = "select t_2043.FName as 产品系列,t4.FOrderBillNo as 订单号,t2.FBillNo as 单据编号,t3.FName as 客户名称,t1.FBarCode as 条码,t5.FName as 商品名称,t4.FQty as 数量,CONVERT(decimal(28,4),ISNULL( t1.FPrice,0)) as 单价  from t_PDABarCodeSign_Out t1 inner join ICStockBill t2 on t1.FInterID = t2.FInterID inner join t_Organization t3 on t2.FSupplyID = t3.FItemID inner join t_PDABarCodeSign t4 on t1.FBarCode=t4.FBarCode inner join t_ICItem t5 on t5.FItemID = t4.FItemID left join t_Item_2043  t_2043  on t5.F_109=t_2043.FItemID  where t1.FInterID = "+parameter;
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    int i = rs.getRow();
                    System.out.println("rs的长度"+i);
                    while (rs.next()) {
                        WebResponse.CheckLogBackBean bean = webResponse.new CheckLogBackBean();
                        bean.FBarCode = rs.getString("条码");
                        bean.FBillNo = rs.getString("单据编号");
                        bean.FCLientName = rs.getString("客户名称");
                        bean.FName = rs.getString("商品名称");
                        bean.FNum = rs.getString("数量");
                        bean.FPrice = rs.getString("单价");
                        bean.FOrderBillNo = rs.getString("订单号");
                        bean.FProductType = rs.getString("产品系列");
//                        bean.FInterID = rs.getString("FInterID");
                        container.add(bean);
                    }
                    webResponse.state = true;
                    webResponse.size = container.size();
                    webResponse.backString = "二级数据获取成功";
                    webResponse.checkLogBackBeans = container;
                    Lg.e("二级数据获取成功：",webResponse);
                    response.getWriter().write(gson.toJson(webResponse));
                }else{
                    response.getWriter().write(gson.toJson(new WebResponse(false,"未查询到数据")));
                }


            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));

            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
