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
import java.sql.SQLException;

/**
 * Servlet implementation class PurchaseInStoreUpload
 */
@WebServlet("/WebCqPdUpload")
public class WebCqPdUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebCqPdUpload() {
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
            parameter = ReadAsChars(request);//解密数据
        }catch (Exception e){
            response.getWriter().write(gson.toJson(new WebResponse(false,"上传失败,请求体解析错误")));
        }
		Connection conn = null;
		PreparedStatement sta = null;
		boolean execute = true;
		if(parameter!=null&&!parameter.equals("")){
			try {
				conn = JDBCUtil.getConn4Web();
				PostBean pBean = gson.fromJson(parameter, PostBean.class);
//				for(int i =0;i<pBean.list.size();i++){
					sta = conn.prepareStatement("exec proc_UpdateBarCodeFirst ?");
//					String main = pBean.list.get(i).main;
					sta.setString(1, pBean.json);
//					sta.setString(2, "");
//					sta.setString(3, "");
//					sta.setString(4, "");
//					sta.setString(5, "");
//					sta.setString(6, "");
//					for(int j = 0;j<pBean.list.get(i).detail.size();j++){
//						sta.setString(j+2, pBean.list.get(i).detail.get(j));
//					}
					execute = sta.execute();
					System.out.println(execute+"");
					
//				}
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
