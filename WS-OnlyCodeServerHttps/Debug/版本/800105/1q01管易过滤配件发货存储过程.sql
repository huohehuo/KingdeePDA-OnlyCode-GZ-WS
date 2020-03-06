if (exists (select * from sys.objects where name = 'proc_LogisticsFilter'))
    drop proc proc_LogisticsFilter
go
create proc proc_LogisticsFilter
( 
 @detailStr1 nvarchar(max),--��ϸ����
 @detailStr2 nvarchar(max),
 @detailStr3 nvarchar(max),
 @detailStr4 nvarchar(max),
 @detailStr5 nvarchar(max)
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
create table #Tmp11111 --������ʱ��#Tmp
( 
     FStockID int,--�ֿ�ID
     FStockName varchar(128),--�ֿ����
     FNumber varchar(128),--��Ʒ����
     FQtyOrder decimal(18,8),--��������
     FOrderBillNo varchar(128),--������
     FLogistics varchar(128),--�������Ż��߷�������
     FExplanation varchar(255)--˵��
)
declare @FExplanation varchar(255),
        @FNumber varchar(128),--��Ʒ����
        @FQtyOrder decimal(18,8),--��������
        @FOrderBillNo varchar(128),--������
        @FLogistics varchar(128),--��������
        @FStockName varchar(255),
        @FStockID int,--�ֿ�ID
        @FStockNumber varchar(128),--�ֿ����
        @detailqty int,               --��ϸ�����ĸ���
        @detailcount int,             --��ϸÿ�����ݵĳ��� 
        @detailIndex int,            --��ϸÿ���±�
        @countindex int              --�ָ���|������
       set @FExplanation ='OK'
       set @detailqty=0        
       set @detailcount=5           
    while(@detailqty<5)--�ж�����ϸ���ĸ�����
    begin
    if(@detailqty=1)
	begin
	set @detailStr1=@detailStr2
	end  
	if(@detailqty=2)
	begin
	set @detailStr1=@detailStr3
	end 
	if(@detailqty=3)
	begin
	set @detailStr1=@detailStr4
	end 
	if(@detailqty=4)
	begin
	set @detailStr1=@detailStr5
	end 
	if(@detailStr1='' or @detailStr1=null)
	begin
	break;
	end
	set @detailIndex=0 
	select @countindex=len(@detailStr1)-len(replace(@detailStr1, '|', ''))
	while(@countindex>@detailIndex*@detailcount)
	begin
	set @FNumber=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+1)    --��Ʒ����
	set @FQtyOrder=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --��������
	set @FOrderBillNo=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --���۶�����
	set @FLogistics=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)  --��������
    set @FStockNumber=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5)  --�ֿ����
   

    if exists(select 1 from t_ICItem where FNumber=@FNumber and isnull(F_115,'���')='��Ʒ')
    begin
        if exists(select 1 from t_Stock where FNumber=@FStockNumber)
    begin
      if exists(select 1 from t_Stock where FNumber=@FStockNumber and FSPGroupID>0)
        set @FExplanation =@FStockNumber +  '�ֿ⿪����λ����'
      select @FStockID = FItemID,@FStockName=FName from t_Stock where FNumber=@FStockNumber
    end
    else
    begin
    set @FExplanation = @FStockNumber +  '�ֿ���벻����ϵͳ��'
    end
        
       if exists(select 1 from #Tmp11111 where FNumber=@FNumber)
         update #Tmp11111 set FQtyOrder= FQtyOrder+@FQtyOrder where FNumber=@FNumber
       else
         insert into #Tmp11111(FNumber,FQtyOrder,FOrderBillNo,FLogistics,FExplanation,FStockID,FStockName)values(@FNumber,@FQtyOrder,@FOrderBillNo,@FLogistics,'OK',@FStockID,@FStockName)
    end
    
	set @detailIndex=@detailIndex+1
    
end
set @detailqty=@detailqty+1
end
if @FExplanation<>'OK'
begin
  update #Tmp11111 set FExplanation=@FExplanation
end
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FExplanation)values('�õ���û�г�Ʒ��Ʒ')
end 
select t1.FStockID as �ֿ�ID,t1.FStockName as �ֿ�����,0 as ��λID,t1.FExplanation as ˵��,t2.FNumber as ���ϱ���,t2.FName as ��������,t2.FModel as ����ͺ�,t1.FLogistics as ��������,t1.FOrderBillNo as ���۶�����,convert(float,t1.FQtyOrder) as ��������,t3.FName as ��λ,t2.FUnitID,t2.FItemID from #Tmp11111 t1 left join t_ICItem t2 on t1.FNumber=t2.FNumber left join t_Measureunit t3 on t2.FUnitID=t3.FItemID
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
