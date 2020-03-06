if (exists (select * from sys.objects where name = 'proc_FInishFirstCome'))
    drop proc proc_FInishFirstCome
go
create proc proc_FInishFirstCome
(  
  @FBarCode varchar(128) --������
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
    declare @FItemID int,
            @FDateInStore varchar(12)--�������
            if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode)
            begin
           select @FDateInStore=CONVERT(varchar(100), FDateInStore, 23),@FItemID=t2.FItemID from t_PDAAssemble t1 left join  t_PDABarCodeSign t2 on t1.FInterID=t2.FInterIDAssemble where t1.FBillNo=@FBarCode
    
            end 
            else
            begin           
            select @FDateInStore=CONVERT(varchar(100), FDateInStore, 23),@FItemID=FItemID from t_PDABarCodeSign where FBarCode=@FBarCode
            end
    create table #tmp1 (
  FID int identity(1,1) primary key not null,
  FNumber varchar(255),
  FName varchar(255),
  FBarCode  varchar(128),
FBatchNo varchar(128) default '',
FStockID int default 0,
FStockPlaceID int default 0,
FItemID int  default 0,
FStockName varchar(255) default '',
FStockPlaceName varchar(255) default '',
FQty decimal(28,10)
)
    
insert into #tmp1(FBarCode,FNumber,FName,FStockName,FStockPlaceName,FQty) 
select t1.FBarCode,t2.FNumber,t2.FName,t6.FName,t7.FName,t1.FQty from t_PDABarCodeSign t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_Stock t6 on t1.FStockID=t6.FItemID left join t_StockPlace t7 on t1.FStockPlaceID=t7.FSPID where t1.FItemID = @FItemID and t1.FIsInStore='�����' and isnull(t1.FIsOutStore,'δ����')='δ����' and not exists(select 1 from a_DetailsTable where FBarCode=t1.FBarCode) and t1.FDateInStore<@FDateInStore   
select FBarCode as ����,FNumber as ���ϱ���,FName as ��������,FStockName as �ֿ�����,FStockPlaceName as ��λ����,convert(float,FQty) as ���� from #tmp1 order by FID asc
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