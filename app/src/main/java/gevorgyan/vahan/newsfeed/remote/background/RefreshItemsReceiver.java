package gevorgyan.vahan.newsfeed.remote.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import gevorgyan.vahan.newsfeed.domain.model.Article;

public class RefreshItemsReceiver extends BroadcastReceiver {

    public static final String KEY_ARTICLES = "articles";

    private RefreshItemsReceiverCallbacks callbacks;

    public interface RefreshItemsReceiverCallbacks{
        void refresh(List<Article> articles);
    };

    public RefreshItemsReceiver(RefreshItemsReceiverCallbacks callbacks){
        this.callbacks = callbacks;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        List<Article> articles = (List<Article>) intent.getExtras().getSerializable(KEY_ARTICLES);
        callbacks.refresh(articles);
    }
}
