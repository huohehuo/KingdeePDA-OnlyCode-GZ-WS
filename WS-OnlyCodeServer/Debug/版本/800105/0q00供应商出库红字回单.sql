if (exists (select * from sys.objects where name = 'proc_SaleStoreRed'))
    drop proc proc_SaleStoreRed
go
create proc proc_SaleStoreRed
(
 @mainStr nvarchar(1000),--主表参数
 @detailStr1 nvarchar(max),--明细参数
 @detailStr2 nvarchar(max),
 @detailStr3 nvarchar(max),
 @detailStr4 nvarchar(max),
 @detailStr5 nvarchar(max)
)
as 
--------------开启一个模式，也就是不再刷新多少行受影响的信息，可以提高性能
set nocount on
--------------开始存储过程
begin
--------------事务选项设置为出错全部回滚
set xact_abort on
--------------开启事务
begin tran
declare @FUserID int,--供应商用户ID
        @FInterID varchar(20),     --单号id
        @FBillNo varchar(50),      -- 编号   
        @FROB varchar(20),         --红蓝字标识
        @Fdate varchar(50),       --日期  
        @FSupplyID varchar(20),   --购货单位id
        @FCurrencyID nvarchar(20),--币别id 
        @FSaleStyle varchar(20),  --销售方式id 
        @FFetchAdd varchar(100),  --交货地点名
        @FCheckDate varchar(50),  --审核日期 
        @FFManagerID varchar(20), --发货
        @FSManagerID varchar(20), --保管
        @FManagerID varchar(20),  --主管id
        @FDeptID varchar(20),     --部门id
        @FEmpID varchar(20),      --业务员id
        @FBillerID varchar(20),   --制单人id 
        @FSettleDate varchar(50), --结算日期
        @FExplanation varchar(200),--摘要 
        @FMarketingStyle varchar(20),--销售业务类型
        @FOrderID varchar(50),--PDA单据编号
        @FPDAID varchar(50),  --PDA序列号 
         @FUserName1 varchar(128),--门店用户
        @FPDASource varchar(128),--单据来源 
        @FSelTranType varchar(20)  --源单类型
--set @FBillerID = dbo.getString(@mainStr,'|',1) --操作员  
--set @Fdate =dbo.getString(@mainStr,'|',2)      --日期
--set @FSettleDate=dbo.getString(@mainStr,'|',3) --结算日期
--set @FSaleStyle =dbo.getString(@mainStr,'|',4) --销售方式  
--set @FFetchAdd =dbo.getString(@mainStr,'|',5)  --交货地点
--set @FDeptID =dbo.getString(@mainStr,'|',6)   --部门id
--set @FEmpID =dbo.getString(@mainStr,'|',7)    --业务员id
--set @FManagerID =dbo.getString(@mainStr,'|',8) --主管id
--set @FSupplyID =dbo.getString(@mainStr,'|',9)   --购货单位
--set @FExplanation =dbo.getString(@mainStr,'|',10)--摘要
--set @FFManagerID=dbo.getString(@mainStr,'|',11) --发货
--set @FSManagerID=dbo.getString(@mainStr,'|',12) --保管
--set @FROB=dbo.getString(@mainStr,'|',13)         --蓝字红字
--set @FMarketingStyle=dbo.getString(@mainStr,'|',14)--销售业务类型
--set @FSelTranType=dbo.getString(@mainStr,'|',15)  --源单类型
--set @Fdate = convert(varchar(20),GETDATE(),23)
--if exists(select 1 from t_PDABarCodeType where FType=1)
--begin
-- set @FOrderID=dbo.getString(@mainStr,'|',16) --PDA单据编号   
--set @FPDAID=dbo.getString(@mainStr,'|',17) --PDA序列号 
--end
set @FUserID = dbo.getString(@mainStr,'|',1) --操作员(供应商用户ID)
set @FOrderID=dbo.getString(@mainStr,'|',2) --PDA单据编号   
set @FPDAID=dbo.getString(@mainStr,'|',3) --PDA序列号 
set @FROB='红字'
set @Fdate = convert(varchar(20),GETDATE(),23)
if not exists(select 1 from t_UserPDASupply where FID=@FUserID)
begin
print convert(int,'用户检测数据失败,请你查看PDA登陆用户是否有效用户')
end
else
begin
set @FSettleDate = @Fdate
 select @FUserName1=FName,@FBillerID=FUserID,@FSupplyID = FCustID,@FSaleStyle=FSaleStyle,@FDeptID=FDeptID,@FEmpID=FEmpID,@FManagerID= FMangerID,@FFManagerID=FFManagerID,@FSManagerID=FSManagerID,@FExplanation=FExplanation from t_UserPDASupply where FID=@FUserID
