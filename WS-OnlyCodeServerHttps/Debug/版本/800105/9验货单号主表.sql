if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeCheckBillNo')
begin
create table t_PDABarCodeCheckBillNo
(   
  FID int identity(1,1) primary key not null, 
  FDate datetime,--回单日期
  FUserID int,--制单人
  FInterID int not null,--单据内码
  FBillNo varchar(255) not null,  --单据编号
  FTypeID int,--单据类型表 0出库 1红字
  FRemark varchar(8000)     --备注   
) 
CREATE UNIQUE INDEX t_PDABarCodeCheckBillNo_Index_FInterID
ON t_PDABarCodeCheckBillNo (FInterID)
end
 