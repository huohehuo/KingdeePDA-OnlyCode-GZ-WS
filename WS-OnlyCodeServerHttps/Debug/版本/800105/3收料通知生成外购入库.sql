if (exists (select * from sys.objects where name = 'proc_PushReceiving'))
    drop proc proc_PushReceiving
go
create proc proc_PushReceiving
(
 @mainStr nvarchar(1000),--主表参数
 @detailStr1 varchar(max),--明细参数
 @detailStr2 varchar(max),
 @detailStr3 varchar(max),
 @detailStr4 varchar(max),
 @detailStr5 varchar(max)
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
declare @FInterID varchar(20),     --单号id
        @FBillNo varchar(50),      -- 编号   
        @FROB varchar(20),         --红蓝字标识
        @Fdate varchar(50),       --日期  
        @FSupplyID varchar(20),   --购货单位id
        @FCurrencyID nvarchar(20),--币别id 
        @FSaleStyle varchar(20),  --销售方式id 
        @FPOStyle varchar(20),    --采购方式
        @FFetchAdd varchar(100),  --交货地点名
        @FCheckDate varchar(50),  --审核日期 
        @FFManagerID varchar(20), --发货
        @FSManagerID varchar(20), --保管
        @FManagerID varchar(20),  --主管id
        @FDeptID varchar(20),     --部门id
        @FEmpID varchar(20),      --业务员id
        @FBillerID varchar(20),   --制单人id 
        @FSettleDate varchar(50), --付款日期
        @FHeadSelfA0145 varchar(128),--是否良品
        @FExplanation varchar(200),--摘要 
        @FMarketingStyle varchar(20),--销售业务类型
        @FSelTranType varchar(20)  --源单类型
set @FBillerID = dbo.getString(@mainStr,'|',1) --操作员  
set @Fdate =dbo.getString(@mainStr,'|',2)      --日期 
set @FSettleDate=dbo.getString(@mainStr,'|',3) --付款日期
set @FSupplyID =dbo.getString(@mainStr,'|',4)   --购货单位id 
set @FPOStyle =dbo.getString(@mainStr,'|',5) --采购方式   
set @FDeptID =dbo.getString(@mainStr,'|',6)   --部门id
set @FEmpID =dbo.getString(@mainStr,'|',7)    --业务员id
set @FManagerID =dbo.getString(@mainStr,'|',8) --主管id
set @FFManagerID=dbo.getString(@mainStr,'|',9) --发货
set @FSManagerID=dbo.getString(@mainStr,'|',10) --保管 
set @FHeadSelfA0145 =dbo.getString(@mainStr,'|',11) --是否良品 
set @Fdate = convert(varchar(20),GETDATE(),23)  
exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --得到@FInterID 
------------------------------------------------------------得到编号
if(@FROB='红字')
set @FROB=-1
else
set @FROB=1
 set @FBillNo ='' 
 exec proc_GetICBillNo 1, @FBillNo out 
-----------------------------------------------------------得到编号
set @FCurrencyID=1 --币别
set @FCheckDate=null --审核时间  
set @FExplanation='' --备注
  declare @IsExist varchar(10), --是否存在
            @value varchar(10)--库存更新类型
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FVchInterID,FROB,FHookStatus,Fdate,
FSupplyID,FCheckDate,FFManagerID,FSManagerID,FBillerID,FPOStyle,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,
FMultiCheckDate5,FMultiCheckDate6,FRelateBrID,FPOOrdBillNo,FOrgBillInterID,FSelTranType,FBrID,FExplanation,FDeptID,FManagerID,FEmpID,
FCussentAcctID,FManageType,FSettleDate,FPrintCount,FHeadSelfA0145) 
VALUES (@FInterID,@FBillNo,'0',1,0,0,@value,0,1,0,@Fdate,@FSupplyID,@FCheckDate,@FFManagerID,@FSManagerID,@FBillerID,@FPOStyle,Null,
Null,Null,Null,Null,Null,0,'',0,72,0,@FExplanation,@FDeptID,@FManagerID,@FEmpID,0,0,@FSettleDate,0,@FHeadSelfA0145)
update ICStockBill set FUUID=newid() where FInterID=@FInterID

