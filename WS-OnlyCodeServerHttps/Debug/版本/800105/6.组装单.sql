if (exists (select * from sys.objects where name = 'proc_Assemble'))
    drop proc proc_Assemble
go
create proc proc_Assemble
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
  
if exists(select 1 from  t_PDAAssemble where FBillNo=@FOrderID)
  begin
  print convert(int,'装箱失败,该箱码已装箱')
  end
  insert into t_PDAAssemble(FBillNo,FItemID,FUnitID,FIndex,FQty,FDatePrint,FStatus,FTypeID,FRemark1,FRemark2,FRemark3,FRemark4,FDate,FUserID)
            values(@FOrderID,0,0,0,0,NULl,1,0,NULL,NULL,NULL,NULL,GETDATE(),@FBillerID ) 
select @FInterIDAssemble=FInterID from t_PDAAssemble where FBillNo=@FOrderID
 insert into t_PDABarCodeSign(FInterIDIn,FDateInStore,FIsInStore,FUserInStore,FStockID,FStockPlaceID,FIndex,FInterID,FEntryID,FBillNo,FItemID,FUnitID,FBarCode,FDatePrint,FBatchNo,FKFDate,FKFPeriod,FDatePrintShort,FQty,FPrintType,FQtyOut,FRemark7,FRemark8,FRemark9,FRemark10,FInterIDAssemble,FRemark1)
                    select 0,CONVERT(varchar(20),GETDATE(),23),'已入库',@FBillerID,FStockID,FStockPlaceID,0,0,0,'',FItemID,FUnitID,FBarCode,GETDATE(),FBatchNo,'',0,'',1,'',0,'','','','',@FInterIDAssemble,'PDA装箱入库' from a_DetailsTable where FPDAID=@FPDAID and FOrderID=@FOrderID
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
