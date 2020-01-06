if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeSign_Out')
begin
create table t_PDABarCodeSign_Out
(    
  FBarCode varchar(255) not null,  --����
  FInterID int  not null, --���ⵥ��
  FBillNo varchar(128),--����
  FQty decimal(28,10),  --�������  
  FStatus int, --0 �ر�
  FTypeID int,  --��������
  FInterID_Before int, --֮ǰ�������� 
  FDateOutStore_Before datetime,--֮ǰ�ĳ�������
  FUserOutStore_Before varchar(128),--֮ǰ�ĳ�����
  FRemark_Before varchar(128),
  FRemark varchar(255)--��ע
) 
CREATE UNIQUE INDEX [idx_t_PDABarCodeSign_Out] ON [dbo].t_PDABarCodeSign_Out 
(
	FBarCode ASC,
	FInterID ASC 
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FDateUpLoad')
begin
alter table t_PDABarCodeSign_Out add FDateUpLoad datetime --�ص�����
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FIsInStore')
begin
alter table t_PDABarCodeSign_Out add FIsInStore varchar(128) --���״̬
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FPrice')
begin
alter table t_PDABarCodeSign_Out add FPrice decimal(28,10) --����۸�
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FTypeID')
begin
alter table t_PDABarCodeSign_Out add FTypeID int --��������
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FStockID')
begin
alter table t_PDABarCodeSign_Out add FStockID int --����ֿ�
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FStockPlaceID')
begin
alter table t_PDABarCodeSign_Out add FStockPlaceID int --�����λ
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign_Out' AND T2.NAME='FMDUserID')
begin
alter table t_PDABarCodeSign_Out add FMDUserID int -- 
end