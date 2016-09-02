package wumf.com.appsprovider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by max on 02.09.16.
 */
public class AppProvider {

    public final static AppProvider instance = new AppProvider();

    private OnChangeLastInstalledAppsListener listener;
    private Context context;
    private PackageManager pm;
    private String myAppPN;

    private AppProvider() { }

    public AppProvider setListener(OnChangeLastInstalledAppsListener listener) {
        this.listener = listener;
        return this;
    }

    public AppProvider setMyAppPackageName(String packageName) {
        myAppPN = packageName;
        return this;
    }

    public AppProvider setContext(Context context) {
        this.context = context;
        this.pm = context.getPackageManager();
        return this;
    }

    public AppProvider initBaseInfo() {
        //load all apps without label and icon
        List<ResolveInfo> resolveInfos = getResolveInfos();
        List<App> apps = resolveInfoToApp(resolveInfos);
        List<App> limitedApps = new ArrayList<>();
        for (int i = apps.size()-1; i >= apps.size()-1-listener.appsCount; i--) {
            limitedApps.add(apps.get(i));
        }
        listener.change(limitedApps);
        return this;
    }

    public AppProvider initFullAppInfo(List<App> apps, Runnable finishInit) {
        //load label and icon if need
        return this;
    }

    private List<ResolveInfo> getResolveInfos() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<android.content.pm.ResolveInfo> appList = pm.queryIntentActivities(mainIntent, PackageManager.GET_META_DATA);
        return appList;
    }

    private List<App> resolveInfoToApp(List<ResolveInfo> list) {
        List<App> result = new ArrayList<>();
        long systemInstallDate = -1;
        for (ResolveInfo resolveInfo : list) {
            if (TextUtils.equals(myAppPN, resolveInfo.activityInfo.packageName)) {
                continue; //skip my app
            } else {
                result.add(resolveInfoToApp(resolveInfo));
            }
        }

        systemInstallDate = systemInstallDate + TimeUnit.MINUTES.toMillis(30);

        for (App app : new ArrayList<>(result)) {
            if (app.installDate < systemInstallDate) {
                result.remove(app); //remove also some system apps
            }
        }

        return result;
    }

    private App resolveInfoToApp(ResolveInfo resolveInfo) {
        return new App(resolveInfo.activityInfo.name, resolveInfo.activityInfo.packageName,
                null, null, getInstallDate(resolveInfo.activityInfo.packageName));
    }

    private long getInstallDate(String packageName) {
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
