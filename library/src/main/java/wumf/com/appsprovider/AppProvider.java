package wumf.com.appsprovider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import wumf.com.appsprovider.util.FileGenerator;
import wumf.com.appsprovider.util.SaveIconUtils;

/**
 * Created by max on 02.09.16.
 */
public class AppProvider {

    public final static AppProvider instance = new AppProvider();

    private OnChangeLastInstalledAppsListener listener;
    private Context context;
    private PackageManager pm;
    private String myAppPN;

    private SaveIconUtils saveIconUtils;
    private FileGenerator fileGenerator;

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
        saveIconUtils = new SaveIconUtils(context);
        fileGenerator = new FileGenerator(context);
        return this;
    }

    public AppProvider initBaseInfo() {
        //load all apps without label and icon
        List<ResolveInfo> resolveInfos = getResolveInfos();
        Map<String, ResolveInfo> map = new HashMap<>();
        List<App> apps = resolveInfoToApp(resolveInfos, map);
        List<App> limitedApps = new ArrayList<>();
        for (int i = apps.size()-1; i >= apps.size()-1-listener.appsCount; i--) {
            limitedApps.add(apps.get(i));
        }
        listener.setMap(map);
        listener.change(limitedApps);
        return this;
    }

    public AppProvider initFullAppInfo(final List<App> apps, final Runnable finishInit) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                for (App app : apps) {
                    ResolveInfo ri = listener.getMap().get(app.appPackage);
                    app.name = ri.loadLabel(pm).toString();
                    app.icon = loadAndSaveIconInFile(pm, ri);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finishInit.run();
            }
        }.execute();

        return this;
    }

    private List<ResolveInfo> getResolveInfos() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<android.content.pm.ResolveInfo> appList = pm.queryIntentActivities(mainIntent, PackageManager.GET_META_DATA);
        return appList;
    }

    private List<App> resolveInfoToApp(List<ResolveInfo> list, Map<String, ResolveInfo> map) {
        List<App> result = new ArrayList<>();
        long systemInstallDate = -1;
        int i = 0;
        for (ResolveInfo resolveInfo : list) {
            if (TextUtils.equals(myAppPN, resolveInfo.activityInfo.packageName)) {
                continue; //skip my app
            } else {
                map.put(resolveInfo.activityInfo.packageName, resolveInfo);
                result.add(resolveInfoToApp(resolveInfo));
            }
        }

        systemInstallDate = systemInstallDate + TimeUnit.MINUTES.toMillis(30);

        for (App app : new ArrayList<>(result)) {
            if (app.installDate < systemInstallDate) {
                result.remove(app); //remove also some system apps
            }
        }

        Collections.sort(result, new Comparator<App>() {
            @Override
            public int compare(App app1, App app2) {
                if (app1.installDate == app2.installDate) {
                    return 0;
                }

                if (app1.installDate > app2.installDate) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

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

    private String loadAndSaveIconInFile(PackageManager pm, ResolveInfo resolveInfo) {
        Drawable drawable = resolveInfo.loadIcon(pm);
        File file = fileGenerator.generate(resolveInfo);
        saveIconUtils.saveInFile(file, drawable);
        return file.getAbsolutePath();
    }

}
