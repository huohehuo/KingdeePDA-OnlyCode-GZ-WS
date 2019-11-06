if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeSign_Check')
begin
create table t_PDABarCodeSign_Check
(    
  FBarCode varchar(255) not null,  --条码
  FInterID int  not null, --盘点方案ID
  FInterIDAssemble int,
  FQty decimal(28,10),  --添加数量
 
) 
CREATE UNIQUE INDEX [idx_t_PDABarCodeSign_Check] ON [dbo].t_PDABarCodeSign_Check 
(
	FBarCode ASC,
	FInterID ASC 
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end
 