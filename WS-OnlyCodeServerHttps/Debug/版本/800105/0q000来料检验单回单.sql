if (exists (select * from sys.objects where name = 'proc_MaterialsAllCheck'))
    drop proc proc_MaterialsAllCheck
go
create proc proc_MaterialsAllCheck
(
 @mainStr nvarchar(1000),--主表参数
 @detailStr1 varchar(max),--明细参数
 @detailStr2 varchar(max),
 @detailStr3 varchar(max),
 @detailStr4 varchar(max),
 @detailStr5 varchar(max)
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
declare @FUserID int,--供应商用户ID
        @FInterID varchar(20),     --单号id
        @FBillNo varchar(50),      -- 编号 
        @FAreaPS varchar(20),      --销售范围
        @FTranType varchar(20),    --单据类型 
        @Fdate varchar(50),       --日期 
        @FCustID varchar(20),     --购货单位id
        @FCurrencyID nvarchar(20),--币别id
        @FSettleID nvarchar(20),  --结算方式id
        @FSaleStyle varchar(20),  --销售方式id
        @FFetchStyle varchar(20), --交货方式id
        @FFetchAdd varchar(100),  --交货地点名
        @FCheckDate varchar(50),  --审核日期
        @FMangerID varchar(20),   --主管id
        @FDeptID varchar(20),     --部门id
        @FEmpID varchar(20),      --业务员id
        @FBillerID varchar(20),   --制单人id
        @FExchangeRate varchar(50),--汇率
        @FSettleDate varchar(50), --结算日期
        @FExplanation varchar(200),--摘要 
           @FOrderID varchar(50),--PDA单据编号
        @FPDAID varchar(50),  --PDA序列号 
          @FUserName varchar(128),--门店用户
        @FPDASource varchar(128),--单据来源 
        @FSelTranType varchar(20)   --源单类型ID
set @FUserID = dbo.getString(@mainStr,'|',1) --操作员(供应商用户ID)
set @FOrderID=dbo.getString(@mainStr,'|',2) --PDA单据编号   
set @FPDAID=dbo.getString(@mainStr,'|',3) --PDA序列号 

set @Fdate = convert(varchar(20),GETDATE(),23)
 declare @FEntryID int,       --明细序号
         @FEntryID_SRC int,
         @FSCBillInterID int,
         @FResult int,
         @FID_SRC int,
         @FSManagerID int,--质检员@FSManagerID=FSManagerID,@FFManagerID=FFManagerID,
         @FFManagerID int,--送检人
         @FCheckQty decimal(28,10), --检验数量
         @FSampleBreakQty decimal(28,10), --样品破坏数
         @FPassQty decimal(28,10), --合格数
        @detailqty int,               --明细参数的个数
        @detailcount int,             --明细每行数据的长度 
        @detailIndex int,            --明细每行下标
        @countindex int              --分隔符|的数量
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
	set @FID_SRC=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+1)    --原单ID(FInterID)
	set @FEntryID_SRC=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --明细ID(FEntryID)
	set @FSCBillInterID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --质检方案ID
	set @FResult=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)    --质检结果ID
    set @FSManagerID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5) --质检员ID
	set @FFManagerID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6) --送检人id
	set @FCheckQty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7) --检验数量
    set @FSampleBreakQty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8)  --样品破坏数
	set @FPassQty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)  --合格数
    exec proc_MaterialsAllCheck2 @FUserID,@FID_SRC,@FEntryID_SRC,@FSCBillInterID,@FResult,@FSManagerID,@FFManagerID,@FCheckQty,@FSampleBreakQty,@FPassQty
	set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex

	 
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
