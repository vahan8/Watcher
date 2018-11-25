package gevorgyan.vahan.newsfeed.data.pref;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import gevorgyan.vahan.newsfeed.App;

public final class NewsFeedPrefManager {
    private static final String KEY_NEWEST_ARTICLE_ID = "KEY_NEWEST_ARTICLE_ID";

    private NewsFeedPrefManager() {
    }

    public static String getNewestArticleId() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        return sharedPreferences.getString(KEY_NEWEST_ARTICLE_ID, "");
    }

    public static void setNewestArticleId(String articleId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NEWEST_ARTICLE_ID, articleId);
        editor.commit();
    }

}