end
set @FPDASource = 'APP制单'
exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --得到@FInterID
if(@FROB='红字')
set @FROB=-1
else
set @FROB=1
------------------------------------------------------------得到编号
set @FBillNo = ''
 exec proc_GetICBillNo 21, @FBillNo out 
-----------------------------------------------------------得到编号
set @FCurrencyID=1 --币别
set @FCheckDate=null --审核时间  
  declare @IsExist varchar(10), --是否存在
            @value varchar(10)--库存更新类型
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FROB,FHookStatus,Fdate,FSupplyID,FSaleStyle,FCheckDate,
FConfirmDate,FFManagerID,FSManagerID,FBillerID,FConfirmer,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FPOOrdBillNo,
FMultiCheckDate6,FRelateBrID,FOrgBillInterID,FMarketingStyle,FSelTranType,FPrintCount,FBrID,FFetchAdd,FExplanation,FConfirmMem,FDeptID,FEmpID,FManagerID,
FVIPCardID,FReceiver,FVIPScore,FHolisticDiscountRate,FPOSName,FWorkShiftId,FLSSrcInterID,FManageType,FPayCondition,FSettleDate,FInvoiceStatus,FConsignee,
FHeadSelfB0156,FHeadSelfB0157,FHeadSelfB0159,FHeadSelfB0160,FHeadSelfB0161,FHeadSelfB0162,FHeadSelfB0163,FHeadSelfB0164,FHeadSelfB0165,
FHeadSelfB0166,FHeadSelfB0167,FHeadSelfB0168,FHeadSelfB0169) --
SELECT @FInterID,@FBillNo,'0',21,0,0,@value,-1,0,@Fdate,@FSupplyID,@FSaleStyle,Null,Null,@FFManagerID,@FSManagerID,@FBillerID,0,Null,Null,Null,Null,Null,'',
Null,0,0,12530,81,0,0,'','','',@FDeptID,@FEmpID,@FManagerID,0,'',0,0,'',0,0,0,0,@FSettleDate,'',0,CONVERT(varchar(128),GETDATE(),20),'国内线上经销商','13510235785','','','',990139,'',Null,'',Null,@FPDASource,@FUserName1
update ICStockBill set FUUID=newid() where FInterID=@FInterID

