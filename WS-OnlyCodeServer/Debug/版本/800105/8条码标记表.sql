if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeSign')
begin
create table t_PDABarCodeSign
(   
  FID int identity(1,1) primary key not null,
  FIndex int not null,   --ÿ�����
  FInterID int not null, --������ӡ��������
  FEntryID int not null, 
  FBillNo varchar(100),  --�����������
  FInterIDIn int,        --��ⵥ��id
  FInterIDOut int,      --���ⵥ��id 
  FInterIDAssemble int,   --��װ����
  FInterIDDisassemble int,--
  FItemID int not null,    --��Ʒid   
  FUnitID int,    --��Ʒid  
  FBarCode varchar(255) not null,  --����
  FDatePrint datetime,    --��ӡ����
  FDateInStore datetime,  --���ʱ��
  FDateOutStore datetime, --����ʱ��
  FIsInStore varchar(20),--���״̬
  FIsOutStore varchar(20),--����״̬  
  FUserInStore varchar(20), --PDA�����
  FUserOutStore varchar(20),  --PDA������
  FStockID int,   --�ֿ�id
  FStockPlaceID int, --��λid
  FBatchNo varchar(255),     --����
  FKFPeriod int,  --������,
  FKFDate varchar(20), --��������
  FDatePrintShort varchar(20),
  --FDateAdd varchar(128),--�������
  FQty decimal(28,10),   --��װ���� 
  FQtyOut decimal(28,10),--��������  
  FPrintType varchar(20),--��ӡ����
  FUserPrint int,--��ӡ��
  FRemark1 varchar(255),
  FRemark2 varchar(255),
  FRemark3 varchar(255),  
  FRemark4 varchar(255),
  FRemark5 varchar(255),
  FRemark6 varchar(255),
  FRemark7 varchar(255),
  FRemark8 varchar(255),
  FRemark9 varchar(255),
  FRemark10 varchar(255),
  FRemark varchar(50)     --��ע   
) 
CREATE UNIQUE INDEX PDABarCodeSign_Index_FBarCode
ON t_PDABarCodeSign (FBarCode)
 
CREATE NONCLUSTERED INDEX [idx_t_PDABarCodeSign] ON [dbo].t_PDABarCodeSign 
(
	[FItemID] ASC,
	FUnitID ASC, 
	FInterIDIn ASC,
	FInterIDOut ASC,
	FStockID ASC, 
	FStockPlaceID ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FIDLogistics')
begin
alter table t_PDABarCodeSign add FIDLogistics int default 0 --�������ⵥ��
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FOver')
begin
alter table t_PDABarCodeSign add FOver int default 0 --��������
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FOrderBillNo')
begin
alter table t_PDABarCodeSign add FOrderBillNo varchar(128) --���۶�����
end

IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FInterIDAllot')
begin
alter table t_PDABarCodeSign add FInterIDAllot int default 0 --������ID
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FAllotBillNo')
begin
alter table t_PDABarCodeSign add FAllotBillNo varchar(128) --����������
end
IF Not EXISTS ( SELECT 1 FROM SYSOBJECTS T1  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  WHERE T1.NAME='t_PDABarCodeSign' AND T2.NAME='FDateUpLoad')
begin
alter table t_PDABarCodeSign add FDateUpLoad datetime --�ص�����
end
