package gevorgyan.vahan.newsfeed.ui;

import androidx.appcompat.app.AppCompatActivity;
import gevorgyan.vahan.newsfeed.App;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();
        App.setVisible(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.setVisible(true);
    }
}
