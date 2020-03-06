package Server.NumInStorage;

import Bean.DownloadReturnBean;
import Bean.InStoreNumRequestBean;
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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/GetInStoreNumBySW")
public class GetInStoreNumBySW extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        String json = request.getParameter("json");
        if(json!=null&&!json.equals("")){
            try {
                InStoreNumRequestBean iBean = new Gson().fromJson(json,InStoreNumRequestBean.class);
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip")
                        , request.getParameter("sqlport"), request.getParameter("sqlname")),
                        request.getParameter("sqlpass"), request.getParameter("sqluser"));
                System.out.println(request.getParameter("sqlip")+" "
                        +request.getParameter("sqlport")+" "+request.getParameter("sqlname")
                        +" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
                System.out.println(json);
                String sql = "select t2.FUnitID,t1.FItemID,t1.FStockID,convert(float,t1.FQty) as FQty,t1.FBal,t1.FStockPlaceID," +
                        "t1.FKFPeriod,t1.FKFDate,rtrim(t1.FBatchNo) AS FBatchNo FROM ICInventory t1 LEFT JOIN t_ICItem t2 on " +
                        "t1.FItemID = t2.FItemID WHERE " +
                        "t1.FStockID = ? AND t1.FStockPlaceID = ? AND t1.FQty>0";
                sta = conn.prepareStatement(sql);
                sta.setString(1,iBean.Storage);
                sta.setString(2,iBean.waveHouse);
                rs = sta.executeQuery();
                ArrayList<DownloadReturnBean.InstorageNum> container = new ArrayList<>();
                DownloadReturnBean dBean = new DownloadReturnBean();
                while (rs.next()) {
                    DownloadReturnBean.InstorageNum bean = dBean.new InstorageNum();
                    bean.FItemID = rs.getString("FItemID");
                    bean.FStockID = rs.getString("FStockID");
                    bean.FQty = rs.getString("FQty");
                    bean.FBal = rs.getString("FBal");
                    bean.FStockPlaceID = rs.getString("FStockPlaceID");
                    bean.FKFPeriod = rs.getString("FKFPeriod");
                    bean.FKFDate = rs.getString("FKFDate");
                    bean.FBatchNo = rs.getString("FBatchNo");
                    bean.FUnitID = rs.getString("FUnitID");
                    container.add(bean);
                }
                if(container.size()>0){
                    dBean.InstorageNum = container;
                    writer.write(CommonJson.getCommonJson(true,new Gson().toJson(dBean)));
                }else{
                    writer.write(CommonJson.getCommonJson(false,"未查询到数据"));

                }
            } catch (SQLException e) {
                writer.write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                writer.write(CommonJson.getCommonJson(false,"服务器错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
                e.printStackTrace();
            }finally {
                JDBCUtil.close(rs,sta,conn);
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
