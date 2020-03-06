package Server.prop;

import Utils.CommonJson;
import Utils.JDBCUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Servlet implementation class GetFile
 */
@WebServlet("/GetFile")
public class GetFile extends HttpServlet {


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        Connection conn = null;
        PreparedStatement sta = null;
        String version = request.getParameter("version");
        String rem = request.getParameter("rem");
        System.out.println("Rem:"+rem);
        try {
            conn = JDBCUtil.getSQLiteConn();
            String SQL = "UPDATE NEW_VERSION SET Version = ?,Rem = ?  WHERE id = 1";
            sta = conn.prepareStatement(SQL);
            sta.setInt(1, Integer.parseInt(version));
            sta.setString(2,rem);
            int i = sta.executeUpdate();
            if(i>0){
                writer.write("ok");
            }else{
                writer.write("no");
            }
        } catch (ClassNotFoundException | SQLException e) {
            response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            e.printStackTrace();
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}


}