declare @FEntryID varchar(20),       --明细序号
        @FItemID varchar(20),        --商品id
        @FQty float,                --基本单位数量
        @FUnitID varchar(20),       --单位id
        @Fauxqty float,            --上传的数量 
        @Fauxprice float,      -- 单位成本单价
        @Famount float,        --成本金额
        @FPrice float,
        @FTaxAmount float,     --
        @FConsignPrice float,   --销售单价
        @FConsignAmount float, --销售金额(销售单价*数量-折扣额)
        @FPlanPrice float,     --基本单位计划单价
        @FAuxPlanPrice float, --单位计划单价
        @FPlanAmount float,   --计划价金额     
        @FDiscountRate float,  --折扣率
        @FDiscountAmount float,--折扣额(含税单价*数量*折扣率)  
        @FBatchNo varchar(50),   --批号 
        @FDCStockID varchar(20), --仓库id
        @FDCSPID varchar(20),    --仓位
        @FCoefficient varchar(20),   --换算率
            @FSecCoefficient float, --辅助单位换算率
        @FSecQty decimal(28,10),   --辅助单位数量
          @FSecUnitID  varchar(50),  
             @FKFDate varchar(50),    --生产日期
        @FOLOrderBillNo varchar(128),--网上订单号
          @FBarCode varchar(128),
          @FEntrySelfB0176 varchar(255),
        @FKFPeriod int,       --保质期
        @FPeriodDate varchar(50),--有效期
        @detailqty int,               --明细参数的个数
        @detailcount int,             --明细每行数据的长度 
        @detailIndex int,            --明细每行下标
        @countindex int              --分隔符|的数量
       set @detailqty=0        
       set @detailcount=10           
    while(@detailqty<5)--判断是明细的哪个参数
    begin
    if(@detailqty=1)
	begin
	set @detailStr1=@detailStr2
	end  
	if(@detailqty=2)
	begin
	set @detailStr1=@detailStr3
	end 
	if(@detailqty=3)
	begin
	set @detailStr1=@detailStr4
	end 
	if(@detailqty=4)
	begin
	set @detailStr1=@detailStr5
	end 
	if(@detailStr1='' or @detailStr1=null)
	begin
	break;
	end
	set @detailIndex=0 
	select @countindex=len(@detailStr1)-len(replace(@detailStr1, '|', ''))
	while(@countindex>@detailIndex*@detailcount)
	begin
	set @FItemID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+1)    --商品id
	set @FUnitID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --单位id
	set @FConsignPrice=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --销售单价
	set @Fauxqty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)       --数量
	--set @FDiscountRate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5)   --折扣率
	set @FDCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5)     --仓库id
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6)       --批号
	set @FDCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7)        --仓位ID
	set @FKFDate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8)  
	set @FKFPeriod=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)
	set @FOLOrderBillNo =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+10)--网上订单号
	
	set @Fauxqty = -@Fauxqty
	
	set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex
    	set @FDiscountRate = 0 
                 if(@FKFDate is null or @FKFDate='')
		set @FPeriodDate=null
		else
		begin
			select @FPeriodDate = DATEADD(day,@FKFPeriod,@FKFDate) 
		end  
	set @FPrice=0
	set @FAuxPrice=@FPrice
    set @FAmount=@FPrice*@Fauxqty 
	select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --单位换算率
	select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --基本单位数量 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --单位计划单价
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --计划单价金额
	set @FDiscountAmount=@Fauxqty*@FConsignPrice*(@FDiscountRate/100) --折扣额
	set @FConsignAmount=@Fauxqty*@FConsignPrice-@FDiscountAmount --金额
	 
    	 select @FEntrySelfB0176=t_2043.FName from t_ICItem t4 left join t_Item_2043  t_2043  on t4.F_109=t_2043.FItemID where t4.FItemID=@FItemID
       --物料辅助单位
       select @FSecUnitID=FSecUnitID,@FSecCoefficient=FSecCoefficient from t_ICItem where FItemID=@FItemID
      if(@FSecCoefficient<>0) --这里判断上传的是辅助单位还是基本单位 如果成立说明上传的是辅助单位
      begin
      set @FSecQty = @FQty/@FSecCoefficient
      end
      else
      begin
      set @FSecQty = 0
      end
  --    if(@value=1)
		--begin
		  select @IsExist=COUNT(1) from ICInventory where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  if(@IsExist=0)
			begin
			INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
			SELECT '0',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,@FKFPeriod,@FKFDate,-@FQty,-@FSecQty
			end
		  else
			begin
			update ICInventory set FQty=FQty-@FQty,FSecQty=FSecQty-@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  end    
		--end
		if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
         select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID
           if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FOLOrderBillNo=@FOLOrderBillNo)
		       begin
		         update ICStockBillEntry set  FSecQty=isnull(FSecQty,0)+@FSecQty,FConsignAmount=isnull(FConsignAmount,0)+@FConsignAmount,FDiscountAmount=isnull(FDiscountAmount,0)+@FDiscountAmount,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FOLOrderBillNo=@FOLOrderBillNo
		       end
		        else
		        begin 
INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FOLOrderBillNo,FAuxPropID,FBatchNo,FQty,FUnitID,FAuxQtyMust,Fauxqty,FSecCoefficient,FSecQty,
FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FIsVMI,FEntrySupply,FDCStockID,FDCSPID,FConsignPrice,FDiscountRate,
FConsignAmount,FDiscountAmount,FOrgBillEntryID,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FContractBillNo,FContractInterID,
FContractEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FAllHookQTY,FCurrentHookQTY,FQtyMust,FSepcialSaleId,FPlanMode,FMTONo,FClientOrderNo,FConfirmMemEntry,
FClientEntryID,FChkPassItem,FSEOutBillNo,FSEOutEntryID,FSEOutInterID,FReturnNoticeBillNo,FReturnNoticeEntryID,FReturnNoticeInterID,FProductFileQty,FEntrySelfB0176)  
SELECT @FInterID,@FEntryID,'0','','',@FItemID,@FOLOrderBillNo,0,@FBatchNo,@FQty,@FUnitID,0,@Fauxqty,@FSecCoefficient,@FSecQty,0,0,0,0,'',@FKFDate,@FKFPeriod,@FPeriodDate,0,0,@FDCStockID,@FDCSPID,@FConsignPrice,0,@FConsignAmount,0,0,0,'',0,0,0,'',0,0,'',0,0,0,0,0,0,14036,'','','','',1058,'',0,0,'',0,0,0,@FEntrySelfB0176 
         end
