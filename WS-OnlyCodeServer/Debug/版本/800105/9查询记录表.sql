if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeSignScan_Search')
begin
create table t_PDABarCodeSignScan_Search
( 
  FID int identity(1,1) primary key not null,   
  FUserID int,--ɨ���� 
  FOrderID varchar(128),--����ID
  FPDAID varchar(128),--�豸ID
  FDate datetime,--�Ƶ�����
  FBarCode varchar(255) not null,  --����
  FItemID int,
  FNum int --ɨ�����
) 
CREATE UNIQUE INDEX t_PDABarCodeSignScan_Search_Index_FBarCode
ON t_PDABarCodeSignScan_Search (FBarCode) 
end
 