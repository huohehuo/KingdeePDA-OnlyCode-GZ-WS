if (exists (select * from sys.objects where name = 'proc_CheckProductIn'))
drop proc proc_CheckProductIn
go
create proc proc_CheckProductIn
(
  @mainStr nvarchar(1000) --�������
 
)
as 
set nocount on     --����һ��ģʽ��Ҳ���ǲ���ˢ�¶�������Ӱ�����Ϣ�������������
set xact_abort on  --����ѡ������Ϊ����ȫ���ع�
begin tran declare
          @FDate varchar(20),            --�������� 
          @FOrderID varchar(128),     
          @FPDAID varchar(128),   
          @FBillerID varchar(20),        --�Ƶ���ID 
          @FInterID int,             --����ID  
     @FBillNo varchar(128)
          set @FBillerID=dbo.getString(@mainStr,'|',1)     -- �Ƶ���ID 
          set @FInterID=dbo.getString(@mainStr,'|',2)      --���ݱ������
          set @FOrderID=dbo.getString(@mainStr,'|',3)      --PDA���ݱ��
          set @FPDAID=dbo.getString(@mainStr,'|',4)      --PDA���ID
             set  @FDate= CONVERT(varchar(20),GETDATE(),23) 
          --����
          if exists(select 1 from ICStockBill where FInterID=@FInterID and FStatus=1)
          begin
          print convert(int, '�ص�ʧ��,�õ��������!')
          end
          select @FBillNo=FBillNo from ICStockBill where FInterID=@FInterID
if not exists(select 1 from t_PDABarCodeSign_Check where FInterID=@FInterID)
begin
insert into t_PDABarCodeSign_Check(FInterID,FBarCode,FQty,FBillNo,FRemark,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before)select @FInterID,a.FBarCode,a.FQty,@FBillNo,'��Ʒ��ⵥ���',b.FInterIDOut,b.FDateOutStore,b.FUserOutStore from a_DetailsTable  a inner join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID
end
update t_PDABarCodeSign  set FStockID=t.FStockID,FStockPlaceID=t.FStockPlaceID,FInterIDIn=@FInterID,FDateInStore=@fdate,FIsInStore='�����',FUserInStore=@FBillerID,FRemark1='��Ʒ��ⵥ���'  from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID

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
INSERT INTO #TempBill(FBrNo,FInterID,FEntryID,FTranType,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FSupplyID,FQty,FSecQty,FAmount)
SELECT '',u1.FInterID,u1.FEntryID,2 AS FTranType,u1.FItemID,ISNULL(u1.FBatchNo,'') AS FBatchNo,ISNULL(u1.FMTONo,'') AS FMTONo,
       u1.FAuxPropID,ISNULL(u1.FDCStockID,0) AS FDCStockID,ISNULL(u1.FDCSPID,0) AS FDCSPID,ISNULL(u1.FKFPeriod,0) AS FKFPeriod,
       LEFT(ISNULL(CONVERT(VARCHAR(20),u1.FKFdate ,120),''),10) AS FKFDate,FEntrySupply,
1*u1.FQty AS FQty,1*u1.FSecQty AS FSecQty,1*u1.FAmount
FROM ICStockBillEntry u1 
WHERE u1.FInterID=@FInterID
 order by  u1.FEntryID
end
SELECT * INTO #TempBill2 FROM #TempBill 
UPDATE t1
SET t1.FQty=t1.FQty+(u1.FQty),
t1.FSecQty=t1.FSecQty+(u1.FSecQty)
FROM ICInventory t1 INNER JOIN
(SELECT FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate
        ,SUM(FQty) AS FQty,SUM(FSecQty) AS FSecQty
 FROM #TempBill2 
 GROUP BY FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate
) u1
ON t1.FItemID=u1.FItemID AND t1.FBatchNo=u1.FBatchNo AND t1.FMTONo=u1.FMTONo AND t1.FAuxPropID=u1.FAuxPropID
   AND t1.FStockID=u1.FStockID AND t1.FStockPlaceID=u1.FStockPlaceID 
   AND t1.FKFPeriod=u1.FKFPeriod AND t1.FKFDate=u1.FKFDate

DELETE u1
FROM ICInventory t1 INNER JOIN #TempBill2 u1
ON t1.FItemID=u1.FItemID AND t1.FBatchNo=u1.FBatchNo AND t1.FMTONo=u1.FMTONo AND t1.FAuxPropID=u1.FAuxPropID
   AND t1.FStockID=u1.FStockID AND t1.FStockPlaceID=u1.FStockPlaceID 
   AND t1.FKFPeriod=u1.FKFPeriod AND t1.FKFDate=u1.FKFDate

INSERT INTO ICInventory(FBrNo,FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,FQty,FSecQty)
SELECT '',FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate,
       SUM(FQty) AS FQty,SUM(FSecQty) AS FSecQty
FROM #TempBill2
GROUP BY FItemID,FBatchNo,FMTONo,FAuxPropID,FStockID,FStockPlaceID,FKFPeriod,FKFDate
DROP TABLE #TempBill2
DROP TABLE #TempBill

Update ICStockBill Set FCheckerID=@FBillerID,FStatus=1,FCheckDate=@FDate WHERE FInterID=@FInterID
   --Declare @FSouceTrantype  as int 
   Declare @bWriteMethod as Int --�Ƿ񰴱�׼����
   Declare @dRate as Decimal(23,10) --������
   Declare @FWIPQty as Decimal(23,10) --����Ʒ����
   Declare @FICMOInterID as int 
 SELECT @FICMOInterID = FICMOInterID  FROM ICStockBillEntry  WHERE FInterID = @FInterID
IF Exists(SELECT 1 FROM tempdb..sysobjects WHERE ID = OBJECT_ID(N'tempdb..#GradeProduct')) 
Drop Table #GradeProduct
 
  CREATE TABLE #GradeProduct(FICMOInterID INT DEFAULT(0), FItemID INT DEFAULT(0), FMaterielType  INT DEFAULT(0), FAuxQtyScrap DECIMAL(23,10))
 INSERT #GradeProduct
 SELECT c.FInterID,p.FItemID, p.FMaterielType, ISNULL(p.FAuxQtyMust/NULLIF(c.FAuxQty,1),0)  FROM ICMO c
 INNER JOIN  PPBOMEntry  p ON c.FInterID = p.FICMOInterID AND p.FMaterielType = 374 AND c.FInterID = @FICMOInterID
Declare @#Data5A2D7FD6827A422C95F78E7CB465CD0B table(FICMOinteriD int ,FStockQty Decimal(23,10),FChkPassItem INT, FItemID INT)
Insert into @#Data5A2D7FD6827A422C95F78E7CB465CD0B
   SELECT u2.FICMOinteriD,SUM(CAST(ISNULL(u2.FAuxQty,0) as Decimal(23,10))*Cast(tu.FCoefficient as Decimal(23,10))) as FStockQty,u2.FChkPassItem, u2.FItemID FROM ICStockBillEntry u2 
   INNER JOIN t_MeasureUnit tu ON tu.FItemID=u2.FUnitID 
   WHERE (u2.FPPBOMEntryID=0) and u2.FSourceTrantype>0 and  u2.FInterID= @FInterID
     AND NOT EXISTS(SELECT 1 FROM ICMORptEntry  
                    WHERE FInterID=u2.FSourceInterID AND FEntryID=u2.FSourceEntryID 
                    AND u2.FSourceTrantype =551 AND FMaterielType>0) 
   Group by u2.FICMOInterID,u2.FChkPassItem,tu.FCoefficient,u2.FItemID 
 UNION ALL
   SELECT u2.FICMOinteriD,SUM(CAST(ISNULL(u2.FAuxQty,0) as Decimal(23,10))*Cast(tu.FCoefficient as Decimal(23,10))) as FStockQty,u2.FChkPassItem, u2.FItemID FROM ICStockBillEntry u2 
   INNER JOIN t_MeasureUnit tu ON tu.FItemID=u2.FUnitID 
   INNER JOIN #GradeProduct G ON G.FICMOInterID = U2.FICMOInterID  
   WHERE (U2.FItemID = G.FItemID) and u2.FSourceTrantype>0 and  u2.FInterID= @FInterID
     AND NOT EXISTS(SELECT 1 FROM ICMORptEntry  
                    WHERE FInterID=u2.FSourceInterID AND FEntryID=u2.FSourceEntryID 
                    AND u2.FSourceTrantype =551 AND FMaterielType>0) 
   Group by u2.FICMOInterID,u2.FChkPassItem,tu.FCoefficient,u2.FItemID 
