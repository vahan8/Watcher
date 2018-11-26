package gevorgyan.vahan.newsfeed.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gevorgyan.vahan.newsfeed.data.db.DbHelper;
import gevorgyan.vahan.newsfeed.domain.model.Article;

public class ArticleDao {

    public static final String TABLE_NAME = "ARTICLES";

    public static final String ID = "fID";
    public static final String SECTION_ID = "fSECTIONID";
    public static final String SECTION_NAME = "fSECTIONNAME";
    public static final String WEB_PUBLICATION_DATE = "fWEBPUBLICATIONDATE";
    public static final String WEB_TITLE = "fWEBTITLE";
    public static final String WEB_URL = "fWEBURL";
    public static final String API_URL = "fAPIURL";
    public static final String THUMBNAIL_URL = "fTHUMBNAILURL";
    public static final String IS_PINNED = "fISPINNED";
    public static final String CREATTION_DATE = "fCREATIONDATE";
    public static final String IMAGE = "fIMAGE";

    private ArticleDao() {
    }

    public static void deleteAndInsert(List<Article> articles) throws SQLiteException {
        insert(articles, true);
    }

    public static void insert(Article article) throws SQLiteException {
        List<Article> articles = new ArrayList<>();
        articles.add(article);
        insert(articles, false);
    }

    public static void insert(List<Article> articles) throws SQLiteException {
        insert(articles, false);
    }

    private static void insert(List<Article> articles, boolean deleteBeforeInsert) throws SQLiteException {
        SQLiteDatabase db = DbHelper.getInstance().getWritableDatabase();
        db.beginTransaction();
        try {
            if (deleteBeforeInsert) {
                db.delete(TABLE_NAME, null, null);
            }
            for (Article article : articles) {
                ContentValues cv = new ContentValues();
                cv.put(ID, article.getId());
                cv.put(SECTION_ID, article.getSectionId());
                cv.put(SECTION_NAME, article.getSectionName());
                cv.put(WEB_PUBLICATION_DATE, article.getWebPublicationDate().getTime());
                cv.put(WEB_TITLE, article.getWebTitle());
                cv.put(WEB_URL, article.getWebUrl());
                cv.put(API_URL, article.getApiUrl());
                cv.put(THUMBNAIL_URL, article.getThumbnailUrl());
                cv.put(IS_PINNED, article.isPinned());
                cv.put(CREATTION_DATE, article.getCreationDate().getTime());
                cv.put(IMAGE, article.getImageBitmap());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public static void deleteTable() throws SQLiteException {
        SQLiteDatabase db = DbHelper.getInstance().getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLE_NAME, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private static Cursor getArticlesCursor(String articleId, Boolean onlyPinned, String... orderByColumns) throws SQLiteException {
        SQLiteDatabase db = DbHelper.getInstance().getReadableDatabase();
        String sql = "select "
                + ID                    + " , "
                + SECTION_ID            + " , "
                + SECTION_NAME          + " , "
                + WEB_PUBLICATION_DATE  + " , "
                + WEB_TITLE             + " , "
                + WEB_URL               + " , "
                + API_URL               + " , "
                + THUMBNAIL_URL         + " , "
                + IS_PINNED             + " , "
                + CREATTION_DATE        + " , "
                + IMAGE                 + " , "
                + "rowid as _id "
                + " from "
                + TABLE_NAME;

        ArrayList<String> args = new ArrayList<>();
        String whereAnd = " where ";
        if (articleId != null) {
            sql += whereAnd + ID + "=?";
            args.add(articleId);
            whereAnd = " and ";
        }

        if (onlyPinned != null) {
            sql += whereAnd + IS_PINNED + "=?";
            args.add(onlyPinned ? "1" : "0");
            whereAnd = " and ";
        }

        if (orderByColumns.length > 0) {
            sql = sql + " order by " + orderByColumns[0];
            for (int i = 1; i < orderByColumns.length; i++) {
                sql = sql + "," + orderByColumns[i];
            }
        }
        Cursor cursor = db.rawQuery(sql, args.toArray(new String[]{}));
        return cursor;
    }

    public static List<Article> getArticles(Boolean onlyPinned) throws SQLiteException {
        List<Article> itemAvailableRems = new ArrayList<>();
        Cursor cursor = getArticlesCursor(null, onlyPinned, CREATTION_DATE + " asc ");
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();
                article.setId(cursor.getString(cursor.getColumnIndex(ID)));
                article.setSectionId(cursor.getString(cursor.getColumnIndex(SECTION_ID)));
                article.setSectionName(cursor.getString(cursor.getColumnIndex(SECTION_NAME)));
                article.setWebPublicationDate(new Date(cursor.getLong(cursor.getColumnIndex(WEB_PUBLICATION_DATE))));
                article.setWebTitle(cursor.getString(cursor.getColumnIndex(WEB_TITLE)));
                article.setWebUrl(cursor.getString(cursor.getColumnIndex(WEB_URL)));
                article.setApiUrl(cursor.getString(cursor.getColumnIndex(API_URL)));
                article.setThumbnailUrl(cursor.getString(cursor.getColumnIndex(THUMBNAIL_URL)));
                article.setPinned(cursor.getInt(cursor.getColumnIndex(THUMBNAIL_URL)) != 0);
                article.setImageBitmap(cursor.getBlob(cursor.getColumnIndex(IMAGE)));
                itemAvailableRems.add(article);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemAvailableRems;
    }

    public static boolean exists(String articleId){
        Cursor cursor = getArticlesCursor(articleId, null);
        return cursor.moveToFirst();
    }

}
