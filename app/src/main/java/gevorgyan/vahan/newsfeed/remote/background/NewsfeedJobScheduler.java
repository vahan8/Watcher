package gevorgyan.vahan.newsfeed.remote.background;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

public final class NewsfeedJobScheduler {
    private NewsfeedJobScheduler() {
    }

    private static final int JOB_PERIOD = 30 * 1000;

    public static void scheduleJRefreshItemsJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, RefreshItemsJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);

        //Latest versions have problem with periodic jobs less than 15 minutes interval
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(JOB_PERIOD);
            builder.setOverrideDeadline(JOB_PERIOD);
        } else {
            builder.setPeriodic(JOB_PERIOD);
        }

        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresCharging(false);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
}
