if (exists (select * from sys.objects where name = 'proc_OtherIn'))
    drop proc proc_OtherIn
go
create proc proc_OtherIn
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
declare @FInterID varchar(20),     --单号id
        @FBillNo varchar(50),      -- 编号   
        @FROB varchar(20),         --红蓝字标识
        @Fdate varchar(50),       --日期  
        @FSupplyID varchar(20),   --购货单位id
        @FCurrencyID nvarchar(20),--币别id 
        @FCheckDate varchar(50),  --审核日期 
        @FFManagerID varchar(20), --验收
        @FSManagerID varchar(20), --保管
        @FManagerID varchar(20),  --主管id
        @FDeptID varchar(20),     --部门id
        @FEmpID varchar(20),      --业务员id
        @FBillerID varchar(20),   --制单人id 
        @FSettleDate varchar(50), --结算日期
        @FExplanation varchar(200),--摘要 
               @FOrderID varchar(50),--PDA单据编号
        @FPDAID varchar(50),  --PDA序列号
        @FBillTypeID varchar(20),   --入库类型ID
        @FHeadSelfA9740 varchar(20),--是否良品
        @FSelTranType varchar(20)  --源单类型
set @FBillerID = dbo.getString(@mainStr,'|',1) --操作员  
set @Fdate =dbo.getString(@mainStr,'|',2)      --日期 
set @FDeptID =dbo.getString(@mainStr,'|',3)   --部门id
set @FEmpID =dbo.getString(@mainStr,'|',4)    --业务员id
set @FManagerID =dbo.getString(@mainStr,'|',5) --负责人ID
set @FSupplyID =dbo.getString(@mainStr,'|',6)   --供应商ID
set @FExplanation =dbo.getString(@mainStr,'|',7)--摘要
set @FFManagerID=dbo.getString(@mainStr,'|',8) --验收
set @FSManagerID=dbo.getString(@mainStr,'|',9) --保管
set @FROB=dbo.getString(@mainStr,'|',10)         --蓝字红字
set @FBillTypeID=dbo.getString(@mainStr,'|',11) --入库类型ID
set @FSelTranType=dbo.getString(@mainStr,'|',12)  --源单类型 
if exists(select 1 from t_PDABarCodeType where FType=1)
begin
 set @FOrderID=dbo.getString(@mainStr,'|',13) --PDA单据编号   
set @FPDAID=dbo.getString(@mainStr,'|',14) --PDA序列号 
end
set @FHeadSelfA9740=dbo.getString(@mainStr,'|',15)  --是否良品 

set @Fdate = convert(varchar(20),GETDATE(),23)
exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --得到@FInterID
------------------------------------------------------------得到编号
if(@FROB='红字')
set @FROB=-1
else
set @FROB=1
set @FBillNo = ''
 exec proc_GetICBillNo 10, @FBillNo out 
-----------------------------------------------------------得到编号
set @FCurrencyID=1 --币别
set @FCheckDate=null --审核时间  
  declare @IsExist varchar(10), --是否存在
            @value varchar(10)--库存更新类型
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FROB,FHookStatus,Fdate,
FSupplyID,FCheckDate,FFManagerID,FSManagerID,FBillerID,FDeptID,FBillTypeID,FMultiCheckDate1,FMultiCheckDate2,
FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FVchInterID,FMultiCheckDate6,FPOOrdBillNo,FRelateBrID,FSelTranType,
FBrID,FManagerID,FEmpID,FExplanation,FManageType,FPrintCount,FHeadSelfA9740,FHeadSelfA9739) 
SELECT @FInterID,@FBillNo,'0',10,0,0,@value,@FROB,0,@Fdate,@FSupplyID,@FCheckDate,@FFManagerID,@FSManagerID,@FBillerID,@FDeptID,@FBillTypeID,Null,Null,Null,Null,Null,0,Null,'',0,@FSelTranType,0,@FManagerID,@FEmpID,@FExplanation,0,0,@FHeadSelfA9740,CONVERT(varchar(128),GETDATE(),20)
update ICStockBill set FUUID=newid() where FInterID=@FInterID