end
set @detailqty=@detailqty+1
end
EXEC p_UpdateBillRelateData 21,@FInterID,'ICStockBill','ICStockBillEntry' 

declare @FHeadSelfB0163 varchar(1024),
        @FHeadSelfB0155 varchar(255),-- 币别,
        @FHeadSelfB0157 varchar(255), -- 客户分类
        @FHeadSelfB0159 varchar(255), --收货电话地址
        @FHeadSelfB0160	 varchar(255), --业务说明
        @FHeadSelfB0161  varchar(255) --单据说明
 --        if exists(select 1 from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0 )
 --begin
 --select @FSManagerID=t2.FEmpID from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0
 --end


select @FHeadSelfB0157 = t2.FName from t_Organization t1 left join t_SubMessage t2 on t1.FTypeID = t2.FInterID where FItemID=@FSupplyID
update ICStockBill set FHeadSelfB0157=@FHeadSelfB0157, FHeadSelfB0160=@FHeadSelfB0160,FHeadSelfB0161=@FHeadSelfB0161,FExplanation=@FExplanation,FSaleStyle=@FSaleStyle,FHeadSelfB0159=@FHeadSelfB0159,FSManagerID=@FSManagerID,FEmpID=@FEmpID,FDeptID=@FDeptID where FInterID=@FInterID
if exists(select 1 from t_PDABarCodeType where FType=1)
begin
--单个
insert into t_PDABarCodeSign_Out(FInterID,FBarCode,FQty,FBillNo,FRemark,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before,FStatus,FDateUpLoad,FTypeID,FPrice,FStockID,FStockPlaceID)select @FInterID,a.FBarCode,a.FQty,@FBillNo,'供应商销售出库红字',b.FInterIDOut,b.FDateOutStore,b.FUserOutStore,1,convert(varchar(20),GETDATE(),20),21,a.FPrice,b.FStockID,b.FStockPlaceID from a_DetailsTable  a inner join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID
update t_PDABarCodeSign  set FInterIDOut=@FInterID,FDateOutStore=@fdate,FIsOutStore='未出库',FUserOutStore=@FBillerID,FQtyOut =FQtyOut + t.FQty from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
update t_PDABarCodeSign  set FInterIDDisassemble=FInterIDAssemble, FInterIDAssemble = 0,FIsOutStore='未出库'   from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID and a.FInterID=1) as t where t_PDABarCodeSign.FBarCode=t.FBarCode and t.FInterID=1
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
end
--审核
declare @FUserName varchar(128)
select @FUserName = FName from t_User where FUserID=@FBillerID
UPDATE ICStockBill SET FStatus=1,FMultiCheckStatus=16,FHeadSelfB0165 = GetDate(),FHeadSelfB0164 = @FUserName,FCheckDate = GetDate(),FCheckerID=@FBillerID
 WHERE FInterID = @FInterID
 AND FTranType=21 
 UPDATE ICClassMCTaskCenter SET FStatus=1 ,FProcessUserID=@FBillerID,FUpdateDate=getdate()
 WHERE FClassTypeID=1021 AND FBillID=@FInterID AND FStatus=0 AND (FTaskType=0 OR FTaskType=2) AND FNextLevelTag =2001
 IF EXISTS(SELECT FOrderInterID FROM ICStockBillEntry WHERE FOrderInterID>0 AND FInterID=@FInterID)
