package com.fangzuo.assist.Utils;

public class Config {
    public static final String Error_Url = "http://148.70.108.65:8080/LogAssist/GetLogMessage";
    public static String Company="广州文石信息";
    public static String User_Permit="User_Permit";
    public static final String Data_Url = "DownData/AllData.txt";

    public static String PDA_Project_Type="PDA_Project_Type";//项目一期二期区分
    public static final String OBJ_BLUETOOTH="key_Bluetooth_object";
    public static final String DATABASESETTING = "master";
    public static final String PDA = "PDA";
    public static final String PrintNum = "PrintNum";
    public static final String[] PDA_Type = {"请选择设备型号","G02A设备", "8000设备", "5000设备","手机端"};

    //用于接口回调的判断------------------------------------------
    public static final String IO_type_Test="IO_type_Test";
    public static final String IO_type_TestDataBase="IO_type_TestDataBase";
    public static final String IO_type_SetProp="IO_type_SetProp";
    public static final String IO_type_connectToSQL="IO_type_connectToSQL";


    //用于Presenter下载表的type区分
    public static final int Data_Bibie           =1;    //币别表
    public static final int Data_Department      =2;    //部门表
    public static final int Data_Employee        =3;    //职员表
    public static final int Data_WaveHouse       =4;    //仓位表
    public static final int Data_InstorageNums   =5;    //库存表
    public static final int Data_Storage         =6;    //仓库表
    public static final int Data_Unit            =7;    //单位表
    public static final int Data_BarCodes        =8;    //条码表":
    public static final int Data_Suppliers       =9;    //供应商表"
    public static final int Data_PayTypes        =10;   //结算方式表
    public static final int Data_Product         =11;   //商品资料表
    public static final int Data_User            =12;   //用户信息表
    public static final int Data_Client          =13;   //客户信息表
    public static final int Data_GoodsDepartments=14;   //交货单位"
    public static final int Data_PurchaseMethod  =15;   //销售/采购方式表
    public static final int Data_yuandanType     =16;   //源单类型"
    public static final int Data_Wanglaikemu     =17;   //往来科目"
    public static final int Data_PriceMethods    =18;   //价格政策"
    public static final int Data_InStoreType     =19;   //入库类型"
    public static final int Data_Company         =20;   //公司信息表
    public static final int Data_Product_Type    =21;   //物料类别


    public static final String saveKdNo   ="saveKdNo";   //物料类别
    public static final String autoAdd   ="autoAdd";   //物料类别
    public static final String autoGetStorage    ="autoGetStorage";   //物料类别
    public static final String Text_Log    ="Text_Log1";   //物料类别
    public static final String Storage    ="Storage";   //物料类别
    public static final String StorageOut    ="StorageOut";   //物料类别
    public static final String StorageIn    ="StorageIn";   //物料类别
    public static final String Unit    ="Unit";   //物料类别
    public static final String WaveHouse    ="WaveHouse";   //物料类别
    public static final String Product    ="Product";   //物料类别
    public static final String Batch    ="Batch";   //物料类别
    public static final String BoxCode    ="BoxCode";   //物料类别
    public static final String People1    ="People1";   //物料类别
    public static final String People2    ="People2";   //物料类别
    public static final String People3    ="People3";   //物料类别
    public static final String People4    ="People4";   //物料类别
    public static final String People5    ="People5";   //物料类别

    public static final int PushDownMTActivity             =10001;
    public static final int PushDownPOActivity             =10002;
    public static final int PushDownSNActivity             =10003;
    public static final int ShouLiaoTongZhiActivity        =10004;
    public static final int OutsourcingOrdersISActivity    =10005;
    public static final int OutsourcingOrdersIS2Activity    =1000502;
    public static final int OutsourcingOrdersOSActivity    =10006;
    public static final int ProducePushInStoreActivity     =10007;
    public static final int ProducePushInStore2Activity    =1000702;
    public static final int ShengchanrenwudanxiatuilingliaoActivity =10008;
    public static final int Shengchanrenwudanxiatuilingliao2Activity =1000802;
    public static final int CGDDPDSLTZDActivity                     =10009;
    public static final int WwOrder2SLTZActivity                     =1000902;
    public static final int XSDDPDFLTZDActivity                     =10010;
    public static final int SCRWDPDSCHBDActivity                    =10011;
    public static final int HBDPDCPRKActivity                       =10012;
    public static final int OutCheckGoodsActivity                   =10013;
    public static final int FHTZDDBActivity                         =10014;

    public static final int PackageActivity                         =10015;
    public static final int PurchaseInStorage                       =10016;
    public static final int ProductInStorageActivity                =10017;
    public static final int ProductInStorageRedActivity             =1001702;
    public static final int OtherInStoreActivity                    =10018;
    public static final int OtherInStore2Activity                   =1001802;
    public static final int SoldOutActivity                         =10019;
    public static final int ProduceAndGetActivity                   =10020;
    public static final int OtherOutStoreActivity                   =10021;
    public static final int OtherOutStore2Activity                  =1002102;
    public static final int OtherOutStore4RedActivity               =10028;
    public static final int PDActivity                              =10022;
    public static final int DBActivity                              =10023;
    public static final int GyYhActivity                            =10024;
    public static final int GyYhRedActivity                         =1002402;
    public static final int PdBackMsg2SaleOutRedActivity            =10025;
    public static final int DbCheckGoodsActivity                    =10026;
    public static final int ProductInCheckGoodsActivity             =10027;
    public static final int PdShouLiao2LLCheckActivity              =10032;
    public static final int PdProductGetCheckActivity              =10033;
    public static final int ShouLiaoOrder2WwrkActivity              =10034;
    //p2
    public static final int ScanCheckActivity                       =10028;
    public static final int CreateSaleOutActivity                   =10029;
    public static final int CreateSaleOutRedActivity                =10030;
    public static final int PD42Activity                            =10031;



}
