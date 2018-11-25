package gevorgyan.vahan.newsfeed.service;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import gevorgyan.vahan.newsfeed.data.pref.NewsFeedPrefManager;
import gevorgyan.vahan.newsfeed.domain.model.SearchQuery;
import gevorgyan.vahan.newsfeed.domain.model.SearchQueryResponse;
import gevorgyan.vahan.newsfeed.remote.Api;
import gevorgyan.vahan.newsfeed.remote.RequestCallbacks;

public class RefreshItemsFirebaseJobService extends JobService {

    public final static String KEY_CALLBACKS = "callbacks";


    @Override
    public boolean onStartJob(final JobParameters job) {
        Log.e("job_refresh", "started");
        RequestCallbacks callbacks = new RequestCallbacks() {
                @Override
                public void onSuccess(Object response) {
                    Log.e("job_refresh", "visible");
                    SearchQuery result = ((SearchQueryResponse) response).getResponse();
                   // Log.e("status", "t" + result.getStatus());
                    Log.e("total", "t" + result.getTotal());
                    String firstArticleId = result.getSearchArticles().get(0).getId();
                    if (!firstArticleId.equals(NewsFeedPrefManager.getNewestArticleId())) {
                        NewsFeedPrefManager.setNewestArticleId(firstArticleId);
//                        if(App.isVisible()){
//                            Intent localIntent = new Intent(Constants.BROADCAST_ACTION)
//                                    .putExtra(Constants.SHOULD_REFRESH, true);
//                            LocalBroadcastManager.getInstance(RefreshItemsJobService.this).sendBroadcast(localIntent);
//                        }else{
//                            NotificationUtils.sendNotification(App.getContext(), "Newsfeed", "New Article is available");
//                        }

                    }
                   jobFinished(job, false);
                }

                @Override
                public void onFailure() {
                    Log.e("job_refresh", "failure");
                }
            };
        // Api.checkForNewArticles(callbacks);
        Api.downloadArticles(1, callbacks);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

}
