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
 *           销售出库：条码检测与写入临时表
 */
@WebServlet(urlPatterns = "/CodeCheckAndInsert4CqPdIO")
public class CodeCheckAndInsert4CqPdIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String barcode = request.getParameter("barcode");
        String pdfid = request.getParameter("pdfid");
//        String userid = request.getParameter("fid");
        String imie = request.getParameter("imie");
        String qty="0";
        Gson gson = new Gson();
        Connection conn = null;
        PreparedStatement sta = null;
        PreparedStatement sta2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String backString = "";
        if (null ==barcode ||"".equals(barcode)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"条码为空，查询失败")));
            return;
        }
//        if (null ==userid ||"".equals(userid)){
//            response.getWriter().write(gson.toJson(new WebResponse(false,"用户ID为空，查询失败")));
//            return;
//        }
        if (null ==pdfid ||"".equals(pdfid)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"盘点方案ID为空，查询失败")));
            return;
        }
        if (null ==imie ||"".equals(imie)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"唯一码为空，添加失败")));
            return;
        }
        Lg.e("barcode",barcode);
//        Lg.e("userid",userid);
        ArrayList<WebResponse.Product> container = new ArrayList<>();
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
                sta = conn.prepareStatement("exec proc_CheckBarCodeFirst_check ?,?");
                sta.setString(1, barcode);
                sta.setString(2, pdfid);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    while (rs.next()) {
                        WebResponse.Product bean = webResponse.new Product();
                        Lg.e("执行",rs.getString("说明"));
                        backString =rs.getString("说明");
                        bean.FName = rs.getString("物料名称");
                        bean.FNumber = rs.getString("物料代码");
                        bean.FModel = rs.getString("规格型号");
//                        bean.FItemID				=rs.getString("FItemID");
//                        bean.FUnitID				=rs.getString("FUnitID");
//                        bean.FStockID				=rs.getString("FStockID");
//                        bean.FStockPlaceID			=rs.getString("FStockPlaceID");
//                        bean.FBatchNo				=rs.getString("FBatchNo");
//                        bean.FKFPeriod				=rs.getString("FKFPeriod");
//                        bean.FKFDate				=rs.getString("FKFDate");
//                        bean.FPrice				=rs.getString("单价");
//                        bean.FQty = rs.getString("FQty");
//                        bean.FProductType = rs.getString("产品系列");
//                        bean.FBelongStorage = rs.getString("所属仓库");
//                        qty = rs.getString("FQty");
                        bean.FBarcode = barcode;
                        container.add(bean);
                    }
                }
                if ("OK".equals(backString)){
                        backString = "";
                        conn = JDBCUtil.getConn4Web();
                        sta2 = conn.prepareStatement("exec proc_CheckBarCodeFirst_Insert ?,?,?");
                        sta2.setString(1, pdfid);
                        sta2.setString(2, imie);
                        sta2.setString(3, barcode);
                        rs2 = sta2.executeQuery();
//                        WebResponse webResponse = new WebResponse();
                        if(rs2!=null){
                            while (rs2.next()) {
                                backString	= rs2.getString("单据编号");
                            }
                        }
                        if ("OK".equals(backString)){
                            webResponse.state = true;
                            webResponse.backString = "条码检测并写入临时表成功";
                            webResponse.products = container;
                            Lg.e("条码检测并写入临时表成功：",webResponse);
                            response.getWriter().write(gson.toJson(webResponse));
                        }else{
                            response.getWriter().write(gson.toJson(new WebResponse(false,"条码写入临时表失败："+backString)));
                        }
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
