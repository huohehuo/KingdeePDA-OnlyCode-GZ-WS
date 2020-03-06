if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeSignScan_Search')
begin
create table t_PDABarCodeSignScan_Search
( 
  FID int identity(1,1) primary key not null,   
  FUserID int,--扫描人 
  FOrderID varchar(128),--单号ID
  FPDAID varchar(128),--设备ID
  FDate datetime,--制单日期
  FBarCode varchar(255) not null,  --条码
  FItemID int,
  FNum int --扫描次数
) 
CREATE UNIQUE INDEX t_PDABarCodeSignScan_Search_Index_FBarCode
ON t_PDABarCodeSignScan_Search (FBarCode) 
end
 