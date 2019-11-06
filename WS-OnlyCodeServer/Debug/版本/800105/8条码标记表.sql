if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeSign')
begin
create table t_PDABarCodeSign
(   
  FID int identity(1,1) primary key not null,
  FIndex int not null,   --每天序号
  FInterID int not null, --所属打印单据内码
  FEntryID int not null, 
  FBillNo varchar(100),  --所属订单编号
  FInterIDIn int,        --入库单号id
  FInterIDOut int,      --出库单号id 
  FInterIDAssemble int,   --组装单号
  FInterIDDisassemble int,--
  FItemID int not null,    --产品id   
  FUnitID int,    --产品id  
  FBarCode varchar(255) not null,  --条码
  FDatePrint datetime,    --打印日期
  FDateInStore datetime,  --入库时间
  FDateOutStore datetime, --出库时间
  FIsInStore varchar(20),--入库状态
  FIsOutStore varchar(20),--出库状态  
  FUserInStore varchar(20), --PDA入库人
  FUserOutStore varchar(20),  --PDA出库人
  FStockID int,   --仓库id
  FStockPlaceID int, --仓位id
  FBatchNo varchar(255),     --批次
  FKFPeriod int,  --保质期,
  FKFDate varchar(20), --生产日期
  FDatePrintShort varchar(20),
  --FDateAdd varchar(128),--添加日期
  FQty decimal(28,10),   --包装数量 
  FQtyOut decimal(28,10),--出库数量  
  FPrintType varchar(20),--打印类型
  FUserPrint int,--打印人
  FRemark1 varchar(255),
  FRemark2 varchar(255),
  FRemark3 varchar(255),  
  FRemark4 varchar(255),
  FRemark5 varchar(255),
  FRemark6 varchar(255),
  FRemark7 varchar(255),
  FRemark8 varchar(255),
  FRemark9 varchar(255),
  FRemark10 varchar(255),
  FRemark varchar(50)     --备注   
) 
CREATE UNIQUE INDEX PDABarCodeSign_Index_FBarCode
ON t_PDABarCodeSign (FBarCode)
 
CREATE NONCLUSTERED INDEX [idx_t_PDABarCodeSign] ON [dbo].t_PDABarCodeSign 
(
	[FItemID] ASC,
	FUnitID ASC, 
	FInterIDIn ASC,
	FInterIDOut ASC,
	FStockID ASC, 
	FStockPlaceID ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FIDLogistics')
begin
alter table t_PDABarCodeSign add FIDLogistics int default 0 --物流出库单号
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FOver')
begin
alter table t_PDABarCodeSign add FOver int default 0 --返工次数
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FOrderBillNo')
begin
alter table t_PDABarCodeSign add FOrderBillNo varchar(128) --销售订单号
end

IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FInterIDAllot')
begin
alter table t_PDABarCodeSign add FInterIDAllot int default 0 --调拨单ID
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FAllotBillNo')
begin
alter table t_PDABarCodeSign add FAllotBillNo varchar(128) --调拨单单号
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FDateUpLoad')
begin
alter table t_PDABarCodeSign add FDateUpLoad datetime --回单日期
end
