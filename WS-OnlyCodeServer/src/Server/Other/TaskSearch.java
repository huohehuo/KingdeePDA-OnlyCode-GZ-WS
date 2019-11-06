package Server.Other;

import Bean.TaskBean;
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

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/TaskSearch")
public class TaskSearch extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        String json = request.getParameter("json");
        if(json!=null&&!json.equals("")){
            try {
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip")
                        , request.getParameter("sqlport"), request.getParameter("sqlname")),
                        request.getParameter("sqlpass"), request.getParameter("sqluser"));
                System.out.println(request.getParameter("sqlip")+" "
                        +request.getParameter("sqlport")+" "+request.getParameter("sqlname")
                        +" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
                String SQL = "select t1.FBillNo  as FBillNo,case t1.FStartFlag when 0 then '未开工' else '开工' end as FStartFlag,Case t1.FStatus when 0 then '计划' when 3 then '结案' when 5 then '确认' Else '下达' end AS FStatus,t2.FNumber as FNumber,t2.FName as FName,t2.FModel as FModel,CONVERT( float,t1.FAuxQty) as FAuxQty,t1.FGMPBatchNo as FGMPBatchNo,t3.FName as FDepartment, t1.FStatus AS FStatus1,FStartFlag AS FStartFlag1,t1.FSuspend, t1.FCancellation ,t1.FInterID from ICMO t1 left join t_ICItem t2 on t1.FItemID=t2.FItemID left join t_Department t3 on t1.FWorkShop = t3.FItemID where t1.FBillNo=?";
                sta = conn.prepareStatement(SQL);
                sta.setString(1,json);
                rs = sta.executeQuery();
                if(rs!=null&&rs.next()){
                    TaskBean tBean = new TaskBean();
                    tBean.FAuxQty = rs.getString("FAuxQty");
                    tBean.FBillNo = rs.getString("FBillNo");
                    tBean.FStartFlag = rs.getString("FStartFlag");
                    tBean.FStatus = rs.getString("FStatus");
                    tBean.FNumber = rs.getString("FNumber");
                    tBean.FName = rs.getString("FName");
                    tBean.FModel = rs.getString("FModel");
                    tBean.FGMPBatchNo = rs.getString("FGMPBatchNo");
                    tBean.FDepartment = rs.getString("FDepartment");
                    tBean.FSuspend = rs.getString("FSuspend");
                    tBean.FCancellation = rs.getString("FCancellation");
                    tBean.FInterID = rs.getString("FInterID");
                    tBean.FStartFlag1 = rs.getString("FStartFlag1");
                    tBean.FStatus1 = rs.getString("FStatus1");
                    writer.write(CommonJson.getCommonJson(true,new Gson().toJson(tBean)));
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
