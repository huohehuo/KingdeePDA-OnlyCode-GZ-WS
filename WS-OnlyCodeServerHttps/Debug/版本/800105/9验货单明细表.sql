if not exists(select * from sysobjects where xtype='u' and name='t_PDABarCodeCheckBillNoEntry')
begin
create table t_PDABarCodeCheckBillNoEntry
(
  FID int  not null,     
  FPDAID varchar(50),  --PDA���к�
  FOrderID varchar(30),--����id
  FBarCode varchar(200) not null,  --���� 
  FItemID int, --��Ʒid
  FUnitID int,--��λID
  FStockID int,   --�ֿ�id
  FStockPlaceID int, --��λid
  FStockID_B int,--֮ǰ�ֿ�
  FStockPlaceID_B int,--֮ǰ��λ
  FBatchNo varchar(255),     --����
  FKFPeriod int,  --������,
  FQty decimal(28,8),--����   
  FKFDate varchar(20), --��������  
)  
end 

 