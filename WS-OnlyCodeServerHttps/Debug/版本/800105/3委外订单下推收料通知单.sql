if (exists (select * from sys.objects where name = 'proc_OutsourcingOrderReceiving'))
    drop proc proc_OutsourcingOrderReceiving
go
create proc proc_OutsourcingOrderReceiving
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
        @FExplanation varchar(200),--摘要 
        @FMarketingStyle varchar(20),--销售业务类型
        @FExchangeRate float,
        @FSelTranType varchar(20)  --源单类型
set @FBillerID = dbo.getString(@mainStr,'|',1) --操作员  
set @Fdate =dbo.getString(@mainStr,'|',2)      --日期  
set @FFetchAdd =dbo.getString(@mainStr,'|',3)      --交货地点
set @FSupplyID =dbo.getString(@mainStr,'|',4)   --购货单位id 
set @FPOStyle =dbo.getString(@mainStr,'|',5) --采购方式   
set @FDeptID =dbo.getString(@mainStr,'|',6)   --部门id
set @FEmpID =dbo.getString(@mainStr,'|',7)    --业务员id 
set @FExplanation =dbo.getString(@mainStr,'|',8)    --摘要
set @FFManagerID=dbo.getString(@mainStr,'|',9) --主管 
set @Fdate = convert(varchar(20),GETDATE(),23)  
exec GetICMaxNum 'POInstock',@FInterID output,1,@FBillerID --得到@FInterID

set @FFManagerID = 0
------------------------------------------------------------得到编号
set @FBillNo = ''
 exec proc_GetICBillNo 72, @FBillNo out 
-----------------------------------------------------------得到编号
set @FCurrencyID=1 --币别
set @FCheckDate=null --审核时间  
set @FExplanation='' --备注

  declare @IsExist varchar(10), --是否存在
            @value varchar(10)--库存更新类型
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
INSERT INTO POInstock(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,Fdate,FSupplyID,FCheckDate,FFManagerID,FDeptID,FEmpID,
FBillerID,FCurrencyID,FBizType,FExchangeRateType,FExchangeRate,FPOStyle,FWWType,FRelateBrID,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,
FMultiCheckDate4,FMultiCheckDate5,FMultiCheckDate6,FSelTranType,FFetchAdd,FExplanation,FAreaPS,FManageType,FPOMode,FPrintCount,FHeadSelfP0341) 
SELECT @FInterID,@FBillNo,'0',72,0,0,@value,@Fdate,@FSupplyID,Null,@FFManagerID,@FDeptID,@FEmpID,@FBillerID,1,12511,1,1,@FPOStyle,14190,0,Null,Null,
Null,Null,Null,Null,1007105,'','',20302,0,36680,0,CONVERT(varchar(20),getdate(),20)
declare @FEntryID varchar(20),       --新的明细序号
        @FSourceEntryID varchar(20), --下推单据的明细id
        @FSourceInterId varchar(20), --下推单据的FInterID
        @FSourceBillNo varchar(20),  --下推的单据的单据编号
        @FItemID varchar(20),        --商品id
        @FQty float,                --基本单位数量
        @FQtyMust float,            --基本单位可验数量
        @FAuxQtyMust float,        --单位可验收数量
        @FUnitID varchar(20),       --单位id
        @Fauxqty float,            --上传的数量 
        @Fauxprice float,          -- 单价
        @Famount float,          --金额
        @FTaxAmount float,     -- 
        @FRateOrder float,--采购订单汇率
        @FPlanPrice float,     --基本单位计划单价
        @FAuxPlanPrice float, --单位计划单价
        @FPlanAmount float,   --计划价金额     
        @FDiscountRate float,  --折扣率
        @FDiscountAmount float,--折扣额(含税单价*数量*折扣率)   
        @FDCStockID varchar(20), --仓库id
        @FDCSPID varchar(20), --仓位id
        @FBatchNo varchar(50),--批号
        @FCoefficient varchar(20),   --换算率
          @FSecCoefficient float, --辅助单位换算率
        @FSecQty decimal(28,10),   --辅助单位数量
          @FSecUnitID  varchar(50),  
          @FDischarged int,--采购检验方式
       @FDetailID int,
          
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
	select @FExchangeRate=isnull(FExchangeRate,1),@FSourceBillNo=FBillNo  from ICSubContract where FInterID=@FSourceInterId --下推的单据编号
		set @Fauxprice = @Fauxprice * @FExchangeRate 
	select @FAuxQtyMust = FAuxQty-FAuxCommitQty,@FDetailID = FDetailID  from ICSubContractEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
	 
	select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --单位换算率
	set @FQtyMust=@FAuxQtyMust*@FCoefficient --基本单位可验收的数量 
	select @FPlanPrice=isnull(FPlanPrice,0)*@FExchangeRate from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --基本单位数量 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --单位计划单价
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --计划单价金额 
	set @Famount=@Fauxqty*@Fauxprice
	set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex
    
       --物料辅助单位
       select @FSecUnitID=FSecUnitID,@FSecCoefficient=FSecCoefficient,@FDischarged = FInspectionLevel from t_ICItem where FItemID=@FItemID
      if(@FSecCoefficient<>0) --这里判断上传的是辅助单位还是基本单位 如果成立说明上传的是辅助单位
      begin
      set @FSecQty = @FQty/@FSecCoefficient
      end
      else
      begin
      set @FSecQty = 0
      end
 
