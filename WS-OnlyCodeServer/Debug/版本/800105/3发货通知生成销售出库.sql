if (exists (select * from sys.objects where name = 'proc_DeliveryCheck'))
    drop proc proc_DeliveryCheck
go
create proc proc_DeliveryCheck
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
        @FSettleDate varchar(50), --付款日期
        @FExplanation varchar(200),--摘要 
        @FMarketingStyle varchar(20),--销售业务类型 
          @FOrderID varchar(50),--PDA单据编号
        @FPDAID varchar(50),  --PDA序列号 
        @FExchangeRate float,
        @FSelTranType varchar(20)  --源单类型
set @FBillerID = dbo.getString(@mainStr,'|',1) --操作员  
set @Fdate =dbo.getString(@mainStr,'|',2)      --日期 
set @FSettleDate=dbo.getString(@mainStr,'|',3) --付款日期
set @FSupplyID =dbo.getString(@mainStr,'|',4)   --购货单位id 
set @FSaleStyle =dbo.getString(@mainStr,'|',5) --销售方式   
set @FDeptID =dbo.getString(@mainStr,'|',6)   --部门id
set @FEmpID =dbo.getString(@mainStr,'|',7)    --业务员id
set @FManagerID =dbo.getString(@mainStr,'|',8) --主管id
set @FFManagerID=dbo.getString(@mainStr,'|',9) --发货
set @FSManagerID=dbo.getString(@mainStr,'|',10) --保管   

if exists(select 1 from t_PDABarCodeType where FType=1)
begin
 set @FOrderID=dbo.getString(@mainStr,'|',11) --PDA单据编号   
set @FPDAID=dbo.getString(@mainStr,'|',12) --PDA序列号 
end

set @Fdate = convert(varchar(20),GETDATE(),23)
exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --得到@FInterID
------------------------------------------------------------得到编号
set @FBillNo = ''
 exec proc_GetICBillNo 21, @FBillNo out 
-----------------------------------------------------------得到编号
set @FCurrencyID=1 --币别
set @FCheckDate=null --审核时间  
set @FExplanation='' --备注
  declare @IsExist varchar(10), --是否存在
            @value varchar(10)--库存更新类型
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
    set @FManagerID = 0 --主管设置为空  
        if exists(select 1 from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID)
begin
  ---报检人
  select @FFManagerID = t2.FItemID from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID
end
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FROB,FHookStatus,Fdate,FSupplyID,FSaleStyle,FCheckDate,
FConfirmDate,FFManagerID,FSManagerID,FBillerID,FConfirmer,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FPOOrdBillNo,
FMultiCheckDate6,FRelateBrID,FOrgBillInterID,FMarketingStyle,FSelTranType,FPrintCount,FBrID,FFetchAdd,FExplanation,FConfirmMem,FDeptID,FEmpID,FManagerID,
FVIPCardID,FReceiver,FVIPScore,FHolisticDiscountRate,FPOSName,FWorkShiftId,FLSSrcInterID,FManageType,FPayCondition,FSettleDate,FInvoiceStatus,FConsignee,
FHeadSelfB0156,FHeadSelfB0157,FHeadSelfB0159,FHeadSelfB0160,FHeadSelfB0161,FHeadSelfB0162,FHeadSelfB0163,FHeadSelfB0164,FHeadSelfB0165,
FHeadSelfB0166,FHeadSelfB0167,FHeadSelfB0168) 
SELECT @FInterID,@FBillNo,'0',21,0,0,@value,1,0,@Fdate,@FSupplyID,@FSaleStyle,Null,Null,@FFManagerID,@FSManagerID,@FBillerID,0,Null,Null,Null,Null,Null,'',
Null,0,0,12530,83,0,0,'','','',@FDeptID,@FEmpID,@FManagerID,0,'',0,0,'',0,0,0,0,@FSettleDate,'',0,CONVERT(varchar(128),GETDATE(),20),'国内线上经销商','13510235785','','','',990139,'',Null,'',Null,'PDA制单'
update ICStockBill set FUUID=newid() where FInterID=@FInterID

