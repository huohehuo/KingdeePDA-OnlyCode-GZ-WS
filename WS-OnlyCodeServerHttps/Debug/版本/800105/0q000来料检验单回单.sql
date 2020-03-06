if (exists (select * from sys.objects where name = 'proc_MaterialsAllCheck'))
    drop proc proc_MaterialsAllCheck
go
create proc proc_MaterialsAllCheck
(
 @mainStr nvarchar(1000),--�������
 @detailStr1 varchar(max),--��ϸ����
 @detailStr2 varchar(max),
 @detailStr3 varchar(max),
 @detailStr4 varchar(max),
 @detailStr5 varchar(max)
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
declare @FUserID int,--��Ӧ���û�ID
        @FInterID varchar(20),     --����id
        @FBillNo varchar(50),      -- ��� 
        @FAreaPS varchar(20),      --���۷�Χ
        @FTranType varchar(20),    --�������� 
        @Fdate varchar(50),       --���� 
        @FCustID varchar(20),     --������λid
        @FCurrencyID nvarchar(20),--�ұ�id
        @FSettleID nvarchar(20),  --���㷽ʽid
        @FSaleStyle varchar(20),  --���۷�ʽid
        @FFetchStyle varchar(20), --������ʽid
        @FFetchAdd varchar(100),  --�����ص���
        @FCheckDate varchar(50),  --�������
        @FMangerID varchar(20),   --����id
        @FDeptID varchar(20),     --����id
        @FEmpID varchar(20),      --ҵ��Աid
        @FBillerID varchar(20),   --�Ƶ���id
        @FExchangeRate varchar(50),--����
        @FSettleDate varchar(50), --��������
        @FExplanation varchar(200),--ժҪ 
           @FOrderID varchar(50),--PDA���ݱ��
        @FPDAID varchar(50),  --PDA���к� 
          @FUserName varchar(128),--�ŵ��û�
        @FPDASource varchar(128),--������Դ 
        @FSelTranType varchar(20)   --Դ������ID
set @FUserID = dbo.getString(@mainStr,'|',1) --����Ա(��Ӧ���û�ID)
set @FOrderID=dbo.getString(@mainStr,'|',2) --PDA���ݱ��   
set @FPDAID=dbo.getString(@mainStr,'|',3) --PDA���к� 

set @Fdate = convert(varchar(20),GETDATE(),23)
 declare @FEntryID int,       --��ϸ���
         @FEntryID_SRC int,
         @FSCBillInterID int,
         @FResult int,
         @FID_SRC int,
         @FSManagerID int,--�ʼ�Ա@FSManagerID=FSManagerID,@FFManagerID=FFManagerID,
         @FFManagerID int,--�ͼ���
         @FCheckQty decimal(28,10), --��������
         @FSampleBreakQty decimal(28,10), --��Ʒ�ƻ���
         @FPassQty decimal(28,10), --�ϸ���
        @detailqty int,               --��ϸ�����ĸ���
        @detailcount int,             --��ϸÿ�����ݵĳ��� 
        @detailIndex int,            --��ϸÿ���±�
        @countindex int              --�ָ���|������
       set @detailqty=0        
       set @detailcount=9           
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
	set @FID_SRC=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+1)    --ԭ��ID(FInterID)
	set @FEntryID_SRC=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --��ϸID(FEntryID)
	set @FSCBillInterID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)  --�ʼ췽��ID
	set @FResult=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)    --�ʼ���ID
    set @FSManagerID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5) --�ʼ�ԱID
	set @FFManagerID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6) --�ͼ���id
	set @FCheckQty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7) --��������
    set @FSampleBreakQty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8)  --��Ʒ�ƻ���
	set @FPassQty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)  --�ϸ���
    exec proc_MaterialsAllCheck2 @FUserID,@FID_SRC,@FEntryID_SRC,@FSCBillInterID,@FResult,@FSManagerID,@FFManagerID,@FCheckQty,@FSampleBreakQty,@FPassQty
	set @detailIndex=@detailIndex+1
    set @FEntryID=@detailqty*50+@detailIndex

	 
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
