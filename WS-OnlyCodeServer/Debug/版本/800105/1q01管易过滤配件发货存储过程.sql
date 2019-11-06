if (exists (select * from sys.objects where name = 'proc_LogisticsFilter'))
    drop proc proc_LogisticsFilter
go
create proc proc_LogisticsFilter
( 
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
create table #Tmp11111 --创建临时表#Tmp
( 
     FStockID int,--仓库ID
     FStockName varchar(128),--仓库编码
     FNumber varchar(128),--商品编码
     FQtyOrder decimal(18,8),--订单数量
     FOrderBillNo varchar(128),--订单号
     FLogistics varchar(128),--物流单号或者发货单号
     FExplanation varchar(255)--说明
)
declare @FExplanation varchar(255),
        @FNumber varchar(128),--商品编码
        @FQtyOrder decimal(18,8),--订单数量
        @FOrderBillNo varchar(128),--订单号
        @FLogistics varchar(128),--物流单号
        @FStockName varchar(255),
        @FStockID int,--仓库ID
        @FStockNumber varchar(128),--仓库编码
        @detailqty int,               --明细参数的个数
        @detailcount int,             --明细每行数据的长度 
        @detailIndex int,            --明细每行下标
        @countindex int              --分隔符|的数量
       set @FExplanation ='OK'
       set @detailqty=0        
       set @detailcount=5           
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
	set @FNumber=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+1)    --商品编码
	set @FQtyOrder=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --订单数量
	set @FOrderBillNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --销售订单号
	set @FLogistics=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)  --物流单号
    set @FStockNumber=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5)  --仓库编码
   

    if exists(select 1 from t_ICItem where FNumber=@FNumber and isnull(F_115,'配件')='成品')
    begin
        if exists(select 1 from t_Stock where FNumber=@FStockNumber)
    begin
      if exists(select 1 from t_Stock where FNumber=@FStockNumber and FSPGroupID>0)
        set @FExplanation =@FStockNumber +  '仓库开启仓位管理'
      select @FStockID = FItemID,@FStockName=FName from t_Stock where FNumber=@FStockNumber
    end
    else
    begin
    set @FExplanation = @FStockNumber +  '仓库编码不存在系统！'
    end
        
       if exists(select 1 from #Tmp11111 where FNumber=@FNumber)
         update #Tmp11111 set FQtyOrder= FQtyOrder+@FQtyOrder where FNumber=@FNumber
       else
         insert into #Tmp11111(FNumber,FQtyOrder,FOrderBillNo,FLogistics,FExplanation,FStockID,FStockName)values(@FNumber,@FQtyOrder,@FOrderBillNo,@FLogistics,'OK',@FStockID,@FStockName)
    end
    
	set @detailIndex=@detailIndex+1
    
end
set @detailqty=@detailqty+1
end
if @FExplanation<>'OK'
begin
  update #Tmp11111 set FExplanation=@FExplanation
end
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FExplanation)values('该单据没有成品商品')
end 
select t1.FStockID as 仓库ID,t1.FStockName as 仓库名称,0 as 仓位ID,t1.FExplanation as 说明,t2.FNumber as 物料编码,t2.FName as 物料名称,t2.FModel as 规格型号,t1.FLogistics as 物流单号,t1.FOrderBillNo as 销售订单号,convert(float,t1.FQtyOrder) as 订单数量,t3.FName as 单位,t2.FUnitID,t2.FItemID from #Tmp11111 t1 left join t_ICItem t2 on t1.FNumber=t2.FNumber left join t_Measureunit t3 on t2.FUnitID=t3.FItemID
drop table #Tmp11111
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