declare @FEntryID varchar(20),       --新的明细序号
        @FSourceEntryID varchar(20), --下推单据的明细id
        @FSourceInterId varchar(20), --下推单据的FInterID
        @FSourceBillNo varchar(20),  --下推的单据的单据编号
        @FSEOutInterID varchar(20),
        @FSEOutBillNo varchar(20),
        @FSEOutEntryID varchar(20),
        
        @FItemID varchar(20),        --商品id
        @FQty float,                --基本单位数量
        @FQtyMust float,            --基本单位可验数量
        @FAuxQtyMust float,        --单位可验收数量
        @FUnitID varchar(20),       --单位id
        @Fauxqty float,            --上传的数量 
        @FConsignPrice float,      -- 单价
        @FConsignAmount float,          --金额
        @FTaxAmount float,     -- 
            @FKFDate varchar(50),    --生产日期
        @FBarCode_Assemble varchar(128),--装箱条码
          @FBarCode varchar(128),
        @FKFPeriod int,       --保质期
        @FPeriodDate varchar(50),--有效期
        @FPlanPrice float,     --基本单位计划单价
        @FAuxPlanPrice float, --单位计划单价
        @FPlanAmount float,   --计划价金额     
        @FDiscountRate float,  --折扣率
        @FDiscountAmount float,--折扣额(含税单价*数量*折扣率)   
        @FDCStockID varchar(20), --仓库id
        @FDCSPID varchar(20),    --仓位id
        @FBatchNo varchar(50),     --批号
        @FCoefficient varchar(20),   --换算率
          @FOrderEntryID varchar(20),--订单序号id
        @FOrderInterID varchar(20),--订单finterID
        @FOrderBillNo varchar(50), --订单编号
          @FSecCoefficient float, --辅助单位换算率
        @FSecQty decimal(28,10),   --辅助单位数量
        @FEntrySelfB0176 varchar(800),--成品系列
          @FSecUnitID  varchar(50), 
          @FEntrySelfB0175 varchar(255),--快递单号
          @FEntrySelfB0173 decimal(28,10),--本位币
          @FEntrySelfB0174  decimal(28,10),--本位币
          @FOLOrderBillNo varchar(128),--125
        @detailqty int,               --明细参数的个数
        @detailcount int,             --明细每行数据的长度 
        @detailIndex int,            --明细每行下标
        @countindex int              --分隔符|的数量
       set @detailqty=0        
       set @detailcount=13           
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
	set @FConsignPrice=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --单价
	set @Fauxqty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)    --数量  
	set @FDCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5) --仓库id
	set @FDCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6) --仓位id
	set @FSourceEntryID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7) --下推的明细id
	set @FSourceInterId=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8) --下推的明FInterID
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9) --下推的明FInterID
	  set @FKFDate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+10)  
	set @FKFPeriod=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+11)
	set @FBarCode_Assemble =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+12)--装箱条码 如果不是装箱条码 该地方为''
	set @FEntrySelfB0175=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+13) --快递单号
		set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex
     select @FEntrySelfB0176=t_2043.FName from t_ICItem t4 left join t_Item_2043  t_2043  on t4.F_109=t_2043.FItemID where t4.FItemID=@FItemID
 		   if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode_Assemble)
       begin       
         declare My_cursor cursor dynamic --scroll表示可以向前或向后移动    dynamic：表示可写也可读
         for 
         select  t2.FBarCode,t2.FItemID,t2.FBatchNo,t2.FKFDate,t2.FKFPeriod,t2.FQty,t2.FStockID,t2.FStockPlaceID from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t2.FInterIDAssemble =  t1.FInterID where t1.FBillNo=@FBarCode_Assemble
         open My_cursor      
         fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty,@FDCStockID,@FDCSPID --游标停在第一条记录前面，第一次执行，测试有没有记录存在,
         while(@@FETCH_STATUS = 0) --取数据 0表示成功执行FETCH语句  -1 表示FETCH语句失败，例如移动行指针使其超出了结果集 -2 表示被提取的行不存在。
         begin  
	select @FExchangeRate=isnull(FExchangeRate,1),@FSourceBillNo=FBillNo,@FSEOutBillNo=FBillNo from SEOutStock where FInterID=@FSourceInterId --下推的单据编号
	set @FConsignPrice = @FConsignPrice * @FExchangeRate
	select @FEntrySelfB0173 = FAuxPrice,@FAuxQtyMust = FAuxQty-FAuxCommitQty,@FOrderEntryID=FOrderEntryID,@FOrderInterID=FOrderInterID,@FOrderBillNo=FOrderBillNo,@FSEOutInterID=FInterID,@FSEOutEntryID=FEntryID from SEOutStockEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
	set @FEntrySelfB0174=@FEntrySelfB0173*@Fauxqty
	set @FOLOrderBillNo=@FOrderBillNo
	select @FCoefficient=isnull(FCoefficient,1) from t_MeasureUnit where FMeasureUnitID=@FUnitID --单位换算率
	set @FQtyMust=@FAuxQtyMust*@FCoefficient --基本单位可验收的数量 
	select @FPlanPrice=isnull(FPlanPrice,0)* @FExchangeRate from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --基本单位数量 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --单位计划单价
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --计划单价金额 
	set @FConsignAmount=@Fauxqty*@FConsignPrice 
       if(@FKFDate is null or @FKFDate='')
		set @FPeriodDate=null
		else
		begin
			select @FPeriodDate = DATEADD(day,@FKFPeriod,@FKFDate) 
		end  
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
			SELECT '0',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,@FKFPeriod,@FKFDate,-@FQty,-@FSecQty
			end
		  else
			begin
			update ICInventory set FQty=FQty-@FQty,FSecQty=FSecQty-@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  end    
		end
		if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
         select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
           if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID and isnull(FEntrySelfB0175,'') = @FEntrySelfB0175)
		       begin
		         update ICStockBillEntry set FSecQty=isnull(FSecQty,0)+@FSecQty,FConsignAmount=isnull(FConsignAmount,0)+@FConsignAmount,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FEntrySelfB0174=FEntrySelfB0174+@FEntrySelfB0174 where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID and isnull(FEntrySelfB0175,'') = @FEntrySelfB0175
		       end
		        else
		        begin
 INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FOLOrderBillNo,
 FAuxPropID,FBatchNo,FQty,FUnitID,FAuxQtyMust,Fauxqty,FSecCoefficient,FSecQty,FAuxPlanPrice,
 FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FIsVMI,FEntrySupply,FDCStockID,
 FDCSPID,FConsignPrice,FDiscountRate,FConsignAmount,FDiscountAmount,FOrgBillEntryID,FSNListID,
 FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FContractBillNo,FContractInterID,
 FContractEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FAllHookQTY,FCurrentHookQTY,FQtyMust,
 FSepcialSaleId,FPlanMode,FMTONo,FClientOrderNo,FConfirmMemEntry,FClientEntryID,FChkPassItem,
 FSEOutBillNo,FSEOutEntryID,FSEOutInterID,FEntrySelfB0175,FEntrySelfB0176,FEntrySelfB0173,FEntrySelfB0174)
  SELECT @FInterID,@FEntryID,'0','','',@FItemID,@FOLOrderBillNo,0,@FBatchNo,@FQty,@FUnitID,@FAuxQtyMust,@Fauxqty,
 @FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,0,0,'',@FKFDate,@FKFPeriod,@FPeriodDate,0,0,@FDCStockID,@FDCSPID,@FConsignPrice,0,
  @FConsignAmount,0,0,0,@FSourceBillNo,83,@FSourceInterId,@FSourceEntryID,'',0,0,@FOrderBillNo,@FOrderInterID,@FOrderEntryID,0,0,@FQtyMust,0,14036,'','','','0',1058,@FSEOutBillNo,@FSEOutEntryID,@FSEOutInterID,@FEntrySelfB0175,@FEntrySelfB0176,@FEntrySelfB0173,@FEntrySelfB0174
 end
     fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty,@FDCStockID,@FDCSPID --再次将游标停在第一条记录前面，第一次执行，测试有没有记录存在,估测也是为@@FETCH_STATUS赋值
				 end  
				 close my_cursor
				 deallocate my_cursor  
       end
       else  
       begin
       	select @FExchangeRate=isnull(FExchangeRate,1),@FSourceBillNo=FBillNo,@FSEOutBillNo=FBillNo from SEOutStock where FInterID=@FSourceInterId --下推的单据编号
	set @FConsignPrice = @FConsignPrice * @FExchangeRate
	select @FEntrySelfB0173 = FAuxPrice,@FAuxQtyMust = FAuxQty-FAuxCommitQty,@FOrderEntryID=FOrderEntryID,@FOrderInterID=FOrderInterID,@FOrderBillNo=FOrderBillNo,@FSEOutInterID=FInterID,@FSEOutEntryID=FEntryID from SEOutStockEntry where FInterID=@FSourceInterId and FEntryID=@FSourceEntryID
 	set @FOLOrderBillNo=@FOrderBillNo
 	set @FEntrySelfB0174=@FEntrySelfB0173*@Fauxqty
	select @FCoefficient=isnull(FCoefficient,1) from t_MeasureUnit where FMeasureUnitID=@FUnitID --单位换算率
	set @FQtyMust=@FAuxQtyMust*@FCoefficient --基本单位可验收的数量 
	select @FPlanPrice=isnull(FPlanPrice,0)* @FExchangeRate from t_ICItem where   FItemID=@FItemID 
	set @FQty=@Fauxqty*@FCoefficient                  --基本单位数量 
	set @FAuxPlanPrice=@FPlanPrice*@FCoefficient   --单位计划单价
	set @FPlanAmount=@FAuxPlanPrice*@Fauxqty          --计划单价金额 
	set @FConsignAmount=@Fauxqty*@FConsignPrice 
       if(@FKFDate is null or @FKFDate='')
		set @FPeriodDate=null
		else
		begin
			select @FPeriodDate = DATEADD(day,@FKFPeriod,@FKFDate) 
		end  
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
			SELECT '0',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,@FKFPeriod,@FKFDate,-@FQty,-@FSecQty
			end
		  else
			begin
			update ICInventory set FQty=FQty-@FQty,FSecQty=FSecQty-@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID and FKFPeriod=@FKFPeriod and FKFDate=@FKFDate
		  end    
		end
		if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
         select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
           if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID and isnull(FEntrySelfB0175,'') = @FEntrySelfB0175)
		       begin
		         update ICStockBillEntry set FSecQty=isnull(FSecQty,0)+@FSecQty,FConsignAmount=isnull(FConsignAmount,0)+@FConsignAmount,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FEntrySelfB0174=FEntrySelfB0174+@FEntrySelfB0174 where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSourceInterId=@FSourceInterId and FSourceEntryID=@FSourceEntryID and isnull(FEntrySelfB0175,'') = @FEntrySelfB0175
		       end
		        else
		        begin
 INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FMapNumber,FMapName,FItemID,FOLOrderBillNo,
 FAuxPropID,FBatchNo,FQty,FUnitID,FAuxQtyMust,Fauxqty,FSecCoefficient,FSecQty,FAuxPlanPrice,
 FPlanAmount,Fauxprice,Famount,Fnote,FKFDate,FKFPeriod,FPeriodDate,FIsVMI,FEntrySupply,FDCStockID,
 FDCSPID,FConsignPrice,FDiscountRate,FConsignAmount,FDiscountAmount,FOrgBillEntryID,FSNListID,
 FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FContractBillNo,FContractInterID,
 FContractEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FAllHookQTY,FCurrentHookQTY,FQtyMust,
 FSepcialSaleId,FPlanMode,FMTONo,FClientOrderNo,FConfirmMemEntry,FClientEntryID,FChkPassItem,
 FSEOutBillNo,FSEOutEntryID,FSEOutInterID,FEntrySelfB0175,FEntrySelfB0176,FEntrySelfB0173,FEntrySelfB0174)
  SELECT @FInterID,@FEntryID,'0','','',@FItemID,@FOLOrderBillNo,0,@FBatchNo,@FQty,@FUnitID,@FAuxQtyMust,@Fauxqty,
 @FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,0,0,'',@FKFDate,@FKFPeriod,@FPeriodDate,0,0,@FDCStockID,@FDCSPID,@FConsignPrice,0,
  @FConsignAmount,0,0,0,@FSourceBillNo,83,@FSourceInterId,@FSourceEntryID,'',0,0,@FOrderBillNo,@FOrderInterID,@FOrderEntryID,0,0,@FQtyMust,0,14036,'','','','0',1058,@FSEOutBillNo,@FSEOutEntryID,@FSEOutInterID,@FEntrySelfB0175,@FEntrySelfB0176,@FEntrySelfB0173,@FEntrySelfB0174
 end
 end