UPDATE m SET m.FStockQty = m.FStockQty / ISNULL(g.FAuxQtyScrap,1)
FROM @#Data5A2D7FD6827A422C95F78E7CB465CD0B m 
INNER JOIN #GradeProduct g ON g.FICMOInterID = m.FICMOinteriD AND m.FItemID = g.FItemID
DROP TABLE #GradeProduct
 Declare @FSourceTrantype as int 
 Declare @FROB as int 
 select @FROB=u1.FROB,@FSourceTrantype=t1.FSourceTrantype  --(select fsourcetrantype from icstockbillentry m1 where m1.finterid=t1.fsourceinterid) end) 
 from ICStockBillEntry t1 join ICStockBill u1 on t1.finterid=u1.finterid where t1.finterid=@FInterID
 if @FROB=1 or (@FROB=-1 And @FSourceTrantype in (551,581))
 Begin 
 IF (Select Count(*) From ICStockBillEntry Where FSourceTrantype =581 And FInterID=@FInterID)>0 
     Update src Set src.FQtyStock = src.FQtyStock+dest.FQty
     ,src.FAuxQtyStock = (src.FQtyStock+dest.FQty)/CAST(t1.FCoefficient as Decimal(23,10))
     From ICOperShift src INNER JOIN 
     (Select u1.FSourceInterID as FSourceInterID,u1.FSourceEntryID,u1.FItemID,SUM(u1.FQty) as FQty
     From  ICStockBillEntry u1 
     Where u1.FSourceTrantype=581 And u1.FInterID=@FInterID
     GROUP BY u1.FSourceInterID,u1.FSourceEntryID,u1.FItemID) dest 
     ON dest.FSourceInterID = src.FInterID
     AND dest.FItemID = src.FItemID 
     INNER JOIN t_MeasureUnit t1 ON src.FUnitID=t1.FItemID
 IF (Select Count(*) From ICStockBillEntry Where FSourceTrantype =551 And FInterID=@FInterID)>0 
     Update src Set src.FQtyStock = src.FQtyStock+dest.FQty
     ,src.FAuxQtyStock = (src.FQtyStock+dest.FQty)/CAST(t1.FCoefficient as Decimal(23,10))
     From ICMORptEntry src INNER JOIN 
     (Select u1.FSourceInterID as FSourceInterID,u1.FSourceEntryID,u1.FItemID,SUM(u1.FQty) as FQty
     From  ICStockBillEntry u1 
     INNER JOIN ICMORptEntry u2 ON u2.FInterID=u1.FSourceInterID AND u2.FEntryID=u1.FSourceEntryID 
     Where u1.FSourceTrantype=551 /*AND u2.FMaterielType=0*/ AND u1.FChkPassItem =1058 And u1.FInterID=@FInterID
     GROUP BY u1.FSourceInterID,u1.FSourceEntryID,u1.FItemID) dest 
     ON dest.FSourceInterID = src.FInterID
     AND dest.FItemID = src.FItemID and dest.FSourceEntryID=src.FEntryID 
     INNER JOIN t_MeasureUnit t1 ON src.FUnitID=t1.FItemID
     Update src Set src.FDiscardStockInQty = src.FDiscardStockInQty+dest.FQty
     ,src.FDiscardStockInAuxQty = (src.FDiscardStockInQty+dest.FQty)/CAST(t1.FCoefficient as Decimal(23,10))
     From ICMORptEntry src INNER JOIN 
     (Select u1.FSourceInterID as FSourceInterID,u1.FSourceEntryID,u1.FItemID,SUM(u1.FQty) as FQty
     From  ICStockBillEntry u1 
     INNER JOIN ICMORptEntry u2 ON u2.FInterID=u1.FSourceInterID AND u2.FEntryID=u1.FSourceEntryID 
     Where u1.FSourceTrantype=551 /*AND u2.FMaterielType=0*/ AND u1.FChkPassItem =1059 And u1.FInterID=@FInterID
     GROUP BY u1.FSourceInterID,u1.FSourceEntryID,u1.FItemID) dest 
     ON dest.FSourceInterID = src.FInterID
     AND dest.FItemID = src.FItemID and dest.FSourceEntryID=src.FEntryID 
     INNER JOIN t_MeasureUnit t1 ON src.FUnitID=t1.FItemID
     Update src Set src.FQtyStock = src.FQtyStock+dest.FQty
     ,src.FAuxQtyStock = (src.FQtyStock+dest.FQty)/CAST(t1.FCoefficient as Decimal(23,10))
     From ICMORptEntry src INNER JOIN 
     QMSourceInfo tmp ON src.FInterID = tmp.FQCReqInterID AND src.FEntryID = FQCReqEntryID
     INNER JOIN ICQCBill tmpHead ON tmpHead.FInterID = tmp.FInterID INNER JOIN 
     (Select u1.FSourceInterID as FSourceInterID,u1.FSourceEntryID,u1.FItemID,SUM(u1.FQty) as FQty
     From  ICStockBillEntry u1 
     Where u1.FSourceTrantype=1001001 AND u1.FChkPassItem =1058 And u1.FInterID=@FInterID
     GROUP BY u1.FSourceInterID,u1.FSourceEntryID,u1.FItemID) dest 
     ON dest.FSourceInterID = tmp.FInterID
     AND dest.FItemID = tmpHead.FItemID and dest.FSourceEntryID=tmp.FDetailID 
     INNER JOIN t_MeasureUnit t1 ON src.FUnitID=t1.FItemID
     Update src Set src.FDiscardStockInQty = src.FDiscardStockInQty+dest.FQty
     ,src.FDiscardStockInAuxQty = (src.FDiscardStockInQty+dest.FQty)/CAST(t1.FCoefficient as Decimal(23,10))
     From ICMORptEntry src INNER JOIN 
     QMSourceInfo tmp ON src.FInterID = tmp.FQCReqInterID AND src.FEntryID = FQCReqEntryID
     INNER JOIN ICQCBill tmpHead ON tmpHead.FInterID = tmp.FInterID INNER JOIN 
     (Select u1.FSourceInterID as FSourceInterID,u1.FSourceEntryID,u1.FItemID,SUM(u1.FQty) as FQty
     From  ICStockBillEntry u1 
     Where u1.FSourceTrantype=1001001 AND u1.FChkPassItem =1059 And u1.FInterID=@FInterID
     GROUP BY u1.FSourceInterID,u1.FSourceEntryID,u1.FItemID) dest 
     ON dest.FSourceInterID = tmp.FInterID
     AND dest.FItemID = tmpHead.FItemID and dest.FSourceEntryID=tmp.FDetailID 
     INNER JOIN t_MeasureUnit t1 ON src.FUnitID=t1.FItemID
 END
 ELSE
 Begin
 IF (Select Count(*) From ICStockBillEntry m1 inner join ICStockBillEntry t1 on m1.finterid=t1.fsourceinterid Where m1.FSourceTrantype =581 And t1.FInterID=@FInterID)>0 
     Update src Set src.FQtyStock = src.FQtyStock+dest.FQty
     ,src.FAuxQtyStock = (src.FQtyStock+dest.FQty)/CAST(t1.FCoefficient as Decimal(23,10))
     From ICOperShift src --INNER JOIN 
      --icstockbillentry m1 on m1.fsourceinterid=src.FInterID 
      INNER JOIN
     (Select m1.FSourceInterID,Dests.FItemID,SUM(Dests.FQty) as FQty From icstockbillentry m1 Inner join      (Select u1.FSourceInterID as FSourceInterID,u1.FSourceEntryID,u1.FItemID,SUM(u1.FQty) as FQty
     From  ICStockBillEntry u1 inner join icstockbillentry t1  on t1.finterid=u1.fsourceinterid AND t1.FEntryID=u1.FSourceEntryID 
     Where t1.FSourceTrantype=581 And  u1.FInterID=@FInterID
     GROUP BY u1.FSourceInterID,u1.FSourceEntryID,u1.FItemID) dests 
     ON dests.FSourceInterID = m1.Finterid and dests.FSourceEntryID=m1.FEntryID Group By m1.FSourceInterID,dests.FItemID) dest
     ON dest.FItemID = src.FItemID AND dest.fsourceinterid=src.FInterID 
     INNER JOIN t_MeasureUnit t1 ON src.FUnitID=t1.FItemID
 IF (Select Count(*) From ICStockBillEntry m1 inner join ICStockBillEntry t1 on m1.finterid=t1.fsourceinterid Where m1.FSourceTrantype =551 And t1.FInterID=@FInterID)>0 
     Update src Set src.FQtyStock = src.FQtyStock+dest.FQty
     ,src.FAuxQtyStock = (src.FQtyStock+dest.FQty)/CAST(t1.FCoefficient as Decimal(23,10))
     From ICMORptEntry src --INNER JOIN 
      --icstockbillentry m1 on m1.fsourceinterid=src.FInterID and m1.FSourceEntryID=src.FEntryID 
      INNER JOIN
     (Select m1.FSourceInterID,m1.FSourceEntryID,Dests.FItemID,SUM(Dests.FQty) as FQty From icstockbillentry m1 Inner join      (Select u1.FSourceInterID as FSourceInterID,u1.FSourceEntryID,u1.FItemID,SUM(u1.FQty) as FQty
     From  ICStockBillEntry u1 inner join icstockbillentry t1  on t1.finterid=u1.fsourceinterid AND t1.FEntryID=u1.FSourceEntryID 
     Where t1.FSourceTrantype=551 AND t1.FChkPassItem = 1058 And  u1.FInterID=@FInterID
     GROUP BY u1.FSourceInterID,u1.FSourceEntryID,u1.FItemID) dests 
     ON dests.FSourceInterID = m1.Finterid and dests.FSourceEntryID=m1.FEntryID Group By m1.FSourceInterID,m1.FSourceEntryID,dests.FItemID) dest
     ON dest.FItemID = src.FItemID AND dest.fsourceinterid=src.FInterID and dest.FSourceEntryID=src.FEntryID 
     INNER JOIN t_MeasureUnit t1 ON src.FUnitID=t1.FItemID
     Update src Set src.FDiscardStockInQty = src.FDiscardStockInQty+dest.FQty
     ,src.FDiscardStockInAuxQty = (src.FDiscardStockInQty+dest.FQty)/CAST(t1.FCoefficient as Decimal(23,10))
     From ICMORptEntry src --INNER JOIN 
      --icstockbillentry m1 on m1.fsourceinterid=src.FInterID and m1.FSourceEntryID=src.FEntryID 
      INNER JOIN
     (Select m1.FSourceInterID,m1.FSourceEntryID,Dests.FItemID,SUM(Dests.FQty) as FQty From icstockbillentry m1 Inner join      (Select u1.FSourceInterID as FSourceInterID,u1.FSourceEntryID,u1.FItemID,SUM(u1.FQty) as FQty
     From  ICStockBillEntry u1 inner join icstockbillentry t1  on t1.finterid=u1.fsourceinterid AND t1.FEntryID=u1.FSourceEntryID
     Where t1.FSourceTrantype=551 AND t1.FChkPassItem = 1059 And  u1.FInterID=@FInterID
     GROUP BY u1.FSourceInterID,u1.FSourceEntryID,u1.FItemID) dests 
     ON dests.FSourceInterID = m1.Finterid and dests.FSourceEntryID=m1.FEntryID Group By m1.FSourceInterID,m1.FSourceEntryID,dests.FItemID) dest
     ON dest.FItemID = src.FItemID AND dest.fsourceinterid=src.FInterID and dest.FSourceEntryID=src.FEntryID 
     INNER JOIN t_MeasureUnit t1 ON src.FUnitID=t1.FItemID
 END 
     Update src Set src.FQtyStock = src.FQtyStock+dest.FQty
     ,src.FAuxQtyStock = (src.FQtyStock+dest.FQty)/CAST(t1.FCoefficient as Decimal(23,10))
     From ICMORptEntry src INNER JOIN 
     QMSourceInfo tmp ON src.FInterID = tmp.FQCReqInterID AND src.FEntryID = FQCReqEntryID
     INNER JOIN ICQCBill tmpHead ON tmpHead.FInterID = tmp.FInterID INNER JOIN 
     (Select m1.FSourceInterID,m1.FSourceEntryID,Dests.FItemID,SUM(Dests.FQty) as FQty From icstockbillentry m1 Inner join 
     (Select u1.FSourceInterID as FSourceInterID,u1.FSourceEntryID,u1.FItemID,SUM(u1.FQty) as FQty
       From  ICStockBillEntry u1 inner join icstockbillentry t1  on t1.finterid=u1.fsourceinterid and t1.fentryid=u1.fsourceentryid
     Where u1.FSourceTrantype=2 AND u1.FChkPassItem =1058 And u1.FInterID=@FInterID
     GROUP BY u1.FSourceInterID,u1.FSourceEntryID,u1.FItemID) dests 
     ON dests.FSourceInterID = m1.Finterid and dests.FSourceEntryID=m1.FEntryID Group By m1.FSourceInterID,m1.FSourceEntryID,dests.FItemID) dest
     ON dest.FSourceInterID = tmp.FInterID
     AND dest.FItemID = tmpHead.FItemID and dest.FSourceEntryID=tmp.FDetailID 
     INNER JOIN t_MeasureUnit t1 ON src.FUnitID=t1.FItemID
     Update src Set src.FDiscardStockInQty = src.FDiscardStockInQty+dest.FQty
     ,src.FDiscardStockInAuxQty = (src.FDiscardStockInQty+dest.FQty)/CAST(t1.FCoefficient as Decimal(23,10))
     From ICMORptEntry src INNER JOIN 
     QMSourceInfo tmp ON src.FInterID = tmp.FQCReqInterID AND src.FEntryID = FQCReqEntryID
     INNER JOIN ICQCBill tmpHead ON tmpHead.FInterID = tmp.FInterID INNER JOIN 
     (Select m1.FSourceInterID,m1.FSourceEntryID,Dests.FItemID,SUM(Dests.FQty) as FQty From icstockbillentry m1 Inner join 
     (Select u1.FSourceInterID as FSourceInterID,u1.FSourceEntryID,u1.FItemID,SUM(u1.FQty) as FQty
       From  ICStockBillEntry u1 inner join icstockbillentry t1  on t1.finterid=u1.fsourceinterid and t1.fentryid=u1.fsourceentryid
     Where u1.FSourceTrantype=2 AND u1.FChkPassItem =1059 And u1.FInterID=@FInterID
     GROUP BY u1.FSourceInterID,u1.FSourceEntryID,u1.FItemID) dests 
     ON dests.FSourceInterID = m1.Finterid and dests.FSourceEntryID=m1.FEntryID Group By m1.FSourceInterID,m1.FSourceEntryID,dests.FItemID) dest
     ON dest.FSourceInterID = tmp.FInterID
     AND dest.FItemID = tmpHead.FItemID and dest.FSourceEntryID=tmp.FDetailID 
     INNER JOIN t_MeasureUnit t1 ON src.FUnitID=t1.FItemID

