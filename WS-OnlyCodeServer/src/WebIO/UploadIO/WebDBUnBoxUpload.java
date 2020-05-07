package WebIO.UploadIO;

import Utils.JDBCUtil;
import Utils.Lg;
import WebIO.PostBean;
import WebIO.WebResponse;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Servlet implementation class PurchaseInStoreUpload
 */
@WebServlet("/WebDBUnBoxUpload")
public class WebDBUnBoxUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebDBUnBoxUpload() {
        super();
        // TODO Auto-generat/WebCreateSOUploaded constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String getbody=null;
        ResultSet rs = null;
        String parameter= null;
		Lg.e("到达");
		try{
            parameter = ReadAsChars(request);//解密数据
        }catch (Exception e){
            response.getWriter().write(gson.toJson(new WebResponse(false,"上传失败,请求体解析错误")));
        }
		Connection conn = null;
		PreparedStatement sta = null;
		boolean execute = true;
        ArrayList<WebResponse.CodeCheckBackDataBean> container = new ArrayList<>();
		if(parameter!=null&&!parameter.equals("")){
            WebResponse webResponse = new WebResponse();
			try {
				conn = JDBCUtil.getConn4Web();
				PostBean pBean = gson.fromJson(parameter, PostBean.class);
					sta = conn.prepareStatement("exec proc_UpdatePDAAssembleSplit1 ?");
					sta.setString(1, pBean.json);
                rs = sta.executeQuery();
                if(rs!=null){
                    while (rs.next()) {
                        WebResponse.CodeCheckBackDataBean cBean = webResponse.new CodeCheckBackDataBean();
                        cBean.FTip					=rs.getString("说明");
                        cBean.FBillNo				=rs.getString("FBillNo");
                        cBean.FItemID				=rs.getString("FItemID");
                        cBean.FUnitID				=rs.getString("FUnitID");
                        cBean.FQty					=rs.getString("FQty");
                        cBean.FStockID				=rs.getString("FStockID");
                        cBean.FStockPlaceID			=rs.getString("FStockPlaceID");
                        cBean.FBatchNo				=rs.getString("FBatchNo");
                        cBean.FKFPeriod				=rs.getString("FKFPeriod");
                        cBean.FKFDate				=rs.getString("FKFDate");
                        container.add(cBean);
                    }
                }
                Lg.e("得到数据",container);
                if (container.size()>0){
                    webResponse.state = true;
                    webResponse.size = container.size();
                    webResponse.backString = "拆箱成功";
//                    webResponse.codeCheckBackDataBeans = container;
                    response.getWriter().write(gson.toJson(webResponse));
                }else{
                    webResponse.state=false;
                    webResponse.backString = "拆箱失败,无任何数据";
                    response.getWriter().write(gson.toJson(webResponse));
                }
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                webResponse.state=false;
                webResponse.backString = "拆箱失败："+e.toString();
                response.getWriter().write(gson.toJson(webResponse));
//				response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));
//				response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

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
    // 字符串读取post请求中的body数据
    private String ReadAsChars(HttpServletRequest request)
    {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
