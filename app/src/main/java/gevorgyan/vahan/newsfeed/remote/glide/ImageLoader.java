package gevorgyan.vahan.newsfeed.remote.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import androidx.annotation.NonNull;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageLoader {


//    private static void load(@NonNull GlideRequests glide, ImageView imageView, String url, Drawable placeholder, boolean onlyFromCache) {
//        if (onlyFromCache) {
//            glide.load(url)
//                    .onlyRetrieveFromCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(placeholder)
//                    .into(imageView);
//        } else {
//            glide.load(url)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(placeholder)
//                    .transition(withCrossFade())
//                    .into(imageView);
//        }
//    }

    public static void load(Context context, ImageView imageView, String url, Drawable placeholder) {
        load(Glide.with(context), imageView, url, placeholder);
    }

    private static void load(RequestManager requestManager, ImageView imageView, String url, Drawable placeholder) {
        requestManager
                .load(url)
                //.centerCrop()
               // .diskCacheStrategy(DiskCacheStrategy.ALL)
               // .placeholder(placeholder)
                //.crossFade()
                .into(imageView);

        /* circle
                requestManager
                .load(url)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholder)
                        //.crossFade()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(App.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
         */

    }

}
