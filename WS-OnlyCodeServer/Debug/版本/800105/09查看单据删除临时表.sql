if (exists (select * from sys.objects where name = 'proc_DeleteDetailsTable'))
    drop proc proc_DeleteDetailsTable
go
create proc proc_DeleteDetailsTable
(
 @detailStr1 varchar(max),--明细参数
 @detailStr2 varchar(max),
 @detailStr3 varchar(max),
 @detailStr4 varchar(max),
 @detailStr5 varchar(max) 
)
as
set nocount on
--------------开始存储过程
begin
--------------事务选项设置为出错全部回滚
set xact_abort on
--------------开启事务
begin tran 
declare  
        @FPDAID varchar(50),  --PDA序列号
        @FOrderID varchar(30),--单据id
        
        @FQty decimal(28,10),  --添加数量
        @FBarCode varchar(255),     --条码
        @detailqty int,             --明细参数的个数
        @detailcount int,           --明细每行数据的长度 
        @detailIndex int,           --明细每行下标
        @countindex int             --分隔符|的数量
       set @detailqty=0        
       set @detailcount=4   
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
	set @FBarCode=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+1)    --条码
	set @FQty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --数量
  
    set @FPDAID =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --PDA序列号
    set @FOrderID =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)  --单据id
    set @detailIndex=@detailIndex+1 
  	   if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode)
   begin
   update a_DetailsTable set FQty=a_DetailsTable.FQty-t.FQty from (select t2.FBarCode,t2.FQty from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where  t1.FBillNo= @FBarCode) t where t.FBarCode=a_DetailsTable.FBarCode  and FPDAID=@FPDAID and FOrderID=@FOrderID
   end
   else
        begin
         if exists(select 1 from a_DetailsTable where  FBarCode=@FBarCode and FPDAID=@FPDAID and FOrderID=@FOrderID)
           update  a_DetailsTable set FQty=FQty-@FQty where FBarCode=@FBarCode  and FPDAID=@FPDAID and FOrderID=@FOrderID
        end
         
    end
    set @detailqty=@detailqty+1
    end
delete from a_DetailsTable where FQty<=0
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
