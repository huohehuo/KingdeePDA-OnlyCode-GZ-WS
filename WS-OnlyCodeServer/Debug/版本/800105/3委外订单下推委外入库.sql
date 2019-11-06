if (exists (select * from sys.objects where name = 'proc_PushOutsourceIn'))
    drop proc proc_PushOutsourceIn
go
create proc proc_PushOutsourceIn
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
        @FCurrencyID varchar(20),--币别id 
        @FSaleStyle varchar(20),  --销售方式id 
        @FPOStyle varchar(20),    --采购方式
        @FFetchAdd varchar(100),  --交货地点名
        @FCheckDate varchar(50),  --审核日期 
        @FFManagerID varchar(20), --发货
        @FSManagerID varchar(20), --保管
        @FManagerID varchar(20),  --主管id
        @FDeptID varchar(20),     --部门id
        @FEmpID varchar(20),      --业务员id
        @FBillerID varchar(20),   --制单人id 
        @FSettleDate varchar(50), --结算日期
        @FExplanation varchar(200),--摘要 
        @FMarketingStyle varchar(20),--销售业务类型
        @FCussentAcctID varchar(20), --往来科目ID
        @FPurposeID varchar(20), --委外类型
               @FOrderID varchar(50),--PDA单据编号
        @FPDAID varchar(50),  --PDA序列号 
            @FExchangeRate float,
        @FSelTranType varchar(20)  --源单类型
set @FBillerID = dbo.getString(@mainStr,'|',1) --操作员  
set @Fdate =dbo.getString(@mainStr,'|',2)      --日期 
set @FSupplyID =dbo.getString(@mainStr,'|',3)   --购货单位
--set @FExplanation =dbo.getString(@mainStr,'|',10)--摘要
set @FFManagerID=dbo.getString(@mainStr,'|',4) --验收
set @FSManagerID=dbo.getString(@mainStr,'|',5) --保管
set @FROB=dbo.getString(@mainStr,'|',6)         --蓝字红字
set @FPurposeID=dbo.getString(@mainStr,'|',7)  --委外类型id  
set @FExplanation =dbo.getString(@mainStr,'|',8)--摘要 
if exists(select 1 from t_PDABarCodeType where FType=1)
begin
 set @FOrderID=dbo.getString(@mainStr,'|',9) --PDA单据编号   
set @FPDAID=dbo.getString(@mainStr,'|',10) --PDA序列号 
end

exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --得到@FInterID
------------------------------------------------------------得到编号
    set @FBillNo ='' 
 exec proc_GetICBillNo 5, @FBillNo out 
-----------------------------------------------------------得到编号
set @FCurrencyID=1 --币别
set @FCheckDate=null --审核时间  
set @FExplanation='' --备注

  declare @IsExist varchar(10), --是否存在
            @value varchar(10)--库存更新类型
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FROB,FHookStatus,Fdate,FSupplyID,FPurposeID,FCheckDate,
FFManagerID,FSManagerID,FBillerID,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FVchInterID,FMultiCheckDate6,
FSelTranType,FCussentAcctID,FManageType,FSettleDate,FRelateBrID,FPOOrdBillNo,FBrID,FPrintCount,FPayCondition,FHeadSelfA0537,FHeadSelfA0538,FHeadSelfA0539) 
SELECT @FInterID,@FBillNo,'0',5,0,0,@value,1,0,@Fdate,@FSupplyID,@FPurposeID,Null,@FFManagerID,@FSManagerID,@FBillerID,Null,Null,Null,Null,Null,0,Null,1007105,0,0,@FSettleDate,0,'',0,0,1001,CONVERT (varchar(50),GETDATE(),20),990136,'PDA制单'

update ICStockBill set FUUID=newid() where FInterID=@FInterID

