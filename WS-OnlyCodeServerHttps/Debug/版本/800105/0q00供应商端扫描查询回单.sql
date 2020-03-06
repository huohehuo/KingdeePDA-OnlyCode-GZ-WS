if (exists (select * from sys.objects where name = 'proc_UpLoadBarCodeSignScan'))
    drop proc proc_UpLoadBarCodeSignScan
go
create proc proc_UpLoadBarCodeSignScan
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
          @FUserID varchar(128)--�Ƶ���           
          set @FUserID  = dbo.getString(@mainStr,'|',1) --�Ƶ���
          set @FOrderID =dbo.getString(@mainStr,'|',2) --���ݱ��
          set @FPDAID =dbo.getString(@mainStr,'|',3) --PDAID
          insert into t_PDABarCodeSignScan_Search(FUserID,FBarCode,FItemID,FOrderID,FPDAID,FNum,FDate)
          select @FUserID,FBarCode,FItemID,FOrderID,FPDAID,1,CONVERT(varchar(20),GETDATE(),20) from a_DetailsTable   where FOrderID=@FOrderID and FPDAID=@FPDAID
 
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
 
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
 
