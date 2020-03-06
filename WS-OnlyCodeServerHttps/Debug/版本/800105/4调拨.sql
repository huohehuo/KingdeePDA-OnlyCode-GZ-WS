if (exists (select * from sys.objects where name = 'proc_Allot'))
    drop proc proc_Allot
go
create proc proc_Allot
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
        @Fdate varchar(50),       --日期       
        @FCheckDate varchar(50),  --审核日期
        @FMangerID varchar(20),   --主管id
        @FDeptID varchar(20),     --部门id
        @FEmpID varchar(20),      --业务员id
        @FBillerID varchar(20),   --制单人id
        @FFManagerID varchar(20), --验收
        @FHeadSelfD0135 varchar(20), --调拨类别,
        @FHeadSelfD0136 varchar(20),-- 是否良品
         @FOrderID varchar(50),--PDA单据编号
        @FPDAID varchar(50),  --PDA序列号 
        @FSManagerID varchar(20)  --保管  
set @FBillerID = dbo.getString(@mainStr,'|',1) --操作员
set @Fdate =dbo.getString(@mainStr,'|',2)      --日期  
set @FDeptID =dbo.getString(@mainStr,'|',3)   --部门id
set @FEmpID =dbo.getString(@mainStr,'|',4)    --业务员id
set @FSManagerID =dbo.getString(@mainStr,'|',5) --保管id   
set @FFManagerID =dbo.getString(@mainStr,'|',6) --验收id
if exists(select 1 from t_PDABarCodeType where FType=1)
begin
set @FOrderID=dbo.getString(@mainStr,'|',7) --PDA单据编号   
set @FPDAID=dbo.getString(@mainStr,'|',8) --PDA序列号 
end
set @FHeadSelfD0135=dbo.getString(@mainStr,'|',9) --调拨类别ID（也是语句中的FInterID）
set @FHeadSelfD0136=dbo.getString(@mainStr,'|',10) --是否良品ID（也是语句中的FInterID）
set @FDeptID= 0
set @FEmpID= 0
set @Fdate = convert(varchar(20),GETDATE(),23)
exec GetICMaxNum 'ICStockBill',@FInterID output,1,@FBillerID --得到@FInterID
------------------------------------------------------------得到编号
 set @FBillNo ='' 
 exec proc_GetICBillNo 41, @FBillNo out 
-----------------------------------------------------------得到编号 
 
  declare @IsExist varchar(10), --是否存在
            @value varchar(10)--库存更新类型
    select @value= Fvalue  from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' 
set @FCheckDate=null --审核时间 
--    if exists(select 1 from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID)
--begin
--  ---报检人
--  select @FFManagerID = t2.FItemID from t_User t1 left join t_Emp t2 on t1.FName=t2.FName where t1.FUserID=@FBillerID
--end
 
INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,FHookStatus,Fdate,FCheckDate,FFManagerID,FSManagerID,
FBillerID,FMultiCheckDate1,FMultiCheckDate2,FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FMultiCheckDate6,FSelTranType,FBrID,FDeptID,FEmpID,FRefType,
FPrintCount,FHeadSelfD0134,FHeadSelfD0135,FHeadSelfD0136,FHeadSelfD0137,FHeadSelfD0138,FHeadSelfD0139,FHeadSelfD0140,FHeadSelfD0141,FHeadSelfD0142,FHeadSelfD0143) 
SELECT @FInterID,@FBillNo,'0',41,0,0,0,0,@Fdate,Null,@FFManagerID,@FSManagerID,@FBillerID,Null,Null,Null,Null,Null,Null,85,0,0,0,12561,0,'',@FHeadSelfD0135,@FHeadSelfD0136,'',Null,'',Null,'','','PDA制单'

 UPDATE ICStockBill SET FUUID=NEWID() WHERE FInterID=@FInterID
