if (exists (select * from sys.objects where name = 'proc_UpdatePDAAssembleSplit1'))
    drop proc proc_UpdatePDAAssembleSplit1
go
create proc proc_UpdatePDAAssembleSplit1
(
 @mainStr nvarchar(max)--主表参数 
)
as
set nocount on
--------------开始存储过程
begin
--------------事务选项设置为出错全部回滚
set xact_abort on
--------------开启事务
begin tran 
declare  
  @FBarCode varchar(255) ,--条码  
  @FBillerID int,--制单人
  @FExplanation varchar(255),
  @FRemark varchar(50)     --备注   
 set @FBarCode = dbo.getString(@mainStr,'|',1) --条码 
 set @FBillerID = dbo.getString(@mainStr,'|',2)--制单人ID 
 set @FRemark= dbo.getString(@mainStr,'|',3)--说明(PDA就穿PDA)
 
 create table #Temp112
 (
    FExplanation varchar(255) 
 )
 set @FExplanation = '拆箱成功!'
 if exists(select 1 from t_PDABarCodeSign where FBarCode=@FBarCode)
 begin
    if exists(select 1 from t_PDABarCodeSign t1 inner join t_PDAAssemble t2 on t1.FInterIDAssemble=t2.FInterID where t1.FBarCode=@FBarCode)
    begin
     insert into t_PDAAssemble_Split(FUserID,FInterID,FBillNo,FBarCode,FDate,FRemark) select @FBillerID,t1.FInterIDAssemble,t2.FBillNo,t1.FBarCode,CONVERT(varchar(50),GETDATE(),20),@FRemark from t_PDABarCodeSign t1 inner join t_PDAAssemble t2 on t1.FInterIDAssemble=t2.FInterID where t1.FBarCode=@FBarCode
     update t_PDABarCodeSign set FInterIDAssemble  = 0 where FBarCode =@FBarCode
    end
    else
    begin
      set @FExplanation = '拆箱失败,该条码没绑定箱码或已拆箱!'
    end
 end
 else 
 begin
  set @FExplanation = '拆箱失败,系统不存在该条码!'
 end
 
 
insert into #Temp112(FExplanation)values(@FExplanation)
select FExplanation as 说明 from #Temp112 
drop table #Temp112 
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
