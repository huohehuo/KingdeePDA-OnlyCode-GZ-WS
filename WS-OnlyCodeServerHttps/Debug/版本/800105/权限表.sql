if not exists(select * from sysobjects where xtype='u' and name='t_UserPermitPC')
create table t_UserPermitPC
(
FUserID varchar(20) primary key not null, --用户id
FUserName varchar(100),           --用户名
FUserState varchar(10),           --用户说明
FCondition varchar(8000),         --条件(管理员)
FAssetID varchar(8000),           --类别id
FDepartmentID varchar(8000),      --部门id
FRemark varchar(20),              --是否已授权
FPassWord varchar(100)            --用户密码
)