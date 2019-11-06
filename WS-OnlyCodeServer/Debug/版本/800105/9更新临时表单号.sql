if (exists (select * from sys.objects where name = 'proc_UpdateDetailBillNo'))
    drop proc proc_UpdateDetailBillNo
go
create proc proc_UpdateDetailBillNo
(
 @mainStr nvarchar(1000),--�������
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
declare  
         @FOrderID varchar(50),--PDA���ݱ��
        @FPDAID varchar(50),  --PDA���к� 
        @FSelTranType varchar(20)  --Դ������
 

set @FOrderID=dbo.getString(@mainStr,'|',1) --�µ�PDA����  
set @FPDAID=dbo.getString(@mainStr,'|',2) --PDA����
 
 
 

declare  @FOrderIDOld  varchar(50), --�ϵ�PDA���� 
        
        @detailqty int,               --��ϸ�����ĸ���
        @detailcount int,             --��ϸÿ�����ݵĳ��� 
        @detailIndex int,            --��ϸÿ���±�
        @countindex int              --�ָ���|������
       set @detailqty=0        
       set @detailcount=2           
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
	set @FOrderIDOld=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+1)    --�ϵ�PDA����
	set @FPDAID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --PDAID
  update a_DetailsTable set FOrderID=@FOrderID where FOrderID=@FOrderIDOld and FPDAID=@FPDAID
		set @detailIndex=@detailIndex+1
    
end
set @detailqty=@detailqty+1
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