INSERT INTO POInstockEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FAuxPropID,FBatchNo,FQty,FUnitID,Fauxqty,FSecCoefficient,FSecQty,
FDischarged,FCheckMethod,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FStockID,FDCSPID,FSourceBillNo,FSourceTranType,FSourceInterId,
FSourceEntryID,FContractBillNo,FContractInterID,FContractEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FOrderType,FAuxQtyPass,
FQtyPass,FSecQtyPass,FAuxConPassQty,FConPassQty,FSecConPassQty,FAuxNotPassQty,FNotPassQty,FSecNotPassQty,FAuxSampleBreakQty,FSampleBreakQty,
FSecSampleBreakQty,FScrapQty,FAuxScrapQty,FSecScrapQty,FAuxRelateQty,FRelateQty,FSecRelateQty,FAuxQCheckQty,FQCheckQty,FSecQCheckQty,FAuxBackQty,
FBackQty,FSecBackQty,FScrapInCommitQty,FAuxScrapInCommitQty,FSecScrapInCommitQty,FDeliveryNoticeFID,FDeliveryNoticeEntryID,FTime,FSamBillNo,
FSamInterID,FSamEntryID,FEntrySelfP0377,FEntrySelfP0378,FEntrySelfP0379)  
SELECT @FInterID,@FEntryID,'0','','',@FItemID,0,@FBatchNo,@FQty,@FUnitID,@Fauxqty,@FSecCoefficient,@FSecQty,1059,353,@Fauxprice,@Famount,
'',Null,0,Null,@FDCStockID,@FDCSPID,@FSourceBillNo,1007105,@FSourceInterId,@FDetailID,'',0,0,@FSourceBillNo,@FSourceInterId,@FSourceEntryID,
14036,'',1007105,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'',0,0,0,0,'' 
end
set @detailqty=@detailqty+1
end
EXEC p_UpdateBillRelateData 72,@FInterID,'POInstock','POInstockEntry' 
select @FDeptID=FDepartment,@FEmpID =FEmployee   from ICSubContract where FInterID = @FSourceInterId
	update POInStock set   FDeptID=@FDeptID,FEmpID =@FEmpID where FInterID =@FInterID
UPDATE pn SET FCommitQty=ISNULL(pn.FCommitQty,0)+ISNULL(t.FQty,0),FAuxCommitQty=ISNULL(pn.FAuxCommitQty,0)+ISNULL(t.FQty,0)/ISNULL(m.FCoefficient,1)
,FSecCommitQty=ISNULL(pn.FSecCommitQty,0)+t.FSecQty
FROM ICPurchaseEntry pn
INNER JOIN ICPurchase p ON p.FInterID=pn.FInterID
INNER JOIN(SELECT SUM(ISNULL(pn.FQty,0)) AS FQty,SUM(ISNULL(pn.FSecQty,0)) AS FSecQty,pn.FInterID,pn.FEntryID,pn.FSourceInterID,pn.FSourceEntryID
            FROM POInStockEntry pn
            INNER JOIN POInstock p ON p.FInterID=pn.FInterID
        WHERE pn.FSourceTranType=1007105 AND p.FInterID=@FInterID
            GROUP BY pn.FInterID,pn.FEntryID,pn.FSourceInterID,pn.FSourceEntryID ) t ON t.FSourceInterID=pn.FInterID AND t.FSourceEntryID=pn.FEntryID
