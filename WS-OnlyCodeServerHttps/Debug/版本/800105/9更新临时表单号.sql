if (exists (select * from sys.objects where name = 'proc_UpdateDetailBillNo'))
    drop proc proc_UpdateDetailBillNo
go
create proc proc_UpdateDetailBillNo
(
 @mainStr nvarchar(1000),--主表参数
 @detailStr1 nvarchar(max),--明细参数
 @detailStr2 nvarchar(max),
 @detailStr3 nvarchar(max),
 @detailStr4 nvarchar(max),
 @detailStr5 nvarchar(max)
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
declare  
         @FOrderID varchar(50),--PDA单据编号
        @FPDAID varchar(50),  --PDA序列号 
        @FSelTranType varchar(20)  --源单类型
 

set @FOrderID=dbo.getString(@mainStr,'|',1) --新的PDA单号  
set @FPDAID=dbo.getString(@mainStr,'|',2) --PDA内码
 
 
 

declare  @FOrderIDOld  varchar(50), --老的PDA单号 
        
        @detailqty int,               --明细参数的个数
        @detailcount int,             --明细每行数据的长度 
        @detailIndex int,            --明细每行下标
        @countindex int              --分隔符|的数量
       set @detailqty=0        
       set @detailcount=2           
    while(@detailqty<5)--判断是明细的哪个参数
    begin
    if(@detailqty=1)
	begin
	set @detailStr1=@detailStr2
	end  
	if(@detailqty=2)
	begin
	set @detailStr1=@detailStr3
	end 
	if(@detailqty=3)
	begin
	set @detailStr1=@detailStr4
	end 
	if(@detailqty=4)
	begin
	set @detailStr1=@detailStr5
	end 
	if(@detailStr1='' or @detailStr1=null)
	begin
	break;
	end
	set @detailIndex=0 
	select @countindex=len(@detailStr1)-len(replace(@detailStr1, '|', ''))
	while(@countindex>@detailIndex*@detailcount)
	begin
	set @FOrderIDOld=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+1)    --老的PDA单号
	set @FPDAID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --PDAID
  update a_DetailsTable set FOrderID=@FOrderID where FOrderID=@FOrderIDOld and FPDAID=@FPDAID
		set @detailIndex=@detailIndex+1
    
end
set @detailqty=@detailqty+1
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

