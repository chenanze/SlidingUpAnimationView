package chenanze.com.slidingupanimationview;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private final String TAG = getClass().getSimpleName();

    private SlidingUpAnimationView mSlidingView;
    private int mScreenWidth;
    private int mScreenHeight;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        Log.d(TAG, "widthPixels: "+dm.widthPixels);
        Log.d(TAG, "heightPixels: "+dm.heightPixels);
//
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d(TAG, "after getWindowManager().getDefaultDisplay().getMetrics(dm); ");
        Log.d(TAG, "widthPixels: "+metrics.widthPixels);
        Log.d(TAG, "heightPixels: "+metrics.heightPixels);
//        int navigationBarHight = 0;
//        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
//        if (!hasHomeKey) { //判断没有HOME键时存在导航栏
//            Resources resources = this.getResources();
//            navigationBarHight = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
//        }

//        mSlidingView = (SlidingUpAnimationView) findViewById(R.id.sliding_view);
        mListView = (ListView) findViewById(R.id.test_lv);
//        mSlidingView.setInitPoint(mScreenWidth, mScreenHeight ,navigationBarHight);
        List<Map<String, String>> datas = new ArrayList<>();
        List<String> titles = new ArrayList<String>() {{
            add("test1");
            add("test2");
            add("test3");
            add("test4");
            add("test5");
            add("test6");
            add("test7");
            add("test8");
            add("test9");
            add("test10");
            add("test11");
            add("test12");
            add("test13");
            add("test14");
            add("test15");
            add("test16");
            add("test17");
            add("test17");
            add("test17");
            add("test17");
            add("test17");
            add("test17");
        }};
        for (String title : titles) {
            Map<String, String> map = new HashMap<>();
            map.put("title", title);
            datas.add(map);
        }
        String[] from = {"title"};
        int[] to = {R.id.title_tv};
        mListView.setAdapter(new SimpleAdapter(this, datas, R.layout.list_item, from, to));
    }


}
