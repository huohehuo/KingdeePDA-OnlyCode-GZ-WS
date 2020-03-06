package Server.Check;

import Bean.DownloadReturnBean;
import Bean.PurchaseInStoreUploadBean;
import Bean.SendOrderListBean;
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
@WebServlet("/getSendOrderRedList")
public class getSendOrderRedList extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public getSendOrderRedList() {
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
		ArrayList<SendOrderListBean> list = new ArrayList<>();

		if(parameter!=null&&!parameter.equals("")){
			try {
				DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
				System.out.println(parameter);
				conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
				System.out.println(request.getParameter("sqlip")+" "+request.getParameter("sqlport")+" "+request.getParameter("sqlname")+" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
				
				PurchaseInStoreUploadBean pBean = gson.fromJson(parameter, PurchaseInStoreUploadBean.class);
				for(int i =0;i<pBean.list.size();i++) {
				    sta = conn.prepareStatement("exec proc_LogisticsFilter_Red ?,?,?,?,?");
//					String main = pBean.list.get(i).main;
                    sta.setString(1, "");
                    sta.setString(2, "");
                    sta.setString(3, "");
                    sta.setString(4, "");
                    sta.setString(5, "");
//					sta.setString(6, "");
                        Lg.e("执行"+i,pBean.list.get(i).detail.size());
                    for (int j = 0; j < pBean.list.get(i).detail.size(); j++) {
                        sta.setString(j+1, pBean.list.get(i).detail.get(j));
                    }
                }
				rs = sta.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						SendOrderListBean cBean = new SendOrderListBean();
						cBean.FTip					=rs.getString("说明");
						cBean.FItemID				=rs.getString("FItemID");
						cBean.FUnitID				=rs.getString("FUnitID");
						cBean.FUnit				=rs.getString("单位");
						cBean.FQty					=rs.getString("订单数量");
						cBean.FQtying					="0";
						cBean.FModel				=rs.getString("规格型号");
						cBean.FName			=rs.getString("物料名称");
						cBean.FNumber				=rs.getString("物料编码");
						cBean.FWlNo				=rs.getString("物流单号");
						cBean.FSaleNo				=rs.getString("销售订单号");
						cBean.FStorageID				=rs.getString("仓库ID");
						cBean.FStorage				=rs.getString("仓库名称");
						cBean.FWaveHouseID				=rs.getString("仓位ID");
						cBean.FActivity				=pBean.list.get(0).main;
						list.add(cBean);
					}
				}

				downloadReturnBean.sendOrderListBeans = list;
				Lg.e("getSendOrderList",list);
				response.getWriter().write(CommonJson.getCommonJson(true,gson.toJson(downloadReturnBean)));
//				}
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
