if (exists (select * from sys.objects where name = 'proc_ReturnGoodsCheck'))
    drop proc proc_ReturnGoodsCheck
go
create proc proc_ReturnGoodsCheck
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
          @FOrderID varchar(50),--PDA���ݱ��
        @FPDAID varchar(50),  --PDA���к� 
        @FExchangeRate float,
        @FSelTranType varchar(20)  --Դ������
set @FBillerID = dbo.getString(@mainStr,'|',1) --����Ա  
set @Fdate =dbo.getString(@mainStr,'|',2)      --���� 
set @FSettleDate=dbo.getString(@mainStr,'|',3) --��������
set @FSupplyID =dbo.getString(@mainStr,'|',4)   --������λid 
set @FSaleStyle =dbo.getString(@mainStr,'|',5) --���۷�ʽ   
set @FDeptID =dbo.getString(@mainStr,'|',6)   --����id
set @FEmpID =dbo.getString(@mainStr,'|',7)    --ҵ��Աid
set @FManagerID =dbo.getString(@mainStr,'|',8) --����id
set @FFManagerID=dbo.getString(@mainStr,'|',9) --����
set @FSManagerID=dbo.getString(@mainStr,'|',10) --����   
if exists(select 1 from t_PDABarCodeType where FType=1)
begin
 set @FOrderID=dbo.getString(@mainStr,'|',11) --PDA���ݱ��   
set @FPDAID=dbo.getString(@mainStr,'|',12) --PDA���к� 
end

set @Fdate = convert(varchar(20),GETDATE(),23)
exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --�õ�@FInterID
------------------------------------------------------------�õ����
set @FBillNo = ''
 exec proc_GetICBillNo 21, @FBillNo out 
-----------------------------------------------------------�õ����
set @FCurrencyID=1 --�ұ�
set @FCheckDate=null --���ʱ��  
set @FExplanation='' --��ע
  declare @IsExist varchar(10), --�Ƿ����
            @value varchar(10)--����������
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
            if exists(select 1 from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID)
begin
  ---������
  select @FFManagerID = t2.FItemID from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID
end
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FROB,FHookStatus,Fdate,FSupplyID,FSaleStyle,FCheckDate,
FConfirmDate,FFManagerID,FSManagerID,FBillerID,FConfirmer,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FPOOrdBillNo,
FMultiCheckDate6,FRelateBrID,FOrgBillInterID,FMarketingStyle,FSelTranType,FPrintCount,FBrID,FFetchAdd,FExplanation,FConfirmMem,FDeptID,FEmpID,FManagerID,
FVIPCardID,FReceiver,FVIPScore,FHolisticDiscountRate,FPOSName,FWorkShiftId,FLSSrcInterID,FManageType,FPayCondition,FSettleDate,FInvoiceStatus,FConsignee,FHeadSelfB0156,FHeadSelfB0157,FHeadSelfB0159,FHeadSelfB0160,FHeadSelfB0161,FHeadSelfB0162,FHeadSelfB0163,FHeadSelfB0164,FHeadSelfB0165,
FHeadSelfB0166,FHeadSelfB0167,FHeadSelfB0168)
 SELECT @FInterID,@FBillNo,'0',21,0,0,@value,-1,0,@Fdate,@FSupplyID,102,Null,Null,@FFManagerID,@FSManagerID,@FBillerID,0,Null,Null,Null,Null,Null,'',Null,
0,0,12530,82,0,0,'','','',@FDeptID,@FEmpID,@FManagerID,0,'',0,0,'',0,0,0,0,@FSettleDate,'',0,CONVERT(varchar(128),GETDATE(),20),'���ƿͻ������⣩','','','','',990140,'',Null,'',Null,'PDA�Ƶ�'
update ICStockBill set FUUID=newid() where FInterID=@FInterID

