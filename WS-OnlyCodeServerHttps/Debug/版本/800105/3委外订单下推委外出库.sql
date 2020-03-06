if (exists (select * from sys.objects where name = 'proc_PushOutsourceOut'))
    drop proc proc_PushOutsourceOut
go
create proc proc_PushOutsourceOut
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
        @FCurrencyID varchar(20),--�ұ�id 
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
        @FCussentAcctID varchar(20), --������ĿID
        @FPurposeID varchar(20), --ί������
          @FOrderID varchar(50),--PDA���ݱ�� 
        @FPDAID varchar(50),  --PDA���к�
        @FSelTranType varchar(20)  --Դ������
set @FBillerID = dbo.getString(@mainStr,'|',1) --����Ա  
set @Fdate =dbo.getString(@mainStr,'|',2)      --���� 
set @FSupplyID =dbo.getString(@mainStr,'|',3)   --������λ
--set @FExplanation =dbo.getString(@mainStr,'|',10)--ժҪ
set @FFManagerID=dbo.getString(@mainStr,'|',4) --����
set @FSManagerID=dbo.getString(@mainStr,'|',5) --����
set @FROB=dbo.getString(@mainStr,'|',6)         --���ֺ���
set @FPurposeID=dbo.getString(@mainStr,'|',7)  --ί������id  
set @FExplanation =dbo.getString(@mainStr,'|',8)--ժҪ
  set @FOrderID=dbo.getString(@mainStr,'|',9) --PDA���ݱ��   
set @FPDAID=dbo.getString(@mainStr,'|',10) --PDA���к�
exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --�õ�@FInterID
------------------------------------------------------------�õ����
    set @FBillNo ='' 
 exec proc_GetICBillNo 28, @FBillNo out 
-----------------------------------------------------------�õ����
set @FCurrencyID=1 --�ұ�
set @FCheckDate=null --���ʱ��  
set @FExplanation='' --��ע

  declare @IsExist varchar(10), --�Ƿ����
            @value varchar(10)--����������
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FVchInterID,FROB,FHookStatus,Fdate,FSupplyID,FPurposeID,
Fnote,FCheckDate,FSManagerID,FFManagerID,FBillerID,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FMultiCheckDate6,
FSelTranType,FBackFlushed,FManageType,FPrintCount,FRelateBrID,FPOOrdBillNo,FBrID) 
VALUES (@FInterID,@FBillNo,'0',28,0,0,@value,0,1,0,@Fdate,@FSupplyID,@FPurposeID,'',@FCheckDate,@FSManagerID,@FFManagerID,@FBillerID,Null,Null,Null,Null,Null,Null,1007105,0,0,0,0,'',0)
update ICStockBill set FUUID=newid() where FInterID=@FInterID

declare @FEntryID varchar(20),       --�µ���ϸ���
        @FSourceEntryID varchar(20), --���Ƶ��ݵ���ϸid
        @FSourceInterId varchar(20), --���Ƶ��ݵ�FInterID
        @FSourceBillNo varchar(20),  --���Ƶĵ��ݵĵ��ݱ��
        @FOrderBillNo varchar(20),
        @FOrderEntryID varchar(20),
        @FOrderInterID varchar(20),
        @FICMOInterID varchar(20),
        @FPPBomEntryID varchar(20),
        @FItemID varchar(20),        --��Ʒid
        @FQty float,                --������λ����
        @FQtyMust float,            --������λ��������
        @FAuxQtyMust float,        --��λ����������
        @FUnitID varchar(20),       --��λid
        @Fauxqty float,            --�ϴ������� 
        @Fauxprice float,          -- ����
        @Famount float,          --��� 
          @FTaxRate float,        --˰��
        @FTaxAmount float,     -- ˰��
        @FProcessPrice float, --��λ�ɱ�
        @FProcessCost float,  --��λ�ɱ����
        
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
          @FComplexQty varchar(128),--����+��λ���
          @FUnitName varchar(128),--��λ����
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
	select @FSourceBillNo=FBillNo,@FSupplyID = FSupplyID from ICSubContract where FInterID=@FSourceInterId --���Ƶĵ��ݱ��
	select @FAuxQtyMust = FAuxQty-FAuxCommitQty from ICSubContractEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
	  select  @FICMOInterID=FInterID, @FPPBomEntryID=FEntryID   from PPBOMEntry where FICMoInterID  =  @FSourceInterId and FOrderEntryID=@FSourceEntryID and FItemID = @FItemID
	     
	 
		set @FOrderBillNo = @FSourceBillNo
	set @FOrderEntryID = @FSourceEntryID
	set @FOrderInterID = @FSourceInterId
		select @FCoefficient=FCoefficient,@FUnitName=FName from t_MeasureUnit where FMeasureUnitID=@FUnitID --��λ������
	set @FQtyMust=@FAuxQtyMust*@FCoefficient --������λ�����յ�����  
	 select @FPlanPrice=isnull(FPlanPrice,0),@FTaxRate=isnull(FTaxRate,0) from t_ICItem where   FItemID=@FItemID  
	
	 set   @FProcessPrice = @Fauxprice*@FCoefficient
	 set  @FProcessCost = @FProcessPrice*@Fauxqty
	set @FQty=@Fauxqty*@FCoefficient                  --������λ���� 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --��λ�ƻ�����
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --�ƻ����۽�� 
	set @Famount=@Fauxqty*@Fauxprice 
	set @FTaxAmount = @Famount*(1+@FTaxRate)
	set @FComplexQty = CONVERT (varchar(50),@Fauxqty)+ @FUnitName
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
			SELECT '0',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,0,'',-@FQty,-@FSecQty
			end
		  else
			begin
			update ICInventory set FQty=FQty-@FQty,FSecQty=FSecQty-@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID 
		  end    
		end
INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FAuxPropID,FBatchNo,FQty,FQtyMust,FUnitID,FAuxQtyMust,Fauxqty,
FSecCoefficient,FSecQty,FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FDCStockID,FDCSPID,FSNListID,FSourceBillNo,FSourceTranType,
FSourceInterId,FSourceEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FICMOInterID,FPPBomEntryID,FInStockID,FPlanMode,FMTONo) 
VALUES (@FInterID,@FEntryID,'0','','',@FItemID,0,@FBatchNo,@FQty,@FQtyMust,@FUnitID,@FAuxQtyMust,@Fauxqty,@FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,@Fauxprice,@Famount,
'',Null,0,Null,@FDCStockID,@FDCSPID,0,@FSourceBillNo,1007105,@FSourceInterId,@FSourceEntryID,@FOrderBillNo,@FOrderInterID,@FOrderEntryID,@FICMOInterID,@FPPBomEntryID,0,14036,'') 
end
set @detailqty=@detailqty+1
end
	--goto error1
EXEC p_UpdateBillRelateData 28,@FInterID,'ICStockBill','ICStockBillEntry' 
 update ICStockBill set FSupplyID =@FSupplyID where FInterID=@FInterID
 delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
set nocount on
declare @fcheck_fail int
declare @fsrccommitfield_prevalue decimal(28,13)
declare @fsrccommitfield_endvalue decimal(28,13)
declare @maxorder int 
update src set @fsrccommitfield_prevalue= isnull(src.fqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+(case src.fmaterieltype when 376 then dest.fqty*cast(-1 as float) else dest.fqty end),
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (src.fstockqty+dest.fqty>=src.fdiscardqty) then @fcheck_fail else -1 end) end,
     src.fqty=@fsrccommitfield_endvalue,
     src.fauxqty=@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)
 from ppbomentry src 
     inner join 
 (select u1.ficmointerid as fsourceinterid,u1.fppbomentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 and u1.ficmointerid>0 group by u1.ficmointerid,u1.fppbomentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fppbomentryid
 inner join t_measureunit t1 on src.funitid=t1.fitemid
select @fcheck_fail=0
select @fcheck_fail=-1 from ppbomentry src
inner join (select u1.ficmointerid as fsourceinterid,u1.fppbomentryid,u1.fitemid,sum(u1.fqty) as fqty from icstockbillentry u1  where u1.finterid=@FInterID and u1.ficmointerid> 0 group by u1.ficmointerid,u1.fppbomentryid,u1.fitemid) dest
on dest.fsourceinterid = src.finterid and dest.fitemid = src.fitemid and src.fentryid = dest.fppbomentryid 
where src.fstockqty +dest.fqty -src.fdiscardqty<0 and src.fmaterieltype<>376
if @@rowcount>0 select @fcheck_fail=-1

if (select cast(isnull(fvalue,0) as int) from t_systemprofile where fcategory='sh' and fkey='itemscrap_controlqty')=1 
if (isnull(@fcheck_fail,0)=-1) 
begin
   raiserror('ί��ӹ����ⵥ�����������ܴ��ڶ�ӦͶ�ϵ��������ϵ��������뱨����֮�',18,18)
   goto error1
end
else
if exists (select 1 from ppbom src right join  (select u1.ficmointerid as fsourceinterid,u1.fppbomentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 and u1.ficmointerid>0 group by u1.ficmointerid,u1.fppbomentryid,u1.fitemid) dest on dest.fsourceinterid = src.finterid where dest.fsourceinterid>0 and src.finterid is null)
begin
raiserror('��ѡ�����ѱ�ɾ��',18,18)
goto error1
end
 IF EXISTS ( SELECT 1 FROM syscolumns WHERE name = 'FConnectFlag'  AND id = object_id('ICSubContract') )  AND EXISTS ( SELECT 1 FROM syscolumns WHERE name='FSourceInterID'  AND id = object_id('ICStockBillEntry') ) EXEC('UPDATE t3 SET t3.FConnectFlag=t3.FConnectFlag+1
FROM ICStockBill t1 INNER JOIN ICStockBillEntry t2 ON t1.FInterID=t2.FInterID
INNER JOIN ICSubContract t3 ON t3.FClassTypeID=t2.FSourceTranType AND t3.FInterID=t2.FSourceInterID
WHERE t1.FTranType=28 AND t1.FInterID='+@FInterID+' AND t2.FSourceInterID>0 ' )
 ELSE IF EXISTS ( SELECT 1 FROM syscolumns WHERE name = 'FChildren'  AND id = object_id('ICSubContract') )  AND EXISTS ( SELECT 1 FROM syscolumns WHERE name='FSourceInterID'  AND id = object_id('ICStockBillEntry') ) EXEC('UPDATE t3 SET t3.FChildren=t3.FChildren+1
FROM ICStockBill t1 INNER JOIN ICStockBillEntry t2 ON t1.FInterID=t2.FInterID
INNER JOIN ICSubContract t3 ON t3.FClassTypeID=t2.FSourceTranType AND t3.FInterID=t2.FSourceInterID
WHERE t1.FTranType=28 AND t1.FInterID='+@FInterID+' AND t2.FSourceInterID>0 ' ) 

UPDATE P1 SET P1.FLockFlag=(CASE WHEN ISNULL(t1.FQty,0)<=0 THEN 0 ELSE 1 END)
  FROM PPBOMEntry P1 INNER JOIN ICStockBillEntry u1 ON p1.FICMOInterID = u1.FOrderInterID AND p1.FOrderEntryID=u1.FOrderEntryID AND u1.FPPBOMEntryID = p1.FEntryID AND u1.FItemID=P1.FItemID
  LEFT OUTER JOIN t_LockStock t1 ON P1.FInterID=t1.FInterID AND P1.FEntryID=t1.FEntryID AND t1.FTranType=88
WHERE u1.FInterID=@FInterID AND ISNULL(u1.FMTONo,'') = '' 
   if exists( 
   select 1 from ICStockBillEntry enty 
   inner join ICStockBill bill on enty.FinterID=bill.FinterID
   inner join t_PDASNTemp  temp on temp.FBillNo=bill.FBillNo and enty.FEntryID=temp.FEntryID
   where enty.FinterID=@FInterID  )
   begin
   Update enty
   Set enty.FPDASn = temp.FPDASn
   from ICStockBillEntry enty
   inner join ICStockBill bill
   on enty.FinterID=bill.FinterID
   inner join t_PDASNTemp  temp
   on temp.FBillNo=bill.FBillNo and enty.FEntryID=temp.FEntryID
   Where enty.FinterID = @FInterID  and len(temp.FPDASn)>0  and  temp.FPDASn is not null  
   End
   Delete temp
   from ICStockBillEntry enty
   inner join ICStockBill bill
   on enty.FinterID=bill.FinterID
   inner join t_PDASNTemp  temp
   on temp.FBillNo=bill.FBillNo and enty.FEntryID=temp.FEntryID
   Where enty.FinterID = @FInterID   

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
