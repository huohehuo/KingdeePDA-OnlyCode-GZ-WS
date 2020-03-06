if (exists (select * from sys.objects where name = 'proc_BarCodeSignScan_Insert'))
    drop proc proc_BarCodeSignScan_Insert
go
create proc proc_BarCodeSignScan_Insert
(
  @FOrderID varchar(255),--PDA���ݺ�
  @FPDAID varchar(255),  --PDA���к�
  @FBarCode varchar(128)  --����
 
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
 
     if not exists(select 1 from a_DetailsTable where FBarCode=@FBarCode)
       begin
     insert into a_DetailsTable(FPDAID,FOrderID,FBarCode,FItemID,FStockID,FStockPlaceID, FBatchNo,  FKFPeriod,  FKFDate,  FQty)
        select top 1  @FPDAID,@FOrderID, FBarCode, FItemID,FStockID, FStockPlaceID,  FBatchNo,  FKFPeriod,  FKFDate,  FQty from t_PDABarCodeSign where FBarCode=@FBarCode
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
