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
 * 销售出库分类汇总
 */
@WebServlet(urlPatterns = "/ScanForStoreIO")
public class ScanForStoreIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String parameter=null;
//        try{
//            parameter = ReadAsChars(request);//解密数据
//        }catch (Exception e){
//            response.getWriter().write(gson.toJson(new WebResponse(false,"上传失败,请求体解析错误")));
//        }
        String json = request.getParameter("barcode");
//        String barcode = request.getParameter("number");
//        String fid = request.getParameter("fid");
//        String userid = request.getParameter("name");
//        String imie = request.getParameter("storage_name");
//        String qty="0";
        Connection conn = null;
        PreparedStatement sta = null;
        PreparedStatement sta2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String backString = "";
        String condition = "";


//        PostBean pBean = gson.fromJson(parameter, PostBean.class);
//        if (null ==pBean.FLevel ||"".equals(pBean.FLevel)){
//            response.getWriter().write(gson.toJson(new WebResponse(false,"查询条件(等级)为空，查询失败")));
//            return;
//        }
//
//        if ("1".equals(pBean.FLevel)){
//            condition +=" and t1.FMDUserID ='"+pBean.fid+"'";
//        }
//
//        if ("2".equals(pBean.FLevel)){
//            condition +=" and t2.FSupplyID in（select FCustID from t_UserPDASupply where FID ='"+pBean.fid+"')";
//            //门店
//            if (null !=pBean.md_ids &&!"".equals(pBean.md_ids)){
//                condition += " and t1.FMDUserID in(" + pBean.md_ids+")";
//            }
//
//        }
//        if ("3".equals(pBean.FLevel)){
//            //客户
//            if (null !=pBean.client_ids &&!"".equals(pBean.client_ids)){
//                condition += " and t2.FSupplyID in(" + pBean.client_ids+")";
//            }
//            //门店
//            if (null !=pBean.md_ids &&!"".equals(pBean.md_ids)){
//                condition += " and t1.FMDUserID in(" + pBean.md_ids+")";
//            }
//
//        }

        ArrayList<WebResponse.ScanStoreList> container = new ArrayList<>();
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
//                String SQL = "select t2.FModel,t2.FNumber as 物料编码,t2.FName as 物料名称,convert(float,t1.FQty) as 基本单位库存,convert(float,t1.FSecQty) as 辅助单位库存,t6.FName as 辅助单位,t5.FName as 基本单位,t3.FName as 仓库,t4.FName as 仓位,t1.FBatchNo as 批次,t1.FKFDate as 生产日期,t1.FKFPeriod as 保质期 from ICInventory t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_stock t3 on t1.FStockID = t3.FItemID left join t_stockPlace t4 on t1.FStockPlaceID = t4.FSPID left join t_Measureunit t5 on t2.FUnitID = t5.FItemID left join t_Measureunit t6 on t2.FSecUnitID = t6.FItemID where 1=1 "+condition;
                String SQL = "select top 100  t2.FNumber as 物料代码,t2.FName as 物料名称,t6.FName as 所属仓库,isnull(t1.FIsInStore,'未入库') as 入库状态,case when t1.FQty=t1.FQtyOut then '已出库' else '未出库' end as 出库状态,t1.FBillNo as 订单号,t11.FBillNo as 所属箱码 from t_PDABarCodeSign t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join   t_Stock t6 on t1.FStockID=t6.FItemID left join t_StockPlace t7 on t1.FStockPlaceID=t7.FSPID    left join t_PDAAssemble t11 on t1.FInterIDAssemble = t11.FInterID  where 1=1 and t1.FBarCode = '"+json+"'";
                Lg.e("SQL"+SQL);
                sta = conn.prepareStatement(SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    while (rs.next()) {
                        WebResponse.ScanStoreList bean = webResponse.new ScanStoreList();
                        bean.FNumber = rs.getString("物料代码");
                        bean.FName = rs.getString("物料名称");
                        bean.FStorage = rs.getString("所属仓库");
                        bean.FInStoreState = rs.getString("入库状态");
                        bean.FOutStoreState = rs.getString("出库状态");
                        bean.FBillNo = rs.getString("订单号");
                        bean.FBoxCode = rs.getString("所属箱码");
                        container.add(bean);
                    }
                }

                webResponse.state = true;
                webResponse.size = container.size();
                webResponse.backString = "库存查询成功";
                webResponse.scanStoreLists = container;
                Lg.e("库存查询成功：",webResponse);
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
