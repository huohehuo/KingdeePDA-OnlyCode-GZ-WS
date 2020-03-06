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
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/StoreCheck2IO")
public class StoreCheck2IO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String FItemID = request.getParameter("FItemID");
        String FStockID = request.getParameter("FStockID");
//        String userid = request.getParameter("name");
//        String imie = request.getParameter("storage_name");
        String qty="0";
        Gson gson = new Gson();
        Connection conn = null;
        PreparedStatement sta = null;
        PreparedStatement sta2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String backString = "";
        String condition = "";
        ArrayList<WebResponse.CheckLogBackBean> container = new ArrayList<>();
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
//                String SQL = "select  t1.FBarCode as 条码,t2.FNumber as 商品代码,t2.FName as 商品名称,t2.FModel as 规格 ,convert(float,t1.FQty) as 数量,t3.FName as 单位 from t_PDABarCodeSign t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_MeasureUnit t3 on t1.FUnitID=t3.FItemID left join t_Stock t6 on t1.FStockID=t6.FItemID  left join t_StockPlace t7 on t1.FStockPlaceID=t7.FSPID where t1.FIsInStore='已入库' and t1.FItemID='"+FItemID+"' and t1.FStockID = '"+FStockID+"'";
                String SQL = "select  t_2043.FName as 产品系列,t1.FBarCode as 条码,t2.FNumber as 商品代码,t2.FName as 商品名称,t2.FModel as 规格 ,convert(float,t1.FQty) as 数量,t3.FName as 单位 from t_PDABarCodeSign t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_MeasureUnit t3 on t1.FUnitID=t3.FItemID left join t_Stock t6 on t1.FStockID=t6.FItemID  left join t_StockPlace t7 on t1.FStockPlaceID=t7.FSPID left join t_Item_2043  t_2043  on t2.F_109=t_2043.FItemID where t1.FIsInStore='已入库' and t1.FItemID='"+FItemID+"' and t1.FStockID = '"+FStockID+"'";
                Lg.e("SQL"+SQL);
                sta = conn.prepareStatement(SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    while (rs.next()) {
                        WebResponse.CheckLogBackBean bean = webResponse.new CheckLogBackBean();
                        bean.FBarCode = rs.getString("条码");
                        bean.FName = rs.getString("商品名称");
                        bean.FNumber = rs.getString("商品代码");
                        bean.FModel = rs.getString("规格");
                        bean.FNum = rs.getString("数量");
                        bean.FUnit = rs.getString("单位");
                        bean.FProductType = rs.getString("产品系列");
                        container.add(bean);
                    }
                }
                webResponse.state = true;
                webResponse.size = container.size();
                webResponse.backString = "二级库存查询成功";
                webResponse.checkLogBackBeans = container;
                Lg.e("条码检测并写入临时表成功：",webResponse);
                response.getWriter().write(gson.toJson(webResponse));
            } catch (SQLException e) {
                e.printStackTrace();
                Lg.e("数据库错误：","数据库错误："+e.toString());
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));
//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Lg.e("数据库错误：","数据库错误："+e.toString());
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));

//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }

//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
