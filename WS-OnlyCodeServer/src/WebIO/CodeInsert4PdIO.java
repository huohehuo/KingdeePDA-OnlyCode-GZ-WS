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
@WebServlet(urlPatterns = "/CodeInsert4PdIO")
public class CodeInsert4PdIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String barcode = request.getParameter("barcode");
        String pdname = request.getParameter("pdname");
//        String userid = request.getParameter("fid");
        String FStockID = request.getParameter("storageid");
        String FStockPlaceID = request.getParameter("wavehouseid");
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
            response.getWriter().write(gson.toJson(new WebResponse(false,"条码为空，写入临时表失败")));
            return;
        }
//        if (null ==userid ||"".equals(userid)){
//            response.getWriter().write(gson.toJson(new WebResponse(false,"用户ID为空，查询失败")));
//            return;
//        }
        if (null ==pdname ||"".equals(pdname)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"盘点方案名称为空，写入临时表失败")));
            return;
        }
        if (null ==FStockID ||"".equals(FStockID)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"仓库为空，写入临时表失败")));
            return;
        }
        if (null ==imie ||"".equals(imie)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"唯一码为空，写入临时表失败")));
            return;
        }
        Lg.e("barcode",barcode);
//        Lg.e("userid",userid);
        ArrayList<WebResponse.CodeCheckBackDataBean> container = new ArrayList<>();
//        Lg.e("列表",testBS);
            try {
                WebResponse webResponse = new WebResponse();

                        backString = "";
                        conn = JDBCUtil.getConn4Web();
                        sta2 = conn.prepareStatement("exec proc_CheckBarCode_Insert ?,?,?,?,?");
                        sta2.setString(1, pdname);
                        sta2.setString(2, imie);
                        sta2.setString(3, barcode);
                        sta2.setString(4, FStockID);
                        sta2.setString(5, FStockPlaceID);
                        rs2 = sta2.executeQuery();
//                        WebResponse webResponse = new WebResponse();
                        if(rs2!=null){
                            while (rs2.next()) {
                                backString	= rs2.getString("单据编号");
                            }
                        }
                        if ("OK".equals(backString)){
                            webResponse.state = true;
                            webResponse.backString = "条码写入临时表成功";
                            Lg.e("条码写入临时表成功：",webResponse);
                            response.getWriter().write(gson.toJson(webResponse));
                        }else{
                            webResponse.state = true;
                            webResponse.backString = "条码写入临时表失败："+backString;
                            Lg.e("写入失败返回",webResponse);
                            response.getWriter().write(gson.toJson(webResponse));
                        }
            } catch (SQLException e) {
//                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));
//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));

//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }

//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