declare @FEntryID varchar(20),       --�µ���ϸ���
        @FSourceEntryID varchar(20), --���Ƶ��ݵ���ϸid
        @FSourceInterId varchar(20), --���Ƶ��ݵ�FInterID
        @FSourceBillNo varchar(20),  --���Ƶĵ��ݵĵ��ݱ��
        @FSEOutInterID varchar(20),
        @FSEOutBillNo varchar(20),
        @FSEOutEntryID varchar(20),
        
        @FItemID varchar(20),        --��Ʒid
        @FQty float,                --������λ����
        @FQtyMust float,            --������λ��������
        @FAuxQtyMust float,        --��λ����������
        @FUnitID varchar(20),       --��λid
        @Fauxqty float,            --�ϴ������� 
        @FConsignPrice float,      -- ����
        @FConsignAmount float,          --���
        @FTaxAmount float,     -- 
            @FKFDate varchar(50),    --��������
        @FBarCode_Assemble varchar(128),--װ������
          @FBarCode varchar(128),
        @FKFPeriod int,       --������
        @FPeriodDate varchar(50),--��Ч��
        @FPlanPrice float,     --������λ�ƻ�����
        @FAuxPlanPrice float, --��λ�ƻ�����
        @FPlanAmount float,   --�ƻ��۽��     
        @FDiscountRate float,  --�ۿ���
        @FDiscountAmount float,--�ۿ۶�(��˰����*����*�ۿ���)   
        @FDCStockID varchar(20), --�ֿ�id
        @FDCSPID varchar(20),    --��λid
        @FBatchNo varchar(50),     --����
        @FCoefficient varchar(20),   --������
          @FOrderEntryID varchar(20),--�������id
        @FOrderInterID varchar(20),--����finterID
        @FOrderBillNo varchar(50), --�������
          @FSecCoefficient float, --������λ������
        @FSecQty decimal(28,10),   --������λ����
          @FSecUnitID  varchar(50), 
       @FEntrySelfB0176 varchar(800),--��Ʒϵ��
        @detailqty int,               --��ϸ�����ĸ���
        @detailcount int,             --��ϸÿ�����ݵĳ��� 
        @detailIndex int,            --��ϸÿ���±�
        @countindex int              --�ָ���|������
       set @detailqty=0        
       set @detailcount=12           
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
	set @FConsignPrice=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --����
	set @Fauxqty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)    --����  
	set @FDCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5) --�ֿ�id
	set @FDCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6) --��λid
	set @FSourceEntryID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7) --���Ƶ���ϸid
	set @FSourceInterId=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8) --���Ƶ���FInterID
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9) --���Ƶ���FInterID
	  set @FKFDate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+10)  
	set @FKFPeriod=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+11)
	set @FBarCode_Assemble =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+12)--װ������ �������װ������ �õط�Ϊ''
		set @detailIndex=@detailIndex+1
      select @FEntrySelfB0176=t_2043.FName from t_ICItem t4 left join t_Item_2043  t_2043  on t4.F_109=t_2043.FItemID where t4.FItemID=@FItemID
    set @FEntryID=@detailqty*50+@detailIndex 
 		   if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode_Assemble)
       begin       
         declare My_cursor cursor dynamic --scroll��ʾ������ǰ������ƶ�    dynamic����ʾ��дҲ�ɶ�
         for 
         select  t2.FBarCode,t2.FItemID,t2.FBatchNo,t2.FKFDate,t2.FKFPeriod,t2.FQty,t2.FStockID,t2.FStockPlaceID from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t2.FInterIDAssemble =  t1.FInterID where t1.FBillNo=@FBarCode_Assemble
         open My_cursor      
         fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty,@FDCStockID,@FDCSPID --�α�ͣ�ڵ�һ����¼ǰ�棬��һ��ִ�У�������û�м�¼����,
         while(@@FETCH_STATUS = 0) --ȡ���� 0��ʾ�ɹ�ִ��FETCH���  -1 ��ʾFETCH���ʧ�ܣ������ƶ���ָ��ʹ�䳬���˽���� -2 ��ʾ����ȡ���в����ڡ�
         begin  
	set @Fauxqty=-@Fauxqty
	select @FExchangeRate=isnull(FExchangeRate,1),@FSourceBillNo=FBillNo,@FSEOutBillNo=FBillNo from SEOutStock where FInterID=@FSourceInterId --���Ƶĵ��ݱ��
	set @FConsignPrice = @FConsignPrice * @FExchangeRate
	select @FAuxQtyMust = FAuxQty-FAuxCommitQty,@FOrderEntryID=FOrderEntryID,@FOrderInterID=FOrderInterID,@FOrderBillNo=FOrderBillNo,@FSEOutInterID=FSourceInterId,@FSEOutEntryID=FSourceEntryID from SEOutStockEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
	select @FCoefficient=isnull(FCoefficient,1) from t_MeasureUnit where FMeasureUnitID=@FUnitID --��λ������
	set @FQtyMust=@FAuxQtyMust*@FCoefficient --������λ�����յ����� 
	select @FPlanPrice=isnull(FPlanPrice,0)* @FExchangeRate from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --������λ���� 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --��λ�ƻ�����
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --�ƻ����۽�� 
	set @FConsignAmount=@Fauxqty*@FConsignPrice 
	set @FAuxQtyMust=-@FAuxQtyMust
		set @FQtyMust=-@FQtyMust
	
       if(@FKFDate is null or @FKFDate='')
		set @FPeriodDate=null
		else
		begin
			select @FPeriodDate = DATEADD(day,@FKFPeriod,@FKFDate) 
		end  
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
		end
		if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
         select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
           if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID)
		       begin
		         update ICStockBillEntry set FSecQty=isnull(FSecQty,0)+@FSecQty,FConsignAmount=isnull(FConsignAmount,0)+@FConsignAmount,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID
		       end
		        else
		        begin
 INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FOLOrderBillNo,
 FAuxPropID,FBatchNo,FQty,FUnitID,FAuxQtyMust,Fauxqty,FSecCoefficient,FSecQty,FAuxPlanPrice,
 FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FIsVMI,FEntrySupply,FDCStockID,
 FDCSPID,FConsignPrice,FDiscountRate,FConsignAmount,FDiscountAmount,FOrgBillEntryID,FSNListID,
 FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FContractBillNo,FContractInterID,
 FContractEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FAllHookQTY,FCurrentHookQTY,FQtyMust,
 FSepcialSaleId,FPlanMode,FMTONo,FClientOrderNo,FConfirmMemEntry,FClientEntryID,FChkPassItem,
 FSEOutBillNo,FSEOutEntryID,FSEOutInterID,FReturnNoticeBillNO,FReturnNoticeEntryID,FReturnNoticeInterID,FEntrySelfB0176)
  SELECT @FInterID,@FEntryID,'0','','',@FItemID,'',0,@FBatchNo,@FQty,@FUnitID,@FAuxQtyMust,@Fauxqty,
 @FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,0,0,'',@FKFDate,@FKFPeriod,@FPeriodDate,0,0,@FDCStockID,@FDCSPID,@FConsignPrice,0,
  @FConsignAmount,0,0,0,@FSourceBillNo,83,@FSourceInterId,@FSourceEntryID,'',0,0,@FOrderBillNo,@FOrderInterID,@FOrderEntryID,0,0,@FQtyMust,0,14036,'','','','0',1058,@FSEOutBillNo,@FSEOutEntryID,@FSEOutInterID,@FSourceBillNo,@FSourceEntryID,@FSourceInterId,@FEntrySelfB0176
 end
     fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty,@FDCStockID,@FDCSPID --�ٴν��α�ͣ�ڵ�һ����¼ǰ�棬��һ��ִ�У�������û�м�¼����,����Ҳ��Ϊ@@FETCH_STATUS��ֵ
				 end  
				 close my_cursor
				 deallocate my_cursor  
       end
       else  
       begin
      set @Fauxqty=-@Fauxqty
       	select @FExchangeRate=isnull(FExchangeRate,1),@FSourceBillNo=FBillNo,@FSEOutBillNo=FBillNo from SEOutStock where FInterID=@FSourceInterId --���Ƶĵ��ݱ��
	set @FConsignPrice = @FConsignPrice * @FExchangeRate
	select @FAuxQtyMust = FAuxQty-FAuxCommitQty,@FOrderEntryID=FOrderEntryID,@FOrderInterID=FOrderInterID,@FOrderBillNo=FOrderBillNo,@FSEOutInterID=FSourceInterId,@FSEOutEntryID=FSourceEntryID,@FSEOutBillNo=FSourceBillNo from SEOutStockEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
	set @FAuxQtyMust = -@FAuxQtyMust
	select @FCoefficient=isnull(FCoefficient,1) from t_MeasureUnit where FMeasureUnitID=@FUnitID --��λ������
	set @FQtyMust=@FAuxQtyMust*@FCoefficient --������λ�����յ����� 
	select @FPlanPrice=isnull(FPlanPrice,0)* @FExchangeRate from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --������λ���� 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --��λ�ƻ�����
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --�ƻ����۽�� 
	set @FConsignAmount=@Fauxqty*@FConsignPrice 
       if(@FKFDate is null or @FKFDate='')
		set @FPeriodDate=null
		else
		begin
			select @FPeriodDate = DATEADD(day,@FKFPeriod,@FKFDate) 
		end  
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
		end
		if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
         select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
           if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID)
		       begin
		         update ICStockBillEntry set FSecQty=isnull(FSecQty,0)+@FSecQty,FConsignAmount=isnull(FConsignAmount,0)+@FConsignAmount,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID
		       end
		        else
		        begin
 INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FOLOrderBillNo,
 FAuxPropID,FBatchNo,FQty,FUnitID,FAuxQtyMust,Fauxqty,FSecCoefficient,FSecQty,FAuxPlanPrice,
 FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FIsVMI,FEntrySupply,FDCStockID,
 FDCSPID,FConsignPrice,FDiscountRate,FConsignAmount,FDiscountAmount,FOrgBillEntryID,FSNListID,
 FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FContractBillNo,FContractInterID,
 FContractEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FAllHookQTY,FCurrentHookQTY,FQtyMust,
 FSepcialSaleId,FPlanMode,FMTONo,FClientOrderNo,FConfirmMemEntry,FClientEntryID,FChkPassItem,
 FSEOutBillNo,FSEOutEntryID,FSEOutInterID,FReturnNoticeBillNO,FReturnNoticeEntryID,FReturnNoticeInterID,FEntrySelfB0176)
  SELECT @FInterID,@FEntryID,'0','','',@FItemID,'',0,@FBatchNo,@FQty,@FUnitID,@FAuxQtyMust,@Fauxqty,
 @FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,0,0,'',@FKFDate,@FKFPeriod,@FPeriodDate,0,0,@FDCStockID,@FDCSPID,@FConsignPrice,0,
  @FConsignAmount,0,0,0,@FSourceBillNo,82,@FSourceInterId,@FSourceEntryID,'',0,0,@FOrderBillNo,@FOrderInterID,@FOrderEntryID,0,0,@FQtyMust,0,14036,'','','','0',1058,@FSEOutBillNo,@FSEOutEntryID,@FSEOutInterID,@FSourceBillNo,@FSourceEntryID,@FSourceInterId,@FEntrySelfB0176
 end
 end
