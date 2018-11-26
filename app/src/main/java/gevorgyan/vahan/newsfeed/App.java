package gevorgyan.vahan.newsfeed;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class App extends Application {
    private static App instance;

    private static boolean visible;

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        visible = true;

        if (BuildConfig.DEBUG) {
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
        }
    }


    public static boolean isVisible() {
        return visible;
    }

    public static void setVisible(boolean value) {
        visible = value;
    }
}
