if not exists(select * from sysobjects where xtype='u' and name='t_PDASaleOrderEntry')
begin
create table t_PDASaleOrderEntry
(   
   FSourceEntryID int,--���۶�����ϸID
   FDCStockID int,--�ֿ�ID
   FDCSPID int,--��λID
   FBatchNo varchar(255),--����
   FKFDate varchar(20),--��������
   FKFPeriod int,--������
)  
end
 