declare @FEntryID varchar(20),       --新的明细序号
        @FSourceEntryID varchar(20), --下推单据的明细id
        @FSourceInterId varchar(20), --下推单据的FInterID
        @FSourceBillNo varchar(20),  --下推的单据的单据编号
        @FOrderBillNo varchar(50),
        @FOrderInterID varchar(50),
        @FOrderEntryID varchar(50),
        
        @FItemID varchar(20),        --商品id
        @FQty float,                --基本单位数量
        @FQtyMust float,            --基本单位可验数量
        @FAuxQtyMust float,        --单位可验收数量
        @FUnitID varchar(20),       --单位id
        @Fauxqty float,            --上传的数量 
        @Fauxprice float,          -- 单价
        @Famount float,          --金额
        @FTaxAmount float,     -- 
        
        @FPlanPrice float,     --基本单位计划单价
        @FAuxPlanPrice float, --单位计划单价
        @FPlanAmount float,   --计划价金额     
        @FDiscountRate float,  --折扣率
        @FDiscountAmount float,--折扣额(含税单价*数量*折扣率)   
        @FDCStockID varchar(20), --仓库id
        @FDCSPID varchar(20), --仓位id
        @FBatchNo varchar(50),--批次
        @FCoefficient varchar(20),   --换算率
          @FSecCoefficient float, --辅助单位换算率
        @FSecQty decimal(28,10),   --辅助单位数量
          @FSecUnitID  varchar(50), 
        @detailqty int,               --明细参数的个数
        @detailcount int,             --明细每行数据的长度 
        @detailIndex int,            --明细每行下标
        @countindex int              --分隔符|的数量
       set @detailqty=0        
       set @detailcount=9           
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
	set @Fauxprice=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --单价
	set @Fauxqty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)    --数量  
	set @FDCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5) --仓库id
	set @FDCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6) --仓位id
	set @FSourceEntryID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7) --下推的明细id
	set @FSourceInterId=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8) --下推的明FInterID
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)
	select @FSourceBillNo=FBillNo,@FPOStyle = FPOStyle,@FDeptID=FDeptID,@FEmpID=FEmpID,@FFetchAdd=FFetchAdd,@FExplanation=FExplanation  from POInstock where FInterID=@FSourceInterId --下推的单据编号
	select @FAuxQtyMust = FAuxQty-FAuxCommitQty,@FOrderBillNo=FOrderBillNo,@FOrderInterID=FOrderInterID,@FOrderEntryID=FOrderEntryID from POInstockEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
	select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --单位换算率
	set @FQtyMust=@FAuxQtyMust*@FCoefficient --基本单位可验收的数量 
	select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --基本单位数量 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --单位计划单价
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --计划单价金额 
	set @Famount=@Fauxqty*@Fauxprice
	set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex
    
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
      if(@value=1)
		begin
		  select @IsExist=COUNT(1) from ICInventory where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID
		  if(@IsExist=0)
			begin
			INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
			SELECT '0',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,0,'',@FQty,@FSecQty
			end
		  else
			begin
			update ICInventory set FQty=FQty+@FQty,FSecQty=FSecQty+@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID 
		  end    
		end
 
INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FAuxPropID,FBatchNo,FQtyMust,FQty,FUnitID,FAuxQtyMust,
Fauxqty,FSecCoefficient,FSecQty,FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FDCStockID,FDCSPID,
FOrgBillEntryID,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FContractBillNo,FContractInterID,FContractEntryID,
FOrderBillNo,FOrderInterID,FOrderEntryID,FAllHookQTY,FAllHookAmount,FCurrentHookQTY,FCurrentHookAmount,FAuxQtyInvoice,FSecInvoiceQty,
FQtyInvoice,FPlanMode,FMTONo,FChkPassItem) 
VALUES (@FInterID,@FEntryID,'0','','',@FItemID,0,@FBatchNo,@FQtyMust,@FQty,@FUnitID,@FAuxQtyMust,@Fauxqty,@FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,
@Fauxprice,@Famount,'',Null,0,Null,@FDCStockID,@FDCSPID,0,0,@FSourceBillNo,72,@FSourceInterId,@FSourceEntryID,'',0,0,@FOrderBillNo,@FOrderInterID,@FOrderEntryID,0,0,0,0,0,0,0,14036,'',1058) 


