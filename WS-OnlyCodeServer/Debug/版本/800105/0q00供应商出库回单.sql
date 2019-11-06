if (exists (select * from sys.objects where name = 'proc_SupplySaleOrder')) 
    drop proc proc_SupplySaleOrder
go
create proc proc_SupplySaleOrder
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

declare @FUserID int,--��Ӧ���û�ID
        @FInterID varchar(20),     --����id
        @FBillNo varchar(50),      -- ��� 
        @FAreaPS varchar(20),      --���۷�Χ
        @FTranType varchar(20),    --�������� 
        @Fdate varchar(50),       --���� 
        @FCustID varchar(20),     --������λid
        @FCurrencyID nvarchar(20),--�ұ�id
        @FSettleID nvarchar(20),  --���㷽ʽid
        @FSaleStyle varchar(20),  --���۷�ʽid
        @FFetchStyle varchar(20), --������ʽid
        @FFetchAdd varchar(100),  --�����ص���
        @FCheckDate varchar(50),  --�������
        @FMangerID varchar(20),   --����id
        @FDeptID varchar(20),     --����id
        @FEmpID varchar(20),      --ҵ��Աid
        @FBillerID varchar(20),   --�Ƶ���id
        @FExchangeRate varchar(50),--����
        @FSettleDate varchar(50), --��������
        @FExplanation varchar(200),--ժҪ 
           @FOrderID varchar(50),--PDA���ݱ��
        @FPDAID varchar(50),  --PDA���к� 
          @FUserName varchar(128),--�ŵ��û�
        @FPDASource varchar(128),--������Դ 
        @FSelTranType varchar(20)   --Դ������ID
set @FUserID = dbo.getString(@mainStr,'|',1) --����Ա(��Ӧ���û�ID)
set @FOrderID=dbo.getString(@mainStr,'|',2) --PDA���ݱ��   
set @FPDAID=dbo.getString(@mainStr,'|',3) --PDA���к� 

set @Fdate = convert(varchar(20),GETDATE(),23)
if not exists(select 1 from t_UserPDASupply where FID=@FUserID)
begin
print convert(int,'�û��������ʧ��,����鿴PDA��½�û��Ƿ���Ч�û�')
end
else
begin
set @FSettleDate = @Fdate
select @FUserName=FName,@FBillerID=FUserID,@FSaleStyle=FSaleStyle,@FFetchAdd=FFetchAdd,@FDeptID=FDeptID,@FEmpID=FEmpID,@FMangerID=FMangerID,@FCustID=FCustID,@FExplanation=FExplanation,@FAreaPS=FAreaPS,
@FSettleID=FSettleID,@FFetchStyle=FFetchStyle,@FExplanation=FExplanation from t_UserPDASupply where FID=@FUserID
end
set @FPDASource = 'APP�Ƶ�'
--set @Fdate =dbo.getString(@mainStr,'|',2)      --����
--set @FSettleDate=dbo.getString(@mainStr,'|',3) --��������
--set @FSaleStyle =dbo.getString(@mainStr,'|',4) --���۷�ʽID
--set @FFetchAdd =dbo.getString(@mainStr,'|',5)  --�����ص�
--set @FDeptID =dbo.getString(@mainStr,'|',6)   --����id
--set @FEmpID =dbo.getString(@mainStr,'|',7)    --ҵ��Աid
--set @FMangerID =dbo.getString(@mainStr,'|',8) --����id
--set @FCustID =dbo.getString(@mainStr,'|',9)   --������λID
--set @FExplanation =dbo.getString(@mainStr,'|',10)--ժҪ
--set @FAreaPS =dbo.getString(@mainStr,'|',11)    --���۷�ΧID 
--set @FSettleID =dbo.getString(@mainStr,'|',12)  --���㷽ʽID
--set @FFetchStyle =dbo.getString(@mainStr,'|',13)--������ʽID
--set @FSelTranType=dbo.getString(@mainStr,'|',14)--Դ������ID

