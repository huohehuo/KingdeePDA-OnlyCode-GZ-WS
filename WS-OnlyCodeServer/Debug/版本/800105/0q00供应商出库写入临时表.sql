if (exists (select * from sys.objects where name = 'proc_SupplyOutStoreBarCode_Insert'))
    drop proc proc_SupplyOutStoreBarCode_Insert
go
create proc proc_SupplyOutStoreBarCode_Insert
(
  @FOrderID varchar(255),--PDA���ݺ�
  @FPDAID varchar(255),  --PDA���к�
  @FBarCode varchar(128), --����
  @FQty decimal(28,10),  --��������
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
        @FPrice decimal(28,10),--���۵�����
          @FIsInStore varchar(20),--���״̬
          @FIsOutStore varchar(20)--����״̬
set @FBillNo='OK'
 
     declare @FInterID int --�Ƿ���� 1�� 0δ��
     if exists(select 1 from t_PDABarCodeSign where FBarCode=@FBarCode and FInterIDAssemble>0)
     begin
     set @FInterID=1
     end
     else
     begin
      set @FInterID=0
     end 
     if not exists(select 1 from a_DetailsTable where FBarCode=@FBarCode)
     begin
     declare @FItemID int,
             @FCustID int
             select  top 1 @FItemID=   FItemID  from t_PDABarCodeSign where FBarCode=@FBarCode
                select @FCustID=FCustID from t_UserPDASupply where FID = @FUserID
             select top 1   @FPrice=FPrice  from t_PDAModifyPriceEntry where FItemID=@FItemID and FCustID=@FCustID order by FEntryID desc
     insert into a_DetailsTable(FPDAID,FOrderID,FBarCode,FItemID,FStockID,FStockPlaceID, FBatchNo,  FKFPeriod,  FKFDate,  FQty,FInterID,FPrice)
     select top 1  @FPDAID,@FOrderID, FBarCode, FItemID,FStockID,FStockPlaceID,  FBatchNo,  FKFPeriod,  FKFDate,@FQty,@FInterID,@FPrice from t_PDABarCodeSign where FBarCode=@FBarCode 
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
