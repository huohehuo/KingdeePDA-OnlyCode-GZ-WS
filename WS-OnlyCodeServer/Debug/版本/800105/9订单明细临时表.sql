if not exists(select * from sysobjects where xtype='u' and name='t_PDASaleOrderEntry')
begin
create table t_PDASaleOrderEntry
(   
   FSourceEntryID int,--销售订单明细ID
   FDCStockID int,--仓库ID
   FDCSPID int,--仓位ID
   FBatchNo varchar(255),--批号
   FKFDate varchar(20),--生产日期
   FKFPeriod int,--保质期
)  
end
 