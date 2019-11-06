package com.fangzuo.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.fangzuo.assist.Dao.GProduct;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GPRODUCT".
*/
public class GProductDao extends AbstractDao<GProduct, Long> {

    public static final String TABLENAME = "GPRODUCT";

    /**
     * Properties of entity GProduct.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FInterID = new Property(1, String.class, "FInterID", false, "FINTER_ID");
        public final static Property FID = new Property(2, String.class, "FID", false, "FID");
        public final static Property FName = new Property(3, String.class, "FName", false, "FNAME");
    }


    public GProductDao(DaoConfig config) {
        super(config);
    }
    
    public GProductDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GPRODUCT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"FINTER_ID\" TEXT," + // 1: FInterID
                "\"FID\" TEXT," + // 2: FID
                "\"FNAME\" TEXT);"); // 3: FName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GPRODUCT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GProduct entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String FInterID = entity.getFInterID();
        if (FInterID != null) {
            stmt.bindString(2, FInterID);
        }
 
        String FID = entity.getFID();
        if (FID != null) {
            stmt.bindString(3, FID);
        }
 
        String FName = entity.getFName();
        if (FName != null) {
            stmt.bindString(4, FName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GProduct entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String FInterID = entity.getFInterID();
        if (FInterID != null) {
            stmt.bindString(2, FInterID);
        }
 
        String FID = entity.getFID();
        if (FID != null) {
            stmt.bindString(3, FID);
        }
 
        String FName = entity.getFName();
        if (FName != null) {
            stmt.bindString(4, FName);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GProduct readEntity(Cursor cursor, int offset) {
        GProduct entity = new GProduct( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // FInterID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // FID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // FName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GProduct entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFInterID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFID(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GProduct entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GProduct entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GProduct entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}