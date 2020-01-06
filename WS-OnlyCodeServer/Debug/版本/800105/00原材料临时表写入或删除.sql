if (exists (select * from sys.objects where name = 'proc_InsertDetailsTable'))
    drop proc proc_InsertDetailsTable
go
create proc proc_InsertDetailsTable
(
 @FTypeID int ,--0���� 1 ɾ�� 
 @detailStr1 varchar(max),--��ϸ����
 @detailStr2 varchar(max),
 @detailStr3 varchar(max),
 @detailStr4 varchar(max),
 @detailStr5 varchar(max) 
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
        @FPDAID varchar(50),  --PDA���к�
        @FOrderID varchar(30),--����id
        @FUnitID int,  --��λID
        @FItemID int, --��Ʒid
        @FStockID int,   --�ֿ�id
        @FStockPlaceID int, --��λid
        @FQty decimal(28,10),  --�������
        @FBatchNo varchar(255),     --����
        @FType varchar(128),--��������
        @detailqty int,             --��ϸ�����ĸ���
        @detailcount int,           --��ϸÿ�����ݵĳ��� 
        @detailIndex int,           --��ϸÿ���±�
        @countindex int             --�ָ���|������
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
	set @FItemID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+1)    --��Ʒid
	set @FUnitID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+2)    --��λID
	set @FQty=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+3)    --����
	set @FStockID=dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+4)   --�ֿ�ID
    set @FStockPlaceID =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+5)  --��λID
    set @FBatchNo =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+6)  --���� 
    set @FPDAID =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+7)  --PDA���к�
    set @FOrderID =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+8)  --����id
    set @FType =dbo.getString(@detailStr1,'|',@detailcount*@detailIndex+9)  --��������
 
    set @detailIndex=@detailIndex+1 
     if(@FTypeID=0 and @FBatchNo<>'')
     begin
         if exists(select 1 from a_DetailsTable where FItemID=@FItemID and FUnitID=@FUnitID and FStockID=@FStockID and FStockPlaceID=@FStockPlaceID and FBatchNo=@FBatchNo and FPDAID=@FPDAID and FOrderID=@FOrderID and FType=@FType)
           update  a_DetailsTable set FQty=FQty+@FQty where FItemID=@FItemID and FUnitID=@FUnitID and FStockID=@FStockID and FStockPlaceID=@FStockPlaceID and FBatchNo=@FBatchNo and FPDAID=@FPDAID and FOrderID=@FOrderID and FType=@FType
         else
          insert into a_DetailsTable(FBarCode,FItemID,FBatchNo,FUnitID,FStockID,FStockPlaceID,FOrderID,FPDAID,FQty,FType)values(NEWID(),@FItemID,@FBatchNo,@FUnitID,@FStockID,@FStockPlaceID,@FOrderID,@FPDAID,@FQty,@FType)          
     end 
     else
     begin
      if exists(select 1 from a_DetailsTable where FItemID=@FItemID and FUnitID=@FUnitID and FStockID=@FStockID and FStockPlaceID=@FStockPlaceID and FBatchNo=@FBatchNo and FPDAID=@FPDAID and FOrderID=@FOrderID and FType=@FType)
           update  a_DetailsTable set FQty=FQty-@FQty where FItemID=@FItemID and FUnitID=@FUnitID and FStockID=@FStockID and FStockPlaceID=@FStockPlaceID and FBatchNo=@FBatchNo and FPDAID=@FPDAID and FOrderID=@FOrderID and FType=@FType
     end
    end
    set @detailqty=@detailqty+1
    end
delete from a_DetailsTable where FQty=0
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
