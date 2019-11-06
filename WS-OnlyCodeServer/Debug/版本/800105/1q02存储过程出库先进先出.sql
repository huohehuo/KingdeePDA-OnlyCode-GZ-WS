if (exists (select * from sys.objects where name = 'proc_FInishFirstCome'))
    drop proc proc_FInishFirstCome
go
create proc proc_FInishFirstCome
(  
  @FBarCode varchar(128) --条形码
)
as 
--------------开启一个模式，也就是不再刷新多少行受影响的信息，可以提高性能
set nocount on
--------------开始存储过程
begin
--------------事务选项设置为出错全部回滚
set xact_abort on
--------------开启事务
begin tran
    declare @FItemID int,
            @FDateInStore varchar(12)--入库日期
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
select t1.FBarCode,t2.FNumber,t2.FName,t6.FName,t7.FName,t1.FQty from t_PDABarCodeSign t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_Stock t6 on t1.FStockID=t6.FItemID left join t_StockPlace t7 on t1.FStockPlaceID=t7.FSPID where t1.FItemID = @FItemID and t1.FIsInStore='已入库' and isnull(t1.FIsOutStore,'未出库')='未出库' and not exists(select 1 from a_DetailsTable where FBarCode=t1.FBarCode) and t1.FDateInStore<@FDateInStore   
select FBarCode as 条码,FNumber as 物料编码,FName as 物料名称,FStockName as 仓库名称,FStockPlaceName as 仓位名称,convert(float,FQty) as 数量 from #tmp1 order by FID asc
drop table #tmp1
commit tran 
return;
--------------存在错误
if(0<>@@error)
	goto error1
--------------回滚事务	
error1:
	rollback tran;
--------------结束存储过程
end 