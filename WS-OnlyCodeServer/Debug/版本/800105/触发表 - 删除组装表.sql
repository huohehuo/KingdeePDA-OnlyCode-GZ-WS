if exists (select * from sys.objects where name = 't_FZPDAAssembledelete')
drop  trigger t_FZPDAAssembledelete
go
create trigger t_FZPDAAssembledelete
on t_PDAAssemble
for delete
as 
declare @FInterIDAssemble int
select @FInterIDAssemble=FInterID  from deleted
update t_PDABarCodeSign set FInterIDAssemble = NULL where FInterIDAssemble = @FInterIDAssemble


