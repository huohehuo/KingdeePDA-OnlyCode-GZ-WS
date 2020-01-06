package WebIO.UploadIO;

import Bean.PurchaseInStoreUploadBean;
import Utils.JDBCUtil;
import Utils.Lg;
import WebIO.WebResponse;
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
@WebServlet("/WebCreateSOUpload")
public class WebCreateSOUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebCreateSOUpload() {
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
        String parameter= null;
		Lg.e("到达");
		try{
//            getbody = ReadAsChars(request);//获取post请求中的body数据
//            PostBean postBean = gson.fromJson(getbody, PostBean.class);//解析
//            parameter = Base64Util.decryptBASE64(postBean.json);//解密数据
            parameter = JDBCUtil.ReadAsChars(request);//解密数据
        }catch (Exception e){
            response.getWriter().write(gson.toJson(new WebResponse(false,"上传失败,请求体解析错误")));
        }

//		String json = "{\"list\": [{\"detail\": [\"7738|337|5.0000000000|1.0|565|0|||0\"],\"main\": \"1|0|123456789\"}]}{\"list\": [{\"detail\": [\"7738|337|5.0000000000|1.0|565|0|||0\"],\"main\": \"1|0|123456789\"}]}{\"list\": [{\"detail\": [\"7738|337|5.0000000000|1.0|565|0|||0\"],\"main\": \"1|0|123456789\"}]}{\"list\": [{\"detail\": [\"7738|337|5.0000000000|1.0|565|0|||0\"],\"main\": \"1|0|123456789\"}]}{\"list\": [{\"detail\": [\"7738|337|5.0000000000|1.0|565|0|||0\"],\"main\": \"1|0|123456789\"}]}";
//		Lg.e("获得请求1", Base64Util.encryptBASE64(json));
//        String jiami =Base64Util.encryptBASE64(json) ;
//		Lg.e("获得请求2", Base64Util.decryptBASE64(jiami));
//		Lg.e("获得请求3", MD5Util.convertMD5(MD5Util.convertMD5(json)));
//		Lg.e("获得请求3", MD5Util.convertMD5(json));

		Connection conn = null;
		PreparedStatement sta = null;
		boolean execute = true;
		if(parameter!=null&&!parameter.equals("")){
			try {
				conn = JDBCUtil.getConn4Web();
				PurchaseInStoreUploadBean pBean = gson.fromJson(parameter, PurchaseInStoreUploadBean.class);
				for(int i =0;i<pBean.list.size();i++){
					sta = conn.prepareStatement("exec proc_SupplySaleOrder ?,?,?,?,?,?");
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
					execute = sta.execute();
					System.out.println(execute+"");
					
				}
				if(!execute){
				    Lg.e("返回",new WebResponse(true,"上传成功"));
					response.getWriter().write(gson.toJson(new WebResponse(true,"上传成功")));
//					response.getWriter().write(CommonJson.getCommonJson(true, ""));
				}else{
				    Lg.e("返回",new WebResponse(false,"上传失败"));
					response.getWriter().write(gson.toJson(new WebResponse(false,"上传失败")));
//					response.getWriter().write(CommonJson.getCommonJson(false, "上传失败"));
				}
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));
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

}
