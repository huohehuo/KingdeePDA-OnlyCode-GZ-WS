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
     *          条码检测     弃用
 */
@WebServlet(urlPatterns = "/CodeCheckIO")
public class CodeCheckIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String barcode = request.getParameter("barcode");
        String userid = request.getParameter("fid");
//        String imie = request.getParameter("imie");
        String SQL = "";
        Gson gson = new Gson();
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        String backString = "";
        if (null ==barcode ||"".equals(barcode)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"条码为空，查询失败")));
            return;
        }
        if (null ==userid ||"".equals(userid)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"用户ID为空，查询失败")));
            return;
        }
        Lg.e("barcode",barcode);
        Lg.e("userid",userid);
        ArrayList<WebResponse.Product> container = new ArrayList<>();
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
//                SQL = "select FName as 用户名,FPassWord as 密码,FUserID,FID as 用户ID,isnull(FLevel,1) as FLevel,ISNULL(FPermit,'') as FPermit from t_UserPDASupply where FDeleted=0 AND FName ="+name+ " AND FPassWord = "+pwd;
//                sta = conn.prepareStatement(SQL);
                sta = conn.prepareStatement("exec proc_SupplyOutStoreBarCode_check ?,?");
                sta.setString(1, barcode);
                sta.setString(2, userid);

                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    while (rs.next()) {
                        WebResponse.Product bean = webResponse.new Product();
                        Lg.e("执行",rs.getString("说明"));
                        backString =rs.getString("说明");
                        bean.FName = rs.getString("商品名称");
                        bean.FNumber = rs.getString("商品编码");
                        bean.FItemID				=rs.getString("FItemID");
                        bean.FUnitID				=rs.getString("FUnitID");
                        bean.FStockID				=rs.getString("FStockID");
                        bean.FStockPlaceID			=rs.getString("FStockPlaceID");
                        bean.FBatchNo				=rs.getString("FBatchNo");
                        bean.FKFPeriod				=rs.getString("FKFPeriod");
                        bean.FKFDate				=rs.getString("FKFDate");
                        bean.FPrice				=rs.getString("单价");
                        bean.FQty = rs.getString("FQty");
                        bean.FBarcode = barcode;
                        container.add(bean);
                    }
                }
                if ("OK".equals(backString)){
                        webResponse.state = true;
                        webResponse.backString = "条码检测成功";
                        webResponse.products = container;
                        Lg.e("条码检测成功：",webResponse);
                        response.getWriter().write(gson.toJson(webResponse));
                }else{
                    response.getWriter().write(gson.toJson(new WebResponse(false,"条码检测失败："+backString)));
                }

            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));
//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));

//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }

//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
