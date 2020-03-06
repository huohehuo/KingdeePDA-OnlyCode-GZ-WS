if (exists (select * from sys.objects where name = 'proc_UpdateBarCodeFirst'))
    drop proc proc_UpdateBarCodeFirst
go
create proc proc_UpdateBarCodeFirst
( 
  @mainStr varchar(8000)  
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

declare   @FOrderID varchar(50),--PDA单据编号
          @FPDAID varchar(50),  --PDA序列号 
          @FUserIDSupply int,
          @FID int,--盘点方案ID
          @FCheckUserID varchar(128)--制单人          
          set @FID = dbo.getString(@mainStr,'|',1) --盘点方案ID
          set @FCheckUserID  = dbo.getString(@mainStr,'|',2) --制单人
          set @FOrderID =dbo.getString(@mainStr,'|',3) --盘点方案ID
          set @FPDAID =dbo.getString(@mainStr,'|',4) --PDA序列号
          select @FCheckUserID=FUserID,@FUserIDSupply = FID from t_UserPDASupply where FID = @FCheckUserID
 		    --存在系统	
 			insert into t_PDABarCodeCheckFirstEntry(FID,FCheckUserID,FUserIDSupply,FCheckDate ,FInterID,FEntryID,FBillNo,FInterIDIn,FInterIDOut,FInterIDAssemble,FInterIDDisassemble,FItemID,FUnitID,FBarCode,FDatePrint,FDateInStore,FDateOutStore,FIsInStore,FIsOutStore,FUserInStore,FUserOutStore,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FDatePrintShort,FQty,FQtying,FPrintType,FUserPrint,FRemark1,FRemark2,FRemark3,FRemark4,FRemark5,FRemark6,FRemark7,FRemark8,FRemark9,FRemark10,FRemark,FOver,FOrderBillNo,FStatus)
			select @FID,@FCheckUserID,@FUserIDSupply,CONVERT(varchar(50),getdate(),20),FInterID,FEntryID,FBillNo,FInterIDIn,FInterIDOut,FInterIDAssemble,FInterIDDisassemble,FItemID,FUnitID,FBarCode,FDatePrint,FDateInStore,FDateOutStore,FIsInStore,FIsOutStore,FUserInStore,FUserOutStore,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FDatePrintShort,FQty-ISnull(FQtyOut,0),0,FPrintType,FUserPrint,FRemark1,FRemark2,FRemark3,FRemark4,FRemark5,FRemark6,FRemark7,FRemark8,FRemark9,FRemark10,FRemark,FOver,FOrderBillNo,1 
			from t_PDABarCodeSign t1 where FBarCode in(select FBarCode from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID) 
            --不存在系统
            	insert into t_PDABarCodeCheckFirstEntry(FID,FCheckUserID,FUserIDSupply,FCheckDate,FInterID,FEntryID,FBillNo,FInterIDIn,FInterIDOut,FInterIDAssemble,FInterIDDisassemble,FItemID,FUnitID,FBarCode,FDatePrint,FDateInStore,FDateOutStore,FIsInStore,FIsOutStore,FUserInStore,FUserOutStore,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FDatePrintShort,FQty,FQtying,FPrintType,FUserPrint,FRemark1,FRemark2,FRemark3,FRemark4,FRemark5,FRemark6,FRemark7,FRemark8,FRemark9,FRemark10,FRemark,FOver,FOrderBillNo,FStatus)
			select @FID,@FCheckUserID,@FUserIDSupply,CONVERT(varchar(50),getdate(),20),0,0,'',0,0,0,0,0,0,FBarCode,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,'',0,'','',1,0,'',0,'','','','','','','','','','','',0,'',0 from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID and not exists(select 1 from t_PDABarCodeSign where FBarCode=a_DetailsTable.FBarCode)
 
--end
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
 
