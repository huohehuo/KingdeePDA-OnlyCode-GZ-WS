package Server.prop;

import Bean.TimeBean;
import Utils.CommonJson;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by NB on 2017/8/17.
 */
@WebServlet("/GetServerTime")
public class GetServerTime extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Date date = new Date();
        long curTime = date.getTime();
        TimeBean timeBean = new TimeBean();
        timeBean.time = curTime;
        Gson gson = new Gson();
        response.getWriter().write(CommonJson.getCommonJson(true,gson.toJson(timeBean)));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
