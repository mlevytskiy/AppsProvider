package wumf.com.sample;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.List;

import wumf.com.appsprovider.App;
import wumf.com.sample.eventbus.ChangeAppsEvent;

public class MainActivity extends Activity {

    private TextView[] textViews = new TextView[6];
    private ImageView[] imageViews = new ImageView[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViews[0] = (TextView) findViewById(R.id.t1);
        textViews[1] = (TextView) findViewById(R.id.t2);
        textViews[2] = (TextView) findViewById(R.id.t3);
        textViews[3] = (TextView) findViewById(R.id.t4);
        textViews[4] = (TextView) findViewById(R.id.t5);
        textViews[5] = (TextView) findViewById(R.id.t6);

        imageViews[0] = (ImageView) findViewById(R.id.i1);
        imageViews[1] = (ImageView) findViewById(R.id.i2);
        imageViews[2] = (ImageView) findViewById(R.id.i3);
        imageViews[3] = (ImageView) findViewById(R.id.i4);
        imageViews[4] = (ImageView) findViewById(R.id.i5);
        imageViews[5] = (ImageView) findViewById(R.id.i6);



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
            App app = apps.get(i);
            if (TextUtils.isEmpty(app.name)) {
                textViews[i].setText(app.appPackage);
            } else {
                textViews[i].setText(app.name);
                imageViews[i].setImageURI(Uri.fromFile(new File(app.icon)));
            }
        }
    }

}