end
set @detailqty=@detailqty+1
end
update ICStockBill set FPOStyle = @FPOStyle,FDeptID=@FDeptID,FEmpID=@FEmpID,FFetchAdd=@FFetchAdd,FExplanation=@FExplanation where FInterID=@FInterID

EXEC p_UpdateBillRelateData 1,@FInterID,'ICStockBill','ICStockBillEntry' 
update ICStockBill set FEmpID=@FEmpID,FDeptID=@FDeptID where FInterID=@FInterID
set nocount on
declare @fcheck_fail int
declare @fsrccommitfield_prevalue decimal(28,13)
declare @fsrccommitfield_endvalue decimal(28,13)
declare @maxorder int 
update src set @fsrccommitfield_prevalue= isnull(src.fconcommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fauxqty*cast(t1.fcoefficient as float),
@maxorder=(select fvalue from t_systemprofile where fcategory='ic' and fkey='cqtylargerpoqty'),
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
     src.fconcommitqty=@fsrccommitfield_endvalue,
     src.fauxconcommitqty=@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)
 from poinstockentry src 
     inner join poinstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fauxqty) as fauxqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 and u1.fchkpassitem=1058 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fsourceentryid
 inner join t_measureunit t1 on src.funitid=t1.fitemid
  
Update v1 SET v1.FStatus= (CASE WHEN u1.sumqty>0 THEN (CASE WHEN u1.qty <= u1.sumqty THEN 3 ELSE 1 END) ELSE v1.FStatus END),FChildren=(CASE WHEN u1.sumqty>0 THEN 1 ELSE 0 END)
From POInStock v1
inner join (select t2.FInterID,SUM(t2.fqty) AS qty, SUM(t2.fconcommitqty + t2.fcommitqty+t2.FSampleBreakQty) AS sumqty from POInStockEntry t2
    inner join ICStockBillEntry t3 on t2.FInterID = t3.fsourceinterid
    where t3.fsourcetrantype=702 AND t3.FInterID = @FInterID group by t2.FInterID) u1
on v1.FInterID = u1.FInterID

SELECT v1.FInterID,v1.FEntryID,CASE WHEN (SELECT FValue FROM t_SystemProfile WHERE FCategory='IC' AND FKey='MaterialReturnDirect')=1 THEN SUM(ISNULL(v1.FAuxBackQty,0)) ELSE 0 END+SUM(ISNULL(v1.FAuxConCommitQty,0))+ SUM(ISNULL(v1.FAuxScrapInCommitQty,0)) AS FCommitAuxQty,SUM(ISNULL(v1.Fauxqty,0)) AS Fauxqty
      INTO #tempTable FROM POInStockEntry v1 
INNER JOIN ICStockBillEntry t1 ON v1.FInterID = t1.FSourceInterID AND t1.FSourceTranType = 72
       WHERE t1.FInterID = @FInterID GROUP BY v1.FInterID,v1.FEntryID
