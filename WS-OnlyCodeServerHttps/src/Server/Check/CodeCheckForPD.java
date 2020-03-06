package Server.Check;

import Bean.CodeCheckBean;
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
 * Servlet implementation class PurchaseInStoreUpload
 */
@WebServlet("/CodeCheckForPD")
public class CodeCheckForPD extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeCheckForPD() {
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
		ResultSet rs = null;
		ArrayList<DownloadReturnBean.CodeCheckBackDataBean> list = new ArrayList<>();
		if(parameter!=null&&!parameter.equals("")){
			try {
				DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
				System.out.println(parameter);
				conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
				System.out.println(request.getParameter("sqlip")+" "+request.getParameter("sqlport")+" "+request.getParameter("sqlname")+" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));

				CodeCheckBean bean = gson.fromJson(parameter, CodeCheckBean.class);
				sta = conn.prepareStatement("exec proc_CheckBarCode_check ?,?");

//					sta.setString(1, bean.FOrderID);
//					sta.setString(2, bean.FPDAID);
					sta.setString(1, bean.FBarCode);
					sta.setString(2, bean.FOrderID);
					rs = sta.executeQuery();
					if (rs != null) {
						while (rs.next()) {
							DownloadReturnBean.CodeCheckBackDataBean cBean = downloadReturnBean.new CodeCheckBackDataBean();
							cBean.FTip					=rs.getString("说明");
							cBean.FItemID				=rs.getString("FItemID");
							cBean.FUnitID				=rs.getString("FUnitID");
							cBean.FQty					=rs.getString("FQty");
							cBean.FStockID				=rs.getString("FStockID");
							cBean.FStockPlaceID			=rs.getString("FStockPlaceID");
							cBean.FBatchNo				=rs.getString("FBatchNo");
							cBean.FKFPeriod				=rs.getString("FKFPeriod");
							cBean.FKFDate				=rs.getString("FKFDate");
							list.add(cBean);
						}
					}

				downloadReturnBean.codeCheckBackDataBeans = list;
				System.out.println(list.toString());
				response.getWriter().write(CommonJson.getCommonJson(true,gson.toJson(downloadReturnBean)));
//				response.getWriter().write(CommonJson.getCommonJson(true, ""));
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
