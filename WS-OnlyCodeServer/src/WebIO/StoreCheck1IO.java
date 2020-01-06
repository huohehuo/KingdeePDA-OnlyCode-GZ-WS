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

import static Utils.JDBCUtil.ReadAsChars;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/StoreCheck1IO")
public class StoreCheck1IO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String parameter=null;
        try{
            parameter = ReadAsChars(request);//解密数据
        }catch (Exception e){
//            response.getWriter().write(gson.toJson(new WebResponse(false,"上传失败,请求体解析错误")));
        }
//        String barcode = request.getParameter("number");
//        String fid = request.getParameter("fid");
//        String userid = request.getParameter("name");
//        String imie = request.getParameter("storage_name");
        String qty="0";
        Connection conn = null;
        PreparedStatement sta = null;
        PreparedStatement sta2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String backString = "";
        String condition = "";
        if (null ==parameter ||"".equals(parameter)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"查询条件拼接出错，查询失败")));
            return;
        }
        PostBean pBean = gson.fromJson(parameter, PostBean.class);

        if (null !=pBean.number &&!"".equals(pBean.number)){
            condition += " and  t2.FNumber like '%" + pBean.number+"%'";
//            response.getWriter().write(gson.toJson(new WebResponse(false,"条码为空，查询失败")));
//            return;
        }
        if (null !=pBean.name &&!"".equals(pBean.name)){
            condition += " and t2.FName like '%" + pBean.name+"%'";
        }

        if (null !=pBean.storage_ids &&!"".equals(pBean.storage_ids)){
            condition += " and t1.FStockID in(" + pBean.storage_ids+")";
        }
        //过滤产品系列
        if (null !=pBean.product_type_ids &&!"".equals(pBean.product_type_ids)){
            condition += " and t_2043.FItemID in(" + pBean.product_type_ids+")";
        }
//        condition += " and  t2.FNumber like '%" + barcode+"%' and t2.FName like '%" + userid+"%' and t3.FName like '%" + imie+"%'";


        ArrayList<WebResponse.CheckLogBackBean> container = new ArrayList<>();
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
//                String SQL = "select t2.FModel,t2.FNumber as 物料编码,t2.FName as 物料名称,convert(float,t1.FQty) as 基本单位库存,convert(float,t1.FSecQty) as 辅助单位库存,t6.FName as 辅助单位,t5.FName as 基本单位,t3.FName as 仓库,t4.FName as 仓位,t1.FBatchNo as 批次,t1.FKFDate as 生产日期,t1.FKFPeriod as 保质期,t1.FStockID,t1.FItemID from ICInventory t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_stock t3 on t1.FStockID = t3.FItemID left join t_stockPlace t4 on t1.FStockPlaceID = t4.FSPID left join t_Measureunit t5 on t2.FUnitID = t5.FItemID left join t_Measureunit t6 on t2.FSecUnitID = t6.FItemID  where 1=1 and t1.FQty<>0 and t1.FStockID in(select FStockID from t_UserPDASupplyStock where FID = "+fid+" ) "+condition;
                String SQL = "select t_2043.FName as 产品系列,t2.FModel,t2.FNumber as 物料编码,t2.FName as 物料名称,convert(float,t1.FQty) as 基本单位库存,convert(float,t1.FSecQty) as 辅助单位库存,t6.FName as 辅助单位,t5.FName as 基本单位,t3.FName as 仓库,t4.FName as 仓位,t1.FBatchNo as 批次,t1.FKFDate as 生产日期,t1.FKFPeriod as 保质期,t1.FStockID,t1.FItemID from ICInventory t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_stock t3 on t1.FStockID = t3.FItemID left join t_stockPlace t4 on t1.FStockPlaceID = t4.FSPID left join t_Measureunit t5 on t2.FUnitID = t5.FItemID left join t_Measureunit t6 on t2.FSecUnitID = t6.FItemID left join t_Item_2043  t_2043  on t2.F_109=t_2043.FItemID where 1=1 and t1.FQty<>0 and t1.FStockID in(select FStockID from t_UserPDASupplyStock where FID = "+pBean.fid+" ) "+condition;
                Lg.e("SQL"+SQL);
                sta = conn.prepareStatement(SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    while (rs.next()) {
                        WebResponse.CheckLogBackBean bean = webResponse.new CheckLogBackBean();
                        bean.FNumber = rs.getString("物料编码");
                        bean.FName = rs.getString("物料名称");
                        bean.FBaseQty = rs.getString("基本单位库存");
                        bean.FSecQty = rs.getString("辅助单位库存");
                        bean.FSecUnit = rs.getString("辅助单位");
                        bean.FBaseUnit = rs.getString("基本单位");
                        bean.FBatchNo = rs.getString("批次");
                        bean.FStorage = rs.getString("仓库");
                        bean.FWaveHouse = rs.getString("仓位");
                        bean.FKFDate = rs.getString("生产日期");
                        bean.FKFPeriod = rs.getString("保质期");
                        bean.FStockID = rs.getString("FStockID");
                        bean.FItemID = rs.getString("FItemID");
                        bean.FProductType = rs.getString("产品系列");
                        container.add(bean);
                    }
                }

                webResponse.state = true;
                webResponse.size = container.size();
                webResponse.backString = "库存查询成功";
                webResponse.checkLogBackBeans = container;
                Lg.e("条码检测并写入临时表成功：",webResponse);
                response.getWriter().write(gson.toJson(webResponse));
            } catch (SQLException e) {
                e.printStackTrace();
                Lg.e("数据库错误：","数据库错误："+e.toString());
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));
//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Lg.e("数据库错误：","数据库错误："+e.toString());
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));

//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }

//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
