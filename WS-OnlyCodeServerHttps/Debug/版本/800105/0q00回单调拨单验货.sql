if (exists (select * from sys.objects where name = 'proc_CheckAllot1'))
drop proc proc_CheckAllot1
go
create proc proc_CheckAllot1
(
  @mainStr nvarchar(1000) --主表参数
 
)
as 
set nocount on     --开启一个模式，也就是不再刷新多少行受影响的信息，可以提高性能
set xact_abort on  --事务选项设置为出错全部回滚
begin tran declare
          @FDate varchar(20),            --单据日期 
          @FOrderID varchar(128),     
          @FPDAID varchar(128),   
          @FBillerID varchar(20),        --制单人ID 
          @FInterID int,             --单据ID 
          @FHeadSelfD0136 int,--是否良品
          @FBillNo varchar(128)
          set @FBillerID=dbo.getString(@mainStr,'|',1)     -- 制单人ID 
          set @FInterID=dbo.getString(@mainStr,'|',2)      --单据编号内码
          set @FOrderID=dbo.getString(@mainStr,'|',3)      --PDA单据编号
          set @FPDAID=dbo.getString(@mainStr,'|',4)      --PDA序号ID
             set  @FDate= CONVERT(varchar(20),GETDATE(),23) 
          --单个
          if exists(select 1 from ICStockBill where FInterID=@FInterID and FStatus=1)
          begin
          print convert(int, '回单失败,该单据已审核!')
          end
          select @FBillNo=FBillNo,@FHeadSelfD0136=FHeadSelfD0136 from ICStockBill where FInterID=@FInterID