LEFT JOIN t_MeasureUnit m ON m.FItemID=pn.FUnitID
WHERE t.FInterID=@FInterID
UPDATE pon SET FCommitQty=ISNULL(pon.FCommitQty,0)+ISNULL(t.FQty,0),FAuxCommitQty=ISNULL(pon.FAuxCommitQty,0)+ISNULL(t.FQty,0)/ISNULL(m.FCoefficient,1)
,FSecCommitQty=ISNULL(pon.FSecCommitQty,0)+t.FSecQty
FROM ICSubContractEntry pon
INNER JOIN ICSubContract po ON po.FInterID=pon.FInterID
INNER JOIN ICPurchaseEntry pn ON  pn.FOrderInterID=pon.FInterID AND pn.FOrderEntryID=pon.FEntryID AND pn.FSourceTranType=1007105
INNER JOIN ICPurchase p ON p.FInterID=pn.FInterID
INNER JOIN(SELECT SUM(ISNULL(psn.FQty,0)) AS FQty,SUM(ISNULL(psn.FSecQty,0)) AS FSecQty,psn.FInterID,psn.FEntryID,psn.FSourceInterID,psn.FSourceEntryID
            FROM POInStockEntry psn
            INNER JOIN POInstock ps ON ps.FInterID=psn.FInterID
            WHERE psn.FSourceTranType=1007105 AND ps.FInterID=@FInterID
            GROUP BY psn.FInterID,psn.FEntryID,psn.FSourceInterID,psn.FSourceEntryID ) t ON t.FSourceInterID=pn.FInterID AND t.FSourceEntryID=pn.FEntryID
LEFT JOIN t_MeasureUnit m ON m.FItemID=pn.FUnitID
WHERE t.FInterID=@FInterID
UPDATE src SET FStatus=CASE WHEN dest.FRestQty<=0 THEN 3 ELSE 2 END
FROM ICSubContract src
 INNER JOIN ( 
 select FInterID,SUM(FRestQty) AS FRestQty FROM 
 (SELECT pon.FInterID,pon.FEntryID,CASE WHEN SUM(ISNULL(pon.FQty,0)-ISNULL(pon.FCommitQty,0))>0 THEN 1 ELSE 0 END AS FRestQty
FROM ICSubContractEntry pon
INNER JOIN ICSubContract po ON po.FInterID=pon.FInterID
LEFT JOIN ICPurchaseEntry pn ON pn.FOrderInterID=pon.FInterID AND pn.FOrderEntryID=pon.FEntryID AND pn.FSourceTranType=1007105
LEFT JOIN POInStockEntry psn ON psn.FSourceInterID=pn.FInterID AND psn.FSourceEntryID=pn.FEntryID
WHERE pon.FInterID in (select distinct FOrderInterID from POInStockEntry where FInterID= @FInterID)
GROUP BY pon.FInterID,pon.FEntryID) t1 GROUP BY FInterID) dest ON dest.FInterID=src.FInterID

set nocount on
declare @fcheck_fail int
declare @fsrccommitfield_prevalue decimal(28,13)
declare @fsrccommitfield_endvalue decimal(28,13)
declare @maxorder int 
update src set @fsrccommitfield_prevalue= isnull(src.fcommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fqty,
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
  src.fcommitqty= case when src.finlowlimitqty < 0 then -1 * @fsrccommitfield_endvalue else @fsrccommitfield_endvalue end,
     src.fauxcommitqty= case when src.finlowlimitqty < 0 then -1 * (@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)) else @fsrccommitfield_endvalue/cast(t1.fcoefficient as float) end
 from icsubcontractentry src 
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  poinstockentry u1 
 where u1.finterid=@FInterID
 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fdetailid = dest.fsourceentryid
 inner join t_measureunit t1 on src.funitid=t1.fitemid
 UPDATE T1 SET T1.FStatus=
