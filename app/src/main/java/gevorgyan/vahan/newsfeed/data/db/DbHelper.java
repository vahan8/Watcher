package gevorgyan.vahan.newsfeed.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import gevorgyan.vahan.newsfeed.App;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "the_watcher";
    private static final int VERSION = 10000;

    private static DbHelper instance;

    private DbHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);

    }

    public static synchronized DbHelper getInstance() {
        if (instance == null) {
            instance = new DbHelper(App.getContext(), VERSION);
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        DbCreator.createTableArticles(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}