if (exists (select * from sys.objects where name = 'proc_OtherIn'))
    drop proc proc_OtherIn
go
create proc proc_OtherIn
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
        @FCheckDate varchar(50),  --������� 
        @FFManagerID varchar(20), --����
        @FSManagerID varchar(20), --����
        @FManagerID varchar(20),  --����id
        @FDeptID varchar(20),     --����id
        @FEmpID varchar(20),      --ҵ��Աid
        @FBillerID varchar(20),   --�Ƶ���id 
        @FSettleDate varchar(50), --��������
        @FExplanation varchar(200),--ժҪ 
               @FOrderID varchar(50),--PDA���ݱ��
        @FPDAID varchar(50),  --PDA���к�
        @FBillTypeID varchar(20),   --�������ID
        @FHeadSelfA9740 varchar(20),--�Ƿ���Ʒ
        @FSelTranType varchar(20)  --Դ������
set @FBillerID = dbo.getString(@mainStr,'|',1) --����Ա  
set @Fdate =dbo.getString(@mainStr,'|',2)      --���� 
set @FDeptID =dbo.getString(@mainStr,'|',3)   --����id
set @FEmpID =dbo.getString(@mainStr,'|',4)    --ҵ��Աid
set @FManagerID =dbo.getString(@mainStr,'|',5) --������ID
set @FSupplyID =dbo.getString(@mainStr,'|',6)   --��Ӧ��ID
set @FExplanation =dbo.getString(@mainStr,'|',7)--ժҪ
set @FFManagerID=dbo.getString(@mainStr,'|',8) --����
set @FSManagerID=dbo.getString(@mainStr,'|',9) --����
set @FROB=dbo.getString(@mainStr,'|',10)         --���ֺ���
set @FBillTypeID=dbo.getString(@mainStr,'|',11) --�������ID
set @FSelTranType=dbo.getString(@mainStr,'|',12)  --Դ������ 
if exists(select 1 from t_PDABarCodeType where FType=1)
begin
 set @FOrderID=dbo.getString(@mainStr,'|',13) --PDA���ݱ��   
set @FPDAID=dbo.getString(@mainStr,'|',14) --PDA���к� 
end
set @FHeadSelfA9740=dbo.getString(@mainStr,'|',15)  --�Ƿ���Ʒ 

set @Fdate = convert(varchar(20),GETDATE(),23)
exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --�õ�@FInterID
------------------------------------------------------------�õ����
if(@FROB='����')
set @FROB=-1
else
set @FROB=1
set @FBillNo = ''
 exec proc_GetICBillNo 10, @FBillNo out 
-----------------------------------------------------------�õ����
set @FCurrencyID=1 --�ұ�
set @FCheckDate=null --���ʱ��  
  declare @IsExist varchar(10), --�Ƿ����
            @value varchar(10)--����������
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FROB,FHookStatus,Fdate,
FSupplyID,FCheckDate,FFManagerID,FSManagerID,FBillerID,FDeptID,FBillTypeID,FMultiCheckDate1,FMultiCheckDate2,
FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FVchInterID,FMultiCheckDate6,FPOOrdBillNo,FRelateBrID,FSelTranType,
FBrID,FManagerID,FEmpID,FExplanation,FManageType,FPrintCount,FHeadSelfA9740,FHeadSelfA9739) 
SELECT @FInterID,@FBillNo,'0',10,0,0,@value,@FROB,0,@Fdate,@FSupplyID,@FCheckDate,@FFManagerID,@FSManagerID,@FBillerID,@FDeptID,@FBillTypeID,Null,Null,Null,Null,Null,0,Null,'',0,@FSelTranType,0,@FManagerID,@FEmpID,@FExplanation,0,0,@FHeadSelfA9740,CONVERT(varchar(128),GETDATE(),20)
update ICStockBill set FUUID=newid() where FInterID=@FInterID

