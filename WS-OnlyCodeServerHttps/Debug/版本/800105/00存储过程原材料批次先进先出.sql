if (exists (select * from sys.objects where name = 'proc_BatchNoFirstCome'))
    drop proc proc_BatchNoFirstCome
go
create proc proc_BatchNoFirstCome
(  
   @FItemID int, --��ƷID 
   @FBatchNo varchar(128) --����(�������8λ)
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
    
    create table #tmp1 (
  FID int identity(1,1) primary key not null,
FBatchNo varchar(128) default '',
FStockID int default 0,
FStockPlaceID int default 0,
FItemID int  default 0,
FStockName varchar(255) default '',
FStockPlaceName varchar(255) default '',
FQty decimal(28,10)
)
if(@FBatchNo<>'')
begin
insert into #tmp1(FItemID,FBatchNo,FStockID,FStockName,FStockPlaceID,FStockPlaceName,FQty)   select t1.FItemID,t1.FBatchNo,t1.FStockID,t3.FName,t1.FStockPlaceID,t4.FName,t1.FQty-isnull(t2.FQty,0) from ICInventory t1 left join ( select SUM(isnull(a1.FQty,0)*a2.FCoefficient)  as FQty ,a1.FItemID,a1.FStockID,a1.FStockPlaceID,a1.FBatchNo from a_DetailsTable a1 left join t_MeasureUnit a2 on a1.FUnitID=a2.FItemID group by a1.FItemID,a1.FStockID,a1.FStockPlaceID,a1.FBatchNo ) t2 on t1.FItemID=t2.FItemID and t1.FStockID=t2.FStockID and t1.FStockPlaceID = t2.FStockPlaceID and t1.FBatchNo=t2.FBatchNo left join t_Stock t3 on t1.FStockID=t3.FItemID left join t_StockPlace t4 on t1.FStockPlaceID=t4.FSPID where t1.FItemID=@FItemID and t1.FQty-isnull(t2.FQty,0)>0 and len(t1.FBatchNo)>7 and left(t1.FBatchNo,8)<left(@FBatchNo,8) order by t1.FBatchNo
end
select FID,FItemID,FBatchNo as ����,FStockID,FStockName as �ֿ�����,FStockPlaceID,FStockPlaceName as ��λ����,convert(float,FQty) as ������λ��� from #tmp1 order by FID asc
drop table #tmp1
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