BEGIN 
  UPDATE u1
  SET u1.FStockQty=u1.FStockQty+1*Cast(u2.FStockQty as Float)
       ,u1.FSecStockQty=u1.FSecStockQty+1*Cast(u2.FSecStockQty as Float)
       ,u1.FAuxStockQty=ROUND((u1.FStockQty+1*Cast(u2.FStockQty as Float))/Cast(t3.FCoefficient as Float),t1.FQtyDecimal)
  FROM SEOrderEntry u1 
  INNER JOIN 
  (SELECT FOrderInterID,FOrderEntryID,FItemID,SUM(FQty)AS FStockQty,SUM(FAuxQty) AS FAuxStockQty,SUM(FSecQty) AS FSecStockQty
   FROM ICStockBillEntry WHERE FInterID=@FInterID
   GROUP BY FOrderInterID,FOrderEntryID,FItemID) u2
  ON u1.FInterID=u2.FOrderInterID AND u1.FEntryID=u2.FOrderEntryID AND u1.FItemID=u2.FItemID
   INNER JOIN t_ICItemBase t1 ON u1.FItemID=t1.FItemID
   INNER JOIN t_MeasureUnit t3 ON u1.FUnitID=t3.FItemID
 
  IF OBJECT_ID('tempdb..#tmpPMCIndex','U') IS NOT NULL 
    DROP TABLE #tmpPMCIndex
  SELECT u0.FIndex
    INTO #tmpPMCIndex
  FROM ICPlan_PMCdetail u0
  INNER JOIN SEOrderEntry u1 ON u0.FRelTranType=81 AND u0.FRelInterID=u1.FInterID AND u0.FRelEntryID=u1.FEntryID AND  u0.FBillType In(22,25)
  INNER JOIN 
  ( SELECT DISTINCT FOrderInterID,FOrderEntryID,FItemID
    FROM ICStockBillEntry WHERE  FOrderInterID>0 AND FInterID=@FInterID
   ) u2 ON u1.FInterID=u2.FOrderInterID AND u1.FEntryID=u2.FOrderEntryID AND u1.FItemID=u2.FItemID 
  CREATE CLUSTERED INDEX PK_#tmpPMCIndex ON #tmpPMCIndex(FIndex) 
 
  UPDATE u0
    SET u0.FWillOutQty=CASE WHEN u1.FQty>u1.FStockQty THEN u1.FQty-u1.FStockQty ELSE 0 END 
  FROM ICPlan_PMCdetail u0
  INNER JOIN SEOrderEntry u1 ON u0.FRelTranType=81 AND u0.FRelInterID=u1.FInterID AND u0.FRelEntryID=u1.FEntryID AND  u0.FBillType In(22,25)
  WHERE exists(select 1 from  #tmpPMCIndex where FIndex=u0.FIndex)
  DROP TABLE #tmpPMCIndex
 
END 
IF EXISTS(SELECT FOrderInterID FROM ICStockBillEntry WHERE FSEOutInterID>0 AND FInterID=@FInterID)
 UPDATE u1
 SET u1.FStockQty=u1.FStockQty+1*Cast(u2.FStockQty as Float)
     ,u1.FSecStockQty=u1.FSecStockQty+1*Cast(u2.FSecStockQty as Float)
     ,u1.FAuxStockQty=ROUND((u1.FStockQty+1*Cast(u2.FStockQty as Float))/Cast(t3.FCoefficient as Float),t1.FQtyDecimal)
 FROM SEOutStockEntry u1 
 INNER JOIN 
 (SELECT FSEOutInterID,FSEOutEntryID,FItemID,SUM(FQty)AS FStockQty,SUM(FAuxQty) AS FAuxStockQty,SUM(FSecQty) AS FSecStockQty
  FROM ICStockBillEntry WHERE FInterID=@FInterID
  GROUP BY FSEOutInterID,FSEOutEntryID,FItemID) u2
 ON u1.FInterID=u2.FSEOutInterID AND u1.FEntryID=u2.FSEOutEntryID AND u1.FItemID=u2.FItemID
 INNER JOIN t_ICItemBase t1 ON u1.FItemID=t1.FItemID INNER JOIN t_MeasureUnit t3 ON u1.FUnitID=t3.FItemID
--审核

if not exists(  select   1  from ICStockBillEntry where FInterID=@FInterID)
begin
    delete  ICStockBill where FInterID=@FInterID
	goto error1
end


commit tran 
return;
--------------存在错误
if(0<>@@error)
	goto error1
--------------回滚事务	
error1:
	rollback tran;
--------------结束存储过程
end
