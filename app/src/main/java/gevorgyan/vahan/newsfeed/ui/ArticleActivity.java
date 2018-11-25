package gevorgyan.vahan.newsfeed.ui;

import androidx.appcompat.widget.Toolbar;
import gevorgyan.vahan.newsfeed.R;
import gevorgyan.vahan.newsfeed.data.dao.ArticleDao;
import gevorgyan.vahan.newsfeed.domain.model.Article;
import gevorgyan.vahan.newsfeed.remote.glide.ImageLoader;
import gevorgyan.vahan.newsfeed.util.ImageUtils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

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

//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<SearchResponse> call = apiInterface.getArticle(article.getApiUrl());
//        call.enqueue(new Callback<SearchResponse>() {
//            @Override
//            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<SearchResponse> call, Throwable t) {
//
//            }
//        });


        //     webView.getSettings().setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB
        webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        //  webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT ); // load online by default

        // if ( !isNetworkAvailable() ) { // loading offline
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(article.getWebUrl());
        // }

        //   webView.loadUrl( "http://www.google.com" );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_article, menu);
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
                save();
                finish();
                return true;
            case R.id.menu_pin:
                article.setPinned(true);
                save();
                setResult(RESULT_OK);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
