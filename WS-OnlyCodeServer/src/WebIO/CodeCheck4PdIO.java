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
@WebServlet(urlPatterns = "/CodeCheck4PdIO")
public class CodeCheck4PdIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String barcode = request.getParameter("barcode");
        String pdfid = request.getParameter("pdfid");
//        String userid = request.getParameter("fid");
//        String imie = request.getParameter("imie");
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
//        if (null ==imie ||"".equals(imie)){
//            response.getWriter().write(gson.toJson(new WebResponse(false,"唯一码为空，添加失败")));
//            return;
//        }
        Lg.e("barcode",barcode);
        Lg.e("pdfid",pdfid);
        ArrayList<WebResponse.CodeCheckBackDataBean> container = new ArrayList<>();
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
                sta = conn.prepareStatement("exec proc_CheckBarCode_check ?,?");
                sta.setString(1, barcode);
                sta.setString(2, pdfid);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    while (rs.next()) {
                        WebResponse.CodeCheckBackDataBean cBean = webResponse.new CodeCheckBackDataBean();
                        cBean.FTip					=rs.getString("说明");
                        cBean.FItemID				=rs.getString("FItemID");
                        cBean.FUnitID				=rs.getString("FUnitID");
                        cBean.FQty					=rs.getString("FQty");
                        cBean.FStockID				=rs.getString("FStockID");
                        cBean.FStockPlaceID			=rs.getString("FStockPlaceID");
                        cBean.FBatchNo				=rs.getString("FBatchNo");
                        cBean.FKFPeriod				=rs.getString("FKFPeriod");
                        cBean.FKFDate				=rs.getString("FKFDate");
                        container.add(cBean);
                    }
                }
                Lg.e("得到数据",container);
                if ("OK".equals(container.get(0).FTip)){
                    webResponse.state = true;
                    webResponse.size = container.size();
                    webResponse.backString = "条码检测成功";
                    webResponse.codeCheckBackDataBeans = container;
                    Lg.e("条码检测成功：",webResponse);
                    response.getWriter().write(gson.toJson(webResponse));
                }else{
                    webResponse.state=false;
                    webResponse.backString = "条码检测失败："+container.get(0).FTip;
                    Lg.e("条码检测失败返回",webResponse);
                    response.getWriter().write(gson.toJson(webResponse));
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
