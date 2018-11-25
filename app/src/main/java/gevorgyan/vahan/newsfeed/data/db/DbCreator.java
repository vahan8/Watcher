package gevorgyan.vahan.newsfeed.data.db;

import android.database.sqlite.SQLiteDatabase;

import gevorgyan.vahan.newsfeed.data.dao.ArticleDao;

public class DbCreator {

    public static void createTableArticles(SQLiteDatabase db) {
        db.execSQL("create table " +
                ArticleDao.TABLE_NAME + " ("
                + ArticleDao.ID                   + " text not null primary key,"
                + ArticleDao.SECTION_ID           + " text not null,"
                + ArticleDao.SECTION_NAME         + " text not null,"
                + ArticleDao.WEB_PUBLICATION_DATE + " integer,"
                + ArticleDao.WEB_TITLE            + " text,"
                + ArticleDao.WEB_URL              + " text,"
                + ArticleDao.API_URL              + " text,"
                + ArticleDao.THUMBNAIL_URL        + " text,"
                + ArticleDao.IS_PINNED            + " integer,"
                + ArticleDao.CREATTION_DATE       + " integer,"
                + ArticleDao.IMAGE                + " blob)");
    }

}
