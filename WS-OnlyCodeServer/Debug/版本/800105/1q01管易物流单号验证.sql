if (exists (select * from sys.objects where name = 'proc_Logistics_check'))
    drop proc proc_Logistics_check
go
create proc proc_Logistics_check
(  
  @FBillNo varchar(128)  --物流单号 
)
as 
--------------开启一个模式，也就是不再刷新多少行受影响的信息，可以提高性能
set nocount on
--------------开始存储过程
begin
--------------事务选项设置为出错全部回滚
set xact_abort on
--------------开启事务
begin tran
declare @FExplanation varchar(128) 
      
 set @FExplanation='OK'
 create table #Tmp11111 --创建临时表#Tmp
(  
    FExplanation varchar(255),--说明
)
if exists(select 1 from t_PDALogisticsSingle where FBillNo=@FBillNo)
set @FExplanation=@FBillNo+'物流单号已验过'
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FExplanation)values(@FExplanation)
end
select FExplanation as 说明 from #Tmp11111  
drop table #Tmp11111
commit tran 
return;
--------------存在错误
if(0<>@@error)
	goto error1
--------------回滚事务	
error1:
	rollback tran;
--------------结束存储过程
end
