if (exists (select * from sys.objects where name = 'proc_PDAModifyPrices'))
drop proc proc_PDAModifyPrices
go
create proc proc_PDAModifyPrices
(
  @mainStr nvarchar(1000) --主表参数
 
)
as 
set nocount on     --开启一个模式，也就是不再刷新多少行受影响的信息，可以提高性能
set xact_abort on  --事务选项设置为出错全部回滚
begin tran declare
          @FDate varchar(20),            --单据日期 
          
          @FUserID varchar(20),        --制单人ID 
          @FItemID int,             --商品ID  
          @FStockID int,--仓库ID
          @FPrice decimal(28,10), --价格
          @FBillNo varchar(128)
          set @FUserID=dbo.getString(@mainStr,'|',1)     -- 制单人ID 
          set @FItemID=dbo.getString(@mainStr,'|',2)      --商品ID
          set @FStockID=dbo.getString(@mainStr,'|',3)      --仓库ID
          set @FPrice=dbo.getString(@mainStr,'|',4)      --单价
             set  @FDate= CONVERT(varchar(20),GETDATE(),20)  
   insert into t_PDAModifyPrices(FUserID,FItemID,FStockID,FPrice,FDate)values(@FUserID,@FItemID,@FStockID,@FPrice,@FDate)
commit tran 
return;
if(0<>@@error)
	goto error1
error1:
	rollback tran; 