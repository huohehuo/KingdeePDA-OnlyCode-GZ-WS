package Server.Check;

import Bean.CheckInOutBean;
import Bean.DownloadReturnBean;
import Bean.InOutBean;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Servlet implementation class PurchaseInStoreUpload
 */
@WebServlet("/CheckCodeOut")
public class CheckCodeOut extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckCodeOut() {
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
		ArrayList<InOutBean> list = new ArrayList<>();
		if(parameter!=null&&!parameter.equals("")){
			try {
				DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
				System.out.println(parameter);
				conn = JDBCUtil.getConn(request);
				CheckInOutBean bean = gson.fromJson(parameter, CheckInOutBean.class);
				sta = conn.prepareStatement("exec proc_OutStoreBarCode_check ?");

					sta.setString(1, bean.FBarCode);
					rs = sta.executeQuery();
					if (rs != null) {
						while (rs.next()) {
							InOutBean cBean = new InOutBean();
							cBean.FTip					=rs.getString("说明");
							cBean.FBillNo				=rs.getString("FBillNo");
							cBean.FItemID					=rs.getString("FItemID");
							cBean.FUnitID				=rs.getString("FUnitID");
							cBean.FQty					=rs.getString("FQty");
							cBean.FStockID				=rs.getString("FStockID");
							cBean.FStockPlaceID			=rs.getString(	"FStockPlaceID");
							cBean.FBatchNo				=rs.getString("FBatchNo");
							cBean.FKFPeriod			=rs.getString("FKFPeriod");
							cBean.FKFDate			=rs.getString("FKFDate");
							cBean.FNumber					=rs.getString("FNumber");
							list.add(cBean);
						}
					}

				downloadReturnBean.inOutBeans = list;
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
