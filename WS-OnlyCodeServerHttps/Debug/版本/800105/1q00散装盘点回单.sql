if (exists (select * from sys.objects where name = 'proc_UpLooseFZ'))
    drop proc proc_UpLooseFZ
go
create proc proc_UpLooseFZ
(
 @mainStr nvarchar(1000) --主表参数
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
        @FBillerID int,
        @FPDAID varchar(50),      --PDA序列号
        @FDateRet varchar(50),    --回单时间
        @FOrderID varchar(50),    --PDA单据编号
        @FInterIDAssemble int,
        @Fdate varchar(50),       --日期  
         
        @FTypeID int  --源单类型
set @FBillerID = dbo.getString(@mainStr,'|',1)   --操作员
set @Fdate =dbo.getString(@mainStr,'|',2)        --日期  
set @FDateRet =dbo.getString(@mainStr,'|',3)        --日期 
set @FPDAID =dbo.getString(@mainStr,'|',4)        --PDA序列号   
set @FOrderID =dbo.getString(@mainStr,'|',5)        --PDA单据编号 (也是箱码)
  
--if exists(select 1 from  t_PDAAssemble where FBillNo=@FOrderID)
--  begin
--  print convert(int,'装箱失败,该箱码已装箱')
--  end
   delete from t_PDABarCodeSign_In where FBarCode  in(select FBarCode from a_DetailsTable where FPDAID=@FPDAID and FOrderID=@FOrderID and FIsInStore='已入库') and (FStatus <> 1 or FInterID=0)
   insert into t_PDABarCodeSign_In(FInterID,FBarCode,FQty,FBillNo,FRemark,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before,FStatus,FDateUpLoad,FStockID,FStockPlaceID)  
   select 0,FBarCode,FQty,'','PDA散装入库',0,NULL,NULL,1,convert(varchar(20),GETDATE(),20),FStockID,FStockPlaceID from a_DetailsTable where FPDAID=@FPDAID and FOrderID=@FOrderID and FIsInStore='已入库' 
 insert into t_PDABarCodeSign(FInterIDIn,FDateInStore,FIsInStore,FUserInStore,FStockID,FStockPlaceID,FIndex,FInterID,FEntryID,FBillNo,FItemID,FUnitID,FBarCode,FDatePrint,FBatchNo,FKFDate,FKFPeriod,FDatePrintShort,FQty,FPrintType,FQtyOut,FRemark7,FRemark8,FRemark9,FRemark10,FInterIDAssemble,FRemark1,FIsOutStore,FRemark2)
                    select 0,case when FIsInStore='已入库' then CONVERT(varchar(20),GETDATE(),23) else NULL end,FIsInStore,case when FIsInStore='已入库' then @FBillerID else NULL end,FStockID,FStockPlaceID,0,0,0,'',FItemID,FUnitID,FBarCode,GETDATE(),FBatchNo,'',0,'',1,'',case when FIsOutStore='已出库' then 1 else 0 end,'','','','',0,'PDA散装入库',FIsOutStore,case when FIsOutStore='已出库' then 'PDA散装出库' else NULL end from a_DetailsTable where FPDAID=@FPDAID and FOrderID=@FOrderID

 delete from a_DetailsTable where FPDAID=@FPDAID and FOrderID=@FOrderID
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