exec GetICMaxNum 'SEOrder',@FInterID output,1,@FBillerID --�õ�@FInterID
------------------------------------------------------------�õ����
set @FBillNo = ''
declare @FShortDate varchar(128),
        @FIndex1 int,
        @FCustNumber varchar(128) --�ͻ����
        select @FCustNumber = RIGHT(FNumber,4) from t_Organization where FItemID=@FCustID
        set @FShortDate = CONVERT(varchar(6),GETDATE(),112)
if exists(select 1 from t_PDASaleOrderIndex1 where FCustID = @FCustID and FShortDate = @FShortDate)
begin
update t_PDASaleOrderIndex1 set FIndex = FIndex+1 where FCustID = @FCustID and FShortDate = @FShortDate
select @FIndex1=FIndex from t_PDASaleOrderIndex1 where FCustID = @FCustID and FShortDate = @FShortDate
end
else
begin
insert into t_PDASaleOrderIndex1(FCustID,FShortDate,FIndex)values(@FCustID,@FShortDate,1)
set @FIndex1 = 1
end
 
set @FBillNo = 'ONYX'+@FCustNumber + @FShortDate+ right('000000'+ CONVERT(varchar(12),@FIndex1),4)
print @FBillNo
 --exec proc_GetICBillNo 81, @FBillNo out 
-----------------------------------------------------------�õ����
set @FCurrencyID=1 --�ұ�
set @FCheckDate=null --���ʱ��
set @FExchangeRate=1 --������

INSERT INTO SEOrder(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FDiscountType,Fdate,FCustAddress,FSaleStyle,FFetchStyle,FCurrencyID,FCustID,FFetchAdd,FCheckDate,FMangerID,FDeptID,FEmpID,FBillerID,FSettleID,FExchangeRateType,FExchangeRate,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FMultiCheckDate6,FPOOrdBillNo,FRelateBrID,FTransitAheadTime,FImport,FSelTranType,FBrID,FSettleDate,FExplanation,FAreaPS,FManageType,FSysStatus,FValidaterName,FConsignee,FVersionNo,FChangeDate,FChangeUser,FChangeCauses,FChangeMark,FPrintCount,FPlanCategory,FHeadSelfS0152,FHeadSelfS0160,FHeadSelfS0161) --
SELECT @FInterID,@FBillNo,'0',81,0,0,0,@Fdate,0,@FSaleStyle,'',@FCurrencyID,@FCustID,@FFetchAdd,@FCheckDate,@FMangerID,@FDeptID,@FEmpID,@FBillerID,0,1,@FExchangeRate,Null,Null,Null,Null,Null,Null,'',0,'0',0,0,0,@FSettleDate,@FExplanation,@FAreaPS,0,0,'',0,'000',Null,0,'','',0,'1',CONVERT(varchar(50),GETDATE(),20),@FUserName,@FPDASource
 
delete from t_PDASaleOrderEntry
 
