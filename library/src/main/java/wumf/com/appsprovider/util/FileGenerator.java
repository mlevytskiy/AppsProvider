package wumf.com.appsprovider.util;

import android.content.Context;
import android.content.pm.ResolveInfo;

import java.io.File;

/**
 * Created by max on 30.09.15.
 */
public class FileGenerator {

    private Context context;

    public FileGenerator(Context context) {
        this.context = context;
    }

    public File generate(ResolveInfo resolveInfo) {
        return new File(context.getFilesDir(), resolveInfo.activityInfo.name);
    }

}
