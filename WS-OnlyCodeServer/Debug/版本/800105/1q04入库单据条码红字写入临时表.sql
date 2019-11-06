if (exists (select * from sys.objects where name = 'proc_InStoreBarCode_Red_Insert'))
    drop proc proc_InStoreBarCode_Red_Insert
go
create proc proc_InStoreBarCode_Red_Insert
(
  @FOrderID varchar(255),--PDA���ݺ�
  @FPDAID varchar(255),  --PDA���к�
  @FBarCode varchar(128), --����
  @FQty decimal(28,10)  --��������
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
          @FIsOutStore varchar(20)--����״̬
set @FBillNo='OK'
  	   if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode)--�ж��Ƿ�װ������
begin  
    insert into a_DetailsTable(FPDAID,FOrderID,FBarCode,FItemID,FStockID,FStockPlaceID, FBatchNo,  FKFPeriod,  FKFDate,  FQty)
      select  @FPDAID,@FOrderID, t2.FBarCode, t2.FItemID,t2.FStockID, t2.FStockPlaceID,  t2.FBatchNo,  t2.FKFPeriod,  t2.FKFDate,  t2.FQty-t2.FQtyOut from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t2.FQty-t2.FQtyOut>0 and t1.FBillNo= @FBarCode
end
else
begin
     if not exists(select 1 from a_DetailsTable where FBarCode=@FBarCode)
    begin
     insert into a_DetailsTable(FPDAID,FOrderID,FBarCode,FItemID,FStockID,FStockPlaceID, FBatchNo,  FKFPeriod,  FKFDate,  FQty)
  select top 1  @FPDAID,@FOrderID, FBarCode, FItemID,FStockID,FStockPlaceID,  FBatchNo,  FKFPeriod,  FKFDate,@FQty from t_PDABarCodeSign where FBarCode=@FBarCode 
  end
end
 
create table #Tmp11111 --������ʱ��#Tmp
(
    FBillNo   varchar(255), 
)
set @FBillNo='OK'
 
insert into #Tmp11111(FBillNo)values(@FBillNo)
select FBillNo as ���ݱ�� from #Tmp11111
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
