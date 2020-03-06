package Server.NumInStorage;

import Bean.InStorageNumListBean;
import Bean.InStoreNumBean;
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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/GetInStorageNumList")
public class GetInStorageNumList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        String json = request.getParameter("json");
        String SQL = "";
        String con = "";
        if(json!=null&&!json.equals("")){
            InStoreNumBean bean = new Gson().fromJson(json,InStoreNumBean.class);
            try {
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip")
                        , request.getParameter("sqlport"), request.getParameter("sqlname")),
                        request.getParameter("sqlpass"), request.getParameter("sqluser"));
                System.out.println(request.getParameter("sqlip")+" "
                        +request.getParameter("sqlport")+" "+request.getParameter("sqlname")
                        +" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
                if (null!=bean.FItemID && !"".equals(bean.FItemID)){
                    con= con+" and t1.FItemID = "+bean.FItemID;
                }
                if (null!=bean.FStockID && !"".equals(bean.FStockID)){
                    con =con + " and t1.FStockID= "+bean.FStockID;
                }
                if (null!=bean.FStockPlaceID && !"".equals(bean.FStockPlaceID)){
                    con =con + "  and t1.FStockPlaceID= "+bean.FStockPlaceID;
                }
                if (null!=bean.FBatchNo && !"".equals(bean.FBatchNo)){
                    con =con + "  and t1.FBatchNo= "+bean.FBatchNo;
                }
                Lg.e("查询条件",bean);
                SQL = "select t2.FModel,t2.FNumber as 物料编码,t2.FName as 物料名称,convert(float,t1.FQty) as 基本单位库存,convert(float,t1.FSecQty) as 辅助单位库存,t6.FName as 辅助单位,t5.FName as 基本单位,t3.FName as 仓库,t4.FName as 仓位,t1.FBatchNo as 批次,t1.FKFDate as 生产日期,t1.FKFPeriod as 保质期 from ICInventory t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_stock t3 on t1.FStockID = t3.FItemID left join t_stockPlace t4 on t1.FStockPlaceID = t4.FSPID left join t_Measureunit t5 on t2.FUnitID = t5.FItemID left join t_Measureunit t6 on t2.FSecUnitID = t6.FItemID where 1=1 "+con;

                System.out.println("GetInStorageNumList:"+SQL);
//                if(json!=null&&json!=null&) {
//                    SQL = "select t2.FNumber as 物料编码,t2.FName as 物料名称,convert(float,t1.FQty) as 基本单位库存,convert(float,t1.FSecQty) as 辅助单位库存,t5.FName as 基本单位,t3.FName as 仓库,t4.FName as 仓位,t1.FBatchNo as 批次,t1.FKFDate as 生产日期,t1.FKFPeriod as 保质期 from ICInventory t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_stock t3 on t1.FStockID = t3.FItemID left join t_stockPlace t4 on t1.FStockPlaceID = t4.FSPID left join t_Measureunit t5 on t2.FUnitID = t5.FItemID where 1=1 " + json;
//                    sta = conn.prepareStatement(SQL);
//                    sta.setString(1,json);
//                    sta.setString(2,json);
//                    sta.setString(3,json);
//                }
//                esle if(json!=null)
//                {
//                    SQL = "select t2.FNumber as 物料编码,t2.FName as 物料名称,convert(float,t1.FQty) as 基本单位库存,convert(float,t1.FSecQty) as 辅助单位库存,t5.FName as 基本单位,t3.FName as 仓库,t4.FName as 仓位,t1.FBatchNo as 批次,t1.FKFDate as 生产日期,t1.FKFPeriod as 保质期 from ICInventory t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_stock t3 on t1.FStockID = t3.FItemID left join t_stockPlace t4 on t1.FStockPlaceID = t4.FSPID left join t_Measureunit t5 on t2.FUnitID = t5.FItemID where t1.FItemID=? ";
//                    sta = conn.prepareStatement(SQL);
//                    sta.setString(1,json);
//                }
//                else  if(json!=null)
//                {
//                    SQL = "select t2.FNumber as 物料编码,t2.FName as 物料名称,convert(float,t1.FQty) as 基本单位库存,convert(float,t1.FSecQty) as 辅助单位库存,t5.FName as 基本单位,t3.FName as 仓库,t4.FName as 仓位,t1.FBatchNo as 批次,t1.FKFDate as 生产日期,t1.FKFPeriod as 保质期 from ICInventory t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_stock t3 on t1.FStockID = t3.FItemID left join t_stockPlace t4 on t1.FStockPlaceID = t4.FSPID left join t_Measureunit t5 on t2.FUnitID = t5.FItemID where   t1.FStockID=? and t1.FStockPlaceID=?";
//                    sta = conn.prepareStatement(SQL);
//                    sta.setString(1,json);
//                    sta.setString(2,json);
//                }
//                else
//                {
//                    SQL = "select t2.FNumber as 物料编码,t2.FName as 物料名称,convert(float,t1.FQty) as 基本单位库存,convert(float,t1.FSecQty) as 辅助单位库存,t5.FName as 基本单位,t3.FName as 仓库,t4.FName as 仓位,t1.FBatchNo as 批次,t1.FKFDate as 生产日期,t1.FKFPeriod as 保质期 from ICInventory t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_stock t3 on t1.FStockID = t3.FItemID left join t_stockPlace t4 on t1.FStockPlaceID = t4.FSPID left join t_Measureunit t5 on t2.FUnitID = t5.FItemID ";
//                    sta = conn.prepareStatement(SQL);
//                }
                sta = conn.prepareStatement(SQL);
                rs = sta.executeQuery();
                ArrayList<InStorageNumListBean.inStoreList> container = new ArrayList<>();
                InStorageNumListBean iBean = new InStorageNumListBean();
                while (rs.next()){
                    InStorageNumListBean.inStoreList inBean = iBean.new inStoreList();
                    inBean.FNumber = rs.getString("物料编码");
                    inBean.FName = rs.getString("物料名称");
                    inBean.FQty = rs.getString("基本单位库存");
                    inBean.FSecQty = rs.getString("辅助单位库存");
                    inBean.FUnit = rs.getString("基本单位");
                    inBean.FSecUnit = rs.getString("辅助单位");
                    inBean.FStockID = rs.getString("仓库");
                    inBean.FStockPlaceID = rs.getString("仓位");
                    inBean.FBatchNo = rs.getString("批次");
                    inBean.FKFDate = rs.getString("生产日期");
                    inBean.FKFPeriod = rs.getString("保质期");
                    inBean.FModel = rs.getString("FModel");
                    container.add(inBean);
                }
                if(container.size()>0){
                    iBean.list = container;
                    writer.write(CommonJson.getCommonJson(true,new Gson().toJson(iBean)));
                }else{
                    writer.write(CommonJson.getCommonJson(false,"未查询到数据"));
                }
            } catch (SQLException e) {
                writer.write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                writer.write(CommonJson.getCommonJson(false,"服务器错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));
                e.printStackTrace();
            }finally {
                JDBCUtil.close(rs,sta,conn);
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
