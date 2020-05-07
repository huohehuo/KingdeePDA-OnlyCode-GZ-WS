package WebIO.SearchIO;

import Utils.JDBCUtil;
import Utils.Lg;
import WebIO.WebResponse;
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
@WebServlet(urlPatterns = "/ProductIO")
public class ProductIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String parameter = request.getParameter("FItemID");
//        if (null ==fid ||"".equals(fid)){
//            response.getWriter().write(gson.toJson(new WebResponse(false,"用户ID为空，查询失败")));
//            return;
//        }
//        String version = request.getParameter("version");
        String SQL = "";
        String con = "";
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        ArrayList<WebResponse.Product> container = new ArrayList<>();
//        System.out.println(parameter);
//        if (parameter != null) {
//        List<TestB> testBS = new ArrayList<>();
//        testBS.add(new TestB("one","A"));
//        testBS.add(new TestB("one2","B"));
//        testBS.add(new TestB("one3","C"));
//        Lg.e("列表",testBS);

            try {
                conn = JDBCUtil.getConn4Web();
                SQL = "select top 50 FSecCoefficient,FSecUnitID,FIsSnManage,FItemID,FNumber,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod,FModel,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,'' as FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 and (FItemID = "+parameter+") order by FNumber"+con;
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    int i = rs.getRow();
                    while (rs.next()) {
                        WebResponse.Product bean = webResponse.new Product();
                        bean.FSecCoefficient = rs.getString("FSecCoefficient");
                        bean.FSecUnitID = rs.getString("FSecUnitID");
                        bean.FBatchManager = rs.getString("FBatchManager");
                        bean.FIsSnManage = rs.getString("FIsSnManage");
                        bean.FDefaultLoc = rs.getString("FDefaultLoc");
                        bean.FItemID = rs.getString("FItemID");
                        bean.FModel = rs.getString("FModel");
                        bean.FISKFPeriod = rs.getString("FISKFPeriod");
                        bean.FKFPeriod = rs.getString("FKFPeriod");
                        bean.FName = rs.getString("FName");
                        bean.FNumber = rs.getString("FNumber");
                        bean.FSalePrice = rs.getString("FSalePrice");
                        bean.FSPID = rs.getString("FSPID");
                        bean.FUnitGroupID = rs.getString("FUnitGroupID");
                        bean.FUnitID = rs.getString("FUnitID");
                        container.add(bean);
                    }
                    webResponse.state=true;
                    webResponse.size = container.size();
                    webResponse.products = container;
                    Lg.e("返回数据：",webResponse);
                    response.getWriter().write(gson.toJson(webResponse));
//                    response.getWriter().write(CommonJson.getCommonJson(true,"{answer:123}"));
//                    response.getWriter().write("{\"answer\":\"one\",\"value\":\"A\"}");
                }else{
                    response.getWriter().write(gson.toJson(new WebResponse(false,"数据库表查询失败-nothing data")));
//                    response.getWriter().write(CommonJson.getCommonJson(false,"未查询到数据"));
                }


            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库表查询失败-SQL Check Error")));
//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库表查询失败")));
//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }

//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
