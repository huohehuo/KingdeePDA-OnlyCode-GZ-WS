if (exists (select * from sys.objects where name = 'proc_PushReceiving'))
    drop proc proc_PushReceiving
go
create proc proc_PushReceiving
(
 @mainStr nvarchar(1000),--�������
 @detailStr1 varchar(max),--��ϸ����
 @detailStr2 varchar(max),
 @detailStr3 varchar(max),
 @detailStr4 varchar(max),
 @detailStr5 varchar(max)
)
as 
--------------����һ��ģʽ��Ҳ���ǲ���ˢ�¶�������Ӱ�����Ϣ�������������
set nocount on
--------------��ʼ�洢����
begin
--------------����ѡ������Ϊ����ȫ���ع�
set xact_abort on
--------------�������� 
begin tran
declare @FInterID varchar(20),     --����id
        @FBillNo varchar(50),      -- ���   
        @FROB varchar(20),         --�����ֱ�ʶ
        @Fdate varchar(50),       --����  
        @FSupplyID varchar(20),   --������λid
        @FCurrencyID nvarchar(20),--�ұ�id 
        @FSaleStyle varchar(20),  --���۷�ʽid 
        @FPOStyle varchar(20),    --�ɹ���ʽ
        @FFetchAdd varchar(100),  --�����ص���
        @FCheckDate varchar(50),  --������� 
        @FFManagerID varchar(20), --����
        @FSManagerID varchar(20), --����
        @FManagerID varchar(20),  --����id
        @FDeptID varchar(20),     --����id
        @FEmpID varchar(20),      --ҵ��Աid
        @FBillerID varchar(20),   --�Ƶ���id 
        @FSettleDate varchar(50), --��������
        @FExplanation varchar(200),--ժҪ 
        @FMarketingStyle varchar(20),--����ҵ������
        @FSelTranType varchar(20)  --Դ������
set @FBillerID = dbo.getString(@mainStr,'|',1) --����Ա  
set @Fdate =dbo.getString(@mainStr,'|',2)      --���� 
set @FSettleDate=dbo.getString(@mainStr,'|',3) --��������
set @FSupplyID =dbo.getString(@mainStr,'|',4)   --������λid 
set @FPOStyle =dbo.getString(@mainStr,'|',5) --�ɹ���ʽ   
set @FDeptID =dbo.getString(@mainStr,'|',6)   --����id
set @FEmpID =dbo.getString(@mainStr,'|',7)    --ҵ��Աid
set @FManagerID =dbo.getString(@mainStr,'|',8) --����id
set @FFManagerID=dbo.getString(@mainStr,'|',9) --����
set @FSManagerID=dbo.getString(@mainStr,'|',10) --���� 
set @Fdate = convert(varchar(20),GETDATE(),23)  
exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --�õ�@FInterID 
------------------------------------------------------------�õ����
if(@FROB='����')
set @FROB=-1
else
set @FROB=1
 set @FBillNo ='' 
 exec proc_GetICBillNo 1, @FBillNo out 
-----------------------------------------------------------�õ����
set @FCurrencyID=1 --�ұ�
set @FCheckDate=null --���ʱ��  
set @FExplanation='' --��ע
  declare @IsExist varchar(10), --�Ƿ����
            @value varchar(10)--����������
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FVchInterID,FROB,FHookStatus,Fdate,
FSupplyID,FCheckDate,FFManagerID,FSManagerID,FBillerID,FPOStyle,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,
FMultiCheckDate5,FMultiCheckDate6,FRelateBrID,FPOOrdBillNo,FOrgBillInterID,FSelTranType,FBrID,FExplanation,FDeptID,FManagerID,FEmpID,
FCussentAcctID,FManageType,FSettleDate,FPrintCount) 
VALUES (@FInterID,@FBillNo,'0',1,0,0,@value,0,1,0,@Fdate,@FSupplyID,@FCheckDate,@FFManagerID,@FSManagerID,@FBillerID,@FPOStyle,Null,
Null,Null,Null,Null,Null,0,'',0,72,0,@FExplanation,@FDeptID,@FManagerID,@FEmpID,0,0,@FSettleDate,0)
update ICStockBill set FUUID=newid() where FInterID=@FInterID