declare @FEntryID varchar(20),       --明细序号
        @FItemID varchar(20),        --商品id
        @FQty float,                --基本单位数量
        @FUnitID varchar(20),       --单位id
        @Fauxqty float,            --上传的数量 
        @Fauxprice float,          -- 单价
        @Famount float,          --金额
        @FTaxAmount float,     -- 
        
        @FPlanPrice float,     --基本单位计划单价
        @FAuxPlanPrice float, --单位计划单价
        @FPlanAmount float,   --计划价金额     
        @FDiscountRate float,  --折扣率
        @FDiscountAmount float,--折扣额(含税单价*数量*折扣率)   
        @FDCStockID varchar(20), --仓库id
        @FCoefficient varchar(20),   --换算率
        @FBatchNo varchar(50),     --批号
        @FDCSPID varchar (20),     --仓位ID
          @FSecCoefficient float, --辅助单位换算率
        @FSecQty decimal(28,10),   --辅助单位数量
          @FSecUnitID  varchar(50), 
            @FKFDate varchar(50),    --生产日期
        @FBarCode_Assemble varchar(128),--装箱条码
          @FBarCode varchar(128),
        @FKFPeriod int,       --保质期
        @FPeriodDate varchar(50),--有效期
        @detailqty int,               --明细参数的个数
        @detailcount int,             --明细每行数据的长度 
        @detailIndex int,            --明细每行下标
        @countindex int              --分隔符|的数量
       set @detailqty=0        
       set @detailcount=11           
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
	set @FUnitID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --单位id 
	set @Fauxprice=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --单价
	set @Fauxqty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)       --数量
	set @FDiscountRate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5)  --折扣率 
	set @FDCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6)     --仓库id
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7)       --批号
	set @FDCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8)        --仓位ID
		set @FKFDate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)  
	set @FKFPeriod=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+10)
	set @FBarCode_Assemble =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+11)--装箱条码 如果不是装箱条码 该地方为''
	set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex 
		 if(SUBSTRING(@FBarCode_Assemble,1,2)='ZZ')
       begin       
         declare My_cursor cursor dynamic --scroll表示可以向前或向后移动    dynamic：表示可写也可读
         for 
         select  t2.FBarCode,t2.FItemID,t2.FBatchNo,t2.FKFDate,t2.FKFPeriod,t2.FQty from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t2.FInterIDAssemble =  t1.FInterID where t1.FBillNo=@FBarCode_Assemble
         open My_cursor      
         fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty --游标停在第一条记录前面，第一次执行，测试有没有记录存在,
         while(@@FETCH_STATUS = 0) --取数据 0表示成功执行FETCH语句  -1 表示FETCH语句失败，例如移动行指针使其超出了结果集 -2 表示被提取的行不存在。
         begin  
	 if(@FROB=-1)
         begin
         set @Fauxqty=-@Fauxqty
         end
          if(@FKFDate is null or @FKFDate='')
		set @FPeriodDate=null
		else
		begin
			select @FPeriodDate = DATEADD(day,@FKFPeriod,@FKFDate) 
		end  
	select @FCoefficient=isnull(FCoefficient,1) from t_MeasureUnit where FMeasureUnitID=@FUnitID --单位换算率
	select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --基本单位数量 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --单位计划单价
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --计划单价金额 
	set @Famount=@Fauxqty*@Fauxprice
	 
          --物料辅助单位
       select @FSecUnitID=FSecUnitID,@FSecCoefficient=FSecCoefficient from t_ICItem where FItemID=@FItemID
      if(@FSecCoefficient<>0) --这里判断上传的是辅助单位还是基本单位 如果成立说明上传的是辅助单位
      begin
      set @FSecQty = @FQty/@FSecCoefficient
      end
      else
      begin
      set @FSecQty = 0
      end
      
		  if(@value=1)
		begin
		  select @IsExist=COUNT(1) from ICInventory where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  if(@IsExist=0)
			begin
			INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
			SELECT '0',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,@FKFPeriod,@FKFDate,@FQty,@FSecQty
			end
		  else
			begin
			update ICInventory set FQty=FQty+@FQty,FSecQty=FSecQty+@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID  and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  end    
		end
			if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
            select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
             if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID)
		       begin
		         update ICStockBillEntry set FSecQty=isnull(FSecQty,0)+@FSecQty,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FAmount=ISNULL(FAmount,0)+@Famount where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID
		       end
		        else
		        begin
INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FAuxPropID,FBatchNo,FQtyMust,FQty,FUnitID,FAuxQtyMust,Fauxqty,FSecCoefficient,FSecQty,
FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FDCStockID,FDCSPID,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,
FSourceEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FCostPercentage,FDeliveryNoticeFID,FDeliveryNoticeEntryID,FChkPassItem)  
SELECT @FInterID,@FEntryID,'0','','',@FItemID,0,@FBatchNo,0,@FQty,@FUnitID,0,@Fauxqty,@FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,@Fauxprice,@Famount,'',@FKFDate,@FKFPeriod,@FPeriodDate,@FDCStockID,@FDCSPID,0,'',0,0,0,'',0,0,14036,'',0,0,0,1058 
end
 	 fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty --再次将游标停在第一条记录前面，第一次执行，测试有没有记录存在,估测也是为@@FETCH_STATUS赋值
				 end  
				 close my_cursor
				 deallocate my_cursor  
       end  
     else
     begin
               if(@FKFDate is null or @FKFDate='')
		set @FPeriodDate=null
		else
		begin
			select @FPeriodDate = DATEADD(day,@FKFPeriod,@FKFDate) 
		end  
	select @FCoefficient=isnull(FCoefficient,1) from t_MeasureUnit where FMeasureUnitID=@FUnitID --单位换算率
	select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --基本单位数量 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --单位计划单价
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --计划单价金额 
	set @Famount=@Fauxqty*@Fauxprice
	 
          --物料辅助单位
       select @FSecUnitID=FSecUnitID,@FSecCoefficient=FSecCoefficient from t_ICItem where FItemID=@FItemID
      if(@FSecCoefficient<>0) --这里判断上传的是辅助单位还是基本单位 如果成立说明上传的是辅助单位
      begin
      set @FSecQty = @FQty/@FSecCoefficient
      end
      else
      begin
      set @FSecQty = 0
      end
      
		  if(@value=1)
		begin
		  select @IsExist=COUNT(1) from ICInventory where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  if(@IsExist=0)
			begin
			INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
			SELECT '0',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,@FKFPeriod,@FKFDate,@FQty,@FSecQty
			end
		  else
			begin
			update ICInventory set FQty=FQty+@FQty,FSecQty=FSecQty+@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID  and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  end    
		end
			if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
            select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
             if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID)
		       begin
		         update ICStockBillEntry set FSecQty=isnull(FSecQty,0)+@FSecQty,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FAmount=ISNULL(FAmount,0)+@Famount where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID
		       end
		        else
		        begin
INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FAuxPropID,FBatchNo,FQtyMust,FQty,FUnitID,FAuxQtyMust,Fauxqty,FSecCoefficient,FSecQty,
FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FDCStockID,FDCSPID,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,
FSourceEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FCostPercentage,FDeliveryNoticeFID,FDeliveryNoticeEntryID,FChkPassItem)  
SELECT @FInterID,@FEntryID,'0','','',@FItemID,0,@FBatchNo,0,@FQty,@FUnitID,0,@Fauxqty,@FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,@Fauxprice,@Famount,'',@FKFDate,@FKFPeriod,@FPeriodDate,@FDCStockID,@FDCSPID,0,'',0,0,0,'',0,0,14036,'',0,0,0,1058 
end
end
end
set @detailqty=@detailqty+1
end
EXEC p_UpdateBillRelateData 10,@FInterID,'ICStockBill','ICStockBillEntry' 
  if exists(select 1 from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0 )
 begin
 select @FSManagerID=isnull(t2.FEmpID,0) from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0
  if(CONVERT(int, @FSManagerID)=0)
  begin
      if exists(select 1 from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID)
   begin
  ---报检人
  select @FSManagerID = t2.FItemID from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID
  end
  end
 end
 if(CONVERT(int, @FSManagerID)>0)
 begin
 update ICStockBill set FSManagerID=@FSManagerID,FFManagerID=@FSManagerID where FInterID=@FInterID
 end

if exists(select 1 from t_PDABarCodeType where FType=1)
begin
insert into t_PDABarCodeSign_In(FInterID,FBarCode,FQty,FBillNo,FRemark,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before,FStatus,FDateUpLoad,FStockID,FStockPlaceID)select @FInterID,a.FBarCode,a.FQty,@FBillNo,'其他入库单',b.FInterIDIn,b.FDateInStore,b.FUserInStore,1,convert(varchar(20),GETDATE(),20),a.FStockID,a.FStockPlaceID from a_DetailsTable  a inner join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID

update t_PDABarCodeSign  set FStockID=t.FStockID,FStockPlaceID=t.FStockPlaceID,FInterIDIn=@FInterID,FDateInStore=@fdate,FIsInStore='已入库',FUserInStore=@FBillerID,FRemark1='其他入库单',FDateUpLoad = convert(varchar(20),GETDATE(),20)  from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
end
if not exists(  select   1  from ICStockBillEntry where FInterID=@FInterID)
begin
    delete  ICStockBill where FInterID=@FInterID
	goto error1
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