IF Object_id('TEMPDB..#TEMP_StockQty') IS NOT NULL 
  DROP TABLE #TEMP_StockQty
CREATE TABLE #TEMP_StockQty ( 
  FICMOinteriD INT,
  FStockQty Decimal(23,10), 
  FChkPassItem INT
  ) 
 
 INSERT #TEMP_StockQty
 SELECT   d.FICMOinteriD,sum(d.FStockQty) AS  FStockQty, D.FChkPassItem AS FChkPassItem
 FROM @#Data5A2D7FD6827A422C95F78E7CB465CD0B d
 GROUP BY d.FChkPassItem, d.FICMOinterID
 
 IF (select count(1) from #TEMP_StockQty ) >0  
   UPDATE u1 SET
   u1.FStockQty=ISNULL(u1.FStockQty,0)+ m2.FStockQty,
   u1.FAuxStockQty=ISNULL(u1.FAuxStockQty,0)+
   m2.FStockQty/cast(t2.FCoEfficient as float)
   FROM ICMO u1 INNER JOIN #TEMP_StockQty m2 on u1.FInterID=m2.FICMOInterID and m2.FChkPassItem = 1058 
   INNER JOIN t_MeasureUnit t2 ON u1.FUnitID=t2.FMeasureUnitID 
   UPDATE u1 SET
   u1.FDiscardStockInQty=ISNULL(u1.FDiscardStockInQty,0)+ m2.FStockQty,
   u1.FDiscardStockInAuxQty=ISNULL(u1.FDiscardStockInAuxQty,0)+
   m2.FStockQty/cast(t2.FCoEfficient as float)
   FROM ICMO u1 INNER JOIN  #TEMP_StockQty  m2 on u1.FInterID=m2.FICMOInterID and m2.FChkPassItem = 1059 
   INNER JOIN t_MeasureUnit t2 ON u1.FUnitID=t2.FMeasureUnitID 
   DROP TABLE #TEMP_StockQty

   IF OBJECT_ID('tempdb..#tmpPMCIndex','U') IS NOT NULL 
       DROP TABLE #tmpPMCIndex
   SELECT u1.FIndex
              ,CASE when m2.FQty>=(ISNULL(m2.FStockQty,0)+ISNULL(m2.FDiscardStockInQty,0)) THEN m2.FQty-(ISNULL(m2.FStockQty,0)+ISNULL(m2.FDiscardStockInQty,0))  ELSE 0 END AS  FWillInQty
              ,ISNULL(m2.FStockQty,0) AS FStockQty
   INTO #tmpPMCIndex 
   FROM ICPlan_PmcDetail u1 
   INNER JOIN  ICMO  m2 on u1.FRelInterID=m2.FInterID AND u1.FRelTranType=85 AND u1.FRelEntryID=0 AND u1.FBillType=1
   INNER JOIN @#Data5A2D7FD6827A422C95F78E7CB465CD0B  m3 on  m2.FInterID=m3.FICMOInterID 
   OPTION(RECOMPILE) 
   CREATE CLUSTERED INDEX PK_#tmpPMCIndex ON #tmpPMCIndex(FIndex)  

   UPDATE u1 
   SET  u1.FWillInQty=ISNULL(u2.FWillInQty,0)
          ,u1.FStockQty=ISNULL(u2.FStockQty,0)
   FROM ICPlan_PmcDetail u1 
   INNER JOIN  #tmpPMCIndex u2 ON u1.FIndex=u2.FIndex 
   DROP TABLE #tmpPMCIndex

Declare @#ICSTOCK5A2D7FD6827A422C95F78E7CB465CD0B table (FICMOInterID int ,FPPBOMEntryID int ,FItemID int ,FStockQty decimal(28,10))
Insert into @#ICSTOCK5A2D7FD6827A422C95F78E7CB465CD0B   SELECT u2.FICMOInterID,u2.FPPBOMEntryID,u2.FItemID,SUM(ISNULL(u2.FQty,0)) AS FStockQty  FROM ICStockBillEntry  u2 
   WHERE  u2.FSourceTrantype>0 and u2.FPPBOMEntryID>0 and  u2.FInterID= @FInterID
   GROUP BY u2.FICMOInterID,u2.FPPBOMEntryID,u2.FItemID
 IF (select count(1) from @#ICSTOCK5A2D7FD6827A422C95F78E7CB465CD0B ) >0 
BEGIN
   UPDATE u1 SET
   u1.FStockQty=ISNULL(u1.FStockQty,0)+ ISNULL(m2.FStockQty,0) ,
   u1.FAuxStockQty=ISNULL(u1.FAuxStockQty,0)+ISNULL(ROUND(
  ISNULL(cast(m2.FStockQty as float),0) /cast(t2.FCoEfficient as float),t1.FQtyDecimal),0)
   FROM PPBOMEntry u1 INNER JOIN @#ICSTOCK5A2D7FD6827A422C95F78E7CB465CD0B
     m2 
   ON u1.FItemID=m2.FItemID AND u1.FEntryID=m2.FPPBOMEntryID AND u1.FICMOInterID=m2.FICMOinterID 
   INNER JOIN t_ICItem t1 ON m2.FItemID=t1.FItemID 
   INNER JOIN t_MeasureUnit t2 ON u1.FUnitID=t2.FMeasureUnitID 
   WHERE u1.FMaterielType IN (372,373,374)  --������Ʒ�ȼ�Ʒ�� 

   UPDATE u1 
        SET  u1.FWillInQty=CASE WHEN ISNULL(u2.FQtyMust,0)>=ISNULL(u2.FStockQty,0) THEN ISNULL(u2.FQtyMust,0)-ISNULL(u2.FStockQty,0) ELSE 0 END 
              ,u1.FStockQty=ISNULL(u2.FStockQty,0)
   FROM ICPlan_PmcDetail u1 
   INNER JOIN  PPBOMEntry  u2 on  u1.FRelTranType=88 AND u1.FRelInterID=u2.FInterID AND  u1.FRelEntryID=u2.FEntryID AND u1.FBillType=16
   INNER JOIN @#ICSTOCK5A2D7FD6827A422C95F78E7CB465CD0B u3 on u1.FParentInterID=u3.FICMOInterID AND u3.FICMOInterID=u1.FParentInterID AND u1.FParentTrantype=85
   WHERE u2.FMaterielType IN (372,373,374)  --������Ʒ�ȼ�Ʒ�� 
END
SET NOCOUNT ON
DECLARE @DeductPoint AS INT,@DeductMode AS INT
SET @DeductMode=0
SET @DeductPoint=0
--������ʱ�� ��¼Դ����Ϣ������ⵥ�����񵥻㱨��
CREATE TABLE #SourceBill(
               FInterID INT--Դ������
              ,FEntryID INT DEFAULT -1--Դ����¼
              ,FItemID INT --����
              ,FICMOInterID INT--���񵥻�ί�ⶩ��������
              ,FICMOEntryID INT DEFAULT 0--ί�ⶩ���ķ�¼
              ,FMOCoefficient DECIMAL(23,10) DEFAULT 1--���񵥵�λ�Ļ�����
              ,FPPBOMEntryID INT DEFAULT 0--Ͷ�ϵ��ķ�¼
              ,FSourceInterID INT DEFAULT 0--Դ�����루�����ƺ��֣�
              ,FSourceEntryID INT DEFAULT 0--Դ����¼�������ƺ��֣�
              ,FSourceTranType INT DEFAULT 0--Դ�����ͣ������ƺ��֣�
              ,FQty DECIMAL(23,10)--�����������������ǵȼ�Ʒ��Ҫ����ɲ���Ʒ
              ,FROB INT DEFAULT 1--�����ֱ��
              ,FBillNO NVARCHAR(255)--Դ�����
              ,FTranType INT DEFAULT -1--Դ������
              ,FItemConsumeBillNO NVARCHAR(255) DEFAULT ''--���Ϻ��ñ��
              ,FDeleted INT DEFAULT 0--ɾ������
             )
CREATE TABLE #ItemConsume(
            FInterID INT DEFAULT -1
           ,FEntryID INT DEFAULT -1
           ,FSourceInterID INT--Դ������
           ,FSourceEntryID INT--Դ����¼
           ,FICMOInterID INT--��������
           ,FICMOEntryID INT DEFAULT 0--ί�ⶩ����¼
           ,FProductID INT--��ƷID
           ,FPPBOMBillNO NVARCHAR(255)--Ͷ�ϵ����
           ,FPPBOMInterID INT--Ͷ�ϵ�����
           ,FPPBOMEntryID INT--Ͷ�ϵ���¼
           ,FItemID INT--ԭ���Ϸ�¼
           ,FUnitID INT--��λ
           ,FQtyConsume DECIMAL(23,10)--���Ϻ���
           ,FAuxQtyConsume DECIMAL(23,10)--���õ�λ���Ϻ���
           ,FAccumulateQtyConsume DECIMAL(23,10)--�ۼƺ��� �����ܵ���������㱨��Ϣ��������
           ,FAccumulateAuxQtyConsume DECIMAL(23,10)--���õ�λ�ۼƺ��� �����ܵ���������㱨��Ϣ��������
           ,FPPBOMQtyConsume DECIMAL(23,10)--Ͷ�ϵ��ϵ��ۻ�����
           ,FPPBOMAuxQtyConsume DECIMAL(23,10)--Ͷ�ϵ��ϵĳ��õ�λ�ۻ�����
           ,FCancellation INT DEFAULT 0--���ϵ���
           ,FAuxQtyMust DECIMAL(23,10)  --���õ�λӦ������
           ,FQtyMust DECIMAL(23,10)  --������λӦ������
           ,FOperSN INT DEFAULT(0) --����� 
           ,FOperID INT DEFAULT(0) --���� 
           ,FProductPropID INT DEFAULT(0) --��Ʒ��������
           ,FItemPropID INT DEFAULT(0)) --���ϸ�������
