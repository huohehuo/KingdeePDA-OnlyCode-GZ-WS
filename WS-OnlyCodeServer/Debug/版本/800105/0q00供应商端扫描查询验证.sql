if (exists (select * from sys.objects where name = 'proc_BarCodeSignScan_check'))
    drop proc proc_BarCodeSignScan_check
go
create proc proc_BarCodeSignScan_check
(  
  @FBarCode varchar(128) --���� 
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
          @FQtyAll decimal(28,10),--װ������
          @FIsOutStore varchar(20)--����״̬
set @FBillNo='OK'
  	   
  if exists(select 1 from t_PDABarCodeSign where FBarCode=@FBarCode)--�ж������Ƿ����ϵͳ��
  begin
        if not exists(select 1 from a_DetailsTable where FBarCode=@FBarCode) --�̵������Ƿ������
        begin
		   if exists(select 1 from t_PDABarCodeSignScan_Search where FBarCode=@FBarCode)
		    set @FBillNo = '�������Ѵ���ɨ���¼,�����ٴ�ɨ��'
	    end
	    else
	     begin
	         set @FBillNo= '�������ѷ���ҵ���ϵ,��ʱ����ɨ��'
	     end
  end
  else
  begin
  	 set @FBillNo='�����벻��������ϵͳ��'
  end
 
 
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
    FBillNo   varchar(255),--���ݱ��
    FExplanation varchar(255),--˵��
)
 
insert into #Tmp11111(FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,FExplanation)
select top 1 FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,@FBillNo from t_PDABarCodeSign where FBarCode=@FBarCode
 
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FExplanation)values(@FBillNo)
end
select t1.FExplanation as ˵��,t1.FBillNo,t1.FItemID,t1.FUnitID,convert(float,t1.FQty) as FQty,t1.FStockID,t1.FStockPlaceID,t1.FBatchNo,t1.FKFPeriod,t1.FKFDate,t2.FNumber as ��Ʒ����,t2.FName as ��Ʒ���� from #Tmp11111 t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID
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
