package Server.ProductSearch;

import Bean.DownloadReturnBean;
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
import java.util.ArrayList;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/SearchStorage")
public class SearchStorage extends HttpServlet {
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
        ArrayList<DownloadReturnBean.storage> container = new ArrayList<>();
        System.out.println(parameter);
        if (parameter != null) {
            try {
                System.out.println(request.getParameter("sqlip") + " " + request.getParameter("sqlport") + " " + request.getParameter("sqlname") + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
//                SQL = "SELECT  FInterID, FID,FName  FROM t_SubMessage Where FInterID>0  AND FDeleted=0  And FTypeID=10001 and (FInterID like '%"+parameter+"%' or FName like '%"+parameter+"%') order by FID";//专业版
                SQL = "select t1.FItemID,t1.FEmpID,t1.FName,t1.FNumber,t1.FTypeID,t1.FSPGroupID,t1.FGroupID,t1.FStockGroupID,t1.FIsStockMgr,t1.FUnderStock from t_Stock t1 left join t_Item t2 on t1.FItemID=t2.FItemID WHERE t2.FItemClassID=5 AND t2.FDetail=1  AND (((FTypeID not in (501,502,503)) and FTypeID <> 504)) AND t2.FDeleteD=0  and (t1.FNumber like '%"+parameter+"%' or t1.FName like '%"+parameter+"%')";//专业版
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
                if(rs!=null){
                    int i = rs.getRow();
                    System.out.println("rs的长度"+i);
                    while (rs.next()) {
                        DownloadReturnBean.storage bean = downloadReturnBean.new storage();
                        bean.FItemID = rs.getString("FItemID");
                        bean.FEmpID = rs.getString("FEmpID");
                        bean.FName = rs.getString("FName");
                        bean.FNumber = rs.getString("FNumber");
                        bean.FTypeID = rs.getString("FTypeID");
                        bean.FSPGroupID = rs.getString("FSPGroupID");
                        bean.FGroupID = rs.getString("FGroupID");
                        bean.FStockGroupID = rs.getString("FStockGroupID");
                        bean.FIsStockMgr = rs.getString("FIsStockMgr");
                        bean.FUnderStock = rs.getString("FUnderStock");
                        container.add(bean);
                    }
                    downloadReturnBean.storage = container;
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
