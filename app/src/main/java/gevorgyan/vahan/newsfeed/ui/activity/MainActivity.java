package gevorgyan.vahan.newsfeed.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import gevorgyan.vahan.newsfeed.App;
import gevorgyan.vahan.newsfeed.R;
import gevorgyan.vahan.newsfeed.ui.fragment.MainFragment;
import gevorgyan.vahan.newsfeed.ui.fragment.SavedArticlesFragment;
import gevorgyan.vahan.newsfeed.remote.background.NewsfeedJobScheduler;
import gevorgyan.vahan.newsfeed.util.NotificationUtils;

public class MainActivity extends BaseActivity {

    private int current_navigation_item_id = R.id.navigation_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance()).commitNow();
        }

        // Notifications
        NotificationUtils.createNewsfeedNotificationChannel();

        //Schedule job
        //     JobDispatcher.stopRefreshArticles(App.getContext());
        //     JobDispatcher.stopSendNotifications(App.getContext());
        //   JobDispatcher.scheduleRefreshArticles(App.getContext());
        //   JobDispatcher.scheduleSendNotification(App.getContext());

        NewsfeedJobScheduler.scheduleJRefreshItemsJob(App.getContext());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() != current_navigation_item_id) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            current_navigation_item_id = R.id.navigation_home;
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance()).commitNow();
                            return true;
                        case R.id.navigation_saved:
                            current_navigation_item_id = R.id.navigation_saved;
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, SavedArticlesFragment.newInstance()).commitNow();
                            return true;
                    }
                }
                current_navigation_item_id = item.getItemId();
                return false;

            }
        });

    }
}
