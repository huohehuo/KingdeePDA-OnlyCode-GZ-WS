if not exists(select * from sysobjects where xtype='u' and name='t_PCPrintBarCode')
create table t_PCPrintBarCode
(
  FBillNo varchar(50),    --单据编号
  FInterID varchar(50),   --编号id
  FItemID varchar(50),    --产品id
  FQty varchar(50),       --生产数量
  FQtyInStore varchar(50),--装箱数量
  FQtyPrint varchar(50),  --已打印数量 
  FQtyResidue varchar(50),--剩余数量
  FNum varchar(50),       --开始的装箱序号
  FPrintState varchar(50),--打印状态
  FRemark varchar(50)     --打印说明
)
 