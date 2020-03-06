if (exists (select * from sys.objects where name = 'proc_Allot1_check'))
    drop proc proc_Allot1_check
go
create proc proc_Allot1_check
(  
  @FBarCode varchar(128) --���� 
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
declare @FBillNo varchar(128),
        @FInterIDAssemble int,--�Ƿ�װ��
          @FIsInStore varchar(20),--���״̬
          @FQtyAll decimal(28,10),--װ������
          @FIsOutStore varchar(20)--����״̬
set @FBillNo='OK'
 if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode)
begin
  if exists( select 1 from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t1.FBillNo= @FBarCode)--�ж��Ƿ���ڸ����� 
	begin
	 if exists(select 1 from a_DetailsTable where FBarCode in (select FBarCode from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t1.FBillNo= @FBarCode))
	  begin
	  		set @FBillNo='��װ�����ѷ���ҵ���ϵ,��ʱ�������'
	  end
	  else
	  begin
	      select  @FIsInStore=t2.FIsInStore,@FIsOutStore=case when t2.FQty=t2.FQtyOut then '�ѳ���' else 'δ����' end  from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t1.FBillNo= @FBarCode
		  if(@FIsInStore='�����')
		   begin
		    select  @FQtyAll=SUM(t2.FQty-t2.FQtyOut) from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where  t1.FBillNo= @FBarCode
		    if(@FQtyAll=0)
		    begin
		     set @FBillNo='����������ȫ����,�����ٴγ���'
		    end
		   end 
		  else 
	        begin
	        set @FBillNo='δ��������,���ܳ���'
	        end 
	   end 
	  
	end
	else
	begin
	  set @FBillNo='ϵͳ�����ڸ�����'
	end
end
else
begin
  if exists(select 1 from t_PDABarCodeSign where FBarCode=@FBarCode)--�ж������Ƿ����ϵͳ��
  begin
         if not exists(select 1 from a_DetailsTable where FBarCode=@FBarCode) --�̵������Ƿ������
         begin
			  select @FIsInStore=isnull(FIsInStore,'δ���'),@FIsOutStore=case when FQty=FQtyOut then '�ѳ���' else 'δ����' end ,@FInterIDAssemble=isnull(FInterIDAssemble,0) from t_PDABarCodeSign where FBarCode=@FBarCode
			  if(@FIsInStore='�����')
			  begin
				select @FQtyAll=FQty-FQtyOut from t_PDABarCodeSign where FBarCode=@FBarCode
				if(@FQtyAll=0)
				begin
				set @FBillNo='����������ȫ����,�����ٴγ���'
				end
			  end
			  else
			  begin
				set @FBillNo='δ�������벻�ܳ���'
			  end
	     end
	     else
	     begin
	         set @FBillNo= '�������ѷ���ҵ���ϵ,��ʱ���ܳ���'
	     end
  end
  else
  begin
  	 set @FBillNo='�����벻��������ϵͳ��'
  end 
end
 
 create table #Tmp11111 --������ʱ��#Tmp
( 
    FItemID int,--��ƷID
    FUnitID int,--��λID
    FQty decimal(28,10),--����
    FStockID int,
    FStockPlaceID int,
    FBatchNo varchar(128),
    FKFPeriod int,
    FKFDate varchar(12),
    FBillNo   varchar(255),--���ݱ��
    FAssembleBillNo varchar(128),--����
    FExplanation varchar(255),--˵��
)
if(@FBillNo='OK')
begin
   if exists(select 1 from t_PDABarCodeSign t1 inner join t_PDAAssemble t2 on t1.FInterIDAssemble=t2.FInterID where FBarCode=@FBarCode)
   begin
   set @FBillNo = 'OK2'
  --select @FBillNo = '�������Ѱ�����:' +t2.FBillNo +',���ܵ�������,���Ȳ���!' from  t_PDABarCodeSign t1 inner join t_PDAAssemble t2 on t1.FInterIDAssemble=t2.FInterID where FBarCode=@FBarCode
   end
end
if exists(select 1 from t_PDAAssemble where FBillNo=@FBarCode)
begin
select  @FQtyAll=SUM(t2.FQty-t2.FQtyOut) from t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t1.FBillNo= @FBarCode
insert into #Tmp11111(FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,FExplanation) select  top 1 t2.FItemID,t2.FUnitID,@FQtyAll,t2.FStockID,t2.FStockPlaceID,t2.FBatchNo,t2.FKFPeriod,t2.FKFDate,t2.FBillNo,@FBillNo from  t_PDAAssemble t1 left join t_PDABarCodeSign t2 on t1.FInterID = t2.FInterIDAssemble where t1.FBillNo= @FBarCode
end
else
begin
insert into #Tmp11111(FItemID,FUnitID,FQty,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,FExplanation)
select top 1 FItemID,FUnitID,FQty-FQtyOut,FStockID,FStockPlaceID,FBatchNo,FKFPeriod,FKFDate,FBillNo,@FBillNo from t_PDABarCodeSign where FBarCode=@FBarCode
end
if not exists(select 1 from #Tmp11111)
begin
insert into #Tmp11111(FExplanation)values(@FBillNo)
end
select t1.FExplanation as ˵��,t1.FBillNo,t1.FItemID,t1.FUnitID,convert(float,FQty) as FQty,t1.FStockID,t1.FStockPlaceID,t1.FBatchNo,t1.FKFPeriod,t1.FKFDate,t2.FNumber from #Tmp11111 t1 inner join t_ICItem t2 on t1.FItemID = t2.FItemID
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
