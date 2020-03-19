package Server.upload;

import Bean.DownloadReturnBean;
import Bean.PurchaseInStoreUploadBean;
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
@WebServlet("/CGDDPDSLTZDUpload")
public class CGDDPDSLTZDUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CGDDPDSLTZDUpload() {
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
		ArrayList<DownloadReturnBean.PrintHistory> list = new ArrayList<>();
		if(parameter!=null&&!parameter.equals("")){
			try {
				DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
				System.out.println(parameter);
				conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
				System.out.println(request.getParameter("sqlip")+" "+request.getParameter("sqlport")+" "+request.getParameter("sqlname")+" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
				
				PurchaseInStoreUploadBean pBean = gson.fromJson(parameter, PurchaseInStoreUploadBean.class);
				for(int i =0;i<pBean.list.size();i++){
					sta = conn.prepareStatement("exec proc_PurchaseOrderReceiving ?,?,?,?,?,?");
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
					rs = sta.executeQuery();
					if (rs != null) {
						while (rs.next()) {
							DownloadReturnBean.PrintHistory cBean = downloadReturnBean.new PrintHistory();
							cBean.FBarCode					=rs.getString("单据编号");
							cBean.FBJMan					=rs.getString("报检人");
							cBean.FName					=rs.getString("物料名称");
							cBean.FNumber					=rs.getString("物料代码");
							cBean.FModel					=rs.getString("规格型号");
							cBean.FNum					=rs.getString("报检数量");
							cBean.FSupplier					=rs.getString("供应商");
							cBean.FPlanType					=rs.getString("计划类别");
							cBean.tag					="14";//所属单据的tag标识
							list.add(cBean);
						}
					}
				}
				if (list.size()>0){
					Lg.e("返回：",list);
					downloadReturnBean.printHistories = list;
					response.getWriter().write(CommonJson.getCommonJson(true, gson.toJson(downloadReturnBean)));
				}else{
					response.getWriter().write(CommonJson.getCommonJson(false, "回单失败"));
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