end
set @detailqty=@detailqty+1
end 
EXEC p_UpdateBillRelateData 21,@FInterID,'ICStockBill','ICStockBillEntry' 

declare @FHeadSelfB0163 varchar(1024),
        @FHeadSelfB0155 varchar(255),-- 币别,
        @FHeadSelfB0157 varchar(255), -- 客户分类
        @FHeadSelfB0159 varchar(255), --收货电话地址
        @FHeadSelfB0160	 varchar(255), --业务说明
        @FHeadSelfB0170 varchar(128),--是否报关
        @FHeadSelfB0161  varchar(255) --单据说明
         if exists(select 1 from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0 )
 begin
 select @FSManagerID=t2.FEmpID from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0
 end
 
select @FHeadSelfB0170 = FHeadSelfS0247,@FHeadSelfB0160=FHeadSelfS0242,@FHeadSelfB0161=FHeadSelfS0244,@FExplanation=FExplanation,@FSaleStyle=FSalType,@FHeadSelfB0163=FHeadSelfS0243,@FHeadSelfB0159=FHeadSelfS0240,@FDeptID=FDeptID,@FEmpID=FEmpID from  SEOutStock where FInterID=@FSourceInterId
select @FHeadSelfB0157 = t2.FName from t_Organization t1 left join t_SubMessage t2 on t1.FTypeID = t2.FInterID where FItemID=@FSupplyID
update ICStockBill set FHeadSelfB0170=@FHeadSelfB0170,FHeadSelfB0157=@FHeadSelfB0157, FHeadSelfB0160=@FHeadSelfB0160,FHeadSelfB0161=@FHeadSelfB0161,FExplanation=@FExplanation,FSaleStyle=@FSaleStyle,FHeadSelfB0163=@FHeadSelfB0163,FHeadSelfB0159=@FHeadSelfB0159,FSManagerID=@FSManagerID,FEmpID=@FEmpID,FDeptID=@FDeptID where FInterID=@FInterID
if exists(select 1 from t_PDABarCodeType where FType=1)
begin
--单个
insert into t_PDABarCodeSign_Out(FInterID,FBarCode,FQty,FBillNo,FRemark,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before,FStatus,FDateUpLoad,FStockID,FStockPlaceID)select @FInterID,a.FBarCode,a.FQty,@FBillNo,'发货通知单下推销售出库',b.FInterIDOut,b.FDateOutStore,b.FUserOutStore,1,convert(varchar(20),GETDATE(),20),b.FStockID,b.FStockPlaceID from a_DetailsTable  a inner join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID
update t_PDABarCodeSign  set FInterIDOut=@FInterID,FDateOutStore=@fdate,FIsOutStore='已出库',FUserOutStore=@FBillerID,FQtyOut =FQtyOut + t.FQty,FOrderBillNo=@FOrderBillNo   from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
update t_PDABarCodeSign  set FInterIDDisassemble=FInterIDAssemble, FInterIDAssemble = 0,FIsOutStore='已出库'   from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID and a.FInterID=1) as t where t_PDABarCodeSign.FBarCode=t.FBarCode and t.FInterID=1
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
     src.fcommitqty=@fsrccommitfield_endvalue,
     src.fauxcommitqty=@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)
 from seoutstockentry src 
     inner join seoutstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fsourceentryid
 inner join t_measureunit t1 on src.funitid=t1.fitemid
 
