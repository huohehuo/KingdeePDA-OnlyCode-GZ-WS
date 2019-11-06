if (exists (select * from sys.objects where name = 'proc_LooseBarCode_Insert'))
    drop proc proc_LooseBarCode_Insert
go
create proc proc_LooseBarCode_Insert
(
  @FOrderID varchar(255),--PDA���ݺ�(Ҳ������)
  @FPDAID varchar(255),  --PDA���к�
  @FBarCode varchar(128), --����
  @FItemID int,--��ƷID
  @FUnitID int,--��λID
  @FStockID int,
  @FStockPlaceID int,
  @FBatchNo varchar(128),
  @FIsInStore varchar(128), --���״̬
  @FIsOutStore varchar(128) --����״̬
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
        @FBillerID int, 
        @FDateRet varchar(50),    --�ص�ʱ��
        @Fdate varchar(50),       --����     
       
        @FTypeID int  --Դ������
 create table #Tmp11111 --������ʱ��#Tmp
(
    FItemID int,--��ƷID
    FUnitID int,--��λID
    FQty decimal(28,10),--����
    FStockID int,
    FStockPlaceID int,
    FBatchNo varchar(128),
    FKFPeriod int,
    FKFDate varchar(12),
    FBillNo   varchar(255),--˵�� 
)
if(@FIsOutStore='�ѳ���')
begin
set @FIsInStore='�����'
end
set @FBillNo='OK'
 	insert into a_DetailsTable(FPDAID,FOrderID,FBarCode,FItemID,FStockID,FStockPlaceID, FBatchNo,  FKFPeriod,  FKFDate,  FQty,FUnitID,FIsInStore,FIsOutStore)
    values(@FPDAID,@FOrderID,@FBarCode,@FItemID,@FStockID,@FStockPlaceID, @FBatchNo,  0,  '',  1,@FUnitID,@FIsInStore,@FIsOutStore) 
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FBillNo)values(@FBillNo)
end
select FBillNo as ˵��,FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate from #Tmp11111
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
