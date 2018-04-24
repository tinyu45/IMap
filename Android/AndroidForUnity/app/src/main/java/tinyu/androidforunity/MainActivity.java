package tinyu.androidforunity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.unity3d.player.UnityPlayerActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void StartMap(View v)
    {
        Intent it=new Intent(this, IMap.class);  //意图传递
        it.putExtra("success", "恭喜你，进入地图了。。。");
        startActivity(it);
    }


}