end
set @detailqty=@detailqty+1
end 
EXEC p_UpdateBillRelateData 21,@FInterID,'ICStockBill','ICStockBillEntry' 
declare  
        @FHeadSelfB0155 varchar(255),-- �ұ�,
        @FHeadSelfB0157 varchar(255) -- �ͻ�����
              if exists(select 1 from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0 )
 begin
 select @FSManagerID=t2.FEmpID from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0
 end
  update ICStockBill set FEmpID=@FEmpID where FInterID=@FInterID 
 select @FSaleStyle=FSalType,@FDeptID=FDeptID,@FEmpID=FEmpID from  SEOutStock where FInterID=@FSourceInterId
 select @FHeadSelfB0157 = t2.FName from t_Organization t1 left join t_SubMessage t2 on t1.FTypeID = t2.FInterID where FItemID=@FSupplyID
update ICStockBill set  FHeadSelfB0157=@FHeadSelfB0157, @FDeptID=FDeptID,@FEmpID=FEmpID,FSManagerID=@FSManagerID  where FInterID=@FInterID

if exists(select 1 from t_PDABarCodeType where FType=1)
begin
--����
insert into t_PDABarCodeSign_Out(FInterID,FBarCode,FQty,FBillNo,FRemark,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before,FStatus,FDateUpLoad,FStockID,FStockPlaceID)select @FInterID,a.FBarCode,-a.FQty,@FBillNo,'�˻�֪ͨ���������۳���',b.FInterIDOut,b.FDateOutStore,b.FUserOutStore,1,convert(varchar(20),GETDATE(),20),b.FStockID,b.FStockPlaceID from a_DetailsTable  a inner join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID
update t_PDABarCodeSign  set FStockID=t.FStockID,FStockPlaceID=t.FStockPlaceID,FInterIDOut=@FInterID,FDateOutStore=@fdate,FIsOutStore='δ����',FUserOutStore=@FBillerID,FQtyOut =FQtyOut - t.FQty   from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
update t_PDABarCodeSign  set FInterIDDisassemble=FInterIDAssemble, FInterIDAssemble = 0 ,FIsOutStore='δ����'  from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID and a.FInterID=1) as t where t_PDABarCodeSign.FBarCode=t.FBarCode and t.FInterID=1
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
end

