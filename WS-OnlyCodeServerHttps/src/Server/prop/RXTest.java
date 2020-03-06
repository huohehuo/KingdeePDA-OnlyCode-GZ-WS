package Server.prop;

import Bean.User;
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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/RXTest")
public class RXTest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        String json = request.getParameter("json");
        if(json!=null&&!json.equals("")){
            User user = new Gson().fromJson(json,User.class);
            if(user.name.equals("123")&&user.pass.equals("123")){
                writer.write(CommonJson.getCommonJson(true,new Gson().toJson(new User("wly","123"))));
            }else{
                writer.write(CommonJson.getCommonJson(false,"SB"));
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
