if not exists(select * from sysobjects where xtype='u' and name='t_PDAAssemble')
begin
create table t_PDAAssemble
(
  FUserID int,--�Ƶ���
  FInterID int IDENTITY(1,1) not null, --��������
  FBillerID int ,   --�Ƶ���
  FBillNo varchar(255) not null,    --���ݱ�� 
  FItemID int  not null,    --��Ʒid
  FUnitID  int,      --��λid 
  FIndex int not null,           --���
  FQty decimal(28,10),                  --������
  FDate datetime not null ,    --��������
  FDatePrint datetime,        --��ӡ����
  FStatusPrint varchar(20),  --��ӡ״̬
  FPDAID varchar(255),--PDA���
  FInterIDIntStore int,      --��ⵥ������
  FInterIDOuttStore int,     --���ⵥ������
  FStatus int,               --δ��װ֮ǰ״̬ 0 δ��� 1����� 
  FTypeID int,               -- �������� 0 ��װ�� 1 ��ж��
  FRemark varchar(255), 
  FRemark1 varchar(255),
  FRemark2 varchar(255),  
  FRemark3 varchar(255), 
  FRemark4 varchar(255) 
)
CREATE UNIQUE INDEX t_PDAAssemble_FBillNo
ON t_PDAAssemble (FBillNo)
end  
   