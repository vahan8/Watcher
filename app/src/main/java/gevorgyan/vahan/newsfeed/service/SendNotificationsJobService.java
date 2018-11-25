package gevorgyan.vahan.newsfeed.service;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import gevorgyan.vahan.newsfeed.App;
import gevorgyan.vahan.newsfeed.remote.Api;
import gevorgyan.vahan.newsfeed.remote.RequestCallbacks;
import gevorgyan.vahan.newsfeed.util.NotificationUtils;

public class SendNotificationsJobService extends JobService {


    @Override
    public boolean onStartJob(JobParameters job) {
        Log.e("job_send", "started");
        if (true) {

//            Api.checkForNewArticles(new RequestCallbacks() {
//                @Override
//                public void onSuccess(Object response) {
//                    NotificationUtils.sendNotification(App.getContext(), "Newsfeed", "New Article is available");
//                }
//
//                @Override
//                public void onFailure(String errorMessage) {
//
//                }
//            });
            NotificationUtils.sendNotification(App.getContext(), "Newsfeed", "New Article is available");
            return false;
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
}
