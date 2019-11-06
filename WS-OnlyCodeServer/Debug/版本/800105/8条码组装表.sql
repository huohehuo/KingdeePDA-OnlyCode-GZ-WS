if not exists(select * from sysobjects where xtype='u' and name='t_PDAAssemble')
begin
create table t_PDAAssemble
(
  FUserID int,--制单人
  FInterID int IDENTITY(1,1) not null, --单据内码
  FBillerID int ,   --制单人
  FBillNo varchar(255) not null,    --单据编号 
  FItemID int  not null,    --产品id
  FUnitID  int,      --单位id 
  FIndex int not null,           --序号
  FQty decimal(28,10),                  --总数量
  FDate datetime not null ,    --单据日期
  FDatePrint datetime,        --打印日期
  FStatusPrint varchar(20),  --打印状态
  FPDAID varchar(255),--PDA序号
  FInterIDIntStore int,      --入库单据内码
  FInterIDOuttStore int,     --出库单据内码
  FStatus int,               --未组装之前状态 0 未入库 1已入库 
  FTypeID int,               -- 单据类型 0 组装单 1 拆卸单
  FRemark varchar(255), 
  FRemark1 varchar(255),
  FRemark2 varchar(255),  
  FRemark3 varchar(255), 
  FRemark4 varchar(255) 
)
CREATE UNIQUE INDEX t_PDAAssemble_FBillNo
ON t_PDAAssemble (FBillNo)
end  
   