declare @FEntryID varchar(20),       --�µ���ϸ���
        @FSourceEntryID varchar(20), --���Ƶ��ݵ���ϸid
        @FSourceInterId varchar(20), --���Ƶ��ݵ�FInterID
        @FSourceBillNo varchar(20),  --���Ƶĵ��ݵĵ��ݱ��
        @FOrderBillNo varchar(50),
        @FOrderInterID varchar(50),
        @FOrderEntryID varchar(50),
        
        @FItemID varchar(20),        --��Ʒid
        @FQty float,                --������λ����
        @FQtyMust float,            --������λ��������
        @FAuxQtyMust float,        --��λ����������
        @FUnitID varchar(20),       --��λid
        @Fauxqty float,            --�ϴ������� 
        @Fauxprice float,          -- ����
        @Famount float,          --���
        @FTaxAmount float,     -- 
        
        @FPlanPrice float,     --������λ�ƻ�����
        @FAuxPlanPrice float, --��λ�ƻ�����
        @FPlanAmount float,   --�ƻ��۽��     
        @FDiscountRate float,  --�ۿ���
        @FDiscountAmount float,--�ۿ۶�(��˰����*����*�ۿ���)   
        @FDCStockID varchar(20), --�ֿ�id
        @FDCSPID varchar(20), --��λid
        @FBatchNo varchar(50),--����
        @FCoefficient varchar(20),   --������
          @FSecCoefficient float, --������λ������
        @FSecQty decimal(28,10),   --������λ����
          @FSecUnitID  varchar(50), 
        @detailqty int,               --��ϸ�����ĸ���
        @detailcount int,             --��ϸÿ�����ݵĳ��� 
        @detailIndex int,            --��ϸÿ���±�
        @countindex int              --�ָ���|������
       set @detailqty=0        
       set @detailcount=9           
    while(@detailqty<5)--�ж�����ϸ���ĸ�����
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
	set @FItemID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+1)    --��Ʒid
	set @FUnitID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --��λid 
	set @Fauxprice=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --����
	set @Fauxqty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)    --����  
	set @FDCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5) --�ֿ�id
	set @FDCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6) --��λid
	set @FSourceEntryID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7) --���Ƶ���ϸid
	set @FSourceInterId=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8) --���Ƶ���FInterID
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)
	select @FSourceBillNo=FBillNo,@FPOStyle = FPOStyle,@FDeptID=FDeptID,@FEmpID=FEmpID,@FFetchAdd=FFetchAdd,@FExplanation=FExplanation  from POInstock where FInterID=@FSourceInterId --���Ƶĵ��ݱ��
	select @FAuxQtyMust = FAuxQty-FAuxCommitQty,@FOrderBillNo=FOrderBillNo,@FOrderInterID=FOrderInterID,@FOrderEntryID=FOrderEntryID from POInstockEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
	select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --��λ������
	set @FQtyMust=@FAuxQtyMust*@FCoefficient --������λ�����յ����� 
	select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --������λ���� 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --��λ�ƻ�����
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --�ƻ����۽�� 
	set @Famount=@Fauxqty*@Fauxprice
	set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex
    
        --���ϸ�����λ
       select @FSecUnitID=FSecUnitID,@FSecCoefficient=FSecCoefficient from t_ICItem where FItemID=@FItemID
      if(@FSecCoefficient<>0) --�����ж��ϴ����Ǹ�����λ���ǻ�����λ �������˵���ϴ����Ǹ�����λ
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
FQtyInvoice,FPlanMode,FMTONo) 
VALUES (@FInterID,@FEntryID,'0','','',@FItemID,0,@FBatchNo,@FQtyMust,@FQty,@FUnitID,@FAuxQtyMust,@Fauxqty,@FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,
@Fauxprice,@Famount,'',Null,0,Null,@FDCStockID,@FDCSPID,0,0,@FSourceBillNo,72,@FSourceInterId,@FSourceEntryID,'',0,0,@FOrderBillNo,@FOrderInterID,@FOrderEntryID,0,0,0,0,0,0,0,14036,'') 


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
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fqty,
@maxorder=(select fvalue from t_systemprofile where fcategory='ic' and fkey='cqtylargerpoqty'),
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
     src.fconcommitqty=@fsrccommitfield_endvalue,
     src.fauxconcommitqty=@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)
 from poinstockentry src 
     inner join poinstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
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
UPDATE m1 SET m1.FQty = m1.FQty +(-1)*m2.FQty 
             ,m1.FSecQty = m1.FSecQty +(-1)*m2.FSecQty 
