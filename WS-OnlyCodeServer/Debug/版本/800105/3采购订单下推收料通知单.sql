if (exists (select * from sys.objects where name = 'proc_PurchaseOrderReceiving'))
    drop proc proc_PurchaseOrderReceiving
go
create proc proc_PurchaseOrderReceiving
(
 @mainStr nvarchar(1000),--�������
 @detailStr1 nvarchar(max),--��ϸ����
 @detailStr2 nvarchar(max),
 @detailStr3 nvarchar(max),
 @detailStr4 nvarchar(max),
 @detailStr5 nvarchar(max)
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
        @FExchangeRate float,
        @FSelTranType varchar(20)  --Դ������
set @FBillerID = dbo.getString(@mainStr,'|',1) --����Ա  
set @Fdate =dbo.getString(@mainStr,'|',2)      --����  
set @FFetchAdd =dbo.getString(@mainStr,'|',3)      --�����ص�
set @FSupplyID =dbo.getString(@mainStr,'|',4)   --������λid 
set @FPOStyle =dbo.getString(@mainStr,'|',5) --�ɹ���ʽ   
set @FDeptID =dbo.getString(@mainStr,'|',6)   --����id
set @FEmpID =dbo.getString(@mainStr,'|',7)    --ҵ��Աid 
set @FExplanation =dbo.getString(@mainStr,'|',8)    --ժҪ
set @FFManagerID=dbo.getString(@mainStr,'|',9) --���� 
set @Fdate = convert(varchar(20),GETDATE(),23)  
exec GetICMaxNum 'POInstock',@FInterID output,1,@FBillerID --�õ�@FInterID

set @FFManagerID = 0
------------------------------------------------------------�õ����
set @FBillNo = ''
 exec proc_GetICBillNo 72, @FBillNo out 
-----------------------------------------------------------�õ����
set @FCurrencyID=1 --�ұ�
set @FCheckDate=null --���ʱ��  
set @FExplanation='' --��ע

  declare @IsExist varchar(10), --�Ƿ����
            @value varchar(10)--����������
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
INSERT INTO POInstock(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,Fdate,FSupplyID,FCheckDate,FFManagerID,FDeptID,FEmpID,FBillerID,
FCurrencyID,FBizType,FExchangeRateType,FExchangeRate,FPOStyle,FWWType,FRelateBrID,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,
FMultiCheckDate5,FMultiCheckDate6,FSelTranType,FFetchAdd,FExplanation,FAreaPS,FManageType,FPOMode,FPrintCount) 
SELECT @FInterID,@FBillNo,'0',72,0,0,@value,@Fdate,@FSupplyID,Null,@FFManagerID,@FDeptID,@FEmpID,@FBillerID,@FCurrencyID,12510,1,1,@FPOStyle,0,0,Null,Null,Null,Null,Null,Null,71,@FFetchAdd,@FExplanation,20302,0,36680,0

declare @FEntryID varchar(20),       --�µ���ϸ���
        @FSourceEntryID varchar(20), --���Ƶ��ݵ���ϸid
        @FSourceInterId varchar(20), --���Ƶ��ݵ�FInterID
        @FSourceBillNo varchar(20),  --���Ƶĵ��ݵĵ��ݱ��
        @FItemID varchar(20),        --��Ʒid
        @FQty float,                --������λ����
        @FQtyMust float,            --������λ��������
        @FAuxQtyMust float,        --��λ����������
        @FUnitID varchar(20),       --��λid
        @Fauxqty float,            --�ϴ������� 
        @Fauxprice float,          -- ����
        @Famount float,          --���
        @FTaxAmount float,     -- 
        @FRateOrder float,--�ɹ���������
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
          @FDischarged int,--�ɹ����鷽ʽ
       
          
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
	select @FExchangeRate=isnull(FExchangeRate,1),@FSourceBillNo=FBillNo,@FPOStyle=FPOStyle from POOrder where FInterID=@FSourceInterId --���Ƶĵ��ݱ��
		set @Fauxprice = @Fauxprice * @FExchangeRate 
	select @FAuxQtyMust = FAuxQty-FAuxCommitQty,@FRateOrder = ISNULL(FCess,0)  from POOrderEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
	 
	select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --��λ������
	set @FQtyMust=@FAuxQtyMust*@FCoefficient --������λ�����յ����� 
	select @FPlanPrice=isnull(FPlanPrice,0)*@FExchangeRate from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --������λ���� 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --��λ�ƻ�����
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --�ƻ����۽�� 
	set @Famount=@Fauxqty*@Fauxprice
	set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex
    
       --���ϸ�����λ
       select @FSecUnitID=FSecUnitID,@FSecCoefficient=FSecCoefficient,@FDischarged = FInspectionLevel from t_ICItem where FItemID=@FItemID
      if(@FSecCoefficient<>0) --�����ж��ϴ����Ǹ�����λ���ǻ�����λ �������˵���ϴ����Ǹ�����λ
      begin
      set @FSecQty = @FQty/@FSecCoefficient
      end
      else
      begin
      set @FSecQty = 0
      end
 
  
  INSERT INTO POInstockEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FAuxPropID,FBatchNo,FQty,FUnitID,Fauxqty,FSecCoefficient,FSecQty,FDischarged,
  FCheckMethod,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FStockID,FDCSPID,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FContractBillNo,
  FContractInterID,FContractEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FOrderType,FAuxQtyPass,FQtyPass,FSecQtyPass,FAuxConPassQty,
  FConPassQty,FSecConPassQty,FAuxNotPassQty,FNotPassQty,FSecNotPassQty,FAuxSampleBreakQty,FSampleBreakQty,FSecSampleBreakQty,FScrapQty,FAuxScrapQty,
  FSecScrapQty,FAuxRelateQty,FRelateQty,FSecRelateQty,FAuxQCheckQty,FQCheckQty,FSecQCheckQty,FAuxBackQty,FBackQty,FSecBackQty,FScrapInCommitQty,
  FAuxScrapInCommitQty,FSecScrapInCommitQty,FDeliveryNoticeFID,FDeliveryNoticeEntryID,FTime,FSamBillNo,FSamInterID,FSamEntryID)  
  SELECT @FInterID,@FEntryID,'0','','',@FItemID,0,@FBatchNo,@FQty,@FUnitID,@Fauxqty,@FSecCoefficient,@FSecQty,1059,@FDischarged,@Fauxprice,@Famount,'',Null,0,Null,@FDCStockID,@FDCSPID,
  @FSourceBillNo,71,@FSourceInterId,@FSourceEntryID,'',0,0,@FSourceBillNo,@FSourceInterId,@FSourceEntryID,14036,'',71,@FAuxQty,@FQty,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'',0,0 
