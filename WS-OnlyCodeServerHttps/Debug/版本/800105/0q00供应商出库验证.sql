if (exists (select * from sys.objects where name = 'proc_SupplyOutStoreBarCode_check'))
    drop proc proc_SupplyOutStoreBarCode_check
go
create proc proc_SupplyOutStoreBarCode_check
(  
  @FBarCode varchar(128), --条码 
  @FUserID int --供应商用户ID
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
          @FItemID int,--商品ID
          @FCustID int,--供应商ID
          @FQtyAll decimal(28,10),--装箱总数
           @FPrice decimal(28,10),--调价单单价
           @FUnitID int,--单位ID
          @FIsOutStore varchar(20)--出库状态
set @FBillNo='OK'
  if exists(select 1 from t_PDABarCodeSign where FBarCode=@FBarCode)--判断条码是否存在系统中
  begin
         if not exists(select 1 from a_DetailsTable where FBarCode=@FBarCode) --盘点条码是否已添加
         begin
			  select @FUnitID=FUnitID,@FItemID=FItemID,@FIsInStore=isnull(FIsInStore,'未入库'),@FIsOutStore=case when FQty=FQtyOut then '已出库' else '未出库' end ,@FInterIDAssemble=isnull(FInterIDAssemble,0) from t_PDABarCodeSign where FBarCode=@FBarCode
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
 if(@FBillNo='OK')
 begin 
   select @FCustID=FCustID from t_UserPDASupply where FID = @FUserID
   if not exists(select 1 from IcPrcPlyEntry t1 left join IcPrcPly t2 on t1.FInterID=t2.FInterID left join IcPrcPlyEntrySpec t3 on t1.FRelatedID=t3.FRelatedID and t1.FInterID=t3.FInterID And t1.FItemID=t3.FItemID left join t_Organization t4 on t4.FTypeID = t3.FRelatedID   where t1.FItemID=@FItemID and t1.FUnitID=@FUnitID and (t4.FItemID=@FCustID  or t1.FRelatedID=@FCustID) and (t1.FBegQty<=1 And t1.FEndQty>=1 or (t1.FBegQty =0 and t1.FEndQty=0)) and (t1.FBegDate<=CONVERT(varchar(20),GETDATE(),23) And t1.FEndDate>=CONVERT(varchar(20),GETDATE(),23) ) and FChecked=1 )
   begin
   set @FBillNo = '该条码对应客户没有对应的价格政策,不能出库'
   end
   else
   begin 
   select top 1   @FPrice=FPrice from IcPrcPlyEntry t1 left join IcPrcPly t2 on t1.FInterID=t2.FInterID left join IcPrcPlyEntrySpec t3 on t1.FRelatedID=t3.FRelatedID and t1.FInterID=t3.FInterID And t1.FItemID=t3.FItemID left join t_Organization t4 on t4.FTypeID = t3.FRelatedID   where t1.FItemID=@FItemID and t1.FUnitID=@FUnitID and (t4.FItemID=@FCustID  or t1.FRelatedID=@FCustID) and (t1.FBegQty<=1 And t1.FEndQty>=1 or (t1.FBegQty =0 and t1.FEndQty=0)) and (t1.FBegDate<=CONVERT(varchar(20),GETDATE(),23) And t1.FEndDate>=CONVERT(varchar(20),GETDATE(),23) ) and FChecked=1 order by  t2.FPri,t1.FEntryID
   end
   --select @FCustID=FCustID from t_UserPDASupply where FID = @FUserID
   --if not exists(select 1 from t_PDAModifyPriceEntry where FItemID=@FItemID and FCustID = @FCustID)
   --  set @FBillNo = '该条码对应的物料以及仓库没有做调价单,不能出库'
   --else
   -- begin 
   --    select top 1   @FPrice=FPrice  from t_PDAModifyPriceEntry where FItemID=@FItemID and FCustID=@FCustID order by FEntryID desc
   -- end
 end
create table #Tmp11111 --创建临时表#Tmp
( 
    FItemID int,--商品ID
    FUnitID int,--单位ID
    FQty decimal(28,10),--数量
    FPrice decimal(28,10),
    FStockID int,
    FStockPlaceID int,
    FBatchNo varchar(128),
    FKFPeriod int,
    FKFDate varchar(12),
    FBillNo   varchar(255),--单据编号
    FExplanation varchar(255),--说明
)
 
insert into #Tmp11111(FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,FExplanation,FPrice)
select top 1 FItemID,FUnitID,FQty-FQtyOut,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,@FBillNo,@FPrice from t_PDABarCodeSign where FBarCode=@FBarCode
 
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FExplanation)values(@FBillNo)
end
select  t6.FName as 所属仓库,t_2043.FName as 产品系列,t1.FExplanation as 说明,t1.FBillNo,t1.FItemID,t1.FUnitID,convert(float,FQty) as FQty,t1.FStockID,t1.FStockPlaceID,t1.FBatchNo,t1.FKFPeriod,t1.FKFDate,t1.FPrice as 单价,t2.FNumber as 商品编码,t2.FName as 商品名称 from #Tmp11111 t1 inner join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_Item_2043  t_2043  on t2.F_109=t_2043.FItemID  left join t_Stock t6 on t1.FStockID =t6.FItemID
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