CREATE TABLE #StockBill(
               FInterID INT--��ⵥ����
              ,FICMOInterID INT--��������
              ,FICMOEntryID INT DEFAULT 0--ί�ⶩ����¼
              ,FStockQty Decimal(23,10)--�����������
              ,FAccumulateStockQty Decimal(23,10) DEFAULT 0--�ۼ��������
              ,FPlanProductQty Decimal(23,10)  DEFAULT 0--Ԥ��������������
              ,FQtyFinish DECIMAL(23,10)--����ʵ����
             )

INSERT INTO #SourceBill(FInterID,FEntryID,FTranType,FDeleted)
VALUES(@FInterID,-1,2,0)
--����FTranType��FBillNO�����Ϣ
--����FTranType��FBillNO�����Ϣ
UPDATE t SET FBillNO=v.FBillNO,FTranType=v.FTranType,FROB=v.FRob
FROM #SourceBill t
INNER JOIN ICStockBill v On t.FInterID=v.FInterID
--��ȡ���������ķ�¼��Ϣ
 update s set s.FItemID = u.FItemID,s.FICMOInterID = u.FICMOInterID,s.FICMOEntryID =0,
s.FPPBOMEntryID = u.FPPBOMEntryID,s.FQty = CASE WHEN u.FPPBOMEntryID=0 THEN u.FQty ELSE u.FQty*ISNULL(tm.FCoefficient,1)/ISNULL(u1.FQtyScrap,1) END ,
s.FSourceTranType = u.FSourceTranType,s.FSourceInterID = u.FSourceInterID,s.FSourceEntryID=u.FSourceEntryID
FROM ICStockBillEntry u
INNER JOIN #SourceBill s ON s.FInterID=u.FInterID AND s.FEntryID = u.FEntryID
INNER JOIN ICMO m ON m.FInterID=u.FICMOInterID
LEFT JOIN t_MeasureUnit tm ON tm.FMeasureUnitID=m.FUnitID
LEFT JOIN PPBOMEntry u1 ON u1.FICMOInterID=u.FICMOInterID AND u1.FEntryID=u.FPPBOMEntryID AND u1.FMaterielType=374--�ȼ�Ʒ
LEFT JOIN PPBOMEntry u2 ON u2.FICMOInterID=u.FICMOInterID AND u2.FEntryID=u.FPPBOMEntryID AND u2.FMaterielType IN (372,373)--������Ʒ������
Where s.FEntryID <> -1 And (u.FPPBOMEntryID = 0 Or (u.FPPBOMEntryID > 0 And u1.FInterID Is Not Null)) 
 AND u2.FInterID IS NULL
