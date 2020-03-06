package Server.prop;

import Bean.ConnectSQLBean;
import Bean.DownloadReturnBean;
import Bean.DownloadReturnBean.*;
import Utils.CommonJson;
import Utils.JDBCUtil;
import Utils.Lg;
import Utils.getDataBaseUrl;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Servlet implementation class DownloadInfo
 */
@WebServlet("/DownloadInfo")
public class DownloadInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;



    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadInfo() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection connection = null;
        Statement statement = null;
        ResultSet rSet = null;
        String version = null;
        ArrayList<PriceMethod> priceMethods;
        ArrayList<InStorageType> InStorageTypes;
        ArrayList<wanglaikemu> wanglaikemu;
        ArrayList<yuandanType> getyuandanType;
        ArrayList<purchaseMethod> purchaseMethod;
        ArrayList<GetGoodsDepartment> goodsDepartments;
        ArrayList<Client> client;
        ArrayList<User> user;
        ArrayList<product> products;
        ArrayList<payType> payTypes;
        ArrayList<suppliers> suppliers;
        ArrayList<BarCode> barCodes;
        ArrayList<Unit> unit;
        ArrayList<storage> storage;
        ArrayList<InstorageNum> instorageNums;
        ArrayList<wavehouse> wavehouse;
        ArrayList<employee> empArrayList;
        ArrayList<department> department;
        ArrayList<bibiezhong> container;
        ArrayList<GProduct> gProducts;
        ArrayList<Suppliers4P2> suppliers4P2s;
        ArrayList<CheckType> checkTypes;
        Gson gson = new Gson();
        int size = 0;
        DownloadReturnBean dBean = new DownloadReturnBean();

        if (request.getParameter("json") != null && !(request.getParameter("json")).equals("")) {
            ConnectSQLBean sqlBean = gson.fromJson(request.getParameter("json"), ConnectSQLBean.class);
            System.out.println(request.getParameter("json"));
            ArrayList<Integer> choose = sqlBean.choose;
            version = sqlBean.Version;
            try {
                connection = JDBCUtil.getConn(getDataBaseUrl.getUrl(sqlBean.ip, sqlBean.port, sqlBean.database), sqlBean.password, sqlBean.username);
                statement = connection.createStatement();
                System.out.println(request.getParameter("json"));
                for (Integer aChoose : choose) {
                    System.out.println("DownloadInfo下载定位:"+aChoose);
                    switch (aChoose) {
                        case 1:
                            container = getBiBieBiaoData(statement, rSet, version, dBean);
                            System.out.println("container" + container.size());
                            dBean.bibiezhongs = container;
                            size += container.size();
                            break;
                        case 2:
                            department = getDepartment(statement, rSet, version, dBean);
                            System.out.println("department" + department.size());
                            dBean.department = department;
                            size += department.size();
                            break;

                        case 3:
                            empArrayList = getEmployee(statement, rSet, version, dBean);
                            System.out.println("emp" + empArrayList.size());
                            dBean.employee = empArrayList;
                            size += empArrayList.size();
                            break;
                        case 4:
                            wavehouse = getWaveHouse(statement, rSet, version, dBean);
                            System.out.println("wavehouse" + wavehouse.size());
                            dBean.wavehouse = wavehouse;
                            size += wavehouse.size();
                            break;
                        case 5:
                            instorageNums = getInstorageNums(statement, rSet, version, dBean);
                            System.out.println("instorageNums" + instorageNums.size());
                            dBean.InstorageNum = instorageNums;
                            size += instorageNums.size();
                            break;
                        case 6:
                            storage = getStorage(statement, rSet, version, dBean);
                            System.out.println("storage" + storage.size());
                            dBean.storage = storage;
                            size += storage.size();
                            break;
                        case 7:
                            unit = getUnit(statement, rSet, version, dBean);
                            System.out.println("unit" + unit.size());
                            dBean.units = unit;
                            size += unit.size();
                            break;
                        case 8:
                            barCodes = getBarCodes(statement, rSet, version, dBean);
                            System.out.println("barCodes" + barCodes.size());
                            dBean.BarCode = barCodes;
                            size += barCodes.size();
                            break;
                        case 9:
                            suppliers = getSuppliers(statement, rSet, version, dBean);
                            System.out.println("suppliers" + suppliers.size());
                            dBean.suppliers = suppliers;
                            size += suppliers.size();
                            break;
                        case 10:
                            payTypes = getpayTypes(statement, rSet, version, dBean);
                            System.out.println("payTypes" + payTypes.size());
                            dBean.payTypes = payTypes;
                            size += payTypes.size();
                            break;
                        case 11:
                            products = getProduct(statement, rSet, version, dBean);
                            System.out.println("products" + products.size());
                            dBean.products = products;
                            size += products.size();
                            break;
                        case 12:
                            user = getUser(statement, rSet, version, dBean);
                            System.out.println("user" + user.size());
                            dBean.User = user;
                            size += user.size();
                            break;
                        case 13:
                            client = getClient(statement, rSet, version, dBean);
                            System.out.println("client" + client.size());
                            dBean.clients = client;
                            size += client.size();
                            break;
                        case 14:
                            goodsDepartments = getGoodsDepartments(statement, rSet, version, dBean);
                            System.out.println("goodsDepartments" + goodsDepartments.size());
                            dBean.getGoodsDepartments = goodsDepartments;
                            size += goodsDepartments.size();
                            break;
                        case 15:
                            purchaseMethod = getPurchaseMethod(statement, rSet, version, dBean);
                            System.out.println("purchaseMethod" + purchaseMethod.size());
                            dBean.purchaseMethod = purchaseMethod;
                            size += purchaseMethod.size();
                            break;
                        case 16:
                            getyuandanType = getyuandanType(statement, rSet, version, dBean);
                            System.out.println("getyuandanType" + getyuandanType.size());
                            dBean.yuandanTypes = getyuandanType;
                            size += getyuandanType.size();
                            break;
                        case 17:
                            wanglaikemu = getWanglaikemu(statement, rSet, version, dBean);
                            System.out.println("wanglaikemu" + wanglaikemu.size());
                            dBean.wanglaikemu = wanglaikemu;
                            size += wanglaikemu.size();
                            break;
                        case 18:
                            priceMethods = getPriceMethods(statement, rSet, version, dBean);
                            System.out.println("priceMethods" + priceMethods.size());
                            dBean.priceMethods = priceMethods;
                            size += priceMethods.size();
                            break;
                        case 19:
                            InStorageTypes = getInStoreType(statement, rSet, version, dBean);
                            System.out.println("InStorageTypes" + InStorageTypes.size());
                            dBean.inStorageTypes = InStorageTypes;
                            size += InStorageTypes.size();
                        case 20:
                            gProducts = getGProduct(statement, rSet, version, dBean);
                            System.out.println("gProducts" + gProducts.size());
                            dBean.gProducts = gProducts;
                            size += gProducts.size();
                            break;
                        case 21:
                            suppliers4P2s = getSuppliers4P2(statement, rSet, version, dBean);
                            System.out.println("suppliers4P2s" + suppliers4P2s.size());
                            Lg.e("供应商登陆数据：",suppliers4P2s);
                            dBean.suppliers4P2s = suppliers4P2s;
                            size += suppliers4P2s.size();
                            break;
                        case 22:
                            checkTypes = getCheckTypes(statement, rSet, version, dBean);
                            System.out.println("checkTypes" + checkTypes.size());
                            Lg.e("checkTypes：",checkTypes);
                            dBean.checkTypes = checkTypes;
                            size += checkTypes.size();
                            break;
                        default:
                            break;
                    }
                }
                dBean.size = size;
//                Lg.e("返回数据",dBean);
                response.getWriter().write(CommonJson.getCommonJson(true, gson.toJson(dBean)));


            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                response.getWriter().write(CommonJson.getCommonJson(false, "数据库连接错误"));
            }finally {
                JDBCUtil.close(rSet,null,connection);
                if(statement!=null){
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            response.getWriter().write(CommonJson.getCommonJson(false, "未接受到Json数据"));
        }

    }

    private ArrayList<PriceMethod> getPriceMethods(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<PriceMethod> container = new ArrayList<DownloadReturnBean.PriceMethod>();
        String sql = "select t1.FInterID,t1.FEntryID,t2.FPri,convert(float,FPrice) as FPrice,t2.FName,t1.FItemID,t1.FUnitID,t1.FRelatedID,t1.FBegQty,t1.FEndQty,CONVERT(varchar(50), t1.FBegDate, 23) as FBegDate,CONVERT(varchar(50), t1.FEndDate, 23) as FEndDate from IcPrcPlyEntry t1 left join IcPrcPly t2 on t1.FInterID=t2.FInterID and FChecked=1 and FRelatedID<>0";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.PriceMethod bean = dBean.new PriceMethod();
                bean.FInterID = rSet.getString("FInterID");
                bean.FEntryID = rSet.getString("FEntryID");
                bean.FPri = rSet.getString("FPri");
                bean.FPrice = rSet.getString("FPrice");
                bean.FName = rSet.getString("FName");
                bean.FItemID = rSet.getString("FItemID");
                bean.FUnitID = rSet.getString("FUnitID");
                bean.FRelatedID = rSet.getString("FRelatedID");
                bean.FBegQty = rSet.getString("FBegQty");
                bean.FEndQty = rSet.getString("FEndQty");
                bean.FBegDate = rSet.getString("FBegDate");
                bean.FEndDate = rSet.getString("FEndDate");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return container;
    }

    private ArrayList<wanglaikemu> getWanglaikemu(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<wanglaikemu> container = new ArrayList<DownloadReturnBean.wanglaikemu>();
        String sql = "SELECT FAccountID,FNumber,FFullName FROM t_Account WHERE ( FDelete=0 Or  FIsAcnt=1)  AND FControlSystem=1 and FLevel=2 ORDER BY FNumber";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.wanglaikemu bean = dBean.new wanglaikemu();
                bean.FAccountID = rSet.getString("FAccountID");
                bean.FNumber = rSet.getString("FNumber");
                bean.FFullName = rSet.getString("FFullName");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return container;
    }
    private ArrayList<GProduct> getGProduct(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<GProduct> container = new ArrayList<>();
        String sql = "SELECT   FInterID, FID,FName FROM t_SubMessage Where FInterID>0  AND FDeleted=0  And FTypeID=10002 ORDER BY FID";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.GProduct bean = dBean.new GProduct();
                bean.FID = rSet.getString("FID");
                bean.FName = rSet.getString("FName");
                bean.FInterID = rSet.getString("FInterID");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return container;
    }
    private ArrayList<Suppliers4P2> getSuppliers4P2(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<Suppliers4P2> container = new ArrayList<>();
        String sql = "select FName as 用户名,FPassWord as 密码,FUserID,FID as 用户ID,isnull(FLevel,1) as FLevel,ISNULL(FPermit,'') as FPermit from t_UserPDASupply where FDeleted=0";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.Suppliers4P2 bean = dBean.new Suppliers4P2();
                bean.FID = rSet.getString("用户ID");
                bean.FName = rSet.getString("用户名");
                bean.FPassWord = rSet.getString("密码");
                bean.FUserID = rSet.getString("FUserID");
                bean.FLevel = rSet.getString("FLevel");
                bean.FPermit = rSet.getString("FPermit");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return container;
    }
    private ArrayList<CheckType> getCheckTypes(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<CheckType> container = new ArrayList<>();
        String sql = "select FInterID,FBillNo from ICQCScheme where FTranType=45 and  FInterID>0 and FCheckerID>0 and FCancellation=0 And GetDate() between  FValidDate And  FInvalidDate";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.CheckType bean = dBean.new CheckType();
                bean.FInterID = rSet.getString("FInterID");
                bean.FBillNo = rSet.getString("FBillNo");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return container;
    }

    //出入库类型
    private ArrayList<InStorageType> getInStoreType(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<InStorageType> container = new ArrayList<>();
        String sql = "select FID,FName from ICBillType";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.InStorageType bean = dBean.new InStorageType();
                bean.FID = rSet.getString("FID");
                bean.FName = rSet.getString("FName");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return container;
    }

    private ArrayList<yuandanType> getyuandanType(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<yuandanType> container = new ArrayList<DownloadReturnBean.yuandanType>();
        String sql = "select abs(FID) as FID,FName_CHS from ICClassType where FName_CHS in ('产品预测单','销售报价单','发货通知','销售订单','产品入库','采购订单','收料通知单','采购申请','生产任务单','退货通知','外购入库','委外订单')";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.yuandanType bean = dBean.new yuandanType();
                bean.FID = rSet.getString("FID");
                bean.FName_CHS = rSet.getString("FName_CHS");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return container;
    }

    private ArrayList<purchaseMethod> getPurchaseMethod(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<purchaseMethod> container = new ArrayList<DownloadReturnBean.purchaseMethod>();
        String sql = "SELECT  FTypeID,FInterID FItemID, FID FNumber,FName FROM t_SubMessage Where FInterID>0 AND FDeleted=0  And (FTypeID=162 or FTypeID =101 or FTypeID=997 or FTypeID=32 or FTypeID=33 or FTypeID=668 or FTypeID = 471 or FTypeID=632) AND (FInterID<> 20296 and FInterID<>20298) And (FInterID<> 20296) or (FDeleted=0  And FTypeID=106) ORDER BY FID";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.purchaseMethod bean = dBean.new purchaseMethod();
                bean.FTypeID = rSet.getString("FTypeID");
                bean.FItemID = rSet.getString("FItemID");
                bean.FNumber = rSet.getString("FNumber");
                bean.FName = rSet.getString("FName");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return container;
    }

    private ArrayList<GetGoodsDepartment> getGoodsDepartments(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<GetGoodsDepartment> container = new ArrayList<DownloadReturnBean.GetGoodsDepartment>();
        String sql = "SELECT  TOP 50 t1.FItemID ,t1.FDeleted ,t1.FNumber,t1.FName,t1.FDetail FROM t_Item t1  with(index (uk_Item2)) LEFT JOIN t_Department x2 ON t1.FItemID = x2.FItemID  WHERE FItemClassID = 2 AND t1.FDetail=1  AND t1.FDeleteD=0  ORDER BY t1.FNumber";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.GetGoodsDepartment bean = dBean.new GetGoodsDepartment();
                bean.FItemID = rSet.getString("FItemID");
                bean.FDeleted = rSet.getString("FDeleted");
                bean.FNumber = rSet.getString("FNumber");
                bean.FName = rSet.getString("FName");
                bean.FDetail = rSet.getString("FDetail");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return container;
    }

    private ArrayList<Client> getClient(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<Client> container = new ArrayList<DownloadReturnBean.Client>();
        String sql = "SELECT  t1.FItemID ,FItemClassID,t1.FNumber,t1.FParentID,FLevel,FDetail,t1.FName,FAddress,FPhone,FEmail,x2.FTypeID  FROM t_Item t1  with(index (uk_Item2)) LEFT JOIN t_Organization x2 ON t1.FItemID = x2.FItemID  WHERE FItemClassID = 1 AND (t1.FDetail = 1) AND t1.FDeleteD=0  ORDER BY t1.FNumber";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.Client bean = dBean.new Client();
                bean.FItemID = rSet.getString("FItemID");
                bean.FItemClassID = rSet.getString("FItemClassID");
                bean.FNumber = rSet.getString("FNumber");
                bean.FParentID = rSet.getString("FParentID");
                bean.FLevel = rSet.getString("FLevel");
                bean.FDetail = rSet.getString("FDetail");
                bean.FName = rSet.getString("FName");
                bean.FAddress = rSet.getString("FAddress");
                bean.FPhone = rSet.getString("FPhone");
                bean.FEmail = rSet.getString("FEmail");
                bean.FTypeID = rSet.getString("FTypeID");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


        return container;

    }

    private ArrayList<User> getUser(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<User> container = new ArrayList<DownloadReturnBean.User>();

//        String sql = "select FUserID,FName,'' as FPassWord,FEmpID,FPrimaryGroup as " +
//                "FGroupName,'' as FPermit from t_User where abs(FUserID) between " +
//                "16384 and 2147483647 Order By FUserID";
//        String sql = "select t1.FUserID,t1.FName,isNull(t2.FPassWord,'') as FPassWord,FEmpID,FPrimaryGroup as " +
//                "FGroupName,'' as FPermit from t_User t1 left join  t_UserPermitPC t2 on " +
//                "t1.FUserID=t2.FUserID where abs(t1.FUserID) between 16384 and 2147483647 " +
//                "and t2.FRemark in('一般权限','管理员权限','保管员权限') or t1.FName " +
//                "in('Administrator','Manager') Order By t1.FUserID";
        String sql = "select t1.FUserID,t1.FName,isnull(t2.FPassWord,'') as " +
                "FPassWord,t1.FEmpID,t1.FPrimaryGroup as FGroupName,isnull(t2.FCondition,'') " +
                "as FPermit from t_User t1 left join  t_UserPermitPC t2 " +
                "on t1.FUserID=t2.FUserID where abs(t1.FUserID) between " +
                "16384 and 2147483647 and t2.FRemark in('一般权限','管理员权限','保管员权限') " +
                "or t1.FName in('Administrator','Manager')  Order By t1.FUserID";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.User bean = dBean.new User();
                bean.FUserID = rSet.getString("FUserID");
                bean.FName = rSet.getString("FName");
                bean.FPassWord = rSet.getString("FPassWord");
                bean.FGroupName = rSet.getString("FGroupName");
                bean.FPermit = rSet.getString("FPermit");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return container;
    }

    private ArrayList<product> getProduct(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<product> container = new ArrayList<DownloadReturnBean.product>();
        String sql = "";
        if (version.equals("500116") || version.equals("500115") ) {
            sql = "select FIsSnManage,FItemID,FISKFPeriod,FKFPeriod,FNumber,FModel,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,'' as FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 order by FName collate Chinese_PRC_CI_AS";//k3 rise 12.3
        } else if (version.equals("800103")  || version.equals("800102") || version.contains("5001")) {
            sql = "select FIsSnManage,FItemID,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod ,FNumber,FModel,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 order by FName collate Chinese_PRC_CI_AS";//�콢���k3
        } else {
            sql = "select FIsSnManage,FItemID,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod ,FNumber,FModel,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,'' as FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 order by FName collate Chinese_PRC_CI_AS";//רҵ��
        }
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.product bean = dBean.new product();
                bean.FIsSnManage = rSet.getString("FIsSnManage");
                bean.FItemID = rSet.getString("FItemID");
                bean.FISKFPeriod = rSet.getString("FISKFPeriod");
                bean.FKFPeriod = rSet.getString("FKFPeriod");
                bean.FNumber = rSet.getString("FNumber");
                bean.FModel = rSet.getString("FModel");
                bean.FName = rSet.getString("FName");
                bean.FFullName = rSet.getString("FFullName");
                bean.FUnitID = rSet.getString("FUnitID");
                bean.FUnitGroupID = rSet.getString("FUnitGroupID");
                bean.FDefaultLoc = rSet.getString("FDefaultLoc");
                bean.FProfitRate = rSet.getString("FProfitRate");
                bean.FTaxRate = rSet.getString("FTaxRate");
                bean.FOrderPrice = rSet.getString("FOrderPrice");
                bean.FSalePrice = rSet.getString("FSalePrice");
                bean.FPlanPrice = rSet.getString("FPlanPrice");
                bean.FBarcode = rSet.getString("FBarcode");
                bean.FSPID = rSet.getString("FSPID");
                bean.FBatchManager = rSet.getString("FBatchManager");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return container;
    }

    private ArrayList<payType> getpayTypes(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<payType> container = new ArrayList<DownloadReturnBean.payType>();
        String sql = "select FItemID,FName,FNumber  from t_Settle where FItemID!=0";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.payType bean = dBean.new payType();
                bean.FItemID = rSet.getString("FItemID");
                bean.FName = rSet.getString("FName");
                bean.FNumber = rSet.getString("FNumber");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return container;
    }

    private ArrayList<suppliers> getSuppliers(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<suppliers> container = new ArrayList<DownloadReturnBean.suppliers>();
        String sql = "SELECT  t1.FItemID ,FItemClassID,t1.FNumber,t1.FParentID,FLevel,FDetail,t1.FName,FAddress,FPhone,FEmail   FROM t_Item t1  with(index (uk_Item2)) LEFT JOIN t_Supplier x2 ON t1.FItemID = x2.FItemID  WHERE FItemClassID = 8 AND (t1.FDetail = 1) AND t1.FDeleteD=0  ORDER BY t1.FNumber";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.suppliers bean = dBean.new suppliers();
                bean.FItemID = rSet.getString("FItemID");
                bean.FItemClassID = rSet.getString("FItemClassID");
                bean.FNumber = rSet.getString("FNumber");
                bean.FParentID = rSet.getString("FParentID");
                bean.FLevel = rSet.getString("FLevel");
                bean.FDetail = rSet.getString("FDetail");
                bean.FName = rSet.getString("FName");
                bean.FAddress = rSet.getString("FAddress");
                bean.FPhone = rSet.getString("FPhone");
                bean.FEmail = rSet.getString("FEmail");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return container;
    }

    private ArrayList<BarCode> getBarCodes(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<BarCode> container = new ArrayList<>();
        String sql = "";
        System.out.println("dwlVersion:"+version);
        if (version.contains("5001") && !version.equals("500116") && !version.equals("500115")) {
            sql = "select t2.FName,t1.FTypeID,t1.FItemID,t1.FBarCode,t2.FNumber,t1.FUnitID from t_Barcode t1 left join t_ICItem t2 on t1.FItemID=t2.FItemID";//�콢��
        } else {
            sql = "select t2.FName,t1.FTypeID,t1.FItemID,t1.FBarCode,t2.FNumber,'' as FUnitID from t_Barcode t1 left join t_ICItem t2 on t1.FItemID=t2.FItemID";
            System.out.println("sql2");
        }
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.BarCode bean = dBean.new BarCode();
                bean.FName = rSet.getString("FName");
                bean.FTypeID = rSet.getString("FTypeID");
                bean.FItemID = rSet.getString("FItemID");
                bean.FBarCode = rSet.getString("FBarCode");
                bean.FNumber = rSet.getString("FNumber");
                bean.FUnitID = rSet.getString("FUnitID");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("获取条码："+container.toString());
        return container;
    }

    //获取单位
    private ArrayList<Unit> getUnit(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<Unit> container = new ArrayList<DownloadReturnBean.Unit>();
        String sql = "select FMeasureUnitID,FUnitGroupID,FNumber,FName,FCoefficient from t_Measureunit where FMeasureUnitID!=0";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.Unit bean = dBean.new Unit();
                bean.FMeasureUnitID = rSet.getString("FMeasureUnitID");
                bean.FUnitGroupID = rSet.getString("FUnitGroupID");
                bean.FNumber = rSet.getString("FNumber");
                bean.FName = rSet.getString("FName");
                bean.FCoefficient = rSet.getString("FCoefficient");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return container;
    }

    private ArrayList<storage> getStorage(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<storage> container = new ArrayList<DownloadReturnBean.storage>();
        String sql = null;
        if (version.contains("5001")) {
            sql = "select FItemID,FEmpID,FName,FNumber,FTypeID,FSPGroupID,FGroupID," +
                    "FStockGroupID,FIsStockMgr,FUnderStock from t_Stock where FTypeID " +
                    "not in (501,502,503) AND FDeleteD=0";//k3 rise 12.3
        } else if (version.contains("80010" )) {
            sql = "select t1.FItemID,t1.FEmpID,t1.FName,t1.FNumber,t1.FTypeID,t1.FSPGroupID," +
                    "t1.FGroupID,t1.FStockGroupID,t1.FIsStockMgr,t1.FUnderStock from t_Stock" +
                    " t1 left join t_Item t2 on t1.FItemID=t2.FItemID WHERE t2.FItemClassID=5 AND t2.FDetail=1  AND (( FTypeID <> 504)) AND t2.FDeleteD=0 "; //���k3
        } else {
            sql = "select FItemID,FEmpID,FName,FNumber,FTypeID,FSPGroupID,FGroupID,FStockGroupID," +
                    "FIsStockMgr,'' as FUnderStock from t_Stock where FDeleteD=0 ";//רҵ��
        }
        Lg.e("仓库："+sql);
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.storage bean = dBean.new storage();
                bean.FItemID = rSet.getString("FItemID");
                bean.FEmpID = rSet.getString("FEmpID");
                bean.FName = rSet.getString("FName");
                bean.FNumber = rSet.getString("FNumber");
                bean.FTypeID = rSet.getString("FTypeID");
                bean.FSPGroupID = rSet.getString("FSPGroupID");
                bean.FGroupID = rSet.getString("FGroupID");
                bean.FStockGroupID = rSet.getString("FStockGroupID");
                bean.FIsStockMgr = rSet.getString("FIsStockMgr");
                bean.FUnderStock = rSet.getString("FUnderStock");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return container;
    }

    private ArrayList<InstorageNum> getInstorageNums(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<InstorageNum> container = new ArrayList<>();
        String sql = "select FItemID,FStockID,convert(float,FQty) as FQty,FBal,FStockPlaceID," +
                "FKFPeriod,FKFDate,rtrim(FBatchNo) AS FBatchNo FROM ICInventory";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.InstorageNum bean = dBean.new InstorageNum();
                bean.FItemID = rSet.getString("FItemID");
                bean.FStockID = rSet.getString("FStockID");
                bean.FQty = rSet.getString("FQty");
                bean.FBal = rSet.getString("FBal");
                bean.FStockPlaceID = rSet.getString("FStockPlaceID");
                bean.FKFPeriod = rSet.getString("FKFPeriod");
                bean.FKFDate = rSet.getString("FKFDate");
                bean.FBatchNo = rSet.getString("FBatchNo");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return container;
    }

    private ArrayList<wavehouse> getWaveHouse(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<wavehouse> container = new ArrayList<>();
        String sql = "select a.FSPID,a.FSPGroupID,a.FNumber,a.FName,a.FFullName,a.FLevel,a.FDetail,a.FParentID,"
                + "'' as FClassTypeID,ISNULL(b.FDefaultSPID,0) as FDefaultSPID from t_StockPlace a left join "
                + "t_StockPlaceGroup b on a.FSPID=b.FDefaultSPID where a.FDetail=1";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.wavehouse bean = dBean.new wavehouse();
                bean.FSPID = rSet.getString("FSPID");
                bean.FSPGroupID = rSet.getString("FSPGroupID");
                bean.FNumber = rSet.getString("FNumber");
                bean.FName = rSet.getString("FName");
                bean.FFullName = rSet.getString("FFullName");
                bean.FLevel = rSet.getString("FLevel");
                bean.FDetail = rSet.getString("FDetail");
                bean.FParentID = rSet.getString("FParentID");
                bean.FClassTypeID = rSet.getString("FClassTypeID");
                bean.FDefaultSPID = rSet.getString("FDefaultSPID");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return container;
    }

    private ArrayList<employee> getEmployee(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        ArrayList<employee> container = new ArrayList<>();
        String sql = "select FItemID,FName,FNumber,FDepartmentID,FEmpGroup,FEmpGroupID from t_Emp where FItemID>0 and FDeleteD=0 order by FName collate Chinese_PRC_CI_AS";
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.employee emp = dBean.new employee();
                emp.FItemID = rSet.getString("FItemID");
                emp.FName = rSet.getString("FName");
                emp.FNumber = rSet.getString("FNumber");
                emp.FDpartmentID = rSet.getString("FDepartmentID");
                emp.FEmpGroup = rSet.getString("FEmpGroup");
                emp.FEmpGroupID = rSet.getString("FEmpGroupID");
                container.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return container;
    }

    private ArrayList<DownloadReturnBean.department> getDepartment(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        String sql = "select FItemID , FNumber, FName, FParentID  from t_Department";
        ArrayList<DownloadReturnBean.department> container = new ArrayList<>();
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.department bean = dBean.new department();
                bean.FItemID = rSet.getString("FItemID");
                bean.FName = rSet.getString("FName");
                bean.FNumber = rSet.getString("FNumber");
                bean.FparentID = rSet.getString("FParentID");
                container.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return container;
    }

    private ArrayList<DownloadReturnBean.bibiezhong> getBiBieBiaoData(Statement statement, ResultSet rSet, String version, DownloadReturnBean dBean) {
        String sql = null;
        ArrayList<DownloadReturnBean.bibiezhong> container = new ArrayList<>();
        if (version.contains("3003"))//
        {
            sql = "select FCurrencyID,FNumber,FName,FExchangeRate,'' as FClassTypeID  from t_Currency where FCurrencyID!=0";//רҵ��
        } else {
            sql = "select FCurrencyID,FNumber,FName,FExchangeRate,FClassTypeID  from t_Currency where FCurrencyID!=0";//���k3���콢��
        }
        try {
            rSet = statement.executeQuery(sql);
            while (rSet.next()) {
                DownloadReturnBean.bibiezhong bean = dBean.new bibiezhong();
                bean.FCurrencyID = rSet.getString("FCurrencyID");
                bean.FNumber = rSet.getString("FNumber");
                bean.FName = rSet.getString("FName");
                bean.FExChangeRate = rSet.getString("FExchangeRate");
                container.add(bean);
                System.out.println(bean.FCurrencyID + "\\" + bean.FNumber + "\\" + bean.FName + "\\" + bean.FExChangeRate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return container;
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
