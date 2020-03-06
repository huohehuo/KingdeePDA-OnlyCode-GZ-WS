if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[getString]') and xtype in (N'FN', N'IF', N'TF'))
drop function [dbo].getString
go
create function getString
(
  @str varchar(8000), --Ҫ�ָ���ַ�
  @split varchar(10), --�ָ���
  @index int          --ȡ�ڼ���Ԫ��
)
returns varchar(1024)
 AS
   begin
   declare @location int 
   declare @start int 
    declare   @next int
     declare  @seed int
   set @str=Ltrim(Rtrim(@str)) 
   set @start=1 
   set @next=1 
   set @seed=Len(@split) 
   set @location=Charindex(@split, @str) 
   while @location <> 0 
   AND @index > @next 
   begin 
   set @start=@location + @seed 
   set @location=Charindex(@split, @str, @start) 
   set @next=@next + 1 
   end 
   if @location = 0 
   select @location = Len(@str) + 1 
   return Substring(@str, @start, @location - @start) 
   end
   