end
set @detailqty=@detailqty+1
end
EXEC p_UpdateBillRelateData 72,@FInterID,'POInstock','POInstockEntry' 
	update POInStock set  FPOStyle=@FPOStyle where FInterID =@FInterID
	
 UPDATE pn SET FCommitQty=ISNULL(pn.FCommitQty,0)+ISNULL(t.FQty,0),FAuxCommitQty=ISNULL(pn.FAuxCommitQty,0)+ISNULL(t.FQty,0)/ISNULL(m.FCoefficient,1)
,FSecCommitQty=ISNULL(pn.FSecCommitQty,0)+t.FSecQty
FROM ICPurchaseEntry pn
INNER JOIN ICPurchase p ON p.FInterID=pn.FInterID
INNER JOIN(SELECT SUM(ISNULL(pn.FQty,0)) AS FQty,SUM(ISNULL(pn.FSecQty,0)) AS FSecQty,pn.FInterID,pn.FEntryID,pn.FSourceInterID,pn.FSourceEntryID
            FROM POInStockEntry pn
            INNER JOIN POInstock p ON p.FInterID=pn.FInterID
        WHERE pn.FSourceTranType=71 AND p.FInterID=@FInterID
            GROUP BY pn.FInterID,pn.FEntryID,pn.FSourceInterID,pn.FSourceEntryID ) t ON t.FSourceInterID=pn.FInterID AND t.FSourceEntryID=pn.FEntryID
LEFT JOIN t_MeasureUnit m ON m.FItemID=pn.FUnitID
WHERE t.FInterID=@FInterID
UPDATE pon SET FCommitQty=ISNULL(pon.FCommitQty,0)+ISNULL(t.FQty,0),FAuxCommitQty=ISNULL(pon.FAuxCommitQty,0)+ISNULL(t.FQty,0)/ISNULL(m.FCoefficient,1)
,FSecCommitQty=ISNULL(pon.FSecCommitQty,0)+t.FSecQty
FROM POOrderEntry pon
INNER JOIN POOrder po ON po.FInterID=pon.FInterID
INNER JOIN ICPurchaseEntry pn ON  pn.FOrderInterID=pon.FInterID AND pn.FOrderEntryID=pon.FEntryID AND pn.FSourceTranType=71
INNER JOIN ICPurchase p ON p.FInterID=pn.FInterID
INNER JOIN(SELECT SUM(ISNULL(psn.FQty,0)) AS FQty,SUM(ISNULL(psn.FSecQty,0)) AS FSecQty,psn.FInterID,psn.FEntryID,psn.FSourceInterID,psn.FSourceEntryID
            FROM POInStockEntry psn
            INNER JOIN POInstock ps ON ps.FInterID=psn.FInterID
            WHERE psn.FSourceTranType=71 AND ps.FInterID=@FInterID
            GROUP BY psn.FInterID,psn.FEntryID,psn.FSourceInterID,psn.FSourceEntryID ) t ON t.FSourceInterID=pn.FInterID AND t.FSourceEntryID=pn.FEntryID