set nocount on
declare @fcheck_fail int
declare @fsrccommitfield_prevalue decimal(28,13)
declare @fsrccommitfield_endvalue decimal(28,13)
declare @maxorder int 
update src set @fsrccommitfield_prevalue= isnull(src.fbcommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue-dest.fqty,
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
     src.fbcommitqty=@fsrccommitfield_endvalue,
     src.fauxbcommitqty=@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)
 from seoutstockentry src 
     inner join seoutstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fsourceentryid
 inner join t_measureunit t1 on src.funitid=t1.fitemid
 UPDATE T1 SET T1.FStatus=
CASE WHEN NOT EXISTS(SELECT 1 FROM SEOutStockEntry WHERE FInterID=T1.FInterID AND FBCommitQty<>0) THEN 1 
WHEN NOT EXISTS (SELECT 1 FROM SEOutStockEntry WHERE FInterID=T1.FInterID AND FQty>FBCommitQty) THEN 3 
ELSE 2 END,
T1.FCLOSED=CASE WHEN NOT EXISTS (SELECT 1 FROM SEOutStockEntry WHERE FInterID=T1.FInterID AND FQty>FBCommitQty) THEN 1 ELSE 0 END 
FROM SEOutStock T1, ICStockBillEntry T2 
WHERE T1.FInterID = T2.FSourceInterID
 AND T2.FInterID=@FInterID
