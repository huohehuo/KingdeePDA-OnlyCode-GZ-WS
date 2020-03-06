package Server.Check;

import Bean.CodeStateCheckBean;
import Bean.InStorageNumListBean;
import Bean.InStoreNumBean;
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
@WebServlet(urlPatterns = "/CodeStateCheck")
public class CodeStateCheck extends HttpServlet {
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
//            InStoreNumBean bean = new Gson().fromJson(json,InStoreNumBean.class);
            try {
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip")
                        , request.getParameter("sqlport"), request.getParameter("sqlname")),
                        request.getParameter("sqlpass"), request.getParameter("sqluser"));
                System.out.println(request.getParameter("sqlip")+" "
                        +request.getParameter("sqlport")+" "+request.getParameter("sqlname")
                        +" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));

                    con= con+" and t1.FBarCode='"+json+"'";
//                if (!"".equals(bean.FItemID)){
//                    con= con+" and t1.FItemID = "+bean.FItemID;
//                }
//                if (!"".equals(bean.FStockID)){
//                    con =con + " and t1.FStockID= "+bean.FStockID;
//                }
//                if (!"".equals(bean.FStockPlaceID)){
//                    con =con + "  and t1.FStockPlaceID= "+bean.FStockPlaceID;
//                }
                SQL = "select top 1 t1.FBarCode as 条码,t1.FDatePrint as 打印日期,t2.FNumber as 商品代码,t2.FName as 商品名称,t2.FModel as 规格,convert(decimal(28,0),t1.FQty) as 包装数量,convert(decimal(28,0),t1.FQtyOut) as 出库数量,convert(decimal(28,0),t1.FQty-t1.FQtyOut) as 剩余数量,t3.FName as 单位,t6.FName as 所属仓库,t7.FName as 所属仓位,t1.FIsInStore as 入库状态,t4.FBillNo as 入库单号,t1.FIsOutStore as 出库状态,t9.A as 出库单号,t1.FRemark6 as 入库类型,t1.FRemark7 as 出库类型,t1.FPrintType as 打印类型,t1.FBillNo as 订单号,t1.FBatchNo as 批次,t1.FKFDate as 生产日期,t1.FKFPeriod as 保质期,t1.FRemark1 as 备注1,t1.FRemark2 as 备注2,t1.FRemark3 as 备注3,t1.FRemark4 as 备注4 from t_PDABarCodeSign t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_MeasureUnit t3 on t1.FUnitID=t3.FItemID left join ICStockBill t4 on t1.FInterIDIn = t4.FInterID left join ICStockBill t5 on t1.FInterIDOut = t5.FInterID left join t_Stock t6 on t1.FStockID=t6.FItemID left join t_StockPlace t7 on t1.FStockPlaceID=t7.FSPID  left join ( SELECT FBarCode,STUFF((SELECT ',' + FRemark+':'+FBillNo  FROM t_PDABarCodeSign_Out t1 WHERE  isnull(t1.FStatus,1)<>0 and FBarCode=A.FBarCode FOR XML PATH('')),1, 1, '') AS A \n" +
                        "FROM t_PDABarCodeSign_Out A " +
                        "GROUP BY FBarCode ) t9 on t1.FBarCode = t9.FBarCode where 1=1 "+con;

                System.out.println("CodeStateCheck:"+SQL);
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
//                ArrayList<InStorageNumListBean.inStoreList> container = new ArrayList<>();
//                InStorageNumListBean iBean = new InStorageNumListBean();
                CodeStateCheckBean bean = new CodeStateCheckBean();
                while (rs.next()){
//                    InStorageNumListBean.inStoreList inBean = iBean.new inStoreList();
                    bean.FBarCode            = rs.getString("条码");
                    bean.FDatePrint          = rs.getString("打印日期");
                    bean.FNumber             = rs.getString("商品代码");
                    bean.FName               = rs.getString("商品名称");
                    bean.Model               = rs.getString("规格");
                    bean.FQty                = rs.getString("包装数量");
                    bean.FQtyOut             = rs.getString("出库数量");
                    bean.FQtyLast            = rs.getString("剩余数量");
                    bean.Unit                = rs.getString("单位");
                    bean.storage             = rs.getString("所属仓库");
                    bean.wavehouse           = rs.getString("所属仓位");
                    bean.FIsInStore          = rs.getString("入库状态");
                    bean.inStoreBillNo       = rs.getString("入库单号");
                    bean.FIsOutStore         = rs.getString("出库状态");
                    bean.outStoreBillNo      = rs.getString("出库单号");
                    bean.inStoreType         = rs.getString("入库类型");
                    bean.outStoreype         = rs.getString("出库类型");
                    bean.FPrintType          = rs.getString("打印类型");
                    bean.printBillno         = rs.getString("订单号");
                    bean.FBatchNo            = rs.getString("批次");
                    bean.FKFDate             = rs.getString("生产日期");
                    bean.FKFPeriod           = rs.getString("保质期");
                    bean.FRemark             = rs.getString("备注1");
                    bean.FRemark2            = rs.getString("备注2");
                    bean.FRemark3            = rs.getString("备注3");
                    bean.FRemark4            = rs.getString("备注4");


//                    container.add(inBean);
                }
                if(null!=bean.FBarCode){
                    System.out.println("有数据");
                    writer.write(CommonJson.getCommonJson(true,new Gson().toJson(bean)));
                }else{
                    System.out.println("无数据");
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
