if (exists (select * from sys.objects where name = 'proc_CheckBarCodeAPP'))
    drop proc proc_CheckBarCodeAPP
go
create proc proc_CheckBarCodeAPP
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
          set @FOrderID =dbo.getString(@mainStr,'|',3) --��ƷID
          set @FPDAID =dbo.getString(@mainStr,'|',4) --��λID
          select @FCheckUserID=FUserID,@FUserIDSupply = FID from t_UserPDASupply where FID = @FCheckUserID
          insert into t_PDABarCodeSign_Check(FInterID,FBarCode,FQty)select @FID,t1.FBarCode,t1.FQty from a_DetailsTable t1 where FOrderID=@FOrderID and FPDAID=@FPDAID
update t_PDABarCodeCheckEntry set FQtying =FQtying+t.FQty,FCheckDate=CONVERT(varchar(128),GETDATE(),23),FCheckUserID=@FCheckUserID,@FUserIDSupply=FUserIDSupply   from (select a.* from a_DetailsTable a left join t_PDABarCodeCheckEntry b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID and b.FID = @FID) as t where t_PDABarCodeCheckEntry.FBarCode=t.FBarCode and t_PDABarCodeCheckEntry.FID=@FID
delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
if not exists(select 1 from t_PDABarCodeCheckEntry where FID=@FID and FQty-FQtying>0)
begin
  update t_PDABarCodeCheck set FStatus = 1 where FID=@FID
end
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
 
