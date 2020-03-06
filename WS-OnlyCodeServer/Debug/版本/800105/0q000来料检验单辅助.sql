if (exists (select * from sys.objects where name = 'proc_MaterialsAllCheck2'))
    drop proc proc_MaterialsAllCheck2
go
create proc proc_MaterialsAllCheck2
(
  @FBillerID int,    --�Ƶ���id 
  @FID_SRC int,--ԭ��ID
  @FEntryID_SRC int, --��ϸID
  @FSCBillInterID int,--�ʼ췽��ID
  @FResult int,--�ʼ���
  @FSManagerID int,--�ʼ�Ա@FSManagerID=FSManagerID,@FFManagerID=FFManagerID,
  @FFManagerID int,--�ͼ���
  @FCheckQty decimal(28,10), --��������
  @FSampleBreakQty decimal(28,10), --��Ʒ�ƻ���
  @FPassQty decimal(28,10) --�ϸ���
)
as 
--------------����һ��ģʽ��Ҳ���ǲ���ˢ�¶�������Ӱ�����Ϣ�������������
set nocount on
--------------��ʼ�洢����
begin
--------------����ѡ������Ϊ����ȫ���ع�
set xact_abort on
--------------��������
begin tran
declare @FInterID varchar(20),     --����id
        @FBillNo varchar(50),      -- ���    
        @Fdate varchar(50),       --����  
        @FBizType int,  --
        @FItemID int,
        @FUnitID int,
        @FAuxPropID int,
        @FCheckMethod int,
        @FBatchNo varchar(128),
         @FSCBillNo varchar(128),--ԭ������
       @FBillNo_SRC varchar(128),--����֪ͨ������
       @FOrderBillNo varchar(128),--��������
      @FOrderEntryID int,
      @FOrderInterID int,
         @FNotPassQty decimal(28,10),--���ϸ�����
         @FSendUpQty decimal(28,10),--������
        @FSupplyID varchar(20)   --������λid 
        
         set @Fdate = convert(varchar(20),GETDATE(),23)
  
exec GetICMaxNum 'ICQCBill',@FInterID output 
------------------------------------------------------------�õ����
set @FBillNo = ''
 exec proc_GetICBillNo 1001000, @FBillNo out 
-----------------------------------------------------------�õ����
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
Values(1,@FInterID,-72,@FBillNo_SRC,@FID_SRC,@FEntryID_SRC,@FBillNo_SRC,@FEntryID_SRC,@FID_SRC,@FOrderBillNo,@FOrderEntryID,@FOrderInterID,@FItemID,@FUnitID,@FSendUpQty,@FCheckQty,@FPassQty,@FNotPassQty,0)

 
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
 UPDATE POInstock SET FChildren=FChildren+1,FStatus=2 WHERE FInterID IN (@FInterID)
 
 
