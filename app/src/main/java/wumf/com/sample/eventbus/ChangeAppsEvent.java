package wumf.com.sample.eventbus;

import java.util.List;

import wumf.com.appsprovider.App;

/**
 * Created by max on 02.09.16.
 */
public class ChangeAppsEvent {

    public final List<App> apps;

    public ChangeAppsEvent(List<App> apps) {
        this.apps = apps;
    }

}
