if not exists(select * from sysobjects where xtype='u' and name='t_UserPermitPC')
create table t_UserPermitPC
(
FUserID varchar(20) primary key not null, --�û�id
FUserName varchar(100),           --�û���
FUserState varchar(10),           --�û�˵��
FCondition varchar(8000),         --����(����Ա)
FAssetID varchar(8000),           --���id
FDepartmentID varchar(8000),      --����id
FRemark varchar(20),              --�Ƿ�����Ȩ
FPassWord varchar(100)            --�û�����
)