LEFT JOIN t_MeasureUnit m ON m.FItemID=pn.FUnitID
WHERE t.FInterID=@FInterID

UPDATE src SET FStatus=CASE WHEN dest.FRestQty<=0 THEN 3 ELSE 2 END
FROM POOrder src
 INNER JOIN ( 
 select FInterID,SUM(FRestQty) AS FRestQty FROM 
 (SELECT pon.FInterID,pon.FEntryID,CASE WHEN SUM(ISNULL(pon.FQty,0)-ISNULL(pon.FCommitQty,0))>0 THEN 1 ELSE 0 END AS FRestQty
FROM POOrderEntry pon
INNER JOIN POOrder po ON po.FInterID=pon.FInterID
LEFT JOIN ICPurchaseEntry pn ON pn.FOrderInterID=pon.FInterID AND pn.FOrderEntryID=pon.FEntryID AND pn.FSourceTranType=71
LEFT JOIN POInStockEntry psn ON psn.FSourceInterID=pn.FInterID AND psn.FSourceEntryID=pn.FEntryID
WHERE pon.FInterID in (select distinct FOrderInterID from POInStockEntry where FInterID= @FInterID)
GROUP BY pon.FInterID,pon.FEntryID) t1 GROUP BY FInterID) dest ON dest.FInterID=src.FInterID

set nocount on
declare @fcheck_fail int
declare @fsrccommitfield_prevalue decimal(28,13)
declare @fsrccommitfield_endvalue decimal(28,10)
declare @maxorder int 
update src set @fsrccommitfield_prevalue= isnull(src.fcommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fqty,
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when ((src.fqty > @fsrccommitfield_prevalue and isnull(srcHead.fstatus, 0)>0) or (src.fqty <= @fsrccommitfield_prevalue and isnull(srcHead.fstatus, 0) in (1,2)) or (src.fqty > @fsrccommitfield_endvalue and isnull(srcHead.fstatus, 0)>0)) then @fcheck_fail else -1 end) end,
     src.fcommitqty=@fsrccommitfield_endvalue,
     src.fauxcommitqty=@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)
 from poorderentry src 
     inner join poorder srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  poinstockentry u1 
 where u1.finterid=@FInterID
 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fsourceentryid
 inner join t_measureunit t1 on src.funitid=t1.fitemid
