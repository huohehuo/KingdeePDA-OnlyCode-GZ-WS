package WebIO;

import Utils.JDBCUtil;
import Utils.Lg;
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
         * 条码记录查询一级
 */
@WebServlet(urlPatterns = "/CheckLogSearch1IO")
public class CheckLogSearch1IO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String parameter = request.getParameter("billno");
        String redorblue = request.getParameter("redorblue");//红字:-1 /蓝字：1 所有：0
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        String fid = request.getParameter("fid");
        String FLevel = request.getParameter("FLevel");
//        if (null ==parameter ||"".equals(parameter)){
//            response.getWriter().write(gson.toJson(new WebResponse(false,"条码为空，查询失败")));
//            return;
//        }
        if (null ==start ||"".equals(start)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"起始时间不能为空，查询失败")));
            return;
        }
        if (null ==end ||"".equals(end)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"结束时间不能为空，查询失败")));
            return;
        }
        if (null ==FLevel ||"".equals(FLevel)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"等级数据不能为空，查询失败")));
            return;
        }

        String SQL = "";
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        String condition = "";
        ArrayList<WebResponse.CheckLogBackBean> container = new ArrayList<>();
        System.out.println(parameter);
//        if (null!=parameter) {
            try {

                conn = JDBCUtil.getConn4Web();
//                CheckLogSearchBean searchBean = gson.fromJson(parameter, CheckLogSearchBean.class);
                if (parameter != null && !parameter.equals("")) {
                        condition += " and  t2.FBillNo = '" + parameter+"'";
                }
                if (redorblue != null) {
                    if ("-1".equals(redorblue)){
                        condition += " and  t2.FROB = -1";
                    }else if ("1".equals(redorblue)){
                        condition += " and  t2.FROB = 1";
                    }
                }
                if ("1".equals(FLevel)){
                    condition +=" and t_100.FID = '"+fid+"'";
                }
                if ("2".equals(FLevel)){
                    condition +=" and t2.FSupplyID in(select FCustID from t_UserPDASupply where FID = '"+fid+"')";
                }


//                if (searchBean.FClientID != null && !searchBean.FClientID.equals("")) {
//                    condition += " and t_100.FID = '" + searchBean.FClientID+"'";
//                }
//                if (searchBean.FStartTime != null && !searchBean.FStartTime.equals("") && searchBean.FEndTime != null && !searchBean.FEndTime.equals("")) {
                    condition += " and  t1.FDateUpLoad between " + "\'" + start + "\'" + "and" + "\'" + end +" 23:59:59"+ "\'";
//                }
//                SQL = "select t2.FBillNo as 单据编号,t3.FName as 客户名称,t5.FName as 商品名称,convert(float,SUM(t4.FQty)) as 数量,CONVERT(decimal(28,4),ISNULL( t1.FPrice,0)) as 单价,t1.FInterID  from t_PDABarCodeSign_Out t1 inner join ICStockBill t2 on t1.FInterID = t2.FInterID inner join t_Organization t3 on t2.FSupplyID = t3.FItemID inner join t_PDABarCodeSign t4 on t1.FBarCode=t4.FBarCode inner join t_ICItem t5 on t5.FItemID = t4.FItemID where t1.FTypeID = 21 "+condition+" group by t1.FInterID,t4.FItemID,t3.FName,t2.FBillNo,t5.FName,t1.FPrice";
//                SQL = "select t2.FBillNo as 单据编号,t3.FName as 客户名称,convert(float,SUM(t4.FQty)) as 数量,CONVERT(float,sum( t1.FPrice)) as 总价,t1.FInterID  from t_PDABarCodeSign_Out t1 inner join ICStockBill t2 on t1.FInterID = t2.FInterID inner join t_Organization t3 on t2.FSupplyID = t3.FItemID inner join t_PDABarCodeSign t4 on t1.FBarCode=t4.FBarCode inner join t_ICItem t5 on t5.FItemID = t4.FItemID where t1.FTypeID = 21 "+condition+" group by t1.FInterID,t3.FName,t2.FBillNo";
                SQL = "select t2.FBillNo as 单据编号,t3.FName as 客户名称,convert(float,SUM(t4.FQty)) as 数量,CONVERT(float,sum( isnull(t1.FPrice,0))) as 总价,case when t2.FROB = -1 then '红字' else '蓝字' end as 红蓝字,t_100.FName as 门店用户,t1.FInterID  from t_PDABarCodeSign_Out t1   inner join ICStockBill t2 on t1.FInterID = t2.FInterID inner join t_Organization t3 on t2.FSupplyID = t3.FItemID left join t_UserPDASupply t_100 on t1.FMDUserID=t_100.FID inner join t_PDABarCodeSign t4 on t1.FBarCode=t4.FBarCode inner join t_ICItem t5 on t5.FItemID = t4.FItemID  where t1.FTypeID = 21 "+condition+" group by t1.FInterID,t3.FName,t2.FBillNo,t2.FROB,t_100.FName";
                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    int i = rs.getRow();
                    System.out.println("rs的长度"+i);
                    while (rs.next()) {
                        WebResponse.CheckLogBackBean bean = webResponse.new CheckLogBackBean();
                        bean.FBillNo = rs.getString("单据编号");
                        bean.FCLientName = rs.getString("客户名称");
//                        bean.FName = rs.getString("商品名称");
                        bean.FNum = rs.getString("数量");
                        bean.FPrice = rs.getString("总价");
                        bean.FRedOrBlue = rs.getString("红蓝字");
                        bean.FMDUser = rs.getString("门店用户");
                        bean.FInterID = rs.getString("FInterID");

                        container.add(bean);
                    }
                    webResponse.state = true;
                    webResponse.size = container.size();
                    webResponse.backString = "一级数据获取成功";
                    webResponse.checkLogBackBeans = container;
                    Lg.e("一级数据获取成功：",webResponse);
                    response.getWriter().write(gson.toJson(webResponse));
                }else{
                    response.getWriter().write(gson.toJson(new WebResponse(false,"未查询到数据")));
                }


            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));

            }

//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
