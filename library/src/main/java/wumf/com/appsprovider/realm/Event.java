package wumf.com.appsprovider.realm;

import io.realm.RealmObject;

/**
 * Created by max on 02.09.16.
 */
public class Event extends RealmObject {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
