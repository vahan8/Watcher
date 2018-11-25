package gevorgyan.vahan.newsfeed.ui.main;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import gevorgyan.vahan.newsfeed.App;
import gevorgyan.vahan.newsfeed.R;
import gevorgyan.vahan.newsfeed.ui.BaseActivity;
import gevorgyan.vahan.newsfeed.ui.saved.SavedArticlesFragment;
import gevorgyan.vahan.newsfeed.util.JobDispatcher;
import gevorgyan.vahan.newsfeed.util.MyJobScheduler;
import gevorgyan.vahan.newsfeed.util.NotificationUtils;

public class MainActivity extends BaseActivity {

    private int current_navigation_item_id = R.id.navigation_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  setTheme(android.R.style.Theme_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
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

        MyJobScheduler.scheduleJob(App.getContext());

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
