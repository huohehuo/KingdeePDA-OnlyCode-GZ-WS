package Server.NumInStorage;

import Bean.InStoreNumBean;
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

/**
 * Created by NB on 2017/8/7.alksdjlfkja
 *
 *              查看库存
 */
@WebServlet(urlPatterns = "/GetInStoreNum")
public class GetInStoreNum extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String parameter = request.getParameter("json");
        String version = request.getParameter("version");
        String SQL;
        String num;
        String con = "";
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        System.out.println("GetInStoreNum--json:"+parameter);
        if (parameter != null && !parameter.equals("")) {
            InStoreNumBean iBean = new Gson().fromJson(parameter,InStoreNumBean.class);
            System.out.println(request.getParameter("sqlip") + " " + request.getParameter("sqlport") + " " + request.getParameter("sqlname") + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
            if(iBean.FKFDate!=null){
                con = "and FKFDate ="+iBean.FKFDate;
            }
            try {
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
                SQL = "select convert(float,FQty) as FQty FROM ICInventory where FItemID = ? and FStockID = ? and FStockPlaceID = ? and FBatchNo = ? "+con;

                System.out.println("GetInStoreNum--SQL:"+SQL);
                sta = conn.prepareStatement(SQL);
                sta.setString(1,iBean.FItemID);
                sta.setString(2,iBean.FStockID);
                sta.setString(3,iBean.FStockPlaceID);
                sta.setString(4,iBean.FBatchNo);
                rs = sta.executeQuery();
                if(rs!=null){
                    rs.next();
                    if (rs.getRow()>=0){
                        num = rs.getString("FQty");
                        System.out.println("库存:"+num);
                        response.getWriter().write(CommonJson.getCommonJson(true,num));
                    }else{
                        response.getWriter().write(CommonJson.getCommonJson(true,"0.00"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(CommonJson.getCommonJson(false,"无库存信息"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write(CommonJson.getCommonJson(false,"数据库驱动错误"));
            }

        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
