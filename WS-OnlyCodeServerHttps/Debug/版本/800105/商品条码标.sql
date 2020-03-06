if not exists(select * from sysobjects where xtype='u' and name='t_PCPrintGoodsBarCode')
create table t_PCPrintGoodsBarCode
(  
  FBarCode varchar(100),    --条码
  FItemID varchar(50),    --产品id     
  FPrintState varchar(50),--打印状态
  FRemark varchar(50)     --打印说明
)
 