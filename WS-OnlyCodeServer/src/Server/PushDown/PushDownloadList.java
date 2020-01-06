package Server.PushDown;

import Bean.DownLoadSubListBean;
import Bean.PushDownDLBean;
import Bean.PushDownRKBean;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



//获取单据的订单信息
@WebServlet("/PushDownloadList")
public class PushDownloadList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        ArrayList<PushDownDLBean.DLbean> container = new ArrayList<>();
        ArrayList<PushDownRKBean.Rkbean> containerRk = new ArrayList<>();
        String json = request.getParameter("json");
        String version = request.getParameter("version");
        DownLoadSubListBean dBean = new Gson().fromJson(json, DownLoadSubListBean.class);
        String SQL = "";
        System.out.println("获得数据:" + json);
        System.out.println("获取tag:" + dBean.tag);
        switch (dBean.tag) {
            case 1:
                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID," +
                        "convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                        "from SEOrderEntry t1 left join SEOrder t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                        "t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and FMRPClosed=0 and " +
                        "t1.FInterID=" + dBean.interID;
                        break;
            case 2:
                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo," +
                        "t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                        "from POOrderEntry t1 left join POOrder t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                        "t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID " +
                        "where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and FMRPClosed=0 and " +
                        "t1.FInterID=" + dBean.interID;
                        break;

            case 4:
                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo," +
                        "t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,ISNULL(t1.FQtyPass,0) + ISNULL(t1.FConPassQty, 0)" +
                        "-ISNULL(t1.FConCommitQty, 0)) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying from " +
                        "POInstockEntry t1 left join POInstock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on " +
                        "t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join " +
                        "t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) " +
                        "and  t2.FTranType=72 and t2.FAreaPS = 20302 AND t2.FBizType = 12510 and ISNULL(t1.FQtyPass,0) + " +
                        "ISNULL(t1.FConPassQty, 0)-ISNULL(t1.FConCommitQty, 0) >0 and t1.FInterID=" + dBean.interID;
                        break;
            case 7:
                SQL = "select '' AS FStoctName,'' AS FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID," +
                        "t1.FUnitID,convert(float,FAuxQty) as FAuxQty,convert(float,FAuxPrice) as " +
                        "FAuxPrice,'0' as FQtying,t1.FDCStockID,t1.FDCSPID,t1.FBatchNo from ICStockBillEntry " +
                        "t1 left join ICStockBill t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on " +
                        "t1.FItemID=t3.FItemID where (t2.FTranType=21 AND ((t2.FCheckerID<=0 OR t2.FCheckerID  " +
                        "IS NULL)  AND  t2.FCancellation = 0)) and t1.FInterID=" + dBean.interID;
                break;
            case 8:
                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t4.FName,t5.FName,t3.FName,t3.FNumber,t3.FModel," +
                        "t2.FBillNo,convert(varchar(12),t1.FKFDate,23) as FKFDate,t1.FKFPeriod,t1.FInterID,FEntryID,t1.FItemID," +
                        "t1.FUnitID,convert(float,FAuxQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying," +
                        "t1.FSCStockID,t1.FDCSPID,t1.FBatchNo from ICStockBillEntry t1 left join ICStockBill t2 on " +
                        "t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t4 " +
                        "on t1.FSCStockID = t4.FItemID left join t_StockPlace t5 on t1.FDCSPID=t5.FSPID left join " +
                        "t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = " +
                        "t12.FSPID where (t2.FTranType=24 AND ((t2.FCheckerID<=0 OR t2.FCheckerID  IS NULL)  AND  " +
                        "t2.FCancellation = 0)) and t1.FInterID=" + dBean.interID;
                        break;
            case 25:
            case 9:
                if (version.startsWith("3003")){
                    SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,FInterID,FBillNo,t1.FDeptID FWorkShop,t2.FName as FDepartmentName,t1.FItemID,t1.FUnitID,FDate FPlanCommitDate,FPlanFinishDate,(FAuxQty-FAuxFinishQty)   as FAuxQty,(FAuxFinishQty) as FAux,'0' as FQtying,'0' as FAuxPrice,t3.FName, t3.FModel,0 as FEntryID,t3.FNumber from ICMO t1 left join t_Department t2 on t1.FDeptID =t2.FItemID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID =t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where  FAuxQty-FAuxFinishQty>0 and FStatus in(1,2) and t1.FInterID=" + dBean.interID;
                }else{
                    SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,FInterID,FBillNo,FWorkShop,t2.FName as " +
                            "FDepartmentName,t1.FItemID,t1.FUnitID,FPlanCommitDate,FPlanFinishDate,convert(float,(FAuxQty-FAuxCommitQty)) " +
                            "as FAuxQty,(FAuxQtyForItem+FAuxQtyScrap) as FAux,'0' as FQtying,'0' as FAuxPrice,t3.FName," +
                            "t3.FModel,0 as FEntryID,t3.FNumber from ICMO t1 left join t_Department t2 on t1.FWorkShop = " +
                            "t2.FItemID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = " +
                            "t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where  FAuxQty-FAuxCommitQty>0 " +
                            "and FStatus in(1,2) and t1.FInterID=" + dBean.interID;
                }
//                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,FInterID,FBillNo,t1.FDeptID FWorkShop,t2.FName as FDepartmentName,t1.FItemID,t1.FUnitID,FDate FPlanCommitDate,FPlanFinishDate,(FAuxQty-FAuxFinishQty)   as FAuxQty,(FAuxFinishQty) as FAux,'0' as FQtying,'0' as FAuxPrice,t3.FName, t3.FModel,0 as FEntryID,t3.FNumber from ICMO t1 left join t_Department t2 on t1.FDeptID =t2.FItemID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID =t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where  FAuxQty-FAuxFinishQty>0 " +
//                        "and FStatus in(1,2) and t1.FInterID=" + dBean.interID;
                         break;
            case 10:
                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo," +
                        "t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQtyPass-FAuxQtySelStock) as FAuxQty," +
                        "0 as FAuxPrice,'0' as FQtying from ICMORptEntry t1 left join ICMORpt t2 on t1.FInterID=t2.FInterID " +
                        "left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc " +
                        "left join t_StockPlace t12 on t3.FSPID = t12.FSPID where   (t2.FStatus=1 or t2.FStatus=2) and " +
                        "t1.FAuxQtyPass-t1.FAuxQtySelStock>0 and t1.FInterID=" + dBean.interID;
                        break;
            case 26:
            case 11:
                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID," +
                        "FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) " +
                        "as FAuxPrice,'0' as FQtying from ICSubContractEntry t1 left join ICSubContract t2 on " +
                        "t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 " +
                        "on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where " +
                        "t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                        "t1.FInterID=" + dBean.interID;
                        break;
            case 12:
                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t2.FInterID," +
                        "t1.FOrderEntryID as FEntryID,t1.FItemID,t1.FUnitID,convert(float,t1.FAuxQtyMust-t1.FAuxQty) as FAuxQty," +
                        "0 as FAuxPrice,'0' as FQtying from PPBOMEntry t1 left join ICSubContract t2 on t1.FICMoInterID=" +
                        "t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = " +
                        "t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where  (t2.FStatus=1 or t2.FStatus=2) " +
                        "and t1.FAuxQtyMust-t1.FAuxQty>0 and t2.FInterID=" + dBean.interID;
                        break;
            case 27:
            case 13:
                SQL =   "select '' as FAuxPrice, '0' as FQtying,  t11.FName as FStoctName,t12.FName as FSPName,t4.FItemID,(t4.FAuxQtyMust+t4.FAuxQtySupply-t4.FAuxQty) " +
                        "as FAuxQty,t4.FICMOInterID,t1.FInterID,t1.FBillNo,t5.FWorkSHop,t2.FName as FDepartmentName,t4.FItemID," +
                        "t4.FUnitID,FPlanCommitDate,FPlanFinishDate,t3.FName,t3.FModel,t4.FEntryID,t3.FNumber  from ICMO t1 " +
                        "left join t_Department t2 on t1.FWorkShop = t2.FItemID " +
                        "left join PPBOMEntry t4 on t1.FInterID=t4.FICMOInterID  left join t_ICItem t3 on t4.FItemID=t3.FItemID " +
                        "left join PPBOM t5 on t4.FInterID=t5.FInterID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc " +
                        "left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t1.FStatus in(1,2) and " +
                        "t4.FAuxQtyMust+t4.FAuxQtySupply-t4.FAuxQty>0 and t5.FType<>1067 and t1.FInterID = " + dBean.interID;
                        break;
            case 14:
                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                        "from POOrderEntry t1 left join POOrder t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                        "t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                        "t1.FInterID=" + dBean.interID;
                break;
            case 30:
                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,0 as FQtying from ICSubContractEntry t1 left join ICSubContract t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and t1.FInterID=" + dBean.interID;
                break;
            case 15://销售订单下推发料通知单

            SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID," +
                    "convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                    "from SEOrderEntry t1 left join SEOrder t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                    "t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                    "t1.FInterID=" + dBean.interID;
            break;
            case 16:
                SQL =   "select '' AS FStoctName,'' AS FSPName,'0' AS FQtying,'' AS FAuxPrice,FInterID,FBillNo,FWorkShop,t2.FName as FDepartmentName,t1.FItemID,t1.FUnitID,FPlanCommitDate,FPlanFinishDate,(FAuxQty-FAuxCommitQty) as FAuxQty," +
                        "(FAuxQtyForItem+FAuxQtyScrap) as FAuxQtyForItem,t3.FName,t3.FModel,0 as FEntryID,t3.FNumber from ICMO t1 left join t_Department t2 on t1.FWorkShop = t2.FItemID " +
                        "left join t_ICItem t3 on t1.FItemID=t3.FItemID where  FAuxQty-FAuxCommitQty>0 and FStatus in(1,2) and t1.FInterID =" + dBean.interID;
                        break;

            case 18://汇报单下推产品入库
                SQL =   "select '' AS FStoctName,'' AS FSPName, t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID," +
                        "convert(float,FAuxQtyPass-FAuxQtySelStock) as FAuxQty,0 as FAuxPrice,'0' as FQtying from " +
                        "ICMORptEntry t1 left join ICMORpt t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on " +
                        "t1.FItemID=t3.FItemID where   (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQtyPass-" +
                        "t1.FAuxQtySelStock>0  and t1.FInterID="+dBean.interID;
                        break;
            case 19:
                SQL = "select '' as FSPName,'' as FStoctName, t3.FName,t3.FNumber,t3.FModel,t2.FBillNo," +
                        "t1.FInterID,FEntryID,t1.FItemID," +
                        "t1.FUnitID,convert(float,FAuxQty) as FAuxQty,convert(float,FAuxPrice) as " +
                        "FAuxPrice,'0' as FQtying,t1.FDCStockID,t1.FDCSPID,t1.FBatchNo,t1.FSCStockID," +
                        "t1.FSCSPID from ICStockBillEntry t1 left join ICStockBill t2 on t1.FInterID=" +
                        "t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID where (t2.FTranType" +
                        "=41 AND ((t2.FCheckerID<=0 OR t2.FCheckerID  IS NULL)  AND  t2.FCancellation = " +
                        "0)) and t1.FInterID="+dBean.interID;
                break;
            case 20:
                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                        "from SEOutStockEntry t1 left join SEOutStock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                        "t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                        "t1.FInterID=" + dBean.interID;
                break;
            case 21:
                SQL = "select t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FID as FInterID,FEntryID,t1.FItemID," +
                        "t1.FUnitID,convert(float,FBaseQty-FDistQty) as FAuxQty,convert(float,t1.FOrderPrice) " +
                        "as FAuxPrice,'0' as FQtying,t1.FDCStockID,t1.FDCSPID,'' as FSCStockID,'' as FSCSPID " +
                        "from t_rt_RequestGoodsEntry t1 left join t_rt_RequestGoods t2 on t1.FID=t2.FID left join " +
                        "t_ICItem t3 on t1.FItemID=t3.FItemID where t1.FBaseQty-t1.FDistQty>0 and (t2.FCheckStatus>0  ) " +
                        "and t2.FClosed=0 and FCancellation<>1 and t1.FInterID="+dBean.interID;
                break;
            case 22:
                SQL = "select FBillNo,t2.FName,FDate,FDeptID as FSupplyID ,FDeptID,FEmpID,'' as FMangerID,FInterID from " +
                        "ICStockBill t1 left join t_Department t2 on t1.FDeptID=t2.FItemID  where (t1.FTranType=2 AND " +
                        "((t1.FCheckerID<=0 OR t1.FCheckerID  IS NULL)  AND  t1.FCancellation = 0) AND t1.FInterID="+dBean.interID+")";
                break;
            case 23:
                SQL =   "select isnull(t3.F_115,'配件') as 物料类型,t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo," +
                        "t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                        "from SEOutStockEntry t1 left join SEOutStock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                        "t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                        "t1.FInterID=" + dBean.interID;
                break;
            case 24:
                SQL =   "select isnull(t3.F_115,'配件') as 物料类型,'' as FStoctName,''as FSPName, t1.FSCStockID,FSCSPID,t4.FName as 入库仓库,t5.FName as 出库仓库,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,AbS(FAuxQty)) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying,t1.FDCStockID,t1.FDCSPID,t1.FBatchNo from ICStockBillEntry t1 left join ICStockBill t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t4 on t1.FDCStockID=t4.FItemID left join t_Stock t5 on t1.FSCStockID=t5.FItemID where t2.FCancellation = 0 and t2.FTranType=41 and t2.FROB=1  and t1.FInterID=" + dBean.interID;
                break;
            case 3:
                SQL =   "select isnull(t3.F_115,'配件') as 物料类型,t11.FItemID  as 仓库ID,t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo," +
                        "t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                        "from SEOutStockEntry t1 left join SEOutStock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                        "t3.FItemID left join t_Stock t11 on t11.FItemID = t1.FStockID left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                        "t1.FInterID=" + dBean.interID;
                break;
            case 28:
//                SQL =   "select isnull(t3.F_115,'配件') as 物料类型,'' as FStoctName,''as FSPName, t1.FSCStockID,FSCSPID,t4.FName as 入库仓库,t5.FName as 出库仓库,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,AbS(FAuxQty)) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying,t1.FDCStockID,t1.FDCSPID,t1.FBatchNo from ICStockBillEntry t1 left join ICStockBill t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t4 on t1.FDCStockID=t4.FItemID left join t_Stock t5 on t1.FSCStockID=t5.FItemID where t2.FCancellation = 0 and t2.FTranType=41 and t2.FROB=1  and t1.FInterID=" + dBean.interID;
                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,ISNULL(t1.FAuxQty, 0)-ISNULL(t1.FAuxRelateQty, 0) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying from POInstockEntry t1 left join POInstock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and  t2.FTranType=72 and ISNULL(t1.FAuxQty,0) -ISNULL(t1.FAuxRelateQty, 0) >0 and t1.FInterID=" + dBean.interID;
                break;


        }
        try {
            System.out.println("下载ID：" + dBean.interID);
            System.out.println("SQL:" + SQL);
            conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
            System.out.println(request.getParameter("sqlip") + " " + request.getParameter("sqlport") + " " + request.getParameter("sqlname") + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
            sta = conn.prepareStatement(SQL);
            rs = sta.executeQuery();
            PushDownDLBean pushDownDLBean = new PushDownDLBean();
            PushDownRKBean pushDownRKBean = new PushDownRKBean();
            while (rs.next()) {
                if (dBean.tag == 22){
                    PushDownRKBean.Rkbean rkbean = pushDownRKBean.new Rkbean();
                    rkbean.FBillNo = rs.getString("FBillNo");
                    rkbean.FName = rs.getString("FName");
                    rkbean.FDate = rs.getString("FDate");
                    rkbean.FSupplyID = rs.getString("FSupplyID");
                    rkbean.FDeptID = rs.getString("FDeptID");
                    rkbean.FEmpID = rs.getString("FEmpID");
                    rkbean.FInterID = rs.getString("FInterID");
                    rkbean.FMangerID = rs.getString("FMangerID");
                    containerRk.add(rkbean);

                }else{
                    PushDownDLBean.DLbean dLbean = pushDownDLBean.new DLbean();
                    dLbean.FAuxPrice = rs.getString("FAuxPrice");
                    dLbean.FAuxQty = rs.getString("FAuxQty");
                    dLbean.FBillNo = rs.getString("FBillNo");
                    dLbean.FEntryID = rs.getString("FEntryID");
                    dLbean.FInterID = rs.getString("FInterID");
                    dLbean.FItemID = rs.getString("FItemID");
                    dLbean.FModel = rs.getString("FModel");
                    dLbean.FName = rs.getString("FName");
                    dLbean.FNumber = rs.getString("FNumber");
                    dLbean.FQtying = rs.getString("FQtying");
                    dLbean.FUnitID = rs.getString("FUnitID");
                    dLbean.FDCStockName = rs.getString("FStoctName");
                    dLbean.FDCSPName = rs.getString("FSPName");
                    if (dBean.tag == 7) {
                        dLbean.FDCStockID = rs.getString("FDCStockID");
                        dLbean.FDCSPID = rs.getString("FDCSPID");
                        dLbean.FBatchNo = rs.getString("FBatchNo");
                    } else if (dBean.tag == 8) {
                        dLbean.FKFDate = rs.getString("FKFDate");
                        dLbean.FKFPeriod = rs.getString("FKFPeriod");
                        dLbean.FDCStockID = rs.getString("FSCStockID");
                        dLbean.FDCSPID = rs.getString("FDCSPID");
                        dLbean.FBatchNo = rs.getString("FBatchNo");
                    }else if(dBean.tag == 16){
                        dLbean.FAuxPrice = rs.getString("FWorkShop");
                    }else if(dBean.tag == 19){
                        dLbean.FDCStockID = rs.getString("FSCStockID");
                        dLbean.FDCSPID = rs.getString("FDCSPID");
                    }else if (dBean.tag == 3){
                        dLbean.FDCStockID = rs.getString("仓库ID");
                        dLbean.FProductType = rs.getString("物料类型");
                    }else if (dBean.tag == 23){
                        dLbean.FProductType = rs.getString("物料类型");
                    }else if (dBean.tag == 24){
                        dLbean.FProductType = rs.getString("物料类型");
                        dLbean.FSCStockID = rs.getString("FSCStockID");
                        dLbean.FDCStockID = rs.getString("FDCStockID");
                        dLbean.FDCSPID = rs.getString("FDCSPID");
                        dLbean.FSCSPID = rs.getString("FSCSPID");
                        dLbean.FBatchNo = rs.getString("FBatchNo");
                        dLbean.FStorageIn = rs.getString("入库仓库");
                        dLbean.FStorageOut = rs.getString("出库仓库");
                    }
                    dLbean.tag = dBean.tag;

                    container.add(dLbean);
//                    System.out.println("下载列表长度" + container.size() + "");
//                    if (container.size() > 0) {
//                        System.out.println("返回数据："+container.toString());
//                        pushDownDLBean.list = container;
//                        response.getWriter().write(CommonJson.getCommonJson(true, new Gson().toJson(pushDownDLBean.list)));
//                    } else {
//                        response.getWriter().write(CommonJson.getCommonJson(false, "没找到数据"));
//                    }
                }
            }
            if (dBean.tag == 22){
                if (containerRk.size() > 0) {
                    Lg.e("明细返回数据：",containerRk);
                    pushDownRKBean.list = containerRk;
                    response.getWriter().write(CommonJson.getCommonJson(true, new Gson().toJson(pushDownRKBean)));
                } else {
                    response.getWriter().write(CommonJson.getCommonJson(false, "没找到数据"));
                }
            }else{
                if (container.size() > 0) {
                    Lg.e("明细返回数据：",container);
                    pushDownDLBean.list = container;
                    response.getWriter().write(CommonJson.getCommonJson(true, new Gson().toJson(pushDownDLBean)));
                } else {
                    response.getWriter().write(CommonJson.getCommonJson(false, "没找到数据"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write(CommonJson.getCommonJson(false, "数据库错误\r\n----------------\r\n错误原因:\r\n" + e.toString()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().write(CommonJson.getCommonJson(false, "数据库错误\r\n----------------\r\n错误原因:\r\n" + e.toString()));
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