declare @FEntryID varchar(20),       --新的明细序号
        @FSourceEntryID varchar(20), --下推单据的明细id
        @FSourceInterId varchar(20), --下推单据的FInterID
        @FSourceBillNo varchar(20),  --下推的单据的单据编号
        @FOrderBillNo varchar(20),
        @FOrderEntryID varchar(20),
        @FOrderInterID varchar(20),
        @FItemID varchar(20),        --商品id
        @FQty float,                --基本单位数量
        @FQtyMust float,            --基本单位可验数量
        @FAuxQtyMust float,        --单位可验收数量
        @FUnitID varchar(20),       --单位id
        @Fauxqty float,            --上传的数量 
        @Fauxprice float,          -- 单价
        @Famount float,          --金额 
          @FTaxRate float,        --税率
        @FTaxAmount float,     -- 税额
        @FProcessPrice float, --单位成本
        @FProcessCost float,  --单位成本金额
        
        @FPlanPrice float,     --基本单位计划单价
        @FAuxPlanPrice float, --单位计划单价
        @FPlanAmount float,   --计划价金额     
        @FDiscountRate float,  --折扣率
        @FDiscountAmount float,--折扣额(含税单价*数量*折扣率)   
        @FDCStockID varchar(20), --仓库id
        @FDCSPID varchar(20), --仓位id
        @FBatchNo varchar(50),--批号
        @FCoefficient varchar(20),   --换算率
        @FNote varchar(255),--备注
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
       set @detailcount=12           
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
	set @Fauxqty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)    --数量  
	set @FDCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5) --仓库id
	set @FDCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6) --仓位id
	set @FSourceEntryID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7) --下推的明细id
	set @FSourceInterId=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8) --下推的明FInterID
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9) 
	 set @FKFDate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+10)  
	set @FKFPeriod=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+11)
	set @FBarCode_Assemble =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+12)--装箱条码 如果不是装箱条码 该地方为''
		set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex
	   if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode_Assemble)
       begin       
         declare My_cursor cursor dynamic --scroll表示可以向前或向后移动    dynamic：表示可写也可读
         for 
         select  t2.FBarCode,t2.FItemID,t2.FBatchNo,t2.FKFDate,t2.FKFPeriod,t2.FQty from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t2.FInterIDAssemble =  t1.FInterID where t1.FBillNo=@FBarCode_Assemble
         open My_cursor      
         fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty --游标停在第一条记录前面，第一次执行，测试有没有记录存在,
         while(@@FETCH_STATUS = 0) --取数据 0表示成功执行FETCH语句  -1 表示FETCH语句失败，例如移动行指针使其超出了结果集 -2 表示被提取的行不存在。
         begin  
	select @FExchangeRate=isnull(FExchangeRate,1),@FSourceBillNo=FBillNo,@FSupplyID =FSupplyID from ICSubContract where FInterID=@FSourceInterId --下推的单据编号
	select @FAuxQtyMust = FAuxQty-FAuxCommitQty from ICSubContractEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
		set @Fauxprice = @Fauxprice*@FExchangeRate
	set @FOrderBillNo = @FSourceBillNo
	set @FOrderEntryID = @FSourceEntryID
	set @FOrderInterID = @FSourceInterId
	select @FSourceEntryID=FDetailID  from ICSubContractEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
	select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --单位换算率
	set @FQtyMust=@FAuxQtyMust*@FCoefficient --基本单位可验收的数量  
	 select @FPlanPrice=isnull(FPlanPrice,0)*@FExchangeRate,@FTaxRate=isnull(FTaxRate,0) from t_ICItem where   FItemID=@FItemID  
	
	 set   @FProcessPrice = @Fauxprice*@FCoefficient
	 set  @FProcessCost = @FProcessPrice*@Fauxqty
	set @FQty=@Fauxqty*@FCoefficient                  --基本单位数量 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --单位计划单价
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --计划单价金额 
	set @Famount=@Fauxqty*@Fauxprice 
	set @FTaxAmount = @Famount*(1+@FTaxRate) 
      if(@value=1)
		begin
		  select @IsExist=COUNT(1) from ICInventory where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  if(@IsExist=0)
			begin
			INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
			SELECT '0',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,@FKFPeriod,@FKFDate,@FQty,0
			end
		  else
			begin
			update ICInventory set FQty=FQty+@FQty,FSecQty=FSecQty+0 where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID  and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  end    
		end
		if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
		      select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
		      if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID)
		       begin
		         update ICStockBillEntry set FSecQty=isnull(FSecQty,0)+0,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FAmount=ISNULL(FAmount,0) where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID
		       end
		        else
		        begin
INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FAuxPropID,FBatchNo,FQtyMust,FQty,FUnitID,FAuxQtyMust,Fauxqty,
FSecCoefficient,FSecQty,FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FDCStockID,FDCSPID,FMaterialCostPrice,FMaterialCost,
FProcessPrice,FProcessCost,FTaxRate,FTaxAmount,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FOrderBillNo,FOrderInterID,
FOrderEntryID,FICMOInterID,FPPBomEntryID,FPlanMode,FMTONo,FChkPassItem,FDeliveryNoticeFID,FDeliveryNoticeEntryID,FProcessTaxPrice,FProcessTaxCost)  
SELECT @FInterID,@FEntryID,'0','','',@FItemID,0,@FBatchNo,@FQtyMust,@FQty,@FUnitID,@FAuxQtyMust,@Fauxqty,0,0,@FAuxPlanPrice,@FPlanAmount,@Fauxprice,@Famount,
'',@FKFDate,@FKFPeriod,@FPeriodDate,@FDCStockID,@FDCSPID,0,0,@FProcessPrice,@FProcessCost,@FTaxRate,@FTaxAmount,0,@FSourceBillNo,1007105,@FSourceInterId,@FSourceEntryID,
@FOrderBillNo,@FOrderInterID,@FOrderEntryID,0,0,14036,'',1058,0,0,@Fauxprice*(1+@FTaxRate) ,@Famount*(1+@FTaxRate) 
               	end
 	 fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty --再次将游标停在第一条记录前面，第一次执行，测试有没有记录存在,估测也是为@@FETCH_STATUS赋值
				 end  
				 close my_cursor
				 deallocate my_cursor  
       end  
     else
     begin
     	select @FExchangeRate=isnull(FExchangeRate,1),@FSourceBillNo=FBillNo,@FSupplyID =FSupplyID from ICSubContract where FInterID=@FSourceInterId --下推的单据编号
	select @FAuxQtyMust = FAuxQty-FAuxCommitQty from ICSubContractEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
		set @Fauxprice = @Fauxprice*@FExchangeRate
	set @FOrderBillNo = @FSourceBillNo
	set @FOrderEntryID = @FSourceEntryID
	set @FOrderInterID = @FSourceInterId
		select @FSourceEntryID=FDetailID  from ICSubContractEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
	select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --单位换算率
	set @FQtyMust=@FAuxQtyMust*@FCoefficient --基本单位可验收的数量  
	 select @FPlanPrice=isnull(FPlanPrice,0)*@FExchangeRate,@FTaxRate=isnull(FTaxRate,0) from t_ICItem where   FItemID=@FItemID  
	
	 set   @FProcessPrice = @Fauxprice*@FCoefficient
	 set  @FProcessCost = @FProcessPrice*@Fauxqty
	set @FQty=@Fauxqty*@FCoefficient                  --基本单位数量 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --单位计划单价
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --计划单价金额 
	set @Famount=@Fauxqty*@Fauxprice 
	set @FTaxAmount = @Famount*(1+@FTaxRate) 
      if(@value=1)
		begin
		  select @IsExist=COUNT(1) from ICInventory where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  if(@IsExist=0)
			begin
			INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
			SELECT '0',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,@FKFPeriod,@FKFDate,@FQty,0
			end
		  else
			begin
			update ICInventory set FQty=FQty+@FQty,FSecQty=FSecQty+0 where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID  and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  end    
		end
		if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
		      select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
		      if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID)
		       begin
		         update ICStockBillEntry set FSecQty=isnull(FSecQty,0)+0,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FAmount=ISNULL(FAmount,0) where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID
		       end
		        else
		        begin
INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FAuxPropID,FBatchNo,FQtyMust,FQty,FUnitID,FAuxQtyMust,Fauxqty,
FSecCoefficient,FSecQty,FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FDCStockID,FDCSPID,FMaterialCostPrice,FMaterialCost,
FProcessPrice,FProcessCost,FTaxRate,FTaxAmount,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FOrderBillNo,FOrderInterID,
FOrderEntryID,FICMOInterID,FPPBomEntryID,FPlanMode,FMTONo,FChkPassItem,FDeliveryNoticeFID,FDeliveryNoticeEntryID,FProcessTaxPrice,FProcessTaxCost)  
SELECT @FInterID,@FEntryID,'0','','',@FItemID,0,@FBatchNo,@FQtyMust,@FQty,@FUnitID,@FAuxQtyMust,@Fauxqty,0,0,@FAuxPlanPrice,@FPlanAmount,@Fauxprice,@Famount,
'',@FKFDate,@FKFPeriod,@FPeriodDate,@FDCStockID,@FDCSPID,0,0,@FProcessPrice,@FProcessCost,@FTaxRate,@FTaxAmount,0,@FSourceBillNo,1007105,@FSourceInterId,@FSourceEntryID,
@FOrderBillNo,@FOrderInterID,@FOrderEntryID,0,0,14036,'',1058,0,0,@Fauxprice*(1+@FTaxRate) ,@Famount*(1+@FTaxRate) 
               	end
           end
end
set @detailqty=@detailqty+1
end
EXEC p_UpdateBillRelateData 5,@FInterID,'ICStockBill','ICStockBillEntry' 
select @FPurposeID=FInvStyle from  ICSubContract where FInterID=@FSourceInterId 
 update ICStockBill set FSupplyID =@FSupplyID,FPurposeID=@FPurposeID where FInterID=@FInterID
 
 if exists(select 1 from t_PDABarCodeType where FType=1)
begin
insert into t_PDABarCodeSign_In(FInterID,FBarCode,FQty,FBillNo,FRemark,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before,FStatus,FDateUpLoad,FStockID,FStockPlaceID)select @FInterID,a.FBarCode,a.FQty,@FBillNo,'委外订单下推委外入库单',b.FInterIDIn,b.FDateInStore,b.FUserInStore,1,convert(varchar(20),GETDATE(),20),a.FStockID,a.FStockPlaceID from a_DetailsTable  a inner join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID

update t_PDABarCodeSign  set FStockID=t.FStockID,FStockPlaceID=t.FStockPlaceID,FInterIDIn=@FInterID,FDateInStore=@fdate,FIsInStore='已入库',FUserInStore=@FBillerID,FRemark1='委外订单下推委外入库单',FDateUpLoad = convert(varchar(20),GETDATE(),20)  from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
end
 
set nocount on
declare @fcheck_fail int
declare @fsrccommitfield_prevalue decimal(28,13)
declare @fsrccommitfield_endvalue decimal(28,13)
declare @maxorder int 
update src set @fsrccommitfield_prevalue= isnull(src.fcommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fqty,
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
  src.fcommitqty= case when src.finlowlimitqty < 0 then -1 * @fsrccommitfield_endvalue else @fsrccommitfield_endvalue end,
     src.fauxcommitqty= case when src.finlowlimitqty < 0 then -1 * (@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)) else @fsrccommitfield_endvalue/cast(t1.fcoefficient as float) end
 from icsubcontractentry src 
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fdetailid = dest.fsourceentryid
 inner join t_measureunit t1 on src.funitid=t1.fitemid
 
  
update src set @fsrccommitfield_prevalue= isnull(src.fseccommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fsecqty,
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
  src.fseccommitqty= case when src.fsecqty < 0 then -1 * @fsrccommitfield_endvalue else @fsrccommitfield_endvalue end
 from icsubcontractentry src 
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fsecqty) as fsecqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fdetailid = dest.fsourceentryid

 
 UPDATE T1 SET T1.FStatus=
CASE WHEN NOT EXISTS(SELECT 1 FROM ICSubContractEntry WHERE FInterID=T1.FInterID AND FCommitQty<>0) THEN 1 
WHEN NOT EXISTS (SELECT 1 FROM ICSubContractEntry WHERE FInterID=T1.FInterID AND FInLowLimitQty>FAuxCommitQty) THEN 3 
ELSE 2 END,
T1.FCloserID=CASE WHEN NOT EXISTS (SELECT 1 FROM ICSubContractEntry WHERE FInterID=T1.FInterID AND FInLowLimitQty>FAuxCommitQty) THEN @FBillerID ELSE 0 END ,
T1.FCLOSED=CASE WHEN NOT EXISTS (SELECT 1 FROM ICSubContractEntry WHERE FInterID=T1.FInterID AND FInLowLimitQty>FAuxCommitQty) THEN 1 ELSE 0 END 
FROM ICSubContract T1, ICStockBillEntry T2 
WHERE t2.FSourceTranType=1007105 AND T1.FInterID = T2.FSourceInterID
 AND T2.FInterID=@FInterID
UPDATE T1 SET T1.FClosedDate=CASE WHEN ISNULL(t1.FClosed,0)=1 THEN GETDATE() ELSE NULL END
FROM ICSubContract T1, ICStockBillEntry T2 
WHERE T1.FInterID = T2.FSourceInterID
 AND T2.FInterID=@FInterID
 IF EXISTS ( SELECT 1 FROM syscolumns WHERE name = 'FConnectFlag'  AND id = object_id('ICSubContract') )  AND EXISTS ( SELECT 1 FROM syscolumns WHERE name='FSourceInterID'  AND id = object_id('ICStockBillEntry') ) EXEC('UPDATE t3 SET t3.FConnectFlag=t3.FConnectFlag+1
FROM ICStockBill t1 INNER JOIN ICStockBillEntry t2 ON t1.FInterID=t2.FInterID
INNER JOIN ICSubContract t3 ON t3.FTranType=t2.FSourceTranType AND t3.FInterID=t2.FSourceInterID
WHERE t1.FTranType=5 AND t1.FInterID=@FInterID AND t2.FSourceInterID>0 ' )
 ELSE IF EXISTS ( SELECT 1 FROM syscolumns WHERE name = 'FChildren'  AND id = object_id('ICSubContract') )  AND EXISTS ( SELECT 1 FROM syscolumns WHERE name='FSourceInterID'  AND id = object_id('ICStockBillEntry') ) EXEC('UPDATE t3 SET t3.FChildren=t3.FChildren+1
FROM ICStockBill t1 INNER JOIN ICStockBillEntry t2 ON t1.FInterID=t2.FInterID
INNER JOIN ICSubContract t3 ON t3.FTranType=t2.FSourceTranType AND t3.FInterID=t2.FSourceInterID
WHERE t1.FTranType=5 AND t1.FInterID=@FInterID AND t2.FSourceInterID>0 ' )

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
