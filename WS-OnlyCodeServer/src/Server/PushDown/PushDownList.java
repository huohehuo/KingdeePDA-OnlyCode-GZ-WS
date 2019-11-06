package Server.PushDown;

import Bean.PushDownListRequestBean;
import Bean.PushDownListReturnBean;
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

/**
 * Created by NB on 2017/8/23.
 *              获取单据列表信息
 */
@WebServlet("/PushDownList")
public class PushDownList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        ArrayList<PushDownListReturnBean.PushDownListBean> container = new ArrayList<>();
        Gson gson = new Gson();
        String SQL = "";
        String condition = "";
        try {
            String json = request.getParameter("json");
            String version = request.getParameter("version");

            System.out.println("获得json："+json);
            PushDownListRequestBean pushDownListRequestBean = gson.fromJson(json, PushDownListRequestBean.class);
            if (pushDownListRequestBean.FWLUnitID != null && !pushDownListRequestBean.FWLUnitID.equals("")) {
                if (pushDownListRequestBean.id == 1 || pushDownListRequestBean.id == 3) {
                    condition += " and  FSupplyID = " + pushDownListRequestBean.FWLUnitID;
                } else {
                    condition += " and  FSupplyID = " + pushDownListRequestBean.FWLUnitID;
                }

            }
            if (pushDownListRequestBean.code != null && !pushDownListRequestBean.code.equals("")) {
                condition += " and  FBillNo like \'%" + pushDownListRequestBean.code + "%\'";
            }
            if (pushDownListRequestBean.id == 9){
                if (pushDownListRequestBean.StartTime != null && !pushDownListRequestBean.StartTime.equals("") && pushDownListRequestBean.endTime != null && !pushDownListRequestBean.endTime.equals("")) {
                    condition += " and  FPlanCommitDate between " + "\'" + pushDownListRequestBean.StartTime + "\'" + "and" + "\'" + pushDownListRequestBean.endTime + "\'";
                }
            }else{
                if (pushDownListRequestBean.StartTime != null && !pushDownListRequestBean.StartTime.equals("") && pushDownListRequestBean.endTime != null && !pushDownListRequestBean.endTime.equals("")) {
                    if (pushDownListRequestBean.id == 24){
                        condition += " and  FDate between " + "\'" + pushDownListRequestBean.StartTime + "\'" + "and" + "\'" + pushDownListRequestBean.endTime + "\'";
                    }else{
                        condition += " and  t.FDate between " + "\'" + pushDownListRequestBean.StartTime + "\'" + "and" + "\'" + pushDownListRequestBean.endTime + "\'";
                    }
                }
            }

            System.out.println("查询条件:" + condition);
            switch (pushDownListRequestBean.id) {
                case 1:
                    //下载销售订单
                    SQL = "select * from(select distinct(t1.FBillNo),t2.FName,t1.FDate,FCustID as FSupplyID ,FDeptID,FEmpID,FMangerID,t1.FInterID from SEOrder t1 left join t_Organization t2 on t1.FCustID=t2.FItemID left join SEOrderEntry t3 on t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0) t where 1=1 " + condition + " order by t.FBillNo desc";
                    break;
                case 2:
                    //下载采购订单
                    SQL = "select * from(select distinct(t1.FBillNo),t2.FName,t1.FDate,FSupplyID ,FDeptID,FEmpID," +
                            "FMangerID,t1.FInterID from POOrder t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID " +
                            "left join POOrderEntry t3 on t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 " +
                            "and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0) t " +
                            "where 1=1 " + condition + " order by t.FBillNo desc";
                    break;
                case 3:
                    //下载发货通知
//                    SQL = "select * from (select distinct(t1.FBillNo),t2.FName,t1.FDate,FCustID as FSupplyID ,t1.FHeadSelfS0240 as 收件人及电话地址," +
//                            "FDeptID,FEmpID,'' as FMangerID,t1.FInterID from SEOutStock t1 left join " +
//                            "t_Organization t2 on t1.FCustID=t2.FItemID left join SEOutStockEntry t3 on " +
//                            "t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 AND t1.FTranType=83 and (t1.FStatus=1 " +
//                            "or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0) t  where " +
//                            "1=1 " + condition + " order by t.FBillNo desc";
                    SQL = "select * from (select distinct(t1.FBillNo),t2.FName,t1.FDate,FCustID as FSupplyID ,t1.FHeadSelfS0240 as 收件人及电话地址,t4.FName as 是否检验,FDeptID,FEmpID,'' as FMangerID," +
                            "t1.FInterID from SEOutStock t1 left join t_Organization t2 on t1.FCustID=t2.FItemID left join SEOutStockEntry t3 on t1.FInterID=t3.FInterID left join t_SubMessage t4 on (t1.FHeadSelfS0243 = t4.FInterID and t4.FTypeID =10003) WHERE t3.FAuxQty-t3.FAuxCommitQty>0 AND t1.FTranType=83 and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0) t  where " +
                            "1=1 " + condition + " order by t.FBillNo desc";
                    break;
                case 4://收料通知单下外购入库
                    SQL = "select top 100 * from (select distinct  t1.FBillNo,t2.FName,t1.FDate,FSupplyID ," +
                            "FDeptID,FEmpID,'' FMangerID,t1.FInterID from POInstock t1 left join t_Supplier " +
                            "t2 on t1.FSupplyID=t2.FItemID left join POInstockEntry t3 on t1.FInterID=t3.FInterID " +
                            "where ISNULL(t3.FQtyPass,0) + ISNULL(t3.FConPassQty, 0)-ISNULL(t3.FConCommitQty, 0)>0 " +
                            "and (t1.FStatus=1 or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0 and  " +
                            "t1.FTranType=72 and t1.FAreaPS = 20302 AND t1.FBizType = 12510) t where 1=1  " + condition + " order by t.FBillNo desc";
                    break;
                case 5:
                    //下载任务单产品入库
                    SQL = "select * from (select FInterID,FBillNo,FWorkShop,t2.FName as FDepartmentName," +
                            "FPlanCommitDate,row_number() over (order by t1.FBillNo desc) as row_num from " +
                            "ICMO t1 left join t_Department t2 on t1.FWorkShop = t2.FItemID where  " +
                            "FAuxQtyPass-FAuxCommitQty>0 and FStatus in(1,2)) t ";
                    break;
                case 6:
                    //下载任务单生产领料
                    SQL = "select * from (select *,ROW_NUMBER()over(order by FInterID desc) as row_num " +
                            "from (select distinct(t4.FICMOInterID),t1.FInterID,t1.FBillNo,t5.FWorkSHop," +
                            "t2.FName as FDepartmentName,t1.FItemID,t1.FUnitID,FPlanCommitDate  from ICMO " +
                            "t1 left join t_Department t2 on t1.FWorkShop = t2.FItemID  left join PPBOMEntry " +
                            "t4 on t1.FInterID=t4.FICMOInterID left join PPBOM t5 on t4.FInterID=t5.FInterID " +
                            "where t1.FStatus in(1,2) and t4.FAuxQtyMust+t4.FAuxQtySupply-t4.FAuxQty>0 and " +
                            "t5.FType<>1067) t  where 1=1 )t2";
                    break;
                case 7:
                    //出库验货
                    SQL = "select FBillNo,t2.FName,FDate,FSupplyID ,FDeptID,FEmpID,'' as " +
                            "FMangerID,FInterID from ICStockBill t1 left join t_Organization " +
                            "t2 on t1.FSupplyID=t2.FItemID  where (t1.FTranType=21 AND " +
                            "((t1.FCheckerID<=0 OR t1.FCheckerID  IS NULL)  and t1.FCancellation = " +
                            "0 " + condition + "))";
                    break;
                case 8://领料单验货
                    SQL = "select distinct(t1.FBillNo),t2.FName,t1.FDate,FDeptID as FSupplyID ,FDeptID,FEmpID," +
                            "'' as FMangerID,t1.FInterID from ICStockBill t1 left join t_Department t2 on " +
                            "t1.FDeptID=t2.FItemID left join ICStockBillEntry t3 on t1.FInterID=t3.FInterID " +
                            "where (t1.FTranType=24 AND ((t1.FCheckerID<=0 OR t1.FCheckerID  IS NULL)  AND  " +
                            "t1.FCancellation = 0))" + condition + "order by t1.FBillNo desc";
                    break;
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
//                    SQL = "select '' AS FMangerID,'' AS FEmpID,'' AS FSupplyID,FDeptID AS FDeptID, FDate,FInterID,FBillNo,FDeptID FWorkShop,t2.FName,FDate as  FPlanCommitDate,row_number() over (order by t1.FBillNo desc) as row_num from ICMO t1 left join t_Department t2 on t1.FDeptID = t2.FItemID left join t_ICItem t3 on t1.FItemID=t3.FItemID where  FAuxQty-FAuxFinishQty>0 and FStatus in(1,2) " + condition;
                    break;
                case 10://生产汇报单
                    SQL = "select distinct  t1.FBillNo,t2.FName,t1.FDate,t1.FWorkShop as FSupplyID ,t1.FWorkShop " +
                            "as FDeptID,'' FEmpID,'' FMangerID,t1.FInterID from ICMORpt t1 left join ICMORptEntry " +
                            "t3 on t1.FInterID=t3.FInterID  left join t_Department t2 on t1.FWorkShop=t2.FItemID " +
                            "where t3.FAuxQtyPass-t3.FAuxQtySelStock>0 and (t1.FStatus=1 or t1.FStatus=2)  " +
                            "and FCancellation=0" + condition + "order by t1.FBillNo desc";
                    break;
                case 11://委外订单下推委外入库
                    SQL = "select top 50 * from (select distinct  t1.FBillNo,t2.FName,t1.FDate,FSupplyID ,'' FDeptID,'' FEmpID,FMangerID,t1.FInterID from ICSubContract t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID left join ICSubContractEntry t3 on t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 and (t1.FStatus=1 or t1.FStatus=2) and FCancellation=0 and t3.FMrpClosed = 0) t where 1=1  " + condition + " order by t.FBillNo desc";
                    break;
                case 12://委外订单下推委外出库
                    SQL = "select top 50 * from (select distinct  t1.FBillNo,t2.FName,t1.FDate,FSupplyID ,'' FDeptID,'' FEmpID,FMangerID,t1.FInterID from ICSubContract t1 left join t_Supplier t2 on t1.FSupplyID=t2.FItemID left join PPBOMEntry t3 on t1.FInterID=t3.FICMOInterID where t3.FAuxQtyMust+FAuxQtySupply-t3.FAuxQty>0 and (t1.FStatus=1 or t1.FStatus=2) and FCancellation=0  ) t where 1=1  " + condition + " order by t.FBillNo desc";
                    break;
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
                    SQL = "select * from(select distinct(t1.FBillNo),t2.FName,t1.FDate,FCustID as FSupplyID ," +
                            "FDeptID,FEmpID,FMangerID,t1.FInterID from SEOrder t1 left join t_Organization " +
                            "t2 on t1.FCustID=t2.FItemID left join SEOrderEntry t3 on t1.FInterID=t3.FInterID " +
                            "where t3.FAuxQty-t3.FAuxCommitQty>0 and (t1.FStatus=1 or t1.FStatus=2) and " +
                            "t1.FClosed=0 and FCancellation=0) t where 1=1 " + condition + " order by " +
                            "t.FBillNo desc";
                    break;
                case 16://生产任务单下推生生产汇报单
                    SQL = "select '' AS FMangerID,'' AS FEmpID,'' AS FSupplyID,'' AS FDeptID,'' AS FDate,FInterID,FBillNo,FWorkShop," +
                            "t2.FName,FPlanCommitDate," +
                            "row_number() over (order by t1.FBillNo desc) as row_num from ICMO t1 " +
                            "left join t_Department t2 on t1.FWorkShop = t2.FItemID left join t_ICItem " +
                            "t3 on t1.FItemID=t3.FItemID where  FAuxQty-FAuxCommitQty>0 and FStatus in(1,2) " +
                            "" + condition + " and t3.FProChkMde IN (351,353)";
                    break;
                case 18://汇报单下推产品入库
                    if (request.getParameter("version").contains("500")) {
                        SQL = "select top 50 * from (select distinct  t1.FBillNo,t2.FName,t1.FDate,t1.FWorkShop as FSupplyID,t1.FWorkShop " +
                                "as FDeptID,'' FEmpID,'' FMangerID,t1.FInterID from ICMORpt t1 left join ICMORptEntry " +
                                "t3 on t1.FInterID=t3.FInterID  left join t_Department t2 on t1.FWorkShop=t2.FItemID " +
                                "where t3.FAuxQtyPass-t3.FAuxQtySelStock>0 and (t1.FStatus=1 or t1.FStatus=2) AND FCancellation=0) t  where 1=1 "
                                + condition;
                    } else {
                        SQL = "select top 50 * from (select distinct  t1.FBillNo,t2.FName,t1.FDate,t3.FWorkShopID as FSupplyID ,t3.FWorkShopID " +
                                "as FDeptID,'' FEmpID,'' FMangerID,t1.FInterID from ICMORpt t1 left join ICMORptEntry t3 on t1.FInterID=t3.FInterID  " +
                                "left join t_Department t2 on t3.FWorkShopID=t2.FItemID where t3.FAuxQtyPass-t3.FAuxQtySelStock>0 " +
                                "and (t1.FStatus=1 or t1.FStatus=2)  and FCancellation=0 ) t  where 1=1 " + condition ;
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
                    SQL = "select * from (select distinct(t1.FBillNo),t2.FName,t1.FDate,FCustID " +
                            "as FSupplyID ,FDeptID,FEmpID,'' as FMangerID,t1.FInterID from " +
                            "SEOutStock t1 left join t_Organization t2 on t1.FCustID=t2.FItemID " +
                            "left join SEOutStockEntry t3 on t1.FInterID=t3.FInterID where " +
                            "t3.FAuxQty-t3.FAuxCommitQty>0 and (t1.FStatus=1 or t1.FStatus=2) " +
                            "and t1.FClosed=0 and FCancellation=0) t  where 1=1 " + condition + " order " +
                            "by t.FBillNo desc";
                    break;
                case 21://要货单下推调拨单
                    SQL = "select distinct(t1.FBillNo),t2.FName,t1.FBillDate as FDate,FSrcBrID " +
                            "as FSupplyID ,t1.FDstBrID as FDeptID,FEmpID,'' FMangerID,t1.FID " +
                            "as FInterID from t_rt_RequestGoods t1 left join t_SonCompany t2 " +
                            "on t1.FSrcBrID=t2.FItemID left join t_rt_RequestGoodsEntry t3 on " +
                            "t1.FID=t3.FID where t3.FBaseQty-t3.FDistQty>0 and (t1.FCheckStatus>0  ) " +
                            "and t1.FClosed=0 and FCancellation<>1 " + condition + "ORDER BY t1.FBillNo ASC ";
                    break;
                case 22://产品入库验货
                    SQL = "select distinct(t1.FBillNo),t2.FName,t1.FDate,FDeptID as FSupplyID ,FDeptID,FEmpID,'' " +
                            "as FMangerID,t1.FInterID from ICStockBill t1 left join t_Department t2 on t1.FDeptID=t2.FItemID " +
                            "left join ICStockBillEntry t3 on t1.FInterID=t3.FInterID where (t1.FTranType=2 AND " +
                            "((t1.FCheckerID<=0 OR t1.FCheckerID  IS NULL)  AND  t1.FCancellation = 0)) " + condition + "ORDER BY t1.FBillNo ASC ";
                    break;
                case 23:
                    //下载发货通知
                    SQL = "select * from (select distinct(t1.FBillNo),t2.FName,t1.FDate,FCustID as FSupplyID ," +
                            "FDeptID,FEmpID,'' as FMangerID,t1.FInterID from SEOutStock t1 left join " +
                            "t_Organization t2 on t1.FCustID=t2.FItemID left join SEOutStockEntry t3 on " +
                            "t1.FInterID=t3.FInterID where t3.FAuxQty-t3.FAuxCommitQty>0 AND t1.FTranType=82 and (t1.FStatus=1 " +
                            "or t1.FStatus=2) and t1.FClosed=0 and FCancellation=0) t  where " +
                            "1=1 " + condition + " order by t.FBillNo desc";
                    break;
                case 24:
                    //下载发货通知
                    SQL = "select FBillNo,'' FName,FDate,FSupplyID ,FDeptID,FEmpID,'' as FMangerID,FInterID from ICStockBill t1    where (t1.FTranType=41 AND (not exists(select 1 from t_PDABarCodeCheckBillNo where FInterID=t1.FInterID) and t1.FStatus in(0)  and t1.FCancellation = 0 ))"+ condition;
                    break;

            }
            System.out.println("下推查找语句：" + SQL);
            conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
            System.out.println(request.getParameter("sqlip") + " " + request.getParameter("sqlport") + " " + request.getParameter("sqlname") + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
            sta = conn.prepareStatement(SQL);
            rs = sta.executeQuery();
            PushDownListReturnBean pushDownListReturnBean = new PushDownListReturnBean();
            while (rs.next()) {
                PushDownListReturnBean.PushDownListBean pushDownListBean = pushDownListReturnBean.new PushDownListBean();
                pushDownListBean.FDate = rs.getString("FDate");
                pushDownListBean.FBillNo = rs.getString("FBillNo");
                pushDownListBean.FDeptID = rs.getString("FDeptID");
                pushDownListBean.FSupplyID = rs.getString("FSupplyID");
                pushDownListBean.FEmpID = rs.getString("FEmpID");
                pushDownListBean.FManagerID = rs.getString("FMangerID");
                pushDownListBean.FInterID = rs.getString("FInterID");
                pushDownListBean.FName = rs.getString("FName");
                if (pushDownListRequestBean.id == 3){
                    pushDownListBean.FPhoneAddress = rs.getString("收件人及电话地址");
                    pushDownListBean.FIfCheck = rs.getString("是否检验");
                }
                pushDownListBean.tag = pushDownListRequestBean.id;
                container.add(pushDownListBean);
            }
            if (container.size() > 0) {
                Lg.e("表头数据返回：",container);
                pushDownListReturnBean.list = container;
                response.getWriter().write(CommonJson.getCommonJson(true, gson.toJson(pushDownListReturnBean)));
            } else {
                response.getWriter().write(CommonJson.getCommonJson(false, "未找到数据"));
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
