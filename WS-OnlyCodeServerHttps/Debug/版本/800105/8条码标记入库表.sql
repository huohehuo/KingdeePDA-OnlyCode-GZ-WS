if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeSign_In')
begin
create table t_PDABarCodeSign_In
(    
  FBarCode varchar(255) not null,  --条码
  FInterID int  not null, --入库单号
  FBillNo varchar(128),--单号
  FQty decimal(28,10),  --添加数量  
  FStatus int, --0 关闭
  FTypeID int,  --单据类型
  FInterID_Before int, --之前单据内码 
  FDateOutStore_Before datetime,--之前的出库日期
  FUserOutStore_Before varchar(128),--之前的出库人
  FRemark_Before varchar(128),
  FRemark varchar(255),--备注,
   FStockID int,--入库仓库
  FStockPlaceID int,--入库仓位
  FDateUpLoad datetime --回单日期
) 
CREATE UNIQUE INDEX [idx_t_PDABarCodeSign_In] ON [dbo].t_PDABarCodeSign_In 
(
	FBarCode ASC,
	FInterID ASC 
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end
 