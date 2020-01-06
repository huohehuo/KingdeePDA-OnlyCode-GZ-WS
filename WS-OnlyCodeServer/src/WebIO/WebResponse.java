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