INSERT INTO #SourceBill(FInterID,FEntryID,FItemID,FICMOInterID,FICMOEntryID,FPPBOMEntryID
                       ,FBillNO,FTranType,FROB,FMOCoefficient
                       ,FQty,FSourceTranType,FSourceInterID,FSourceEntryID,FDeleted)
SELECT u.FInterID,u.FEntryID,u.FItemID,u.FICMOInterID,0,u.FPPBOMEntryID
      ,s.FBillNO,s.FTranType,s.FROB,ISNULL(tm.FCoefficient,1)
      ,CASE WHEN u.FPPBOMEntryID=0 THEN u.FQty ELSE u.FQty*ISNULL(tm.FCoefficient,1)/ISNULL(u1.FQtyScrap,1) END AS FQty,u.FSourceTranType,u.FSourceInterID,u.FSourceEntryID,s.FDeleted
FROM ICStockBillEntry u
INNER JOIN #SourceBill s ON s.FInterID=u.FInterID 
INNER JOIN ICMO m ON m.FInterID=u.FICMOInterID
LEFT JOIN t_MeasureUnit tm ON tm.FMeasureUnitID=m.FUnitID
LEFT JOIN PPBOMEntry u1 ON u1.FICMOInterID=u.FICMOInterID AND u1.FEntryID=u.FPPBOMEntryID AND u1.FMaterielType=374--�ȼ�Ʒ
LEFT JOIN PPBOMEntry u2 ON u2.FICMOInterID=u.FICMOInterID AND u2.FEntryID=u.FPPBOMEntryID AND u2.FMaterielType IN (372,373)--������Ʒ������
WHERE s.FEntryID=-1 AND (u.FPPBOMEntryID=0 OR (u.FPPBOMEntryID>0 AND u1.FInterID IS NOT NULL))--����Ʒ��ȼ�Ʒ
      AND u2.FInterID IS NULL
DELETE FROM #SourceBill WHERE FEntryID=-1
--���µ��ݱ��
Update s SET FItemConsumeBillNO='MCR003650'
FROM #SourceBill s
INNER JOIN (SELECT TOP 1 FInterID,FEntryID FROM #SourceBill WHERE FItemConsumeBillNO='') st ON st.FInterID=s.FInterID AND st.FEntryID=s.FEntryID

--��ȡ���θ�����ⵥ���ۻ�����������ȼ�Ʒ����λ��������ɲ���Ʒ
INSERT INTO #StockBill(FInterID,FICMOInterID,FStockQty)
SELECT u.FInterID,u.FICMOInterID,SUM(u1.FQty) AS FStockQty
FROM ICStockBillEntry u
INNER JOIN #SourceBill u1 ON u1.FInterID=u.FInterID AND u.FEntryID=u1.FEntryID
GROUP BY u.FInterID,u.FICMOInterID
--�������񵥵Ĳ�����Ϣ���Ѿ�������Ϣ����Ϣ(��Ҫ����ȼ�Ʒ������)
UPDATE d SET FAccumulateStockQty=m.FStockQty+ISNULL(djp.FAccumulateStockQty,0)*ISNULL(tm.FCoefficient,1),FPlanProductQty=m.FQty
FROM #StockBill d
INNER JOIN ICMO m ON m.FInterID=d.FICMOInterID
LEFT JOIN t_MeasureUnit tm ON tm.FMeasureUnitID=m.FUnitID
LEFT JOIN (SELECT FICMOInterID,SUM(FStockQty/FQtyScrap) AS FAccumulateStockQty FROM PPBOMEntry WHERE FMaterielType=374 GROUP BY FICMOInterID) djp ON djp.FICMOInterID=d.FICMOInterID

--��ȡ���۵ĺ��ñ�
INSERT INTO #ItemConsume(FSourceInterID,FSourceEntryID,FICMOInterID,FICMOEntryID,FProductID,FPPBOMBillNO,FPPBOMInterID,FPPBOMEntryID,FItemID,FUnitID
                        ,FPPBOMQtyConsume,FPPBOMAuxQtyConsume
                        ,FQtyConsume
                        ,FAuxQtyConsume
                        ,FAccumulateQtyConsume
                        ,FAccumulateAuxQtyConsume
                        ,FQtyMust
                        ,FAuxQtyMust
                        ,FOperSN   --�����
                        ,FOperID --�������
                        ,FProductPropID  --��Ʒ��������
                        ,FItemPropID  --���ϸ�������
                        )