declare @FEntryID int,       --��ϸ���
        @FItemID int,        --��Ʒid
        @FQty float,                --��������
        @FUnitID varchar(20),       --��λid
        @Fauxqty float,            --������λ���� 
        @Fauxprice decimal(28,10),      --����
        @FAuxTaxPrice decimal(28,10),   --��˰����
        @Famount decimal(28,4),        --���(����*����-����*����*�ۿ���)
        @FCess float,          --˰��
        @FTaxRate float,       --�ۿ���
        @FTaxAmount decimal(28,4),     --�ۿ۶�(��˰����*����*�ۿ���)             
        @FAuxPriceDiscount float,--ʵ�ʺ�˰����(��˰����-�ۿ���*��˰����)
        @FTaxAmt decimal(28,4),        --����˰��(ʵ�ʺ�˰����*����-���)
        @FAllAmount decimal(28,4),     --��˰�ϼ�(����*ʵ�ʺ�˰����)
            @FKFDate varchar(50),    --��������  
        @FKFPeriod int,       --������ 
        @FDat varchar(50),           --��������@FDate���ڴ������Զ���Ϊ@FDat 
        @FAdviceConsignDate varchar(50), --���齻������
        @FCoefficient varchar(20),   --������
         @FDCStockID varchar(20), --�ֿ�id
        @FDCSPID varchar(20),    --��λid
        @FBatchNo varchar(50),     --����
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
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7) --����
    set @FKFDate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8)  --��������
	set @FKFPeriod=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)  --������
    set @FTaxRate = 0
    set @FDat=@Fdate
	--select @FCess = FTaxRate from t_ICItem where FItemID=@FItemID --˰��
	set @FAuxTaxPrice = @Fauxprice
	select @FCess = FValueAddRate from t_Organization where FItemID=@FCustID --˰�� ȡ�ͻ�˰��
	set @Fauxprice = @Fauxprice /(1+ @FCess/100)
	
	select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --��λ������
	set @FQty=@Fauxqty*@FCoefficient  --������λ����
    
	
	--select @FAuxTaxPrice=isnull(FSalePrice,0) from t_ICItem where FItemID=@FItemID --��˰����
 --   if(@FAuxTaxPrice=0)
 --   begin
 --   set @FAuxTaxPrice= @Fauxprice *(1+ @FCess/100)   --��˰����
 --   end
    set @Famount=@Fauxqty*(@FAuxTaxPrice/(1+@FCess/100))-@Fauxqty*(@FAuxTaxPrice/(1+@FCess/100))*(@FTaxRate/100) --���
	set @FTaxAmount= @FAuxTaxPrice*@Fauxqty*(@FTaxRate/100)--�ۿ۶�
	set @FAuxPriceDiscount= @FAuxTaxPrice-@FAuxTaxPrice*(@FTaxRate/100)--ʵ�ʺ�˰����
	set @FTaxAmt=@FAuxPriceDiscount*@FQty-@Famount  --����˰��
	set @FAllAmount=@FQty*@FAuxPriceDiscount  --��˰�ϼ�
	set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex
 
	  set @FAdviceConsignDate=@FDat
  insert into t_PDASaleOrderEntry(FSourceEntryID,FDCStockID,FDCSPID,FBatchNo,FKFDate,FKFPeriod)  values(@FEntryID,@FDCStockID,@FDCSPID,@FBatchNo,@FKFDate,@FKFPeriod)
INSERT INTO SEOrderEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FAuxPropID,FQty,
FUnitID,Fauxqty,FSecCoefficient,FSecQty,Fauxprice,FAuxTaxPrice,
Famount,FCess,FTaxRate,FUniDiscount,FTaxAmount,FAuxPriceDiscount,FTaxAmt,FAllAmount,FTranLeadTime,
FInForecast,FDate,Fnote,FPlanMode,FMTONo,FBomInterID,FCostObjectID,FAdviceConsignDate,FATPDeduct,
FLockFlag,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FContractBillNo,
FContractInterID,FContractEntryID,FAuxQtyInvoice,FQtyInvoice,FSecInvoiceQty,FSecCommitInstall,
FCommitInstall,FAuxCommitInstall,FAllStdAmount,FMrpLockFlag,FHaveMrp,FReceiveAmountFor_Commit,
FOrderBillNo,FOrderEntryID) VALUES 
(@FInterID,@FEntryID,'0','','',@FItemID,0,@FQty,@FUnitID,@Fauxqty,0,0,
@Fauxprice,@FAuxTaxPrice,@Famount,@FCess,@FTaxRate,0,@FTaxAmount,@FAuxPriceDiscount,@FTaxAmt,
@FAllAmount,'',0,@FDat,'',14036,'',0,'0',@FAdviceConsignDate,0,0,'',0,0,0,'',0,0,0,0,0,0,0,0,@FAllAmount,0,0,0,'','') 
end
set @detailqty=@detailqty+1
end
EXEC p_UpdateBillRelateData 81,@FInterID,'SEOrder','SEOrderEntry' 
if not exists(  select   1  from SEOrderEntry where FInterID=@FInterID)
begin
    delete  SEOrder where FInterID=@FInterID
	goto error1
