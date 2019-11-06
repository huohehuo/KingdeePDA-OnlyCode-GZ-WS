package Server.BarcodeOnly;

import Bean.DetailsTableBean;
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
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/DeleteCheckIsUsed")
public class DeleteCheckIsUsed extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        String parameter = request.getParameter("json");
        if(parameter!=null){
                try {
                    String FBarCode = parameter;
                    conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
                    System.out.println("/InsertCheckIsUsed"+request.getParameter("sqlip")+" "+request.getParameter("sqlport")+" "+request.getParameter("sqlname")+" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
                    String SQL = "DELETE FROM a_DetailsTable WHERE FBarCode = ?";
                    sta = conn.prepareStatement(SQL);
                    sta.setString(1,FBarCode);
                    System.out.println("FBarCode"+FBarCode);
                    int result = sta.executeUpdate();
                    if(result>0){
                        response.getWriter().write(CommonJson.getCommonJson(true,""));
                    }else{
                        response.getWriter().write(CommonJson.getCommonJson(false,"未找到数据或数据删除失败"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

                } catch (ClassNotFoundException e) {
                    response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

                }finally {
                    JDBCUtil.close(rs,sta,conn);
                }


        }else{
            response.getWriter().write(CommonJson.getCommonJson(false,"数据有误"));
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
