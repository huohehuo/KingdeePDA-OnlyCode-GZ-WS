package Server.Other;

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
import java.util.ArrayList;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/LoginInfo")
public class LoginInfo extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
                ArrayList<LoginBean.User> items = new ArrayList<>();
                LoginBean l = new LoginBean();
                LoginBean.User u = l.new User();
                u.username = "a";
                u.pwd = "123";
                items.add(u);
                LoginBean l1 = new LoginBean();
                LoginBean.User u1 = l.new User();
                u1.username = "b";
                u1.pwd = "123";
                items.add(u1);
                LoginBean l2 = new LoginBean();
                LoginBean.User u2 = l.new User();
                u2.username = "c";
                u2.pwd = "123";
                items.add(u2);
                l.items = items;
                writer.write(CommonJson.getCommonJson(true,new Gson().toJson(l)));



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
