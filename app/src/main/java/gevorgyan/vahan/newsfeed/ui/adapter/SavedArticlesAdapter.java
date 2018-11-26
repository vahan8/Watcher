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
import gevorgyan.vahan.newsfeed.domain.enums.ListViewMode;
import gevorgyan.vahan.newsfeed.domain.model.Article;
import gevorgyan.vahan.newsfeed.util.DateUtils;

public class SavedArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LIST = 0;
    private static final int VIEW_TYPE_MINI_CARD = 1;

    private LayoutInflater inflater;
    private List<Article> articles;
    private ListViewMode listViewMode;

    private ItemsClickListener itemClickListener;

    public interface ItemsClickListener {
        void onClick(Article article, ImageView imageView);
    }

    public SavedArticlesAdapter(Context context, List<Article> articles, ListViewMode listViewMode) {
        this.inflater = LayoutInflater.from(context);
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
        protected TextView textViewDate;

        public BaseViewHolder(View itemView) {
            super(itemView);
            initBaseFields();
            itemView.setOnClickListener(this);
        }

        public abstract void initBaseFields();

        //If card and list modes has different fields they should override this function
        void bindData(int position) {
            Article article = articles.get(position);
            textViewTitle.setText(article.getWebTitle());
            textViewCaption.setText(article.getSectionName());
            textViewDate.setText(DateUtils.getFormattedDate(article.getWebPublicationDate()));

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
            textViewDate= itemView.findViewById(R.id.textview_date);
            imageViewThumbnail = itemView.findViewById(R.id.imageview_thumbnail);
        }
    }

    private class CardViewHolder extends BaseViewHolder implements View.OnClickListener {
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
    }

}
