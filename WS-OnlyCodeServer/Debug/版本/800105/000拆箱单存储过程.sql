if (exists (select * from sys.objects where name = 'proc_UpdatePDAAssembleSplit1'))
    drop proc proc_UpdatePDAAssembleSplit1
go
create proc proc_UpdatePDAAssembleSplit1
(
 @mainStr nvarchar(max)--������� 
)
as
set nocount on
--------------��ʼ�洢����
begin
--------------����ѡ������Ϊ����ȫ���ع�
set xact_abort on
--------------��������
begin tran 
declare  
  @FBarCode varchar(255) ,--����  
  @FBillerID int,--�Ƶ���
  @FExplanation varchar(255),
  @FRemark varchar(50)     --��ע   
 set @FBarCode = dbo.getString(@mainStr,'|',1) --���� 
 set @FBillerID = dbo.getString(@mainStr,'|',2)--�Ƶ���ID 
 set @FRemark= dbo.getString(@mainStr,'|',3)--˵��(PDA�ʹ�PDA)
 
 create table #Temp112
 (
    FExplanation varchar(255) 
 )
 set @FExplanation = '����ɹ�!'
 if exists(select 1 from t_PDABarCodeSign where FBarCode=@FBarCode)
 begin
    if exists(select 1 from t_PDABarCodeSign t1 inner join t_PDAAssemble t2 on t1.FInterIDAssemble=t2.FInterID where t1.FBarCode=@FBarCode)
    begin
     insert into t_PDAAssemble_Split(FUserID,FInterID,FBillNo,FBarCode,FDate,FRemark) select @FBillerID,t1.FInterIDAssemble,t2.FBillNo,t1.FBarCode,CONVERT(varchar(50),GETDATE(),20),@FRemark from t_PDABarCodeSign t1 inner join t_PDAAssemble t2 on t1.FInterIDAssemble=t2.FInterID where t1.FBarCode=@FBarCode
     update t_PDABarCodeSign set FInterIDAssemble  = 0 where FBarCode =@FBarCode
    end
    else
    begin
      set @FExplanation = '����ʧ��,������û��������Ѳ���!'
    end
 end
 else 
 begin
  set @FExplanation = '����ʧ��,ϵͳ�����ڸ�����!'
 end
 
 
insert into #Temp112(FExplanation)values(@FExplanation)
select FExplanation as ˵�� from #Temp112 
drop table #Temp112 
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
