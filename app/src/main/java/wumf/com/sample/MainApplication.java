package wumf.com.sample;

import android.app.Application;
import android.os.Handler;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.appsprovider.AppProvider;
import wumf.com.appsprovider.OnChangeLastInstalledAppsListener;
import wumf.com.sample.eventbus.ChangeAppsEvent;

/**
 * Created by max on 02.09.16.
 */
public class MainApplication extends Application {

    public List<App> appsList;

    public void onCreate() {
        super.onCreate();
        final AppProvider appProvider = AppProvider.instance.setContext(this)
                .setMyAppPackageName(getPackageName())
                .setListener(new OnChangeLastInstalledAppsListener(6) {
                    @Override
                    public void change(List<App> apps) {
                        appsList = apps;
                        EventBus.getDefault().post(new ChangeAppsEvent(apps));
                    }
                });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                appProvider.initBaseInfo();
            }
        });
    }

}
