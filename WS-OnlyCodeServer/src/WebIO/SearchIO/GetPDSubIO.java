package WebIO.SearchIO;

import Utils.JDBCUtil;
import Utils.Lg;
import WebIO.WebResponse;
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
 * Created by NB on 2017/8/18.
 */
@WebServlet("/GetPDSubIO")
public class GetPDSubIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String clear = request.getParameter("is_clear");
        String pdfid = request.getParameter("pdfid");
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        Gson gson = new Gson();
        if (null ==clear ||"".equals(clear)){
            clear="0";
        }
        if (null ==pdfid ||"".equals(pdfid)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"盘点方案ID为空，下载失败")));
            return;
        }
        try {
            ArrayList<WebResponse.PDSub> container = new ArrayList<>();
            conn = JDBCUtil.getConn4Web();
//            String json = request.getParameter("json");
//            System.out.println(json);
            WebResponse webResponse = new WebResponse();
//            PDSubRequestBean pBean = gson.fromJson(json,PDSubRequestBean.class);
//            if(pBean!=null&&pBean.Fid.size()>0){
//                for(int i = 0; i<pBean.Fid.size();i++){
                    if("1".equals(clear)){
                        sta = conn.prepareStatement("update t_PDABarCodeCheckEntry  set FQtying = 0,FCheckUserID = NULL,FCheckDate = NULL where FID= ?");
                        sta.setString(1,pdfid);
                        int j = sta.executeUpdate();
                        System.out.println("清零更新:"+j);
                        sta = conn.prepareStatement("delete from t_PDABarCodeSign_check where FInterID= ?");
                        sta.setString(1,pdfid);
                        int s = sta.executeUpdate();
                        System.out.println("清零:"+s);
                    }
//                    sta = conn.prepareStatement("Select a.* From ( Select t1.FInterID,rtrim(t12.fname) as fstockname,t1.FStockID,t1.FItemID,t11.FName as FSPName,t2.FNumber,t2.FName,t2.FModel,t1.FStockPlaceID, t3.FName AS FUnitName,convert(float,t1.FQty) as FQty,'0' as FQtyAct1,'0' as FCheckQty1,'0' as FAdjQty1,'' as FRemark, t1.FUnitID,t4.FUnitGroupID,LTRIM(RTRIM(t1.FBatchNo)) as FBatchNo,convert(float,t1.FQtyAct) as FQtyAct,convert(float,t1.FCheckQty) as FCheckQty,convert(float,t1.FAdjQty) as FAdjQty From ICInvBackup t1 inner join t_ICItem t2 on t1.FItemID=t2.FItemID left join t_MeasureUnit t3 on t2.FUnitID=t3.FItemID left join t_MeasureUnit t4 on t1.funitid=t4.FItemID left join t_MeasureUnit t5 on t2.FSecUnitID=t5.FItemID left join t_StockPlace t11 on t1.FStockPlaceID=t11.FSPID left join t_Stock t12 on t1.FStockID=t12.fitemid left join t_AuxItem t13 on t1.FAuxPropID=t13.fitemid Where 1=1 And t2.FDeleted<>1 And t1.FInterID = ?) as a where 1=1 and 1=1 order by FStockName Desc,FSPName,FName");
                    sta = conn.prepareStatement("select t1.FID,t1.FItemID as 商品ID,t1.FUnitID as 单位ID,t1.FBarCode as 条码,t1.FStockID as 所属仓库ID,t1.FStockPlaceID as 仓位ID,t4.FName as 所属仓位,t3.FName as 所属仓库,t1.FBatchNo as 批号,convert(float,t1.FQty) as 账存,convert(float,t1.FQtying) as 已盘数,t2.FName as 商品名称,t2.FNumber as 商品代码,t2.FModel as 规格型号  from t_PDABarCodeCheckEntry t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_Stock t3 on t1.FStockID=t3.FItemID left join t_StockPlace t4 on t1.FStockPlaceID=t4.FSPID where t1.FID=?");
                    sta.setString(1,pdfid);
                    rs = sta.executeQuery();
                    while(rs.next()){
                        WebResponse.PDSub pdSub = webResponse.new PDSub();
                        pdSub.FID = rs.getString("FID");
                        pdSub.FItemID = rs.getString("商品ID");
                        pdSub.FUnitID = rs.getString("单位ID");
                        pdSub.FBarCode = rs.getString("条码");
                        pdSub.FStockID = rs.getString("所属仓库ID");
                        pdSub.FStockPlaceID = rs.getString("仓位ID");
                        pdSub.FSPName =rs.getString("所属仓位");
                        pdSub.FStockName =rs.getString("所属仓库");
                        pdSub.FNumber = rs.getString("商品代码");
                        pdSub.FName = rs.getString("商品名称");
                        pdSub.FModel = rs.getString("规格型号");
                        pdSub.FQty = rs.getString("账存");
                        pdSub.FBatchNo = rs.getString("批号");
                        pdSub.FCheckQty = rs.getString("已盘数");



//                        pdSub.FQtyAct1 = rs.getString("FQtyAct1");
//                        pdSub.FCheckQty1 = rs.getString("FCheckQty1");
//                        pdSub.FUnitName = rs.getString("FUnitName");
//                        pdSub.FAdjQty1 = rs.getString("FAdjQty1");
//                        pdSub.FRemark = rs.getString("FRemark");
//                        pdSub.FUnitGroupID = rs.getString("FUnitGroupID");
//                        pdSub.FQtyAct = rs.getString("FQtyAct");
//                        pdSub.FAdjQty = rs.getString("FAdjQty");
                        container.add(pdSub);
                    }

//                }
                if(container.size()>0){
                    webResponse.state = true;
                    webResponse.size = container.size();
                    webResponse.backString = "获取数据成功";
                    webResponse.pdSubs = container;
                    Lg.e("获取数据成功："+container.size(),webResponse);
                    response.getWriter().write(gson.toJson(webResponse));
                }else{
                    response.getWriter().write(gson.toJson(new WebResponse(false,"下载失败，无明细数据")));
                }
//            }else{
//                sta = conn.prepareStatement("Select a.* From ( Select t1.FInterID,rtrim(t12.fname) as fstockname,t1.FStockID,t1.FItemID,t11.FName as FSPName,t2.FNumber,t2.FName,t2.FModel,t1.FStockPlaceID, t3.FName AS FUnitName,convert(float,t1.FQty) as FQty,'0' as FQtyAct1,'0' as FCheckQty1,'0' as FAdjQty1,'' as FRemark, t1.FUnitID,t4.FUnitGroupID,LTRIM(RTRIM(t1.FBatchNo)) as FBatchNo,convert(float,t1.FQtyAct) as FQtyAct,convert(float,t1.FCheckQty) as FCheckQty,convert(float,t1.FAdjQty) as FAdjQty From ICInvBackup t1 inner join t_ICItem t2 on t1.FItemID=t2.FItemID left join t_MeasureUnit t3 on t2.FUnitID=t3.FItemID left join t_MeasureUnit t4 on t1.funitid=t4.FItemID left join t_MeasureUnit t5 on t2.FSecUnitID=t5.FItemID left join t_StockPlace t11 on t1.FStockPlaceID=t11.FSPID left join t_Stock t12 on t1.FStockID=t12.fitemid left join t_AuxItem t13 on t1.FAuxPropID=t13.fitemid Where 1=1 And t2.FDeleted<>1) as a where 1=1 and 1=1 order by FStockName Desc,FSPName,FName");
//                rs = sta.executeQuery();
//                while(rs.next()){
//                    PDSub pdSub = new PDSub();
//                    pdSub.FID = rs.getString("FInterID");
//                    pdSub.FStockID = rs.getString("FStockID");
//                    pdSub.FItemID = rs.getString("FItemID");
//                    pdSub.FSPName =rs.getString("FSPName");
//                    pdSub.FNumber = rs.getString("FNumber");
//                    pdSub.FName = rs.getString("FName");
//                    pdSub.FModel = rs.getString("FModel");
//                    pdSub.FStockPlaceID = rs.getString("FStockPlaceID");
//                    pdSub.FUnitName = rs.getString("FUnitName");
//                    pdSub.FQty = rs.getString("FQty");
//                    pdSub.FQtyAct1 = rs.getString("FQtyAct1");
//                    pdSub.FCheckQty1 = rs.getString("FCheckQty1");
//                    pdSub.FAdjQty1 = rs.getString("FAdjQty1");
//                    pdSub.FRemark = rs.getString("FRemark");
//                    pdSub.FUnitID = rs.getString("FUnitID");
//                    pdSub.FUnitGroupID = rs.getString("FUnitGroupID");
//                    pdSub.FBatchNo = rs.getString("FBatchNo");
//                    pdSub.FQtyAct = rs.getString("FQtyAct");
//                    pdSub.FCheckQty = rs.getString("FCheckQty");
//                    pdSub.FAdjQty = rs.getString("FAdjQty");
//
//                    if(pBean.isClear){
//                        sta = conn.prepareStatement("Update ICInvBackup Set FCheckQty=0,FQtyAct=0,FAuxCheckQty=0,FAuxQtyAct=0   Where FInterID = ?");
//                        sta.setString(1,rs.getString("FInterID"));
//                        int j = sta.executeUpdate();
//                        System.out.println("清零:"+j);
//                    }
//                    container.add(pdSub);
//                }
//                if(container.size()>0){
//                    PDsubReturnBean pDsubReturnBean = new PDsubReturnBean();
//                    pDsubReturnBean.items = container;
//                    response.getWriter().write(CommonJson.getCommonJson(true,gson.toJson(pDsubReturnBean)));
//                }else{
//                    response.getWriter().write(CommonJson.getCommonJson(false,"无数据"));
//                }
//            }
        } catch (SQLException e) {
//            e.printStackTrace();
            response.getWriter().write(gson.toJson(new WebResponse(false,"下载失败，数据库错误"+e.toString())));
//            response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
            response.getWriter().write(gson.toJson(new WebResponse(false,"下载失败，数据库错误"+e.toString())));
//            response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

        }finally {
            JDBCUtil.close(rs,sta,conn);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
