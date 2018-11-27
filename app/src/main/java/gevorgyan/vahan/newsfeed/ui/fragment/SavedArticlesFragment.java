package gevorgyan.vahan.newsfeed.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import gevorgyan.vahan.newsfeed.ui.activity.ArticleActivity;
import gevorgyan.vahan.newsfeed.ui.adapter.SavedArticlesAdapter;
import gevorgyan.vahan.newsfeed.ui.viewmodel.SavedArticlesViewModel;
import gevorgyan.vahan.newsfeed.util.RecyclerViewUtils;

public class SavedArticlesFragment extends Fragment {

    private SavedArticlesViewModel viewModel;
    private SavedArticlesAdapter adapter;

    private RecyclerView recyclerView;
    private TextView emptyView;

    private ListViewMode listViewMode;
    private MenuItem menuItemListLayoutMode;

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

        listViewMode = ListViewMode.LIST;

        viewModel = ViewModelProviders.of(this).get(SavedArticlesViewModel.class);
        viewModel.getArticlesObservable().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                adapter.swap(articles);
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
        adapter = new SavedArticlesAdapter(requireActivity(), new ArrayList<Article>(), ListViewMode.LIST);
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListener(new SavedArticlesAdapter.ItemsClickListener() {
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

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_saved_articles, menu);
        menuItemListLayoutMode = menu.findItem(R.id.menu_list_layout_mode);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_list_layout_mode:
                switch (listViewMode) {
                    case MINI_CARD:
                        listViewMode = ListViewMode.LIST;
                        adapter.setListViewMode(ListViewMode.LIST);
                        RecyclerViewUtils.setLayoutManager(requireActivity(), recyclerView, ListViewMode.LIST);
                        menuItemListLayoutMode.setIcon(R.drawable.ic_view_agenda_white_36dp);
                        menuItemListLayoutMode.setTitle(R.string.view_mode_list);
                        break;
                    case LIST:
                        listViewMode = ListViewMode.MINI_CARD;
                        adapter.setListViewMode(ListViewMode.MINI_CARD);
                        RecyclerViewUtils.setLayoutManager(requireActivity(), recyclerView, ListViewMode.MINI_CARD);
                        menuItemListLayoutMode.setIcon(R.drawable.ic_view_list_white_36dp);
                        menuItemListLayoutMode.setTitle(R.string.view_mode_mini_card);
                        break;
                    default:
                        break;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