UPDATE t SET FStatus =CASE WHEN u.FInterID IS NULL THEN 3 ELSE 1 END,FClosed = CASE WHEN u.FInterID IS NULL THEN 1 ELSE 0 END
FROM POInStock t
LEFT JOIN (SELECT DISTINCT FInterID FROM #tempTable WHERE NOT (FCommitAuxQty>=Fauxqty)) u ON u.FInterID=t.FInterID
WHERE 1=1 AND EXISTS
(select 1 from ICStockBillEntry t2 where FSourceTranType=72 and FInterID=@FInterID and t.FInterID=t2.FSourceInterID)
DROP TABLE #tempTable

 
update src set @fsrccommitfield_prevalue= isnull(src.fsecconcommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fsecqty,
@maxorder=(select fvalue from t_systemprofile where fcategory='ic' and fkey='cqtylargerpoqty'),
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
     src.fsecconcommitqty=@fsrccommitfield_endvalue
 from poinstockentry src 
     inner join poinstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fsecqty) as fsecqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 and u1.fchkpassitem=1058 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fsourceentryid
Update v1 SET v1.FStatus= (CASE WHEN u1.sumqty>0 THEN (CASE WHEN u1.qty <= u1.sumqty THEN 3 ELSE 1 END) ELSE v1.FStatus END),FChildren=(CASE WHEN u1.sumqty>0 THEN 1 ELSE 0 END)
From POInStock v1
inner join (select t2.FInterID,SUM(t2.fqty) AS qty, SUM(t2.fconcommitqty + t2.fcommitqty+t2.FSampleBreakQty) AS sumqty from POInStockEntry t2
    inner join ICStockBillEntry t3 on t2.FInterID = t3.fsourceinterid
    where t3.fsourcetrantype=702 AND t3.FInterID = @FInterID group by t2.FInterID) u1
on v1.FInterID = u1.FInterID

update src set @fsrccommitfield_prevalue= isnull(src.fscrapincommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fqty,
@maxorder=(select fvalue from t_systemprofile where fcategory='ic' and fkey='cqtylargerpoqty'),
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
     src.fscrapincommitqty=@fsrccommitfield_endvalue,
     src.fauxscrapincommitqty=@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)
 from poinstockentry src 
     inner join poinstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 and u1.fchkpassitem=1059 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fsourceentryid
 inner join t_measureunit t1 on src.funitid=t1.fitemid
  
  
  Update v1 SET v1.FStatus= (CASE WHEN u1.sumqty>0 THEN (CASE WHEN u1.qty <= u1.sumqty THEN 3 ELSE 1 END) ELSE v1.FStatus END),FChildren=(CASE WHEN u1.sumqty>0 THEN 1 ELSE 0 END)
From POInStock v1
inner join (select t2.FInterID,SUM(t2.fqty) AS qty, SUM(t2.fconcommitqty + t2.fcommitqty+t2.FSampleBreakQty) AS sumqty from POInStockEntry t2
    inner join ICStockBillEntry t3 on t2.FInterID = t3.fsourceinterid
    where t3.fsourcetrantype=702 AND t3.FInterID = @FInterID group by t2.FInterID) u1
on v1.FInterID = u1.FInterID
  
  update src set @fsrccommitfield_prevalue= isnull(src.fsecscrapincommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fsecqty,
@maxorder=(select fvalue from t_systemprofile where fcategory='ic' and fkey='cqtylargerpoqty'),
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
     src.fsecscrapincommitqty=@fsrccommitfield_endvalue
 from poinstockentry src 
     inner join poinstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fsecqty) as fsecqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 and u1.fchkpassitem=1059 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fsourceentryid
IF EXISTS (SELECT 1 FROM ICBillRelations_Sale WHERE FBillType = 1 AND FBillID=@FInterID)
BEGIN
    UPDATE t1 SET t1.FChildren=t1.FChildren+1
    FROM POInstock t1 INNER JOIN POInstockEntry t2 ON     t1.FInterID=t2.FInterID
    INNER JOIN ICBillRelations_Sale t3 ON t3.FMultiEntryID=t2.FEntryID AND t3.FMultiInterID=t2.FInterID
    WHERE t3.FBillType=1 AND t3.FBillID=@FInterID
END
ELSE
BEGIN
    UPDATE t3 SET t3.FChildren=t3.FChildren+1
    FROM ICStockBill t1 INNER JOIN ICStockBillEntry     t2 ON t1.FInterID=t2.FInterID
    INNER JOIN POInstock t3 ON t3.FTranType=t2.FSourceTranType AND t3.FInterID=t2.FSourceInterID
    WHERE t1.FTranType=1 AND t1.FInterID=@FInterID AND t2.FSourceInterID>0
END

 
  
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
