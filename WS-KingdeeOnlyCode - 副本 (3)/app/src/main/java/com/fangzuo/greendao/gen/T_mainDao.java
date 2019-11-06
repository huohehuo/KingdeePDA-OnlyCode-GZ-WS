package com.fangzuo.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.fangzuo.assist.Dao.T_main;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "T_MAIN".
*/
public class T_mainDao extends AbstractDao<T_main, String> {

    public static final String TABLENAME = "T_MAIN";

    /**
     * Properties of entity T_main.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property OrderId = new Property(0, long.class, "orderId", false, "ORDER_ID");
        public final static Property FIndex = new Property(1, String.class, "FIndex", true, "FINDEX");
        public final static Property OrderDate = new Property(2, String.class, "orderDate", false, "ORDER_DATE");
        public final static Property SaleRangeId = new Property(3, String.class, "saleRangeId", false, "SALE_RANGE_ID");
        public final static Property SaleRange = new Property(4, String.class, "saleRange", false, "SALE_RANGE");
        public final static Property SaleWayId = new Property(5, String.class, "saleWayId", false, "SALE_WAY_ID");
        public final static Property SaleWay = new Property(6, String.class, "saleWay", false, "SALE_WAY");
        public final static Property SourceOrderTypeId = new Property(7, String.class, "sourceOrderTypeId", false, "SOURCE_ORDER_TYPE_ID");
        public final static Property SourceOrderType = new Property(8, String.class, "sourceOrderType", false, "SOURCE_ORDER_TYPE");
        public final static Property FDeliveryType = new Property(9, String.class, "FDeliveryType", false, "FDELIVERY_TYPE");
        public final static Property FDeliveryTypeId = new Property(10, String.class, "FDeliveryTypeId", false, "FDELIVERY_TYPE_ID");
        public final static Property FPaymentTypeId = new Property(11, String.class, "FPaymentTypeId", false, "FPAYMENT_TYPE_ID");
        public final static Property FPaymentType = new Property(12, String.class, "FPaymentType", false, "FPAYMENT_TYPE");
        public final static Property FPaymentDate = new Property(13, String.class, "FPaymentDate", false, "FPAYMENT_DATE");
        public final static Property FDeliveryAddress = new Property(14, String.class, "FDeliveryAddress", false, "FDELIVERY_ADDRESS");
        public final static Property FDepartmentId = new Property(15, String.class, "FDepartmentId", false, "FDEPARTMENT_ID");
        public final static Property FDepartment = new Property(16, String.class, "FDepartment", false, "FDEPARTMENT");
        public final static Property FSalesManId = new Property(17, String.class, "FSalesManId", false, "FSALES_MAN_ID");
        public final static Property FSalesMan = new Property(18, String.class, "FSalesMan", false, "FSALES_MAN");
        public final static Property FDirectorId = new Property(19, String.class, "FDirectorId", false, "FDIRECTOR_ID");
        public final static Property FDirector = new Property(20, String.class, "FDirector", false, "FDIRECTOR");
        public final static Property FPurchaseUnitId = new Property(21, String.class, "FPurchaseUnitId", false, "FPURCHASE_UNIT_ID");
        public final static Property FPurchaseUnit = new Property(22, String.class, "FPurchaseUnit", false, "FPURCHASE_UNIT");
        public final static Property FRemark = new Property(23, String.class, "FRemark", false, "FREMARK");
        public final static Property FIdentity = new Property(24, String.class, "FIdentity", false, "FIDENTITY");
        public final static Property FBusinessId = new Property(25, String.class, "FBusinessId", false, "FBUSINESS_ID");
        public final static Property FMaker = new Property(26, String.class, "FMaker", false, "FMAKER");
        public final static Property FMakerId = new Property(27, String.class, "FMakerId", false, "FMAKER_ID");
        public final static Property FBusiness = new Property(28, String.class, "FBusiness", false, "FBUSINESS");
        public final static Property FSendOutId = new Property(29, String.class, "FSendOutId", false, "FSEND_OUT_ID");
        public final static Property FSendOut = new Property(30, String.class, "FSendOut", false, "FSEND_OUT");
        public final static Property FCustodyId = new Property(31, String.class, "FCustodyId", false, "FCUSTODY_ID");
        public final static Property FCustody = new Property(32, String.class, "FCustody", false, "FCUSTODY");
        public final static Property FRedBlue = new Property(33, String.class, "FRedBlue", false, "FRED_BLUE");
        public final static Property FAcountID = new Property(34, String.class, "FAcountID", false, "FACOUNT_ID");
        public final static Property FAcount = new Property(35, String.class, "FAcount", false, "FACOUNT");
        public final static Property Rem = new Property(36, String.class, "Rem", false, "REM");
        public final static Property Supplier = new Property(37, String.class, "supplier", false, "SUPPLIER");
        public final static Property SupplierId = new Property(38, String.class, "supplierId", false, "SUPPLIER_ID");
        public final static Property Activity = new Property(39, int.class, "activity", false, "ACTIVITY");
        public final static Property IMIE = new Property(40, String.class, "IMIE", false, "IMIE");
        public final static Property MakerId = new Property(41, String.class, "MakerId", false, "MAKER_ID");
        public final static Property DataInput = new Property(42, String.class, "DataInput", false, "DATA_INPUT");
        public final static Property DataPush = new Property(43, String.class, "DataPush", false, "DATA_PUSH");
    }


    public T_mainDao(DaoConfig config) {
        super(config);
    }
    
    public T_mainDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"T_MAIN\" (" + //
                "\"ORDER_ID\" INTEGER NOT NULL ," + // 0: orderId
                "\"FINDEX\" TEXT PRIMARY KEY NOT NULL ," + // 1: FIndex
                "\"ORDER_DATE\" TEXT," + // 2: orderDate
                "\"SALE_RANGE_ID\" TEXT," + // 3: saleRangeId
                "\"SALE_RANGE\" TEXT," + // 4: saleRange
                "\"SALE_WAY_ID\" TEXT," + // 5: saleWayId
                "\"SALE_WAY\" TEXT," + // 6: saleWay
                "\"SOURCE_ORDER_TYPE_ID\" TEXT," + // 7: sourceOrderTypeId
                "\"SOURCE_ORDER_TYPE\" TEXT," + // 8: sourceOrderType
                "\"FDELIVERY_TYPE\" TEXT," + // 9: FDeliveryType
                "\"FDELIVERY_TYPE_ID\" TEXT," + // 10: FDeliveryTypeId
                "\"FPAYMENT_TYPE_ID\" TEXT," + // 11: FPaymentTypeId
                "\"FPAYMENT_TYPE\" TEXT," + // 12: FPaymentType
                "\"FPAYMENT_DATE\" TEXT," + // 13: FPaymentDate
                "\"FDELIVERY_ADDRESS\" TEXT," + // 14: FDeliveryAddress
                "\"FDEPARTMENT_ID\" TEXT," + // 15: FDepartmentId
                "\"FDEPARTMENT\" TEXT," + // 16: FDepartment
                "\"FSALES_MAN_ID\" TEXT," + // 17: FSalesManId
                "\"FSALES_MAN\" TEXT," + // 18: FSalesMan
                "\"FDIRECTOR_ID\" TEXT," + // 19: FDirectorId
                "\"FDIRECTOR\" TEXT," + // 20: FDirector
                "\"FPURCHASE_UNIT_ID\" TEXT," + // 21: FPurchaseUnitId
                "\"FPURCHASE_UNIT\" TEXT," + // 22: FPurchaseUnit
                "\"FREMARK\" TEXT," + // 23: FRemark
                "\"FIDENTITY\" TEXT," + // 24: FIdentity
                "\"FBUSINESS_ID\" TEXT," + // 25: FBusinessId
                "\"FMAKER\" TEXT," + // 26: FMaker
                "\"FMAKER_ID\" TEXT," + // 27: FMakerId
                "\"FBUSINESS\" TEXT," + // 28: FBusiness
                "\"FSEND_OUT_ID\" TEXT," + // 29: FSendOutId
                "\"FSEND_OUT\" TEXT," + // 30: FSendOut
                "\"FCUSTODY_ID\" TEXT," + // 31: FCustodyId
                "\"FCUSTODY\" TEXT," + // 32: FCustody
                "\"FRED_BLUE\" TEXT," + // 33: FRedBlue
                "\"FACOUNT_ID\" TEXT," + // 34: FAcountID
                "\"FACOUNT\" TEXT," + // 35: FAcount
                "\"REM\" TEXT," + // 36: Rem
                "\"SUPPLIER\" TEXT," + // 37: supplier
                "\"SUPPLIER_ID\" TEXT," + // 38: supplierId
                "\"ACTIVITY\" INTEGER NOT NULL ," + // 39: activity
                "\"IMIE\" TEXT," + // 40: IMIE
                "\"MAKER_ID\" TEXT," + // 41: MakerId
                "\"DATA_INPUT\" TEXT," + // 42: DataInput
                "\"DATA_PUSH\" TEXT);"); // 43: DataPush
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"T_MAIN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, T_main entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getOrderId());
 
        String FIndex = entity.getFIndex();
        if (FIndex != null) {
            stmt.bindString(2, FIndex);
        }
 
        String orderDate = entity.getOrderDate();
        if (orderDate != null) {
            stmt.bindString(3, orderDate);
        }
 
        String saleRangeId = entity.getSaleRangeId();
        if (saleRangeId != null) {
            stmt.bindString(4, saleRangeId);
        }
 
        String saleRange = entity.getSaleRange();
        if (saleRange != null) {
            stmt.bindString(5, saleRange);
        }
 
        String saleWayId = entity.getSaleWayId();
        if (saleWayId != null) {
            stmt.bindString(6, saleWayId);
        }
 
        String saleWay = entity.getSaleWay();
        if (saleWay != null) {
            stmt.bindString(7, saleWay);
        }
 
        String sourceOrderTypeId = entity.getSourceOrderTypeId();
        if (sourceOrderTypeId != null) {
            stmt.bindString(8, sourceOrderTypeId);
        }
 
        String sourceOrderType = entity.getSourceOrderType();
        if (sourceOrderType != null) {
            stmt.bindString(9, sourceOrderType);
        }
 
        String FDeliveryType = entity.getFDeliveryType();
        if (FDeliveryType != null) {
            stmt.bindString(10, FDeliveryType);
        }
 
        String FDeliveryTypeId = entity.getFDeliveryTypeId();
        if (FDeliveryTypeId != null) {
            stmt.bindString(11, FDeliveryTypeId);
        }
 
        String FPaymentTypeId = entity.getFPaymentTypeId();
        if (FPaymentTypeId != null) {
            stmt.bindString(12, FPaymentTypeId);
        }
 
        String FPaymentType = entity.getFPaymentType();
        if (FPaymentType != null) {
            stmt.bindString(13, FPaymentType);
        }
 
        String FPaymentDate = entity.getFPaymentDate();
        if (FPaymentDate != null) {
            stmt.bindString(14, FPaymentDate);
        }
 
        String FDeliveryAddress = entity.getFDeliveryAddress();
        if (FDeliveryAddress != null) {
            stmt.bindString(15, FDeliveryAddress);
        }
 
        String FDepartmentId = entity.getFDepartmentId();
        if (FDepartmentId != null) {
            stmt.bindString(16, FDepartmentId);
        }
 
        String FDepartment = entity.getFDepartment();
        if (FDepartment != null) {
            stmt.bindString(17, FDepartment);
        }
 
        String FSalesManId = entity.getFSalesManId();
        if (FSalesManId != null) {
            stmt.bindString(18, FSalesManId);
        }
 
        String FSalesMan = entity.getFSalesMan();
        if (FSalesMan != null) {
            stmt.bindString(19, FSalesMan);
        }
 
        String FDirectorId = entity.getFDirectorId();
        if (FDirectorId != null) {
            stmt.bindString(20, FDirectorId);
        }
 
        String FDirector = entity.getFDirector();
        if (FDirector != null) {
            stmt.bindString(21, FDirector);
        }
 
        String FPurchaseUnitId = entity.getFPurchaseUnitId();
        if (FPurchaseUnitId != null) {
            stmt.bindString(22, FPurchaseUnitId);
        }
 
        String FPurchaseUnit = entity.getFPurchaseUnit();
        if (FPurchaseUnit != null) {
            stmt.bindString(23, FPurchaseUnit);
        }
 
        String FRemark = entity.getFRemark();
        if (FRemark != null) {
            stmt.bindString(24, FRemark);
        }
 
        String FIdentity = entity.getFIdentity();
        if (FIdentity != null) {
            stmt.bindString(25, FIdentity);
        }
 
        String FBusinessId = entity.getFBusinessId();
        if (FBusinessId != null) {
            stmt.bindString(26, FBusinessId);
        }
 
        String FMaker = entity.getFMaker();
        if (FMaker != null) {
            stmt.bindString(27, FMaker);
        }
 
        String FMakerId = entity.getFMakerId();
        if (FMakerId != null) {
            stmt.bindString(28, FMakerId);
        }
 
        String FBusiness = entity.getFBusiness();
        if (FBusiness != null) {
            stmt.bindString(29, FBusiness);
        }
 
        String FSendOutId = entity.getFSendOutId();
        if (FSendOutId != null) {
            stmt.bindString(30, FSendOutId);
        }
 
        String FSendOut = entity.getFSendOut();
        if (FSendOut != null) {
            stmt.bindString(31, FSendOut);
        }
 
        String FCustodyId = entity.getFCustodyId();
        if (FCustodyId != null) {
            stmt.bindString(32, FCustodyId);
        }
 
        String FCustody = entity.getFCustody();
        if (FCustody != null) {
            stmt.bindString(33, FCustody);
        }
 
        String FRedBlue = entity.getFRedBlue();
        if (FRedBlue != null) {
            stmt.bindString(34, FRedBlue);
        }
 
        String FAcountID = entity.getFAcountID();
        if (FAcountID != null) {
            stmt.bindString(35, FAcountID);
        }
 
        String FAcount = entity.getFAcount();
        if (FAcount != null) {
            stmt.bindString(36, FAcount);
        }
 
        String Rem = entity.getRem();
        if (Rem != null) {
            stmt.bindString(37, Rem);
        }
 
        String supplier = entity.getSupplier();
        if (supplier != null) {
            stmt.bindString(38, supplier);
        }
 
        String supplierId = entity.getSupplierId();
        if (supplierId != null) {
            stmt.bindString(39, supplierId);
        }
        stmt.bindLong(40, entity.getActivity());
 
        String IMIE = entity.getIMIE();
        if (IMIE != null) {
            stmt.bindString(41, IMIE);
        }
 
        String MakerId = entity.getMakerId();
        if (MakerId != null) {
            stmt.bindString(42, MakerId);
        }
 
        String DataInput = entity.getDataInput();
        if (DataInput != null) {
            stmt.bindString(43, DataInput);
        }
 
        String DataPush = entity.getDataPush();
        if (DataPush != null) {
            stmt.bindString(44, DataPush);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, T_main entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getOrderId());
 
        String FIndex = entity.getFIndex();
        if (FIndex != null) {
            stmt.bindString(2, FIndex);
        }
 
        String orderDate = entity.getOrderDate();
        if (orderDate != null) {
            stmt.bindString(3, orderDate);
        }
 
        String saleRangeId = entity.getSaleRangeId();
        if (saleRangeId != null) {
            stmt.bindString(4, saleRangeId);
        }
 
        String saleRange = entity.getSaleRange();
        if (saleRange != null) {
            stmt.bindString(5, saleRange);
        }
 
        String saleWayId = entity.getSaleWayId();
        if (saleWayId != null) {
            stmt.bindString(6, saleWayId);
        }
 
        String saleWay = entity.getSaleWay();
        if (saleWay != null) {
            stmt.bindString(7, saleWay);
        }
 
        String sourceOrderTypeId = entity.getSourceOrderTypeId();
        if (sourceOrderTypeId != null) {
            stmt.bindString(8, sourceOrderTypeId);
        }
 
        String sourceOrderType = entity.getSourceOrderType();
        if (sourceOrderType != null) {
            stmt.bindString(9, sourceOrderType);
        }
 
        String FDeliveryType = entity.getFDeliveryType();
        if (FDeliveryType != null) {
            stmt.bindString(10, FDeliveryType);
        }
 
        String FDeliveryTypeId = entity.getFDeliveryTypeId();
        if (FDeliveryTypeId != null) {
            stmt.bindString(11, FDeliveryTypeId);
        }
 
        String FPaymentTypeId = entity.getFPaymentTypeId();
        if (FPaymentTypeId != null) {
            stmt.bindString(12, FPaymentTypeId);
        }
 
        String FPaymentType = entity.getFPaymentType();
        if (FPaymentType != null) {
            stmt.bindString(13, FPaymentType);
        }
 
        String FPaymentDate = entity.getFPaymentDate();
        if (FPaymentDate != null) {
            stmt.bindString(14, FPaymentDate);
        }
 
        String FDeliveryAddress = entity.getFDeliveryAddress();
        if (FDeliveryAddress != null) {
            stmt.bindString(15, FDeliveryAddress);
        }
 
        String FDepartmentId = entity.getFDepartmentId();
        if (FDepartmentId != null) {
            stmt.bindString(16, FDepartmentId);
        }
 
        String FDepartment = entity.getFDepartment();
        if (FDepartment != null) {
            stmt.bindString(17, FDepartment);
        }
 
        String FSalesManId = entity.getFSalesManId();
        if (FSalesManId != null) {
            stmt.bindString(18, FSalesManId);
        }
 
        String FSalesMan = entity.getFSalesMan();
        if (FSalesMan != null) {
            stmt.bindString(19, FSalesMan);
        }
 
        String FDirectorId = entity.getFDirectorId();
        if (FDirectorId != null) {
            stmt.bindString(20, FDirectorId);
        }
 
        String FDirector = entity.getFDirector();
        if (FDirector != null) {
            stmt.bindString(21, FDirector);
        }
 
        String FPurchaseUnitId = entity.getFPurchaseUnitId();
        if (FPurchaseUnitId != null) {
            stmt.bindString(22, FPurchaseUnitId);
        }
 
        String FPurchaseUnit = entity.getFPurchaseUnit();
        if (FPurchaseUnit != null) {
            stmt.bindString(23, FPurchaseUnit);
        }
 
        String FRemark = entity.getFRemark();
        if (FRemark != null) {
            stmt.bindString(24, FRemark);
        }
 
        String FIdentity = entity.getFIdentity();
        if (FIdentity != null) {
            stmt.bindString(25, FIdentity);
        }
 
        String FBusinessId = entity.getFBusinessId();
        if (FBusinessId != null) {
            stmt.bindString(26, FBusinessId);
        }
 
        String FMaker = entity.getFMaker();
        if (FMaker != null) {
            stmt.bindString(27, FMaker);
        }
 
        String FMakerId = entity.getFMakerId();
        if (FMakerId != null) {
            stmt.bindString(28, FMakerId);
        }
 
        String FBusiness = entity.getFBusiness();
        if (FBusiness != null) {
            stmt.bindString(29, FBusiness);
        }
 
        String FSendOutId = entity.getFSendOutId();
        if (FSendOutId != null) {
            stmt.bindString(30, FSendOutId);
        }
 
        String FSendOut = entity.getFSendOut();
        if (FSendOut != null) {
            stmt.bindString(31, FSendOut);
        }
 
        String FCustodyId = entity.getFCustodyId();
        if (FCustodyId != null) {
            stmt.bindString(32, FCustodyId);
        }
 
        String FCustody = entity.getFCustody();
        if (FCustody != null) {
            stmt.bindString(33, FCustody);
        }
 
        String FRedBlue = entity.getFRedBlue();
        if (FRedBlue != null) {
            stmt.bindString(34, FRedBlue);
        }
 
        String FAcountID = entity.getFAcountID();
        if (FAcountID != null) {
            stmt.bindString(35, FAcountID);
        }
 
        String FAcount = entity.getFAcount();
        if (FAcount != null) {
            stmt.bindString(36, FAcount);
        }
 
        String Rem = entity.getRem();
        if (Rem != null) {
            stmt.bindString(37, Rem);
        }
 
        String supplier = entity.getSupplier();
        if (supplier != null) {
            stmt.bindString(38, supplier);
        }
 
        String supplierId = entity.getSupplierId();
        if (supplierId != null) {
            stmt.bindString(39, supplierId);
        }
        stmt.bindLong(40, entity.getActivity());
 
        String IMIE = entity.getIMIE();
        if (IMIE != null) {
            stmt.bindString(41, IMIE);
        }
 
        String MakerId = entity.getMakerId();
        if (MakerId != null) {
            stmt.bindString(42, MakerId);
        }
 
        String DataInput = entity.getDataInput();
        if (DataInput != null) {
            stmt.bindString(43, DataInput);
        }
 
        String DataPush = entity.getDataPush();
        if (DataPush != null) {
            stmt.bindString(44, DataPush);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1);
    }    

    @Override
    public T_main readEntity(Cursor cursor, int offset) {
        T_main entity = new T_main( //
            cursor.getLong(offset + 0), // orderId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // FIndex
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // orderDate
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // saleRangeId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // saleRange
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // saleWayId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // saleWay
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // sourceOrderTypeId
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // sourceOrderType
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // FDeliveryType
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // FDeliveryTypeId
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // FPaymentTypeId
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // FPaymentType
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // FPaymentDate
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // FDeliveryAddress
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // FDepartmentId
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // FDepartment
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // FSalesManId
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // FSalesMan
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // FDirectorId
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // FDirector
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // FPurchaseUnitId
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // FPurchaseUnit
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // FRemark
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // FIdentity
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // FBusinessId
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // FMaker
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // FMakerId
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // FBusiness
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // FSendOutId
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // FSendOut
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // FCustodyId
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // FCustody
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33), // FRedBlue
            cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34), // FAcountID
            cursor.isNull(offset + 35) ? null : cursor.getString(offset + 35), // FAcount
            cursor.isNull(offset + 36) ? null : cursor.getString(offset + 36), // Rem
            cursor.isNull(offset + 37) ? null : cursor.getString(offset + 37), // supplier
            cursor.isNull(offset + 38) ? null : cursor.getString(offset + 38), // supplierId
            cursor.getInt(offset + 39), // activity
            cursor.isNull(offset + 40) ? null : cursor.getString(offset + 40), // IMIE
            cursor.isNull(offset + 41) ? null : cursor.getString(offset + 41), // MakerId
            cursor.isNull(offset + 42) ? null : cursor.getString(offset + 42), // DataInput
            cursor.isNull(offset + 43) ? null : cursor.getString(offset + 43) // DataPush
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, T_main entity, int offset) {
        entity.setOrderId(cursor.getLong(offset + 0));
        entity.setFIndex(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setOrderDate(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSaleRangeId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSaleRange(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSaleWayId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSaleWay(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setSourceOrderTypeId(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSourceOrderType(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFDeliveryType(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setFDeliveryTypeId(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setFPaymentTypeId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setFPaymentType(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setFPaymentDate(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setFDeliveryAddress(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setFDepartmentId(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setFDepartment(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setFSalesManId(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setFSalesMan(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setFDirectorId(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setFDirector(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setFPurchaseUnitId(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setFPurchaseUnit(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setFRemark(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setFIdentity(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setFBusinessId(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setFMaker(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setFMakerId(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setFBusiness(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setFSendOutId(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setFSendOut(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setFCustodyId(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setFCustody(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setFRedBlue(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
        entity.setFAcountID(cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34));
        entity.setFAcount(cursor.isNull(offset + 35) ? null : cursor.getString(offset + 35));
        entity.setRem(cursor.isNull(offset + 36) ? null : cursor.getString(offset + 36));
        entity.setSupplier(cursor.isNull(offset + 37) ? null : cursor.getString(offset + 37));
        entity.setSupplierId(cursor.isNull(offset + 38) ? null : cursor.getString(offset + 38));
        entity.setActivity(cursor.getInt(offset + 39));
        entity.setIMIE(cursor.isNull(offset + 40) ? null : cursor.getString(offset + 40));
        entity.setMakerId(cursor.isNull(offset + 41) ? null : cursor.getString(offset + 41));
        entity.setDataInput(cursor.isNull(offset + 42) ? null : cursor.getString(offset + 42));
        entity.setDataPush(cursor.isNull(offset + 43) ? null : cursor.getString(offset + 43));
     }
    
    @Override
    protected final String updateKeyAfterInsert(T_main entity, long rowId) {
        return entity.getFIndex();
    }
    
    @Override
    public String getKey(T_main entity) {
        if(entity != null) {
            return entity.getFIndex();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(T_main entity) {
        return entity.getFIndex() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
