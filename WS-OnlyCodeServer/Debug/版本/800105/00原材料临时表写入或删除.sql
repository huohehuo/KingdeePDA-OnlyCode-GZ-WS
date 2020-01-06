if (exists (select * from sys.objects where name = 'proc_InsertDetailsTable'))
    drop proc proc_InsertDetailsTable
go
create proc proc_InsertDetailsTable
(
 @FTypeID int ,--0新增 1 删除 
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
        @FUnitID int,  --单位ID
        @FItemID int, --商品id
        @FStockID int,   --仓库id
        @FStockPlaceID int, --仓位id
        @FQty decimal(28,10),  --添加数量
        @FBatchNo varchar(255),     --批次
        @FType varchar(128),--单据类型
        @detailqty int,             --明细参数的个数
        @detailcount int,           --明细每行数据的长度 
        @detailIndex int,           --明细每行下标
        @countindex int             --分隔符|的数量
       set @detailqty=0        
       set @detailcount=9   
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
	set @FItemID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+1)    --商品id
	set @FUnitID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --单位ID
	set @FQty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)    --数量
	set @FStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)   --仓库ID
    set @FStockPlaceID =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5)  --仓位ID
    set @FBatchNo =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6)  --批次 
    set @FPDAID =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7)  --PDA序列号
    set @FOrderID =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8)  --单据id
    set @FType =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)  --单据类型
 
    set @detailIndex=@detailIndex+1 
     if(@FTypeID=0 and @FBatchNo<>'')
     begin
         if exists(select 1 from a_DetailsTable where FItemID=@FItemID and FUnitID=@FUnitID and FStockID=@FStockID and FStockPlaceID=@FStockPlaceID and FBatchNo=@FBatchNo and FPDAID=@FPDAID and FOrderID=@FOrderID and FType=@FType)
           update  a_DetailsTable set FQty=FQty+@FQty where FItemID=@FItemID and FUnitID=@FUnitID and FStockID=@FStockID and FStockPlaceID=@FStockPlaceID and FBatchNo=@FBatchNo and FPDAID=@FPDAID and FOrderID=@FOrderID and FType=@FType
         else
          insert into a_DetailsTable(FBarCode,FItemID,FBatchNo,FUnitID,FStockID,FStockPlaceID,FOrderID,FPDAID,FQty,FType)values(NEWID(),@FItemID,@FBatchNo,@FUnitID,@FStockID,@FStockPlaceID,@FOrderID,@FPDAID,@FQty,@FType)          
     end 
     else
     begin
      if exists(select 1 from a_DetailsTable where FItemID=@FItemID and FUnitID=@FUnitID and FStockID=@FStockID and FStockPlaceID=@FStockPlaceID and FBatchNo=@FBatchNo and FPDAID=@FPDAID and FOrderID=@FOrderID and FType=@FType)
           update  a_DetailsTable set FQty=FQty-@FQty where FItemID=@FItemID and FUnitID=@FUnitID and FStockID=@FStockID and FStockPlaceID=@FStockPlaceID and FBatchNo=@FBatchNo and FPDAID=@FPDAID and FOrderID=@FOrderID and FType=@FType
     end
    end
    set @detailqty=@detailqty+1
    end
delete from a_DetailsTable where FQty=0
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
