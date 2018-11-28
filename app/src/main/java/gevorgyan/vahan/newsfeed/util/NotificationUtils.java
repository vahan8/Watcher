package gevorgyan.vahan.newsfeed.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import gevorgyan.vahan.newsfeed.App;
import gevorgyan.vahan.newsfeed.R;
import gevorgyan.vahan.newsfeed.ui.activity.MainActivity;

public final class NotificationUtils {
    private static final int NEWSFEED_NOTIFICATION_CHANNEL_ID = R.string.notification_channel_id;
    private static final int NEWSFEED_NOTIFICATION_CHANNEL_NAME = R.string.app_name;

    private static final int DEFAULT_NOTIFICATION_CHANNEL_ID = NEWSFEED_NOTIFICATION_CHANNEL_ID;

    private NotificationUtils() {
    }

    /**
     * Create channel to show notifications on Android Oreo
     *
     * @param channelId   The id of the channel
     * @param channelName The user-visible name of the channel
     */
    private static void createNotificationChannel(String channelId, String channelName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Context context = App.getContext();
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void createNewsfeedNotificationChannel() {
        Context context = App.getContext();
        createNotificationChannel(context.getString(NEWSFEED_NOTIFICATION_CHANNEL_ID), context.getString(NEWSFEED_NOTIFICATION_CHANNEL_NAME));
    }

    public static Notification createSimpleNotification(String title, String message, PendingIntent pendingIntent, String channelId) {
        Context context = App.getContext();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_app_notification)
                .setContentTitle(title == null ? context.getString(R.string.app_name) : title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        return notificationBuilder.build();
    }

    public static Notification createSimpleNotification(String title, String message, PendingIntent pendingIntent) {
        Context context = App.getContext();
        return createSimpleNotification(title, message, pendingIntent, context.getString(DEFAULT_NOTIFICATION_CHANNEL_ID));
    }


    public static void sendNotification(Context context, String title, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = NotificationUtils.createSimpleNotification(title, message, pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

}
