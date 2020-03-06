if (exists (select * from sys.objects where name = 'proc_PushCheckProductPicking'))
drop proc proc_PushCheckProductPicking
go
create proc proc_PushCheckProductPicking
(
  @mainStr nvarchar(1000)--主表参数
 
)
as 
set nocount on     --开启一个模式，也就是不再刷新多少行受影响的信息，可以提高性能
set xact_abort on  --事务选项设置为出错全部回滚
begin tran declare
          @FDate varchar(20),            --单据日期   
          @FBillerID varchar(20),        --制单人ID 
          @FInterID varchar(50)              --单据ID  
          set @FBillerID=dbo.getString(@mainStr,'|',1)      --制单人ID 
            set @FInterID=dbo.getString(@mainStr,'|',2)      --单据编号内码 
            if exists(select 1 from ICStockBill where FInterID=@FInterID and FStatus=1)
		    begin
		    set @FInterID ='回单失败,该单据已审核!'
		    end
   set  @FDate= CONVERT(varchar(20),GETDATE(),23) 
  if exists (select 1 from t_systemprofile where Fkey in ('UPSTOCKWHENSAVE') AND FCateGory='IC' and Fvalue = 0)
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
 FQty DECIMAL(28,10) NOT NULL DEFAULT(0),
 FSecQty DECIMAL(28,10) NOT NULL DEFAULT(0)
)
INSERT INTO #TempBill(FBrNo,FInterID,FEntryID,FTranType,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
SELECT '',u1.FInterID,u1.FEntryID,24 AS FTranType,u1.FItemID,ISNULL(u1.FBatchNo,'') AS FBatchNo,ISNULL(u1.FMTONo,'') AS FMTONo,
       u1.FAuxPropID,ISNULL(u1.FDCStockID,0) AS FDCStockID,ISNULL(u1.FDCSPID,0) AS FDCSPID,ISNULL(u1.FKFPeriod,0) AS FKFPeriod,
       LEFT(ISNULL(CONVERT(VARCHAR(20),u1.FKFdate ,120),''),10) AS FKFDate,
-1*u1.FQty AS FQty,-1*u1.FSecQty AS FSecQty
FROM ICStockBillEntry u1 
WHERE u1.FInterID=@FInterID
 order by  u1.FEntryID
  
   --改变库存
UPDATE t1
SET t1.FQty=t1.FQty+(u1.FQty),
t1.FSecQty=t1.FSecQty+(u1.FSecQty)
FROM ICInventory t1 INNER JOIN
(SELECT FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate
        ,SUM(FQty) AS FQty,SUM(FSecQty) AS FSecQty
 FROM #TempBill
 GROUP BY FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate
) u1
ON t1.FItemID=u1.FItemID AND t1.FBatchNo=u1.FBatchNo AND t1.FMTONo=u1.FMTONo AND t1.FAuxPropID=u1.FAuxPropID
   AND t1.FStockID=u1.FStockID AND t1.FStockPlaceID=u1.FStockPlaceID 
   AND t1.FKFPeriod=u1.FKFPeriod AND t1.FKFDate=u1.FKFDate

DELETE u1
FROM ICInventory t1 INNER JOIN #TempBill u1
ON t1.FItemID=u1.FItemID AND t1.FBatchNo=u1.FBatchNo AND t1.FMTONo=u1.FMTONo AND t1.FAuxPropID=u1.FAuxPropID
   AND t1.FStockID=u1.FStockID AND t1.FStockPlaceID=u1.FStockPlaceID 
   AND t1.FKFPeriod=u1.FKFPeriod AND t1.FKFDate=u1.FKFDate

INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
SELECT '',FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,
       SUM(FQty) AS FQty,SUM(FSecQty) AS FSecQty
FROM #TempBill
GROUP BY FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate

DROP TABLE #TempBill
  end
 
 UPDATE ICStockBill SET FOrderAffirm=0 WHERE FInterID=@FInterID
 Update ICStockBill Set FCheckerID=@FBillerID,FStatus=1,FCheckDate=@FDate WHERE FInterID=@FInterID
IF  Exists(Select 1 From Syscolumns Where name='FMultiCheckStatus'  AND id=object_id('ICStockBill'))
Exec ('UPDATE ICStockBill SET  FMultiCheckStatus=0     WHERE FInterID='+@FInterID+' ')
 
commit tran 
return;
if(0<>@@error)
	goto error1
error1:
	rollback tran;