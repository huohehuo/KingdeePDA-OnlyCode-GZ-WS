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
 *              写入临时表   弃用
 */
@WebServlet(urlPatterns = "/CodeInsertIO")
public class CodeInsertIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String barcode = request.getParameter("barcode");
        String fid = request.getParameter("fid");
        String imie = request.getParameter("imie");
        String qty = request.getParameter("qty");
        String SQL = "";
        Gson gson = new Gson();
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        String backString = "";
        if (null ==barcode ||"".equals(barcode)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"条码为空，添加失败")));
            return;
        }
        if (null ==fid ||"".equals(fid)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"用户ID为空，添加失败")));
            return;
        }
        if (null ==qty ||"".equals(qty)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"数量为空，添加失败")));
            return;
        }
        if (null ==imie ||"".equals(imie)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"唯一码为空，添加失败")));
            return;
        }
        ArrayList<WebResponse.Product> container = new ArrayList<>();
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
//                SQL = "select FName as 用户名,FPassWord as 密码,FUserID,FID as 用户ID,isnull(FLevel,1) as FLevel,ISNULL(FPermit,'') as FPermit from t_UserPDASupply where FDeleted=0 AND FName ="+name+ " AND FPassWord = "+pwd;
//                sta = conn.prepareStatement(SQL);
                sta = conn.prepareStatement("exec proc_SupplyOutStoreBarCode_Insert ?,?,?,?,?");
                sta.setString(1, "0");
                sta.setString(2, imie);
                sta.setString(3, barcode);
                sta.setString(4, qty);
                sta.setString(5, fid);

//                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    while (rs.next()) {
                        backString					=rs.getString("单据编号");
                    }
                }
                if ("OK".equals(backString)){
                    webResponse.state = true;
                    webResponse.backString = "写入临时表成功";
//                    webResponse.products = container;
                    Lg.e("登录返回：",webResponse);
                    response.getWriter().write(gson.toJson(webResponse));
                }else{
                    response.getWriter().write(gson.toJson(new WebResponse(false,"条码写入临时表失败："+backString)));
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
