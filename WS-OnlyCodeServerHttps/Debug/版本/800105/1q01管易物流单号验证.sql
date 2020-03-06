if (exists (select * from sys.objects where name = 'proc_Logistics_check'))
    drop proc proc_Logistics_check
go
create proc proc_Logistics_check
(  
  @FBillNo varchar(128)  --�������� 
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
declare @FExplanation varchar(128) 
      
 set @FExplanation='OK'
 create table #Tmp11111 --������ʱ��#Tmp
(  
    FExplanation varchar(255),--˵��
)
if exists(select 1 from t_PDALogisticsSingle where FBillNo=@FBillNo)
set @FExplanation=@FBillNo+'�������������'
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FExplanation)values(@FExplanation)
end
select FExplanation as ˵�� from #Tmp11111  
drop table #Tmp11111
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
