if exists (select * from sys.objects where name = 't_FZICtockBillInsert')
drop  trigger t_FZICtockBillInsert
go
create trigger t_FZICtockBillInsert
on ICStockBill
for Insert
as 
declare @FInterID int,
     @FTranType int,
     @FDate datetime,
     @FBillerID int,
     @FROB int
select @FInterID=FInterID,@FTranType=FTranType,@FROB=FROB,@FBillerID=FBillerID from inserted
if(@FTranType in (1,2,5,10))
begin
if(@FROB=-1)
begin
 update t_PDABarCodeSign set  FIsInStore='未入库',FInterIDIn=@FInterID,FDateInStore=@FDate,FUserInStore=@FBillerID from (select FBarCode,FQty from t_PDABarCodeSign_In where  FInterID=@FInterID)  as t  where t.FBarCode=t_PDABarCodeSign.FBarCode    
     update  t_PDABarCodeSign_In set FStatus=1 where FInterID=@FInterID
end
else
begin
  update t_PDABarCodeSign  set FIsInStore='已入库',FInterIDIn=@FInterID,FDateInStore=@FDate,FUserInStore=@FBillerID where FInterIDIn=@FInterID 
       update  t_PDABarCodeSign_In set FStatus=1 where FInterID=@FInterID
end
end
if(@FTranType in (21,28,29,24))
begin
 
     update t_PDABarCodeSign set FQtyOut = FQtyOut+t.FQty,FInterIDOut=@FInterID,FDateOutStore=@FDate,FUserOutStore=@FBillerID from (select FBarCode,FQty from t_PDABarCodeSign_out where FInterID=@FInterID)  as t  where t.FBarCode=t_PDABarCodeSign.FBarCode    
            update t_PDABarCodeSign set FQtyOut = 0,FIsInStore='已入库',FIsOutStore='未出库' from (select FBarCode,FQty,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before from t_PDABarCodeSign_out where  FInterID=@FInterID and FIsInStore='未入库')  as t  where t.FBarCode=t_PDABarCodeSign.FBarCode  
     update  t_PDABarCodeSign_out set FStatus=1 where FInterID=@FInterID 
end
if(@FTranType=41)
begin
--if  exists(select 1 from ICStockBillEntry where FInterID=@FInterID)
-- begin
     update t_PDABarCodeSign set FStockID=t.FStockID_Now,FStockPlaceID =t.FStockPlaceID_Now ,FInterIDAllot=t.FInterID,FAllotBillNo=t.FBillNo from (select  FStockID_Before,FStockPlaceID_Before,FStockID_Now,FStockPlaceID_Now,FBarCode,FInterID,FBillNo from t_PDABarCodeSign_Allot where FInterID=@FInterID)  as t  where t.FBarCode=t_PDABarCodeSign.FBarCode
     update  t_PDABarCodeSign_Allot set FStatus=1 where FInterID=@FInterID
 --end
end



