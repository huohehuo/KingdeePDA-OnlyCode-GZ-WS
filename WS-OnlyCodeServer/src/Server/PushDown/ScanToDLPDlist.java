package Server.PushDown;

import Bean.PushDownListRequestBean;
import Bean.ScanPDReturnBean;
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
@WebServlet(urlPatterns = "/ScanToDLPDlist")
public class ScanToDLPDlist extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        String json = request.getParameter("json");
        String version = request.getParameter("version");
        String condition = "";
        String SQL = "";
        if (json != null && !json.equals("")) {
            try {
                PushDownListRequestBean pushDownListRequestBean = new Gson().fromJson(json, PushDownListRequestBean.class);
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip")
                        , request.getParameter("sqlport"), request.getParameter("sqlname")),
                        request.getParameter("sqlpass"), request.getParameter("sqluser"));
                System.out.println(request.getParameter("sqlip") + " "
                        + request.getParameter("sqlport") + " " + request.getParameter("sqlname")
                        + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
                if (pushDownListRequestBean.id == 31){
                    if (pushDownListRequestBean.code != null && !pushDownListRequestBean.code.equals("")) {
                        condition += "and  t1.FBillNo ='" + pushDownListRequestBean.code+"'";
                    }
                }else{
                    if (pushDownListRequestBean.code != null && !pushDownListRequestBean.code.equals("")) {
                        condition += "and  FBillNo ='" + pushDownListRequestBean.code+"'";
                    }
                }

                System.out.println("查询条件:" + condition);
                switch (pushDownListRequestBean.id) {
                    case 1:
                        //下载销售订单
                        SQL = "select * from (select distinct(t1.FBillNo),t2.FName,t1.FDate,FCustID as FSupplyID ,FDeptID,FEmpID,FMangerID,t1.FInterID from SEOrder t1 left join t_Organization t2 on t1.FCustID=t2.FItemID left join SEOrderEntry t3 on t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0) t where 1=1 " + condition + " order by t.FBillNo desc";
                        break;
                    case 2:
                        //下载采购订单
                        SQL = "select * from (select distinct(t1.FBillNo),t2.FName,t1.FDate,FSupplyID ,FDeptID,FEmpID,FMangerID,t1.FInterID from POOrder t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID left join POOrderEntry t3 on t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0) t where 1=1 " + condition + " order by t.FBillNo desc";
                        break;
                    case 3:
                        //下载发货通知
                        SQL = "select * from (select distinct(t1.FBillNo),t2.FName,t1.FDate,FCustID as FSupplyID ,FDeptID,FEmpID,'' as FMangerID,t1.FInterID from SEOutStock t1 left join t_Organization t2 on t1.FCustID=t2.FItemID left join SEOutStockEntry t3 on t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 AND t1.FTranType=83  and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0) t  where 1=1 " + condition + " order by t.FBillNo desc";
                        break;
                    case 4:
                        SQL = "select top 100 * from (select distinct  t1.FBillNo,t2.FName,t1.FDate,FSupplyID ,FDeptID,FEmpID,'' FMangerID,t1.FInterID from POInstock t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID left join POInstockEntry t3 on t1.FInterID=t3.FInterID where ISNULL(t3.FQtyPass,0) + ISNULL(t3.FConPassQty, 0)-ISNULL(t3.FConCommitQty, 0)>0 and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0 and  t1.FTranType=72 and t1.FAreaPS = 20302 AND t1.FBizType = 12510) t where 1=1  " + condition + " order by t.FBillNo desc";
                        break;
                    case 5:
                        //下载任务单产品入库
                        SQL = "select * from (select FInterID,FBillNo,FWorkShop,t2.FName as FDepartmentName,FPlanCommitDate,row_number() over (order by t1.FBillNo desc) as row_num from ICMO t1 left join t_Department t2 on t1.FWorkShop = t2.FItemID where  FAuxQtyPass-FAuxCommitQty>0 and FStatus in(1,2)) t ";
                        break;
                    case 6:
                        //下载任务单生产领料
                        SQL = "select * from (select *,ROW_NUMBER()over(order by FInterID desc) as row_num from (select distinct(t4.FICMOInterID),t1.FInterID,t1.FBillNo,t5.FWorkSHop,t2.FName as FDepartmentName,t1.FItemID,t1.FUnitID,FPlanCommitDate  from ICMO t1 left join t_Department t2 on t1.FWorkShop = t2.FItemID  left join PPBOMEntry t4 on t1.FInterID=t4.FICMOInterID left join PPBOM t5 on t4.FInterID=t5.FInterID where t1.FStatus in(1,2) and t4.FAuxQtyMust+t4.FAuxQtySupply-t4.FAuxQty>0 and t5.FType<>1067) t  where 1=1 )t2";
                        break;
                    case 7:
                        //出库验货
                        SQL = "select FBillNo,t2.FName,FDate,FSupplyID ,FDeptID,FEmpID,'' as " +
                                "FMangerID,FInterID from ICStockBill t1 left join t_Organization " +
                                "t2 on t1.FSupplyID=t2.FItemID  where (t1.FTranType=21 AND " +
                                "((t1.FCheckerID<=0 OR t1.FCheckerID  IS NULL)  AND  t1.FCancellation = 0)) "+condition;
                        break;
                    case 8://领料单验货
                        SQL = "select distinct(t1.FBillNo),t2.FName,t1.FDate,FDeptID as FSupplyID ,FDeptID,FEmpID,'' as FMangerID,t1.FInterID from ICStockBill t1 left join t_Department t2 on t1.FDeptID=t2.FItemID left join ICStockBillEntry t3 on t1.FInterID=t3.FInterID where (t1.FTranType=24 AND ((t1.FCheckerID<=0 OR t1.FCheckerID  IS NULL)  AND  t1.FCancellation = 0))" + condition + "order by t1.FBillNo desc";
                        break;
                    case 25://生产单下推产品入库
                    case 9://生产单下推产品入库
                        if (version.startsWith("3003")){
                            SQL = "select '' AS FMangerID,'' AS FEmpID,'' AS FSupplyID,FDeptID AS FDeptID, FDate,FInterID,FBillNo,FDeptID FWorkShop,t2.FName,FDate as  FPlanCommitDate,row_number() over (order by t1.FBillNo desc) as row_num from ICMO t1 left join t_Department t2 on t1.FDeptID = t2.FItemID left join t_ICItem t3 on t1.FItemID=t3.FItemID where  FAuxQty-FAuxFinishQty>0 and FStatus in(1,2) " + condition;
                        }else{
                            SQL = "select '' AS FMangerID,'' AS FEmpID,'' AS FSupplyID,'' AS FDeptID,'' as FDate," +
                                    "FInterID,FBillNo,FWorkShop,t2.FName,FPlanCommitDate," +
                                    "row_number() over (order by t1.FBillNo desc) as row_num from ICMO t1 " +
                                    "left join t_Department t2 on t1.FWorkShop = t2.FItemID left join t_ICItem " +
                                    "t3 on t1.FItemID=t3.FItemID where  FAuxQty-FAuxCommitQty>0 and FStatus " +
                                    "in(1,2) " + condition + " and t3.FProChkMde IN (352)";
                        }
//                        SQL = "select '' AS FMangerID,'' AS FEmpID,'' AS FSupplyID,FDeptID AS FDeptID, FDate,FInterID,FBillNo,FDeptID FWorkShop,t2.FName,FDate as  FPlanCommitDate,row_number() over (order by t1.FBillNo desc) as row_num from ICMO t1 left join t_Department t2 on t1.FDeptID = t2.FItemID left join t_ICItem t3 on t1.FItemID=t3.FItemID where  FAuxQty-FAuxFinishQty>0 and FStatus in(1,2)" + condition + "order by t1.FBillNo desc";
                        break;
                    case 10://生产汇报单
                        SQL = "select distinct  t1.FBillNo,t2.FName,t1.FDate,t1.FWorkShop as FSupplyID ,t1.FWorkShop as FDeptID,'' FEmpID,'' FMangerID,t1.FInterID from ICMORpt t1 left join ICMORptEntry t3 on t1.FInterID=t3.FInterID  left join t_Department t2 on t1.FWorkShop=t2.FItemID where t3.FAuxQtyPass-t3.FAuxQtySelStock>0 and (t1.FStatus=1 or t1.FStatus=2)  and FCancellation=0" + condition + "order by t1.FBillNo desc";
                        break;
                    case 11://委外订单下推委外入库
                    case 26://委外订单下推委外入库
                        SQL = "select top 50 * from (select distinct  t1.FBillNo,t2.FName,t1.FDate,FSupplyID ,'' FDeptID,'' FEmpID,FMangerID,t1.FInterID from ICSubContract t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID left join ICSubContractEntry t3 on t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 and (t1.FStatus=1 or t1.FStatus=2) and FCancellation=0 and t3.FMrpClosed = 0) t where 1=1  " + condition + " order by t.FBillNo desc";
                        break;
                    case 12://委外订单下推委外出库
                        SQL = "select top 50 * from (select distinct  t1.FBillNo,t2.FName,t1.FDate,FSupplyID ,'' FDeptID,'' FEmpID,FMangerID,t1.FInterID from ICSubContract t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID left join PPBOMEntry t3 on t1.FInterID=t3.FICMOInterID where t3.FAuxQtyMust+FAuxQtySupply-t3.FAuxQty>0 and (t1.FStatus=1 or t1.FStatus=2) and FCancellation=0  ) t where 1=1  " + condition + " order by t.FBillNo desc";
                        break;
                    case 27://生产任务单下推生产领料
                    case 13://生产任务单下推生产领料
                        SQL = "select top 50 * from (select distinct(t4.FICMOInterID),t1.FInterID,t1.FBillNo," +
                                "t5.FWorkSHop,'' as FMangerID,'' as FEmpID,'' as FSupplyID,'' as FDeptID," +
                                "t2.FName as FName,t1.FItemID,t1.FUnitID,FPlanCommitDate as FDate  from ICMO " +
                                "t1 left join t_Department t2 on t1.FWorkShop = t2.FItemID  left join PPBOMEntry " +
                                "t4 on t1.FInterID=t4.FICMOInterID left join PPBOM t5 on t4.FInterID=t5.FInterID " +
                                "where t1.FStatus in(1,2) and t4.FAuxQtyMust+t4.FAuxQtySupply-t4.FAuxQty>0 and " +
                                "t5.FType<>1067) t  where 1=1  " + condition + " order by t.FBillNo desc";
                        break;
                    case 14://采购订单下推收料通知单
                        SQL = "select * from(select distinct(t1.FBillNo),t2.FName,t1.FDate,FSupplyID ,FDeptID," +
                                "FEmpID,FMangerID,t1.FInterID from POOrder t1 left join t_Supplier t2 on " +
                                "t1.FSupplyID=t2.FItemID left join POOrderEntry t3 on t1.FInterID=t3.FInterID " +
                                "where t3.FAuxQty-t3.FAuxCommitQty>0 and (t1.FStatus=1 or t1.FStatus=2) and " +
                                "t1.FClosed=0 and FCancellation=0) t where 1=1 " + condition + " order by " +
                                "t.FBillNo desc";
                        break;
                    case 15://销售订单下推发料通知单
                        SQL = "select * from(select distinct(t1.FBillNo),t2.FName,t1.FDate,FSupplyID ,FDeptID," +
                                "FEmpID,FMangerID,t1.FInterID from POOrder t1 left join t_Supplier t2 on " +
                                "t1.FSupplyID=t2.FItemID left join POOrderEntry t3 on t1.FInterID=t3.FInterID " +
                                "where t3.FAuxQty-t3.FAuxCommitQty>0 and (t1.FStatus=1 or t1.FStatus=2) and " +
                                "t1.FClosed=0 and FCancellation=0) t where 1=1 " + condition + " order by " +
                                "t.FBillNo desc";
                        break;
                    case 16://生产任务单下推生生产汇报单
                        SQL = "select FInterID,FBillNo,FWorkShop,t2.FName as FDepartmentName,FPlanCommitDate,row_number() over (order by t1.FBillNo desc) as row_num from ICMO t1 " +
                                "left join t_Department t2 on t1.FWorkShop = t2.FItemID left join t_ICItem t3 on t1.FItemID=t3.FItemID where  FAuxQty-FAuxCommitQty>0 and FStatus in(1,2) and t1.FInterID>1119 " + condition + " and t3.FProChkMde IN (351,353)";
                        break;
                    case 18://汇报单下推产品入库
                        if(request.getParameter("version").contains("500")){
                            SQL = "select distinct  t1.FBillNo,t2.FName,t1.FDate,t1.FWorkShop as FSupplyID ,t1.FWorkShop " +
                                    "as FDeptID,'' FEmpID,'' FMangerID,t1.FInterID from ICMORpt t1 left join ICMORptEntry " +
                                    "t3 on t1.FInterID=t3.FInterID  left join t_Department t2 on t1.FWorkShop=t2.FItemID " +
                                    "where t3.FAuxQtyPass-t3.FAuxQtySelStock>0 and (t1.FStatus=1 or t1.FStatus=2) "
                                    + condition + " and FCancellation=0 ";
                        }else {
                            SQL = "select distinct  t1.FBillNo,t2.FName,t1.FDate,t3.FWorkShopID as FSupplyID ,t3.FWorkShopID " +
                                    "as FDeptID,'' FEmpID,'' FMangerID,t1.FInterID from ICMORpt t1 left join ICMORptEntry t3 on t1.FInterID=t3.FInterID  " +
                                    "left join t_Department t2 on t3.FWorkShopID=t2.FItemID where t3.FAuxQtyPass-t3.FAuxQtySelStock>0 " +
                                    "and (t1.FStatus=1 or t1.FStatus=2) " + condition + " and FCancellation=0 ";
                        }
                        break;
                    case 19://调拨验货
                        SQL = "select distinct(t1.FBillNo),t2.FName,t1.FDate,FDeptID FSupplyID ," +
                                "FDeptID,FEmpID,'' as FMangerID,t1.FInterID from ICStockBill t1 " +
                                "left join t_Department t2 on t1.FDeptID=t2.FItemID left join " +
                                "ICStockBillEntry t3 on t1.FInterID=t3.FInterID where " +
                                "(t1.FTranType=41 AND ((t1.FCheckerID<=0 OR t1.FCheckerID  IS NULL)  " +
                                "AND  t1.FCancellation = 0 " + condition + "))";
                        break;
                    case 20:
                        //下载发货通知调拨
                        SQL = "select * from (select distinct(t1.FBillNo),t2.FName,t1.FDate,FCustID as FSupplyID ,FDeptID,FEmpID,'' as FMangerID,t1.FInterID from SEOutStock t1 left join t_Organization t2 on t1.FCustID=t2.FItemID left join SEOutStockEntry t3 on t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0) t  where 1=1 " + condition + " order by t.FBillNo desc";
                        break;
                    case 23:
                        //下载发货通知
                        SQL = "select * from (select distinct(t1.FBillNo),t2.FName,t1.FDate,FCustID as FSupplyID ,FDeptID,FEmpID,'' as FMangerID,t1.FInterID from SEOutStock t1 left join t_Organization t2 on t1.FCustID=t2.FItemID left join SEOutStockEntry t3 on t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 AND t1.FTranType=82  and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0) t  where 1=1 " + condition + " order by t.FBillNo desc";
                        break;
                    case 28:
                        //下载发货通知
//                    SQL = "select FBillNo,'' FName,FDate,FSupplyID ,FDeptID,FEmpID,'' as FMangerID,FInterID from ICStockBill t1    where (t1.FTranType=41 AND (not exists(select 1 from t_PDABarCodeCheckBillNo where FInterID=t1.FInterID) and t1.FStatus in(0)  and t1.FCancellation = 0 ))"+ condition;
                        SQL = "select * from(select distinct  t1.FBillNo,t2.FName,t1.FDate,FSupplyID ,FDeptID,FEmpID,'' FMangerID,t1.FInterID from POInstock t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID left join POInstockEntry t3 on t1.FInterID=t3.FInterID  where ISNULL(t3.FAuxQty,0) -ISNULL(t3.FAuxRelateQty, 0)>0 and t3.FCheckMethod<>352  and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0 and  t1.FTranType=72) t WHERE 1=1 "+ condition;
                        break;
                    case 30://采购订单下推收料通知单
                        SQL = "select * from(select distinct  t1.FBillNo,t2.FName,t1.FDate,FSupplyID ,'''' FDeptID,'''' FEmpID,FMangerID,t1.FInterID from ICSubContract t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID left join ICSubContractEntry t3 on t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 and t1.FCancellation=0   and (t1.FInvStyle=14190 OR ISNULL(t1.FInvStyle,0)=0) AND (CASE WHEN EXISTS(SELECT TOP 1 1 FROM t_SystemProfile WHERE FCategory='IC' AND FKey='EnableSupplierCooperation' AND FValue>0) THEN (SELECT TOP 1 1 FROM t_Supplier WHERE (FSupplierCoroutineFlag=0 OR (FSupplierCoroutineFlag=1 AND t3.FSupConfirm = 'Y')) AND FItemID=t1.FSupplyID) ELSE 1 END)=1 AND t1.FStatus >0 AND t3.FMrpClosed<>1 AND t3.FInHighLimitQty>t3.FAuxCommitQty AND t1.FClassTypeID=1007105) t where 1=1 " + condition + " order by " +
                                "t.FBillNo desc";
                        break;
                    case 29:
                        //生产领料单验货
//                    SQL = "select * from(select distinct  t1.FBillNo,t2.FName,t1.FDate,FSupplyID ,FDeptID,FEmpID,'' FMangerID,t1.FInterID from POInstock t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID left join POInstockEntry t3 on t1.FInterID=t3.FInterID  where ISNULL(t3.FAuxQty,0) -ISNULL(t3.FAuxRelateQty, 0)>0 and t3.FCheckMethod<>352  and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0 and  t1.FTranType=72) t WHERE 1=1 "+ condition;
                        SQL = "select distinct(t1.FBillNo),t2.FName,t1.FDate,FDeptID as FSupplyID ,FDeptID,FEmpID,0 as FMangerID,t1.FInterID from ICStockBill t1 left join t_Department t2 on t1.FDeptID=t2.FItemID left join ICStockBillEntry t3 on t1.FInterID=t3.FInterID where (t1.FTranType=24 AND  ((t1.FCheckerID<=0 OR t1.FCheckerID  IS NULL)  AND  t1.FCancellation = 0) and t3.FAuxQty-ISNULL(t3.FEntrySelfB0456,0)>0)  "+ condition +" ORDER BY t1.FBillNo ASC";
                        break;
                    case 31:
                        //收料通知单下推委外入库单
//                    SQL = "select * from(select distinct  t1.FBillNo,t2.FName,t1.FDate,FSupplyID ,FDeptID,FEmpID,'' FMangerID,t1.FInterID from POInstock t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID left join POInstockEntry t3 on t1.FInterID=t3.FInterID  where ISNULL(t3.FAuxQty,0) -ISNULL(t3.FAuxRelateQty, 0)>0 and t3.FCheckMethod<>352  and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0 and  t1.FTranType=72) t WHERE 1=1 "+ condition;
                        SQL = "select distinct  t1.FBillNo,t2.FName,t1.FDate,FSupplyID ,'' FDeptID,'0' FEmpID,'0' FMangerID,t1.FInterID from POInstock t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID left join POInstockEntry t3 on t1.FInterID=t3.FInterID where   ((t1.FWWType=14190 OR ISNULL(t1.FWWType,0)=0)  AND (t1.FTranType = 72  And (t1.FPOMode=36680 OR ISNULL(t1.FPOMode,0)=0) AND t1.FStatus in(1, 2) And t1.FAreaPS = 20302 AND t1.FBizType = 12511 AND ( ((t3.FCheckMethod = 352 OR (t3.FCheckMethod <> 352 AND t3.FDischarged = 1058)) AND t3.FQty -t3.FConCommitQty - (CASE WHEN (SELECT FVALUE FROM t_SystemProfile WHERE FCategory = 'IC' AND FKey = 'MaterialReturnDirect') = '1' THEN t3.FBackQty ELSE 0 END)>0) OR (t3.FCheckMethod <> 352 AND t3.FDischarged = 1059 AND  ( t3.FQtyPass + t3.FConPassQty-t3.FConCommitQty >0 OR t3.FScrapQty+t3.FSampleBreakQty-t3.FScrapInCommitQty >0)))) AND t1.FTranType=72)  "+ condition +" ORDER BY t1.FBillNo ASC";
                        break;
                }
                ArrayList<ScanPDReturnBean.PushDownListBean> container1 = new ArrayList<>();
                ScanPDReturnBean scanPDReturnBean = new ScanPDReturnBean();
                ScanPDReturnBean.PushDownListBean pushDownListBean = scanPDReturnBean.new PushDownListBean();
                System.out.println("下推查找语句：" + SQL);
                sta = conn.prepareStatement(SQL);
                rs = sta.executeQuery();
                if (rs.next()) {
                    pushDownListBean.FDate = rs.getString("FDate");
                    pushDownListBean.FBillNo = rs.getString("FBillNo");
                    pushDownListBean.FDeptID = rs.getString("FDeptID");
                    pushDownListBean.FSupplyID = rs.getString("FSupplyID");
                    pushDownListBean.FEmpID = rs.getString("FEmpID");
                    pushDownListBean.FManagerID = rs.getString("FMangerID");
                    pushDownListBean.FInterID = rs.getString("FInterID");
                    pushDownListBean.FName = rs.getString("FName");
                    pushDownListBean.tag = pushDownListRequestBean.id;
                    container1.add(pushDownListBean);
                    switch (pushDownListRequestBean.id){
                        case 1:
                            SQL = "select t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID," +
                                    "convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                                    "from SEOrderEntry t1 left join SEOrder t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                                    "t3.FItemID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                                    "t1.FInterID="+pushDownListBean.FInterID;
                            break;
                        case 2:
                            SQL = "select t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                                    "from POOrderEntry t1 left join POOrder t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                                    "t3.FItemID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                                    "t1.FInterID="+pushDownListBean.FInterID;
                            break;
                        case 3:
                            SQL = "select isnull(t3.F_115,'配件') as 物料类型,t11.FItemID  as 仓库ID,t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                                    "from SEOutStockEntry t1 left join SEOutStock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                                    "t3.FItemID  left join t_Stock t11 on t11.FItemID = t1.FStockID left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                                    "t1.FInterID="+pushDownListBean.FInterID;
                            break;

                        case 4:
                            SQL =  "select t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,ISNULL(t1.FQtyPass,0) + ISNULL(t1.FConPassQty, 0)-ISNULL(t1.FConCommitQty, 0)) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying from POInstockEntry t1 left join POInstock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and  t2.FTranType=72 and t2.FAreaPS = 20302 AND t2.FBizType = 12510 and ISNULL(t1.FQtyPass,0) + ISNULL(t1.FConPassQty, 0)-ISNULL(t1.FConCommitQty, 0) >0 and t1.FInterID="+pushDownListBean.FInterID;
                            break;
                        case 7:
                            SQL = "select '' AS FStoctName,'' AS FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID," +
                                    "t1.FUnitID,convert(float,FAuxQty) as FAuxQty,convert(float,FAuxPrice) as " +
                                    "FAuxPrice,'0' as FQtying,t1.FDCStockID,t1.FDCSPID,t1.FBatchNo from ICStockBillEntry " +
                                    "t1 left join ICStockBill t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on " +
                                    "t1.FItemID=t3.FItemID where (t2.FTranType=21 AND ((t2.FCheckerID<=0 OR t2.FCheckerID  " +
                                    "IS NULL)  AND  t2.FCancellation = 0)) and t1.FInterID=" + pushDownListBean.FInterID;
                            break;
                        case 8:
                            SQL = "select t4.FName,t5.FName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,convert(varchar(12),t1.FKFDate,23) as FKFDate,t1.FKFPeriod,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying,t1.FSCStockID,t1.FDCSPID,t1.FBatchNo from ICStockBillEntry t1 left join ICStockBill t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t4 on t1.FSCStockID = t4.FItemID left join t_StockPlace t5 on t1.FDCSPID=t5.FSPID where (t2.FTranType=24 AND ((t2.FCheckerID<=0 OR t2.FCheckerID  IS NULL)  AND  t2.FCancellation = 0)) and t1.FInterID="+pushDownListBean.FInterID;
                            break;
                        case 25:
                        case 9:
                            if (version.startsWith("3003")){
                                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,FInterID,FBillNo,t1.FDeptID FWorkShop,t2.FName as FDepartmentName,t1.FItemID,t1.FUnitID,FDate FPlanCommitDate,FPlanFinishDate,(FAuxQty-FAuxFinishQty)   as FAuxQty,(FAuxFinishQty) as FAux,'0' as FQtying,'0' as FAuxPrice,t3.FName, t3.FModel,0 as FEntryID,t3.FNumber from ICMO t1 left join t_Department t2 on t1.FDeptID =t2.FItemID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID =t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where  FAuxQty-FAuxFinishQty>0 and FStatus in(1,2) and t1.FInterID=" + pushDownListBean.FInterID;
                            }else{
                                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,FInterID,FBillNo,FWorkShop,t2.FName as " +
                                        "FDepartmentName,t1.FItemID,t1.FUnitID,FPlanCommitDate,FPlanFinishDate,(FAuxQty-FAuxCommitQty) " +
                                        "as FAuxQty,(FAuxQtyForItem+FAuxQtyScrap) as FAux,'0' as FQtying,'0' as FAuxPrice,t3.FName," +
                                        "t3.FModel,0 as FEntryID,t3.FNumber from ICMO t1 left join t_Department t2 on t1.FWorkShop = " +
                                        "t2.FItemID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = " +
                                        "t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where  FAuxQty-FAuxCommitQty>0 " +
                                        "and FStatus in(1,2) and t1.FInterID=" + pushDownListBean.FInterID;
                            }
//                            SQL = "select t11.FName as FStoctName,t12.FName as FSPName,FInterID,FBillNo,t1.FDeptID FWorkShop,t2.FName as FDepartmentName,t1.FItemID,t1.FUnitID,FDate FPlanCommitDate,FPlanFinishDate,(FAuxQty-FAuxFinishQty)   as FAuxQty,(FAuxFinishQty) as FAux,'0' as FQtying,'0' as FAuxPrice,t3.FName, t3.FModel,0 as FEntryID,t3.FNumber from ICMO t1 left join t_Department t2 on t1.FDeptID =t2.FItemID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID =t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where  FAuxQty-FAuxFinishQty>0 and FStatus in(1,2) and t1.FInterID="+pushDownListBean.FInterID;
                            break;
                        case 10:
                            SQL = "select t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQtyPass-FAuxQtySelStock) as FAuxQty,0 as FAuxPrice,'0' as FQtying from ICMORptEntry t1 left join ICMORpt t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID where   (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQtyPass-t1.FAuxQtySelStock>0 and t1.FInterID="+pushDownListBean.FInterID;
                            break;
                        case 26:
                        case 11:
                            SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID," +
                                    "FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) " +
                                    "as FAuxPrice,'0' as FQtying from ICSubContractEntry t1 left join ICSubContract t2 on " +
                                    "t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 " +
                                    "on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where " +
                                    "t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                                    "t1.FInterID=" + pushDownListBean.FInterID;
                            break;
                        case 12:
                            SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t2.FInterID," +
                                    "t1.FOrderEntryID as FEntryID,t1.FItemID,t1.FUnitID,convert(float,t1.FAuxQtyMust-t1.FAuxQty) as FAuxQty," +
                                    "0 as FAuxPrice,'0' as FQtying from PPBOMEntry t1 left join ICSubContract t2 on t1.FICMoInterID=" +
                                    "t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = " +
                                    "t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where  (t2.FStatus=1 or t2.FStatus=2) " +
                                    "and t1.FAuxQtyMust-t1.FAuxQty>0 and t2.FInterID=" +pushDownListBean.FInterID;
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
                                    "t4.FAuxQtyMust+t4.FAuxQtySupply-t4.FAuxQty>0 and t5.FType<>1067 and t1.FInterID = " + pushDownListBean.FInterID;
                            break;
                        case 14:
                            SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                                    "from POOrderEntry t1 left join POOrder t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                                    "t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                                    "t1.FInterID=" + pushDownListBean.FInterID;
                            break;
                        case 15://销售订单下推发料通知单
                            SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                                    "from POOrderEntry t1 left join POOrder t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                                    "t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                                    "t1.FInterID=" + pushDownListBean.FInterID;
                            break;
                        case 16:
                            SQL =   "select FInterID,FBillNo,FWorkShop,t2.FName as FDepartmentName,t1.FItemID,t1.FUnitID,FPlanCommitDate,FPlanFinishDate,(FAuxQty-FAuxCommitQty) as FAuxQty," +
                                    "(FAuxQtyForItem+FAuxQtyScrap) as FAuxQtyForItem,t3.FName,t3.FModel,0 as FEntryID,t3.FNumber from ICMO t1 left join t_Department t2 on t1.FWorkShop = t2.FItemID " +
                                    "left join t_ICItem t3 on t1.FItemID=t3.FItemID where  FAuxQty-FAuxCommitQty>0 and FStatus in(1,2) and t1.FInterID =" +pushDownListBean.FInterID;
                            break;

                        case 18://汇报单下推产品入库
                            SQL =   "select '' AS FStoctName,'' AS FSPName, t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID," +
                                    "convert(float,FAuxQtyPass-FAuxQtySelStock) as FAuxQty,0 as FAuxPrice,'0' as FQtying from " +
                                    "ICMORptEntry t1 left join ICMORpt t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on " +
                                    "t1.FItemID=t3.FItemID where   (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQtyPass-" +
                                    "t1.FAuxQtySelStock>0  and t1.FInterID="+pushDownListBean.FInterID;
                            break;
                        case 19:
                            SQL = "select '' as FSPName,'' as FStoctName, t3.FName,t3.FNumber,t3.FModel,t2.FBillNo," +
                                    "t1.FInterID,FEntryID,t1.FItemID," +
                                    "t1.FUnitID,convert(float,FAuxQty) as FAuxQty,convert(float,FAuxPrice) as " +
                                    "FAuxPrice,'0' as FQtying,t1.FDCStockID,t1.FDCSPID,t1.FBatchNo,t1.FSCStockID," +
                                    "t1.FSCSPID from ICStockBillEntry t1 left join ICStockBill t2 on t1.FInterID=" +
                                    "t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID where (t2.FTranType" +
                                    "=41 AND ((t2.FCheckerID<=0 OR t2.FCheckerID  IS NULL)  AND  t2.FCancellation = " +
                                    "0)) and t1.FInterID="+pushDownListBean.FInterID;
                            break;
                        case 20:
                            SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                                    "from SEOutStockEntry t1 left join SEOutStock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                                    "t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                                    "t1.FInterID=" + pushDownListBean.FInterID;
                            break;
                        case 23:
                            SQL = "select isnull(t3.F_115,'配件') as 物料类型,t11.FItemID  as 仓库ID,t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying " +
                                    "from SEOutStockEntry t1 left join SEOutStock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=" +
                                    "t3.FItemID  left join t_Stock t11 on t11.FItemID = t1.FStockID left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and " +
                                    "t1.FInterID="+pushDownListBean.FInterID;
                            break;
                        case 28:
                            SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,ISNULL(t1.FAuxQty, 0)-ISNULL(t1.FAuxRelateQty, 0) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying from POInstockEntry t1 left join POInstock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and  t2.FTranType=72 and ISNULL(t1.FAuxQty,0) -ISNULL(t1.FAuxRelateQty, 0) >0 and t1.FInterID=" + pushDownListBean.FInterID;
                            break;
                        case 30:
                            SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,FAuxQty-FAuxCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,0 as FQtying from ICSubContractEntry t1 left join ICSubContract t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and t1.FAuxQty-t1.FAuxCommitQty>0 and t1.FInterID=" + pushDownListBean.FInterID;
                            break;
                        case 29://生产领料单验货
//                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,ISNULL(t1.FAuxQty, 0)-ISNULL(t1.FAuxRelateQty, 0) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying from POInstockEntry t1 left join POInstock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and  t2.FTranType=72 and ISNULL(t1.FAuxQty,0) -ISNULL(t1.FAuxRelateQty, 0) >0 and t1.FInterID=" + dBean.interID;
                            SQL =   "select t11.FName AS FStoctName,t12.FName AS FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID, t1.FUnitID,convert(float,FAuxQty-ISNULL(t1.FEntrySelfB0456,0)) as FAuxQty,convert(float,FAuxPrice) as  FAuxPrice,'0' as FQtying,t1.FSCStockID as FDCStockID,t1.FDCSPID,t1.FBatchNo from ICStockBillEntry   t1 left join ICStockBill t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on  t1.FItemID=t3.FItemID left join t_Stock t11 on t1.FSCStockID=t11.FItemID left join t_StockPlace t12 on t1.FDCSPID = t12.FSPID where (t2.FTranType=24 AND  t2.FCancellation = 0) and t1.FAuxQty-ISNULL(t1.FEntrySelfB0456,0)>0 and t1.FInterID=" + pushDownListBean.FInterID;
                            break;
                        case 31://收料通知单下推委外入库单
//                SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,ISNULL(t1.FAuxQty, 0)-ISNULL(t1.FAuxRelateQty, 0) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying from POInstockEntry t1 left join POInstock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where t2.FClosed=0 and (t2.FStatus=1 or t2.FStatus=2) and  t2.FTranType=72 and ISNULL(t1.FAuxQty,0) -ISNULL(t1.FAuxRelateQty, 0) >0 and t1.FInterID=" + dBean.interID;
                            SQL =   "select t11.FName as FStoctName,t12.FName as FSPName,t3.FName,t3.FNumber,t3.FModel,t2.FBillNo,t1.FInterID,FEntryID,t1.FItemID,t1.FUnitID,convert(float,t1.FAuxQtyPass-t1.FAuxConCommitQty) as FAuxQty,convert(float,FAuxPrice) as FAuxPrice,'0' as FQtying from POInstockEntry t1 left join POInstock t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t1.FItemID=t3.FItemID left join t_Stock t11 on t11.FItemID = t3.FDefaultLoc left join t_StockPlace t12 on t3.FSPID = t12.FSPID where    t1.FAuxQtyPass-t1.FAuxConCommitQty>0 and t1.FInterID=" + pushDownListBean.FInterID;
                            break;
                    }
                    ArrayList<ScanPDReturnBean.DLbean> container = new ArrayList<>();
                    sta = conn.prepareStatement(SQL);
                    rs = sta.executeQuery();
                    while(rs.next()){
                        ScanPDReturnBean.DLbean dLbean = scanPDReturnBean.new DLbean();
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
                        if(pushDownListRequestBean.id==7){
                            dLbean.FDCStockID = rs.getString("FDCStockID");
                            dLbean.FDCSPID = rs.getString("FDCSPID");
                            dLbean.FBatchNo = rs.getString("FBatchNo");
                        }else if (pushDownListRequestBean.id == 8){
                            dLbean.FKFDate = rs.getString("FKFDate");
                            dLbean.FKFPeriod = rs.getString("FKFPeriod");
                            dLbean.FDCStockID = rs.getString("FSCStockID");
                            dLbean.FDCSPID = rs.getString("FDCSPID");
                            dLbean.FBatchNo = rs.getString("FBatchNo");
                        }else if (pushDownListRequestBean.id == 3){
                            dLbean.FDCStockID = rs.getString("仓库ID");
                            dLbean.FProductType = rs.getString("物料类型");
                            dLbean.FDCStockName = rs.getString("FStoctName");
                            dLbean.FDCSPName = rs.getString("FSPName");
                        }else if (pushDownListRequestBean.id == 23){
                            dLbean.FProductType = rs.getString("物料类型");
                            dLbean.FDCStockName = rs.getString("FStoctName");
                            dLbean.FDCSPName = rs.getString("FSPName");
                        }else if (pushDownListRequestBean.id == 30){
                            dLbean.FDCStockName = rs.getString("FStoctName");
                            dLbean.FDCSPName = rs.getString("FSPName");
                        }
                        if (pushDownListRequestBean.id == 29){
                            dLbean.FDCStockName = rs.getString("FStoctName");
                            dLbean.FDCSPName = rs.getString("FSPName");
                            dLbean.FDCStockID = rs.getString("FDCStockID");
                            dLbean.FDCSPID = rs.getString("FDCSPID");
                            dLbean.FBatchNo = rs.getString("FBatchNo");
                        }
                        if (pushDownListRequestBean.id == 31){
                            dLbean.FDCStockName = rs.getString("FStoctName");
                            dLbean.FDCSPName = rs.getString("FSPName");
                        }

                        dLbean.tag = pushDownListRequestBean.id;
                        container.add(dLbean);
                        System.out.println("下载列表长度"+container.size()+"");
                    }
                    scanPDReturnBean.list = container;
                    scanPDReturnBean.list1 = container1;
                    writer.write(CommonJson.getCommonJson(true,new Gson().toJson(scanPDReturnBean)));
                }else{
                    writer.write(CommonJson.getCommonJson(false,"未找到数据"));
                }

            } catch (SQLException e) {
                writer.write(CommonJson.getCommonJson(false,"SQL错误\r\n--------\r\n错误原因:"+e.toString()));
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                writer.write(CommonJson.getCommonJson(false,"服务端错误\r\n--------\r\n错误原因:"+e.toString()));
                e.printStackTrace();
            } finally {
                JDBCUtil.close(rs, sta, conn);
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
