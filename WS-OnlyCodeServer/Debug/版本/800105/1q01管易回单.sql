if (exists (select * from sys.objects where name = 'proc_PushCheckLogisticsSingle'))
drop proc proc_PushCheckLogisticsSingle
go
create proc proc_PushCheckLogisticsSingle
(
  @mainStr nvarchar(1000) --主表参数
 
)
as 
set nocount on     --开启一个模式，也就是不再刷新多少行受影响的信息，可以提高性能
set xact_abort on  --事务选项设置为出错全部回滚
begin tran declare
          @FDate varchar(20),            --单据日期 
          @FOrderID varchar(128),     
          @FPDAID varchar(128),   
          @FID int,
          @FBillerID varchar(20),        --制单人ID 
          @FDeliverBillNo varchar(128),--发货单号
          @FTerraceBillNo  varchar(128),--平台单号
          @FCustomerName varchar(255),--客户
          @FBillNo varchar(255)--物流单号  
     
          set @FBillerID=dbo.getString(@mainStr,'|',1)     -- 制单人ID 
          set @FBillNo=dbo.getString(@mainStr,'|',2)      --物流单号
          set @FOrderID=dbo.getString(@mainStr,'|',3)      --PDA单据编号
          set @FPDAID=dbo.getString(@mainStr,'|',4)      --PDA序号ID
          set @FDeliverBillNo=dbo.getString(@mainStr,'|',5) --发货单号
          set @FTerraceBillNo=dbo.getString(@mainStr,'|',6) --平台单号
          set @FCustomerName=dbo.getString(@mainStr,'|',7) --客户
          
          set  @FDate= CONVERT(varchar(20),GETDATE(),20) 
          insert into t_PDALogisticsSingle(FDate,FUserID,FBillNo,FTypeID,FDeliverBillNo,FTerraceBillNo,FCustomerName)values(@FDate,@FBillerID,@FBillNo,0,@FDeliverBillNo,@FTerraceBillNo,@FCustomerName) 
          select @FID=MAX(FID) from t_PDALogisticsSingle 
          insert into t_PDALogisticsSingleEntry(FID,FPDAID,FOrderID,FBarCode,FItemID,FStockID,FStockPlaceID, FBatchNo,  FKFPeriod,  FKFDate,  FQty,FUnitID,FStockID_B,FStockPlaceID_B,FRemark2_B,FDateOutStore_B,FUserOutStore_B,FIDLogistics_B,FIsOutStore_B)
          select @FID,t1.FPDAID,t1.FOrderID,t1.FBarCode,t1.FItemID,t1.FStockID,t1.FStockPlaceID, t1.FBatchNo,  t1.FKFPeriod,  t1.FKFDate,  t1.FQty,t2.FUnitID,t2.FStockID,t2.FStockPlaceID,t2.FRemark2,t2.FDateOutStore,t2.FUserOutStore,FIDLogistics,t2.FIsOutStore from a_DetailsTable t1 left join t_PDABarCodeSign t2 on t1.FBarCode=t2.FBarCode where t1.FOrderID=@FOrderID and t1.FPDAID=@FPDAID
          update t_PDABarCodeSign  set  FIDLogistics=@FID,FDateOutStore=@fdate,FIsOutStore='已出库',FQtyOut =FQtyOut + t.FQty,FRemark2='管易生单',FUserInStore=@FBillerID   from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
          delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
          --单个
  
commit tran 
return;
if(0<>@@error)
	goto error1
error1:
	rollback tran;