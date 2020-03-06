package WebSide;

import Utils.Lg;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于更新时间控制表的当前时间
 */
@WebServlet(urlPatterns = "/HistoryBack")
public class HistoryBack extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        Lg.e("到达TestBack");
        String paramter = request.getParameter("name");
        Lg.e("到达TestBack"+paramter);
//        String url = "http://oa.o-in.me:9001/ena13_manages/"+paramter+".json";
        String url = "http://oa.o-in.me:9001/repairs/history.json?code="+paramter;
        String getJson = HttpRequestUtils.sendGet(url);
        Lg.e("得到数据",getJson);
        BackDataList list = new Gson().fromJson(getJson, BackDataList.class);
//        BackData[] iBean = new Gson().fromJson(getJson,BackData[].class);
        Lg.e("得到json数据",list);
        request.setAttribute("jsonback", list);
        request.getRequestDispatcher("HistoryMsgResult.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
