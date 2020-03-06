if (exists (select * from sys.objects where name = 'proc_MaterialsAllCheck2'))
    drop proc proc_MaterialsAllCheck2
go
create proc proc_MaterialsAllCheck2
(
  @FBillerID int,    --制单人id 
  @FID_SRC int,--原单ID
  @FEntryID_SRC int, --明细ID
  @FSCBillInterID int,--质检方案ID
  @FResult int,--质检结果
  @FSManagerID int,--质检员@FSManagerID=FSManagerID,@FFManagerID=FFManagerID,
  @FFManagerID int,--送检人
  @FCheckQty decimal(28,10), --检验数量
  @FSampleBreakQty decimal(28,10), --样品破坏数
  @FPassQty decimal(28,10) --合格数
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
        @FBizType int,  --
        @FItemID int,
        @FUnitID int,
        @FAuxPropID int,
        @FCheckMethod int,
        @FBatchNo varchar(128),
         @FSCBillNo varchar(128),--原单单号
       @FBillNo_SRC varchar(128),--收料通知单单号
       @FOrderBillNo varchar(128),--订单单号
      @FOrderEntryID int,
      @FOrderInterID int,
         @FNotPassQty decimal(28,10),--不合格数量
         @FSendUpQty decimal(28,10),--报检数
        @FSupplyID varchar(20)   --购货单位id 
        
         set @Fdate = convert(varchar(20),GETDATE(),23)
  
exec GetICMaxNum 'ICQCBill',@FInterID output 
------------------------------------------------------------得到编号
set @FBillNo = ''
 exec proc_GetICBillNo 1001000, @FBillNo out 
-----------------------------------------------------------得到编号
 select @FSupplyID = FSupplyID,@FBizType=FBizType,@FBillNo_SRC = FBillNo from POInStock where FInterID = @FID_SRC
 select @FOrderBillNo=FOrderBillNo,@FOrderInterID=FOrderInterID,@FOrderEntryID=FOrderEntryID,@FSendUpQty=FAuxQty-FAuxRelateQty,@FItemID=FItemID,@FUnitID=FUnitID,@FBatchNo=FBatchNo,@FAuxPropID=FAuxPropID,@FCheckMethod=FCheckMethod,@FSCBillNo=FOrderBillNo from POInStockEntry where FInterID = @FID_SRC and FEntryID=@FEntryID_SRC
 select @FSCBillNo=FBillNo from ICQCScheme where FInterID = @FSCBillInterID
 set @FNotPassQty = @FCheckQty -  @FSampleBreakQty - @FPassQty
 
 INSERT INTO ICQCBill(FInterID,FTranType,FBillNo,FBizType,FSupplyID,FDate,FItemID,FUnitID,FBatchNo,FAuxPropID,FCheckMethod,FSendUpQty,FCheckQty,FSCBillNo,FSCBillInterID,FSampleBreakQty,FPassQty,FNotPassQty,FResult,FCheckTimes,FSManagerID,FFManagerID,FBillerID,FBillDate,FCheckerID,FCheckDate,FStatus,FCancellation,FCancelID,FCancelDate,FConnectFlag,FDefectConnectFlag,FSampStdID,FAQLID,FSampleQty,FAcQty,FReQty,FSampleBadQty,FCriticalSampStdID,FCriticalAQLID,FCriticalSampleQty,FCriticalAcQty,FCriticalReQty,FCriticalSampleBadQty,FResultEx,FNote,FSlightSampStdID,FSlightAQLID,FSlightSampleQty,FSlightAcQty,FSlightReQty,FSlightSampleBadQty,FSpecialUse) 
Values(@FInterID,1001000,@FBillNo,@FBizType,@FSupplyID,@FDate,@FItemID,@FUnitID,@FBatchNo,@FAuxPropID,@FCheckMethod,@FSendUpQty,@FCheckQty,@FSCBillNo,@FSCBillInterID,@FSampleBreakQty,@FPassQty,@FNotPassQty,@FResult,1,@FSManagerID,@FFManagerID,@FBillerID,@Fdate,0,NULL,0,0,0,NULL,0,'',0,0,0,0,0,0,0,0,0,0,0,0,0,'',0,0,0,0,0,0,0)

INSERT INTO ICQCBillEntry(FEntryID,FInterID,FQCItemTypeID,FQCItemID,FAnalysisMethodID,FQCMethodID,FQCBasisID,FQCOtherID1,FQCOtherID2,FQCUnit,FTargetID,FTargetQty,FTargetText,FLowerLimitID,FLowerLimitQty,FLowerLimitText,FUpperLimitID,FUpperLimitQty,FUpperLimitText,FTestValueID,FTestValue,FTestValueText,FLowTolerance,FUpTolerance,FEmphasesCheck,FECriticalSampStdID,FECriticalAQLID,FECriticalSampleQty,FECriticalAcQty,FECriticalReQty,FECriticalBadQty,FSampStdID,FEAQLID,FSampleQty,FEAcQty,FEReQty,FSamplePassQty,FBadQty,FESlightSampStdID,FESlightAQLID,FESlightSampleQty,FESlightAcQty,FESlightReQty,FESlightBadQty,FResult,FDisPassReasonID,FSchemeMemos,FNote,FQCItemDecimal,FCollection)
select FEntryID,@FInterID,0,FQCItemID,FAnalysisMethodID,FQCMethodID,FQCBasisID,FQCOtherID1,FQCOtherID2,FQCUnit,FTargetID,FTargetQty,FTargetText,FLowerLimitID,FLowerLimitQty,FLowerLimitText,FUpperLimitID,FUpperLimitQty,FUpperLimitText,0,0,'',0,0,FEmphasesCheck,FECriticalSampStdID,0,0,0,0,0,0,0,0,0,0,0,0,FESlightSampStdID,0,0,0,0,0,0,0,0,0,0,0 from ICQCSchemeEntry where FInterID = @FSCBillInterID

INSERT INTO QMSourceInfo(FEntryID,FInterID,FClassID_SRC,FBillNo_SRC,FID_SRC,FEntryID_SRC,FQCReqBillNo,FQCReqEntryID,FQCReqInterID,FOrderBillNo,FOrderEntryID,FOrderInterID,
FSourItemID,FSourUnitID,FSourSendUpQty,FSourCheckQty,FSourPassQty,FSourNotPassQty,FSourSampleBreakQty) 
Values(1,@FInterID,-72,@FBillNo_SRC,@FID_SRC,@FEntryID_SRC,@FBillNo_SRC,@FEntryID_SRC,@FID_SRC,@FOrderBillNo,@FOrderEntryID,@FOrderInterID,@FItemID,@FUnitID,@FSendUpQty,@FCheckQty,@FPassQty,0,0)
 
  SET NOCOUNT ON
 CREATE TABLE #TmpCk(FID_SRC INT,FEntryID_SRC INT,FSourUnitID INT,FSourCheckQty Decimal(23,10),FSourPassQty Decimal(23,10),FSourNotPassQty Decimal(23,10),FSourSampleBreakQty Decimal(23,10))
 INSERT INTO #TmpCk(FID_SRC,FEntryID_SRC,FSourUnitID,FSourCheckQty,FSourPassQty,FSourNotPassQty,FSourSampleBreakQty)
 SELECT FID_SRC,FEntryID_SRC,FSourUnitID,SUM(FSourCheckQty) As FSourCheckQty,SUM(FSourPassQty) As FSourPassQty,SUM(FSourNotPassQty) As FSourNotPassQty,
 SUM(FSourSampleBreakQty) As FSourSampleBreakQty FROM QMSourceInfo WHERE FClassID_SRC = -72 AND FInterID IN ( @FInterID)
 GROUP BY FID_SRC,FEntryID_SRC,FSourUnitID
 UPDATE t1 SET t1.FAuxRelateQty=t1.FAuxRelateQty+t2.FSourCheckQty*CONVERT(Decimal(23,10),ISNULL(u.FCoefficient,1)/ISNULL(u1.FCoefficient,1))
,t1.FRelateQty=t1.FRelateQty+t2.FSourCheckQty*CONVERT(Decimal(23,10),ISNULL(u.FCoefficient,1))
,t1.FSecRelateQty=t1.FSecRelateQty+CASE WHEN ISNull(t1.FSecCoefficient,0)=0 THEN 0 ELSE t2.FSourCheckQty*CONVERT(Decimal(23,10),ISNULL(u.FCoefficient,1)/t1.FSecCoefficient) END
 FROM POInStockEntry t1 INNER JOIN #TmpCk t2 ON t1.FInterID=t2.FID_SRC AND t1.FEntryID=t2.FEntryID_SRC
 LEFT JOIN t_MeasureUnit u ON t2.FSourUnitID=u.FMeasureUnitID
 LEFT JOIN t_MeasureUnit u1 ON t1.FUnitID=u1.FMeasureUnitID
 UPDATE t1 SET t1.FAuxRelateQty=CASE WHEN t1.FAuxRelateQty<0 THEN 0 ELSE t1.FAuxRelateQty END 
,t1.FRelateQty=CASE WHEN t1.FRelateQty<0 THEN 0 ELSE t1.FRelateQty END 
,t1.FSecRelateQty=CASE WHEN t1.FSecRelateQty<0 THEN 0 ELSE t1.FSecRelateQty END
 FROM POInStockEntry t1 INNER JOIN #TmpCk t2 ON t1.FInterID=t2.FID_SRC AND t1.FEntryID=t2.FEntryID_SRC
 DROP TABLE #TmpCk
 UPDATE POInstock SET FChildren=FChildren+1,FStatus=2 WHERE FInterID IN (@FID_SRC)

UPDATE ICQCBill SET FConnectFlag=1 WHERE FInterID IN (
SELECT FID_SRC FROM QMSourceInfo WHERE (FClassID_SRC BETWEEN 1001000 AND 1001004) AND FInterID IN (@FInterID))UPDATE t SET t.FQCReqTranType=72,t.FOrderTranType=CASE WHEN t.FOrderInterID>0 THEN (CASE WHEN u.FBizType=12511 THEN 1007105 ELSE 71 END) ELSE 0 END
FROM QMSourceInfo t INNER JOIN ICQCBill u ON t.FInterID=u.FInterID 
WHERE u.FInterID= @FInterID AND t.FQCReqInterID>0 And t.FQCReqTranType=0 
UPDATE ICQCBill SET FType=CASE WHEN FBizType=12511 THEN 394 ELSE 390 END 
WHERE FInterID= @FInterID --检验单新增情况
-- IF NOT EXISTS(SELECT TOP 1 1 FROM QMQualityDefect WHERE FQCBillInterID=@FInterID AND FSourceTranType=1001000) RETURN  --检验单新增时不往下执行
-- --检验单物料改变情况
-- IF NOT EXISTS( 
--     SELECT TOP 1 1 FROM ICQCBill t1 
--     INNER JOIN QMQualityDefect t2 ON t1.FInterID=t2.FQCBillInterID 
--     AND t1.FItemID=t2.FItemID AND t1.FInterID=@FInterID AND t1.FTranType=1001000) 
-- BEGIN 
--     --删除不良原因记录表体
--     DELETE v2 FROM QMQualityDefectEntry v2 
--     INNER JOIN QMQualityDefect v1 ON v1.FID=v2.FID
--     AND v1.FQCBillInterID=@FInterID AND v1.FSourceTranType=1001000

--     DELETE FROM QMQualityDefect WHERE FQCBillInterID=@FInterID AND FSourceTranType=1001000 --删除不良原因记录表头
-- END

-- --检验单物料未改变情况
-- ELSE BEGIN 
--     --检验单表体检验项目修改情况
--     UPDATE t2 SET t2.FInspectionID=0 FROM QMQualityDefectEntry t2 
--     INNER JOIN QMQualityDefect t1 ON t1.FID=t2.FID 
--     AND t1.FQCBillInterID=@FInterID
--     AND NOT EXISTS 
--     (SELECT TOP 1 1 FROM ICQCBillEntry t4 
--         INNER JOIN ICQCBill t3 ON t4.FInterID=t3.FInterID 
--         AND t4.FQCItemID=t2.FInspectionID AND t3.FInterID=@FInterID AND t3.FTranType=1001000) 

--     --删除不良原因记录表体无效缺陷数量 
--     DELETE v2 FROM QMQualityDefectEntry v2
--     INNER JOIN QMQualityDefect v1 ON v1.FID=v2.FID
--     AND v2.FInspectionID=0 AND v2.FQualityID=0 AND v2.FDefectAuxQty=0
--     AND v1.FQCBillInterID=@FInterID AND v1.FSourceTranType=1001000

--     --检验单一般修改情况
--     UPDATE t1 SET 
--         t1.FCheckQty=s1.FCheckQty 
--     FROM QMQualityDefect t1 
--     INNER JOIN ICQCBill s1 ON t1.FQCBillInterID=s1.FInterID AND t1.FSourceTranType=s1.FTranType 
--     AND s1.FInterID=@FInterID AND s1.FTranType=1001000
-- END 


--同步更新样本检测值 
--创建临时表 
 create table #tmpIDSampleValue(fid int not null) 
 insert into #tmpIDSampleValue 
 select t1.fid from qmsamplevalue t1 
 where Exists(select 1 from icqcbillentry t where t1.fid_src = @FInterID
 and t.FAnalysisMethodID=9 and t1.FAnalysisMethodID=10 And t1.findex_src=t.FDetailID) 
 and t1.FClassID_SRC = 1001000
--删除 
 delete from qmsamplevalueentry where fid in (select fid from #tmpIDSampleValue) 
 delete from qmsamplevalue where fid in (select fid from #tmpIDSampleValue) 
--更新 
 update qmsamplevalue set fqcitemid = i.fqcitemid,FAnalysisMethodID = i.FAnalysisMethodID,FSampleQty = ceiling(i.FSampleQty) from qmsamplevalue q join icqcbillentry i 
 on q.fid_src = i.finterid and q.findex_src = i.fdetailid 
 where q.fid_src = @FInterID
 and q.FClassID_SRC = 1001000
--删除临时表 
 drop table #tmpIDSampleValue
 
 ----审核
 Update ICClassCheckStatus1001000 Set FBillID = FBillID  Where FBillID = @FInterID And FPage = 1 And FBillEntryID = 0
 Insert Into ICClassCheckRecords1001000(FPage,FBillID,FBillEntryID,FBillNo, FBillEntryIndex,FCheckLevel,FCheckLevelTo,FMode,FCheckMan, FCheckIdea,FCheckDate,FDescriptions)  Values (1,@FInterID,0,@FBillNo,0,-1,1,0,@FBillerID,'',GetDate(),'审核')
Update ICClassCheckStatus1001000 Set  FBillNo = @FBillNo, FCurrentLevel = FCurrentLevel + 1, FCheckMan1 = @FBillerID, FCheckDate1 = GetDate(), FCheckIdea1 = '' Where FBillID = @FInterID And FPage = 1 And FBillEntryID = 0
UPDATE ICQCBill SET FStatus= 1 WHERE FInterID = @FInterID
CREATE TABLE #TmpCk1(
     FQCReqInterID INT NOT NULL DEFAULT(0),
     FQCReqEntryID INT NOT NULL DEFAULT(0),
     FCoefficient DECIMAL(23,10) NOT NULL DEFAULT(0),
     FSourCheckQty DECIMAL(23,10) NOT NULL DEFAULT(0),
     FSourPassQty DECIMAL(23,10) NOT NULL DEFAULT(0),
     FSourNotPassQty DECIMAL(23,10) NOT NULL DEFAULT(0),
     FSourSampleBreakQty DECIMAL(23,10) NOT NULL DEFAULT(0)
 )

 INSERT INTO #TmpCk1(
     FQCReqInterID , FQCReqEntryID, FCoefficient, FSourCheckQty, FSourPassQty, FSourNotPassQty, FSourSampleBreakQty
 )
 SELECT t2.FQCReqInterID,t2.FQCReqEntryID,u.FCoefficient,SUM(t2.FSourCheckQty) As FSourCheckQty,
 SUM(t2.FSourPassQty) As FSourPassQty,SUM(t2.FSourNotPassQty) As FSourNotPassQty,
 SUM(t2.FSourSampleBreakQty) As FSourSampleBreakQty
 FROM QMSourceInfo t2
 INNER JOIN ICQCBill t ON t2.FInterID=t.FInterID AND t.FResult<>13556
 LEFT JOIN t_MeasureUnit u ON t2.FSourUnitID=u.FMeasureUnitID
 Where t2.FInterID=@FInterID
 GROUP BY t2.FQCReqInterID,t2.FQCReqEntryID,u.FCoefficient
 UPDATE t1 SET t1.FQCheckQty=t1.FQCheckQty+t2.FSourCheckQty * t2.FCoefficient,t1.FQtyPass=t1.FQtyPass +t2.FSourPassQty * t2.FCoefficient 
,t1.FNotPassQty=t1.FNotPassQty +t2.FSourNotPassQty * t2.FCoefficient,t1.FSampleBreakQty=t1.FSampleBreakQty+t2.FSourSampleBreakQty * t2.FCoefficient 
 FROM POInStockEntry t1 INNER JOIN #TmpCk1 t2 ON t1.FInterID=t2.FQCReqInterID AND t1.FEntryID=t2.FQCReqEntryID
 UPDATE t1 SET t1.FAuxQCheckQty=t1.FQCheckQty/ISNull(u.FCoefficient,1),t1.FAuxQtyPass=t1.FQtyPass/ISNull(u.FCoefficient,1),t1.FAuxNotPassQty=t1.FNotPassQty/ISNull(u.FCoefficient,1),t1.FAuxSampleBreakQty=t1.FSampleBreakQty/ISNull(u.FCoefficient,1) 
 ,t1.FSecQCheckQty=CASE WHEN ISNull(t1.FSecCoefficient,0)=0 THEN 0 ELSE t1.FQCheckQty/t1.FSecCoefficient END,t1.FSecQtyPass=CASE WHEN ISNull(t1.FSecCoefficient,0)=0 THEN 0 ELSE t1.FQtyPass/t1.FSecCoefficient END 
 ,t1.FSecNotPassQty=CASE WHEN ISNull(t1.FSecCoefficient,0)=0 THEN 0 ELSE t1.FNotPassQty/t1.FSecCoefficient END,t1.FSecSampleBreakQty=CASE WHEN ISNull(t1.FSecCoefficient,0)=0 THEN 0 ELSE t1.FSampleBreakQty/t1.FSecCoefficient END 
 FROM POInStockEntry t1 INNER JOIN #TmpCk1 t2 ON t1.FInterID=t2.FQCReqInterID AND t1.FEntryID=t2.FQCReqEntryID
 LEFT JOIN t_MeasureUnit u ON t1.FUnitID=u.FMeasureUnitID 
 UPDATE t SET t.FQCheckQty = t.FQCheckQty+t2.FSourCheckQty * t2.FCoefficient
 ,t.FQtyPass = t.FQtyPass+t2.FSourPassQty * t2.FCoefficient 
 ,t.FNotPassQty = t.FNotPassQty+t2.FSourNotPassQty * t2.FCoefficient 
 FROM ICDeliveryNoticeEntry t INNER JOIN POInStockEntry t1 ON t.FID=t1.FSourceInterID AND t.FEntryID=t1.FSourceEntryID AND t1.FSourceTranType=1007351 
 INNER JOIN #TmpCk1 t2 ON t1.FInterID=t2.FQCReqInterID AND t1.FEntryID=t2.FQCReqEntryID
 UPDATE t SET t.FAuxQCheckQty = t.FQCheckQty/ISNull(u.FCoefficient,1) 
 ,t.FAuxQtyPass = t.FQtyPass/ISNull(u.FCoefficient,1) 
 ,t.FAuxNotPassQty = t.FNotPassQty/ISNull(u.FCoefficient,1) 
 FROM ICDeliveryNoticeEntry t INNER JOIN POInStockEntry t1 ON t.FID=t1.FSourceInterID AND t.FEntryID=t1.FSourceEntryID AND t1.FSourceTranType=1007351 
 INNER JOIN #TmpCk1 t2 ON t1.FInterID=t2.FQCReqInterID AND t1.FEntryID=t2.FQCReqEntryID
 INNER JOIN t_MeasureUnit u ON t.FUnitID = u.FMeasureUnitID 
 DELETE FROM SRM_DistributeDataInfo 
 WHERE FInterID IN (SELECT ISNULL(t1.FSourceInterID,0) FROM POInStockEntry t1
                    INNER JOIN #TmpCk1 t2 ON t1.FInterID=t2.FQCReqInterID AND t1.FEntryID=t2.FQCReqEntryID WHERE FSourceTranType=1007351)
 AND FTableName='v_IC_DeliveryNotice'
 INSERT INTO SRM_DistributeDataInfo(FInterID,FTableName,FSynDataWay)
 SELECT ISNULL(t1.FSourceInterID,0),'v_IC_DeliveryNotice',1 FROM POInStockEntry t1
 INNER JOIN #TmpCk1 t2 ON t1.FInterID=t2.FQCReqInterID AND t1.FEntryID=t2.FQCReqEntryID
 WHERE t1.FSourceTranType = 1007351
 DROP TABLE #TmpCk1
Update ICQCBill Set 
FCheckerID =  @FBillerID,FCheckDate =  CONVERT(varchar(20),GETDATE(),20) Where FTranType=1001000 And FInterID = @FInterID
 

----
 
if not exists(  select   1  from ICQCBillEntry where FInterID=@FInterID)
begin
    delete  ICQCBill where FInterID=@FInterID
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