--��Ʒ��ⵥ
SELECT u1.FInterID,u1.FEntryID,u.FICMOInterID,0 AS FICMOEntryID,u1.FItemID,v.FBillNO,u.FInterID,u.FEntryID,u.FItemID,u.FUnitID
       ,u.FQtyConsume,u.FAuxQtyConsume
       --������λԤ�ƺ���
       ,CASE WHEN @DeductMode=0 THEN ROUND(u.FQtyMust*u1.FQty/st.FPlanProductQty,t.FQtyDecimal+1) 
        WHEN @DeductMode=1 THEN ROUND(u1.FQty*u.FQtyScrap/u1.FMOCoefficient,t.FQtyDecimal+1) 
        ELSE (CASE WHEN (u1.FTranType=2 AND u1.FROB=-1)  --�������ƺ���,�ú��ֵķ�̯���ֺ���
               THEN ROUND(ISNULL(B.FQtyConsume,0)*u1.FQty/ISNULL(B.FStockQty,1),t.FQtyDecimal)
               ELSE u1.FROB*ABS(ROUND((u.FQtyMust-u.FQtyConsume)*
                    (CASE WHEN (st.FPlanProductQty-st.FAccumulateStockQty+st.FStockQty)=0 THEN 1
                          ELSE u1.FQty/(st.FPlanProductQty-st.FAccumulateStockQty+st.FStockQty)
                     END),t.FQtyDecimal))
              END)
        END
       --Ԥ�ƺ���
       ,CASE WHEN @DeductMode=0 THEN ROUND(u.FAuxQtyMust*u1.FQty/st.FPlanProductQty,t.FQtyDecimal+1)
        WHEN @DeductMode=1 THEN ROUND(u1.FQty*u.FAuxQtyScrap/u1.FMOCoefficient,t.FQtyDecimal+1) 
        ELSE (CASE WHEN (u1.FTranType=2 AND u1.FROB=-1)  --�������ƺ���,�ú��ֵķ�̯���ֺ���
               THEN ROUND(ISNULL(B.FQtyConsume,0)*u1.FQty/ISNULL(B.FStockQty,1),t.FQtyDecimal)
               ELSE u1.FROB*ABS(ROUND((u.FQtyMust-u.FQtyConsume)*
                    (CASE WHEN (st.FPlanProductQty-st.FAccumulateStockQty+st.FStockQty)=0 THEN 1
                          ELSE u1.FQty/(st.FPlanProductQty-st.FAccumulateStockQty+st.FStockQty)
                     END)/(CASE WHEN tm.FCoefficient=0 THEN 1 ELSE tm.FCoefficient END),t.FQtyDecimal))
              END)
        END
        --������λ�ۼƺ���
       ,CASE WHEN @DeductMode=0 THEN ROUND(u.FQtyMust*st.FAccumulateStockQty/st.FPlanProductQty,t.FQtyDecimal) --�ƻ�����
        WHEN @DeductMode=1 THEN ROUND(st.FAccumulateStockQty*u.FQtyScrap/u1.FMOCoefficient,t.FQtyDecimal) --��׼����
        ELSE 0 END--ʣ������
        --�ۼƺ���
       ,CASE WHEN @DeductMode=0 THEN ROUND(u.FAuxQtyMust*st.FAccumulateStockQty/st.FPlanProductQty,t.FQtyDecimal) --�ƻ�����
        WHEN @DeductMode=1 THEN ROUND(st.FAccumulateStockQty*u.FAuxQtyScrap/u1.FMOCoefficient,t.FQtyDecimal)--��׼����
        ELSE 0 END--ʣ������
       --������λӦ������=������λԤ�ƺ��� ����ͳһ���� 3.1
       ,-1 AS FQtyMust
       --Ӧ������=Ԥ�ƺ��� ����ͳһ���� 3.1
       ,-1 AS FAuxQtyMust
       ,u.FOperSN
       ,u.FOperID,v.FAuxPropID as FProductPropID,u.FAuxPropID as FItemPropID
FROM PPBOMEntry u
INNER JOIN PPBOM v ON v.FInterID=u.FInterID
INNER JOIN t_ICItemBase t ON t.FItemID=u.FItemID--Ͷ�ϵ�ʹ�����ϣ�Ϊ�˻�ȡ����������λ
INNER JOIN t_MeasureUnit tm ON tm.FMeasureUnitID=u.FUnitID--Ͷ�ϵ�ʹ�õĵ�λ
INNER JOIN #StockBill st ON st.FICMOInterID=u.FICMOInterID--��ȡ����ⵥ�����ۼ������Ϣ����Ҫ��Ϊ�˿��ǵȼ�Ʒ����Ϣ�����Բ�ֱ�ӻ�ȡ������Ϣ��
INNER JOIN #SourceBill u1 ON u1.FICMOInterID=st.FICMOInterID AND u1.FInterID=st.FInterID
LEFT JOIN ICShop_ItemConsume cn ON cn.FIsAutoGen=0 AND cn.FSourceInterID=u1.FInterID AND cn.FSourceEntryID=u1.FEntryID AND cn.FSourceTranType=2--�������û��ֹ�����������
LEFT JOIN (--��ȡ���ֵ��ݼ�����������Ϣ
           SELECT bv.FSourceTranType,sun.FInterID,sun.FEntryID,sun.FQty AS FStockQty,bl.FPPBOMInterID,bl.FPPBOMEntryID,FQtyConsume,FAuxQtyConsume
           FROM ICShop_ItemConsumeEntry bl
           INNER JOIN ICShop_ItemConsume bv ON bv.FInterID=bl.FInterID
           INNER JOIN ICStockBillEntry sun ON sun.FInterID=bv.FSourceInterID AND sun.FEntryID=bv.FSourceEntryID 
           INNER JOIN #SourceBill sb ON sb.FSourceInterID=sun.FInterID AND sb.FSourceEntryID=sun.FEntryID AND sb.FSourceTranType=2 AND sb.FROB=-1--��ǰ���ֵ���
          ) B ON B.FInterID=u1.FSourceInterID AND B.FEntryID=u1.FSourceEntryID AND B.FSourceTranType=u1.FTranType AND B.FPPBOMInterID=u.FInterID AND B.FPPBOMEntryID=u.FEntryID AND B.FSourceTranType=2
WHERE u.FMaterielType NOT IN (372,373,374,376) AND u.FQtyMust>0 AND cn.FInterID IS NULL
ORDER BY u1.FInterID,u1.FEntryID,u.FInterID,u.FEntryID--����⣨�㱨������¼��Ͷ�ϵ���Ͷ�ϵ���¼����

--ͳһ���´�3.1
UPDATE #ItemConsume SET FQtyMust=FQtyConsume,FAuxQtyMust=FAuxQtyConsume
IF @@RowCount<>0--û����Ҫ���ɵĺ��ü�¼
BEGIN
DECLARE @DeviationPermmited AS DECIMAL(23,10)--��ȡƫ��ֵ
DECLARE @PPBOMInterID AS INT,@PPBOMEntryID AS INT,@ICMOInterID AS INT,@ICMOEntryID AS INT,@ProductID AS INT
DECLARE @MaxSize AS INT,@Loop AS INT
DECLARE @SumConsume AS DECIMAL(23,10),@SumAuxConsume AS DECIMAL(23,10),@SumConsume1 AS DECIMAL(23,10),@SumAuxConsume1 AS DECIMAL(23,10)
SELECT @DeviationPermmited=FValue FROM t_SystemProfile WHERE FCategory='SH' AND FKey='BackFlushScale'
--SELECT @DeviationPermmited=CONVERT(DECIMAL(23,10),1.0/POWER(convert(decimal(23,10),10),ISNULL(@DeviationPermmited,0)))
--�������� ����ʹ�õı��������߼���Ҫ�ر�ע�⣬�벻Ҫ�����޸�
SELECT IDENTITY(INT,1,1) AS FIndex,FICMOInterID, FICMOEntryID, FPPBOMInterID,FPPBOMEntryID,FProductID,FItemID,SUM(FQtyConsume) AS FQtyConsume,SUM(FAuxQtyConsume) AS FAuxQtyConsume--��ȡ�ۼ�Ԥ�ƺ��ã�һ�ŵ��ݴ��ڶ�����ͬ���񵥵�ʱ��
INTO #SUMItemConsume
FROM #ItemConsume GROUP BY FICMOInterID, FICMOEntryID, FPPBOMInterID,FPPBOMEntryID,FProductID,FItemID
ORDER BY FProductID,FPPBOMInterID,FPPBOMEntryID
UPDATE u SET FQtyConsume=CASE WHEN ABS(u1.FAccumulateQtyConsume-u1.FPPBOMQtyConsume-u.FQtyConsume)< CONVERT(DECIMAL(23,10),@DeviationPermmited*1.0/POWER(convert(decimal(23,10),10),ISNULL(t.FQtyDecimal,0))) THEN (u1.FAccumulateQtyConsume-u1.FPPBOMQtyConsume) ELSE u.FQtyConsume END
            ,FAuxQtyConsume=CASE WHEN ABS(u1.FAccumulateAuxQtyConsume-u1.FPPBOMAuxQtyConsume-u.FAuxQtyConsume)< CONVERT(DECIMAL(23,10),@DeviationPermmited*1.0/POWER(convert(decimal(23,10),10),ISNULL(t.FQtyDecimal,0))) THEN (u1.FAccumulateAuxQtyConsume-u1.FPPBOMAuxQtyConsume) ELSE u.FAuxQtyConsume END
