if (exists (select * from sys.objects where name = 'proc_Allot'))
    drop proc proc_Allot
go
create proc proc_Allot
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
        @Fdate varchar(50),       --����       
        @FCheckDate varchar(50),  --�������
        @FMangerID varchar(20),   --����id
        @FDeptID varchar(20),     --����id
        @FEmpID varchar(20),      --ҵ��Աid
        @FBillerID varchar(20),   --�Ƶ���id
        @FFManagerID varchar(20), --����
        @FHeadSelfD0135 varchar(20), --�������,
        @FHeadSelfD0136 varchar(20),-- �Ƿ���Ʒ
         @FOrderID varchar(50),--PDA���ݱ��
        @FPDAID varchar(50),  --PDA���к� 
        @FSManagerID varchar(20)  --����  
set @FBillerID = dbo.getString(@mainStr,'|',1) --����Ա
set @Fdate =dbo.getString(@mainStr,'|',2)      --����  
set @FDeptID =dbo.getString(@mainStr,'|',3)   --����id
set @FEmpID =dbo.getString(@mainStr,'|',4)    --ҵ��Աid
set @FSManagerID =dbo.getString(@mainStr,'|',5) --����id   
set @FFManagerID =dbo.getString(@mainStr,'|',6) --����id
if exists(select 1 from t_PDABarCodeType where FType=1)
begin
set @FOrderID=dbo.getString(@mainStr,'|',7) --PDA���ݱ��   
set @FPDAID=dbo.getString(@mainStr,'|',8) --PDA���к� 
end
set @FHeadSelfD0135=dbo.getString(@mainStr,'|',9) --�������ID��Ҳ������е�FInterID��
set @FHeadSelfD0136=dbo.getString(@mainStr,'|',10) --�Ƿ���ƷID��Ҳ������е�FInterID��
set @FDeptID= 0
set @FEmpID= 0
set @Fdate = convert(varchar(20),GETDATE(),23)
exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --�õ�@FInterID
------------------------------------------------------------�õ����
 set @FBillNo ='' 
 exec proc_GetICBillNo 41, @FBillNo out 
-----------------------------------------------------------�õ���� 
 
  declare @IsExist varchar(10), --�Ƿ����
            @value varchar(10)--����������
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
set @FCheckDate=null --���ʱ�� 
--    if exists(select 1 from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID)
--begin
--  ---������
--  select @FFManagerID = t2.FItemID from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID
--end
 
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FHookStatus,Fdate,FCheckDate,FFManagerID,FSManagerID,
FBillerID,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FMultiCheckDate6,FSelTranType,FBrID,FDeptID,FEmpID,FRefType,
FPrintCount,FHeadSelfD0134,FHeadSelfD0135,FHeadSelfD0136,FHeadSelfD0137,FHeadSelfD0138,FHeadSelfD0139,FHeadSelfD0140,FHeadSelfD0141,FHeadSelfD0142,FHeadSelfD0143) 
SELECT @FInterID,@FBillNo,'0',41,0,0,0,0,@Fdate,Null,@FFManagerID,@FSManagerID,@FBillerID,Null,Null,Null,Null,Null,Null,85,0,0,0,12561,0,'',@FHeadSelfD0135,@FHeadSelfD0136,'',Null,'',Null,'','','PDA�Ƶ�'

 UPDATE ICStockBill SET FUUID=NEWID() WHERE FInterID=@FInterID
