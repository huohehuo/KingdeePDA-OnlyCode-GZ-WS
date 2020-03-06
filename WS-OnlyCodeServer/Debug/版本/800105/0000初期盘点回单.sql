if (exists (select * from sys.objects where name = 'proc_UpdateBarCodeFirst'))
    drop proc proc_UpdateBarCodeFirst
go
create proc proc_UpdateBarCodeFirst
( 
  @mainStr varchar(8000)  
)
as 
--------------����һ��ģʽ��Ҳ���ǲ���ˢ�¶�������Ӱ�����Ϣ�������������
set nocount on
--------------��ʼ�洢����
begin
--------------����ѡ������Ϊ����ȫ���ع�
set xact_abort on
--------------��������
begin tran

declare   @FOrderID varchar(50),--PDA���ݱ��
          @FPDAID varchar(50),  --PDA���к� 
          @FUserIDSupply int,
          @FID int,--�̵㷽��ID
          @FCheckUserID varchar(128)--�Ƶ���          
          set @FID = dbo.getString(@mainStr,'|',1) --�̵㷽��ID
          set @FCheckUserID  = dbo.getString(@mainStr,'|',2) --�Ƶ���
          set @FOrderID =dbo.getString(@mainStr,'|',3) --�̵㷽��ID
          set @FPDAID =dbo.getString(@mainStr,'|',4) --PDA���к�
          select @FCheckUserID=FUserID,@FUserIDSupply = FID from t_UserPDASupply where FID = @FCheckUserID
 		    --����ϵͳ	
 			insert into t_PDABarCodeCheckFirstEntry(FID,FCheckUserID,FUserIDSupply,FCheckDate ,FInterID,FEntryID,FBillNo,FInterIDIn,FInterIDOut,FInterIDAssemble,FInterIDDisassemble,FItemID,FUnitID,FBarCode,FDatePrint,FDateInStore,FDateOutStore,FIsInStore,FIsOutStore,FUserInStore,FUserOutStore,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FDatePrintShort,FQty,FQtying,FPrintType,FUserPrint,FRemark1,FRemark2,FRemark3,FRemark4,FRemark5,FRemark6,FRemark7,FRemark8,FRemark9,FRemark10,FRemark,FOver,FOrderBillNo,FStatus)
			select @FID,@FCheckUserID,@FUserIDSupply,CONVERT(varchar(50),getdate(),20),FInterID,FEntryID,FBillNo,FInterIDIn,FInterIDOut,FInterIDAssemble,FInterIDDisassemble,FItemID,FUnitID,FBarCode,FDatePrint,FDateInStore,FDateOutStore,FIsInStore,FIsOutStore,FUserInStore,FUserOutStore,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FDatePrintShort,FQty-ISnull(FQtyOut,0),0,FPrintType,FUserPrint,FRemark1,FRemark2,FRemark3,FRemark4,FRemark5,FRemark6,FRemark7,FRemark8,FRemark9,FRemark10,FRemark,FOver,FOrderBillNo,1 
			from t_PDABarCodeSign t1 where FBarCode in(select FBarCode from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID) 
            --������ϵͳ
            	insert into t_PDABarCodeCheckFirstEntry(FID,FCheckUserID,FUserIDSupply,FCheckDate,FInterID,FEntryID,FBillNo,FInterIDIn,FInterIDOut,FInterIDAssemble,FInterIDDisassemble,FItemID,FUnitID,FBarCode,FDatePrint,FDateInStore,FDateOutStore,FIsInStore,FIsOutStore,FUserInStore,FUserOutStore,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FDatePrintShort,FQty,FQtying,FPrintType,FUserPrint,FRemark1,FRemark2,FRemark3,FRemark4,FRemark5,FRemark6,FRemark7,FRemark8,FRemark9,FRemark10,FRemark,FOver,FOrderBillNo,FStatus)
			select @FID,@FCheckUserID,@FUserIDSupply,CONVERT(varchar(50),getdate(),20),0,0,'',0,0,0,0,0,0,FBarCode,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,'',0,'','',1,0,'',0,'','','','','','','','','','','',0,'',0 from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID and not exists(select 1 from t_PDABarCodeSign where FBarCode=a_DetailsTable.FBarCode)
 
--end
commit tran 
return;
--------------���ڴ���
if(0<>@@error)
	goto error1
--------------�ع�����	
error1:
	rollback tran;
--------------�����洢����
end
 
