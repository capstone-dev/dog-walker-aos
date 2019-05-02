package ajou.ac.kr.teaming.activity.search;

import android.Manifest;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapView;

import ajou.ac.kr.teaming.R;

public class SearchMainActivity extends AppCompatActivity {

    private final String TAG = "SearchMainActivity";
    private final String TMAP_API_KEY = "78f4044b-3ca4-439d-8d0e-10135941f054";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);

        LinearLayout mapView = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(TMAP_API_KEY);
        mapView.addView(tMapView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            Log.d("locationTest","동의알림");
        }

//        tMapTapi.setSKTMapAuthentication(TMAP_API_KEY);
//        tMapTapi.setOnAuthenticationListener(new TMapTapi.OnAuthenticationListenerCallback() {
//            @Override
//            public void SKTMapApikeySucceed() {
//                Log.i(TAG, "성공");
//                tMapTapi.invokeTmap();
//            }
//            @Override
//            public void SKTMapApikeyFailed(String errorMsg) {
//                Log.e(TAG, "실패: " + errorMsg);
//            }
//        });
    }
}
