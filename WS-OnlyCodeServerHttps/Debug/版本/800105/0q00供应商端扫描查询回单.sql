if (exists (select * from sys.objects where name = 'proc_UpLoadBarCodeSignScan'))
    drop proc proc_UpLoadBarCodeSignScan
go
create proc proc_UpLoadBarCodeSignScan
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
          @FUserID varchar(128)--制单人           
          set @FUserID  = dbo.getString(@mainStr,'|',1) --制单人
          set @FOrderID =dbo.getString(@mainStr,'|',2) --单据编号
          set @FPDAID =dbo.getString(@mainStr,'|',3) --PDAID
          insert into t_PDABarCodeSignScan_Search(FUserID,FBarCode,FItemID,FOrderID,FPDAID,FNum,FDate)
          select @FUserID,FBarCode,FItemID,FOrderID,FPDAID,1,CONVERT(varchar(20),GETDATE(),20) from a_DetailsTable   where FOrderID=@FOrderID and FPDAID=@FPDAID
 
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
 
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
 
