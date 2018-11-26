package gevorgyan.vahan.newsfeed.ui.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gevorgyan.vahan.newsfeed.R;
import gevorgyan.vahan.newsfeed.domain.enums.ListViewMode;
import gevorgyan.vahan.newsfeed.domain.model.Article;
import gevorgyan.vahan.newsfeed.ui.activity.ArticleActivity;
import gevorgyan.vahan.newsfeed.ui.adapter.ArticlesAdapter;
import gevorgyan.vahan.newsfeed.ui.adapter.PinnedArticlesAdapter;
import gevorgyan.vahan.newsfeed.ui.viewmodel.MainViewModel;
import gevorgyan.vahan.newsfeed.ui.viewmodel.PinnedArticlesViewModel;
import gevorgyan.vahan.newsfeed.util.RecyclerViewUtils;

public class MainFragment extends Fragment {

    private MainViewModel viewModel;
    private PinnedArticlesViewModel pinnedArticlesViewModel;
    private ArticlesAdapter articlesAdapter;
    private PinnedArticlesAdapter pinnedArticlesAdapter;

    private RecyclerView recyclerView;
    private RecyclerView pinnedArticlesRecyclerView;
    private LinearLayout pinnedArticlesLayout;
    private LinearLayout pinnedArticlesTitleLayout;
    private ImageView imageViewShowHide;
    private TextView emptyView;

    private boolean showFavorites = true;

    public static final int ACTIVITY_ARTICLE = 1;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        pinnedArticlesRecyclerView = view.findViewById(R.id.recyclerview_pinned_articles);
        pinnedArticlesLayout = view.findViewById(R.id.layout_pinned_articles);
        pinnedArticlesTitleLayout = view.findViewById(R.id.layout_pinned_articles_title);
        imageViewShowHide = view.findViewById(R.id.imageview_show_hide);
        emptyView = view.findViewById(R.id.textview_empty);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getArticlesObservable().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                articlesAdapter.swap(articles);
                setEmptyViewVisibility();
                Log.e("adapter_articles", "a" + articles.size());
            }
        });
        viewModel.loadData(1);

        pinnedArticlesViewModel = ViewModelProviders.of(this).get(PinnedArticlesViewModel.class);
        pinnedArticlesViewModel.getArticlesObservable().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                pinnedArticlesAdapter.swap(articles);
                if (articles.size() == 0)
                    pinnedArticlesLayout.setVisibility(View.GONE);
                else
                    pinnedArticlesLayout.setVisibility(View.VISIBLE);
            }
        });
        pinnedArticlesViewModel.loadData();

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        RecyclerViewUtils.setLayoutManager(requireActivity(), recyclerView, ListViewMode.LIST);
        articlesAdapter = new ArticlesAdapter(requireActivity(), new ArrayList<Article>(), recyclerView);
        recyclerView.setAdapter(articlesAdapter);
        articlesAdapter.setOnLoadMoreListener(new ArticlesAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                List<Article> articles = viewModel.getArticlesObservable().getValue();
                viewModel.loadData(viewModel.getLastResponsePage() + 1);
            }
        });
        articlesAdapter.setItemClickListener(new ArticlesAdapter.ItemsClickListener() {
            @Override
            public void onClick(Article article, ImageView imageView) {
                openArticle(article, imageView);
            }
        });


        itemAnimator = new DefaultItemAnimator();
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        pinnedArticlesRecyclerView.setItemAnimator(itemAnimator);
        pinnedArticlesRecyclerView.setLayoutManager(layoutManager);
        pinnedArticlesRecyclerView.setHorizontalScrollBarEnabled(true);

        pinnedArticlesAdapter = new PinnedArticlesAdapter(requireActivity(), new ArrayList<Article>());
        pinnedArticlesAdapter.setItemClickListener(new PinnedArticlesAdapter.ItemsClickListener() {
            @Override
            public void onClick(Article article, ImageView imageView) {
                openArticle(article, imageView);
            }
        });
        pinnedArticlesRecyclerView.setAdapter(pinnedArticlesAdapter);

        pinnedArticlesTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showFavorites) {
                    showFavorites = false;
                    pinnedArticlesRecyclerView.setVisibility(View.GONE);
                    imageViewShowHide.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_grey_600_24dp));
                } else {
                    showFavorites = true;
                    pinnedArticlesRecyclerView.setVisibility(View.VISIBLE);
                    imageViewShowHide.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_grey_600_24dp));
                }
            }
        });
        setEmptyViewVisibility();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_ARTICLE:
                if (resultCode == Activity.RESULT_OK) {
                    pinnedArticlesViewModel.loadData();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void openArticle(Article article, final ImageView imageView) {
        article.setImageBitmap(null);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ArticleActivity.KEY_ARTICLE, article);
        Intent intent = new Intent(requireActivity(), ArticleActivity.class);
        intent.putExtras(bundle);

        // View v = imageView.getRootView();
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), imageView, getString(R.string.thumbnail));
        startActivityForResult(intent, ACTIVITY_ARTICLE, options.toBundle());

        //startActivityForResult(intent, ACTIVITY_ARTICLE);
    }

    private void setEmptyViewVisibility() {
        if (articlesAdapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
