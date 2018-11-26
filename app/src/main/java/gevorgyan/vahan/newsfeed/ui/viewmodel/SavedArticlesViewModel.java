package gevorgyan.vahan.newsfeed.ui.viewmodel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import gevorgyan.vahan.newsfeed.data.dao.ArticleDao;
import gevorgyan.vahan.newsfeed.domain.model.Article;

public class SavedArticlesViewModel extends ViewModel {
    private MutableLiveData<List<Article>> articlesObservable;

    public SavedArticlesViewModel() {
        this.articlesObservable = new MutableLiveData<>();
    }

    public LiveData<List<Article>> getArticlesObservable() {
        return articlesObservable;
    }

    public void loadData() {
        List<Article> savedArticles = ArticleDao.getArticles(false);
        articlesObservable.postValue(savedArticles);
    }

}
