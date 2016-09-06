package wumf.com.appsprovider;

import android.content.pm.ResolveInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by max on 02.09.16.
 */
public abstract class OnChangeLastInstalledAppsListener {

    public final int appsCount;
    private Map<String, ResolveInfo> map;

    public OnChangeLastInstalledAppsListener(int appsCount) {
        this.appsCount = appsCount;
    }

    void setMap(Map<String, ResolveInfo> map) {
        this.map = map;
    }

    Map<String, ResolveInfo> getMap() {
        return map;
    }

    public abstract void change(List<App> apps);

}
