if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeSign_Allot')
begin
create table t_PDABarCodeSign_Allot
(    
  FBarCode varchar(255) not null,  --条码
  FInterID int  not null, --出库单号
  FBillNo varchar(128),--单号
  FQty decimal(28,10),  --添加数量
  FStockID_Before int,-- 之前仓库
  FStockPlaceID_Before int,-- 之前仓位
  FStockID_Now int,-- 现在仓库
  FStockPlaceID_Now int,-- 现在仓位
  FStatus int, --0 关闭
  FTypeID int,  --单据类型
  FRemark varchar(255)--备注
) 
CREATE UNIQUE INDEX [idx_t_PDABarCodeSign_Allot] ON [dbo].t_PDABarCodeSign_Allot 
(
	FBarCode ASC,
	FInterID ASC 
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end
 IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Allot' AND T2.NAME='FInterID_Before')
begin
alter table t_PDABarCodeSign_Allot add FInterID_Before int
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Allot' AND T2.NAME='FBillNo_Before')
begin
alter table t_PDABarCodeSign_Allot add FBillNo_Before varchar(128)--单号 
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Allot' AND T2.NAME='FDateUpLoad')
begin
alter table t_PDABarCodeSign_Allot add FDateUpLoad datetime --回单日期
end