FROM #SUMItemConsume u
INNER JOIN (
            SELECT DISTINCT FPPBOMInterID,FPPBOMEntryID,FAccumulateQtyConsume,FAccumulateAuxQtyConsume,FPPBOMQtyConsume,FPPBOMAuxQtyConsume FROM #ItemConsume
) u1 ON u1.FPPBOMInterID=u.FPPBOMInterID AND u1.FPPBOMEntryID=u.FPPBOMEntryID
INNER JOIN t_ICItemBase t ON t.FItemID=u.FItemID
SELECT @Loop=1,@MaxSize=COUNT(FPPBOMInterID) FROM #SUMItemConsume
WHILE @Loop<=@MaxSize
BEGIN
    SELECT @PPBOMInterID=FPPBOMInterID,@PPBOMEntryID=FPPBOMEntryID,@ICMOInterID = FICMOInterID, @ICMOEntryID = FICMOEntryID,@SumConsume=FQtyConsume,@SumAuxConsume=FAuxQtyConsume,@ProductID=FProductID FROM #SUMItemConsume WHERE FIndex=@Loop
    UPDATE u SET @SumConsume1=FQtyConsume=CASE WHEN ABS(@SumConsume-u.FQtyConsume)<CONVERT(DECIMAL(23,10),@DeviationPermmited*1.0/POWER(convert(decimal(23,10),10),ISNULL(t.FQtyDecimal,0))) THEN ROUND(@SumConsume,t.FQtyDecimal) ELSE ROUND(u.FQtyConsume,t.FQtyDecimal) END
                ,@SumAuxConsume1=FAuxQtyConsume=CASE WHEN ABS(@SumAuxConsume-u.FAuxQtyConsume)<CONVERT(DECIMAL(23,10),@DeviationPermmited*1.0/POWER(convert(decimal(23,10),10),ISNULL(t.FQtyDecimal,0))) THEN ROUND(@SumAuxConsume,t.FQtyDecimal) ELSE ROUND(u.FAuxQtyConsume,t.FQtyDecimal) END
                ,@SumConsume=@SumConsume-CASE WHEN ABS(@SumConsume-u.FQtyConsume)<CONVERT(DECIMAL(23,10),@DeviationPermmited*1.0/POWER(convert(decimal(23,10),10),ISNULL(t.FQtyDecimal,0))) THEN ROUND(@SumConsume,t.FQtyDecimal) ELSE ROUND(u.FQtyConsume,t.FQtyDecimal) END
                ,@SumAuxConsume=@SumAuxConsume-CASE WHEN ABS(@SumAuxConsume-u.FAuxQtyConsume)<CONVERT(DECIMAL(23,10),@DeviationPermmited*1.0/POWER(convert(decimal(23,10),10),ISNULL(t.FQtyDecimal,0))) THEN ROUND(@SumAuxConsume,t.FQtyDecimal) ELSE ROUND(u.FAuxQtyConsume,t.FQtyDecimal) END
    FROM #ItemConsume u
    INNER JOIN t_ICItemBase t ON t.FItemID=u.FItemID
    WHERE u.FPPBOMInterID=FPPBOMInterID AND u.FPPBOMEntryID=@PPBOMEntryID AND u.FICMOInterID = @ICMOInterID AND u.FICMOEntryID = @ICMOEntryID AND u.FProductID=@ProductID
    SET @Loop=@Loop+1
END
--����������λ�ͳ��õ�λ�Ļ������ ͳһ���ճ��õ�λ���������λ��ԭ��
UPDATE d SET FQtyConsume=ROUND(ROUND(d.FAuxQtyConsume,t.FQtyDecimal)*tm.FCoefficient,t.FQtyDecimal)
FROM #ItemConsume d
INNER JOIN PPBOMEntry u ON u.FInterID=d.FPPBOMInterID AND u.FEntryID=d.FPPBOMEntryID
INNER JOIN t_ICItemBase t ON t.FItemID=u.FItemID
INNER JOIN t_MeasureUnit tm ON tm.FMeasureUnitID=u.FUnitID
DROP TABLE #SUMItemConsume

