if not exists(select * from sysobjects where xtype='u' and name='t_PCPrintBarCode')
create table t_PCPrintBarCode
(
  FBillNo varchar(50),    --���ݱ��
  FInterID varchar(50),   --���id
  FItemID varchar(50),    --��Ʒid
  FQty varchar(50),       --��������
  FQtyInStore varchar(50),--װ������
  FQtyPrint varchar(50),  --�Ѵ�ӡ���� 
  FQtyResidue varchar(50),--ʣ������
  FNum varchar(50),       --��ʼ��װ�����
  FPrintState varchar(50),--��ӡ״̬
  FRemark varchar(50)     --��ӡ˵��
)
 