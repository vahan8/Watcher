package gevorgyan.vahan.newsfeed.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gevorgyan.vahan.newsfeed.R;
import gevorgyan.vahan.newsfeed.domain.model.Article;
import gevorgyan.vahan.newsfeed.domain.enums.ListViewMode;
import gevorgyan.vahan.newsfeed.remote.glide.ImageLoader;
import gevorgyan.vahan.newsfeed.util.DateUtils;

public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LIST = 0;
    private static final int VIEW_TYPE_MINI_CARD = 1;
    // for laod more
    private static final int VIEW_TYPE_LOADING = 2;

    private LayoutInflater inflater;
    private List<Article> articles;
    private ListViewMode listViewMode;

    private Context context;

    private OnLoadMoreListener onLoadMoreListener;
    private ItemsClickListener itemClickListener;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface ItemsClickListener {
        void onClick(Article article, ImageView imageView);
    }

    public ArticlesAdapter(Context context, List<Article> articles, ListViewMode listViewMode, RecyclerView recyclerView) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.articles = articles;
        this.listViewMode = listViewMode;

        // load more
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setListViewMode(ListViewMode listViewMode) {
        this.listViewMode = listViewMode;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setItemClickListener(ItemsClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void swap(List<Article> articles) {
        this.articles.clear();
        this.articles.addAll(articles);
        this.articles.add(null);
        setLoaded();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (articles.get(position) == null)
            return VIEW_TYPE_LOADING;

        switch (listViewMode) {
            case MINI_CARD:
                return VIEW_TYPE_MINI_CARD;
            case LIST:
                return VIEW_TYPE_LIST;
        }
        return VIEW_TYPE_LIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View v = inflater.inflate(R.layout.item_progressbar, parent, false);
            return new ViewHolderLoading(v);
        } else if (viewType == VIEW_TYPE_LIST) {
            View v = inflater.inflate(R.layout.list_item_articles, parent, false);
            return new ListViewHolder(v);
        } else if (viewType == VIEW_TYPE_MINI_CARD) {
            View v = inflater.inflate(R.layout.minicard_item_articles, parent, false);
            return new CardViewHolder(v);
        } else {
            View v = inflater.inflate(R.layout.item_progressbar, parent, false);
            return new ViewHolderLoading(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder) holder).bindData(position);
        } else if (holder instanceof ViewHolderLoading) {
            ((ViewHolderLoading) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    private abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewTitle;
        protected TextView textViewCaption;
        protected TextView textViewDate;
        protected ImageView imageViewThumbnail;

        public BaseViewHolder(View itemView) {
            super(itemView);
            initBaseFields();

            itemView.setOnClickListener(this);
        }

        public abstract void initBaseFields();

        public void bindData(int position) {
            Article article = articles.get(position);
            textViewTitle.setText(article.getWebTitle());
            textViewDate.setText(DateUtils.getFormattedDate(article.getWebPublicationDate()));
            textViewCaption.setText(article.getSectionName());
            ImageLoader.load(itemView.getContext(), imageViewThumbnail, article.getThumbnailUrl(), null);
        }

        @Override
        public void onClick(View v) {
            Article article = articles.get(getLayoutPosition());
            itemClickListener.onClick(article, imageViewThumbnail);
        }
    }


    private class ListViewHolder extends BaseViewHolder implements View.OnClickListener {
        public ListViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initBaseFields() {
            textViewTitle = itemView.findViewById(R.id.textview_title);
            textViewCaption = itemView.findViewById(R.id.textview_caption);
            textViewDate= itemView.findViewById(R.id.textview_date);
            imageViewThumbnail = itemView.findViewById(R.id.imageview_thumbnail);
        }
    }

    private class CardViewHolder extends BaseViewHolder implements View.OnClickListener {
        private TextView textViewItemPrice;

        public CardViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initBaseFields() {
            textViewTitle = itemView.findViewById(R.id.textview_title);
            textViewCaption = itemView.findViewById(R.id.textview_caption);
            textViewDate= itemView.findViewById(R.id.textview_date);
            imageViewThumbnail = itemView.findViewById(R.id.imageview_thumbnail);

        }
//
//        @Override
//        public void bindData(int position) {
//            super.bindData(position);
//
//            }
//        }
    }

    private class ViewHolderLoading extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ViewHolderLoading(View view) {
            super(view);
            progressBar = view.findViewById(R.id.itemProgressbar);
        }
    }

}
