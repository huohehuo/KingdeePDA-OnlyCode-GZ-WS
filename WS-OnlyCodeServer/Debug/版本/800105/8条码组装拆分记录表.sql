if not exists(select * from sysobjects where xtype='u' and name='t_PDAAssemble_Split')
begin
create table t_PDAAssemble_Split
(
  FUserID int,--制单人
  FID int IDENTITY(1,1) not null, --单据内码
  FInterID int not null, --箱码ID
  FBillNo varchar(255),--箱码  
  FBarCode varchar(255) not null,--条码
  FDate datetime,
  FRemark varchar(255) 
)
 CREATE NONCLUSTERED INDEX [idx1_t_PDAAssemble_Split] ON [dbo].t_PDAAssemble_Split 
(
	
	FBillNo asc,
	FInterID ASC
 
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
CREATE NONCLUSTERED INDEX [idx2_t_PDAAssemble_Split] ON [dbo].t_PDAAssemble_Split 
(
	
	FBarCode asc 
 
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end  
      