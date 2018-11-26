package gevorgyan.vahan.newsfeed.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    public static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static byte[] getByteArrayFromImageView(ImageView imageView) {
        Bitmap bm = null;
        if (imageView.getDrawable() != null)
            bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        byte[] data = null;
        if (bm != null) {
            data = ImageUtils.getByteArrayFromBitmap(bm);
        }
        return data;
    }

}
