package tinyu.androidforunity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Unity extends UnityPlayerActivity {
    public LocationClient mloc=null;
    private StringBuilder stu=new StringBuilder();
    private BDLocation local;
    private HashMap<String, LatLng> sugMap=new HashMap<String, LatLng>();     //建议搜索
    private SuggestionSearch sugsearch=SuggestionSearch.newInstance();
    List<Poi> poiList=null;                                                         //周边POI
    private String _objName;
    private String _funcStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();   //初始 定位 搜索
    }


    void init()
    {
        /****定位相关*************************************************/
        mloc = new LocationClient(getApplicationContext());
        mloc.registerLocationListener(new ILocationListener());
        //开启定位
        LocationClientOption option = new  LocationClientOption();
        option.setCoorType("bd09ll");   //BD09LL
        option.setOpenGps(true);
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);              //获取地址信息
        option.setIsNeedLocationDescribe(true);     //获取位置描述
        option.setIsNeedLocationPoiList(true);      //获取周边POL

        mloc.setLocOption(option);
        mloc.start();

        /******热词建议检索*************/
        sugsearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult res ){
                if(res==null || res.getAllSuggestions()==null)
                {
                    Toast.makeText(getApplication(), "未发现相关建议结果", Toast.LENGTH_LONG).show();
                }
                List<SuggestionResult.SuggestionInfo> suglist=res.getAllSuggestions();
                sugMap.clear();
                StringBuilder sb=new StringBuilder();
                for(int i=0; i<suglist.size(); i++)
                {
                    sugMap.put(suglist.get(i).key, suglist.get(i).pt);
                    if(sb.length()==0)
                        sb.append(suglist.get(i).key);
                    else
                        sb.append("|"+suglist.get(i).key);
                }
                UnityPlayer.UnitySendMessage(_objName, _funcStr, sb.toString());
            }
        });

    }


    /***
     * 定位监听器
     */
    public class ILocationListener extends BDAbstractLocationListener {

        public void onReceiveLocation(BDLocation location) {
            //获取位置信息
            local=location;
            //获取地址---
            /****
             String addr = location.getAddrStr();    //获取详细地址信息
             String country = location.getCountry();    //获取国家
             String province = location.getProvince();    //获取省份
             String city = location.getCity();    //获取城市
             String district = location.getDistrict();    //获取区县
             String street = location.getStreet();    //获取街道信息
             ***/

            //获取位置描述
            String locationDescribe = location.getLocationDescribe();    //获取位置描述信息
            stu.append(locationDescribe);
            poiList = location.getPoiList();  //获取附近pol
        }
    }


    /************
     *
     * Unity 调用相关接口函数
     * @param _title
     * @param _content
     * @return
     */
    public String ShowDialog(final String _title, final String _content){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Unity.this);
                builder.setTitle(_title).setMessage(_content).setPositiveButton("Down", null);
                builder.show();
            }
        });

        return "Java return";
    }

    // 定义一个显示Toast的方法，在Unity中调用此方法
    public void ShowToast(final String mStr2Show){
        // 同样需要在UI线程下执行
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),mStr2Show, Toast.LENGTH_LONG).show();
            }
        });
    }


    //  定义一个手机振动的方法，在Unity中调用此方法
    public void SetVibrator(){
        Vibrator mVibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        mVibrator.vibrate(new long[]{200, 2000, 2000, 200, 200, 200}, -1); //-1：表示不重复 0：循环的震动
    }

    // 第一个参数是unity中的对象名字，记住是对象名字，不是脚本类名
    // 第二个参数是函数名
    // 第三个参数是传给函数的参数，目前只看到一个参数，并且是string的，自己传进去转吧
    public void callUnityFunc(String _objName , String _funcStr, String _content)
    {
        UnityPlayer.UnitySendMessage(_objName, _funcStr, "Come from:" + _content);
    }


    public void callgetLocation(String _objName , String _funcStr)
    {
        String loc;
        if(stu.length()!=0)
        {
            mloc.stop();
            loc=stu.toString();
        }
        else
            loc= "暂无位置信息";
        UnityPlayer.UnitySendMessage(_objName, _funcStr, loc);
    }

    /***
     * UNity跳转至Android
     * Android视图间数据传递
     * http://www.jb51.net/article/108903.htm
     */
    public void ViewToMap() {
        Intent it = new Intent(Unity.this, IMap.class); //数据传递  意图

        if (local != null) {
            it.putExtra("Radius", local.getRadius());
            it.putExtra("Latitude", local.getLatitude());
            it.putExtra("Longitude", local.getLongitude());
        }
        Unity.this.startActivity(it);
    }


    /***
     * 建议搜索
     */
    public void SugSearch(String _objName , String _funcStr, String text)
    {
        String cityName=local==null ? "西安" : local.getCity();
        sugsearch.requestSuggestion(new SuggestionSearchOption()
                .keyword(text)
                .city(cityName));

        this._objName=_objName;
        this._funcStr=_funcStr;
    }

    /***
     * 搜索地点
     */
    public Boolean SearchAndShow(String _objName , String _funcStr, String text)
    {
        if(!sugMap.containsKey(text))
            return false;

        LatLng la=sugMap.get(text);
        Intent it = new Intent(Unity.this, IMap.class);
        it.putExtra("Radius", 0);
        it.putExtra("Latitude", la.latitude);
        it.putExtra("Longitude", la.longitude);
        return true;
    }


    /***
     * 周边POL
     * @param _objName
     * @param _funcStr
     */
    public void callgetPOI(String _objName , String _funcStr)
    {
        StringBuilder PoiMsg=new StringBuilder();
        if(poiList==null || poiList.size()==0)
            PoiMsg.append("暂无POI信息");
        else
        {
            for(Poi info : poiList)
            {
                PoiMsg.append("|"+info.getName());
            }
            PoiMsg.delete(0,1);
        }
        UnityPlayer.UnitySendMessage(_objName, _funcStr, PoiMsg.toString());
    }

}
