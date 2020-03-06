package Server.prop;

import Bean.ConnectSQLBean;
import Utils.CommonJson;
import Utils.JDBCUtil;
import Utils.getDataBaseUrl;
import com.google.gson.Gson;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Servlet implementation class SetPropties
 */
@WebServlet("/SetPropties")
public class SetPropties extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetPropties() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		Connection conn = null;
		Connection conn1 = null;
		PreparedStatement sta = null;
		Statement sta1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String version = null;
		String VersionResulr = "";
		try {
			if(request.getParameter("json")!=null&&!(request.getParameter("json").equals(""))){
				ConnectSQLBean cBean = gson.fromJson(request.getParameter("json"), ConnectSQLBean.class);
				conn1 = JDBCUtil.getConn(getDataBaseUrl.getUrl(cBean.ip, cBean.port, cBean.database), cBean.password, cBean.username);
				sta1 = conn1.createStatement();
				System.out.println(request.getParameter("json"));
				rs1 = sta1.executeQuery("Select FValue From t_SystemProfile Where FCategory='Base' and FKey ='ServicePack'");
				if(rs1.next()){
					System.out.println(1+":"+conn1+"");
					version = rs1.getString("FValue");
					System.out.println("version:"+version);
					VersionResulr = version.replace(".", "");
					System.out.println(2+"VersionResulr:"+VersionResulr);
					conn = JDBCUtil.getSQLiteConn();
					System.out.println(3+":"+conn+"");
					sta = conn.prepareStatement("select * from p_CreateProc where FVersion=? order by FDescription");
					sta.setString(1, VersionResulr);
					rs = sta.executeQuery();
					int updateNum=0;
						while (rs.next()) {
							try{
								int result = sta1.executeUpdate(rs.getString("FSqlStr"));
								System.out.println( rs.getString("FRemark")+"ִ执行结果："+result+"");
								updateNum++;
								if(result!=-1&&result!=0){
									response.getWriter().write(CommonJson.getCommonJson(false,"result:"+result+"\r\n错误项目"+ rs.getString("FRemark")));
								}
							}catch (SQLServerException e) {
								e.printStackTrace();
								response.getWriter().write(CommonJson.getCommonJson(false, "SQLException:"+rs.getString("FRemark")+"\r\n"+e.toString()));
							}
							
						}
					System.out.println(updateNum+"");
					if(updateNum>0&&rs.isAfterLast()){
						response.getWriter().write(CommonJson.getCommonJson(true,VersionResulr));
					}else{
						response.getWriter().write(CommonJson.getCommonJson(false,"配置错误,请确认指定文件是否防止正确"));

					}
				}else{
					response.getWriter().write(CommonJson.getCommonJson(false, "未查询到语句"));
				}
			}else{
				response.getWriter().write(CommonJson.getCommonJson(false,  "Json有误"));
			}
			
			
//		
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
		}
		finally{
			try {
				if(sta!=null){
					sta.close();
				}
				if(rs!=null){rs.close();}
				if(conn!=null){conn1.close();}
				if(sta!=null){sta1.close();}
				if(rs!=null){rs1.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
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
