package Server.upload;

import Bean.DownloadReturnBean;
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

/**
 * Servlet implementation class PurchaseInStoreUpload
 */
@WebServlet("/UnBoxUpload")
public class UnBoxUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UnBoxUpload() {
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
		ResultSet rs = null;
		ArrayList<DownloadReturnBean.CodeCheckBackDataBean> list = new ArrayList<>();
		if(parameter!=null&&!parameter.equals("")){
			try {
				DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
				System.out.println(parameter);
				conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
				System.out.println(request.getParameter("sqlip")+" "+request.getParameter("sqlport")+" "+request.getParameter("sqlname")+" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
				
//				PurchaseInStoreUploadBean pBean = gson.fromJson(parameter, PurchaseInStoreUploadBean.class);
					sta = conn.prepareStatement("exec proc_UpdatePDAAssembleSplit1 ?");
					sta.setString(1, parameter);
					rs = sta.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						DownloadReturnBean.CodeCheckBackDataBean cBean = downloadReturnBean.new CodeCheckBackDataBean();
						cBean.FTip					=rs.getString("说明");
//						cBean.FBillNo				=rs.getString("FBillNo");
//						cBean.FItemID				=rs.getString("FItemID");
//						cBean.FUnitID				=rs.getString("FUnitID");
//						cBean.FQty					=rs.getString("FQty");
//						cBean.FStockID				=rs.getString("FStockID");
//						cBean.FStockPlaceID			=rs.getString("FStockPlaceID");
//						cBean.FBatchNo				=rs.getString("FBatchNo");
//						cBean.FKFPeriod				=rs.getString("FKFPeriod");
//						cBean.FKFDate				=rs.getString("FKFDate");
						list.add(cBean);
					}
				}

				downloadReturnBean.codeCheckBackDataBeans = list;
				Lg.e("返回",list);
				response.getWriter().write(CommonJson.getCommonJson(true,gson.toJson(downloadReturnBean)));


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