CASE WHEN NOT EXISTS(SELECT 1 FROM ICSubContractEntry WHERE FInterID=T1.FInterID AND FCommitQty<>0) THEN 1 
WHEN NOT EXISTS (SELECT 1 FROM ICSubContractEntry WHERE FInterID=T1.FInterID AND FInLowLimitQty>FAuxCommitQty) THEN 3 
ELSE 2 END,
T1.FCloserID=CASE WHEN NOT EXISTS (SELECT 1 FROM ICSubContractEntry WHERE FInterID=T1.FInterID AND FInLowLimitQty>FAuxCommitQty) THEN 16394 ELSE 0 END ,
T1.FCLOSED=CASE WHEN NOT EXISTS (SELECT 1 FROM ICSubContractEntry WHERE FInterID=T1.FInterID AND FInLowLimitQty>FAuxCommitQty) THEN 1 ELSE 0 END 
FROM ICSubContract T1, POInstockEntry T2 
WHERE t2.FOrderType=1007105 AND T1.FInterID = T2.FSourceInterID
 AND T2.FInterID=@FInterID
UPDATE T1 SET T1.FClosedDate=CASE WHEN ISNULL(t1.FClosed,0)=1 THEN GETDATE() ELSE NULL END
FROM ICSubContract T1, POInstockEntry T2 
WHERE T1.FInterID = T2.FSourceInterID
 AND T2.FInterID=@FInterID
set nocount on
 
