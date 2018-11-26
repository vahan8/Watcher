package gevorgyan.vahan.newsfeed.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import androidx.appcompat.widget.Toolbar;
import gevorgyan.vahan.newsfeed.R;
import gevorgyan.vahan.newsfeed.data.dao.ArticleDao;
import gevorgyan.vahan.newsfeed.domain.model.Article;
import gevorgyan.vahan.newsfeed.remote.glide.ImageLoader;
import gevorgyan.vahan.newsfeed.util.ImageUtils;

public class ArticleActivity extends BaseActivity {

    public static final String KEY_ARTICLE = "KEY_ARTICLE";

    private Article article;

    private ImageView imageViewThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        //Get Article from bundle
        Bundle bundle = getIntent().getExtras();
        article = (Article) bundle.getSerializable(KEY_ARTICLE);

        //Get article from db if it is saved
        Article articleFromDb = ArticleDao.getArticle(article.getId());
        if (articleFromDb != null)
            article = articleFromDb;

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
                save();
                finish();
                return true;
            case R.id.menu_pin:
                pin();
                setResult(RESULT_OK);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        article.setSaved(true);
        prepareForSave();
        if (!ArticleDao.exists(article.getId())) {
            article.setPinned(false);
            ArticleDao.insert(article);
        } else {
            ArticleDao.update(article);
        }
    }

    private void pin() {
        if (!ArticleDao.exists(article.getId())) {
            article.setPinned(true);
            article.setSaved(false);
            prepareForSave();
            ArticleDao.insert(article);
            return;
        }

        if (!article.isPinned()) {
            article.setPinned(true);
            prepareForSave();
            ArticleDao.update(article);
        } else {
            if (article.isSaved())
                ArticleDao.update(article);
            else
                ArticleDao.delete(article.getId());
        }
    }

    private void prepareForSave() {
        byte[] data = ImageUtils.getByteArrayFromImageView(imageViewThumbnail);
        if (data != null) {
            article.setImageBitmap(data);
        }
        article.setCreationDate(Calendar.getInstance().getTime());
    }

}
