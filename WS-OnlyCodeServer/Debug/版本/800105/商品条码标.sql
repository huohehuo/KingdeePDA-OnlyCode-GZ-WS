if not exists(select * from sysobjects where xtype='u' and name='t_PCPrintGoodsBarCode')
create table t_PCPrintGoodsBarCode
(  
  FBarCode varchar(100),    --����
  FItemID varchar(50),    --��Ʒid     
  FPrintState varchar(50),--��ӡ״̬
  FRemark varchar(50)     --��ӡ˵��
)
 