update src set @fsrccommitfield_prevalue= isnull(src.fseccommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fsecqty,
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
  src.fseccommitqty= case when src.fsecqty < 0 then -1 * @fsrccommitfield_endvalue else @fsrccommitfield_endvalue end
 from icsubcontractentry src 
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fsecqty) as fsecqty
 from  poinstockentry u1 
 where u1.finterid=@FInterID
 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fdetailid = dest.fsourceentryid

 IF EXISTS ( SELECT 1 FROM syscolumns WHERE name = 'FConnectFlag'  AND id = object_id('ICSubContract') )  AND EXISTS ( SELECT 1 FROM syscolumns WHERE name='FSourceInterID'  AND id = object_id('POInstockEntry') ) EXEC('UPDATE t3 SET t3.FConnectFlag=t3.FConnectFlag+1
FROM POInstock t1 INNER JOIN POInstockEntry t2 ON t1.FInterID=t2.FInterID
INNER JOIN ICSubContract t3 ON t3.FClassTypeID=t2.FSourceTranType AND t3.FInterID=t2.FSourceInterID
WHERE t1.FTranType=72 AND t1.FInterID='+@FInterID+' AND t2.FSourceInterID>0 ' )
 ELSE IF EXISTS ( SELECT 1 FROM syscolumns WHERE name = 'FChildren'  AND id = object_id('ICSubContract') )  AND EXISTS ( SELECT 1 FROM syscolumns WHERE name='FSourceInterID'  AND id = object_id('POInstockEntry') ) EXEC('UPDATE t3 SET t3.FChildren=t3.FChildren+1
FROM POInstock t1 INNER JOIN POInstockEntry t2 ON t1.FInterID=t2.FInterID
INNER JOIN ICSubContract t3 ON t3.FClassTypeID=t2.FSourceTranType AND t3.FInterID=t2.FSourceInterID
WHERE t1.FTranType=72 AND t1.FInterID='+@FInterID+' AND t2.FSourceInterID>0 ' ) 
 
 UPDATE td SET td.FClosed=1,td.FAutoClosed=1 FROM ICSampleReqEntry td INNER JOIN POInstockEntry ts ON 
td.FID =ts.FSourceInterID AND ts.FSourceEntryID = td.FEntryID And ts.FSourceTranType = 1007304
WHERE td.FCommitQty>=td.FQty AND td.FClosed=0 AND ts.FInterID=@FInterID
UPDATE td SET td.FClosed=0,td.FAutoClosed=0 FROM ICSampleReqEntry td INNER JOIN POInstockEntry ts ON 
td.FID =ts.FSourceInterID AND ts.FSourceEntryID = td.FEntryID And ts.FSourceTranType = 1007304
WHERE td.FCommitQty<td.FQty AND td.FClosed=1 AND td.FAutoClosed=1 AND ts.FInterID=@FInterID
Update u1
SET  u1.FReceiptQty=CASE  u2.FTrantype  WHEN 72 THEN ISNULL(u1.FReceiptQty,0)+ 1* Cast(u2.FReceiptQty as Float)  ELSE  ISNULL(u1.FReceiptQty,0) END ,
FAuxReceiptQty= CASE  u2.FTrantype  WHEN 72 THEN ROUND((ISNULL(u1.FReceiptQty,0)+1* Cast(u2.FReceiptQty as Float))/Cast(t3.FCoefficient as Float),t1.FQtyDecimal) ELSE  ISNULL(u1.FAuxReceiptQty,0) END,
FReturnQty=CASE  u2.FTrantype  WHEN 73 THEN ISNULL(u1.FReturnQty,0)+ 1* Cast(u2.FReceiptQty as Float)  ELSE  ISNULL(u1.FReturnQty,0) END ,
FAuxReturnQty=CASE  u2.FTrantype  WHEN 73 THEN ROUND((ISNULL(u1.FReturnQty,0)+1* Cast(u2.FReceiptQty as Float))/Cast(t3.FCoefficient as Float),t1.FQtyDecimal) ELSE  ISNULL(u1.FAuxReturnQty,0) END
FROM  ICSubContractEntry u1
Inner Join
(SELECT b.FTrantype,a.FOrderInterID,a.FOrderEntryID,a.FItemID,SUM(a.FQty)AS FReceiptQty,SUM(a.FAuxQty) AS FAuxReceiptQty
FROM POInstockEntry a  INNER JOIN POInstock b ON a.FInterID=b.FinterID AND b.FTrantype IN (72,73) 
WHERE a.FInterID=@FInterID AND a.FOrderType=1007105
AND EXISTS (SELECT 1 FROM ICSubContractEntry b WHERE b.FEntryID=a.FOrderEntryID AND b.FInterID=a.FOrderInterID )
    GROUP BY b.FTrantype,a.FOrderInterID,a.FOrderEntryID,a.FItemID) u2
ON u1.FInterID=u2.FOrderInterID AND u1.FEntryID=u2.FOrderEntryID AND u1.FItemID=u2.FItemID
INNER JOIN t_ICItem t1 ON u1.FItemID=t1.FItemID
INNER JOIN t_MeasureUnit t3 ON u1.FUnitID=t3.FItemID
---审核
SET NOCOUNT ON
CREATE TABLE #TempBill
(FID INT IDENTITY (1,1),FBrNo VARCHAR(10) NOT NULL DEFAULT(''),
 FInterID INT NOT NULL DEFAULT(0),
 FEntryID INT NOT NULL DEFAULT(0),
 FTranType INT NOT NULL DEFAULT(0),
 FItemID INT NOT NULL DEFAULT(0),
 FBatchNo NVARCHAR(255) NOT NULL DEFAULT(''),
 FMTONo NVARCHAR(255) NOT NULL DEFAULT(''),
 FAuxPropID INT NOT NULL DEFAULT(0),
 FStockID INT NOT NULL DEFAULT(0),
 FStockPlaceID INT NOT NULL DEFAULT(0),
 FKFPeriod INT NOT NULL DEFAULT(0),
 FKFDate VARCHAR(20) NOT NULL DEFAULT(''),
 FSupplyID INT NOT NULL DEFAULT(0),
 FQty DECIMAL(28,10) NOT NULL DEFAULT(0),
 FSecQty DECIMAL(28,10) NOT NULL DEFAULT(0),
 FAmount DECIMAL(28,2)  NOT NULL DEFAULT(0) 
)
INSERT INTO #TempBill(FBrNo,FInterID,FEntryID,FTranType,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
SELECT '',u1.FInterID,u1.FEntryID,72 AS FTranType,u1.FItemID,ISNULL(u1.FBatchNo,'') AS FBatchNo,ISNULL(u1.FMTONo,'') AS FMTONo,
       u1.FAuxPropID,ISNULL(u1.FStockID,0) AS FStockID,ISNULL(u1.FDCSPID,0) AS FDCSPID,ISNULL(u1.FKFPeriod,0) AS FKFPeriod,
       LEFT(ISNULL(CONVERT(VARCHAR(20),u1.FKFdate ,120),''),10) AS FKFDate,
1*u1.FQty AS FQty,1*u1.FSecQty AS FSecQty
FROM POInstockEntry u1 INNER JOIN t_Stock t1 ON u1.FStockID=t1.FItemID
WHERE u1.FInterID=@FInterID AND t1.FTypeID IN (501,503)
 order by  u1.FEntryID
 SELECT * INTO #TempBill2 FROM #TempBill 
UPDATE t1
SET t1.FQty=t1.FQty+(u1.FQty),
t1.FSecQty=t1.FSecQty+(u1.FSecQty)
FROM POInventory t1 INNER JOIN
(SELECT FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate
        ,SUM(FQty) AS FQty,SUM(FSecQty) AS FSecQty
 FROM #TempBill2 
 GROUP BY FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate
) u1
ON t1.FItemID=u1.FItemID AND t1.FBatchNo=u1.FBatchNo AND t1.FMTONo=u1.FMTONo AND t1.FAuxPropID=u1.FAuxPropID
   AND t1.FStockID=u1.FStockID AND t1.FStockPlaceID=u1.FStockPlaceID 
   AND t1.FKFPeriod=u1.FKFPeriod AND t1.FKFDate=u1.FKFDate

DELETE u1
FROM POInventory t1 INNER JOIN #TempBill2 u1
ON t1.FItemID=u1.FItemID AND t1.FBatchNo=u1.FBatchNo AND t1.FMTONo=u1.FMTONo AND t1.FAuxPropID=u1.FAuxPropID
   AND t1.FStockID=u1.FStockID AND t1.FStockPlaceID=u1.FStockPlaceID 
   AND t1.FKFPeriod=u1.FKFPeriod AND t1.FKFDate=u1.FKFDate

INSERT INTO POInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
SELECT '',FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,
       SUM(FQty) AS FQty,SUM(FSecQty) AS FSecQty
FROM #TempBill2
GROUP BY FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate

UPDATE t1
SET t1.FStockTypeID = t2.FTypeID
FROM POInventory t1 INNER JOIN
t_Stock t2 ON t1.FStockID = t2.FItemID
INNER JOIN #TempBill2 t3 
ON t1.FStockID = t3.FStockID 
AND t1.FItemID = t3.FItemID 
AND t1.FBatchNo = t3.FBatchNo 
AND t1.FMTONo = t3.FMTONo 
AND t1.FAuxPropID = t3.FAuxPropID 
AND t1.FStockPlaceID = t3.FStockPlaceID 
AND t1.FKFPeriod = t3.FKFPeriod 
AND t1.FKFDate = t3.FKFDate 
DROP TABLE #TempBill2
DROP TABLE #TempBill

Update POInstock Set FCheckerID=@FBillerID,FStatus=1,FCheckDate=@Fdate WHERE FInterID=@FInterID
UPDATE t1 SET t1.FReceiveQty=t1.FReceiveQty+t2.FQty,t1.FReceiveAuxQty= (t1.FReceiveQty+t2.FQty)/M1.FCoefficient
FROM ICSampleReqEntry t1 INNER JOIN t_MeasureUnit M1 ON T1.FUNITID=M1.FMEASUREUNITID 
INNER Join POInstockEntry t2 ON t1.FID=t2.FSamInterID AND t1.FEntryID=t2.FSamEntryID 
WHERE t2.FInterID=@FInterID
UPDATE t1 SET t1.FSendQty=t1.FSendQty+t2.FQty,t1.FSendAuxQty= (t1.FSendQty+t2.FQty)/M1.FCoefficient
FROM ICSampleReqEntry t1 INNER JOIN t_MeasureUnit M1 ON T1.FUNITID=M1.FMEASUREUNITID 
INNER Join (select FSourceInterId,FSourceEntryID,FQty from POInstockEntry where FInterID = @FInterID and FSourceTranType=1007304 ) t2 ON t1.FID=t2.FSourceInterId AND t1.FEntryID=t2.FSourceEntryID 
--
select t1.FBillNo as 单据编号,t5.FName as 供应商,t4.FName as 报检人,t3.FNumber as 物料代码,t3.FName as 物料名称,t3.FModel as 规格型号,convert(float,SUM( t2.FAuxQty)) as 报检数量 from  POInstock t1 left join POInstockEntry t2 on t1.FInterID=t2.FInterID left join t_ICItem t3 on t2.FItemID = t3.FItemID left join t_user t4 on t1.FBillerID=t4.FUserID left join t_Supplier t5 on t1.FSupplyID = t5.FItemID where t1.FInterID=@FInterID  group by t1.FBillNo,t5.FName,t4.FName,t3.FNumber,t3.FName,t3.FModel,t1.FInterID
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
