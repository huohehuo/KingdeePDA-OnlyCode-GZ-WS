package Server.upload;

import Bean.PurchaseInStoreUploadBean;
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
import java.sql.SQLException;

/**
 * Servlet implementation class PurchaseInStoreUpload
 */
@WebServlet("/ProduceAndGetUpload")
public class ProduceAndGetUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProduceAndGetUpload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String parameter = request.getParameter("json");
		Gson gson = new Gson();
		Connection conn = null;
		PreparedStatement sta = null;
		boolean execute = true;
		if(parameter!=null&&!parameter.equals("")){
			try {
				System.out.println(parameter);
				conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
				System.out.println(request.getParameter("sqlip")+" "+request.getParameter("sqlport")+" "+request.getParameter("sqlname")+" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
				
				PurchaseInStoreUploadBean pBean = gson.fromJson(parameter, PurchaseInStoreUploadBean.class);
				for(int i =0;i<pBean.list.size();i++){
					sta = conn.prepareStatement("exec proc_ProductPicking1 ?,?,?,?,?,?");
					String main = pBean.list.get(i).main;
					sta.setString(1, main);
					sta.setString(2, "");
					sta.setString(3, "");
					sta.setString(4, "");
					sta.setString(5, "");
					sta.setString(6, "");
					for(int j = 0;j<pBean.list.get(i).detail.size();j++){
						sta.setString(j+2, pBean.list.get(i).detail.get(j));
					}
					System.out.println("SQL:"+sta.toString());
					execute = sta.execute();
					System.out.println(execute+"");
					
				}
				response.getWriter().write(CommonJson.getCommonJson(true, ""));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
			}finally {
				JDBCUtil.close(null,sta,conn);
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
