if not exists(select * from sysobjects where xtype='u' and name='t_PDAModifyPrices')
begin
create table t_PDAModifyPrices
(   
  FID int identity(1,1) primary key not null, 
  FItemID int not null,    --��Ʒid    
  FBarCode varchar(255) ,  --���� 
  FStockID int not null,   --�ֿ�id
  FStockPlaceID int, --��λid
  FPrice decimal(28,10), --�۸�
  FUserID int,--�Ƶ���
  FDate datetime,--�Ƶ�����  
)  
 
CREATE NONCLUSTERED INDEX [idx_t_PDAModifyPrices] ON [dbo].t_PDAModifyPrices
(
	[FItemID] ASC, 
	FStockID ASC, 
	FStockPlaceID ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end
 