UPDATE T1 SET T1.FStatus=
CASE WHEN NOT EXISTS(SELECT 1 FROM SEOutStockEntry WHERE FInterID=T1.FInterID AND FCommitQty<>0) THEN 1 
WHEN NOT EXISTS (SELECT 1 FROM SEOutStockEntry WHERE FInterID=T1.FInterID AND FQty>FCommitQty) THEN 3 
ELSE 2 END,
T1.FCLOSED=CASE WHEN NOT EXISTS (SELECT 1 FROM SEOutStockEntry WHERE FInterID=T1.FInterID AND FQty>FCommitQty) THEN 1 ELSE 0 END 
FROM SEOutStock T1, ICStockBillEntry T2 
WHERE T1.FInterID = T2.FSourceInterID
 AND T2.FInterID=@FInterID
 
update src set @fsrccommitfield_prevalue= isnull(src.fseccommitqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fsecqty,
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
     src.fseccommitqty=@fsrccommitfield_endvalue
 from seoutstockentry src 
     inner join seoutstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fsourceinterid as fsourceinterid,u1.fsourceentryid,u1.fitemid,sum(u1.fsecqty) as fsecqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 group by u1.fsourceinterid,u1.fsourceentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fsourceentryid
 
update src set @fsrccommitfield_prevalue= isnull(src.fstockbillqty,0),
     @fsrccommitfield_endvalue=@fsrccommitfield_prevalue+dest.fqty,
     @fcheck_fail=case isnull(@maxorder,0) when 1 then 0 else (case when (1=1) then @fcheck_fail else -1 end) end,
     src.fstockbillqty=@fsrccommitfield_endvalue,
     src.fauxstockbillqty=@fsrccommitfield_endvalue/cast(t1.fcoefficient as float)
 from seoutstockentry src 
     inner join seoutstock srchead on src.finterid=srchead.finterid
     inner join 
 (select u1.fseoutinterid as fsourceinterid,u1.fseoutentryid,u1.fitemid,sum(u1.fqty) as fqty
 from  icstockbillentry u1 
 where u1.finterid=@FInterID
 group by u1.fseoutinterid,u1.fseoutentryid,u1.fitemid) dest 
 on dest.fsourceinterid = src.finterid
 and dest.fitemid = src.fitemid
 and src.fentryid = dest.fseoutentryid
 inner join t_measureunit t1 on src.funitid=t1.fitemid

 IF EXISTS (SELECT 1 FROM ICBillRelations_Sale WHERE FBillType = 21 AND FBillID=@FInterID)
BEGIN
    UPDATE t1 SET t1.FChildren=t1.FChildren+1
    FROM SEOutStock t1 INNER JOIN SEOutStockEntry t2 ON     t1.FInterID=t2.FInterID
    INNER JOIN ICBillRelations_Sale t3 ON t3.FMultiEntryID=t2.FEntryID AND t3.FMultiInterID=t2.FInterID
    WHERE t3.FBillType=21 AND t3.FBillID=@FInterID
END
ELSE
BEGIN
    UPDATE t3 SET t3.FChildren=t3.FChildren+1
    FROM ICStockBill t1 INNER JOIN ICStockBillEntry     t2 ON t1.FInterID=t2.FInterID
    INNER JOIN SEOutStock t3 ON t3.FTranType=t2.FSourceTranType AND t3.FInterID=t2.FSourceInterID
    WHERE t1.FTranType=21 AND t1.FInterID=@FInterID AND t2.FSourceInterID>0
END
UPDATE P1 SET P1.FLockFlag=(CASE WHEN ISNULL(t1.FQty,0)<=0 THEN 0 ELSE 1 END)
FROM SEOrderEntry P1 
INNER JOIN ICStockBillEntry u1 ON u1.FOrderInterID=P1.FInterID AND u1.FOrderEntryID=P1.FEntryID AND u1.FItemID=P1.FItemID
INNER JOIN (SELECT FInterID,FEntryID,SUM(FQty) AS FQty FROM t_LockStock WHERE FTranType=81 GROUP BY FInterID,FEntryID) t1 ON P1.FInterID=t1.FInterID AND P1.FEntryID=t1.FEntryID
WHERE u1.FInterID=@FInterID
  if(@value=1)
		begin
	 IF EXISTS(SELECT FOrderInterID FROM ICStockBillEntry WHERE FOrderInterID>0 AND FInterID=@FInterID)
	BEGIN  
	  UPDATE u1
	  SET u1.FStockQty=u1.FStockQty+1*Cast(u2.FStockQty as Float)
		   ,u1.FSecStockQty=u1.FSecStockQty+1*Cast(u2.FSecStockQty as Float)
		   ,u1.FAuxStockQty=ROUND((u1.FStockQty+1*Cast(u2.FStockQty as Float))/Cast(t3.FCoefficient as Float),t1.FQtyDecimal)
	  FROM SEOrderEntry u1 
	  INNER JOIN 
	  (SELECT FOrderInterID,FOrderEntryID,FItemID,SUM(FQty)AS FStockQty,SUM(FAuxQty) AS FAuxStockQty,SUM(FSecQty) AS FSecStockQty
	   FROM ICStockBillEntry WHERE FInterID=@FInterID
	   GROUP BY FOrderInterID,FOrderEntryID,FItemID) u2
	  ON u1.FInterID=u2.FOrderInterID AND u1.FEntryID=u2.FOrderEntryID AND u1.FItemID=u2.FItemID
	   INNER JOIN t_ICItemBase t1 ON u1.FItemID=t1.FItemID
	   INNER JOIN t_MeasureUnit t3 ON u1.FUnitID=t3.FItemID
	 
	  IF OBJECT_ID('tempdb..#tmpPMCIndex','U') IS NOT NULL 
		DROP TABLE #tmpPMCIndex
	  SELECT u0.FIndex
		INTO #tmpPMCIndex
	  FROM ICPlan_PMCdetail u0
	  INNER JOIN SEOrderEntry u1 ON u0.FRelTranType=81 AND u0.FRelInterID=u1.FInterID AND u0.FRelEntryID=u1.FEntryID AND  u0.FBillType In(22,25)
	  INNER JOIN 
	  ( SELECT DISTINCT FOrderInterID,FOrderEntryID,FItemID
		FROM ICStockBillEntry WHERE  FOrderInterID>0 AND FInterID=@FInterID
	   ) u2 ON u1.FInterID=u2.FOrderInterID AND u1.FEntryID=u2.FOrderEntryID AND u1.FItemID=u2.FItemID 
	  CREATE CLUSTERED INDEX PK_#tmpPMCIndex ON #tmpPMCIndex(FIndex) 
	 
	  UPDATE u0
		SET u0.FWillOutQty=CASE WHEN u1.FQty>u1.FStockQty THEN u1.FQty-u1.FStockQty ELSE 0 END 
	  FROM ICPlan_PMCdetail u0
	  INNER JOIN SEOrderEntry u1 ON u0.FRelTranType=81 AND u0.FRelInterID=u1.FInterID AND u0.FRelEntryID=u1.FEntryID AND  u0.FBillType In(22,25)
	  WHERE exists(select 1 from  #tmpPMCIndex where FIndex=u0.FIndex)
	  DROP TABLE #tmpPMCIndex
	 
	END 
	UPDATE p1 
	SET p1.FMrpClosed=CASE WHEN ISNULL(p1.FMRPAutoClosed,1)=1 THEN (CASE WHEN p1.FStockQty<p1.FQty THEN 0 ELSE 1 END) ELSE p1.FMrpClosed END
	FROM SEOrderEntry p1 INNER JOIN ICStockBillEntry u1 ON u1.FOrderInterID=p1.FInterID AND u1.FOrderEntryID=p1.FEntryID
	WHERE u1.FInterID=@FInterID
	IF EXISTS(SELECT FOrderInterID FROM ICStockBillEntry WHERE FSEOutInterID>0 AND FInterID=@FInterID)
	 UPDATE u1
	 SET u1.FStockQty=u1.FStockQty+1*Cast(u2.FStockQty as Float)
		 ,u1.FSecStockQty=u1.FSecStockQty+1*Cast(u2.FSecStockQty as Float)
		 ,u1.FAuxStockQty=ROUND((u1.FStockQty+1*Cast(u2.FStockQty as Float))/Cast(t3.FCoefficient as Float),t1.FQtyDecimal)
	 FROM SEOutStockEntry u1 
	 INNER JOIN 
	 (SELECT FSEOutInterID,FSEOutEntryID,FItemID,SUM(FQty)AS FStockQty,SUM(FAuxQty) AS FAuxStockQty,SUM(FSecQty) AS FSecStockQty
	  FROM ICStockBillEntry WHERE FInterID=@FInterID
	  GROUP BY FSEOutInterID,FSEOutEntryID,FItemID) u2
	 ON u1.FInterID=u2.FSEOutInterID AND u1.FEntryID=u2.FSEOutEntryID AND u1.FItemID=u2.FItemID
	 INNER JOIN t_ICItemBase t1 ON u1.FItemID=t1.FItemID INNER JOIN t_MeasureUnit t3 ON u1.FUnitID=t3.FItemID
	end
Update t
Set t.FStatus =Case When (SELECT COUNT(1) FROM SEOrderEntry WHERE (FCommitQty>0 OR (ISNULL(FMRPClosed,0)=1 AND ISNULL(FMRPAutoClosed,1)=0)) AND FInterID IN(0))=0 Then 1 When (SELECT COUNT(1) FROM SEOrderEntry te WHERE (ISNULL(FMRPClosed,0)=1 OR  FCommitQty >= FQty ) AND FInterID IN(0))<(SELECT COUNT(1) FROM SEOrderEntry WHERE FInterID IN(0)) Then 2 Else 3 End
,t.FClosed =Case WHEN (SELECT COUNT(1) FROM SEOrderEntry te WHERE ( FCommitQty >= FQty  OR (ISNULL(te.FMRPAutoClosed,1)=0 AND ISNULL(FMRPClosed,0)=1)) AND te.FInterID IN(0))=(SELECT COUNT(1) FROM SEOrderEntry te WHERE te.FInterID IN(0)) Then 1 Else 0 End
From SEOrder t
WHERE t.FInterID IN(0)
update t1 set FcmtQty_O=FcmtQty_O from ExpOutReqEntry t1  inner join (  select sum(t1.FQty) FQty,t3.fdetailid  from ICStockBillEntry t1  inner join ExpOutReqEntry t2 on t2.fdetailid=t1.fsourceEntryid  inner join ExpOutReqEntry t3 on t3.fdetailid=t2.fentryid_src  where fsourceinterid>0 and fsourcebillno<>'' and fsourcetrantype=1007131 and t1.finterid=@FInterID group by t3.fdetailid) t2  on t1.fdetailid=t2.fdetailid

UPDATE t1 SET t1.FDiffQtyClosed = (CASE WHEN t2.FStatus = 3 THEN t1.FQty-(CASE WHEN t1.FStockBillQty>t1.FInvoiceQty THEN t1.FStockBillQty ELSE t1.FInvoiceQty END) ELSE 0 END)
  FROM SEOutStockEntry t1 INNER JOIN SEOutStock t2 ON t1.FInterID = t2.FInterID 
 WHERE t1.FInterID IN (@FSourceInterId)
    UPDATE A SET A.FCommitQty=A.FCommitQty-D.FQty,A.FAuxCommitQty=A.FAuxCommitQty-(D.FQty/T.FCoefficient),
    A.FSecCommitQty=A.FSecCommitQty -D.FSecQty 
    FROM ICStockBillEntry A
    INNER JOIN ICWebReturnEntry B ON B.FID_SRC=A.FInterID AND B.FEntryID_SRC=A.FDetailID AND B.FClassID_SRC=1007572
    INNER JOIN SEOutStockEntry C ON C.FSourceInterId=B.FID AND C.FSourceEntryID=B.FEntryID 
    INNER JOIN ICStockBillEntry D ON D.FSourceInterId =C.FInterID AND D.FSourceEntryID =C.FEntryID AND D.FSourceTranType =82
    LEFT JOIN t_MeasureUnit T ON A.FUnitID=T.FMeasureUnitID 
    WHERE D.FInterID=@FInterID


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
