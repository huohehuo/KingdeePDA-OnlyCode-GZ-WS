if (exists (select * from sys.objects where name = 'proc_OutStoreBarCode_Red_Insert'))
    drop proc proc_OutStoreBarCode_Red_Insert
go
create proc proc_OutStoreBarCode_Red_Insert
(
  @FOrderID varchar(255),--PDA单据号
  @FPDAID varchar(255),  --PDA序列号
  @FBarCode varchar(128), --条码
  @FQty decimal(28,10),  --出库数量
  @FStockID int, --仓库ID
  @FStockPlaceID int--仓位ID
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
declare @FBillNo varchar(128),
        @FInterIDAssemble int,--是否装箱
          @FIsInStore varchar(20),--入库状态
          @FIsOutStore varchar(20)--出库状态
set @FBillNo='OK'
  	   if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode)--判断是否装箱条码
begin  
    insert into a_DetailsTable(FPDAID,FOrderID,FBarCode,FItemID,FStockID,FStockPlaceID, FBatchNo,  FKFPeriod,  FKFDate,  FQty)
      select  @FPDAID,@FOrderID, t2.FBarCode, t2.FItemID,@FStockID, @FStockPlaceID,  t2.FBatchNo,  t2.FKFPeriod,  t2.FKFDate, t2.FQtyOut from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t2.FQtyOut>0 and t1.FBillNo= @FBarCode
end
else
begin
  declare @FInterID int --是否拆箱 1拆 0未拆
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
		 insert into a_DetailsTable(FPDAID,FOrderID,FBarCode,FItemID,FStockID,FStockPlaceID, FBatchNo,  FKFPeriod,  FKFDate,  FQty,FInterID)
	  select top 1  @FPDAID,@FOrderID, FBarCode, FItemID,@FStockID,@FStockPlaceID,  FBatchNo,  FKFPeriod,  FKFDate,@FQty,@FInterID from t_PDABarCodeSign where FBarCode=@FBarCode 
	end
end
 
create table #Tmp11111 --创建临时表#Tmp
(
    FBillNo   varchar(255), 
)
set @FBillNo='OK'
 
insert into #Tmp11111(FBillNo)values(@FBillNo)
select FBillNo as 单据编号 from #Tmp11111
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
