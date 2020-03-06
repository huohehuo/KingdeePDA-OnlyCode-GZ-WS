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
 */
@WebServlet("/InsertForOnlyCodeOut")
public class InsertForOnlyCodeOut extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertForOnlyCodeOut() {
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
		ArrayList<InOutBean> list = new ArrayList<>();
		if(parameter!=null&&!parameter.equals("")){
			try {
				DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
				System.out.println(parameter);
				conn = JDBCUtil.getConn(request);
				CheckInOutBean bean = gson.fromJson(parameter, CheckInOutBean.class);
					sta = conn.prepareStatement("exec proc_OutStoreBarCode_Insert ?,?,?,?");
				sta.setString(1, bean.FOrderID);
				sta.setString(2, bean.FPDAID);
				sta.setString(3, bean.FBarCode);
				sta.setString(4, bean.FSN);
//				sta.setString(5, bean.FBatchNo);
//				sta.setString(6, bean.FStockID);
//				sta.setString(7, bean.FStockPlaceID);
				rs = sta.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						InOutBean cBean = new InOutBean();
						cBean.FTip					=rs.getString("单据编号");
						list.add(cBean);
					}
				}

				downloadReturnBean.inOutBeans = list;
				System.out.println(list.toString());
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