declare @FEntryID varchar(20),       --��ϸ���
        @FItemID varchar(20),        --��Ʒid
        @FQty float,                --������λ����
        @FUnitID varchar(20),       --��λid
        @Fauxqty float,             --�ϴ���λ���� 
        @FSCStockID varchar(20),    --����ֿ�id
        @FSCSPID varchar(20),       --�����λid
        @FDCStockID varchar(20),    --���ֿ�id
        @FDCSPID  varchar(20),      --����λid
        @FBatchNo varchar(50),      --����
        @FCoefficient float(50),  --��λ����
        @FPlanPrice float(50),    --�����ƻ�����
        @FAuxPlanPrice float(50), --��λ�ƻ�����
        @FPlanAmount float(50),   --�ƻ����۽��
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
	set @Fauxqty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)    --����
	set @FDCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4) --���id
	set @FSCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5) --����id
	set @FDCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6) --����λ
	set @FSCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7) --�����λ
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8) --�����λ
		set @FKFDate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)  
	set @FKFPeriod=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+10)
	set @FBarCode_Assemble =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+11)--װ������ �������װ������ �õط�Ϊ''
	set @detailIndex=@detailIndex+1
		   if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode_Assemble)
       begin       
         declare My_cursor cursor dynamic --scroll��ʾ������ǰ������ƶ�    dynamic����ʾ��дҲ�ɶ�
         for 
         select  t2.FBarCode,t2.FItemID,t2.FBatchNo,t2.FKFDate,t2.FKFPeriod,t2.FQty,t2.FStockID,t2.FStockPlaceID from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t2.FInterIDAssemble =  t1.FInterID where t1.FBillNo=@FBarCode_Assemble
         open My_cursor      
         fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty,@FSCStockID,@FSCSPID --�α�ͣ�ڵ�һ����¼ǰ�棬��һ��ִ�У�������û�м�¼����,
         while(@@FETCH_STATUS = 0) --ȡ���� 0��ʾ�ɹ�ִ��FETCH���  -1 ��ʾFETCH���ʧ�ܣ������ƶ���ָ��ʹ�䳬���˽���� -2 ��ʾ����ȡ���в����ڡ�
         begin  
	 select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --��λ������
	 select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	 set @FQty=@Fauxqty*@FCoefficient  
	 set @FAuxPlanPrice = @FPlanPrice*@FCoefficient
	 set @FPlanAmount = @FAuxPlanPrice*@Fauxqty	
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
    --���
      select @IsExist=COUNT(1) from ICInventory where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID
      if(@IsExist=0)
        begin
        INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
        SELECT '',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,0,'',@FQty,@FSecQty
        end
      else
        begin
        update ICInventory set FQty=FQty+@FQty,FSecQty=FSecQty+@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID 
      end   
      --����
       select @IsExist=COUNT(1) from ICInventory where FStockID=@FSCStockID and FStockPlaceID=@FSCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID
      if(@IsExist=0)
        begin
        INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
        SELECT '',@FItemID,@FBatchNo,'',0,@FSCStockID,@FSCSPID,0,'',-@FQty,-@FSecQty
        end
      else
        begin
        update ICInventory set FQty=FQty-@FQty,FSecQty=FSecQty-@FSecQty where FStockID=@FSCStockID and FStockPlaceID=@FSCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID 
      end     
    end 
    if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
         select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
          if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSCStockID=@FSCStockID and FSCSPID=@FSCSPID)
		       begin
		         update ICStockBillEntry set FSecQty=FSecQty+@FSecQty,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FAmount=ISNULL(FAmount,0) where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSCStockID=@FSCStockID and FSCSPID=@FSCSPID
		       end
		        else
		        begin
 INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FItemID,FAuxPropID,FBatchNo,FQty,FDefaultBaseQty,FRealStockBaseQty,FUnitID,FDefaultQty,FRealStockQty,
 Fauxqty,FSecCoefficient,FSecQty,FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,FAuxPriceRef,FAmtRef,Fnote,FKFDate,FKFPeriod,FPeriodDate,FIsVMI,FEntrySupply,
 FSCStockID,FSCSPID,FDCStockID,FDCSPID,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FICMOBillNo,FICMOInterID,FPPBomEntryID,
 FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FChkPassItem,FContractBillNO,FContractEntryID,FContractInterID,FLockFlag,FClientEntryID,FClientOrderNo) 
  SELECT @FInterID,@FEntryID,'0',@FItemID,0,@FBatchNo,@FQty,0,0,@FUnitID,0,0,@Fauxqty,@FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,0,0,0,0,'',@FKFDate,@FKFPeriod,@FPeriodDate,0,0,@FSCStockID,@FSCSPID,@FDCStockID,@FDCSPID,0,'',0,0,0,'',0,0,'',0,0,14036,'',1058,'',0,0,0,'','' 
                end
    fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty,@FSCStockID,@FSCSPID --�ٴν��α�ͣ�ڵ�һ����¼ǰ�棬��һ��ִ�У�������û�м�¼����,����Ҳ��Ϊ@@FETCH_STATUS��ֵ
				 end  
				 close my_cursor
				 deallocate my_cursor  
       end
       else  
       begin
       	 select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --��λ������
	 select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	 set @FQty=@Fauxqty*@FCoefficient  
	 set @FAuxPlanPrice = @FPlanPrice*@FCoefficient
	 set @FPlanAmount = @FAuxPlanPrice*@Fauxqty	
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
    --���
      select @IsExist=COUNT(1) from ICInventory where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID
      if(@IsExist=0)
        begin
        INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
        SELECT '',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,0,'',@FQty,@FSecQty
        end
      else
        begin
        update ICInventory set FQty=FQty+@FQty,FSecQty=FSecQty+@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID 
      end   
      --����
       select @IsExist=COUNT(1) from ICInventory where FStockID=@FSCStockID and FStockPlaceID=@FSCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID
      if(@IsExist=0)
        begin
        INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
        SELECT '',@FItemID,@FBatchNo,'',0,@FSCStockID,@FSCSPID,0,'',-@FQty,-@FSecQty
        end
      else
        begin
        update ICInventory set FQty=FQty-@FQty,FSecQty=FSecQty-@FSecQty where FStockID=@FSCStockID and FStockPlaceID=@FSCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID 
      end     
    end 
    if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
         select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
          if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSCStockID=@FSCStockID and FSCSPID=@FSCSPID)
		       begin
		         update ICStockBillEntry set FSecQty=FSecQty+@FSecQty,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FAmount=ISNULL(FAmount,0) where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSCStockID=@FSCStockID and FSCSPID=@FSCSPID
		       end
		        else
		        begin
 INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FItemID,FAuxPropID,FBatchNo,FQty,FDefaultBaseQty,FRealStockBaseQty,FUnitID,FDefaultQty,FRealStockQty,
 Fauxqty,FSecCoefficient,FSecQty,FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,FAuxPriceRef,FAmtRef,Fnote,FKFDate,FKFPeriod,FPeriodDate,FIsVMI,FEntrySupply,
 FSCStockID,FSCSPID,FDCStockID,FDCSPID,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FICMOBillNo,FICMOInterID,FPPBomEntryID,
 FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FChkPassItem,FContractBillNO,FContractEntryID,FContractInterID,FLockFlag,FClientEntryID,FClientOrderNo) 
  SELECT @FInterID,@FEntryID,'0',@FItemID,0,@FBatchNo,@FQty,0,0,@FUnitID,0,0,@Fauxqty,@FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,0,0,0,0,'',@FKFDate,@FKFPeriod,@FPeriodDate,0,0,@FSCStockID,@FSCSPID,@FDCStockID,@FDCSPID,0,'',0,0,0,'',0,0,'',0,0,14036,'',1058,'',0,0,0,'','' 
                end
       end