declare @FEntryID varchar(20),       --明细序号
        @FItemID varchar(20),        --商品id
        @FQty float,                --基本单位数量
        @FUnitID varchar(20),       --单位id
        @Fauxqty float,             --上传单位数量 
        @FSCStockID varchar(20),    --出库仓库id
        @FSCSPID varchar(20),       --出库仓位id
        @FDCStockID varchar(20),    --入库仓库id
        @FDCSPID  varchar(20),      --入库仓位id
        @FBatchNo varchar(50),      --批号
        @FCoefficient float(50),  --单位换算
        @FPlanPrice float(50),    --基本计划单价
        @FAuxPlanPrice float(50), --单位计划单价
        @FPlanAmount float(50),   --计划单价金额
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
	set @Fauxqty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)    --数量
	set @FDCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4) --入库id
	set @FSCStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5) --出库id
	set @FDCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6) --入库仓位
	set @FSCSPID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7) --出库仓位
	set @FBatchNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8) --出库仓位
		set @FKFDate=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)  
	set @FKFPeriod=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+10)
	set @FBarCode_Assemble =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+11)--装箱条码 如果不是装箱条码 该地方为''
	set @detailIndex=@detailIndex+1
		   if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode_Assemble)
       begin       
         declare My_cursor cursor dynamic --scroll表示可以向前或向后移动    dynamic：表示可写也可读
         for 
         select  t2.FBarCode,t2.FItemID,t2.FBatchNo,t2.FKFDate,t2.FKFPeriod,t2.FQty,t2.FStockID,t2.FStockPlaceID from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t2.FInterIDAssemble =  t1.FInterID where t1.FBillNo=@FBarCode_Assemble
         open My_cursor      
         fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty,@FSCStockID,@FSCSPID --游标停在第一条记录前面，第一次执行，测试有没有记录存在,
         while(@@FETCH_STATUS = 0) --取数据 0表示成功执行FETCH语句  -1 表示FETCH语句失败，例如移动行指针使其超出了结果集 -2 表示被提取的行不存在。
         begin  
	 select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --单位换算率
	 select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	 set @FQty=@Fauxqty*@FCoefficient  
	 set @FAuxPlanPrice = @FPlanPrice*@FCoefficient
	 set @FPlanAmount = @FAuxPlanPrice*@Fauxqty	
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
    --入库
      select @IsExist=COUNT(1) from ICInventory where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID
      if(@IsExist=0)
        begin
        INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
        SELECT '',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,0,'',@FQty,@FSecQty
        end
      else
        begin
        update ICInventory set FQty=FQty+@FQty,FSecQty=FSecQty+@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID 
      end   
      --出库
       select @IsExist=COUNT(1) from ICInventory where FStockID=@FSCStockID and FStockPlaceID=@FSCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID
      if(@IsExist=0)
        begin
        INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
        SELECT '',@FItemID,@FBatchNo,'',0,@FSCStockID,@FSCSPID,0,'',-@FQty,-@FSecQty
        end
      else
        begin
        update ICInventory set FQty=FQty-@FQty,FSecQty=FSecQty-@FSecQty where FStockID=@FSCStockID and FStockPlaceID=@FSCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID 
      end     
    end 
    if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
         select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
          if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSCStockID=@FSCStockID and FSCSPID=@FSCSPID)
		       begin
		         update ICStockBillEntry set FSecQty=FSecQty+@FSecQty,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FAmount=ISNULL(FAmount,0) where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSCStockID=@FSCStockID and FSCSPID=@FSCSPID
		       end
		        else
		        begin
 INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FItemID,FAuxPropID,FBatchNo,FQty,FDefaultBaseQty,FRealStockBaseQty,FUnitID,FDefaultQty,FRealStockQty,
 Fauxqty,FSecCoefficient,FSecQty,FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,FAuxPriceRef,FAmtRef,Fnote,FKFDate,FKFPeriod,FPeriodDate,FIsVMI,FEntrySupply,
 FSCStockID,FSCSPID,FDCStockID,FDCSPID,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FICMOBillNo,FICMOInterID,FPPBomEntryID,
 FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FChkPassItem,FContractBillNO,FContractEntryID,FContractInterID,FLockFlag,FClientEntryID,FClientOrderNo) 
  SELECT @FInterID,@FEntryID,'0',@FItemID,0,@FBatchNo,@FQty,0,0,@FUnitID,0,0,@Fauxqty,@FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,0,0,0,0,'',@FKFDate,@FKFPeriod,@FPeriodDate,0,0,@FSCStockID,@FSCSPID,@FDCStockID,@FDCSPID,0,'',0,0,0,'',0,0,'',0,0,14036,'',1058,'',0,0,0,'','' 
                end
    fetch next from my_cursor into @FBarCode,@FItemID,@FBatchNo,@FKFDate,@FKFPeriod,@Fauxqty,@FSCStockID,@FSCSPID --再次将游标停在第一条记录前面，第一次执行，测试有没有记录存在,估测也是为@@FETCH_STATUS赋值
				 end  
				 close my_cursor
				 deallocate my_cursor  
       end
       else  
       begin
       	 select @FCoefficient=FCoefficient from t_MeasureUnit where FMeasureUnitID=@FUnitID --单位换算率
	 select @FPlanPrice=isnull(FPlanPrice,0) from t_ICItem where   FItemID=@FItemID 
	 set @FQty=@Fauxqty*@FCoefficient  
	 set @FAuxPlanPrice = @FPlanPrice*@FCoefficient
	 set @FPlanAmount = @FAuxPlanPrice*@Fauxqty	
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
    --入库
      select @IsExist=COUNT(1) from ICInventory where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID
      if(@IsExist=0)
        begin
        INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
        SELECT '',@FItemID,@FBatchNo,'',0,@FDCStockID,@FDCSPID,0,'',@FQty,@FSecQty
        end
      else
        begin
        update ICInventory set FQty=FQty+@FQty,FSecQty=FSecQty+@FSecQty where FStockID=@FDCStockID and FStockPlaceID=@FDCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID 
      end   
      --出库
       select @IsExist=COUNT(1) from ICInventory where FStockID=@FSCStockID and FStockPlaceID=@FSCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID
      if(@IsExist=0)
        begin
        INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
        SELECT '',@FItemID,@FBatchNo,'',0,@FSCStockID,@FSCSPID,0,'',-@FQty,-@FSecQty
        end
      else
        begin
        update ICInventory set FQty=FQty-@FQty,FSecQty=FSecQty-@FSecQty where FStockID=@FSCStockID and FStockPlaceID=@FSCSPID and FBatchNo=@FBatchNo and FItemID=@FItemID 
      end     
    end 
    if(@FKFDate is null or @FKFDate='')
		 begin
            set @FKFDate = null
         end
         select @FEntryID=isnull(MAX(FEntryID),0)+1 from ICStockBillEntry where FInterID=@FInterID 
          if exists(select 1 from ICStockBillEntry where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSCStockID=@FSCStockID and FSCSPID=@FSCSPID)
		       begin
		         update ICStockBillEntry set FSecQty=FSecQty+@FSecQty,FQty=isnull(FQty,0)+@FQty,Fauxqty=isnull(Fauxqty,0)+@Fauxqty,FPlanAmount=isnull(FPlanAmount,0)+@FPlanAmount,FAmount=ISNULL(FAmount,0) where FItemID=@FItemID and FBatchNo=@FBatchNo and FUnitID=@FUnitID and isnull(FKFDate,'')=isnull(@FKFDate,'') and FKFPeriod=@FKFPeriod and FDCStockID=@FDCStockID and FDCSPID=@FDCSPID and FInterID =@FInterID and FSCStockID=@FSCStockID and FSCSPID=@FSCSPID
		       end
		        else
		        begin
 INSERT INTO ICStockBillEntry (FInterID,FEntryID,FBrNo,FItemID,FAuxPropID,FBatchNo,FQty,FDefaultBaseQty,FRealStockBaseQty,FUnitID,FDefaultQty,FRealStockQty,
 Fauxqty,FSecCoefficient,FSecQty,FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,FAuxPriceRef,FAmtRef,Fnote,FKFDate,FKFPeriod,FPeriodDate,FIsVMI,FEntrySupply,
 FSCStockID,FSCSPID,FDCStockID,FDCSPID,FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FICMOBillNo,FICMOInterID,FPPBomEntryID,
 FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FChkPassItem,FContractBillNO,FContractEntryID,FContractInterID,FLockFlag,FClientEntryID,FClientOrderNo) 
  SELECT @FInterID,@FEntryID,'0',@FItemID,0,@FBatchNo,@FQty,0,0,@FUnitID,0,0,@Fauxqty,@FSecCoefficient,@FSecQty,@FAuxPlanPrice,@FPlanAmount,0,0,0,0,'',@FKFDate,@FKFPeriod,@FPeriodDate,0,0,@FSCStockID,@FSCSPID,@FDCStockID,@FDCSPID,0,'',0,0,0,'',0,0,'',0,0,14036,'',1058,'',0,0,0,'','' 
                end
       end
