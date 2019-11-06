if (exists (select * from sys.objects where name = 'proc_LooseBarCode_check'))
    drop proc proc_LooseBarCode_check
go
create proc proc_LooseBarCode_check
( 
  @FBarCode varchar(128)   --����  
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

declare  
        @FBillNo varchar(50) 
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
set @FBillNo='OK'
 
  if exists(select 1 from t_PDABarCodeSign where FBarCode=@FBarCode)--�̵������Ƿ���ڴ�ӡϵͳ
	begin
	set @FBillNo= '��SN���Ѵ�������ϵͳ,����ɢװ�̵�'
	end
  else
  begin
     if exists(select 1 from a_DetailsTable where FBarCode=@FBarCode) --�̵������Ƿ������
	  begin
		set @FBillNo= '��SN������װ��,�����ٴ�ɢװ�̵�'
	  end 
  end
 
 
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FBillNo)values(@FBillNo)
end
select FBillNo as ˵�� from #Tmp11111
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
