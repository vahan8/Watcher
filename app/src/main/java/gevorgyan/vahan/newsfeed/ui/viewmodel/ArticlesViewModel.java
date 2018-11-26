package gevorgyan.vahan.newsfeed.ui.viewmodel;

import android.content.IntentFilter;

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
import gevorgyan.vahan.newsfeed.remote.background.RefreshItemsReceiver;
import gevorgyan.vahan.newsfeed.util.Constants;

public class ArticlesViewModel extends ViewModel {
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

    public ArticlesViewModel() {
        this.articlesObservable = new MutableLiveData<>();
        this.callbacks = new SearchRequestCallbacks();

        RefreshItemsReceiver receiver = new RefreshItemsReceiver(new RefreshItemsReceiver.RefreshItemsReceiverCallbacks() {
            @Override
            public void refresh(List<Article> articles) {
                lastResponsePage = 1;
                articlesObservable.postValue(articles);
            }
        });
        IntentFilter statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

        LocalBroadcastManager.getInstance(App.getContext()).registerReceiver(receiver, statusIntentFilter);
    }

    public LiveData<List<Article>> getArticlesObservable() {
        return articlesObservable;
    }

    public int getLastResponsePage() {
        return lastResponsePage;
    }

    public void loadData(int page) {
        Api.downloadArticles(page, callbacks);
    }

}
