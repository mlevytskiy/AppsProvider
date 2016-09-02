package wumf.com.appsprovider;

import java.util.List;

/**
 * Created by max on 02.09.16.
 */
public abstract class OnChangeLastInstalledAppsListener {

    public final int appsCount;

    public OnChangeLastInstalledAppsListener(int appsCount) {
        this.appsCount = appsCount;
    }

    public abstract void change(List<App> apps);

}
