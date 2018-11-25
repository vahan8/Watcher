package gevorgyan.vahan.newsfeed.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import gevorgyan.vahan.newsfeed.App;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "the_watcher";
    private static final int VERSION = 10000;

    private static DbHelper instance;
    private static SQLiteDatabase db;
    private static int referenceCount = 0;

    private DbHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);

    }

    public DbHelper() {
        super(App.getContext(), DATABASE_NAME, null, VERSION);
    }

    public static synchronized DbHelper getInstance() {
        if (instance == null) {
            instance = new DbHelper(App.getContext(), VERSION);
        }
        return instance;
    }



//    public static synchronized void upgrade() {
//        getWritableDb();
//        closeDb();
//    }
//
//    public static synchronized void beginTransaction() {
//        getWritableDb();
//        db.beginTransaction();
//    }
//
//    public static synchronized void setTransactionSuccessful() {
//        db.setTransactionSuccessful();
//    }
//
//    public static synchronized void endTransaction() {
//        if (db != null && db.inTransaction())
//            db.endTransaction();
//        closeDb();
//    }
//
//    public static synchronized long insertOrThrow(String table, ContentValues values) {
//        if (db == null || !db.inTransaction())
//            throw new SQLiteException("You should do insert in transaction.");
//
//        return db.insertOrThrow(table, null, values);
//    }
//
//    public static synchronized long insertOrIgnore(String table, ContentValues values) {
//        if (db == null || !db.inTransaction())
//            throw new SQLiteException("You should do insert in transaction.");
//
//        return db.insertWithOnConflict(table, null, values, CONFLICT_IGNORE);
//    }
//
//    public static synchronized int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
//        if (db == null || !db.inTransaction())
//            throw new SQLiteException("You should do update in transaction.");
//
//        return db.update(table, values, whereClause, whereArgs);
//    }
//
//    public static synchronized int delete(String table, String whereClause, String[] whereArgs) {
//        if (db == null || !db.inTransaction())
//            throw new SQLiteException("You should do delete in transaction.");
//
//        return db.delete(table, whereClause, whereArgs);
//    }
//
//    public static synchronized int delete(String table) {
//        return delete(table, null, null);
//    }
//
//    public static synchronized Cursor query(String sql, List<String> args) {
//        return query(sql, args.toArray(new String[]{}));
//    }
//
//    public static synchronized Cursor query(String sql, String[] args) {
//        Cursor cursor = null;
//        try {
//            cursor = getReadableDb().rawQuery(sql, args);
//            cursor.moveToFirst();
//        } catch (SQLiteException e) {
//            if (cursor != null)
//                cursor.close();
//            else
//                DbHelper.closeDb();
//            throw e;
//        }
//        return cursor;
//    }

//    public static synchronized long queryNumEntries(String table, String selection, String[] selectionArgs) {
//        long res = 0;
//        try {
//            res = DatabaseUtils.queryNumEntries(getReadableDb(), table, selection, selectionArgs);
//        } finally {
//            closeDb();
//        }
//        return res;
//    }
//
//    public static synchronized long longForQuery(String query, String[] selectionArgs) {
//        long res = 0;
//        try {
//            res = DatabaseUtils.longForQuery(getReadableDb(), query, selectionArgs);
//        } finally {
//            closeDb();
//        }
//        return res;
//    }
//
//    /**
//     * You must call this only in scope of transaction
//     *
//     * @return
//     */
//    private static synchronized SQLiteDatabase getWritableDb() {
//        referenceCount++;
//        if (db != null && db.isReadOnly())
//            throw new SQLiteException("You should do all operations in transaction.");
//
//        // If db is opened and writable it will return the same instance of db
//        db = getInstance().getWritableDatabase();
////        LOGI(TAG, "ref count: " + referenceCount);
//        return db;
//    }
//
//    private static synchronized SQLiteDatabase getReadableDb() {
//        referenceCount++;
//        // If db is opened it will return the same instance of db
//        db = getInstance().getReadableDatabase();
////        LOGI(TAG, "ref count: " + referenceCount);
//        return db;
//    }

//    public static synchronized void closeDb() {
//        if (referenceCount == 0)
//            LOGE(TAG, "Attempt to close already closed DB");
//
//        if (referenceCount > 0) {
//            referenceCount--;
//            if (referenceCount == 0 && instance != null) {
//                db = null;
//                instance.close();
//                instance = null;
//            }
//        }
//    }
//


    @Override
    public void onCreate(SQLiteDatabase db) {

        DbCreator.createTableArticles(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}