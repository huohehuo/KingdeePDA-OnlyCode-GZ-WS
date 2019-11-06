package com.fangzuo.greendao.gen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * Master of DAO (schema version 20): knows all DAOs.
 */
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 20;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(Database db, boolean ifNotExists) {
        BarCodeDao.createTable(db, ifNotExists);
        BibieDao.createTable(db, ifNotExists);
        BibieTableDao.createTable(db, ifNotExists);
        ClientDao.createTable(db, ifNotExists);
        DepartmentDao.createTable(db, ifNotExists);
        EmployeeDao.createTable(db, ifNotExists);
        GetGoodsDepartmentDao.createTable(db, ifNotExists);
        InStorageNumDao.createTable(db, ifNotExists);
        InStoreTypeDao.createTable(db, ifNotExists);
        PayTypeDao.createTable(db, ifNotExists);
        PDMainDao.createTable(db, ifNotExists);
        PDSubDao.createTable(db, ifNotExists);
        PriceMethodDao.createTable(db, ifNotExists);
        ProductDao.createTable(db, ifNotExists);
        PurchaseMethodDao.createTable(db, ifNotExists);
        PushDownMainDao.createTable(db, ifNotExists);
        PushDownSubDao.createTable(db, ifNotExists);
        StorageDao.createTable(db, ifNotExists);
        SuppliersDao.createTable(db, ifNotExists);
        T_DetailDao.createTable(db, ifNotExists);
        T_mainDao.createTable(db, ifNotExists);
        UnitDao.createTable(db, ifNotExists);
        UserDao.createTable(db, ifNotExists);
        WanglaikemuDao.createTable(db, ifNotExists);
        WaveHouseDao.createTable(db, ifNotExists);
        YuandanTypeDao.createTable(db, ifNotExists);
        MainLockDataDao.createTable(db, ifNotExists);
        SendOrderListBeanDao.createTable(db, ifNotExists);
        GProductDao.createTable(db, ifNotExists);
        Suppliers4P2Dao.createTable(db, ifNotExists);
    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(Database db, boolean ifExists) {
        BarCodeDao.dropTable(db, ifExists);
        BibieDao.dropTable(db, ifExists);
        BibieTableDao.dropTable(db, ifExists);
        ClientDao.dropTable(db, ifExists);
        DepartmentDao.dropTable(db, ifExists);
        EmployeeDao.dropTable(db, ifExists);
        GetGoodsDepartmentDao.dropTable(db, ifExists);
        InStorageNumDao.dropTable(db, ifExists);
        InStoreTypeDao.dropTable(db, ifExists);
        PayTypeDao.dropTable(db, ifExists);
        PDMainDao.dropTable(db, ifExists);
        PDSubDao.dropTable(db, ifExists);
        PriceMethodDao.dropTable(db, ifExists);
        ProductDao.dropTable(db, ifExists);
        PurchaseMethodDao.dropTable(db, ifExists);
        PushDownMainDao.dropTable(db, ifExists);
        PushDownSubDao.dropTable(db, ifExists);
        StorageDao.dropTable(db, ifExists);
        SuppliersDao.dropTable(db, ifExists);
        T_DetailDao.dropTable(db, ifExists);
        T_mainDao.dropTable(db, ifExists);
        UnitDao.dropTable(db, ifExists);
        UserDao.dropTable(db, ifExists);
        WanglaikemuDao.dropTable(db, ifExists);
        WaveHouseDao.dropTable(db, ifExists);
        YuandanTypeDao.dropTable(db, ifExists);
        MainLockDataDao.dropTable(db, ifExists);
        SendOrderListBeanDao.dropTable(db, ifExists);
        GProductDao.dropTable(db, ifExists);
        Suppliers4P2Dao.dropTable(db, ifExists);
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     * Convenience method using a {@link DevOpenHelper}.
     */
    public static DaoSession newDevSession(Context context, String name) {
        Database db = new DevOpenHelper(context, name).getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    public DaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public DaoMaster(Database db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(BarCodeDao.class);
        registerDaoClass(BibieDao.class);
        registerDaoClass(BibieTableDao.class);
        registerDaoClass(ClientDao.class);
        registerDaoClass(DepartmentDao.class);
        registerDaoClass(EmployeeDao.class);
        registerDaoClass(GetGoodsDepartmentDao.class);
        registerDaoClass(InStorageNumDao.class);
        registerDaoClass(InStoreTypeDao.class);
        registerDaoClass(PayTypeDao.class);
        registerDaoClass(PDMainDao.class);
        registerDaoClass(PDSubDao.class);
        registerDaoClass(PriceMethodDao.class);
        registerDaoClass(ProductDao.class);
        registerDaoClass(PurchaseMethodDao.class);
        registerDaoClass(PushDownMainDao.class);
        registerDaoClass(PushDownSubDao.class);
        registerDaoClass(StorageDao.class);
        registerDaoClass(SuppliersDao.class);
        registerDaoClass(T_DetailDao.class);
        registerDaoClass(T_mainDao.class);
        registerDaoClass(UnitDao.class);
        registerDaoClass(UserDao.class);
        registerDaoClass(WanglaikemuDao.class);
        registerDaoClass(WaveHouseDao.class);
        registerDaoClass(YuandanTypeDao.class);
        registerDaoClass(MainLockDataDao.class);
        registerDaoClass(SendOrderListBeanDao.class);
        registerDaoClass(GProductDao.class);
        registerDaoClass(Suppliers4P2Dao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    /**
     * Calls {@link #createAllTables(Database, boolean)} in {@link #onCreate(Database)} -
     */
    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

}
