package gevorgyan.vahan.newsfeed.domain;

import android.content.Context;

import java.util.List;

import androidx.lifecycle.LiveData;
import gevorgyan.vahan.newsfeed.domain.model.Article;

public class ArticlesLiveData extends LiveData<List<Article>> {
    private Context context;

    public ArticlesLiveData(Context context){
        this.context = context;
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }
}
