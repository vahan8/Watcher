package gevorgyan.vahan.newsfeed.util;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import gevorgyan.vahan.newsfeed.service.RefreshItemsFirebaseJobService;
import gevorgyan.vahan.newsfeed.service.RefreshItemsJobService;
import gevorgyan.vahan.newsfeed.service.SendNotificationsJobService;

public class JobDispatcher {
    private static final String REFRESH_ARTICLES_TAG = "refresh_articles";
    private static final String SEND_NOTIFICATIONS_TAG = "send_notifications";

    private static FirebaseJobDispatcher firebaseJobDispatcher;

    private JobDispatcher() {
    }

    public static void init(Context context) {
        firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
    }

    private static FirebaseJobDispatcher getDispatcher(Context context) {
        if (firebaseJobDispatcher == null)
            init(context);

        return firebaseJobDispatcher;
    }

    public static void scheduleSendNotification(Context context) {
        Job sendNotificationJob = getDispatcher(context).newJobBuilder()
                .setService(SendNotificationsJobService.class)
                .setTag(SEND_NOTIFICATIONS_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(10, 20))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        getDispatcher(context).schedule(sendNotificationJob);
    }

    public static void scheduleRefreshArticles(Context context) {
        Job refreshArticlesJob = getDispatcher(context).newJobBuilder()
                .setService(RefreshItemsFirebaseJobService.class)
                .setTag(REFRESH_ARTICLES_TAG)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(10, 10))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();


        //getDispatcher(context).mustSchedule(refreshArticlesJob);
        getDispatcher(context).schedule(refreshArticlesJob);
    }

    public static void stopSendNotifications(Context context) {
        getDispatcher(context).cancel(SEND_NOTIFICATIONS_TAG);
    }

    public static void stopRefreshArticles(Context context) {
        getDispatcher(context).cancel(REFRESH_ARTICLES_TAG);
    }
}
