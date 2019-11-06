if (exists (select * from sys.objects where name = 'proc_LooseBarCode_check'))
    drop proc proc_LooseBarCode_check
go
create proc proc_LooseBarCode_check
( 
  @FBarCode varchar(128)   --条码  
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

declare  
        @FBillNo varchar(50) 
 create table #Tmp11111 --创建临时表#Tmp
(
     FItemID int,--商品ID
    FUnitID int,--单位ID
    FQty decimal(28,10),--数量
    FStockID int,
    FStockPlaceID int,
    FBatchNo varchar(128),
    FKFPeriod int,
    FKFDate varchar(12),
    FBillNo   varchar(255),--说明 
)
set @FBillNo='OK'
 
  if exists(select 1 from t_PDABarCodeSign where FBarCode=@FBarCode)--盘点条码是否存在打印系统
	begin
	set @FBillNo= '该SN号已存在条码系统,不能散装盘点'
	end
  else
  begin
     if exists(select 1 from a_DetailsTable where FBarCode=@FBarCode) --盘点条码是否已添加
	  begin
		set @FBillNo= '该SN号正在装箱,不能再次散装盘点'
	  end 
  end
 
 
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FBillNo)values(@FBillNo)
end
select FBillNo as 说明 from #Tmp11111
drop table #Tmp11111
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
