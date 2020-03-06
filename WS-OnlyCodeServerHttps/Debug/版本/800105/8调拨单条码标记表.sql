if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeSign_Allot')
begin
create table t_PDABarCodeSign_Allot
(    
  FBarCode varchar(255) not null,  --����
  FInterID int  not null, --���ⵥ��
  FBillNo varchar(128),--����
  FQty decimal(28,10),  --�������
  FStockID_Before int,-- ֮ǰ�ֿ�
  FStockPlaceID_Before int,-- ֮ǰ��λ
  FStockID_Now int,-- ���ڲֿ�
  FStockPlaceID_Now int,-- ���ڲ�λ
  FStatus int, --0 �ر�
  FTypeID int,  --��������
  FRemark varchar(255)--��ע
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
alter table t_PDABarCodeSign_Allot add FBillNo_Before varchar(128)--���� 
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Allot' AND T2.NAME='FDateUpLoad')
begin
alter table t_PDABarCodeSign_Allot add FDateUpLoad datetime --�ص�����
end