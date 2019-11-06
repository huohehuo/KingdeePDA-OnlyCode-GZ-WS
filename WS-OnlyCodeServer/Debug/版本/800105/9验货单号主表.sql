if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeCheckBillNo')
begin
create table t_PDABarCodeCheckBillNo
(   
  FID int identity(1,1) primary key not null, 
  FDate datetime,--�ص�����
  FUserID int,--�Ƶ���
  FInterID int not null,--��������
  FBillNo varchar(255) not null,  --���ݱ��
  FTypeID int,--�������ͱ� 0���� 1����
  FRemark varchar(8000)     --��ע   
) 
CREATE UNIQUE INDEX t_PDABarCodeCheckBillNo_Index_FInterID
ON t_PDABarCodeCheckBillNo (FInterID)
end
 