if (isnull(@fcheck_fail,0)=-1) 
   raiserror('���ܵ�ԭ���ǣ�
 1����ѡ�����ѱ��������ݹ���
 2����ѡ�����ѱ������
 3����ǰ���ݺ���ѡ���ݵĹ���������������ѡ���ݵ�����
 4����ѡ�����Ѿ��ر�',18,18)
else
if exists (select 1 from poorder src right join  (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  poinstockentry u1 
 where u1.finterid=@FInterID
 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest on dest.fsourceinterid = src.finterid where dest.fsourceinterid>0 and src.finterid is null)
raiserror('��ѡ�����ѱ�ɾ��',18,18)

Update t
Set t.FStatus =Case When (SELECT COUNT(1) FROM POOrderEntry WHERE (FCommitQty>0 OR (ISNULL(FMRPClosed,0)=1 AND ISNULL(FMRPAutoClosed,1)=0)) AND FInterID IN(@FSourceInterId))=0 Then 1 When (SELECT COUNT(1) FROM POOrderEntry te WHERE (ISNULL(FMRPClosed,0)=1 OR  FCommitQty >= FQty ) AND FInterID IN(@FSourceInterId))<(SELECT COUNT(1) FROM POOrderEntry WHERE FInterID IN(@FSourceInterId)) Then 2 Else 3 End
,t.FClosed =Case WHEN (SELECT COUNT(1) FROM POOrderEntry te WHERE ( FCommitQty >= FQty  OR (ISNULL(te.FMRPAutoClosed,1)=0 AND ISNULL(FMRPClosed,0)=1)) AND te.FInterID IN(@FSourceInterId))=(SELECT COUNT(1) FROM POOrderEntry te WHERE te.FInterID IN(@FSourceInterId)) Then 1 Else 0 End
From POOrder t
WHERE t.FInterID IN(@FSourceInterId)
 
Update u1
SET  u1.FReceiptQty=CASE  u2.FTrantype  WHEN 72 THEN ISNULL(u1.FReceiptQty,0)+ 1* Cast(u2.FReceiptQty as Float)  ELSE  ISNULL(u1.FReceiptQty,0) END ,
FAuxReceiptQty= CASE  u2.FTrantype  WHEN 72 THEN ROUND((ISNULL(u1.FReceiptQty,0)+1* Cast(u2.FReceiptQty as Float))/Cast(t3.FCoefficient as Float),t1.FQtyDecimal) ELSE  ISNULL(u1.FAuxReceiptQty,0) END,
FReturnQty=CASE  u2.FTrantype  WHEN 73 THEN ISNULL(u1.FReturnQty,0)+ 1* Cast(u2.FReceiptQty as Float)  ELSE  ISNULL(u1.FReturnQty,0) END ,
FAuxReturnQty=CASE  u2.FTrantype  WHEN 73 THEN ROUND((ISNULL(u1.FReturnQty,0)+1* Cast(u2.FReceiptQty as Float))/Cast(t3.FCoefficient as Float),t1.FQtyDecimal) ELSE  ISNULL(u1.FAuxReturnQty,0) END
FROM  POOrderEntry u1
Inner Join
(SELECT b.FTrantype,a.FOrderInterID,a.FOrderEntryID,a.FItemID,SUM(a.FQty)AS FReceiptQty,SUM(a.FAuxQty) AS FAuxReceiptQty
FROM POInstockEntry a  INNER JOIN POInstock b ON a.FInterID=b.FinterID AND b.FTrantype IN (72,73) 
WHERE a.FInterID=@FInterID AND a.FOrderType=71
AND EXISTS (SELECT 1 FROM POOrderEntry b WHERE b.FEntryID=a.FOrderEntryID AND b.FInterID=a.FOrderInterID )
    GROUP BY b.FTrantype,a.FOrderInterID,a.FOrderEntryID,a.FItemID) u2
ON u1.FInterID=u2.FOrderInterID AND u1.FEntryID=u2.FOrderEntryID AND u1.FItemID=u2.FItemID
INNER JOIN t_ICItem t1 ON u1.FItemID=t1.FItemID
INNER JOIN t_MeasureUnit t3 ON u1.FUnitID=t3.FItemID


UPDATE td SET td.FClosed=1,td.FAutoClosed=1 FROM ICSampleReqEntry td INNER JOIN POInstockEntry ts ON 
td.FID =ts.FSourceInterID AND ts.FSourceEntryID = td.FEntryID And ts.FSourceTranType = 1007304
WHERE td.FCommitQty>=td.FQty AND td.FClosed=0 AND ts.FInterID=@FInterID
UPDATE td SET td.FClosed=0,td.FAutoClosed=0 FROM ICSampleReqEntry td INNER JOIN POInstockEntry ts ON 
td.FID =ts.FSourceInterID AND ts.FSourceEntryID = td.FEntryID And ts.FSourceTranType = 1007304
WHERE td.FCommitQty<td.FQty AND td.FClosed=1 AND td.FAutoClosed=1 AND ts.FInterID=@FInterID
IF OBJECT_ID('tempdb..#tmpPMCIndex','U') IS NOT NULL
    DROP TABLE #tmpPMCIndex

SELECT u0.FIndex 
    INTO #tmpPMCIndex 
FROM ICPlan_PMCDetail u0 
INNER JOIN POOrderEntry u1 ON u0.FRelTranType=71 AND u0.FRelInterID=u1.FInterID AND u0.FRelEntryID=u1.FEntryID
Inner Join 
(  SELECT DISTINCT b.FTrantype,a.FOrderInterID,a.FOrderEntryID,a.FItemID
   FROM POInstockEntry a
   INNER JOIN POInstock b ON a.FInterID=b.FinterID AND b.FTrantype IN (72,73)
   Where a.FInterID = @FInterID AND a.FOrderType=71
      AND EXISTS (SELECT 1 FROM POOrderEntry b WHERE b.FEntryID=a.FOrderEntryID AND b.FInterID=a.FOrderInterID)
) u2 ON u1.FInterID=u2.FOrderInterID AND u1.FEntryID=u2.FOrderEntryID AND u1.FItemID=u2.FItemID 

CREATE CLUSTERED INDEX PK_#tmpPMCIndex ON #tmpPMCIndex(FIndex) 

Update u0
  SET u0.FWillInQty=CASE WHEN (ISNULl(u1.FReceiptQty,0)-ISNULL(u1.FReturnQty,0))>u1.FQty THEN
                                          (ISNULl(u1.FReceiptQty,0)-ISNULL(u1.FReturnQty,0))-u1.FStockQty 
           Else 
                 (CASE WHEN u1.FQty>u1.FStockQty THEN u1.FQty-u1.FStockQty ELSE u0.FWillInQty END)
           End
FROM ICPlan_PMCDetail u0 
INNER JOIN POOrderEntry u1 ON u0.FRelTranType=71 AND u0.FRelInterID=u1.FInterID AND u0.FRelEntryID=u1.FEntryID
WHERE exists(select 1 from  #tmpPMCIndex where FIndex=u0.FIndex)
DROP TABLE #tmpPMCIndex

 

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