end
set @detailqty=@detailqty+1
end
EXEC p_UpdateBillRelateData 41,@FInterID,'ICStockBill','ICStockBillEntry' 
  if exists(select 1 from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0 )
 begin
 select @FSManagerID=isnull(t2.FEmpID,0) from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0
 end
 if(CONVERT(int, @FSManagerID)>0)
 begin
 update ICStockBill set FSManagerID=@FSManagerID,FFManagerID=@FSManagerID where FInterID=@FInterID
 end
if exists(select 1 from t_PDABarCodeType where FType=1)
begin
insert into t_PDABarCodeSign_Allot(FInterID,FBarCode,FQty,FBillNo,FRemark,FStockID_Before,FStockPlaceID_Before,FStockID_Now,FStockPlaceID_Now,FStatus,FInterID_Before,FBillNo_Before,FDateUpLoad)
select @FInterID,b.FBarCode,a.FQty,@FBillNo,'������',b.FStockID,b.FStockPlaceID,a.FStockID,a.FStockPlaceID,1,b.FInterIDAllot,b.FAllotBillNo,convert(varchar(20),GETDATE(),20) from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID
update t_PDABarCodeSign  set FStockID=t.FStockID,FStockPlaceID=t.FStockPlaceID,FInterIDAllot=@FInterID,FAllotBillNo=@FBillNo  from (select a.*,b.FStockID as AAA,b.FStockPlaceID as BBB from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
update ICStockBill set FMultiCheckStatus = 2,FUUID=NEWID()  where FInterID=@FInterID
UPDATE ICStockBillEntry SET FAuxprice=FAuxPriceRef,FAmount=FAmtRef  FROM ICStockBillEntry u1 INNER JOIN ICStockBill v1 ON u1.FInterID=v1.FInterID  AND v1.FTranType=41 AND v1.FInterID= 55773 WHERE (v1.FRefType<>12562 or v1.FRefType is null)  
UPDATE POInstock SET FMultiCheckStatus=16
 WHERE FInterID = @FInterID
 AND FTranType=73 
 UPDATE POInstock SET FCheckerID=16456
 WHERE FInterID = @FInterID
 AND FTranType=73
update icclassmctemplatemap set ftemplateid=70 where fclasstypeid=1073 and fbillid=@FInterID
UPDATE ICClassMCTaskCenter SET FStatus=1 ,FProcessUserID=16456,FUpdateDate=getdate()
 WHERE FClassTypeID=1073 AND FBillID=@FInterID AND FStatus=0 AND (FTaskType=0 OR FTaskType=2) AND FNextLevelTag =2000
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
