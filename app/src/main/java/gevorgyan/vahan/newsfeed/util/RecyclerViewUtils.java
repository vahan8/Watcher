package gevorgyan.vahan.newsfeed.util;

import android.app.Activity;
import android.util.DisplayMetrics;

import androidx.annotation.DimenRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gevorgyan.vahan.newsfeed.R;
import gevorgyan.vahan.newsfeed.domain.enums.ListViewMode;

public class RecyclerViewUtils {

    public static void setLayoutManager(Activity activity, RecyclerView recyclerView, ListViewMode listViewMode) {
        // there are some cases when recyclerview can be null, because the view isn't initialized yet (e.g. in onConfigurationChanged method, on first open)
        if (activity == null || recyclerView == null)
            return;

        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        LinearLayoutManager linearLayoutManager = (recyclerView.getLayoutManager() != null) ? (LinearLayoutManager) recyclerView.getLayoutManager() : null;
        if (linearLayoutManager != null) {
            scrollPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (scrollPosition == RecyclerView.NO_POSITION) {
                scrollPosition = linearLayoutManager.findFirstVisibleItemPosition();
            }
        }

        RecyclerView.LayoutManager layoutManager;
        switch (listViewMode) {
            case LIST:
                layoutManager = new LinearLayoutManager(activity);
                break;
            case MINI_CARD:
                layoutManager = new GridLayoutManager(activity, getSpanCount(activity, R.dimen.mini_card_min_width));
                break;
            default:
                layoutManager = new LinearLayoutManager(activity);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    private static int getSpanCount(Activity activity, @DimenRes int itemMinWidth) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int spanCount = metrics.widthPixels / activity.getResources().getDimensionPixelSize(itemMinWidth);
        return (spanCount <= 0 ? 1 : spanCount);
    }

}
