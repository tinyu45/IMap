package tinyu.androidforunity;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.baidu.mapapi.SDKInitializer;

public class DemoApplication extends Application {
    private static DemoApplication minstance=null;

    @Override
    public void onCreate() {
        super.onCreate();
        minstance=this;
        SDKInitializer.initialize(getApplicationContext());
    }

    public static DemoApplication getInstance(){
        return minstance;
    }
}
