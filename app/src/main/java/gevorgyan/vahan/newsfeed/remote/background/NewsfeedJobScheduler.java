package gevorgyan.vahan.newsfeed.remote.background;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

public final class NewsfeedJobScheduler {
    private NewsfeedJobScheduler() {
    }


    public static void scheduleJRefreshItemsJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, RefreshItemsJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(10 * 1000);
            builder.setOverrideDeadline(10 * 1000);
        } else {
            builder.setPeriodic(10 * 1000);
        }

        // builder.setBackoffCriteria(10*1000, JobInfo.BACKOFF_POLICY_LINEAR);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresCharging(false);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
}
