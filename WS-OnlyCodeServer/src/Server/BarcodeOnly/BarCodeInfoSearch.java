package Server.BarcodeOnly;

import Bean.BarCodeInfo;
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
@WebServlet(urlPatterns = "/BarCodeInfoSearch")
public class BarCodeInfoSearch extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        String json = request.getParameter("json");
        if(json!=null&&!json.equals("")){
            try {
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip")
                        , request.getParameter("sqlport"), request.getParameter("sqlname")),
                        request.getParameter("sqlpass"), request.getParameter("sqluser"));
                System.out.println(request.getParameter("sqlip")+" "
                        +request.getParameter("sqlport")+" "+request.getParameter("sqlname")
                        +" "+request.getParameter("sqlpass")+" "+request.getParameter("sqluser"));
                String SQL = "select " +
                        "t1.FBarCode as 条码," +
                        "t1.FDatePrint as 打印日期," +
                        "t2.FNumber as 商品代码," +
                        "t2.FName as 商品名称," +
                        "t2.FName as 规格," +
                        "CONVERT(float, t1.FQty) as 数量," +
                        "t1.FBatchNo as 批次," +
                        "t3.FName as 单位," +
                        "t6.FName as 所属仓库," +
                        "t7.FName as 所属仓位," +
                        "t1.FIsInStore as 入库状态," +
                        "t1.FIsOutStore as 出库状态," +
                        "t4.FBillNo as 入库单号," +
                        "t5.FBillNo as 出库单号," +
                        "t1.FRemark1 as 入库类型," +
                        "t1.FRemark2 as 出库类型," +
                        "t1.FPrintType as 打印类型," +
                        "t1.FBillNo as 打印单号," +
                        "t1.FRemark as 备注," +
                        "t1.FCustom1 as 直拉管类," +
                        "t1.FCustom2 as 定制非卖," +
                        "t1.FCustom3 as 气泡," +
                        "t1.FCustom4 as 气线," +
                        "t1.FCustom5 as 构棱," +
                        "t1.FCustom6 as 白点," +
                        "t1.FCustom7  as 划伤," +
                        "t1.FCustom8  as 水印," +
                        "t1.FCustom9  as 色线," +
                        "t1.FCustom10 as 麻,  " +
                        "t1.FCustom11  as 变色, " +
                        " t1.FCustom12 as 酸斑, " +
                        " t1.FCustom13 as 炸纹, " +
                        " t1.FCustom14  as 烟圈气圈, " +
                        " t1.FCustom15 as 坏头, " +
                        " t1.FCustom16 as 大小头, " +
                        " t1.FCustom17 as 杂规格,  " +
                        "t1.FCustom18 as 过渡,  " +
                        "t1.FCustom19 as 椭圆,  " +
                        "t1.FCustom20 as 偏壁,  " +
                        "t1.FCustom21 as 析晶,  " +
                        "t1.FCustom22 as 炉口,  " +
                        "t1.FCustom23 as 弯,  " +
                        "t1.FCustom24 as 漏料,  " +
                        "t1.FCustom25 as 粘异物,  " +
                        "t1.FCustom26 as 烧口,  " +
                        "t1.FCustom27 as 黑点,  " +
                        "t1.FCustom28 as 波纹,  " +
                        "t1.FCustom29 as 螺纹,  " +
                        "t1.FCustom30 as 管渣,  " +
                        "t1.FCustom31 as 崩口,  " +
                        "t1.FCustom32 as 修理痕,  " +
                        "t1.FCustom33 as 丝头,  " +
                        "t1.FCustom34 as 粘合物," +
                        "t11.FName as 入库制单人," +
                        "t12.FName as 出库制单人," +
                        "t1.FDateInStore as 入库日期," +
                        "t1.FDateOutStore as 出库日期," +
                        "t1.FID,ROW_NUMBER()over(order by t1.FID desc) as 序号 " +
                        "from t_PDABarCodeSign t1 " +
                        "left join t_ICItem t2 on t1.FItemID = t2.FItemID left join " +
                        "t_MeasureUnit t3 on t1.FUnitID=t3.FItemID left join ICStockBill t4 " +
                        "on t1.FInterIDIn = t4.FInterID left join ICStockBill t5 on " +
                        "t1.FInterIDOut = t5.FInterID left join t_Stock t6 on t1.FStockID=t6.FItemID " +
                        "left join t_StockPlace t7 on t1.FStockPlaceID=t7.FSPID  left join t_User t11 " +
                        "on t1.FUserInStore=t11.FUserID  left join t_User t12 on " +
                        "t1.FUserOutStore=t12.FUserID where 1=1 and t1.FBarCode = ?";

                System.out.println("SQL:"+SQL);
                System.out.println("JSON:"+json);
                sta = conn.prepareStatement(SQL);
                sta.setString(1,json);
                rs = sta.executeQuery();
                if(rs.next()){
                    BarCodeInfo b = new BarCodeInfo();
                    b.FBarCode = rs.getString("条码");
                    b.FDatePrint = rs.getString("打印日期");
                    b.FNumber = rs.getString("商品代码");
                    b.FName = rs.getString("商品名称");
                    b.FQty = rs.getString("数量");
                    b.Model = rs.getString("规格");
                    b.FBatchNo = rs.getString("批次");
                    b.Unit = rs.getString("单位");
                    b.storage = rs.getString("所属仓库");
                    b.wavehouse = rs.getString("所属仓位");
                    b.FIsInStore = rs.getString("入库状态");
                    b.FIsOutStore = rs.getString("出库状态");
                    b.inStoreBillNo = rs.getString("入库单号");
                    b.outStoreBillNo = rs.getString("出库单号");
                    b.inStoreType = rs.getString("入库类型");
                    b.outStoreype = rs.getString("出库类型");
                    b.FPrintType = rs.getString("打印类型");
                    b.printBillno = rs.getString("打印单号");
                    b.FRemark = rs.getString("备注");
                    b.FCustom1 = rs.getString("直拉管类");
                    b.FCustom2 = rs.getString("定制非卖");
                    b.FCustom3 = rs.getString("气泡");
                    b.FCustom4 = rs.getString("气线");
                    b.FCustom5 = rs.getString("构棱");
                    b.FCustom6 = rs.getString("白点");
                    b.FCustom7 = rs.getString("划伤");
                    b.FCustom8 = rs.getString("水印");
                    b.FCustom9 = rs.getString("色线");
                    b.FCustom10 = rs.getString("麻");
                    b.FCustom11 = rs.getString("变色");
                    b.FCustom12 = rs.getString("酸斑");
                    b.FCustom13 = rs.getString("炸纹");
                    b.FCustom14 = rs.getString("烟圈气圈");
                    b.FCustom15 = rs.getString("坏头");
                    b.FCustom16 = rs.getString("大小头");
                    b.FCustom17 = rs.getString("杂规格");
                    b.FCustom18 = rs.getString("过渡");
                    b.FCustom19 = rs.getString("椭圆");
                    b.FCustom20 = rs.getString("偏壁");
                    b.FCustom21 = rs.getString("析晶");
                    b.FCustom22 = rs.getString("炉口");
                    b.FCustom23 = rs.getString("弯");
                    b.FCustom24 = rs.getString("漏料");
                    b.FCustom25 = rs.getString("粘异物");
                    b.FCustom26 = rs.getString("烧口");
                    b.FCustom27 = rs.getString("黑点");
                    b.FCustom28 = rs.getString("波纹");
                    b.FCustom29 = rs.getString("螺纹");
                    b.FCustom30 = rs.getString("管渣");
                    b.FCustom31 = rs.getString("崩口");
                    b.FCustom32 = rs.getString("修理痕");
                    b.FCustom33 = rs.getString("丝头");
                    b.FCustom34 = rs.getString("粘合物");
                    b.inMaker = rs.getString("入库制单人");
                    b.outMaker = rs.getString("出库制单人");
                    b.FDateInStore = rs.getString("入库日期");
                    b.FDateOutStore = rs.getString("出库日期");
                    b.FID = rs.getString("序号");
                    writer.write(CommonJson.getCommonJson(true,new Gson().toJson(b)));
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
