if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeSign_In')
begin
create table t_PDABarCodeSign_In
(    
  FBarCode varchar(255) not null,  --����
  FInterID int  not null, --��ⵥ��
  FBillNo varchar(128),--����
  FQty decimal(28,10),  --�������  
  FStatus int, --0 �ر�
  FTypeID int,  --��������
  FInterID_Before int, --֮ǰ�������� 
  FDateOutStore_Before datetime,--֮ǰ�ĳ�������
  FUserOutStore_Before varchar(128),--֮ǰ�ĳ�����
  FRemark_Before varchar(128),
  FRemark varchar(255),--��ע,
   FStockID int,--���ֿ�
  FStockPlaceID int,--����λ
  FDateUpLoad datetime --�ص�����
) 
CREATE UNIQUE INDEX [idx_t_PDABarCodeSign_In] ON [dbo].t_PDABarCodeSign_In 
(
	FBarCode ASC,
	FInterID ASC 
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end
 