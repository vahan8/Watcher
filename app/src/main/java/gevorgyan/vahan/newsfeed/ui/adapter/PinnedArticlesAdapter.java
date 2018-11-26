package gevorgyan.vahan.newsfeed.ui.adapter;

import android.content.Context;
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
import gevorgyan.vahan.newsfeed.remote.glide.ImageLoader;

public class PinnedArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<Article> articles;

    private ItemsClickListener itemClickListener;

    public interface ItemsClickListener {
        void onClick(Article article, ImageView imageView);
    }

    public PinnedArticlesAdapter(Context context, List<Article> articles) {
        this.inflater = LayoutInflater.from(context);
        this.articles = articles;

    }

    public void setItemClickListener(ItemsClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void swap(List<Article> articles) {
        this.articles.clear();
        this.articles.addAll(articles);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_item_pinned_articles, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder)
            ((ViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewTitle;
        TextView textViewCaption;
        private ImageView imageViewThumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textview_title);
            textViewCaption = itemView.findViewById(R.id.textview_caption);
            imageViewThumbnail = itemView.findViewById(R.id.imageview_pinned_thumbnail);

            itemView.setOnClickListener(this);
        }


        void bindData(int position) {
            Article article = articles.get(position);
            textViewTitle.setText(article.getWebTitle());
            ImageLoader.load(itemView.getContext(), imageViewThumbnail, article.getThumbnailUrl(), null);
        }

        @Override
        public void onClick(View v) {
            Article article = articles.get(getLayoutPosition());
            itemClickListener.onClick(article, imageViewThumbnail);
        }
    }

}