FROM POInventory m1 INNER JOIN 
(SELECT u1.FItemID,u1.FAuxPropID,ISNULL(u1.FBatchNo,'') AS FBatchNo,ISNULL(u1.FMTONo,'') AS FMTONo,u1.FStockID ,ISNULL(u1.FDCSPID,0) AS FStockPlaceID
,LEFT(ISNULL(CONVERT(VARCHAR(20),u1.FKFdate ,120),''),10) AS FKFDate,isnull(u1.FKFPeriod,0) AS FKFPeriod
,t3.FTypeID AS FStockTypeID,SUM(u2.FQty) AS FQty,SUM(u2.FSecQty) AS FSecQty
FROM POInstockEntry u1 INNER JOIN ICStockBillEntry u2 ON u1.FInterID=u2.FSourceInterID AND u1.FEntryID=u2.FSourceEntryID
INNER JOIN t_Stock t3 ON u1.FStockID = t3.FItemID
WHERE u2.FInterID=@FInterID AND (u2.FSourceTranType IN (72, 73) AND t3.FTypeID IN (501,503)) 
GROUP BY u1.FItemID,u1.FAuxPropID,u1.FBatchNo,u1.FMTONo,u1.FStockID,u1.FDCSPID,u1.FKFDate,u1.FKFPeriod,t3.FTypeID) m2
ON m1.FItemID=m2.FItemID AND m1.FAuxPropID=m2.FAuxPropID AND m1.FBatchNo=m2.FBatchNo AND m1.FMTONo=m2.FMTONo 
AND m1.FStockID=m2.FStockID AND m1.FStockPlaceID=m2.FStockPlaceID AND m1.FKFDate=m2.FKFDate
AND m1.FKFPeriod=m2.FKFPeriod
  if(@value=1)
	begin
	IF EXISTS(SELECT FOrderInterID FROM ICStockBillEntry WHERE FOrderInterID>0 AND FInterID=@FInterID)
	 UPDATE u1
	 SET u1.FStockQty=u1.FStockQty+1* CAST(u2.FStockQty AS FLOAT)
		 ,u1.FSecStockQty=u1.FSecStockQty+1* CAST(u2.FSecStockQty AS FLOAT)
		 ,u1.FAuxStockQty=ROUND((u1.FStockQty+1* CAST(u2.FStockQty AS FLOAT))/ CAST(t3.FCoefficient AS FLOAT),t1.FQtyDecimal)
	 FROM POOrderEntry u1 
	 INNER JOIN 
	 (SELECT FOrderInterID,FOrderEntryID,FItemID,SUM(FQty)AS FStockQty,SUM(FSecQty)AS FSecStockQty,SUM(FAuxQty) AS FAuxStockQty
	  FROM ICStockBillEntry WHERE FInterID=@FInterID  GROUP BY FOrderInterID,FOrderEntryID,FItemID) u2
	 ON u1.FInterID=u2.FOrderInterID AND u1.FEntryID=u2.FOrderEntryID AND u1.FItemID=u2.FItemID
	 INNER JOIN t_ICItem t1 ON u1.FItemID=t1.FItemID INNER JOIN t_MeasureUnit t3 ON u1.FUnitID=t3.FItemID

	UPDATE p1 
	SET p1.FMrpClosed=CASE WHEN ISNULL(p1.FMRPAutoClosed,1)=1 THEN (CASE WHEN p1.FStockQty<p1.FQty THEN 0 ELSE 1 END) ELSE p1.FMrpClosed END
	FROM POOrderEntry p1 INNER JOIN ICStockBillEntry u1 ON u1.FOrderInterID=p1.FInterID AND u1.FOrderEntryID=p1.FEntryID
	WHERE u1.FInterID=@FInterID
	end
 Update t
Set t.FStatus =Case When (SELECT COUNT(1) FROM POOrderEntry WHERE (FCommitQty>0 OR ( ISNULL(FMRPClosed,0) = 1 And FQty <> 0)) AND FInterID IN(@FSourceInterId))=0 Then 1 When EXISTS(SELECT 1 FROM POOrderEntry te WHERE ISNULL(te.FMRPAutoClosed,1)=1 AND  FCommitQty < FQty  AND te.FInterID IN(@FSourceInterId)) Then 2 Else 3 End
,t.FClosed =Case When EXISTS(SELECT 1 FROM POOrderEntry te WHERE ISNULL(te.FMRPAutoClosed,1)=1 AND  FCommitQty < FQty  AND te.FInterID IN(@FSourceInterId)) Then 0 Else 1 End
From POOrder t
WHERE t.FInterID IN(@FSourceInterId)

Update t1 SET t1.FStatus=
Case when (select count(*) from POInstockEntry where FInterID=t1.FInterID and FConCommitQty<>0)=0 then 1 when (select count(*) from POInstockEntry where FInterID=t2.FSourceInterID and abs(FQty)<=abs(FConCommitQty)+abs(FBackQty))>=(Select Count(1) From POInstockEntry Where FInterID=t2.FSourceInterID) THEN 3 ELSE 2 End 
 From POInstock t1, ICStockBillEntry t2 
Where t1.FInterID = t2.FSourceInterID
 AND t2.FInterID=@FInterID
  
commit tran 
return;
--------------���ڴ���
if(0<>@@error)
	goto error1
--------------�ع�����	
error1:
	rollback tran;
--------------�����洢����
end
