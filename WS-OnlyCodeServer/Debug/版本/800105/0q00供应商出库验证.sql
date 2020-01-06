if (exists (select * from sys.objects where name = 'proc_SupplyOutStoreBarCode_check'))
    drop proc proc_SupplyOutStoreBarCode_check
go
create proc proc_SupplyOutStoreBarCode_check
(  
  @FBarCode varchar(128), --���� 
  @FUserID int --��Ӧ���û�ID
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
declare @FBillNo varchar(128),
        @FInterIDAssemble int,--�Ƿ�װ��
          @FIsInStore varchar(20),--���״̬
          @FItemID int,--��ƷID
          @FCustID int,--��Ӧ��ID
          @FQtyAll decimal(28,10),--װ������
           @FPrice decimal(28,10),--���۵�����
           @FUnitID int,--��λID
          @FIsOutStore varchar(20)--����״̬
set @FBillNo='OK'
  if exists(select 1 from t_PDABarCodeSign where FBarCode=@FBarCode)--�ж������Ƿ����ϵͳ��
  begin
         if not exists(select 1 from a_DetailsTable where FBarCode=@FBarCode) --�̵������Ƿ������
         begin
			  select @FUnitID=FUnitID,@FItemID=FItemID,@FIsInStore=isnull(FIsInStore,'δ���'),@FIsOutStore=case when FQty=FQtyOut then '�ѳ���' else 'δ����' end ,@FInterIDAssemble=isnull(FInterIDAssemble,0) from t_PDABarCodeSign where FBarCode=@FBarCode
			  if(@FIsInStore='�����')
			  begin
				select @FQtyAll=FQty-FQtyOut from t_PDABarCodeSign where FBarCode=@FBarCode
				if(@FQtyAll=0)
				begin
				set @FBillNo='����������ȫ����,�����ٴγ���'
				end
			  end
			  else
			  begin
				set @FBillNo='δ�������벻�ܳ���'
			  end
	     end
	     else
	     begin
	         set @FBillNo= '�������ѷ���ҵ���ϵ,��ʱ���ܳ���'
	     end
  end
  else
  begin
  	 set @FBillNo='�����벻��������ϵͳ��'
  end  
 if(@FBillNo='OK')
 begin 
   select @FCustID=FCustID from t_UserPDASupply where FID = @FUserID
   if not exists(select 1 from IcPrcPlyEntry t1 left join IcPrcPly t2 on t1.FInterID=t2.FInterID left join IcPrcPlyEntrySpec t3 on t1.FRelatedID=t3.FRelatedID and t1.FInterID=t3.FInterID And t1.FItemID=t3.FItemID left join t_Organization t4 on t4.FTypeID = t3.FRelatedID   where t1.FItemID=@FItemID and t1.FUnitID=@FUnitID and (t4.FItemID=@FCustID  or t1.FRelatedID=@FCustID) and (t1.FBegQty<=1 And t1.FEndQty>=1 or (t1.FBegQty =0 and t1.FEndQty=0)) and (t1.FBegDate<=CONVERT(varchar(20),GETDATE(),23) And t1.FEndDate>=CONVERT(varchar(20),GETDATE(),23) ) and FChecked=1 )
   begin
   set @FBillNo = '�������Ӧ�ͻ�û�ж�Ӧ�ļ۸�����,���ܳ���'
   end
   else
   begin 
   select top 1   @FPrice=FPrice from IcPrcPlyEntry t1 left join IcPrcPly t2 on t1.FInterID=t2.FInterID left join IcPrcPlyEntrySpec t3 on t1.FRelatedID=t3.FRelatedID and t1.FInterID=t3.FInterID And t1.FItemID=t3.FItemID left join t_Organization t4 on t4.FTypeID = t3.FRelatedID   where t1.FItemID=@FItemID and t1.FUnitID=@FUnitID and (t4.FItemID=@FCustID  or t1.FRelatedID=@FCustID) and (t1.FBegQty<=1 And t1.FEndQty>=1 or (t1.FBegQty =0 and t1.FEndQty=0)) and (t1.FBegDate<=CONVERT(varchar(20),GETDATE(),23) And t1.FEndDate>=CONVERT(varchar(20),GETDATE(),23) ) and FChecked=1 order by  t2.FPri,t1.FEntryID
   end
   --select @FCustID=FCustID from t_UserPDASupply where FID = @FUserID
   --if not exists(select 1 from t_PDAModifyPriceEntry where FItemID=@FItemID and FCustID = @FCustID)
   --  set @FBillNo = '�������Ӧ�������Լ��ֿ�û�������۵�,���ܳ���'
   --else
   -- begin 
   --    select top 1   @FPrice=FPrice  from t_PDAModifyPriceEntry where FItemID=@FItemID and FCustID=@FCustID order by FEntryID desc
   -- end
 end
create table #Tmp11111 --������ʱ��#Tmp
( 
    FItemID int,--��ƷID
    FUnitID int,--��λID
    FQty decimal(28,10),--����
    FPrice decimal(28,10),
    FStockID int,
    FStockPlaceID int,
    FBatchNo varchar(128),
    FKFPeriod int,
    FKFDate varchar(12),
    FBillNo   varchar(255),--���ݱ��
    FExplanation varchar(255),--˵��
)
 
insert into #Tmp11111(FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,FExplanation,FPrice)
select top 1 FItemID,FUnitID,FQty-FQtyOut,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,@FBillNo,@FPrice from t_PDABarCodeSign where FBarCode=@FBarCode
 
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FExplanation)values(@FBillNo)
end
select  t6.FName as �����ֿ�,t_2043.FName as ��Ʒϵ��,t1.FExplanation as ˵��,t1.FBillNo,t1.FItemID,t1.FUnitID,convert(float,FQty) as FQty,t1.FStockID,t1.FStockPlaceID,t1.FBatchNo,t1.FKFPeriod,t1.FKFDate,t1.FPrice as ����,t2.FNumber as ��Ʒ����,t2.FName as ��Ʒ���� from #Tmp11111 t1 inner join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_Item_2043  t_2043  on t2.F_109=t_2043.FItemID  left join t_Stock t6 on t1.FStockID =t6.FItemID
drop table #Tmp11111
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
