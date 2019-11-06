if exists (select * from sys.objects where name = 't_FZICtockBilldelete')
drop  trigger t_FZICtockBilldelete
go
create trigger t_FZICtockBilldelete
on ICStockBill
for delete
as 
declare @FInterID int,
        @FQty decimal(28,10),  --添加数量  
     @FTranType int,
     @FROB int
select @FInterID=FInterID,@FTranType=FTranType,@FROB=FROB from deleted
if(@FTranType in (1,2,5,10))
begin
if(@FROB=-1)
begin
  update t_PDABarCodeSign set FIsInStore='已入库',FInterIDIn=t.FInterID_Before,FDateInStore=t.FDateOutStore_Before,FUserInStore=t.FUserOutStore_Before from (select FBarCode,FQty,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before from t_PDABarCodeSign_In where  FInterID=@FInterID)  as t  where t.FBarCode=t_PDABarCodeSign.FBarCode    
     update  t_PDABarCodeSign_In set FStatus=0 where FInterID=@FInterID
end
else
begin
 update t_PDABarCodeSign set FIsInStore='未入库',FInterIDIn=t.FInterID_Before,FDateInStore=t.FDateOutStore_Before,FUserInStore=t.FUserOutStore_Before from (select FBarCode,FQty,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before from t_PDABarCodeSign_In where  FInterID=@FInterID)  as t  where t.FBarCode=t_PDABarCodeSign.FBarCode  
  update  t_PDABarCodeSign_In set FStatus=0 where FInterID=@FInterID
 
 --update t_PDABarCodeSign  set FIsInStore='未入库' where FInterIDIn=@FInterID 
end
end
if(@FTranType in (21,28,29,24))
begin 
     update t_PDABarCodeSign set FQtyOut = FQtyOut-t.FQty,FInterIDOut=t.FInterID_Before,FDateOutStore=t.FDateOutStore_Before,FUserOutStore=t.FUserOutStore_Before from (select FBarCode,FQty,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before from t_PDABarCodeSign_out where  FInterID=@FInterID)  as t  where t.FBarCode=t_PDABarCodeSign.FBarCode    
     update t_PDABarCodeSign set FQtyOut = 0,FIsInStore='未入库' from (select FBarCode,FQty,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before from t_PDABarCodeSign_out where  FInterID=@FInterID and FIsInStore='未入库')  as t  where t.FBarCode=t_PDABarCodeSign.FBarCode    
     update  t_PDABarCodeSign_out set FStatus=0 where FInterID=@FInterID 
end
if(@FTranType = 41)  
begin
--if  exists(select 1 from ICStockBillEntry where FInterID=@FInterID)
-- begin 
    update t_PDABarCodeSign set FStockID=t.FStockID_Before,FStockPlaceID =t.FStockPlaceID_Before,FInterIDAllot=t.FInterID_Before,FAllotBillNo=t.FBillNo_Before  from (select  FStockID_Before,FStockPlaceID_Before,FStockID_Now,FStockPlaceID_Now,FBarCode,FInterID_Before,FBillNo_Before from t_PDABarCodeSign_Allot where FInterID=@FInterID)  as t  where t.FBarCode=t_PDABarCodeSign.FBarCode
     update  t_PDABarCodeSign_Allot set FStatus=0 where FInterID=@FInterID
 --end
end



