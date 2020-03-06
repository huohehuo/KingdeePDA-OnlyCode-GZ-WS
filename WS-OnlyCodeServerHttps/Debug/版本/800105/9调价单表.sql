if not exists(select * from sysobjects where xtype='u' and name='t_PDAModifyPrices')
begin
create table t_PDAModifyPrices
(   
  FID int identity(1,1) primary key not null, 
  FItemID int not null,    --产品id    
  FBarCode varchar(255) ,  --条码 
  FStockID int not null,   --仓库id
  FStockPlaceID int, --仓位id
  FPrice decimal(28,10), --价格
  FUserID int,--制单人
  FDate datetime,--制单日期  
)  
 
CREATE NONCLUSTERED INDEX [idx_t_PDAModifyPrices] ON [dbo].t_PDAModifyPrices
(
	[FItemID] ASC, 
	FStockID ASC, 
	FStockPlaceID ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end
 