declare @FEntryID varchar(20),       --��ϸ���
        @FItemID varchar(20),        --��Ʒid
        @FQty float,                --������λ����
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
        @FCoefficient varchar(20),   --������
        @FBatchNo varchar(50),     --����
        @FDCSPID varchar (20),     --��λID
          @FSecCoefficient float, --������λ������
        @FSecQty decimal(28,10),   --������λ����
          @FSecUnitID  varchar(50), 
            @FKFDate varchar(50),    --��������
        @FBarCode_Assemble varchar(128),--װ������
          @FBarCode varchar(128),
        @FKFPeriod int,       --������
        @FPeriodDate varchar(50),--��Ч��
        @detailqty int,               --��ϸ�����ĸ���
        @detailcount int,             --��ϸÿ�����ݵĳ��� 
        @detailIndex int,            --��ϸÿ���±�
        @countindex int              --�ָ���|������
       set @detailqty=0        
       set @detailcount=11           
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
	set @Fauxqty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)       --����
	set @FDiscountRate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5)  --�ۿ��� 
	set @FDCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6)     --�ֿ�id
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7)       --����
	set @FDCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8)        --��λID
		set @FKFDate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)  
	set @FKFPeriod=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+10)
	set @FBarCode_Assemble =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+11)--װ������ �������װ������ �õط�Ϊ''
	set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex 
		 if(SUBSTRING(@FBarCode_Assemble,1,2)='ZZ')
       begin       
         declare My_cursor cursor dynamic --scroll��ʾ������ǰ������ƶ�    dynamic����ʾ��дҲ�ɶ�
         for 
         select  t2.FBarCode,t2.FItemID,t2.FBatchNo,t2.FKFDate,t2.FKFPeriod,t2.FQty from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t2.FInterIDAssemble =  t1.FInterID where t1.FBillNo=@FBarCode_Assemble
         open My_cursor      
         fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty --�α�ͣ�ڵ�һ����¼ǰ�棬��һ��ִ�У�������û�м�¼����,
         while(@@FETCH_STATUS = 0) --ȡ���� 0��ʾ�ɹ�ִ��FETCH���  -1 ��ʾFETCH���ʧ�ܣ������ƶ���ָ��ʹ�䳬���˽���� -2 ��ʾ����ȡ���в����ڡ�
         begin  
	 if(@FROB=-1)
         begin
         set @Fauxqty=-@Fauxqty
         end
          if(@FKFDate is null or @FKFDate='')
		set @FPeriodDate=null
		else
		begin
			select @FPeriodDate = DATEADD(day,@FKFPeriod,@FKFDate) 
		end  
	select @FCoefficient=isnull(FCoefficient,1) from t_MeasureUnit where FMeasureUnitID=@FUnitID --��λ������
	select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --������λ���� 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --��λ�ƻ�����
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --�ƻ����۽�� 
	set @Famount=@Fauxqty*@Fauxprice
	 
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
			SELECT '0',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,@FKFPeriod,@FKFDate,@FQty,@FSecQty
			end
		  else
			begin
			update ICInventory set FQty=FQty+@FQty,FSecQty=FSecQty+@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID  and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  end    
		end
			if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
            select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
             if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID)
		       begin
		         update ICStockBillEntry set FSecQty=isnull(FSecQty,0)+@FSecQty,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FAmount=ISNULL(FAmount,0)+@Famount where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID
		       end
		        else
		        begin
INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FAuxPropID,FBatchNo,FQtyMust,FQty,FUnitID,FAuxQtyMust,Fauxqty,FSecCoefficient,FSecQty,
FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FDCStockID,FDCSPID,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,
FSourceEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FCostPercentage,FDeliveryNoticeFID,FDeliveryNoticeEntryID,FChkPassItem)  
SELECT @FInterID,@FEntryID,'0','','',@FItemID,0,@FBatchNo,0,@FQty,@FUnitID,0,@Fauxqty,@FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,@Fauxprice,@Famount,'',@FKFDate,@FKFPeriod,@FPeriodDate,@FDCStockID,@FDCSPID,0,'',0,0,0,'',0,0,14036,'',0,0,0,1058 
end
 	 fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty --�ٴν��α�ͣ�ڵ�һ����¼ǰ�棬��һ��ִ�У�������û�м�¼����,����Ҳ��Ϊ@@FETCH_STATUS��ֵ
				 end  
				 close my_cursor
				 deallocate my_cursor  
       end  
     else
     begin
               if(@FKFDate is null or @FKFDate='')
		set @FPeriodDate=null
		else
		begin
			select @FPeriodDate = DATEADD(day,@FKFPeriod,@FKFDate) 
		end  
	select @FCoefficient=isnull(FCoefficient,1) from t_MeasureUnit where FMeasureUnitID=@FUnitID --��λ������
	select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --������λ���� 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --��λ�ƻ�����
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --�ƻ����۽�� 
	set @Famount=@Fauxqty*@Fauxprice
	 
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
			SELECT '0',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,@FKFPeriod,@FKFDate,@FQty,@FSecQty
			end
		  else
			begin
			update ICInventory set FQty=FQty+@FQty,FSecQty=FSecQty+@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID  and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  end    
		end
			if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
            select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
             if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID)
		       begin
		         update ICStockBillEntry set FSecQty=isnull(FSecQty,0)+@FSecQty,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FAmount=ISNULL(FAmount,0)+@Famount where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID
		       end
		        else
		        begin
INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FAuxPropID,FBatchNo,FQtyMust,FQty,FUnitID,FAuxQtyMust,Fauxqty,FSecCoefficient,FSecQty,
FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FDCStockID,FDCSPID,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,
FSourceEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FCostPercentage,FDeliveryNoticeFID,FDeliveryNoticeEntryID,FChkPassItem)  
SELECT @FInterID,@FEntryID,'0','','',@FItemID,0,@FBatchNo,0,@FQty,@FUnitID,0,@Fauxqty,@FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,@Fauxprice,@Famount,'',@FKFDate,@FKFPeriod,@FPeriodDate,@FDCStockID,@FDCSPID,0,'',0,0,0,'',0,0,14036,'',0,0,0,1058 
end
end
end
set @detailqty=@detailqty+1
end
EXEC p_UpdateBillRelateData 10,@FInterID,'ICStockBill','ICStockBillEntry' 
  if exists(select 1 from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0 )
 begin
 select @FSManagerID=isnull(t2.FEmpID,0) from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0
  if(CONVERT(int, @FSManagerID)=0)
  begin
      if exists(select 1 from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID)
   begin
  ---������
  select @FSManagerID = t2.FItemID from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID
  end
  end
 end
 if(CONVERT(int, @FSManagerID)>0)
 begin
 update ICStockBill set FSManagerID=@FSManagerID,FFManagerID=@FSManagerID where FInterID=@FInterID
 end

if exists(select 1 from t_PDABarCodeType where FType=1)
begin
insert into t_PDABarCodeSign_In(FInterID,FBarCode,FQty,FBillNo,FRemark,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before,FStatus,FDateUpLoad,FStockID,FStockPlaceID)select @FInterID,a.FBarCode,a.FQty,@FBillNo,'������ⵥ',b.FInterIDIn,b.FDateInStore,b.FUserInStore,1,convert(varchar(20),GETDATE(),20),a.FStockID,a.FStockPlaceID from a_DetailsTable  a inner join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID

update t_PDABarCodeSign  set FStockID=t.FStockID,FStockPlaceID=t.FStockPlaceID,FInterIDIn=@FInterID,FDateInStore=@fdate,FIsInStore='�����',FUserInStore=@FBillerID,FRemark1='������ⵥ',FDateUpLoad = convert(varchar(20),GETDATE(),20)  from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
end
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