UPDATE ICQCBill SET FConnectFlag=1 WHERE FInterID IN (
SELECT FID_SRC FROM QMSourceInfo WHERE (FClassID_SRC BETWEEN 1001000 AND 1001004) AND FInterID IN (@FInterID))UPDATE t SET t.FQCReqTranType=72,t.FOrderTranType=CASE WHEN t.FOrderInterID>0 THEN (CASE WHEN u.FBizType=12511 THEN 1007105 ELSE 71 END) ELSE 0 END
FROM QMSourceInfo t INNER JOIN ICQCBill u ON t.FInterID=u.FInterID 
WHERE u.FInterID= @FInterID AND t.FQCReqInterID>0 And t.FQCReqTranType=0 
UPDATE ICQCBill SET FType=CASE WHEN FBizType=12511 THEN 394 ELSE 390 END 
WHERE FInterID= @FInterID --���鵥�������
 IF  EXISTS(SELECT TOP 1 1 FROM QMQualityDefect WHERE FQCBillInterID=@FInterID AND FSourceTranType=1001000)
  begin  --���鵥����ʱ������ִ��
 --���鵥���ϸı����
 IF NOT EXISTS(      SELECT TOP 1 1 FROM ICQCBill t1      INNER JOIN QMQualityDefect t2 ON t1.FInterID=t2.FQCBillInterID      AND t1.FItemID=t2.FItemID AND t1.FInterID=@FInterID AND t1.FTranType=1001000) 
 BEGIN 
     --ɾ������ԭ���¼����
     DELETE v2 FROM QMQualityDefectEntry v2 
     INNER JOIN QMQualityDefect v1 ON v1.FID=v2.FID
     AND v1.FQCBillInterID=@FInterID AND v1.FSourceTranType=1001000

     DELETE FROM QMQualityDefect WHERE FQCBillInterID=@FInterID AND FSourceTranType=1001000 --ɾ������ԭ���¼��ͷ
 END

 --���鵥����δ�ı����
 ELSE BEGIN 
     --���鵥���������Ŀ�޸����
     UPDATE t2 SET t2.FInspectionID=0 FROM QMQualityDefectEntry t2 
     INNER JOIN QMQualityDefect t1 ON t1.FID=t2.FID 
     AND t1.FQCBillInterID=@FInterID
     AND NOT EXISTS 
     (SELECT TOP 1 1 FROM ICQCBillEntry t4 
         INNER JOIN ICQCBill t3 ON t4.FInterID=t3.FInterID 
         AND t4.FQCItemID=t2.FInspectionID AND t3.FInterID=@FInterID AND t3.FTranType=1001000) 

     --ɾ������ԭ���¼������Чȱ������ 
     DELETE v2 FROM QMQualityDefectEntry v2
     INNER JOIN QMQualityDefect v1 ON v1.FID=v2.FID
     AND v2.FInspectionID=0 AND v2.FQualityID=0 AND v2.FDefectAuxQty=0
     AND v1.FQCBillInterID=@FInterID AND v1.FSourceTranType=1001000

     --���鵥һ���޸����
     UPDATE t1 SET 
         t1.FCheckQty=s1.FCheckQty 
     FROM QMQualityDefect t1 
     INNER JOIN ICQCBill s1 ON t1.FQCBillInterID=s1.FInterID AND t1.FSourceTranType=s1.FTranType 
     AND s1.FInterID=@FInterID AND s1.FTranType=1001000
   END 
  end
 
--ͬ�������������ֵ 
--������ʱ�� 
 create table #tmpIDSampleValue(fid int not null) 
 insert into #tmpIDSampleValue 
 select t1.fid from qmsamplevalue t1 
 where Exists(select 1 from icqcbillentry t where t1.fid_src = @FInterID
 and t.FAnalysisMethodID=9 and t1.FAnalysisMethodID=10 And t1.findex_src=t.FDetailID) 
 and t1.FClassID_SRC = 1001000
--ɾ�� 
 delete from qmsamplevalueentry where fid in (select fid from #tmpIDSampleValue) 
 delete from qmsamplevalue where fid in (select fid from #tmpIDSampleValue) 
--���� 
 update qmsamplevalue set fqcitemid = i.fqcitemid,FAnalysisMethodID = i.FAnalysisMethodID,FSampleQty = ceiling(i.FSampleQty) from qmsamplevalue q join icqcbillentry i 
 on q.fid_src = i.finterid and q.findex_src = i.fdetailid 
 where q.fid_src = @FInterID
 and q.FClassID_SRC = 1001000
--ɾ����ʱ�� 
 drop table #tmpIDSampleValue

   
if not exists(  select   1  from ICQCBillEntry where FInterID=@FInterID)
begin
    delete  ICQCBill where FInterID=@FInterID
	goto error1
end 


commit tran 
return;
--------------���ڴ���
if(0<>@@error)
	goto error1
--------------�ع�����	
error1:
	rollback tran;
--------------�����洢����
end
