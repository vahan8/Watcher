package gevorgyan.vahan.newsfeed.ui.saved;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import gevorgyan.vahan.newsfeed.R;
import gevorgyan.vahan.newsfeed.domain.enums.ListViewMode;
import gevorgyan.vahan.newsfeed.domain.model.Article;
import gevorgyan.vahan.newsfeed.ui.ArticleActivity;
import gevorgyan.vahan.newsfeed.ui.adapter.SavedArticlesAdapter;
import gevorgyan.vahan.newsfeed.util.RecyclerViewUtils;

public class SavedArticlesFragment extends Fragment {

    private SavedArticlesViewModel viewModel;
    private SavedArticlesAdapter articlesAdapter;

    private RecyclerView recyclerView;
    private TextView emptyView;

    public static SavedArticlesFragment newInstance() {
        return new SavedArticlesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_articles, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        emptyView = view.findViewById(R.id.textview_empty);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SavedArticlesViewModel.class);


        viewModel.getArticlesObservable().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                articlesAdapter.swap(articles);
                if (articles.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.loadData();

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        RecyclerViewUtils.setLayoutManager(requireActivity(), recyclerView, ListViewMode.LIST);
        articlesAdapter = new SavedArticlesAdapter(requireActivity(), new ArrayList<Article>(), ListViewMode.LIST, recyclerView);
        recyclerView.setAdapter(articlesAdapter);

        articlesAdapter.setItemClickListener(new SavedArticlesAdapter.ItemsClickListener() {
            @Override
            public void onClick(Article article, final ImageView imageView) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ArticleActivity.KEY_ARTICLE, article);

                Intent intent = new Intent(requireActivity(), ArticleActivity.class);
                intent.putExtras(bundle);

                View v = imageView.getRootView();
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), imageView, getString(R.string.thumbnail));

                startActivity(intent, options.toBundle());
            }
        });

    }

}