set nocount on
 
update src set @fsrccommitfield_prevalue= isnull(src.fseccommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue-dest.fsecqty,
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
     src.fseccommitqty=@fsrccommitfield_endvalue
 from seoutstockentry src 
     inner join seoutstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fsecqty) as fsecqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fsourceentryid

set nocount on
 
update src set @fsrccommitfield_prevalue= isnull(src.fstockbillqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fqty,
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
     src.fstockbillqty=@fsrccommitfield_endvalue,
     src.fauxstockbillqty=@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)
 from seoutstockentry src 
     inner join seoutstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fseoutinterid as fsourceinterid,u1.fseoutentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 group by u1.fseoutinterid,u1.fseoutentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fseoutentryid
 inner join t_measureunit t1 on src.funitid=t1.fitemid
 
update src set @fsrccommitfield_prevalue= isnull(src.fstockbillqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue-dest.fqty,
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
     src.fstockbillqty=@fsrccommitfield_endvalue,
     src.fauxstockbillqty=@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)
 from seoutstockentry src 
     inner join seoutstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.freturnnoticeinterid as fsourceinterid,u1.freturnnoticeentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 group by u1.freturnnoticeinterid,u1.freturnnoticeentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.freturnnoticeentryid
 inner join t_measureunit t1 on src.funitid=t1.fitemid
 
 IF EXISTS (SELECT 1 FROM ICBillRelations_Sale WHERE FBillType = 21 AND FBillID=@FInterID)
BEGIN
    UPDATE t1 SET t1.FChildren=t1.FChildren+1
    FROM SEOutStock t1 INNER JOIN SEOutStockEntry t2 ON     t1.FInterID=t2.FInterID
    INNER JOIN ICBillRelations_Sale t3 ON t3.FMultiEntryID=t2.FEntryID AND t3.FMultiInterID=t2.FInterID
    WHERE t3.FBillType=21 AND t3.FBillID=@FInterID
END
ELSE
BEGIN
    UPDATE t3 SET t3.FChildren=t3.FChildren+1
    FROM ICStockBill t1 INNER JOIN ICStockBillEntry     t2 ON t1.FInterID=t2.FInterID
    INNER JOIN SEOutStock t3 ON t3.FTranType=t2.FSourceTranType AND t3.FInterID=t2.FSourceInterID
    WHERE t1.FTranType=21 AND t1.FInterID=@FInterID AND t2.FSourceInterID>0
END

update t1 set FcmtQty_O=FcmtQty_O from ExpOutReqEntry t1  inner join (  select sum(t1.FQty) FQty,t3.fdetailid  from ICStockBillEntry t1  inner join ExpOutReqEntry t2 on t2.fdetailid=t1.fsourceEntryid  inner join ExpOutReqEntry t3 on t3.fdetailid=t2.fentryid_src  where fsourceinterid>0 and fsourcebillno<>'' and fsourcetrantype=1007131 and t1.finterid=@FInterID group by t3.fdetailid) t2  on t1.fdetailid=t2.fdetailid

    UPDATE A SET A.FCommitQty=A.FCommitQty-D.FQty,A.FAuxCommitQty=A.FAuxCommitQty-(D.FQty/T.FCoefficient),
    A.FSecCommitQty=A.FSecCommitQty -D.FSecQty 
    FROM ICStockBillEntry A
    INNER JOIN ICWebReturnEntry B ON B.FID_SRC=A.FInterID AND B.FEntryID_SRC=A.FDetailID AND B.FClassID_SRC=1007572
    INNER JOIN SEOutStockEntry C ON C.FSourceInterId=B.FID AND C.FSourceEntryID=B.FEntryID 
    INNER JOIN ICStockBillEntry D ON D.FSourceInterId =C.FInterID AND D.FSourceEntryID =C.FEntryID AND D.FSourceTranType =82
    LEFT JOIN t_MeasureUnit T ON A.FUnitID=T.FMeasureUnitID 
    WHERE D.FInterID=@FInterID

 
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
