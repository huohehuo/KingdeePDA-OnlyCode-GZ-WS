if (exists (select * from sys.objects where name = 'proc_Assemble'))
    drop proc proc_Assemble
go
create proc proc_Assemble
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
  
if exists(select 1 from  t_PDAAssemble where FBillNo=@FOrderID)
  begin
  print convert(int,'װ��ʧ��,��������װ��')
  end
  insert into t_PDAAssemble(FBillNo,FItemID,FUnitID,FIndex,FQty,FDatePrint,FStatus,FTypeID,FRemark1,FRemark2,FRemark3,FRemark4,FDate,FUserID)
            values(@FOrderID,0,0,0,0,NULl,1,0,NULL,NULL,NULL,NULL,GETDATE(),@FBillerID ) 
select @FInterIDAssemble=FInterID from t_PDAAssemble where FBillNo=@FOrderID
 insert into t_PDABarCodeSign(FInterIDIn,FDateInStore,FIsInStore,FUserInStore,FStockID,FStockPlaceID,FIndex,FInterID,FEntryID,FBillNo,FItemID,FUnitID,FBarCode,FDatePrint,FBatchNo,FKFDate,FKFPeriod,FDatePrintShort,FQty,FPrintType,FQtyOut,FRemark7,FRemark8,FRemark9,FRemark10,FInterIDAssemble,FRemark1)
                    select 0,CONVERT(varchar(20),GETDATE(),23),'�����',@FBillerID,FStockID,FStockPlaceID,0,0,0,'',FItemID,FUnitID,FBarCode,GETDATE(),FBatchNo,'',0,'',1,'',0,'','','','',@FInterIDAssemble,'PDAװ�����' from a_DetailsTable where FPDAID=@FPDAID and FOrderID=@FOrderID
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
