if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeType')
begin
create table t_PDABarCodeType
(  
   FType int not null,-- 0通用版条码 1唯一码
   FIsAssemble int not null,--0不是组装 1组装
   FRemark varchar(1024)  --备注
)
insert into t_PDABarCodeType(FType,FIsAssemble)values(0,0)
end