package Server.Check;

import Bean.PurchaseInStoreUploadBean;
import Utils.CommonJson;
import Utils.JDBCUtil;
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
 */
@WebServlet("/DeleteForOnlyCode")
public class DeleteForOnlyCode extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteForOnlyCode() {
        super();
    }



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				conn = JDBCUtil.getConn(request);
				PurchaseInStoreUploadBean pBean = gson.fromJson(parameter, PurchaseInStoreUploadBean.class);
				for(int i =0;i<pBean.list.size();i++){
					sta = conn.prepareStatement("exec proc_DeleteDetailsTable ?,?,?,?,?");
//					String main = pBean.list.get(i).main;
//					sta.setString(1, main);
					sta.setString(1, "");
					sta.setString(2, "");
					sta.setString(3, "");
					sta.setString(4, "");
					sta.setString(5, "");
					for(int j = 0;j<pBean.list.get(i).detail.size();j++){
						sta.setString(j+1, pBean.list.get(i).detail.get(j));
					}
					execute = sta.execute();
					System.out.println(execute+"");
				}
				if (!execute){
					response.getWriter().write(CommonJson.getCommonJson(true, ""));
				}else{
					response.getWriter().write(CommonJson.getCommonJson(false, ""));
				}


			} catch (ClassNotFoundException | SQLException e) {
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
		doGet(request, response);
	}

}
