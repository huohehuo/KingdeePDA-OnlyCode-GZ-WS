package Server.prop;

import Bean.ConnectResponseBean;
import Bean.ConnectSQLBean;
import Utils.*;
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
 * Servlet implementation class ConnectDataBase
 */
@WebServlet("/ConnectDataBase")
public class ConnectDataBase extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConnectDataBase() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet rs = null;
		ConnectSQLBean sBean = null;
		Gson gson = new Gson();
		ArrayList<ConnectResponseBean.DataBaseList> container = new ArrayList<ConnectResponseBean.DataBaseList>();
		if(request.getParameter("json")!=null&&!request.getParameter("json").equals("")){
			System.out.println(request.getParameter("json"));
			try{
				sBean = gson.fromJson(request.getParameter("json"), ConnectSQLBean.class);
			}catch(Exception e){
				response.getWriter().write("服务器解析失败");
			}
			
			try {
				System.out.println("1");
				conn  = JDBCUtil.getConn(getDataBaseUrl.getUrl(sBean.ip, sBean.port, sBean.database), sBean.password, sBean.username);
				sta = conn.prepareStatement(SQLInfo.GETDATABASE);
				rs = sta.executeQuery();
				ConnectResponseBean connectResponseBean = new ConnectResponseBean();
				while(rs.next()){
					ConnectResponseBean.DataBaseList dBean = connectResponseBean.new DataBaseList();
					dBean.dataBaseName = rs.getString("cdbname");
					dBean.name = rs.getString("cacc_name");
					container.add(dBean);
				}
				Lg.e("账套",container);
				if(container.size()>0){
					response.getWriter().write(CommonJson.getCommonJson(true, JsonCreater.ConnectResponse(container)));
				}else{
					response.getWriter().write(CommonJson.getCommonJson(true, JsonCreater.ConnectResponse(container)));
				}
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
			}finally {
				JDBCUtil.close(rs,sta,conn);
			}
		
		}else{
			response.getWriter().write(CommonJson.getCommonJson(false, "json数据有误"));
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
