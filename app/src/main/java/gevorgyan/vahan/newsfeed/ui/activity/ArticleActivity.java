package gevorgyan.vahan.newsfeed.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import gevorgyan.vahan.newsfeed.R;
import gevorgyan.vahan.newsfeed.data.dao.ArticleDao;
import gevorgyan.vahan.newsfeed.domain.model.Article;
import gevorgyan.vahan.newsfeed.remote.glide.ImageLoader;
import gevorgyan.vahan.newsfeed.ui.activity.BaseActivity;
import gevorgyan.vahan.newsfeed.util.ImageUtils;

public class ArticleActivity extends BaseActivity {

    public static final String KEY_ARTICLE = "KEY_ARTICLE";

    private Article article;

    private ImageView imageViewThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setAllowEnterTransitionOverlap(true);
        setContentView(R.layout.activity_article);

        //Get Article from bundle
        Bundle bundle = getIntent().getExtras();
        article = (Article) bundle.getSerializable(KEY_ARTICLE);

        Article articleFromDb = ArticleDao.getArticle(article.getId());
        if (articleFromDb != null)
            article = articleFromDb;
        Log.e("pinned0", "" + article.isPinned());

        setTitle(article.getSectionName());

        // Handle Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textViewTitle = findViewById(R.id.textview_title);
        textViewTitle.setText(article.getWebTitle());
        imageViewThumbnail = findViewById(R.id.imageview_thumbnail);
        ImageLoader.load(this, imageViewThumbnail, article.getThumbnailUrl(), null);

        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        // can be checked if no network connection than use cache else network
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(article.getWebUrl());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_article, menu);
        Log.e("pinned", "" + article.isPinned());
        if (article.isPinned()) {
            MenuItem menuItemPin = menu.findItem(R.id.menu_pin);
            menuItemPin.setTitle(R.string.unpin);
            menuItemPin.setIcon(R.drawable.ic_bookmark_border_white_36dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_save:
                article.setPinned(false);
                checkAndSave();
                finish();
                return true;
            case R.id.menu_pin:
                if (!article.isPinned()) {
                    article.setPinned(true);
                    checkAndSave();
                } else {
                    ArticleDao.delete(article.getId());
                }
                setResult(RESULT_OK);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkAndSave() {
        if (!ArticleDao.exists(article.getId()))
            save();
    }

    private void save() {
        Bitmap bm = null;
        if (imageViewThumbnail.getDrawable() != null)
            bm = ((BitmapDrawable) imageViewThumbnail.getDrawable()).getBitmap();

        if (bm != null && article.getImageBitmap() == null) {
            byte[] data = ImageUtils.getBitmapAsByteArray(bm);
            article.setImageBitmap(data);
        }
        article.setCreationDate(Calendar.getInstance().getTime());

        ArticleDao.insert(article);
    }

}
