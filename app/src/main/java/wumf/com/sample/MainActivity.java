package wumf.com.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.sample.eventbus.ChangeAppsEvent;

public class MainActivity extends Activity {

    private TextView[] array = new TextView[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        array[0] = (TextView) findViewById(R.id.t1);
        array[1] = (TextView) findViewById(R.id.t2);
        array[2] = (TextView) findViewById(R.id.t3);
        array[3] = (TextView) findViewById(R.id.t4);
        array[4] = (TextView) findViewById(R.id.t5);
        array[5] = (TextView) findViewById(R.id.t6);

        MainApplication application = (MainApplication) getApplication();
        if (application.appsList != null) {
            fill(application.appsList);
        }
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ChangeAppsEvent changeAppsEvent) {
        fill(changeAppsEvent.apps);
        Toast.makeText(this, "changeAppsEvent", Toast.LENGTH_LONG).show();
    }

    private void fill(List<App> apps) {
        for (int i = 0; i < 6; i++) {
            array[i].setText(apps.get(i).appPackage);
        }
    }

}
