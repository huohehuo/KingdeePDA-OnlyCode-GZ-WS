if not exists(select * from sysobjects where xtype='u' and name='a_DetailsTable')
begin
create table a_DetailsTable
(   
  FPDAID varchar(50),  --PDA���к� 
  FOrderID varchar(30),--����id 
  FBarCode varchar(200) not null,  --���� 
  FItemID int, --��Ʒid
  FStockID int,   --�ֿ�id
  FUnitID int,--��λID
  FStockPlaceID int, --��λid
  FBatchNo varchar(255),     --����
  FKFPeriod int,  --������,
  FKFDate varchar(20), --��������
  FInterID int,--��ӡ����
  FIsInStore varchar(20),--���״̬
  FIsOutStore varchar(20),--����״̬
  FQty decimal(28,10)  --�������  
) 
CREATE NONCLUSTERED INDEX [idx_t_a_DetailsTable] ON [dbo].a_DetailsTable 
(
	FBarCode asc 
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
--CREATE UNIQUE INDEX a_DetailsTable_Index_FBarCode
--ON a_DetailsTable (FBarCode) 
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='a_DetailsTable' AND T2.NAME='FPrice')
begin
alter table a_DetailsTable add FPrice decimal(28,10) DEFAULT 0 --����۸�
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='a_DetailsTable' AND T2.NAME='FType')
begin
alter table a_DetailsTable add FType  varchar(128) --
end