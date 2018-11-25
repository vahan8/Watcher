package gevorgyan.vahan.newsfeed.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import gevorgyan.vahan.newsfeed.R;
import gevorgyan.vahan.newsfeed.domain.model.Article;
import gevorgyan.vahan.newsfeed.domain.enums.ListViewMode;

public class SavedArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LIST = 0;
    private static final int VIEW_TYPE_MINI_CARD = 1;
    // for laod more
    private static final int VIEW_TYPE_LOADING = 1;

    private LayoutInflater inflater;
    private List<Article> articles;
    private ListViewMode listViewMode;

    private Context context;

    private ItemsClickListener itemClickListener;


    public interface ItemsClickListener {
        void onClick(Article article, ImageView imageView);
    }

    public SavedArticlesAdapter(Context context, List<Article> articles, ListViewMode listViewMode, RecyclerView recyclerView) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.articles = articles;
        this.listViewMode = listViewMode;

    }

    public void setListViewMode(ListViewMode listViewMode) {
        this.listViewMode = listViewMode;
    }


    public void setItemClickListener(ItemsClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void swap(List<Article> articles) {
        this.articles.clear();
        this.articles.addAll(articles);
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
        if (viewType == VIEW_TYPE_MINI_CARD) {
            View v = inflater.inflate(R.layout.minicard_item_articles, parent, false);
            return new CardViewHolder(v);
        } else {
            View v = inflater.inflate(R.layout.list_item_articles, parent, false);
            return new ListViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseViewHolder)
            ((BaseViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }


    private abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewTitle;
        protected TextView textViewCaption;
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
            String caption = context.getString(R.string.caption, article.getSectionName());
            textViewCaption.setText(article.getSectionName());

            Bitmap bm = BitmapFactory.decodeByteArray(article.getImageBitmap(), 0, article.getImageBitmap().length);
            imageViewThumbnail.setImageBitmap(bm);

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
            imageViewThumbnail = itemView.findViewById(R.id.imageview_thumbnail);
        }
    }

    private class CardViewHolder extends BaseViewHolder implements View.OnClickListener {
        private TextView textViewItemPrice;

        public CardViewHolder(View itemView) {
            super(itemView);
//            if (customerId != null)
//                itemView.findViewById(R.id.tablerow_discounted_price).setVisibility(View.VISIBLE);
//            textViewItemPrice = itemView.findViewById(R.id.textview_item_price);
//
//            if (!itemAvailableRemShown)
//                itemView.findViewById(R.id.tablerow_item_rem).setVisibility(View.GONE);
        }

        @Override
        public void initBaseFields() {
            textViewTitle = itemView.findViewById(R.id.textview_title);
            textViewCaption = itemView.findViewById(R.id.textview_caption);
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

}
