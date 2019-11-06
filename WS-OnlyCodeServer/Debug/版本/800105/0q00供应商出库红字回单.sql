if (exists (select * from sys.objects where name = 'proc_SaleStoreRed'))
    drop proc proc_SaleStoreRed
go
create proc proc_SaleStoreRed
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
        @FROB varchar(20),         --�����ֱ�ʶ
        @Fdate varchar(50),       --����  
        @FSupplyID varchar(20),   --������λid
        @FCurrencyID nvarchar(20),--�ұ�id 
        @FSaleStyle varchar(20),  --���۷�ʽid 
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
        @FOrderID varchar(50),--PDA���ݱ��
        @FPDAID varchar(50),  --PDA���к� 
         @FUserName1 varchar(128),--�ŵ��û�
        @FPDASource varchar(128),--������Դ 
        @FSelTranType varchar(20)  --Դ������
--set @FBillerID = dbo.getString(@mainStr,'|',1) --����Ա  
--set @Fdate =dbo.getString(@mainStr,'|',2)      --����
--set @FSettleDate=dbo.getString(@mainStr,'|',3) --��������
--set @FSaleStyle =dbo.getString(@mainStr,'|',4) --���۷�ʽ  
--set @FFetchAdd =dbo.getString(@mainStr,'|',5)  --�����ص�
--set @FDeptID =dbo.getString(@mainStr,'|',6)   --����id
--set @FEmpID =dbo.getString(@mainStr,'|',7)    --ҵ��Աid
--set @FManagerID =dbo.getString(@mainStr,'|',8) --����id
--set @FSupplyID =dbo.getString(@mainStr,'|',9)   --������λ
--set @FExplanation =dbo.getString(@mainStr,'|',10)--ժҪ
--set @FFManagerID=dbo.getString(@mainStr,'|',11) --����
--set @FSManagerID=dbo.getString(@mainStr,'|',12) --����
--set @FROB=dbo.getString(@mainStr,'|',13)         --���ֺ���
--set @FMarketingStyle=dbo.getString(@mainStr,'|',14)--����ҵ������
--set @FSelTranType=dbo.getString(@mainStr,'|',15)  --Դ������
--set @Fdate = convert(varchar(20),GETDATE(),23)
--if exists(select 1 from t_PDABarCodeType where FType=1)
--begin
-- set @FOrderID=dbo.getString(@mainStr,'|',16) --PDA���ݱ��   
--set @FPDAID=dbo.getString(@mainStr,'|',17) --PDA���к� 
--end
set @FUserID = dbo.getString(@mainStr,'|',1) --����Ա(��Ӧ���û�ID)
set @FOrderID=dbo.getString(@mainStr,'|',2) --PDA���ݱ��   
set @FPDAID=dbo.getString(@mainStr,'|',3) --PDA���к� 
set @FROB='����'
set @Fdate = convert(varchar(20),GETDATE(),23)
if not exists(select 1 from t_UserPDASupply where FID=@FUserID)
begin
print convert(int,'�û��������ʧ��,����鿴PDA��½�û��Ƿ���Ч�û�')
end
else
begin
set @FSettleDate = @Fdate
 select @FUserName1=FName,@FBillerID=FUserID,@FSupplyID = FCustID,@FSaleStyle=FSaleStyle,@FDeptID=FDeptID,@FEmpID=FEmpID,@FManagerID= FMangerID,@FFManagerID=FFManagerID,@FSManagerID=FSManagerID,@FExplanation=FExplanation from t_UserPDASupply where FID=@FUserID
end
set @FPDASource = 'APP�Ƶ�'
exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --�õ�@FInterID
if(@FROB='����')
set @FROB=-1
else
set @FROB=1
------------------------------------------------------------�õ����
set @FBillNo = ''
 exec proc_GetICBillNo 21, @FBillNo out 
-----------------------------------------------------------�õ����
set @FCurrencyID=1 --�ұ�
set @FCheckDate=null --���ʱ��  
  declare @IsExist varchar(10), --�Ƿ����
            @value varchar(10)--����������
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FROB,FHookStatus,Fdate,FSupplyID,FSaleStyle,FCheckDate,
FConfirmDate,FFManagerID,FSManagerID,FBillerID,FConfirmer,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FPOOrdBillNo,
FMultiCheckDate6,FRelateBrID,FOrgBillInterID,FMarketingStyle,FSelTranType,FPrintCount,FBrID,FFetchAdd,FExplanation,FConfirmMem,FDeptID,FEmpID,FManagerID,
FVIPCardID,FReceiver,FVIPScore,FHolisticDiscountRate,FPOSName,FWorkShiftId,FLSSrcInterID,FManageType,FPayCondition,FSettleDate,FInvoiceStatus,FConsignee,
FHeadSelfB0156,FHeadSelfB0157,FHeadSelfB0159,FHeadSelfB0160,FHeadSelfB0161,FHeadSelfB0162,FHeadSelfB0163,FHeadSelfB0164,FHeadSelfB0165,
FHeadSelfB0166,FHeadSelfB0167,FHeadSelfB0168,FHeadSelfB0169) --
SELECT @FInterID,@FBillNo,'0',21,0,0,@value,-1,0,@Fdate,@FSupplyID,@FSaleStyle,Null,Null,@FFManagerID,@FSManagerID,@FBillerID,0,Null,Null,Null,Null,Null,'',
Null,0,0,12530,81,0,0,'','','',@FDeptID,@FEmpID,@FManagerID,0,'',0,0,'',0,0,0,0,@FSettleDate,'',0,CONVERT(varchar(128),GETDATE(),20),'�������Ͼ�����','13510235785','','','',990139,'',Null,'',Null,@FPDASource,@FUserName1
update ICStockBill set FUUID=newid() where FInterID=@FInterID

