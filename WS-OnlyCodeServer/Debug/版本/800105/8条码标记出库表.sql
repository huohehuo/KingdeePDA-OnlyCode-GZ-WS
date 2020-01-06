if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeSign_Out')
begin
create table t_PDABarCodeSign_Out
(    
  FBarCode varchar(255) not null,  --条码
  FInterID int  not null, --出库单号
  FBillNo varchar(128),--单号
  FQty decimal(28,10),  --添加数量  
  FStatus int, --0 关闭
  FTypeID int,  --单据类型
  FInterID_Before int, --之前单据内码 
  FDateOutStore_Before datetime,--之前的出库日期
  FUserOutStore_Before varchar(128),--之前的出库人
  FRemark_Before varchar(128),
  FRemark varchar(255)--备注
) 
CREATE UNIQUE INDEX [idx_t_PDABarCodeSign_Out] ON [dbo].t_PDABarCodeSign_Out 
(
	FBarCode ASC,
	FInterID ASC 
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FDateUpLoad')
begin
alter table t_PDABarCodeSign_Out add FDateUpLoad datetime --回单日期
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FIsInStore')
begin
alter table t_PDABarCodeSign_Out add FIsInStore varchar(128) --入库状态
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FPrice')
begin
alter table t_PDABarCodeSign_Out add FPrice decimal(28,10) --出库价格
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FTypeID')
begin
alter table t_PDABarCodeSign_Out add FTypeID int --出库类型
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FStockID')
begin
alter table t_PDABarCodeSign_Out add FStockID int --出库仓库
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FStockPlaceID')
begin
alter table t_PDABarCodeSign_Out add FStockPlaceID int --出库仓位
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FMDUserID')
begin
alter table t_PDABarCodeSign_Out add FMDUserID int -- 
end