package gevorgyan.vahan.newsfeed.ui.main;

import android.content.IntentFilter;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import gevorgyan.vahan.newsfeed.App;
import gevorgyan.vahan.newsfeed.domain.model.Article;
import gevorgyan.vahan.newsfeed.domain.model.SearchQueryResponse;
import gevorgyan.vahan.newsfeed.remote.Api;
import gevorgyan.vahan.newsfeed.remote.RequestCallbacks;
import gevorgyan.vahan.newsfeed.util.Constants;
import gevorgyan.vahan.newsfeed.util.RefreshItemsReceiver;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<Article>> articlesObservable;
    private SearchRequestCallbacks callbacks;

    private int lastResponsePage = 1;

    private class SearchRequestCallbacks implements RequestCallbacks{
        @Override
        public void onSuccess(Object response) {
            SearchQueryResponse searchQueryResponse = (SearchQueryResponse) response;
            lastResponsePage = ((SearchQueryResponse) response).getResponse().getCurrentPage();
            List<Article> articles = articlesObservable.getValue() == null ? new ArrayList<Article>() : articlesObservable.getValue();
            articles.addAll(searchQueryResponse.getResponse().getArticles());
            articlesObservable.postValue(articles);
        }

        @Override
        public void onFailure() {

        }
    }

    public MainViewModel() {
        this.articlesObservable = new MutableLiveData<>();
        this.callbacks = new SearchRequestCallbacks();

        RefreshItemsReceiver receiver = new RefreshItemsReceiver(new RefreshItemsReceiver.RefreshItemsReceiverCallbacks() {
            @Override
            public void refresh(List<Article> articles) {
                Log.e("refresh", "bebebe");
                articlesObservable.postValue(articles);
            }
        });
        IntentFilter statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

        LocalBroadcastManager.getInstance(App.getContext()).registerReceiver(receiver, statusIntentFilter);
    }


    public LiveData<List<Article>> getArticlesObservable() {
        return articlesObservable;
    }

    public SearchRequestCallbacks getCallbacks() {
        return callbacks;
    }

    public int getLastResponsePage() {
        return lastResponsePage;
    }

    public void loadData(int page) {
        Api.downloadArticles(page, callbacks);
    }

    public void refresh() {
        articlesObservable.postValue(new ArrayList<Article>());
        Api.downloadArticles(1, callbacks);
    }


}
