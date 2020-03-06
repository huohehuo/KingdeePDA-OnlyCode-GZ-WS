if (exists (select * from sys.objects where name = 'proc_CheckBarCodeFirst_check'))
    drop proc proc_CheckBarCodeFirst_check
go
create proc proc_CheckBarCodeFirst_check
(  
  @FBarCode varchar(128), --���� 
  @FID int--�̵㷽��ID
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
 if  exists(select 1 from a_DetailsTable where FBarCode=@FBarCode and FInterID = @FID) --�̵������Ƿ������
          set @FBillNo= '�������ѷ���ҵ���ϵ,��ʱ�����̵�'
 else if exists(select 1 from t_PDABarCodeCheckFirstEntry where FID = @FID  and FBarCode=@FBarCode)--�ж������Ƿ����ϵͳ��
  begin
     set @FBillNo='�������Ӧ���̵㷽�����̣�' 
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

insert into #Tmp11111(FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FExplanation)
select top 1 FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,@FBillNo from t_PDABarCodeSign where FBarCode=@FBarCode
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FExplanation)values(@FBillNo)
end
select FExplanation as ˵��,isnull(t2.FNumber,'δ������') as ���ϴ���,t2.FName as ��������,t2.FModel as ����ͺ� from #Tmp11111 t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID
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
