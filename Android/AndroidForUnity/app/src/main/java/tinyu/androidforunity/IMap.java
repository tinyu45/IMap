package tinyu.androidforunity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class IMap extends Activity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_imap);
        mMapView=(MapView)findViewById(R.id.bmapView);
        mBaiduMap=mMapView.getMap();

        mBaiduMap.setMyLocationEnabled(true);

        // 获取意图对象
        Intent intent = getIntent();
        //获取传递的值
        if(intent.hasExtra("Radius") && intent.hasExtra("Latitude") && intent.hasExtra("Longitude") )
        {
            float rad = intent.getFloatExtra("Radius", 0);
            double lat = intent.getDoubleExtra("Latitude", 0);
            double lon=intent.getDoubleExtra("Longitude", 0);

            MapStatus.Builder builder=new MapStatus.Builder();
            builder.target(new LatLng(lat, lon)).zoom(15.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(rad)  // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(lat)
                    .longitude(lon)
                    .build();
            mBaiduMap.setMyLocationData(locData);
        }

        /**
        Intent intent=getIntent();
        Toast.makeText(getApplication(), intent.getStringExtra("success"), Toast.LENGTH_LONG).show();
         **/



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
