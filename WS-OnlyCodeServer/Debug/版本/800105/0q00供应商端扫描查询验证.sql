if (exists (select * from sys.objects where name = 'proc_BarCodeSignScan_check'))
    drop proc proc_BarCodeSignScan_check
go
create proc proc_BarCodeSignScan_check
(  
  @FBarCode varchar(128) --条码 
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
          @FQtyAll decimal(28,10),--装箱总数
          @FIsOutStore varchar(20)--出库状态
set @FBillNo='OK'
  	   
  if exists(select 1 from t_PDABarCodeSign where FBarCode=@FBarCode)--判断条码是否存在系统中
  begin
        if not exists(select 1 from a_DetailsTable where FBarCode=@FBarCode) --盘点条码是否已添加
        begin
		   if exists(select 1 from t_PDABarCodeSignScan_Search where FBarCode=@FBarCode)
		    set @FBillNo = '该条码已存在扫描记录,不能再次扫描'
	    end
	    else
	     begin
	         set @FBillNo= '该条码已发生业务关系,暂时不能扫描'
	     end
  end
  else
  begin
  	 set @FBillNo='该条码不存在条码系统中'
  end
 
 
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
    FBillNo   varchar(255),--单据编号
    FExplanation varchar(255),--说明
)
 
insert into #Tmp11111(FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,FExplanation)
select top 1 FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,@FBillNo from t_PDABarCodeSign where FBarCode=@FBarCode
 
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FExplanation)values(@FBillNo)
end
select t1.FExplanation as 说明,t1.FBillNo,t1.FItemID,t1.FUnitID,convert(float,t1.FQty) as FQty,t1.FStockID,t1.FStockPlaceID,t1.FBatchNo,t1.FKFPeriod,t1.FKFDate,t2.FNumber as 商品编码,t2.FName as 商品名称 from #Tmp11111 t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID
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