end
UPDATE SEOrder SET FSysStatus = 3 WHERE FInterID = @FInterID
 UPDATE SEOrder SET FUUID=NEWID() WHERE FInterID=@FInterID
 Update SEOrderEntry set FtaxRate = CASE WHEN Fauxqty*FauxTaxPrice = 0 OR FtaxRate != 0 then FtaxRate else (FtaxAmount/(Fauxqty*FauxTaxPrice))*100 END where Finterid =@FInterID
UPDATE obc SET obc.FItemPropID=oo.FAuxPropID
FROM PPOrderEntry oo INNER JOIN ICOrderBOMChild obc
ON oo.FInterID=@FInterID AND oo.FOrderBOMInterID=obc.FInterID AND obc.FParentID=0
UPDATE obc SET obc.FItemPropID=oo.FAuxPropID
FROM PPOrderEntry oo INNER JOIN ICOrderBOM obc
ON oo.FInterID=@FInterID AND oo.FOrderBOMInterID=obc.FInterID
UPDATE obc SET obc.FItemPropID=oo.FAuxPropID
FROM SEOrderEntry oo INNER JOIN ICOrderBOMChild obc
ON oo.FInterID=@FInterID AND oo.FOrderBOMInterID=obc.FInterID AND obc.FParentID=0
UPDATE obc SET obc.FItemPropID=oo.FAuxPropID
FROM SEOrderEntry oo INNER JOIN ICOrderBOM obc
ON oo.FInterID=@FInterID AND oo.FOrderBOMInterID=obc.FInterID
if exists(select t1.FInterID from Com_Validate t1,SEOrder t2 
 where t2.FInterID= @FInterID and t1.FRejected=0 and t1.FInterID=t2.FInterID and t1.FTranType=81)
BEGIN
update t1  set t1.FReqBillID=t3.FPOOrdBillNo ,t1.FRejectDesc=t2.Fnote,t1.FValidateQty =t2.FQty,t1.FValDate=getdate()
,t1.FBrID=isnull(t3.FBrID,0),t1.FBeValBrID=isnull(t3.FRelateBrID,0)
 ,t1.FDeliveryDate=t2.FDate,t1.FValidater=@FBillerID,t1.FValidaterName=Case when t3.FValidaterName<>'' then t3.FValidaterName else t1.FValidaterName END 
from Com_Validate t1,SEOrderEntry t2,SEOrder t3 where  t1.FInterID=t2.FInterID
 and t1.FEntryID=t2.FEntryID and t1.FInterID=t3.FInterID and t1.FInterID=@FInterID and t1.FTranType=81
update t1 set t1.fvaldate=t2.fvaldate,t1.fdeliverydate=t2.fdeliverydate,t1.fvalidater=t2.fvalidater, t1.frejectdesc = t2.frejectdesc from com_validate t1 inner join com_validate t2 on t1.FInterID=t2.FInterID where(t1.fvalidateqty=0) and t2.fvalidateqty<>0 and t1.FInterID=@FInterID delete t2 
from Com_Validate t1,SEOrderEntry t2  where  t1.FInterID=t2.FInterID
and t2.FQty=0 and t1.FEntryID=t2.FEntryID and t1.FInterID=@FInterID and t1.FTranType=81
update SEOrder set FOrderAffirm=1 where FInterID=@FInterID
update SEOrder set FValidaterName='administrator' where (FValidaterName='' or FValidaterName is NULL) and FInterID=@FInterID
END
UPDATE v1 SET v1.FExecStatus =1 FROM ICCustNetOrder v1 INNER JOIN SEOrderEntry u2 ON v1.FID = u2.FSourceInterID  AND u2.FSourceTranType = 1007553 AND u2.FInterID =@FInterID

----���
Update SEOrder Set FCheckerID=@FBillerID,FStatus=1,FCheckDate=@Fdate WHERE FInterID=@FInterID

