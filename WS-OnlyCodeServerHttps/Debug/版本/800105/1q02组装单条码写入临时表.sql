if (exists (select * from sys.objects where name = 'proc_AssembleBarCode_Insert'))
    drop proc proc_AssembleBarCode_Insert
go
create proc proc_AssembleBarCode_Insert
(
  @FOrderID varchar(255),--PDA单据号(也是箱码)
  @FPDAID varchar(255),  --PDA序列号
  @FBarCode varchar(128), --条码
  @FItemID int,--商品ID
  @FUnitID int,--单位ID
  @FStockID int,
  @FStockPlaceID int,
  @FBatchNo varchar(128) 
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

declare @FInterID varchar(20),     --单号id
        @FBillNo varchar(50),      -- 编号   
        @FBillerID int, 
        @FDateRet varchar(50),    --回单时间
 
        @Fdate varchar(50),       --日期  
         @FIsInStore varchar(20),--入库状态
          @FIsOutStore varchar(20),--出库状态
        @FTypeID int  --源单类型
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
      if not exists(select 1 from a_DetailsTable where FBarCode=@FBarCode)
       begin
 	insert into a_DetailsTable(FPDAID,FOrderID,FBarCode,FItemID,FStockID,FStockPlaceID, FBatchNo,  FKFPeriod,  FKFDate,  FQty,FUnitID)
    values(@FPDAID,@FOrderID,@FBarCode,@FItemID,@FStockID,@FStockPlaceID, @FBatchNo,  0,  '',  1,@FUnitID) 
       end
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FBillNo)values(@FBillNo)
end
select FBillNo as 说明,FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate from #Tmp11111
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