end
set @detailqty=@detailqty+1
end
EXEC p_UpdateBillRelateData 41,@FInterID,'ICStockBill','ICStockBillEntry' 
  if exists(select 1 from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0 )
 begin
 select @FSManagerID=isnull(t2.FEmpID,0) from ICStockBillEntry t1 left join t_Stock t2 on t1.FDCStockID = t2.FItemID  where t1.FInterID = @FInterID and t2.FEmpID >0
 end
 if(CONVERT(int, @FSManagerID)>0)
 begin
 update ICStockBill set FSManagerID=@FSManagerID,FFManagerID=@FSManagerID where FInterID=@FInterID
 end
if exists(select 1 from t_PDABarCodeType where FType=1)
begin
insert into t_PDABarCodeSign_Allot(FInterID,FBarCode,FQty,FBillNo,FRemark,FStockID_Before,FStockPlaceID_Before,FStockID_Now,FStockPlaceID_Now,FStatus,FInterID_Before,FBillNo_Before,FDateUpLoad)
select @FInterID,b.FBarCode,a.FQty,@FBillNo,'调拨单',b.FStockID,b.FStockPlaceID,a.FStockID,a.FStockPlaceID,1,b.FInterIDAllot,b.FAllotBillNo,convert(varchar(20),GETDATE(),20) from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID
update t_PDABarCodeSign  set FStockID=t.FStockID,FStockPlaceID=t.FStockPlaceID,FInterIDAllot=@FInterID,FAllotBillNo=@FBillNo  from (select a.*,b.FStockID as AAA,b.FStockPlaceID as BBB from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
update ICStockBill set FMultiCheckStatus = 2,FUUID=NEWID()  where FInterID=@FInterID
UPDATE ICStockBillEntry SET FAuxprice=FAuxPriceRef,FAmount=FAmtRef  FROM ICStockBillEntry u1 INNER JOIN ICStockBill v1 ON u1.FInterID=v1.FInterID  AND v1.FTranType=41 AND v1.FInterID= 55773 WHERE (v1.FRefType<>12562 or v1.FRefType is null)  
UPDATE POInstock SET FMultiCheckStatus=16
 WHERE FInterID = @FInterID
 AND FTranType=73 
 UPDATE POInstock SET FCheckerID=16456
 WHERE FInterID = @FInterID
 AND FTranType=73
update icclassmctemplatemap set ftemplateid=70 where fclasstypeid=1073 and fbillid=@FInterID
UPDATE ICClassMCTaskCenter SET FStatus=1 ,FProcessUserID=16456,FUpdateDate=getdate()
 WHERE FClassTypeID=1073 AND FBillID=@FInterID AND FStatus=0 AND (FTaskType=0 OR FTaskType=2) AND FNextLevelTag =2000
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