UPDATE t1 SET t1.FSaleQty=t1.FSaleQty+t2.FQty,t1.FAuxSaleQty= (t1.FSaleQty+t2.FQty)/M1.FCoefficient,t1.FOrderClosed=(CASE WHEN t1.FSaleQty+t2.FQty>=t1.FQty THEN 1 ELSE 0 END)
FROM PPOrderEntry t1 INNER JOIN T_MEASUREUNIT M1 ON T1.FUNITID=M1.FMEASUREUNITID 
INNER Join (SELECT FSourceInterID,FSourceEntryID,SUM(FQty) AS FQty FROM SEOrderEntry WHERE FInterID=@FInterID GROUP BY FSourceInterID,FSourceEntryID) t2 ON t1.FInterID=t2.FSourceInterID AND t1.FEntryID=t2.FSourceEntryID
IF EXISTS(SELECT 1 FROM PPOrderEntry t1 INNER Join SEOrderEntry t2 ON t1.FInterID=t2.FSourceInterID AND t2.FInterID=@FInterID WHERE t1.FOrderClosed = 0) 
UPDATE t1 SET FStatus = 1 FROM PPOrder t1 INNER Join SEOrderEntry t2 ON t1.FInterID=t2.FSourceInterID AND t2.FInterID=@FInterID
ELSE
UPDATE t1 SET FStatus = 3 FROM PPOrder t1 INNER Join SEOrderEntry t2 ON t1.FInterID=t2.FSourceInterID AND t2.FInterID=@FInterID

IF OBJECT_ID('tempdb..#tmpPMCIndex','U') IS NOT NULL
  DROP TABLE #tmpPMCIndex
SELECT t1.FIndex
  INTO #tmpPMCIndex
FROM ICPlan_PMCDetail t1
INNER JOIN PPOrderEntry t2 On t1.FRelInterID=t2.FInterID ANd t1.FRelEntryID=t2.FEntryID ANd t1.FRelTranType=87 AND t2.FOrderClosed=1
INNER Join SEOrderEntry t3 ON t2.FInterID=t3.FSourceInterID And t2.FEntryID = t3.FSourceEntryID AND t3.FInterID=@FInterID
CREATE CLUSTERED INDEX PK_#tmpPMCIndex ON #tmpPMCIndex(FIndex) 

DELETE t1 FROM ICPlan_PMCDetail t1 WHERE EXISTS(SELECT 1 FROM #tmpPMCIndex WHERE FIndex=t1.FIndex)
DROP TABLE #tmpPMCIndex

IF OBJECT_ID('tempdb..#tmpPMCPPIndex','U') IS NOT NULL
  DROP TABLE #tmpPMCPPIndex
SELECT t1.FIndex
  INTO #tmpPMCPPIndex 
FROM ICPlan_PMCDetail t1
INNER JOIN PPOrderEntry t2 On t1.FRelInterID=t2.FInterID ANd t1.FRelEntryID=t2.FEntryID ANd t1.FRelTranType=87 AND t2.FOrderClosed=0 AND t2.FSaleQty<t2.FQty
INNER Join SEOrderEntry t3 ON t2.FInterID=t3.FSourceInterID And t2.FEntryID = t3.FSourceEntryID AND t3.FInterID=@FInterID

CREATE CLUSTERED INDEX PK_#tmpPMCPPIndex ON #tmpPMCPPIndex(FIndex) 

UPDATE t1
SET t1.FWillOutQty=t2.FQty-t2.FSaleQty
FROM ICPlan_PMCDetail t1
INNER JOIN PPOrderEntry t2 On t1.FRelInterID=t2.FInterID ANd t1.FRelEntryID=t2.FEntryID ANd t1.FRelTranType=87 
WHERE exists(select 1 from  #tmpPMCPPIndex where FIndex=t1.FIndex)
DROP TABLE #tmpPMCPPIndex
UPDATE v1 SET v1.FExecStatus =2 FROM ICCustNetOrder v1 INNER JOIN SEOrderEntry u2 ON v1.FID = u2.FSourceInterID  AND u2.FSourceTranType = 1007553 AND u2.FInterID =@FInterID
----���

----���۶����������۳��ⵥ
 exec proc_SupplyOutCheck @FUserID,@FInterID,@FOrderID,@FPDAID
 delete from t_PDASaleOrderEntry
----

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
 