declare @FEntryID varchar(20),       --��ϸ���
        @FItemID varchar(20),        --��Ʒid
        @FQty float,                --������λ����
        @FUnitID varchar(20),       --��λid
        @Fauxqty float,            --�ϴ������� 
        @Fauxprice float,      -- ��λ�ɱ�����
        @Famount float,        --�ɱ����
        @FPrice float,
        @FTaxAmount float,     --
        @FConsignPrice float,   --���۵���
        @FConsignAmount float, --���۽��(���۵���*����-�ۿ۶�)
        @FPlanPrice float,     --������λ�ƻ�����
        @FAuxPlanPrice float, --��λ�ƻ�����
        @FPlanAmount float,   --�ƻ��۽��     
        @FDiscountRate float,  --�ۿ���
        @FDiscountAmount float,--�ۿ۶�(��˰����*����*�ۿ���)  
        @FBatchNo varchar(50),   --���� 
        @FDCStockID varchar(20), --�ֿ�id
        @FDCSPID varchar(20),    --��λ
        @FCoefficient varchar(20),   --������
            @FSecCoefficient float, --������λ������
        @FSecQty decimal(28,10),   --������λ����
          @FSecUnitID  varchar(50),  
             @FKFDate varchar(50),    --��������
        @FOLOrderBillNo varchar(128),--���϶�����
          @FBarCode varchar(128),
          @FEntrySelfB0176 varchar(255),
        @FKFPeriod int,       --������
        @FPeriodDate varchar(50),--��Ч��
        @detailqty int,               --��ϸ�����ĸ���
        @detailcount int,             --��ϸÿ�����ݵĳ��� 
        @detailIndex int,            --��ϸÿ���±�
        @countindex int              --�ָ���|������
       set @detailqty=0        
       set @detailcount=10           
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
	set @FConsignPrice=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --���۵���
	set @Fauxqty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)       --����
	--set @FDiscountRate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5)   --�ۿ���
	set @FDCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5)     --�ֿ�id
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6)       --����
	set @FDCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7)        --��λID
	set @FKFDate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8)  
	set @FKFPeriod=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)
	set @FOLOrderBillNo =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+10)--���϶�����
	
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
	select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --��λ������
	select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --������λ���� 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --��λ�ƻ�����
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --�ƻ����۽��
	set @FDiscountAmount=@Fauxqty*@FConsignPrice*(@FDiscountRate/100) --�ۿ۶�
	set @FConsignAmount=@Fauxqty*@FConsignPrice-@FDiscountAmount --���
	 
    	 select @FEntrySelfB0176=t_2043.FName from t_ICItem t4 left join t_Item_2043  t_2043  on t4.F_109=t_2043.FItemID where t4.FItemID=@FItemID
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
        @FHeadSelfB0155 varchar(255),-- �ұ�,
        @FHeadSelfB0157 varchar(255), -- �ͻ�����
        @FHeadSelfB0159 varchar(255), --�ջ��绰��ַ
        @FHeadSelfB0160	 varchar(255), --ҵ��˵��
        @FHeadSelfB0161  varchar(255) --����˵��
 --        if exists(select 1 from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0 )
 --begin
 --select @FSManagerID=t2.FEmpID from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0
 --end


select @FHeadSelfB0157 = t2.FName from t_Organization t1 left join t_SubMessage t2 on t1.FTypeID = t2.FInterID where FItemID=@FSupplyID
update ICStockBill set FHeadSelfB0157=@FHeadSelfB0157, FHeadSelfB0160=@FHeadSelfB0160,FHeadSelfB0161=@FHeadSelfB0161,FExplanation=@FExplanation,FSaleStyle=@FSaleStyle,FHeadSelfB0159=@FHeadSelfB0159,FSManagerID=@FSManagerID,FEmpID=@FEmpID,FDeptID=@FDeptID where FInterID=@FInterID
if exists(select 1 from t_PDABarCodeType where FType=1)
begin
--����
insert into t_PDABarCodeSign_Out(FInterID,FBarCode,FQty,FBillNo,FRemark,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before,FStatus,FDateUpLoad,FTypeID,FPrice,FStockID,FStockPlaceID)select @FInterID,a.FBarCode,a.FQty,@FBillNo,'��Ӧ�����۳������',b.FInterIDOut,b.FDateOutStore,b.FUserOutStore,1,convert(varchar(20),GETDATE(),20),21,a.FPrice,b.FStockID,b.FStockPlaceID from a_DetailsTable  a inner join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID
update t_PDABarCodeSign  set FInterIDOut=@FInterID,FDateOutStore=@fdate,FIsOutStore='δ����',FUserOutStore=@FBillerID,FQtyOut =FQtyOut + t.FQty from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
update t_PDABarCodeSign  set FInterIDDisassemble=FInterIDAssemble, FInterIDAssemble = 0,FIsOutStore='δ����'   from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID and a.FInterID=1) as t where t_PDABarCodeSign.FBarCode=t.FBarCode and t.FInterID=1
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
end
--���
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
--���

if not exists(  select   1  from ICStockBillEntry where FInterID=@FInterID)
begin
    delete  ICStockBill where FInterID=@FInterID
	goto error1
end


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
