if (exists (select * from sys.objects where name = 'proc_CheckBarCodeAPP'))
    drop proc proc_CheckBarCodeAPP
go
create proc proc_CheckBarCodeAPP
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
          set @FOrderID =dbo.getString(@mainStr,'|',3) --商品ID
          set @FPDAID =dbo.getString(@mainStr,'|',4) --单位ID
          select @FCheckUserID=FUserID,@FUserIDSupply = FID from t_UserPDASupply where FID = @FCheckUserID
          insert into t_PDABarCodeSign_Check(FInterID,FBarCode,FQty)select @FID,t1.FBarCode,t1.FQty from a_DetailsTable t1 where FOrderID=@FOrderID and FPDAID=@FPDAID
update t_PDABarCodeCheckEntry set FQtying =FQtying+t.FQty,FCheckDate=CONVERT(varchar(128),GETDATE(),23),FCheckUserID=@FCheckUserID,@FUserIDSupply=FUserIDSupply   from (select a.* from a_DetailsTable a left join t_PDABarCodeCheckEntry b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID and b.FID = @FID) as t where t_PDABarCodeCheckEntry.FBarCode=t.FBarCode and t_PDABarCodeCheckEntry.FID=@FID
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
if not exists(select 1 from t_PDABarCodeCheckEntry where FID=@FID and FQty-FQtying>0)
begin
  update t_PDABarCodeCheck set FStatus = 1 where FID=@FID
end
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
 
