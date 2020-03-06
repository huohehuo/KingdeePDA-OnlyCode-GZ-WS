if (exists (select * from sys.objects where name = 'proc_BatchNoFirstCome'))
    drop proc proc_BatchNoFirstCome
go
create proc proc_BatchNoFirstCome
(  
   @FItemID int, --商品ID 
   @FBatchNo varchar(128) --批号(如果大于8位)
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
select FID,FItemID,FBatchNo as 批号,FStockID,FStockName as 仓库名称,FStockPlaceID,FStockPlaceName as 仓位名称,convert(float,FQty) as 基本单位库存 from #tmp1 order by FID asc
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