UPDATE #ItemConsume SET FQtyMust=FQtyConsume,FAuxQtyMust=FAuxQtyConsume
--�������ü�¼������ü�¼
DECLARE @InterID AS INT,@EntryID AS INT,@Increase AS INT
DECLARE @SourceInterID AS INT,@SourceEntryID INT
SELECT @InterID=1001,@Increase=1
SELECT @Increase=ISNULL(COUNT(1),0) FROM (SELECT DISTINCT FSourceInterID,FSourceEntryID FROM #ItemConsume WHERE FInterID=-1) T--��ȡû������ļ�¼
IF @Increase>0
BEGIN
    EXEC GetICMaxNum 'ICShop_ItemConsume',@InterID OUTPUT,@Increase--��ȡ��������
    SELECT TOP 1 @SourceInterID=FSourceInterID,@SourceEntryID=FSourceEntryID FROM #ItemConsume
    UPDATE #ItemConsume SET @InterID=CASE WHEN (@SourceInterID=FSourceInterID AND @SourceEntryID=FSourceEntryID) THEN @InterID ELSE @InterID+1 END,@SourceInterID=FSourceInterID,@SourceEntryID=FSourceEntryID,FInterID=@InterID--��¼��Ӧ������Ͳ��Ϻ��õ�����
    SELECT TOP 1 @EntryID=0,@InterID=FInterID FROM #ItemConsume
    UPDATE #ItemConsume SET @EntryID=CASE WHEN @InterID=FInterID THEN @EntryID+1 ELSE 1 END,@InterID=FInterID,FEntryID=@EntryID--��¼��Ӧ������Ͳ��Ϻ��õ�����
END

--�������ɲ��Ϻ��ü�¼
INSERT INTO ICShop_ItemConsume(FInterID,FClassTypeID,FBillNO,FSourceInterID,FSourceEntryID
                              ,FSourceBillNO,FSourceTranType,FICMOInterID,FICMOEntryID,FItemID
                              ,FPPBOMInterID,FPPBOMBillNO,FPPBOMTranType,FIsAutoGen,FWIPDeductMode
                              ,FBillerID,FDate,FProductAuxPropID)
SELECT DISTINCT u.FInterID,1002525,u1.FItemConsumeBillNO,u.FSourceInterID,u.FSourceEntryID
                ,u1.FBillNO,u1.FTranType,u.FICMOInterID,u.FICMOEntryID,u.FProductID,u.FPPBOMInterID,u.FPPBOMBillNO,88,1,0,16394,CONVERT(NVARCHAR(10),GETDATE(),121),ISNULL(u.FProductPropID,0)
FROM #ItemConsume u
INNER JOIN #SourceBill u1 ON u1.FInterID=u.FSourceInterID AND u1.FEntryID=u.FSourceEntryID
--�������ɲ��Ϻ��ñ����¼
INSERT INTO ICShop_ItemConsumeEntry(FInterID,FIndex,FICMOInterID,FICMOEntryID,FPPBOMInterID,FPPBOMEntryID
                              ,FItemID,FUnitID,FQtyConsume,FAuxQtyConsume,FAuxQtyMust,FQtyMust,FOperSN,FOperID,FAuxPropID)
SELECT  u.FInterID,u.FEntryID,u.FICMOInterID,u.FICMOEntryID,u.FPPBOMInterID,u.FPPBOMEntryID
               ,u.FItemID,p.FUnitID,u.FQtyConsume,u.FAuxQtyConsume,u.FAuxQtyMust,u.FQtyMust,u.FOperSN,u.FOperID,ISNULL(p.FAuxPropID,0)
FROM #ItemConsume u
INNER JOIN #SourceBill u1 ON u1.FInterID=u.FSourceInterID AND u1.FEntryID=u.FSourceEntryID
INNER JOIN PPBOMEntry p ON p.FInterID=u.FPPBOMInterID AND p.FEntryID=u.FPPBOMEntryID
ORDER BY u.FInterID,u.FEntryID

--����Ͷ�ϵ�
UPDATE u SET FQtyConsume=u.FQtyConsume+(1*s.FQtyConsume)
            ,FAuxQtyConsume=u.FAuxQtyConsume+(1*s.FAuxQtyConsume)
            ,FWIPQty=u.FWIPQty-(1*s.FQtyConsume)--����WIP
            ,FWIPAuxQty=u.FWIPAuxQty-(1*s.FAuxQtyConsume)
FROM PPBOMEntry u
INNER JOIN (
        SELECT FPPBOMInterID,FPPBOMEntryID,SUM(FQtyConsume) AS FQtyConsume,SUM(FAuxQtyConsume) AS FAuxQtyConsume
        FROM #ItemConsume GROUP BY FPPBOMInterID,FPPBOMEntryID
) s ON s.FPPBOMInterID=u.FInterID AND s.FPPBOMEntryID=u.FEntryID

END
--ɾ����ʱ��
DROP TABLE #SourceBill
DROP TABLE #ItemConsume
DROP TABLE #StockBill


Declare @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B table (FICMOInterID int ,FStatus int)
Insert into @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B
 select Distinct FICMOInterID, 0 as FStatus 
 from @#Data5A2D7FD6827A422C95F78E7CB465CD0B
UPDATE Temp001 SET FSTATUS=(CASE WHEN ROUND(m1.FAuxStockQty+m1.FDiscardStockInAuxQty,t1.FQtyDecimal)>=ROUND(m1.FAuxInLowLimitQty,t1.FQtyDecimal)
                            THEN 0 ELSE 1 END)
FROM @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B Temp001
INNER JOIN ICMO m1  ON m1.FInterID=Temp001.FICMOInterID
INNER JOIN t_ICItemBase t1 ON m1.FItemID=t1.FItemID
if (select count(*) from PPBOMEntry inner join @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B t1 on t1.FICMOInterID =  PPBOMEntry.FICMOInterID where PPBOMEntry.FAuxStockQty > 0) = 0 
begin 
   UPDATE @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B SET FSTATUS = -1 
End 
Else 
begin 
   UPDATE Temp001 SET FSTATUS=(CASE WHEN FStatus=-1 THEN 0 ELSE FStatus END) + ISNULL(t1.FCount,0)  FROM @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B Temp001 
   LEFT JOIN (
           SELECT COUNT(1) AS FCount,u1.FICMOInterID 
           FROM PPBOMEntry u1 
           INNER JOIN @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B Temp ON u1.FICMOInterID=Temp.FICMOInterID
           INNER JOIN t_ICItemBase t1 ON u1.FItemID=t1.FItemID
           WHERE ROUND(u1.FWIPAuxQty,t1.FQtyDecimal)<>0
           GROUP BY u1.FICMOInterID
           ) t1 ON temp001.FICMOInterID=t1.FICMOInterID 
end 
DECLARE @StartDate AS DateTime
SELECT @StartDate=FStartDate FROM T_PeriodDate
    WHERE FYear=(SELECT TOP 1 ISNULL(FValue,0) FROM t_SystemProfile WHERE FKey ='CurrentYear' AND FCategory='IC')
      AND FPeriod=(SELECT TOP 1 ISNULL(FValue,0) FROM t_SystemProfile WHERE FKey ='CurrentPeriod' AND FCategory='IC')
Update m1
    SET FStatus=(CASE WHEN Temp001.FStatus=0 THEN 3 ELSE (CASE WHEN FCloseDate>=@StartDate THEN 1 ELSE m1.FStatus END) END)
   ,FCloseDate=(CASE WHEN Temp001.FStatus=0 THEN Convert(varchar(10) ,Getdate(),121) ELSE (CASE WHEN FCloseDate>=@StartDate THEN  Null ELSE m1.FCloseDate END) END)
   ,FCheckerID=(CASE WHEN Temp001.FStatus=0 THEN 16394 ELSE (CASE WHEN FCloseDate>=@StartDate THEN  Null ELSE m1.FCheckerID END) END)
   ,FMrpClosed=(CASE WHEN Temp001.FStatus=0 THEN 1 ELSE (CASE WHEN FCloseDate>=@StartDate THEN 0 ELSE m1.FMrpClosed END) END)
   ,FHandworkClose=(CASE WHEN Temp001.FStatus=0 THEN FHandworkClose ELSE 0 END)
FROM ICMO m1 INNER JOIN @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B Temp001 ON m1.FInterID=Temp001.FICMOInterID 

IF EXISTS(SELECT 1 FROM @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B t1 INNER JOIN ICMO t2 ON t2.FInterID=t1.FICMOInterID AND t2.FStatus=3)
BEGIN
    Delete t1 FROM ICPlan_PMCDetail t1 
    INNER JOIN @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B t2 ON t1.FParentInterID=t2.FICMOInterID  AND  t1.FParentTranType=85 AND t1.FParentEntryID=0 
    INNER JOIN ICMO t3 ON t3.FInterID=t2.FICMOInterID AND t3.FStatus=3
END

IF EXISTS(SELECT 1 FROM @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B t1 INNER JOIN ICMO t2 ON t2.FInterID=t1.FICMOInterID AND t2.FStatus<>3)
BEGIN
    INSERT INTO ICPlan_PMCDetail(FItemID,FUnitID,FNeedDate,FSrcTranType,FSrcInterID,FSrcEntryID,FRelTranType,FRelInterID,FRelEntryID, FParentTranType,FParentInterID,FParentEntryID,
    FNeedQty,FStockQty,FWillInQty,FPlanOrderInterID,FBillType,FBomCategory,FBomInterID,FPlanCategory,FAuxPropID)
    SELECT  v1.FItemID,v2.FUnitID,v1.FPlanFinishDate,v1.FSourceTranType,v1.FSourceInterId,v1.FSourceEntryID,85,v1.FInterID,0 AS FEntryID
            ,85 AS FParentTranType,v1.FInterID AS FParentInterID,0 AS FParentEntryID
            ,v1.FQty AS FNeedQty,v1.FStockQty+v1.FDiscardStockInQty,v1.FQty-v1.FStockQty-v1.FDiscardStockInQty AS FWillInQty,v1.FPlanOrderInterID,1 AS FBillType
            ,v1.FBomCategory,v1.FBomInterID,v1.FPlanCategory,v1.FAuxPropID
    From ICMO v1
    INNER JOIN t_ICItemBase v2 ON v1.FItemID=v2.FItemID
    INNER JOIN @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B v3 ON v1.FInterID=v3.FICMOInterID  AND v1.FStatus<>3 
    Where v1.FQty>(v1.FStockQty+v1.FDiscardStockInQty) 
    AND NOT EXISTS(SELECT 1 FROM ICPlan_PMCDetail  WITH(NOLOCK)  WHERE FRelTranType=85 AND FRelInterID=v1.FInterID AND FRelEntryID=0 AND FBillType=1)
    INSERT INTO ICPlan_PMCDetail(FItemID,FUnitID,FNeedDate,FRelTranType,FRelInterID,FRelEntryID,FParentTranType,FParentInterID,FParentEntryID,
    FNeedQty,FStockQty,FWillOutQty,FWillInQty, FBillType,FPlanCategory,FAuxPropID)
    SELECT t1.FItemID,t1.FUnitID,t1.FSendItemDate AS FNeedDate,88 AS FTranType,t1.FInterID,t1.FEntryID 
               ,ISNULL(t3.fTranType,0) AS FParentTranType,ISNULL(t2.FICMOInterID,0) AS FParentInterID,0 AS FParentEntryID
               ,t1.FQtyMust+t1.FQtySupply AS FNeedQty,t1.FStockQty,CASE WHEN t1.FMaterielType IN(372,373,374) THEN 0 ELSE t1.FQtyMust+t1.FQtySupply-t1.FStockQty  END FWillOutQty
               ,CASE WHEN t1.FMaterielType IN(372,373,374) THEN  t1.FQtyMust+t1.FQtySupply-t1.FStockQty  ELSE 0 END FWillInQty
               ,CASE WHEN t1.FMaterielType IN(372,373,374) THEN  16 ELSE 20 END FBillType
               ,ISNULL(t3.FPlanCategory,0) AS FPlanCategory,t1.FAuxPropID 
    FROM PPBOMEntry t1
    INNER JOIN PPBOM t2 ON t1.FInterID=t2.FInTerID AND t2.FOrderEntryID=0
    INNER JOIN ICMO t3 ON t2.FICMOInterID=t3.FInterID
    INNER JOIN t_MeasureUnit t4 ON t1.FUnitID=t4.FItemID 
    INNER JOIN @#ICMO5A2D7FD6827A422C95F78E7CB465CD0B t5 ON t3.FInterID=t5.FICMOInterID  AND t3.FStatus<>3 
    WHERE  t1.FMaterielType<>376  AND t1.FQtyMust+t1.FQtySupply>=t1.FStockQty
        AND NOT EXISTS(SELECT 1 FROM ICPlan_PMCDetail WITH(NOLOCK)  WHERE FRelTranType=88 AND FRelInterID=t1.FInterID AND FRelEntryID=t1.FEntryID)
    ORDER BY t1.FInterID,t1.FEntryID
END



commit tran 
return;
if(0<>@@error)
	goto error1
error1:
	rollback tran; 