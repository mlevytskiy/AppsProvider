package wumf.com.appsprovider;

/**
 * Created by max on 02.09.16.
 */
public class App {

    public final String id;
    public final String appPackage;
    public final String name;
    public final String icon;
    public final long installDate;

    public App(String id, String appPackage, String name, String icon, long installDate) {
        this.id = id;
        this.appPackage = appPackage;
        this.name = name;
        this.icon = icon;
        this.installDate = installDate;
    }

}
