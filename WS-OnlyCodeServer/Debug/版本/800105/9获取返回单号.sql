if not exists(select * from sysobjects where xtype='u' and name='t_PDAGetBillNo')
begin
create table t_PDAGetBillNo
(
  FID int identity(1,1) primary key not null,
  FBillNo varchar(50),    --���ݱ��
  FSourceBillNo varchar(50),    --���ݱ��
  FPDAID varchar(50),   --���id
  FRemark varchar(8000)
)
CREATE UNIQUE INDEX t_PDAGetBillNo_Index_FPDAID
ON t_PDAGetBillNo (FPDAID)
end