if not exists(select 1 from t_PDABarCodeCheckBillNo where FInterID=@FInterID)
begin
insert into t_PDABarCodeCheckBillNo(FDate,FUserID,FInterID,FBillNo,FTypeID)values(@FDate,@FBillerID,@FInterID,@FBillNo,41)
declare @FID int
select @FID = FID from t_PDABarCodeCheckBillNo where FInterID=@FInterID
insert into t_PDABarCodeCheckBillNoEntry(FID,FPDAID,FOrderID,FBarCode,FItemID,FUnitID,FStockID,FStockPlaceID,FStockID_B,FStockPlaceID_B,FBatchNo,FKFPeriod,FKFDate,FQty)
select @FID,a.FPDAID,a.FOrderID,a.FBarCode,a.FItemID,a.FUnitID,a.FStockID,a.FStockPlaceID,b.FStockID,b.FStockPlaceID,a.FBatchNo,a.FKFPeriod,a.FKFDate,a.FQty from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID
insert into t_PDABarCodeSign_Allot(FInterID,FBarCode,FQty,FBillNo,FRemark,FStockID_Before,FStockPlaceID_Before,FStockID_Now,FStockPlaceID_Now,FStatus,FInterID_Before,FBillNo_Before,FDateUpLoad)
select @FInterID,b.FBarCode,a.FQty,@FBillNo,'调拨单验货',b.FStockID,b.FStockPlaceID,a.FStockID,a.FStockPlaceID,1,b.FInterIDAllot,b.FAllotBillNo,convert(varchar(20),GETDATE(),20) from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID
update t_PDABarCodeSign  set FStockID=t.FStockID,FStockPlaceID=t.FStockPlaceID,FInterIDAllot=@FInterID,FAllotBillNo=@FBillNo  from (select a.*,b.FStockID as AAA,b.FStockPlaceID as BBB from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode

end
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID



 
--更新库存 
if exists(    select 1 from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' and FValue=0)
begin
SET NOCOUNT ON
CREATE TABLE #TempBill
(FID INT IDENTITY (1,1),FBrNo VARCHAR(10) NOT NULL DEFAULT(''),
 FInterID INT NOT NULL DEFAULT(0),
 FEntryID INT NOT NULL DEFAULT(0),
 FTranType INT NOT NULL DEFAULT(0),
 FItemID INT NOT NULL DEFAULT(0),
 FBatchNo NVARCHAR(255) NOT NULL DEFAULT(''),
 FMTONo NVARCHAR(255) NOT NULL DEFAULT(''),
 FAuxPropID INT NOT NULL DEFAULT(0),
 FStockID INT NOT NULL DEFAULT(0),
 FStockPlaceID INT NOT NULL DEFAULT(0),
 FKFPeriod INT NOT NULL DEFAULT(0),
 FKFDate VARCHAR(20) NOT NULL DEFAULT(''),
 FSupplyID INT NOT NULL DEFAULT(0),
 FQty DECIMAL(28,10) NOT NULL DEFAULT(0),
 FSecQty DECIMAL(28,10) NOT NULL DEFAULT(0),
 FAmount DECIMAL(28,2)  NOT NULL DEFAULT(0) 
)
INSERT INTO #TempBill(FBrNo,FInterID,FEntryID,FTranType,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty,FAmount)
SELECT '',u1.FInterID,u1.FEntryID,41 AS FTranType,u1.FItemID,ISNULL(u1.FBatchNo,'') AS FBatchNo,ISNULL(u1.FMTONo,'') AS FMTONo,
       u1.FAuxPropID,ISNULL(u1.FDCStockID,0) AS FDCStockID,ISNULL(u1.FDCSPID,0) AS FDCSPID,ISNULL(u1.FKFPeriod,0) AS FKFPeriod,
       LEFT(ISNULL(CONVERT(VARCHAR(20),u1.FKFdate ,120),''),10) AS FKFDate,
1*u1.FQty AS FQty,1*u1.FSecQty AS FSecQty,1*u1.FAmtRef
FROM ICStockBillEntry u1 
WHERE u1.FInterID=@FInterID
INSERT INTO #TempBill(FBrNo,FInterID,FEntryID,FTranType,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FSupplyID,FQty,FSecQty,FAmount)
SELECT '',u1.FInterID,u1.FEntryID,41 AS FTranType,u1.FItemID,ISNULL(u1.FBatchNo,'') AS FBatchNo,ISNULL(u1.FMTONo,'') AS FMTONo,
       u1.FAuxPropID,ISNULL(u1.FSCStockID,0) AS FSCStockID,ISNULL(u1.FSCSPID,0) AS FSCSPID,ISNULL(u1.FKFPeriod,0) AS FKFPeriod,
       LEFT(ISNULL(CONVERT(VARCHAR(20),u1.FKFdate ,120),''),10) AS FKFDate,FEntrySupply,
-u1.FQty*1 AS FQty,-u1.FSecQty*1 AS FSecQty,1*(-1)*u1.FAmount
FROM ICStockBillEntry u1 
WHERE u1.FInterID=@FInterID
 order by  u1.FEntryID
 SELECT * INTO #TempBill2 FROM #TempBill 
UPDATE t1
SET t1.FQty=t1.FQty+(u1.FQty),
t1.FSecQty=t1.FSecQty+(u1.FSecQty)
FROM ICInventory t1 INNER JOIN
(SELECT FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FSupplyID
        ,SUM(FQty) AS FQty,SUM(FSecQty) AS FSecQty
 FROM #TempBill2
 GROUP BY FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FSupplyID
) u1
ON t1.FItemID=u1.FItemID AND t1.FBatchNo=u1.FBatchNo AND t1.FMTONo=u1.FMTONo AND t1.FAuxPropID=u1.FAuxPropID
   AND t1.FStockID=u1.FStockID AND t1.FStockPlaceID=u1.FStockPlaceID 
   AND t1.FKFPeriod=u1.FKFPeriod AND t1.FKFDate=u1.FKFDate AND t1.FSupplyID=u1.FSupplyID

DELETE u1
FROM ICInventory t1 INNER JOIN #TempBill2 u1
ON t1.FItemID=u1.FItemID AND t1.FBatchNo=u1.FBatchNo AND t1.FMTONo=u1.FMTONo AND t1.FAuxPropID=u1.FAuxPropID
   AND t1.FStockID=u1.FStockID AND t1.FStockPlaceID=u1.FStockPlaceID 
   AND t1.FKFPeriod=u1.FKFPeriod AND t1.FKFDate=u1.FKFDate AND t1.FSupplyID=u1.FSupplyID

INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FSupplyID,FQty,FSecQty)
SELECT '',FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FSupplyID,
       SUM(FQty) AS FQty,SUM(FSecQty) AS FSecQty
FROM #TempBill2
GROUP BY FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FSupplyID
DROP TABLE #TempBill2
DROP TABLE #TempBill
end

 Update t1
set t1.FSTQty = t1.FSTQty + dest.FQty,t1.FAuxSTQty = t1.FAuxSTQty + dest.FAuxQty
from QMINQCReqEntry t1
inner join ICStockBillEntry dest ON t1.FInterID=dest.FSourceInterID AND t1.FEntryID=dest.FSourceEntryID AND dest.FSourceTranType=706
where dest.FInterID =@FInterID
Set NoCount On Set Ansi_Warnings Off;
  WITH cte1 as
(
SELECT u2.FSourceinterid,u2.FPPBomEntryID,u2.FOrderEntryID,u2.FItemID,u2.FSCStockID,u2.FSCSPID,SUM(ISNULL(u2.FQty,0)) AS FStockQty
 FROM ICStockBillEntry u2
Where (u2.FSourceTrantype = 85 Or u2.FSourceTrantype = 1007105) And u2.FinterID = @FInterID
GROUP BY u2.FSourceinterid,u2.FPPBomEntryID,u2.FOrderEntryID,u2.FItemID,u2.FSCStockID,u2.FSCSPID
),
cte2 as
(
   select SUM(FStockQty) as FQty ,FSourceinterid,FPPBomEntryID  FROM cte1 GROUP BY FSourceinterid,FPPBomEntryID
)
SELECT FSourceinterid,FPPBomEntryID,FOrderEntryID,FItemID,FSCStockID,FSCSPID,FTransLateQty1,FTransLateAuxQty1
INTO #DataE41610F1170E4EF5B74BDCF1F5D8CA27
 FROM (
SELECT  CASE WHEN u1.FStockID = m2.FSCStockID AND u1.FSPID = m2.FSCSPID
THEN ISNULL(u1.FTransLateQty,0) - ISNULL((SELECT FQty from cte2 where cte2.FSourceinterid=u1.FICMOInterID AND cte2.FPPBomEntryID =u1.FEntryID),0)
ELSE ISNULL(u1.FTransLateQty,0)+ ISNULL((SELECT FQty from cte2 where cte2.FSourceinterid=u1.FICMOInterID AND cte2.FPPBomEntryID =u1.FEntryID),0) END as FTransLateQty1,
CASE WHEN u1.FStockID = m2.FSCStockID AND u1.FSPID = m2.FSCSPID
THEN ISNULL(u1.FTransLateAuxQty,0) - Round(ISNULL(cast((SELECT FQty from cte2 where cte2.FSourceinterid=u1.FICMOInterID AND cte2.FPPBomEntryID =u1.FEntryID) as float),0) / cast(t2.FCoEfficient as float),t1.FQtyDecimal)
ELSE ISNULL(u1.FTransLateAuxQty,0)+ Round(ISNULL(cast((SELECT FQty from cte2 where cte2.FSourceinterid=u1.FICMOInterID AND cte2.FPPBomEntryID =u1.FEntryID) as float),0) / cast(t2.FCoEfficient as float),t1.FQtyDecimal) END as FTransLateAuxQty1,m2.FSourceinterid,
m2.FPPBomEntryID,m2.FOrderEntryID,m2.FItemID,m2.FSCStockID,m2.FSCSPID FROM PPBOMEntry u1 
INNER JOIN cte1 m2 ON u1.FItemID=m2.FItemID AND u1.FEntryID=m2.FPPBomEntryID AND u1.FOrderEntryID=m2.FOrderEntryID AND u1.FICMOInterID=m2.FSourceinterid
  INNER JOIN t_ICItem t1 ON m2.FItemID=t1.FItemID
  INNER JOIN t_MeasureUnit t2 ON u1.FUnitID=t2.FMeasureUnitID
  Where (u1.FMaterielType = 371)
  ) t
Update e
SET FTransLateQty= Cast(IsNull(s.FTransLateQty1,0) as Decimal(10,4)),
   FTransLateAuxQty= Cast(IsNull(s.FTransLateAuxQty1,0) as Decimal(10,4))
FROM PPBOMEntry e INNER JOIN #DataE41610F1170E4EF5B74BDCF1F5D8CA27 s
 ON s.FSourceinterid=e.FICMOInterID AND s.FItemID=e.FItemID
AND s.FPPBomEntryID=e.FEntryID
AND e.FOrderEntryID=s.FOrderEntryID
 And e.FICMOInterID IN (SELECT FSourceInterID from ICStockBillEntry where FinterID=@FInterID)
drop TABLE #DataE41610F1170E4EF5B74BDCF1F5D8CA27
delete from t_LockStock where FInterID=@FInterID and FTranType=41;update a  set a.FLockFlag=0 from dbo.ICStockBillEntry a INNER JOIN dbo.ICStockBill b ON a.FInterID=b.FInterID WHERE a.FInterID=@FInterID and b.FTranType=41

declare @FHeadSelfD0139 varchar(128) --审核人
select @FHeadSelfD0139=FName from T_user where FUserID =@FBillerID
--通用
declare @FCheckRecordID int,
        @FTaskID int,
        @FUserID varchar(8000)
DELETE t_RoleMyFollowUpInfo WHERE FBillID=@FInterID and FClassTypeID=1041 
Insert INTO t_RoleMyFollowUpInfo(FBillID,FClassTypeID,FBillNo,FBillerID) Values(@FInterID,1041,@FBillNo,@FBillerID)

INSERT INTO ICClassMCRecord1041
      (FTemplateID,FBillID,FCheckerID,FCheckName,FCheckComment,FCheckDate,
       FCheckDirection,FCheckFrom,FCheckFrom_Name,
       FCheckTo,FCheckTo_Name,FAppointCheckerInfo,FAppointRouteInfo)
Values(68,@FInterID,@FBillerID,@FHeadSelfD0139,'', GetDate(),        1,-99,'未启动',       0,'开始','','-1')
if not exists(select 1 from ICClassMCRecord1041 where FBillID = @FInterID)
begin
INSERT INTO icClassMCStatus1041
      (FTemplateID,FBillID,FNeedSubmit,FTagIndex,FNextLevelTagIndex,FNextNodeTagIndex,FIsSpanBusinessNode)
Values(68,@FInterID,0,0,2000,2000,0)
if exists(select 1 from icclassmctemplatemap where FBillID=@FInterID)
begin
insert into icclassmctemplatemap values(1041,@FInterID,68)
end

end
--通用

UPDATE ICStockBill SET FMultiCheckStatus=16
 WHERE FInterID = @FInterID
 AND FTranType=41
  update icclassmctemplatemap set ftemplateid=68 where fclasstypeid=1041 and fbillid=@FInterID
if(@FHeadSelfD0136=990136)  --良品
begin
 if not exists(SELECT 1 FROM ICClassMCTableInfo where FTemplateID=68 and FTagIndex = 2000 and FCheckRight like '%'+@FBillerID+'%')  --仓库审核
  begin
  print convert(int,'用户'+@FHeadSelfD0139+'没有'+@FBillNo + '单据的审核权限')
  end
 UPDATE ICStockBill SET FCheckDate=@FDate,FStatus=1,FCheckerID=@FBillerID, FHeadSelfD0139 = @FHeadSelfD0139,FHeadSelfD0138 = GetDate() WHERE FInterID = @FInterID  AND FTranType=41
INSERT INTO ICClassMCRecord1041
      (FTemplateID,FBillID,FCheckerID,FCheckName,FCheckComment,FCheckDate,
       FCheckDirection,FCheckFrom,FCheckFrom_Name,
       FCheckTo,FCheckTo_Name,FAppointCheckerInfo,FAppointRouteInfo)
Values(68,@FInterID,@FBillerID,@FHeadSelfD0139,'', GetDate(),        2,0,'开始',       2000,'仓库审核','','-1')
select @FCheckRecordID = ident_Current('ICClassMCRecord1041')
INSERT INTO icClassMCStatus1041
      (FTemplateID,FBillID,FNeedSubmit,FTagIndex,FNextLevelTagIndex,FNextNodeTagIndex,FIsSpanBusinessNode)
Values(68,@FInterID,0,2000,1,1,1)

if exists(select 1 from ICClassMCTaskCenter where FTaskType = 0 and FBillID=@FInterID )
begin
  update ICClassMCTaskCenter set FUpdateDate=getdate()  where FTaskType = 0 and FBillID=@FInterID
end
else
begin
INSERT INTO ICClassMCTaskCenter(FCheckRecordID,FClassTypeID,FBillID,FReaded,FSenderID,FContent,FDate,FUpdateDate,FStatus,FNextLevelTag,FTaskType,FBillWidth,FBillHeight,FProcessUserID) 
Values(@FCheckRecordID,1041,@FInterID,0,@FBillerID,'【提交审核】你有调拨单_BOS(单据编号:'+@FBillNo+',审核节点：仓库审核)的审批流任务需要处理', GetDate() , GetDate(),0,2000,0,10665,10170,-2)
select @FTaskID= ident_Current('ICClassMCTaskCenter')
SELECT @FUserID=FCheckRight FROM ICClassMCTableInfo where FTemplateID=68 and FTagIndex = 2000
exec ('INSERT ICClassMCTaskUser(FTaskID,FUserID,FUserType)select '+@FTaskID+',FUserID,0 from t_user  where FUserID in('+@FUserID+')')
end
end
else if(@FHeadSelfD0136=990137)--不良品
begin 
  if not exists(SELECT 1 FROM ICClassMCTableInfo where FTemplateID=68 and FTagIndex = 2000 and FCheckRight like '%'+@FBillerID+'%')  --仓库审核
  begin
  print convert(int,'用户'+@FHeadSelfD0139+'没有'+@FBillNo + '单据的审核权限')
  end
  

INSERT INTO ICClassMCRecord1041
      (FTemplateID,FBillID,FCheckerID,FCheckName,FCheckComment,FCheckDate,
       FCheckDirection,FCheckFrom,FCheckFrom_Name,
       FCheckTo,FCheckTo_Name,FAppointCheckerInfo,FAppointRouteInfo)
Values(68,@FInterID,@FBillerID,@FHeadSelfD0139,'', GetDate(),        2,0,'开始',       2000,'仓库审核','','-1')
INSERT INTO icClassMCStatus1041
      (FTemplateID,FBillID,FNeedSubmit,FTagIndex,FNextLevelTagIndex,FNextNodeTagIndex,FIsSpanBusinessNode)
Values(68,@FInterID,0,2000,2001,2001,0)
select @FCheckRecordID = ident_Current('ICClassMCRecord1041')

if exists(select 1 from ICClassMCTaskCenter where FTaskType = 0 and FBillID=@FInterID )
begin
  update ICClassMCTaskCenter set FUpdateDate=getdate()  where FTaskType = 0 and FBillID=@FInterID
end
else
begin
INSERT INTO ICClassMCTaskCenter(FCheckRecordID,FClassTypeID,FBillID,FReaded,FSenderID,FContent,FDate,FUpdateDate,FStatus,FNextLevelTag,FTaskType,FBillWidth,FBillHeight,FProcessUserID) 
Values(@FCheckRecordID,1041,@FInterID,0,@FBillerID,'【提交审核】你有调拨单_BOS(单据编号:'+@FBillNo+',审核节点：仓库审核)的审批流任务需要处理', GetDate() , GetDate(),0,2000,0,10665,10170,-2)
select @FTaskID= ident_Current('ICClassMCTaskCenter')
SELECT @FUserID=FCheckRight FROM ICClassMCTableInfo where FTemplateID=68 and FTagIndex = 2000
exec ('INSERT ICClassMCTaskUser(FTaskID,FUserID,FUserType)select '+@FTaskID+',FUserID,0 from t_user  where FUserID in('+@FUserID+')')
end

  if not exists(SELECT 1 FROM ICClassMCTableInfo where FTemplateID=68 and FTagIndex = 2001 and FCheckRight like '%'+@FBillerID+'%')  --仓库审核
    begin
    print convert(int,'用户'+@FHeadSelfD0139+'没有'+@FBillNo + '单据的二级审核权限')
    end

INSERT INTO ICClassMCRecord1041
      (FTemplateID,FBillID,FCheckerID,FCheckName,FCheckComment,FCheckDate,
       FCheckDirection,FCheckFrom,FCheckFrom_Name,
       FCheckTo,FCheckTo_Name,FAppointCheckerInfo,FAppointRouteInfo)
Values(68,@FInterID,@FBillerID,@FHeadSelfD0139,'', GetDate(),        2,2000,'仓库审核',       2001,'供应链经理','','-1')
INSERT INTO icClassMCStatus1041
      (FTemplateID,FBillID,FNeedSubmit,FTagIndex,FNextLevelTagIndex,FNextNodeTagIndex,FIsSpanBusinessNode)
Values(68,@FInterID,0,2001,1,1,1)
select @FCheckRecordID = ident_Current('ICClassMCRecord1041')
 
     if exists(select 1 from ICClassMCTaskCenter where FTaskType = 0 and FBillID=@FInterID )
begin
  update ICClassMCTaskCenter set FUpdateDate=getdate()  where FTaskType = 0 and FBillID=@FInterID
end
else
begin
INSERT INTO ICClassMCTaskCenter(FCheckRecordID,FClassTypeID,FBillID,FReaded,FSenderID,FContent,FDate,FUpdateDate,FStatus,FNextLevelTag,FTaskType,FBillWidth,FBillHeight,FProcessUserID) 
Values(@FCheckRecordID,1041,@FInterID,0,@FBillerID,'【提交审核】你有调拨单_BOS(单据编号:'+@FBillNo+',审核节点：供应链经理)的审批流任务需要处理', GetDate() , GetDate(),0,2001,0,10665,10170,-2)
select @FTaskID= ident_Current('ICClassMCTaskCenter')
SELECT @FUserID=FCheckRight FROM ICClassMCTableInfo where FTemplateID=68 and FTagIndex = 2001
exec ('INSERT ICClassMCTaskUser(FTaskID,FUserID,FUserType)select '+@FTaskID+',FUserID,0 from t_user  where FUserID in('+@FUserID+')')
end

 UPDATE ICStockBill SET FCheckDate=@FDate,FStatus=1,FCheckerID=@FBillerID,FHeadSelfD0137 = @FHeadSelfD0139, FHeadSelfD0139 = @FHeadSelfD0139,FHeadSelfD0138 = GetDate(),FHeadSelfD0140 = GetDate() WHERE FInterID = @FInterID  AND FTranType=41
 delete from t_LockStock where FInterID=@FInterID and FTranType=41;update a  set a.FLockFlag=0 from dbo.ICStockBillEntry a INNER JOIN dbo.ICStockBill b ON a.FInterID=b.FInterID WHERE a.FInterID=@FInterID and b.FTranType=41
 
end 
else
begin
if not exists(SELECT 1 FROM ICClassMCTableInfo where FTemplateID=68 and FTagIndex = 2004 and FCheckRight like '%'+@FBillerID+'%')  --仓库审核
  begin
  print convert(int,'用户'+@FHeadSelfD0139+'没有'+@FBillNo + '单据的审核权限')
  end
 UPDATE ICStockBill SET FCheckDate=@FDate,FStatus=1,FCheckerID=@FBillerID, FHeadSelfD0139 = @FHeadSelfD0139,FHeadSelfD0138 = GetDate() WHERE FInterID = @FInterID  AND FTranType=41
if not exists(SELECT 1 FROM ICClassMCTableInfo where FTemplateID=68 and FTagIndex = 2000 and FCheckRight like '%'+@FBillerID+'%')  --仓库审核
  begin
  print convert(int,'用户'+@FHeadSelfD0139+'没有'+@FBillNo + '单据的审核权限')
  end
 UPDATE ICStockBill SET FCheckDate=@FDate,FStatus=1,FCheckerID=@FBillerID, FHeadSelfD0139 = @FHeadSelfD0139,FHeadSelfD0138 = GetDate() WHERE FInterID = @FInterID  AND FTranType=41
INSERT INTO ICClassMCRecord1041
      (FTemplateID,FBillID,FCheckerID,FCheckName,FCheckComment,FCheckDate,
       FCheckDirection,FCheckFrom,FCheckFrom_Name,
       FCheckTo,FCheckTo_Name,FAppointCheckerInfo,FAppointRouteInfo)
Values(68,@FInterID,@FBillerID,@FHeadSelfD0139,'', GetDate(),        2,0,'开始',       2000,'仓库审核','','-1')
select @FCheckRecordID = ident_Current('ICClassMCRecord1041')
INSERT INTO icClassMCStatus1041
      (FTemplateID,FBillID,FNeedSubmit,FTagIndex,FNextLevelTagIndex,FNextNodeTagIndex,FIsSpanBusinessNode)
Values(68,@FInterID,0,2000,1,1,1)

if exists(select 1 from ICClassMCTaskCenter where FTaskType = 0 and FBillID=@FInterID )
begin
  update ICClassMCTaskCenter set FUpdateDate=getdate()  where FTaskType = 0 and FBillID=@FInterID
end
else
begin
INSERT INTO ICClassMCTaskCenter(FCheckRecordID,FClassTypeID,FBillID,FReaded,FSenderID,FContent,FDate,FUpdateDate,FStatus,FNextLevelTag,FTaskType,FBillWidth,FBillHeight,FProcessUserID) 
Values(@FCheckRecordID,1041,@FInterID,0,@FBillerID,'【提交审核】你有调拨单_BOS(单据编号:'+@FBillNo+',审核节点：仓库审核)的审批流任务需要处理', GetDate() , GetDate(),0,2000,0,10665,10170,-2)
select @FTaskID= ident_Current('ICClassMCTaskCenter')
SELECT @FUserID=FCheckRight FROM ICClassMCTableInfo where FTemplateID=68 and FTagIndex = 2004
exec ('INSERT ICClassMCTaskUser(FTaskID,FUserID,FUserType)select '+@FTaskID+',FUserID,0 from t_user  where FUserID in('+@FUserID+')')
end
end
 

commit tran 
return;
if(0<>@@error)
	goto error1
error1:
	rollback tran; 