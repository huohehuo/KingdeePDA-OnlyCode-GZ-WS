package Server.Other;

import Bean.PDListReturnBean;
import Bean.PDMain;
import Utils.CommonJson;
import Utils.JDBCUtil;
import Utils.Lg;
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
import java.util.List;

/**
 * Created by NB on 2017/8/18.
 */
@WebServlet("/GetPDList")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        Gson gson = new Gson();
        try {
            List<PDMain> container = new ArrayList<>();
            conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
            System.out.println(request.getParameter("sqlip")+" "+request.getParameter("sqlport")+" "+request.getParameter("sqlname")+" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
//            sta = conn.prepareStatement("select a.FID,a.FProcessID,FRemark,a.FDate,b.fname as FUsername from icstockcheckprocess a join t_user b on a.foperatorid=b.fuserid  and a.fstatus=0  order by fid Desc");
            sta = conn.prepareStatement("select a.FID 顺序号,a.FName as 盘点方案名称,FRemark as 备注,a.FDate 日期,b.fname as 制单人 from t_PDABarCodeCheck a join t_user b on a.FUserID=b.fuserid  and a.fstatus=0  order by fid Desc");
            rs = sta.executeQuery();
            while(rs.next()){
                PDMain pdMain = new PDMain();
                pdMain.FID = rs.getString("顺序号");
                pdMain.FProcessId = rs.getString("盘点方案名称");
                pdMain.FDate = rs.getString("日期");
                pdMain.FUserName = rs.getString("制单人");
                pdMain.FRemark = rs.getString("备注");
                container.add(pdMain);
            }
            if(container.size()>0){
                PDListReturnBean pBean = new PDListReturnBean();
                pBean.items = container;
                Lg.e("盘点表头"+container.size(),container);
                response.getWriter().write(CommonJson.getCommonJson(true,gson.toJson(pBean)));
            }else{
                response.getWriter().write(CommonJson.getCommonJson(false,"无数据"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
