if (exists (select * from sys.objects where name = 'proc_Allot1_check'))
    drop proc proc_Allot1_check
go
create proc proc_Allot1_check
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
 if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode)
begin
  if exists( select 1 from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t1.FBillNo= @FBarCode)--判断是否存在该箱码 
	begin
	 if exists(select 1 from a_DetailsTable where FBarCode in (select FBarCode from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t1.FBillNo= @FBarCode))
	  begin
	  		set @FBillNo='该装箱码已发生业务关系,暂时不能添加'
	  end
	  else
	  begin
	      select  @FIsInStore=t2.FIsInStore,@FIsOutStore=case when t2.FQty=t2.FQtyOut then '已出库' else '未出库' end  from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t1.FBillNo= @FBarCode
		  if(@FIsInStore='已入库')
		   begin
		    select  @FQtyAll=SUM(t2.FQty-t2.FQtyOut) from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where  t1.FBillNo= @FBarCode
		    if(@FQtyAll=0)
		    begin
		     set @FBillNo='该箱码已完全出库,不能再次出库'
		    end
		   end 
		  else 
	        begin
	        set @FBillNo='未入库的箱码,不能出库'
	        end 
	   end 
	  
	end
	else
	begin
	  set @FBillNo='系统不存在该箱码'
	end
end
else
begin
  if exists(select 1 from t_PDABarCodeSign where FBarCode=@FBarCode)--判断条码是否存在系统中
  begin
         if not exists(select 1 from a_DetailsTable where FBarCode=@FBarCode) --盘点条码是否已添加
         begin
			  select @FIsInStore=isnull(FIsInStore,'未入库'),@FIsOutStore=case when FQty=FQtyOut then '已出库' else '未出库' end ,@FInterIDAssemble=isnull(FInterIDAssemble,0) from t_PDABarCodeSign where FBarCode=@FBarCode
			  if(@FIsInStore='已入库')
			  begin
				select @FQtyAll=FQty-FQtyOut from t_PDABarCodeSign where FBarCode=@FBarCode
				if(@FQtyAll=0)
				begin
				set @FBillNo='该条码已完全出库,不能再次出库'
				end
			  end
			  else
			  begin
				set @FBillNo='未入库的条码不能出库'
			  end
	     end
	     else
	     begin
	         set @FBillNo= '该条码已发生业务关系,暂时不能出库'
	     end
  end
  else
  begin
  	 set @FBillNo='该条码不存在条码系统中'
  end 
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
    FAssembleBillNo varchar(128),--箱码
    FExplanation varchar(255),--说明
)
if(@FBillNo='OK')
begin
   if exists(select 1 from t_PDABarCodeSign t1 inner join t_PDAAssemble t2 on t1.FInterIDAssemble=t2.FInterID where FBarCode=@FBarCode)
   begin
   set @FBillNo = 'OK2'
  --select @FBillNo = '该条码已绑定箱码:' +t2.FBillNo +',不能单独调拨,请先拆箱!' from  t_PDABarCodeSign t1 inner join t_PDAAssemble t2 on t1.FInterIDAssemble=t2.FInterID where FBarCode=@FBarCode
   end
end
if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode)
begin
select  @FQtyAll=SUM(t2.FQty-t2.FQtyOut) from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t1.FBillNo= @FBarCode
insert into #Tmp11111(FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,FExplanation) select  top 1 t2.FItemID,t2.FUnitID,@FQtyAll,t2.FStockID,t2.FStockPlaceID,t2.FBatchNo,t2.FKFPeriod,t2.FKFDate,t2.FBillNo,@FBillNo from  t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t1.FBillNo= @FBarCode
end
else
begin
insert into #Tmp11111(FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,FExplanation)
select top 1 FItemID,FUnitID,FQty-FQtyOut,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,@FBillNo from t_PDABarCodeSign where FBarCode=@FBarCode
end
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FExplanation)values(@FBillNo)
end
select t1.FExplanation as 说明,t1.FBillNo,t1.FItemID,t1.FUnitID,convert(float,FQty) as FQty,t1.FStockID,t1.FStockPlaceID,t1.FBatchNo,t1.FKFPeriod,t1.FKFDate,t2.FNumber from #Tmp11111 t1 inner join t_ICItem t2 on t1.FItemID = t2.FItemID
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
