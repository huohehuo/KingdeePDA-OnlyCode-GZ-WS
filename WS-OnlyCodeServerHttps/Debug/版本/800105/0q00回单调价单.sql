if (exists (select * from sys.objects where name = 'proc_PDAModifyPrices'))
drop proc proc_PDAModifyPrices
go
create proc proc_PDAModifyPrices
(
  @mainStr nvarchar(1000) --�������
 
)
as 
set nocount on     --����һ��ģʽ��Ҳ���ǲ���ˢ�¶�������Ӱ�����Ϣ�������������
set xact_abort on  --����ѡ������Ϊ����ȫ���ع�
begin tran declare
          @FDate varchar(20),            --�������� 
          
          @FUserID varchar(20),        --�Ƶ���ID 
          @FItemID int,             --��ƷID  
          @FStockID int,--�ֿ�ID
          @FPrice decimal(28,10), --�۸�
          @FBillNo varchar(128)
          set @FUserID=dbo.getString(@mainStr,'|',1)     -- �Ƶ���ID 
          set @FItemID=dbo.getString(@mainStr,'|',2)      --��ƷID
          set @FStockID=dbo.getString(@mainStr,'|',3)      --�ֿ�ID
          set @FPrice=dbo.getString(@mainStr,'|',4)      --����
             set  @FDate= CONVERT(varchar(20),GETDATE(),20)  
   insert into t_PDAModifyPrices(FUserID,FItemID,FStockID,FPrice,FDate)values(@FUserID,@FItemID,@FStockID,@FPrice,@FDate)
commit tran 
return;
if(0<>@@error)
	goto error1
error1:
	rollback tran; 