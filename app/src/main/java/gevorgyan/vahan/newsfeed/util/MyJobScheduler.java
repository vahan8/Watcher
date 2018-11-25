package gevorgyan.vahan.newsfeed.util;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import gevorgyan.vahan.newsfeed.remote.RequestCallbacks;
import gevorgyan.vahan.newsfeed.service.RefreshItemsJobIntentService;
import gevorgyan.vahan.newsfeed.service.RefreshItemsJobService;
import gevorgyan.vahan.newsfeed.service.SendNotificationsJobService;

public class MyJobScheduler {

    private static final String REFRESH_ARTICLES_TAG = "refresh_articles";
    private static final String SEND_NOTIFICATIONS_TAG = "send_notifications";

    private static JobScheduler jobScheduler;

    private static int count = 0;

    private MyJobScheduler() {
    }

    public static void init(Context context) {
      //  jobScheduler = new ;
    }

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, RefreshItemsJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(count++, serviceComponent);
       // builder.setMinimumLatency(1 * 1000); // wait at least
     //   builder.setOverrideDeadline(3 * 1000); // maximum delay
      //  builder.setPeriodic(10*1000);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
             builder.setMinimumLatency(10*1000);
            builder.setOverrideDeadline(10 * 1000);
          //  builder.setPeriodic(10*1000, 0);
        } else {
            builder.setPeriodic(10*1000);
        }


       // builder.setBackoffCriteria(10*1000, JobInfo.BACKOFF_POLICY_LINEAR);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }


}
