package gevorgyan.vahan.newsfeed.remote.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

public final class ImageLoader {

    private ImageLoader() {
    }

    public static void load(Context context, ImageView imageView, String url, Drawable placeholder) {
        load(Glide.with(context), imageView, url, placeholder);
    }

    private static void load(RequestManager requestManager, ImageView imageView, String url, Drawable placeholder) {
        requestManager
                .load(url)
                .into(imageView);
    }

}
