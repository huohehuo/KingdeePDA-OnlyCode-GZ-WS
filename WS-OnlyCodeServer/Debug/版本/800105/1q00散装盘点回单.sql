if (exists (select * from sys.objects where name = 'proc_UpLooseFZ'))
    drop proc proc_UpLooseFZ
go
create proc proc_UpLooseFZ
(
 @mainStr nvarchar(1000) --�������
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

declare @FInterID varchar(20),     --����id   
        @FBillerID int,
        @FPDAID varchar(50),      --PDA���к�
        @FDateRet varchar(50),    --�ص�ʱ��
        @FOrderID varchar(50),    --PDA���ݱ��
        @FInterIDAssemble int,
        @Fdate varchar(50),       --����  
         
        @FTypeID int  --Դ������
set @FBillerID = dbo.getString(@mainStr,'|',1)   --����Ա
set @Fdate =dbo.getString(@mainStr,'|',2)        --����  
set @FDateRet =dbo.getString(@mainStr,'|',3)        --���� 
set @FPDAID =dbo.getString(@mainStr,'|',4)        --PDA���к�   
set @FOrderID =dbo.getString(@mainStr,'|',5)        --PDA���ݱ�� (Ҳ������)
  
--if exists(select 1 from  t_PDAAssemble where FBillNo=@FOrderID)
--  begin
--  print convert(int,'װ��ʧ��,��������װ��')
--  end
   delete from t_PDABarCodeSign_In where FBarCode  in(select FBarCode from a_DetailsTable where FPDAID=@FPDAID and FOrderID=@FOrderID and FIsInStore='�����') and (FStatus <> 1 or FInterID=0)
   insert into t_PDABarCodeSign_In(FInterID,FBarCode,FQty,FBillNo,FRemark,FInterID_Before,FDateOutStore_Before,FUserOutStore_Before,FStatus,FDateUpLoad,FStockID,FStockPlaceID)  
   select 0,FBarCode,FQty,'','PDAɢװ���',0,NULL,NULL,1,convert(varchar(20),GETDATE(),20),FStockID,FStockPlaceID from a_DetailsTable where FPDAID=@FPDAID and FOrderID=@FOrderID and FIsInStore='�����' 
 insert into t_PDABarCodeSign(FInterIDIn,FDateInStore,FIsInStore,FUserInStore,FStockID,FStockPlaceID,FIndex,FInterID,FEntryID,FBillNo,FItemID,FUnitID,FBarCode,FDatePrint,FBatchNo,FKFDate,FKFPeriod,FDatePrintShort,FQty,FPrintType,FQtyOut,FRemark7,FRemark8,FRemark9,FRemark10,FInterIDAssemble,FRemark1,FIsOutStore,FRemark2)
                    select 0,case when FIsInStore='�����' then CONVERT(varchar(20),GETDATE(),23) else NULL end,FIsInStore,case when FIsInStore='�����' then @FBillerID else NULL end,FStockID,FStockPlaceID,0,0,0,'',FItemID,FUnitID,FBarCode,GETDATE(),FBatchNo,'',0,'',1,'',case when FIsOutStore='�ѳ���' then 1 else 0 end,'','','','',0,'PDAɢװ���',FIsOutStore,case when FIsOutStore='�ѳ���' then 'PDAɢװ����' else NULL end from a_DetailsTable where FPDAID=@FPDAID and FOrderID=@FOrderID

 delete from a_DetailsTable where FPDAID=@FPDAID and FOrderID=@FOrderID
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
