if not exists(select * from sysobjects where xtype='u' and name='t_PDASaleOrderIndex1')
begin
create table t_PDASaleOrderIndex1
(
  FID int identity(1,1) primary key not null,
  FCustID int,--¿Í»§ID
  FShortDate varchar(20),--
  FIndex int --ÐòºÅ 
)
CREATE NONCLUSTERED INDEX [idx_t_PDASaleOrderIndex1] ON [dbo].t_PDASaleOrderIndex1
(
	FShortDate ASC, 
	FCustID ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
end 