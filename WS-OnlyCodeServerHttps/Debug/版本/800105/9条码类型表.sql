if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeType')
begin
create table t_PDABarCodeType
(  
   FType int not null,-- 0ͨ�ð����� 1Ψһ��
   FIsAssemble int not null,--0������װ 1��װ
   FRemark varchar(1024)  --��ע
)
insert into t_PDABarCodeType(FType,FIsAssemble)values(0,0)
end