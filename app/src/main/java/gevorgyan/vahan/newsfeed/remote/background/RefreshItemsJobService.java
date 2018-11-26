package gevorgyan.vahan.newsfeed.remote.background;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.io.Serializable;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import gevorgyan.vahan.newsfeed.App;
import gevorgyan.vahan.newsfeed.data.pref.NewsFeedPrefManager;
import gevorgyan.vahan.newsfeed.domain.model.SearchQuery;
import gevorgyan.vahan.newsfeed.domain.model.SearchQueryResponse;
import gevorgyan.vahan.newsfeed.remote.Api;
import gevorgyan.vahan.newsfeed.remote.RequestCallbacks;
import gevorgyan.vahan.newsfeed.util.Constants;
import gevorgyan.vahan.newsfeed.util.NotificationUtils;

public class RefreshItemsJobService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters job) {
        Log.e("job_refresh", "started");
        RequestCallbacks callbacks = new RequestCallbacks() {
            @Override
            public void onSuccess(Object response) {
                SearchQuery result = ((SearchQueryResponse) response).getResponse();
                Log.e("total", "t" + result.getTotal());
                String firstArticleId = result.getSearchArticles().get(0).getId();
                if (!firstArticleId.equals(NewsFeedPrefManager.getNewestArticleId())) {
                    NewsFeedPrefManager.setNewestArticleId(firstArticleId);
                    if (App.isVisible()) {
                        Intent localIntent = new Intent(Constants.BROADCAST_ACTION)
                                //.putExtra(Constants.SHOULD_REFRESH, true)
                         .putExtra(RefreshItemsReceiver.KEY_ARTICLES, (Serializable) result.getArticles());
                        LocalBroadcastManager.getInstance(RefreshItemsJobService.this).sendBroadcast(localIntent);
                    } else {
                        NotificationUtils.sendNotification(App.getContext(), "Watcher", result.getSearchArticles().get(0).getWebTitle());
                    }

                }
                //Reschedule the Service before calling job finished
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    NewsfeedJobScheduler.scheduleJRefreshItemsJob(App.getContext());
                jobFinished(job, false);
            }

            @Override
            public void onFailure() {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    NewsfeedJobScheduler.scheduleJRefreshItemsJob(App.getContext());
                jobFinished(job, false);
            }
        };
        Api.downloadArticles(1, callbacks);
        return true;
    }

    @Override
    public boolean onStopJob(android.app.job.JobParameters job) {
        return false;
    }

}
