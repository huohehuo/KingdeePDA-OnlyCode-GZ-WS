if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeCheckBillNoEntry')
begin
create table t_PDABarCodeCheckBillNoEntry
(
  FID int  not null,     
  FPDAID varchar(50),  --PDA序列号
  FOrderID varchar(30),--单据id
  FBarCode varchar(200) not null,  --条码 
  FItemID int, --商品id
  FUnitID int,--单位ID
  FStockID int,   --仓库id
  FStockPlaceID int, --仓位id
  FStockID_B int,--之前仓库
  FStockPlaceID_B int,--之前仓位
  FBatchNo varchar(255),     --批次
  FKFPeriod int,  --保质期,
  FQty decimal(28,8),--数量   
  FKFDate varchar(20), --生产日期  
)  
end 

 