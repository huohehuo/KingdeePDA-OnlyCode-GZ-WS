package WebIO;

import java.util.ArrayList;

public class WebResponse {
	public boolean state;
	public String backString;
	public int size;
	public WebResponse() {
		this.backString = "";
	}
	public WebResponse(boolean state, String backString) {
		this.state = state;
		this.backString = backString;
	}

	public ArrayList<Suppliers4P2> suppliers4P2s;
	public ArrayList<Product> products;
	public ArrayList<CheckLogBackBean> checkLogBackBeans;
	public ArrayList<ProductType> productTypes;
	public ArrayList<Storage> storages;
	public ArrayList<MdUser> mdUsers;
	public ArrayList<Client> clients;
	public ArrayList<SoldOutSortBean> soldOutSortBeans;
	public ArrayList<CqPdPlanBean> cqPdPlanBeans;
	public ArrayList<PDSub> pdSubs;
	public ArrayList<CodeCheckBackDataBean> codeCheckBackDataBeans;

	//条码检测
	public class CodeCheckBackDataBean {
		public String FTip;
		public String FBillNo;
		public String FItemID;
		public String FUnitID;
		public String FQty;
		public String FStockID;
		public String FStockPlaceID;
		public String FBatchNo;
		public String FKFPeriod;
		public String FKFDate;
		public String FNumber;
		public String FName;
		public String FPrice;
		public String FOLOrderBillNo;
	}

	//盘点明细
	public class PDSub {
		public String FID;
		public String FStockID;
		public String FItemID;
		public String FSPName;
		public String FNumber;
		public String FName;
		public String FModel;
		public String FStockPlaceID;
		public String FUnitName;
		public String FQty;//z账存数量
		public String FQtyAct1;
		public String FQtyAct;//实存数量
		public String FCheckQty1;//一盘数量
		public String FCheckQty;//本次盘点数量
		public String FAdjQty1;//本次调整数量
		public String FAdjQty;//已调整数量
		public String FRemark;
		public String FUnitID;
		public String FUnitGroupID;
		public String FBatchNo;
		public String FBarCode;
		public String FStockName;

	}

	//初期盘点方案
	public class CqPdPlanBean {
		public String FName;
		public String FID;
		public String FDate;
		public String FMaker ;
		public String FRemark ;
	}

	//销售出库分类汇总
	public class SoldOutSortBean {
		public String FProductType;
		public String FNum;
		public String FQty;
		public String FMoney ;
	}
	public class Suppliers4P2 {
		public String FPassWord;
		public String FUserID;
		public String FID;
		public String FName ;
		public String FLevel;//等级：1：一般权限，2:管理员权限，3：超级管理员
		public String FPermit;
	}
	//客户
	public class Client {
		public String FItemID;
		public String FName;
		public String FNumber;
	}
	//门店用户
	public class MdUser {
		public String FID;
		public String FName;
	}
	//产品系列
	public class ProductType {
		public String FItemID;
		public String FNumber;
		public String FName;
	}
	//仓库
	public class Storage {
		public String FItemID;
		public String FNumber;
		public String FName;
	}
	public class Product {
		public String FBarcode;
		public String FNumber;
		public String FQty;
		public String FName;
		public String FItemID;
		public String FUnitID;
		public String FStockID;
		public String FStockPlaceID;
		public String FBatchNo;
		public String FKFPeriod;
		public String FKFDate;
		public String FPrice;
		public String FProductType;
		public String FBelongStorage;
		public String FModel;
	}

	public class CheckLogBackBean {
		public String FOrderBillNo;
		public String FProductType;
		public String FMDUser;
		public String FRedOrBlue;
		public String FInterID;
		public String FBillNo;
		public String FCLientName;
		public String FName;
		public String FNum;
		public String FPrice;
		public String FBarCode;


		public String FNumber;
		public String FBaseQty;
		public String FSecQty;
		public String FSecUnit;
		public String FBaseUnit;
		public String FStorage;
		public String FWaveHouse;
		public String FBatchNo;
		public String FKFDate;
		public String FKFPeriod;
		public String FStockID;
		public String FItemID;
		public String FQty;
		public String FUnit;
		public String FModel;

	}


}
